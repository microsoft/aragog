package org.msr.mnr.verification;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
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
import org.msr.mnr.verification.utils.*;

// Types: Key, Stream Type, Output Type
public class LocalSFAProcessor<T> extends KeyedProcessFunction<T, Packet, Notification> {
    private static final long serialVersionUID = 1L;
    private static final int MAX_RTT = 0; // TODO: update this

    private BoolExpr filterExpr;
    private BoolExpr negatedLocExpr;

    private ArrayList<DSFA> dsfas;
    private transient MapState<Integer, List<ConstraintTreeNode>> constraintTreeListMap;
    private transient MapState<Integer, Integer> currentStateMap;
    private transient ValueState<Long> lastTimeState;

    private transient ArrayList<int[]> constraintBuffer;

    // Reordering support
    private transient ValueState<PriorityQueue<Packet>> pendingPacketState;
    private transient ValueState<Long> maxTimeState;

    /**
     * Constructs a StateMachineProcessor from a JSON config.
     * 
     * @param config The JSON config that specifies events and transitions
     * @param filter An optional object that implements the FILTER command. Can be
     *               null if the state machine does not filter events.
     * @param name   The .sm filename. For debugging and notifications.
     */
    public LocalSFAProcessor(ArrayList<DSFA> dsfas, BoolExpr filterExpr, BoolExpr negatedLocExpr) {
        this.dsfas = dsfas;
        this.filterExpr = filterExpr;
        this.negatedLocExpr = negatedLocExpr;
    }

    /**
     * Called on every packet (by Flink). Filters, reorders, and advances the state
     * machine.
     * 
     * @param p Input packet
     */
    @Override
    public void processElement(Packet p, Context ctx, Collector<Notification> out)
            throws Exception {
        // Apply filter. We need to do this inside this KeyedProcessFunction because
        // Filters remove keying. To guarantee packets are together, we apply the filter
        // manually.
        if (filterExpr != null && !filterExpr.evaluate(p)) {
            System.out.println("filtered,1");
            return;
        }

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

        // Actually advance the state machine to maxTime - MAX_RTT
        // Process packets in order from there
        Packet next = pendingPackets.peek();

        // System.out.print("Tree list size in processElementTree: ");
        // System.out.println(constraintTreeList.size());
        while (next != null && next.getTime() <= maxTime - MAX_RTT) {
            boolean suppressible = true;
            if (dsfas.size() == 0) {
                suppressible = false;
            }
            for (int dsfaNum = 0; dsfaNum < dsfas.size(); dsfaNum++) {
                DSFA dsfa = dsfas.get(dsfaNum);
                if (dsfa.hasVariables()) {
                    suppressible &= processElementTree(next, ctx, out, dsfaNum);
                } else {
                    suppressible &= processElementSingle(next, ctx, out, dsfaNum, lastTime);
                }
            }

            suppressible &= negatedLocExpr.evaluate(next);
            if (!suppressible) {
                out.collect(new Notification("Forwarding to Global", 0, p));
            } else{
                System.out.println("suppressed,1");
            }

            lastTime = p.getTime();
            pendingPackets.poll();
            next = pendingPackets.peek();
        }

        pendingPacketState.update(pendingPackets);
        lastTimeState.update(lastTime);
    }

    private boolean processElementSingle(Packet p, Context ctx, Collector<Notification> out,
            Integer dsfaNum, Long lastTime) throws Exception {
        DSFA dsfa = dsfas.get(dsfaNum);
        Integer currentState = currentStateMap.get(dsfaNum);
        if (currentState == null) {
            currentState = dsfa.startState;
        }

        Pair<Integer, Boolean> res = dsfa.advanceAndCheckSuppress(p, currentState);

        currentStateMap.put(dsfaNum, res.getLeft());

        return res.getRight();
    }

    private boolean processElementTree(Packet p, Context ctx, Collector<Notification> out,
            Integer dsfaNum) throws Exception {
        DSFA dsfa = dsfas.get(dsfaNum);

        // If this is a new run, make a fresh tree
        List<ConstraintTreeNode> constraintTreeList = constraintTreeListMap.get(dsfaNum);
        if (constraintTreeList == null) {
            constraintTreeList = new ArrayList<ConstraintTreeNode>();
            constraintTreeList.add(ConstraintTreeNode.makeFreshTree(dsfa.startState,
                    dsfa.locationList, dsfa.variableList));
        }
        assert (!constraintTreeList.isEmpty());

        // Note: don't need to check dsfa.locationList.size() as we are in a local SFA
        if (constraintBuffer == null) {
            constraintBuffer = new ArrayList<int[]>();
            for (int i = 0; i < dsfa.variableList.size(); ++i) {
                constraintBuffer.add(null);
            }
        } else if (constraintBuffer.size() < dsfa.variableList.size()) {
            for (int i = constraintBuffer.size(); i < dsfa.variableList.size(); ++i) {
                constraintBuffer.add(null);
            }
        }

        boolean suppressible = dsfa.advanceConstraintTreeAndCheckSuppress(p, constraintBuffer,
                constraintTreeList);

        constraintTreeListMap.put(dsfaNum, constraintTreeList);
        return suppressible;
    }

    @Override
    public void open(Configuration config) throws Exception {
        MapStateDescriptor<Integer, List<ConstraintTreeNode>> constraintTreeDescriptor = new MapStateDescriptor<Integer, List<ConstraintTreeNode>>(
                "constraintTreeLists", TypeInformation.of(Integer.class),
                TypeInformation.of(new TypeHint<List<ConstraintTreeNode>>() {}));
        constraintTreeListMap = getRuntimeContext().getMapState(constraintTreeDescriptor);

        MapStateDescriptor<Integer, Integer> currentStatesDescriptor = new MapStateDescriptor<Integer, Integer>(
                "currentStates", Integer.class, Integer.class);
        currentStateMap = getRuntimeContext().getMapState(currentStatesDescriptor);

        ValueStateDescriptor<Long> lastTimeDescriptor = new ValueStateDescriptor<>("lastTime", Long.class);
        lastTimeState = getRuntimeContext().getState(lastTimeDescriptor);

        ValueStateDescriptor<PriorityQueue<Packet>> queueDescriptor = new ValueStateDescriptor<>(
                "pendingPackets", new PriorityQueueSerializer());
        pendingPacketState = getRuntimeContext().getState(queueDescriptor);

        ValueStateDescriptor<Long> timeDescriptor = new ValueStateDescriptor<>("maxTime",
                Long.class);
        maxTimeState = getRuntimeContext().getState(timeDescriptor);
    }
}