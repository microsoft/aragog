package org.msr.mnr.verification;

import java.util.*;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.msr.mnr.verification.dsfa.ConstraintTreeNode;
import org.msr.mnr.verification.dsfa.DSFA;
import org.msr.mnr.verification.expressions.BoolExpr;
import org.msr.mnr.verification.expressions.ArithExpr;
import org.msr.mnr.verification.expressions.Expr;
import org.msr.mnr.verification.utils.*;

// Types: Key, Stream Type, Output Type
public class GlobalSFAProcessor<T> extends KeyedProcessFunction<T, Packet, Notification> {
    private static final long serialVersionUID = 1L;
    private static final int MAX_RTT = 0; // TODO: update this

    private BoolExpr filterExpr;
    private HashMap<String, Object> mapFunction;

    private DSFA dsfa;
    private transient ValueState<List<ConstraintTreeNode>> constraintTreeListState;
    private transient ValueState<Integer> currentStateState;
    private transient ValueState<Long> lastTimeState;

    private transient ArrayList<int[]> constraintBuffer;
    
    // Reordering support
    private transient ValueState<PriorityQueue<Packet>> pendingPacketState;
    private transient ValueState<Long> maxTimeState;


    /**
     * Constructs a StateMachineProcessor from a JSON config.
     * 
     * @param config    The JSON config that specifies events and transitions
     * @param filter    An optional object that implements the FILTER command.
     *                  Can be null if the state machine does not filter events.
     * @param name      The .sm filename.  For debugging and notifications.
     */
    public GlobalSFAProcessor(DSFA dsfa, BoolExpr filterExpr, HashMap<String, Object> mapFunction) {
        this.dsfa = dsfa;
        // System.out.println(filterExpr);
        this.filterExpr = filterExpr;
        this.mapFunction = mapFunction;
    }

    /**
     * Called on every packet (by Flink).  Filters, reorders, and advances the state machine.
     * 
     * @param p     Input packet
     */
    @Override
    public void processElement(Packet p, Context ctx, Collector<Notification> out)
            throws Exception {
        // Apply filter. We need to do this inside this KeyedProcessFunction because
        // Filters remove keying. To guarantee packets are together, we apply the filter
        // manually.
        
        if (filterExpr != null && !filterExpr.evaluate(p)) {
            // System.out.println("filter blocks!!!!!!!!");
            return;
        }
        // System.out.println(p);
        // System.out.println("filter allows");

        for (HashMap.Entry<String, Object> entry : mapFunction.entrySet()) {
            String key = entry.getKey();
            int[] value;
            if (entry.getValue() instanceof Expr) {
                value = (int[]) ((Expr) entry.getValue()).evaluate(p);
            } else {
                value = (int[]) entry.getValue();
            }
            // System.out.println(key);
            // System.out.println(value);
            p.set(key, value);
        }

        // System.out.println("!!!!!!!!!!filter allows!!!!!!");
        // Reorder packets. We need to do this here, after the KeyBy function, as keying
        // causes shuffling, which reorders packets.
        PriorityQueue<Packet> pendingPackets = pendingPacketState.value();
        if (pendingPackets == null) {
            pendingPackets = new PriorityQueue<Packet>();
        }
        pendingPackets.add(p);
        Long maxTime = maxTimeState.value();
        if (maxTime == null) {
            maxTime = p.getTime();
            maxTimeState.update(maxTime);
        } else if (p.getTime() > maxTime) {
            maxTime = p.getTime();
            maxTimeState.update(maxTime);
        }

        Long lastTime = lastTimeState.value();
        if (lastTime == null) {
            lastTime = Long.valueOf(0);
        }

        if (dsfa.hasVariables()) {
            // System.out.println("in variables");
            // If this is a new run, make a fresh tree
            List<ConstraintTreeNode> constraintTreeList = constraintTreeListState.value();
            if (constraintTreeList == null) {
                constraintTreeList = new ArrayList<ConstraintTreeNode>();
                constraintTreeList.add(ConstraintTreeNode.makeFreshTree(dsfa.startState,
                        dsfa.locationList, dsfa.variableList));
            } else if (constraintTreeList.size() == 0) {
                constraintTreeList.add(ConstraintTreeNode.makeFreshTree(dsfa.startState,
                        dsfa.locationList, dsfa.variableList));
            }
            assert (!constraintTreeList.isEmpty());

            if (constraintBuffer == null) {
                constraintBuffer = new ArrayList<int[]>();
                for (int i = 0; i < dsfa.locationList.size() + dsfa.variableList.size(); ++i) {
                    constraintBuffer.add(null);
                }
            }

            // Actually advance the state machine to maxTime - MAX_RTT
            // Process packets in order from there
            Packet next = pendingPackets.peek();
            
            while (next != null && next.getTime() <= maxTime - MAX_RTT) {
                processElementTree(next, ctx, out, constraintTreeList);

                lastTime = p.getTime();
                pendingPackets.poll();
                next = pendingPackets.peek();
            }

            // Update state after getting through the batch
            constraintTreeListState.update(constraintTreeList);
        } else {
            // System.out.println("in single");
            Integer currentState = currentStateState.value();
            // System.out.print("currentState in processElement: ");
            // System.out.println(currentState);
            if (currentState == null) {
                currentState = dsfa.startState;
            }

            // Actually advance the state machine to maxTime - MAX_RTT
            // Process packets in order from there
            Packet next = pendingPackets.peek();
            while (next != null && next.getTime() <= maxTime - MAX_RTT) {
                currentState = processElementSingle(next, ctx, out, currentState);

                lastTime = p.getTime();
                pendingPackets.poll();
                next = pendingPackets.peek();
            }


            // Update state after getting through the batch
            currentStateState.update(currentState);
            // System.out.print("updated state to: ");
            // System.out.println(currentState);
        }

        pendingPacketState.update(pendingPackets);
        lastTimeState.update(lastTime);
    }

    private Integer processElementSingle(Packet p, Context ctx, Collector<Notification> out,
            Integer currentState) throws Exception {
        currentState = dsfa.advance(p, currentState);

        if (dsfa.checkInFinal(currentState)) {
            // System.out.print("DSFA name Single: ");
            // System.out.println(dsfa.name);
            out.collect(new Notification("AlertSingle in invariant: " + dsfa.name + " " + p.toString(), 0, p));
        }

        return currentState;
    }

    private void processElementTree(Packet p, Context ctx, Collector<Notification> out,
            List<ConstraintTreeNode> constraintTreeList) throws Exception {
        boolean inFinal = dsfa.advanceConstraintTreeAndCheckFinal(p, constraintBuffer,
                constraintTreeList);
        if (inFinal) {
            // System.out.print("DSFA name Tree: ");
            // System.out.println(dsfa.name);
            out.collect(new Notification("AlertTree in invariant: " + dsfa.name + " " + p.toString(), 0, p));
        }
    }

    @Override
    public void open(Configuration config) throws Exception {
        ValueStateDescriptor<List<ConstraintTreeNode>> constraintTreeDescriptor = new ValueStateDescriptor<>(
                "constraintTree", TypeInformation.of(new TypeHint<List<ConstraintTreeNode>>() {}));
        constraintTreeListState = getRuntimeContext().getState(constraintTreeDescriptor);

        ValueStateDescriptor<Integer> currentStateDescriptor = new ValueStateDescriptor<>(
                "currentState", Integer.class);
        currentStateState = getRuntimeContext().getState(currentStateDescriptor);

        ValueStateDescriptor<Long> lastTimeDescriptor = new ValueStateDescriptor<>(
                "lastTime", Long.class);
        lastTimeState = getRuntimeContext().getState(lastTimeDescriptor);

        ValueStateDescriptor<PriorityQueue<Packet>> queueDescriptor = new ValueStateDescriptor<>(
                "pendingPackets", new PriorityQueueSerializer());
        pendingPacketState = getRuntimeContext().getState(queueDescriptor);

        ValueStateDescriptor<Long> timeDescriptor = new ValueStateDescriptor<>("maxTime",
                Long.class);
        maxTimeState = getRuntimeContext().getState(timeDescriptor);
    }
}
