package main.java.generateSFA;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;

import automata.sfa.SFA;
import automata.Move;
import automata.sfa.SFAInputMove;
import automata.sfa.SFAMove;
import theory.BooleanAlgebra;
import automata.sfa.SFAEpsilon;

import org.sat4j.specs.TimeoutException;

public class GenerateLocalSFA {
    static class LocalSFA {
        SFA<BoolExpr, HashMap<String, Integer>> lsfa;
        HashMap<Integer, Collection<Integer>> stateMapping;
        IntExpr locExpr;
        boolean opposite;

        LocalSFA(IntExpr locExpr) {
            this.locExpr = locExpr;
            this.stateMapping = new HashMap<>();
            this.opposite = false;
        }

        LocalSFA(IntExpr locExpr, boolean _opposite) {
            this.locExpr = locExpr;
            this.stateMapping = new HashMap<>();
            this.opposite = _opposite;
        }
    }

    public static ArrayList<LocalSFA> generateLocalSFAs(
            SFA<BoolExpr, HashMap<String, Integer>> originalSFA, HashMap<String, IntExpr> locations,
            EventSolver eventSolver, BoolExpr filter) throws TimeoutException {
        ArrayList<LocalSFA> localSFAs = new ArrayList<>();

        // Create a local SFA for every named location
        for (IntExpr locExpr : locations.values()) {
            // System.out.println(locExpr);
            LocalSFA l = new LocalSFA(locExpr);
            l.lsfa = makeLocalSFA(l.stateMapping, originalSFA, locExpr, eventSolver, filter, false);
            localSFAs.add(l);
            // System.out.println("----------------------");
            LocalSFA lo = new LocalSFA(locExpr, true);
            lo.lsfa = makeLocalSFA(lo.stateMapping, originalSFA, locExpr, eventSolver, filter, true);
            // System.out.println(lo.lsfa);
            // System.out.println("----------END------------");
            localSFAs.add(lo);
        }

        return localSFAs;
    }

    public static SFA<BoolExpr, HashMap<String, Integer>> makeLocalSFA(
            HashMap<Integer, Collection<Integer>> stateMappingOut,
            SFA<BoolExpr, HashMap<String, Integer>> originalSFA, IntExpr location,
            BooleanAlgebra<BoolExpr, HashMap<String, Integer>> solver, BoolExpr filter,
            boolean opposite)
            throws TimeoutException {
        Collection<SFAMove<BoolExpr, HashMap<String, Integer>>> newTransitions = new LinkedList<>();

        for (Move<BoolExpr, HashMap<String, Integer>> t : originalSFA.getMoves()) {
            SFAInputMove<BoolExpr, HashMap<String, Integer>> im = (SFAInputMove<BoolExpr, HashMap<String, Integer>>) t;
            // System.out.println()
            if (solver instanceof EventSolver) {
                EventSolver eventSolver = (EventSolver) solver;
                if (checklocalPresent(im.guard, location, eventSolver, filter, opposite)) {
                    BoolExpr newGuard = eventSolver.specifyLocation(location, im.guard, opposite);
                    // if (im.suppressible) {
                    //     System.out.print("old guardA: ");
                    //     System.out.println(im.guard);
                    //     System.out.print("new guardA: ");
                    //     System.out.println(newGuard);

                    // }
                    newTransitions.add(new SFAInputMove<>(im.from, im.to, newGuard));
                } else {
                    // System.out.print("current guardE: ");
                    // System.out.println(im.guard);
                    BoolExpr newGuard = eventSolver.specifyLocation(location, im.guard, opposite);
                    // if (im.suppressible) {
                    //     System.out.print("old guardB: ");
                    //     System.out.println(im.guard);
                    //     System.out.print("new guardB: ");
                    //     System.out.println(newGuard);

                    // }
                    newTransitions.add(new SFAInputMove<>(im.from, im.to, newGuard));
                    newTransitions.add(new SFAEpsilon<>(t.from, t.to));
                    // System.out.print("new guardE: ");
                    // System.out.println(newGuard);
                }
            }
        }

        // Create the DSFA and return its determinized version
        SFA<BoolExpr, HashMap<String, Integer>> localsfa = SFA.MkSFA(newTransitions,
                originalSFA.getInitialState(), originalSFA.getFinalStates(), solver);
        // return localsfa.determinize(stateMappingOut, solver);
        return localsfa;
    }

    private static Boolean checklocalPresent(Expr guard, IntExpr location, EventSolver eventSolver,
            BoolExpr filter, boolean opposite) {
        BoolExpr transition = (BoolExpr) guard;

        IntExpr rho = eventSolver.getLocationExpr();
        BoolExpr locationPredicate;
        if (opposite) {
            locationPredicate = eventSolver.MkMatch(rho, "!=", location);
        } else {
            locationPredicate = eventSolver.MkMatch(rho, "==", location);
        }
        BoolExpr implicationX = eventSolver.MkNot(
                eventSolver.MkImplies(eventSolver.MkAnd(transition, filter), locationPredicate));

        return eventSolver.IsUnSAT(implicationX);
    }
}