package main.java.generateSFA;

import org.json.simple.JSONObject;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import automata.Move;
import automata.sfa.SFA;
import automata.sfa.SFAInputMove;
import automata.sfa.SFAMove;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Symbol;
import com.microsoft.z3.EnumSort;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Context;

import org.sat4j.specs.TimeoutException;

public final class Util {

    private Util() {
        throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static HashMap<String, Integer> getConstants(JSONObject constants) {
        HashMap<String, Integer> constantMap = new HashMap<>();
        for (Object key : constants.keySet()) {
            constantMap.put((String) key, ((Long) constants.get(key)).intValue());
        }

        return constantMap;
    }

    public static Collection<SFAMove<BoolExpr, HashMap<String, Integer>>> getValidMoves(SFA<BoolExpr, HashMap<String, Integer>> sfa, Integer to, Integer from) {
        Collection<SFAMove<BoolExpr, HashMap<String, Integer>>> validMoves = new LinkedList<>();

        for (Move<BoolExpr, HashMap<String, Integer>> t : sfa.getMoves()) {
            try {
                SFAInputMove<BoolExpr, ?> im = (SFAInputMove<BoolExpr, HashMap<String, Integer>>) t;
                if (im.guard.isTrue())  {
                    continue;
                } else {
                    validMoves.add(new SFAInputMove<BoolExpr, HashMap<String,Integer>>(from, to, (BoolExpr) im.guard));
                }
            } catch (ClassCastException e) {
                continue;
            }
            
        }
        return validMoves;
    }

    public static BoolExpr createOtherMove(SFA<BoolExpr, HashMap<String, Integer>> sfa, EventSolver eventSolver) {
        BoolExpr ret = eventSolver.True();

        for (Move<BoolExpr, HashMap<String, Integer>> t : sfa.getMoves()) {
            try {
                SFAInputMove<BoolExpr, ?> im = (SFAInputMove<BoolExpr, HashMap<String, Integer>>) t;
                if (im.guard.isTrue())  {
                    continue;
                } else {
                    ret = eventSolver.MkAnd(ret, eventSolver.MkNot(im.guard));
                }
            } catch (ClassCastException e) {
                continue;
            }
            
        }
        return (BoolExpr) ret.simplify();
    }

    public static HashMap<IntExpr, EnumSort> createEnumsMap(HashMap<IntExpr, HashSet<BoolExpr>> filterValues, Context z3ctx) {
        HashMap<IntExpr, EnumSort> enumsMap = new HashMap<>();
        for (HashMap.Entry<IntExpr, HashSet<BoolExpr>> entry : filterValues.entrySet()) {
            IntExpr key = entry.getKey();
            HashSet<BoolExpr> value = entry.getValue();
            if (value.size() < 2) continue;

            Boolean flag = true;
            for (BoolExpr cond : value) {
                if (cond.isNot()) {
                    flag = false;
                } else if (!(cond.isEq())) {
                    throw new NotImplementedException("Unknown operator in BoolExpr: " + cond.toString());
                }
            }
            if (!flag) continue;

            Symbol name = z3ctx.mkSymbol(key.toString());
            Symbol[] valueEnums = new Symbol[value.size()];
            int counter = 0;
            for (BoolExpr cond : value) {
                valueEnums[counter] = z3ctx.mkSymbol(cond.getArgs()[1].toString());
                counter += 1;
            }
            EnumSort currentEnumSort = z3ctx.mkEnumSort(name, valueEnums); 
            enumsMap.put(key, currentEnumSort);
        }
        return enumsMap;
    }

    public static void updateSFAwithEnum(SFA<BoolExpr, HashMap<String, Integer>> sfa, HashMap<IntExpr, EnumSort> enumsMap, EventSolver eventSolver) {
        Collection<SFAMove<BoolExpr, HashMap<String, Integer>>> newTransitions = new LinkedList<>();
        Integer init_state =  sfa.getInitialState();

        for (Move<BoolExpr, HashMap<String, Integer>> t : sfa.getMoves()) {
            SFAInputMove<BoolExpr, ?> im;
            try {
                im = (SFAInputMove<BoolExpr, HashMap<String, Integer>>) t;
                System.out.print("transition guard: ");
                System.out.println(im.guard);
                im.guard = updateGuardWithEnum(im.guard, enumsMap, eventSolver.getContext());
                System.out.print("New transition: ");
                System.out.println(im.guard);
                System.out.println(im.guard.simplify());
                newTransitions.add(new SFAInputMove<BoolExpr, HashMap<String,Integer>>(t.from, t.to, (BoolExpr) im.guard));
            } catch (ClassCastException e) {
                newTransitions.add((SFAMove<BoolExpr, HashMap<String, Integer>>) t);
                continue;
            }
        }

        try {
            Collection<Integer> fin_states = sfa.getFinalStates();
            SFA<BoolExpr, HashMap<String, Integer>> newSfa = SFA.MkSFA(newTransitions, init_state, fin_states, eventSolver);
            System.out.println(newSfa.determinize(eventSolver).minimize(eventSolver));
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public static BoolExpr updateGuardWithEnum(BoolExpr transition, HashMap<IntExpr, EnumSort> enumsMap, Context z3ctx) {
        // System.out.print("transition guard: ");
        // System.out.println(transition);
        if (transition.isEq()) {
            System.out.println("in equals: ");
            IntExpr key = (IntExpr) transition.getArgs()[0];
            for (HashMap.Entry<IntExpr, EnumSort> selectedEnum : enumsMap.entrySet()) {
                if (key.equals(selectedEnum.getKey())) {
                    System.out.println("Key found");
                    Expr[] constants = selectedEnum.getValue().getConsts();

                    for (int i = 0; i < constants.length; i++) { 
                        if ((transition.getArgs()[1].toString()).equals(constants[i].getFuncDecl().getName().toString())) {
                            System.out.println("Value found");
                            // System.out.println()
                            Expr eventType = z3ctx.mkConst(selectedEnum.getValue().toString(), selectedEnum.getValue());
                            BoolExpr test = z3ctx.mkEq( eventType, constants[i]);
                            return test;
                            // System.out.print("New transition: ");
                            // System.out.println(test);
                            // System.out.println(test.simplify());
                            // Solver s = z3ctx.mkSolver();
                            // s.add(test);
                            // System.out.println(s.check());
                            // return s.check() == Status.SATISFIABLE;
                        }
                        // else {
                        //     System.out.print("Matching Value ");
                        //     System.out.print(transition.getArgs()[1]);
                        //     System.out.print(" with ");
                        //     System.out.println(constants[i].getFuncDecl().getName());
                        // }
                    } 
                } 
                // else {
                //     System.out.print("Matching Key ");
                //     System.out.print(key);
                //     System.out.print(" with ");
                //     System.out.println(selectedEnum.getKey());
                // }
            }
            return transition;
        } else if (transition.isAnd()) {
            System.out.println("in and: ");
            BoolExpr firstSide = updateGuardWithEnum((BoolExpr) transition.getArgs()[0], enumsMap, z3ctx);
            BoolExpr secondSide = updateGuardWithEnum((BoolExpr) transition.getArgs()[1], enumsMap, z3ctx);
            if (firstSide.isEq() && secondSide.isNot()) {
                if (firstSide.getArgs()[0].equals(secondSide.getArgs()[0].getArgs()[0])) {
                    System.out.println("Constants match");
                } else {
                    System.out.println("Constants do not match");
                }
            }
            return z3ctx.mkAnd(firstSide, secondSide);
        } else if (transition.isOr()) {
            System.out.println("in or: ");
            return z3ctx.mkOr(updateGuardWithEnum((BoolExpr) transition.getArgs()[0], enumsMap, z3ctx), updateGuardWithEnum((BoolExpr) transition.getArgs()[1], enumsMap, z3ctx));
        } else if (transition.isNot()) {
            System.out.println("in not: ");
            return z3ctx.mkNot(updateGuardWithEnum((BoolExpr) transition.getArgs()[0], enumsMap, z3ctx));
        } else {
            return transition;
        }
    }

    public static void addOtherTransition(SFA<BoolExpr, HashMap<String, Integer>> sfa, EventSolver eventSolver) {
        Collection<SFAMove<BoolExpr, HashMap<String, Integer>>> newTransitions = new LinkedList<>();
        Integer init_state =  sfa.getInitialState();
        System.out.print("Original length: ");
        System.out.println(sfa.getMoves().size());
        for (Move<BoolExpr, HashMap<String, Integer>> t : sfa.getMoves()) {
            SFAInputMove<BoolExpr, ?> im;
            try {
                im = (SFAInputMove<BoolExpr, HashMap<String, Integer>>) t;
                if (im.guard.isTrue())  {
                    // newTransitions.addAll(getValidMoves(sfa, t.from, t.to));
                    newTransitions.add(new SFAInputMove<BoolExpr, HashMap<String,Integer>>(t.from, t.to, (BoolExpr) im.guard));
                    // newTransitions.add(new SFAInputMove<BoolExpr, HashMap<String,Integer>>(t.from, t.to, (BoolExpr) eventSolver.getOther()));
                    newTransitions.add(new SFAInputMove<BoolExpr, HashMap<String,Integer>>(t.from, t.to, createOtherMove(sfa, eventSolver)));
                } else {
                    newTransitions.add((SFAMove<BoolExpr, HashMap<String, Integer>>) t);
                    continue;
                }
            } catch (ClassCastException e) {
                newTransitions.add((SFAMove<BoolExpr, HashMap<String, Integer>>) t);
                continue;
            }
        }

        System.out.println("Printing satisfactions");
        for (Move<BoolExpr, HashMap<String, Integer>> t : newTransitions) {
            try {
                System.out.println(t.isSatisfiable(eventSolver));
                System.out.println(t);
            } catch (TimeoutException e) {
                e.printStackTrace();
            } 
        }

        System.out.print("New length: ");
        System.out.println(newTransitions.size());
        try {
            Collection<Integer> fin_states = sfa.getFinalStates();
            SFA<BoolExpr, HashMap<String, Integer>> newSfa = SFA.MkSFA(newTransitions, init_state, fin_states, eventSolver);
            System.out.println("Printing new SFA");
            System.out.println(newSfa);
            System.out.println("Printing new SFA after determinize");
            System.out.println(newSfa.determinize(eventSolver));
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }


}