package main.java.generateSFA;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Expr;

import automata.Move;
import automata.sfa.SFA;
import automata.sfa.SFAInputMove;
import generateSFA.InvariantLexer;
import generateSFA.InvariantParser;
import main.java.generateSFA.GenerateLocalSFA.LocalSFA;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;


// SFA libary: http://pages.cs.wisc.edu/~loris/symbolicautomata.html
public class GenerateSFA {
    
    public static void main(String[] args) throws Exception {
        ArgumentParser argparser = ArgumentParsers.newFor("GenerateSFA").build()
                .defaultHelp(true)
                .description("Generates the SFA");
        argparser.addArgument("--packet_format")
                .setDefault("../config/SLB/packetformat.json")
                .help("Packet format config file");
        argparser.addArgument("--invar_file")
                .setDefault("../config/SLB/primary_single.invar")
                .help("Input .invar file");
        argparser.addArgument("--debug")
                .action(Arguments.storeTrue())
                .help("Packet format config file");
        argparser.addArgument("--out_dir")
                .setDefault("../out/")
                .help("Output directory");
        argparser.addArgument("--examples")
                .action(Arguments.storeTrue())
                .help("Run examples only");
        Namespace ns = argparser.parseArgs(args);

        if (ns.getBoolean("examples")) {
            ExprExamples examples = new ExprExamples();
            examples.run_all_cases();
            System.exit(0);
        }

        // Create out directory if it does not already exist
        File out_dir = new File(ns.getString("out_dir"));
        if (!out_dir.exists()) {
            out_dir.mkdir();
        }

        // Get basename
        String basename = ns.getString("invar_file");
        int pos = basename.lastIndexOf("/");
        if (pos >= 0) {
            basename = basename.substring(pos + 1);
        }
        pos = basename.lastIndexOf(".");
        if (pos >= 0) {
            basename = basename.substring(0, pos);
        }
        System.out.println("Processing invariant: " + basename);

        // Read packet_format.json file
        JSONObject config = (JSONObject) new JSONParser()
                .parse(new FileReader(ns.getString("packet_format")));

        // Get constants from packet_format.json file
        HashMap<String, Integer> constantMap = null;
        try {
            constantMap = getConstants((JSONObject) config.get("constants"));
        } catch (NullPointerException e) {
            constantMap = new HashMap<>();
        }

        // Run lexer through file
        InvariantLexer lexer = new InvariantLexer(
                CharStreams.fromFileName(ns.getString("invar_file")));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Run parser through file
        InvariantParser parser = new InvariantParser(tokens);

        // Get the top level grammar. In our case, it is policy in InvariantParser.g4
        InvariantParser.PolicyContext tree = parser.policy();
        EventSolver solver = new EventSolver();
        InvariantVisitor visitor = new InvariantVisitor(solver, constantMap);
        SFA<BoolExpr, HashMap<String, Integer>> sfa = visitor.visit(tree);

        // Output global .sfa.dot
        System.out.println("Outputting SFA...");
        if (ns.getBoolean("debug")) {
            System.out.println(sfa);
        }
        sfa.createDotFile(basename + ".sfa", ns.getString("out_dir"));

        // Output global .dsfa.dot
        System.out.println("Outputting Global DSFA...");
        sfa = sfa.determinize(solver).minimize(solver);
        simplifySFA(sfa);
        BoolExpr negatedLocationExpr = calculateSupressionGlobal(sfa, solver);
        negatedLocationExpr = simplifyNegFilter(negatedLocationExpr, visitor.variableMap, solver);
        if (ns.getBoolean("debug")) {
            System.out.println(sfa);
        }
        sfa.createDotFile(basename + ".dsfa", ns.getString("out_dir"));

        // Output global .sm.g
        System.out.println("Serializing Global DSFA...");
        FileWriter fw = new FileWriter(ns.getString("out_dir") + basename + ".sm.g");
        printDSFA(sfa, negatedLocationExpr, visitor.filter, visitor.groupBy, visitor.locationSet,
                visitor.variableMap.keySet(), visitor.functionMap, fw);
        fw.close();

        // Output local .sm.[0-9]+
        System.out.println("Serializing Local DSFAs...");
        ArrayList<LocalSFA> localSFAs = GenerateLocalSFA.generateLocalSFAs(sfa,
                solver.getLocations(), solver, visitor.filter);
        int counter = 0;
        // System.out.println(localSFAs.size());
        for (LocalSFA l : localSFAs) {
            simplifySFA(l.lsfa);
            System.out.println("-------------------------------");
            setSupressionLocal(l.lsfa, l.stateMapping, l.locExpr, sfa, solver, l.opposite);
            if (ns.getBoolean("debug")) {
                System.out.println(l.lsfa);
            }
            l.lsfa = l.lsfa.determinize(l.stateMapping, solver);
            simplifySFA(l.lsfa);
            fw = new FileWriter(ns.getString("out_dir") + basename + ".sm." + counter++);
            printLocalDSFA(l.lsfa, visitor.variableMap.keySet(), fw);
            fw.close();
        }
    }
    
    private static <B> void simplifySFA(SFA<BoolExpr, B> sfa) {
        for (Move<BoolExpr, B> m : sfa.getMoves()) {
            try {
                SFAInputMove<BoolExpr, B> im = (SFAInputMove<BoolExpr, B>) m;
                im.guard = (BoolExpr) im.guard.simplify();
            } catch (ClassCastException e) {
                continue;
            }
            // SFAMove<BoolExpr, B> im = (SFAMove<BoolExpr, B>) m;
            
        }
    }

    private static void setSupressionLocal(
            SFA<BoolExpr, HashMap<String, Integer>> localSFA,
            HashMap<Integer, Collection<Integer>> stateMapping,
            IntExpr locExpr,
            SFA<BoolExpr, HashMap<String, Integer>> globalSFA, EventSolver solver,
            boolean opposite) {
        for (Move<BoolExpr, HashMap<String, Integer>> m : localSFA.getMoves()) {
            SFAInputMove<BoolExpr, ?> im;
            try {
                im = (SFAInputMove<BoolExpr, HashMap<String, Integer>>) m;
            } catch (ClassCastException e) {
                continue;
            }

            im.suppressible = true;

            // System.out.println(im.guard);
            // Why are we just looking at im.from in globalSFA? There is no one-to-one mapping.
            for (SFAInputMove<BoolExpr, ?> originalMove : globalSFA.getInputMovesFrom(im.from)) {
                if (originalMove.suppressible) {
                    continue;
                }

                BoolExpr locSpecialization = solver.MkEq(solver.getLocationExpr(), locExpr);
                // if (opposite) {
                //     locSpecialization = solver.MkNot(locSpecialization);
                //     System.out.print("local guard: ");
                //     System.out.println(im.guard);
                //     System.out.print("original guard: ");
                //     System.out.println(originalMove.guard);
                // }
                BoolExpr test = solver.MkAnd(solver.MkAnd(im.guard, originalMove.guard),
                        locSpecialization);
                if (solver.IsSatisfiable(test)) {
                    im.suppressible = false;
                    // System.out.println("setting to false");
                    break;
                } 
                // else {
                //     System.out.println("letting it remain true");
                // }
            }
        }
    }

    private static BoolExpr calculateSupressionGlobal(SFA<BoolExpr, HashMap<String, Integer>> sfa, EventSolver solver) {
        // Build the equivalence sets
        // If two states have equivalent exit transitions they are "equivalent"
        HashMap<Integer, HashSet<Integer>> equivalenceSets = new HashMap<Integer, HashSet<Integer>>();
        Object[] states = sfa.getStates().toArray();
        for (int i = 0; i < states.length; ++i) {
            Integer state1 = (Integer) states[i];
            Collection<SFAInputMove<BoolExpr, HashMap<String, Integer>>> moves1 = sfa.getInputMovesFrom(state1);

            for (int j = i + 1; j < states.length; ++j) {
                Integer state2 = (Integer) states[j];
                Collection<SFAInputMove<BoolExpr, HashMap<String, Integer>>> moves2 = sfa.getInputMovesFrom(state2);

                // the same state is trivially equivalent
                if (state1.equals(state2)) {
                    if (!equivalenceSets.containsKey(state1)) {
                        equivalenceSets.put(state1, new HashSet<Integer>());
                    }
                    equivalenceSets.get(state1).add(state2);
                    if (!equivalenceSets.containsKey(state2)) {
                        equivalenceSets.put(state2, new HashSet<Integer>());
                    }
                    equivalenceSets.get(state2).add(state1);
                }

                // different move set sizes are not equivalent
                if (moves1.size() != moves2.size()) {
                    continue;
                }

                // Otherwise, need to check each transition
                boolean equivalent = true;
                for (SFAInputMove<BoolExpr, HashMap<String, Integer>> m1 : moves1) {
                    boolean foundEquivalent = false;
                    for (SFAInputMove<BoolExpr, HashMap<String, Integer>> m2 : moves2) {
                        if (!m1.to.equals(m2.to)) {
                            continue;
                        }

                        if (solver.IsUnSAT(solver.MkNot(solver.MkIff(m1.guard, m2.guard)))) {
                            // they must be equivalent!
                            foundEquivalent = true;
                            break;
                        }
                    }
                    
                    if (!foundEquivalent) {
                        equivalent = false;
                        break;
                    }
                }
                
                if (equivalent) {
                    if (!equivalenceSets.containsKey(state1)) {
                        equivalenceSets.put(state1, new HashSet<Integer>());
                    }
                    equivalenceSets.get(state1).add(state2);
                    if (!equivalenceSets.containsKey(state2)) {
                        equivalenceSets.put(state2, new HashSet<Integer>());
                    }
                    equivalenceSets.get(state2).add(state1);
                }
            }
        }   

        // System.out.print("NOFEL: equal states");
        // System.out.println(equivalenceSets);

        // Set supression flags on everything
        for (Move<BoolExpr, HashMap<String, Integer>> m : sfa.getMoves()) {
            SFAInputMove<BoolExpr, ?> im = (SFAInputMove<BoolExpr, HashMap<String, Integer>>) m;

            // If destination is accepting => NOT suppressible
            if (sfa.isFinalState(im.to)) {
                continue;
            }

            // If self loop => suppressible
            if (im.from.equals(im.to)) {
                im.suppressible = true;
                continue;
            }

            // transitions between equivalent states => suppressible
            if (equivalenceSets.containsKey(im.from)
                    && equivalenceSets.get(im.from).contains(im.to)) {
                im.suppressible = true;
            }
        }

        // Finally, get the suppression BoolExpr for the negated location
        BoolExpr ret = solver.False();
        for (Move<BoolExpr, HashMap<String, Integer>> m : sfa.getMoves()) {
            SFAInputMove<BoolExpr, ?> im = (SFAInputMove<BoolExpr, HashMap<String, Integer>>) m;
            if (!im.suppressible) {
                ret = solver.MkOr(ret, im.guard);
            }
        }

        return (BoolExpr) solver.specifyLocation(null, ret, false).simplify();
    }

    private static BoolExpr simplifyNegFilter(BoolExpr filter, HashMap<String, HashSet<VarValues>> variableMap, EventSolver solver) {
        // System.out.println("initial filter: ");
        // System.out.println(filter);
        if (variableMap.size() == 0) {
            // System.out.println("After change: ");
            // System.out.println(filter);
            return filter;
        }
        BoolExpr allExpr = solver.False();
        
        for (String selectedVar : variableMap.keySet()) {
            // System.out.print("selectedVar: ");
            // System.out.println(selectedVar);
            for (VarValues val : variableMap.get(selectedVar)) {
                // System.out.print("Value: ");
                // System.out.println(val.strValue);
                // System.out.println("Going in recurseReplace with true");
                BoolExpr temp = recurseReplace(filter, selectedVar, true, val, solver);
                // System.out.print("true Output: ");
                // System.out.println(temp);
                allExpr = solver.MkOr(allExpr, temp);
                // System.out.println("Out of recurseReplace with true");
                // 
                // System.out.println(allExpr);
                allExpr = (BoolExpr) allExpr.simplify();

                temp = recurseReplace(filter, selectedVar, false, val, solver);
                // System.out.print("false Output: ");
                // System.out.println(temp);
                allExpr = solver.MkOr(allExpr, temp);
                allExpr = (BoolExpr) allExpr.simplify();
                // System.out.print("After false: ");
                // System.out.println(allExpr);
            }
        }
        
        // System.out.println("After change: ");
        // System.out.println(allExpr);
        return allExpr;
    }

    private static BoolExpr recurseReplace(BoolExpr filter, String var, boolean flag, VarValues val, EventSolver solver) {
        
        if (filter.isLE() || filter.isLT() || filter.isGT() || filter.isGE()) {
            // System.out.println("*******Arithmetic Operation. **********");
            if (flag){
                return solver.True();
            } else {
                return solver.False();
            }
        }

        ArithExpr variable = solver.MkSingleArith(var);
        ArithExpr value;
        
        if (val.valType == 0) {
            value = solver.MkSingleArith(val.intValue);
        } else {
            value = solver.MkSingleArith(val.strValue);
        }
        Expr[] filterArgs = filter.getArgs();

        // System.out.println(filter);
        // System.out.println(filterArgs.length);

        if (filter.isNot()) {
            return solver.MkNot(recurseReplace((BoolExpr) filterArgs[0], var, flag, val, solver));
        } else if (filter.isOr()) {
            BoolExpr ret = solver.False();
            for (Expr filterArg : filterArgs) {
                ret = solver.MkOr(recurseReplace((BoolExpr)filterArg, var, flag, val, solver), ret);
            }
            return ret;
        } else if (filter.isAnd()) {
            BoolExpr ret = solver.True();
            for (Expr filterArg : filterArgs) {
                ret = solver.MkAnd(recurseReplace((BoolExpr)filterArg, var, flag, val, solver), ret);
            }
            return ret;
        }

        if (filter.isEq()) {
            // System.out.print(" == ");
            // System.out.println(filterArgs[1]);
            if ((filterArgs[0].equals(variable) && filterArgs[1].equals(value)) || 
                (filterArgs[1].equals(variable) && filterArgs[0].equals(value))) {
                // System.out.println("It is a match");
                if (flag) {
                    return solver.True();
                } else {
                    return solver.False();
                }
            }
        } 
        // System.out.print("No match: ");
        // System.out.println(filter);
        return filter;
    }


    private static HashMap<String, Integer> getConstants(JSONObject constants) {
        HashMap<String, Integer> constantMap = new HashMap<>();
        for (Object key : constants.keySet()) {
            constantMap.put((String) key, ((Long) constants.get(key)).intValue());
        }

        return constantMap;
    }

    private static void printDSFA(SFA<BoolExpr, HashMap<String, Integer>> dsfa,
            BoolExpr negatedLocationExpr, BoolExpr filter, ArrayList<String> groupBy,
            HashSet<String> locationSet, Set<String> variableSet, HashMap<String, Expr> functionMap, 
            FileWriter fw)
            throws IOException {
        // Line 1: negated location expression
        fw.write(negatedLocationExpr.toString().replaceAll("\\R", "").replaceAll(" +", " ") + "\n");

        // Line 2: initial state
        fw.write(dsfa.getInitialState() + "\n");

        // Line 3: final state(s)
        boolean first = true;
        for (Integer s : dsfa.getFinalStates()) {
            if (first) {
                first = false;
            } else {
                fw.append(',');
            }
            fw.write(s.toString());
        }
        fw.write('\n');

        // Line 4: filter
        fw.write(filter.toString().replaceAll("\\R", "").replaceAll(" +", " ") + "\n");
        
        // Line 5: groupby
        first = true;
        for (String s : groupBy) {
            if (first) {
                first = false;
            } else {
                fw.append(',');
            }
            fw.write(s);
        }
        fw.write('\n');
        
        // Line 6: all locations
        first = true;
        for (String s : locationSet) {
            if (first) {
                first = false;
            } else {
                fw.append(',');
            }
            fw.write(s);
        }
        fw.write('\n');

        // Line 7: all variables
        first = true;
        for (String s : variableSet) {
            if (first) {
                first = false;
            } else {
                fw.append(',');
            }
            fw.write(s);
        }
        fw.write('\n');

        // Line 8: Number of map functions
        fw.write(Integer.toString(functionMap.size()));
        fw.write('\n');

        for (HashMap.Entry<String, Expr> entry : functionMap.entrySet()) {
            fw.write(entry.getValue().toString());
            fw.write('\n');
            fw.write(entry.getKey());
            fw.write('\n');
        }

        // Line (8+2n)+: transitions
        for (Move<BoolExpr, HashMap<String, Integer>> m : dsfa.getMoves()) {
            fw.write(((SFAInputMove<BoolExpr, HashMap<String, Integer>>) m).toParsableString()
                    .replaceAll("\\R", "").replaceAll(" +", " ") + '\n');
        }

        fw.close();
    }
    
    private static void printLocalDSFA(SFA<BoolExpr, HashMap<String, Integer>> dsfa, Set<String> variableSet, FileWriter fw) throws IOException {
        // Line 1: initial state
        fw.write(dsfa.getInitialState() + "\n");

        // Line 2: final state(s)
        boolean first = true;
        for (Integer s : dsfa.getFinalStates()) {
            if (first) {
                first = false;
            } else {
                fw.append(',');
            }
            fw.write(s.toString());
        }
        fw.write('\n');

        // Line 3: all variables
        first = true;
        for (String s : variableSet) {
            if (first) {
                first = false;
            } else {
                fw.append(',');
            }
            fw.write(s);
        }
        fw.write('\n');

        // Line 4+: transitions
        for (Move<BoolExpr, HashMap<String, Integer>> m : dsfa.getMoves()) {
            fw.write(((SFAInputMove<BoolExpr, HashMap<String, Integer>>) m).toParsableString()
                    .replaceAll("\\R", "").replaceAll(" +", " ") + '\n');
        }

        fw.close();
    }
}
