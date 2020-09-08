package org.msr.mnr.verification.dsfa;

import java.io.BufferedReader;
import java.io.File;
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
import org.msr.mnr.verification.LocalSFAProcessor;
import org.msr.mnr.verification.expressions.BoolExpr;
import org.msr.mnr.verification.expressions.ExprBuilder;
import org.msr.mnr.verification.utils.Packet;
import org.msr.verification.SimpleSMTLIBLexer;
import org.msr.verification.SimpleSMTLIBParser;
import org.msr.verification.SimpleSMTLIBParser.StartBoolContext;

public class LocalSFAParser {
    private static class Invariant implements Serializable {
        private static final long serialVersionUID = 4756722778520575310L;
        public BoolExpr negatedLocExpr;
        public BoolExpr filter;
        public ArrayList<String> groupByFields;
        public ArrayList<DSFA> dsfas;

        public Invariant() {
            dsfas = new ArrayList<DSFA>();
        }
    }

    private static HashMap<List<String>, KeyedStream<Packet, ?>> streams = new HashMap<List<String>, KeyedStream<Packet, ?>>();

    public static DataStream<Notification> makeDSFAProcessor(DataStream<Packet> parsedPackets,
            String name, ArrayList<File> files) throws IOException {
        Invariant inv = parseStateMachines(name, files);
        // System.out.print("Parsing complete: ");
        // System.out.println(inv.groupByFields.size());
        switch (inv.groupByFields.size()) {
        case 1:
            return LocalSFAParser.<Tuple1<Integer>>createAndAttachGroupBy(inv, parsedPackets, name,
                    TypeInformation.of(new TypeHint<Tuple1<Integer>>() {}));
        case 2:
            return LocalSFAParser.<Tuple2<Integer, Integer>>
                    createAndAttachGroupBy(inv, parsedPackets, name,
                            TypeInformation.of(new TypeHint<Tuple2<Integer, Integer>>(){}));
        case 3:
            return LocalSFAParser.<Tuple3<Integer, Integer, Integer>>
                    createAndAttachGroupBy(inv, parsedPackets, name,
                            TypeInformation.of(new TypeHint<Tuple3<Integer, Integer, Integer>>(){}));
        case 4:
            return LocalSFAParser.<Tuple4<Integer, Integer, Integer, Integer>>
                    createAndAttachGroupBy(inv, parsedPackets, name,
                            TypeInformation.of(new TypeHint<Tuple4<Integer, Integer, Integer, Integer>>(){}));
        case 5:
            return LocalSFAParser.<Tuple5<Integer, Integer, Integer, Integer, Integer>>
                    createAndAttachGroupBy(inv, parsedPackets, name,
                            TypeInformation.of(new TypeHint<Tuple5<Integer, Integer, Integer, Integer, Integer>>(){}));
        }

        throw new RuntimeException("Could not parse the state machine");
    }

    public static Invariant parseStateMachines(String name, ArrayList<File> files)
            throws IOException {
        Invariant completeInv = new Invariant();
        completeInv.dsfas = new ArrayList<>();
        for (File f : files) {
            try (FileReader reader = new FileReader(f)) {
                if (f.getName().endsWith(".sm.g")) {
                    LocalSFAParser.parseGlobalStateMachine(completeInv, f.getName(), reader);
                } else {
                    LocalSFAParser.parseLocalStateMachine(completeInv, f.getName(), reader);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return completeInv;
    }

    public static void parseGlobalStateMachine(Invariant inv, String name, FileReader input)
            throws IOException {
        System.out.println("Parsing state machine: " + name);
        BufferedReader bufin = new BufferedReader(input);

        SimpleSMTLIBLexer lexer;
        CommonTokenStream tokens;
        SimpleSMTLIBParser parser;
        StartBoolContext tree;

        // Line 1: Negative location filter
        String line = bufin.readLine();
        lexer = new SimpleSMTLIBLexer(CharStreams.fromString(line));
        tokens = new CommonTokenStream(lexer);
        parser = new SimpleSMTLIBParser(tokens);
        tree = parser.startBool();
        inv.negatedLocExpr = ExprBuilder.buildBoolExpr(tree, null, null);

        // Line 2: initial state. Skip for local
        bufin.readLine();

        // Line 3: final state(s). Skip for local
        bufin.readLine();

        // Line 4: filter
        line = bufin.readLine();
        if (line.equals("")) {
            // System.out.print("no filter");
            inv.filter = null;
        } else {
            // System.out.println("filterString: " + filterString);
            lexer = new SimpleSMTLIBLexer(CharStreams.fromString(line));
            tokens = new CommonTokenStream(lexer);
            parser = new SimpleSMTLIBParser(tokens);
            tree = parser.startBool();
            inv.filter = ExprBuilder.buildBoolExpr(tree, null, null);
        }

        // Line 5: groupby
        // Build the GROUPBY transformation
        // Due to the nature of Flink KeyedStreams, the executor that reads the KeyedStream (the
        // StateMachineProcessor) needs to be typed to take the exact tuple size.  Hence the
        // many case statements.
        inv.groupByFields = new ArrayList<String>();
        String[] toks = bufin.readLine().split(",");
        for (String s : toks)  {
            inv.groupByFields.add(s);
        }
        // get a sorted, immutable list of the GroupByFields.
        // We do this so that we can re-use keyedStreams.
        Collections.sort(inv.groupByFields);
    }

    public static void parseLocalStateMachine(Invariant inv, String name, FileReader input)
            throws IOException {
        System.out.println("Parsing state machine: " + name);
        BufferedReader bufin = new BufferedReader(input);

        // Line 1: initial state
        Integer startState = Integer.parseInt(bufin.readLine());

        // Line 2: final state(s)
        HashSet<Integer> finalStates = new HashSet<Integer>();
        String[] toks = bufin.readLine().split(",");
        for (String s : toks) {
            finalStates.add(Integer.parseInt(s));
        }

        // Line 3: variables
        ArrayList<String> variableList = new ArrayList<String>();
        String variableStr = bufin.readLine().trim();
        if (!variableStr.isEmpty()) {
            toks = variableStr.split(",");
            for (String s : toks) {
                variableList.add(s);
            }
        }
        // bufin.readLine();
        // bufin.readLine();
        // bufin.readLine();

        // Line 4+: transitions
        // suppress;from;to;guard
        HashMap<Integer, ArrayList<DSFAMove>> moves = new HashMap<Integer, ArrayList<DSFAMove>>();
        while (bufin.ready()) {
            toks = bufin.readLine().split("\\;");
            // System.out.println(Arrays.toString(toks));
            SimpleSMTLIBLexer lexer = new SimpleSMTLIBLexer(CharStreams.fromString(toks[3]));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SimpleSMTLIBParser parser = new SimpleSMTLIBParser(tokens);
            StartBoolContext tree = parser.startBool();

            HashSet<String> locationReferences = new HashSet<String>();
            HashMap<String, String> variableComparisons = new HashMap<String, String>();
            BoolExpr condition = ExprBuilder.buildBoolExpr(tree, locationReferences, variableComparisons);
            boolean supress = Boolean.parseBoolean(toks[0]);
            Integer from = new Integer(toks[1]);
            Integer to = new Integer(toks[2]);
            DSFAMove move = new DSFAMove(condition, supress, to, finalStates.contains(to),
                    locationReferences, variableComparisons);
            if (!moves.containsKey(from)) {
                moves.put(from, new ArrayList<DSFAMove>());
            }
            // System.out.print("move from: " );
            // System.out.println(from);
            moves.get(from).add(move);
        }
        inv.dsfas.add(new DSFA(name, startState, finalStates, moves, new ArrayList<String>(),
                variableList));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Tuple> DataStream<Notification> createAndAttachGroupBy(Invariant inv,
            DataStream<Packet> parsedPackets, String name, TypeInformation<T> ti) {
        // System.out.print("in createAndAttachGroupBy \n ");
        KeyedStream<Packet, T> ks;
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

        return ks.process(new LocalSFAProcessor<T>(inv.dsfas, inv.filter, inv.negatedLocExpr)).name(name);
    }
}
