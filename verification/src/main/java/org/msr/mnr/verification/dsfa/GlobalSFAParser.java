package org.msr.mnr.verification.dsfa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.msr.mnr.verification.Notification;
import org.msr.mnr.verification.GlobalSFAProcessor;
import org.msr.mnr.verification.expressions.BoolExpr;
import org.msr.mnr.verification.expressions.ArithExpr;
import org.msr.mnr.verification.expressions.ExprBuilder;
import org.msr.mnr.verification.utils.Packet;
import org.msr.verification.SimpleSMTLIBLexer;
import org.msr.verification.SimpleSMTLIBParser;
import org.msr.verification.SimpleSMTLIBParser.StartArithContext;
import org.msr.verification.SimpleSMTLIBParser.StartBoolContext;

public class GlobalSFAParser {
    private static class Invariant implements Serializable {
        private static final long serialVersionUID = 4756722778520575310L;
        public ArrayList<String> groupByFields;
        public DSFA dsfa;
        public BoolExpr filter;
        public HashMap<String, Object> mapFunction;
        // public String name;
    }
    

    public static DataStream<Notification> makeDSFAProcessor(DataStream<Packet> parsedPackets,
            FileReader input, String name) throws IOException {
        Invariant inv = parseStateMachine(name, input);

        switch (inv.groupByFields.size()) {
        case 1:
            return GlobalSFAParser.<Tuple1<Integer>>
                    createAndAttachGroupBy(inv, parsedPackets, name,
                            TypeInformation.of(new TypeHint<Tuple1<Integer>>(){}));
        case 2:
            return GlobalSFAParser.<Tuple2<Integer, Integer>>
                    createAndAttachGroupBy(inv, parsedPackets, name,
                            TypeInformation.of(new TypeHint<Tuple2<Integer, Integer>>(){}));
        case 3:
            return GlobalSFAParser.<Tuple3<Integer, Integer, Integer>>
                    createAndAttachGroupBy(inv, parsedPackets, name,
                            TypeInformation.of(new TypeHint<Tuple3<Integer, Integer, Integer>>(){}));
        case 4:
            return GlobalSFAParser.<Tuple4<Integer, Integer, Integer, Integer>>
                    createAndAttachGroupBy(inv, parsedPackets, name,
                            TypeInformation.of(new TypeHint<Tuple4<Integer, Integer, Integer, Integer>>(){}));
        case 5:
            return GlobalSFAParser.<Tuple5<Integer, Integer, Integer, Integer, Integer>>
                    createAndAttachGroupBy(inv, parsedPackets, name,
                            TypeInformation.of(new TypeHint<Tuple5<Integer, Integer, Integer, Integer, Integer>>(){}));
        }

        throw new RuntimeException("Could not parse the state machine");
    }

    public static Invariant parseStateMachine(String name, FileReader input) throws IOException {
        System.out.println("Parsing state machine: " + name);
        BufferedReader bufin = new BufferedReader(input);
        Invariant ret = new Invariant();

        // Line 1: Negative location filter. Ignore for global
        bufin.readLine();

        // Line 2: initial state
        Integer startState = Integer.parseInt(bufin.readLine());

        // Line 3: final state(s)
        HashSet<Integer> finalStates = new HashSet<Integer>();
        String[] toks = bufin.readLine().split(",");
        for (String s : toks)  {
            // System.out.print("Adding to final: ");
            // System.out.println(s);
            finalStates.add(Integer.parseInt(s));
        }

        // Line 4: filter
        // For globals, there shouldn't be a filter
        String filterString =  bufin.readLine();
        SimpleSMTLIBLexer lexer;
        CommonTokenStream tokens;
        SimpleSMTLIBParser parser;
        StartBoolContext tree;
        
        if ("".equals(filterString)) {
            // System.out.print("no filter");
            ret.filter = null;
        } else {
            // System.out.println("filterString: " + filterString);
            lexer = new SimpleSMTLIBLexer(CharStreams.fromString(filterString));
            tokens = new CommonTokenStream(lexer);
            parser = new SimpleSMTLIBParser(tokens);
            tree = parser.startBool();
            ret.filter = ExprBuilder.buildBoolExpr(tree, null, null);
        }
        // System.out.println(ret.filter);

        // Line 5: groupby
        // Build the GROUPBY transformation
        // Due to the nature of Flink KeyedStreams, the executor that reads the KeyedStream (the
        // StateMachineProcessor) needs to be typed to take the exact tuple size.  Hence the
        // many case statements.
        ret.groupByFields = new ArrayList<String>();
        toks = bufin.readLine().split(",");
        for (String s : toks)  {
            ret.groupByFields.add(s);
        }
        // get a sorted, immutable list of the GroupByFields.
        // We do this so that we can re-use keyedStreams.
        Collections.sort(ret.groupByFields);

        // Line 6: locations
        ArrayList<String> locationList = new ArrayList<String>();
        String locationStr = bufin.readLine().trim();
        if (!locationStr.isEmpty()) {
            toks = locationStr.split(",");
            for (String s : toks)  {
                locationList.add(s);
            }
        }

        // Line 7: variables
        ArrayList<String> variableList = new ArrayList<String>();
        String variableStr = bufin.readLine().trim();
        if (!variableStr.isEmpty()) {
            toks = variableStr.split(",");
            for (String s : toks) {
                variableList.add(s);
            }
        }

        int numberOfMaps = Integer.parseInt(bufin.readLine());
        ret.mapFunction = new HashMap<>();
        for (int i = 0; i < numberOfMaps; i++) {
            lexer = new SimpleSMTLIBLexer(CharStreams.fromString(bufin.readLine()));
            tokens = new CommonTokenStream(lexer);
            parser = new SimpleSMTLIBParser(tokens);
            StartArithContext mapTree = parser.startArith();
            // TODO: Pass fields here
            Object mapFunc = ExprBuilder.buildArithExpr(mapTree.arithExpression());
            // System.out.println(mapFunc);
            ret.mapFunction.put(bufin.readLine(), mapFunc);
        }

        // Line 8+: transitions
        // supress$from$to$guard
        HashMap<Integer, ArrayList<DSFAMove>> moves = new HashMap<Integer, ArrayList<DSFAMove>>();
        while (bufin.ready()) {
            toks = bufin.readLine().split("\\;");
            // System.out.println(Arrays.toString(toks));
            boolean suppress = false;
            Integer from = new Integer(toks[1]);
            Integer to = new Integer(toks[2]);

            lexer = new SimpleSMTLIBLexer(CharStreams.fromString(toks[3]));
            tokens = new CommonTokenStream(lexer);
            parser = new SimpleSMTLIBParser(tokens);
            tree = parser.startBool();

            HashSet<String> locationReferences = new HashSet<String>();
            HashMap<String, String> variableComparisons = new HashMap<String, String>();
            BoolExpr condition = ExprBuilder.buildBoolExpr(tree, locationReferences, variableComparisons);
            DSFAMove move = new DSFAMove(condition, suppress, to, finalStates.contains(to),
                    locationReferences, variableComparisons);
            if (!moves.containsKey(from)) {
                moves.put(from, new ArrayList<DSFAMove>());
            }
            moves.get(from).add(move);
        }
        // System.out.print("DSFA name: ");
        // System.out.println(name);
        ret.dsfa = new DSFA(name, startState, finalStates, moves, locationList, variableList);
        return ret;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Tuple> DataStream<Notification> createAndAttachGroupBy(Invariant inv,
            DataStream<Packet> parsedPackets, String name, TypeInformation<T> ti) {
        KeyedStream<Packet, T> ks;

        HashMap<List<String>, KeyedStream<Packet, ?>> streams =
            new HashMap<List<String>, KeyedStream<Packet, ?>>();
            
        if (streams.containsKey(inv.groupByFields)) {
            ks = (KeyedStream<Packet, T>) streams.get(inv.groupByFields);
        } else {
            ks = parsedPackets.keyBy(new KeySelector<Packet, T>() {
                private static final long serialVersionUID = 1L;

                @Override
                public T getKey(Packet p) throws Exception {
                    T tuple = (T) Tuple.newInstance(inv.groupByFields.size());
                    for (int i = 0; i < inv.groupByFields.size(); ++i) {
                        int[] value = p.get(inv.groupByFields.get(i));
                        int hashCode = 0;
                        for (int j = 0; j < value.length; ++j) {
                            hashCode = (int) (31 * hashCode + (value[j] & 0xffffffffL));
                        }
                        tuple.setField(hashCode, i);
                    }

                    return tuple;
                }
            }, ti);
            streams.put(inv.groupByFields, ks);
        }
        return ks.process(new GlobalSFAProcessor<T>(inv.dsfa, inv.filter, inv.mapFunction)).name(name);
    }

    public static void main(String[] args) throws Exception {
        FileReader input = new FileReader(args[0]);
        // GlobalSFAParser.parseStateMachine("main()", input);
    }
}
