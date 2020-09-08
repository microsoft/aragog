package org.msr.mnr.verification.dsfa;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.lang3.tuple.Pair;
import org.msr.mnr.verification.utils.Packet;
import org.msr.mnr.verification.utils.ParseIntArray;


// Types: Key, Stream Type, Output Type
public class DSFA implements Serializable {
    private static final long serialVersionUID = 1L;

    public String name;
    public Integer startState;
    public HashSet<Integer> finalStates;

    public ArrayList<String> locationList;
    public ArrayList<String> variableList;
    private boolean variables;

    // stateId -> {EventIndex -> nextStateId}
    private HashMap<Integer, ArrayList<DSFAMove>> transitions;


    /**
     * Constructs a DSFA
     */
    public DSFA(String name, Integer startState, HashSet<Integer> finalStates,
            HashMap<Integer, ArrayList<DSFAMove>> moves, ArrayList<String> locationList,
            ArrayList<String> variableList) {
        this.name = name;
        this.startState = startState;
        this.finalStates = finalStates;
        this.transitions = moves;
        this.locationList = locationList;
        this.variableList = variableList;
        variables = !locationList.isEmpty() || !variableList.isEmpty();
        // System.out.print("Start State: ");
        // System.out.println(startState);
        // System.out.print("transitions size:");
        // System.out.println(transitions.size());
    }

    public boolean hasVariables() {
        return variables;
    }

    private String mergeRecursive(ConstraintTreeNode current) {
        // System.out.println("in mergeRecursive");
        if (current.children.isEmpty()) {
            // System.out.print("empty tree: ");
            // System.out.println(current);
            return current.currentState.toString();
        }
        // System.out.println("filled tree");

        // Get all subtree string representations
        ArrayList<String> subtreeStrings = new ArrayList<String>();
        for (ConstraintTreeNode child : current.children) {
            subtreeStrings.add(mergeRecursive(child));
            // if (child.currentState.equals(startState)) {   
            //     System.out.println("DELETE SUB");
            // }
        }

        // Deduplicate
        for (int i = subtreeStrings.size() - 1; i > 0; --i) {
            int count = Collections.frequency(subtreeStrings, subtreeStrings.get(i));
            if (count > 1) {
                // System.out.println("REMOVE SUB");
                current.children.remove(i);
                subtreeStrings.remove(i);
            }
        }
        
        // Return my string representation
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < current.children.size(); ++i) {
            sb.append("(");
            sb.append(ParseIntArray.printString(current.children.get(i).constraint));
            sb.append(" ");
            sb.append(subtreeStrings.get(i));
            sb.append(")");
        }
        String treeString = sb.toString();
        // System.out.print("Tree String: ");
        // System.out.print(treeString);
        return treeString;
    }
    
    // TODO: Make a list of the hash of ConstraintTreeNode.removeDuplicates(roots). If list count > 1, remove.
    private void merge(List<ConstraintTreeNode> rootConstraints) {
        // System.out.println("in merge");
        // Get all subtree string representations
        ArrayList<String> subtreeStrings = new ArrayList<String>();
        for (ConstraintTreeNode roots : rootConstraints) {
            subtreeStrings.add(mergeRecursive(roots));
        }

        // Deduplicate
        for (int i = subtreeStrings.size() - 1; i > 0; --i) {
            int count = Collections.frequency(subtreeStrings, subtreeStrings.get(i));
            if (count > 1) {
                // System.out.println("REMOVE");
                rootConstraints.remove(i);
                subtreeStrings.remove(i);
            }
        }

        for (int i = rootConstraints.size() - 1; i >= 0; --i) {
            if (rootConstraints.get(i).currentState.equals(startState)) {
                // System.out.println("DELETE");
                rootConstraints.remove(i);
            }
        }
    }

    public Pair<Integer, Boolean> advanceAndCheckSuppress(Packet p, Integer currentState) {

        // if (transitions.get(currentState) == null) {
        //     System.out.println("transition moves are null");
        //     System.out.print("Name:");
        //     System.out.println(this.name);
        //     System.out.print("Current State:");
        //     System.out.println(currentState);
        // }
        for (DSFAMove move : transitions.get(currentState)) {
            if (move.condition.evaluate(p)) {
                // String nofelOutput = "----------------------------------------\ninvariant name: " + name + "\nCurrent State: " + currentState.toString() + "\nPacket: " + p.toString() + "\nCondition satisfied: " + move.condition.toString() + "\n Moving to: " + move.to.toString() + "\n";
                //     System.out.println(nofelOutput);
                return Pair.of(move.to, move.suppress);
            }
        }

        throw new RuntimeException("[" + name + "] Did not find a valid move for state "
                + currentState + " and packet " + p);
    }

    public Integer advance(Packet p, Integer currentState) {
        for (DSFAMove move : transitions.get(currentState)) {
            if (move.condition.evaluate(p)) {
                // String nofelOutput = "----------------------------------------\ninvariant name: " + name + "\nCurrent State: " + currentState.toString() + "\nPacket: " + p.toString() + "\nCondition satisfied: " + move.condition.toString() + "\n Moving to: " + move.to.toString() + "\n";
                //     System.out.println(nofelOutput);
                return move.to;
            }
        }

        throw new RuntimeException("[" + name + "] Did not find a valid move for state "
                + currentState + " and packet " + p);
    }

    public boolean advanceConstraintTreeAndCheckSuppress(Packet p, ArrayList<int[]> constraints,
            List<ConstraintTreeNode> rootConstraints) {
        boolean suppressible = true;
        for (int i = 0; i < rootConstraints.size(); ++i) {
            suppressible &= traverseConstraintTreeAndCheckSuppress(p, rootConstraints.get(i),
                    constraints, 0, rootConstraints);
        }

        merge(rootConstraints);
        
        return suppressible;
    }

    public boolean advanceConstraintTreeAndCheckFinal(Packet p, ArrayList<int[]> constraints,
            List<ConstraintTreeNode> rootConstraints) {
        boolean inFinal = false;

        for (int i = 0; i < rootConstraints.size(); ++i) {

            inFinal |= traverseConstraintTreeAndCheckFinal(p, rootConstraints.get(i), constraints,
                    0, rootConstraints);
        }
        merge(rootConstraints);

        return inFinal;
    }

    private boolean traverseConstraintTreeAndCheckSuppress(Packet p, ConstraintTreeNode node,
            ArrayList<int[]> constraints, int index, List<ConstraintTreeNode> rootConstraints) {
        // DFS through the constraint tree, assembling the constraints as we go
        constraints.set(index, node.constraint);

        // When we get to a leaf, advance it
        if (node.children.isEmpty()) {
            // Traverse through every possible move. May split.
            for (DSFAMove move : transitions.get(node.currentState)) {
                if (move.condition.evaluate(p, constraints, locationList, variableList,
                        node.currentState, rootConstraints)) {
                    // String nofelOutput = "----------------------------------------\ninvariant name: " + name + "\nCurrent State: " + node.currentState.toString() + "\nPacket: " + p.toString() + "\nCondition satisfied: " + move.condition.toString() + "\n Moving to: " + move.to.toString() + "\n";
                    // System.out.println(nofelOutput);
                    node.currentState = move.to;
                    node.lastTime = p.getTime();
                    return move.suppress;
                }
            }

            throw new RuntimeException("[" + name + "] Did not find a valid move for state "
                    + node.currentState + " and packet " + p);
        }

        // Otherwise, just DFS
        // Use counter iteration so that we catch new children
        boolean suppressible = true;
        for (int i = 0; i < node.children.size(); ++i) {
            suppressible &= traverseConstraintTreeAndCheckSuppress(p, node.children.get(i),
                    constraints, index + 1, rootConstraints);
        }

        return suppressible;
    }

    // Note: All splits will be off of *, there's not ambiguity otherwise
    // Note: All subtrees will have a * as we clone the entire tree
    // Keep it at index 0 of every child array.
    private boolean traverseConstraintTreeAndCheckFinal(Packet p, ConstraintTreeNode node,
            ArrayList<int[]> constraints, int index, List<ConstraintTreeNode> rootConstraints) {
        // DFS through the constraint tree, assembling the constraints as we go
        constraints.set(index, node.constraint);

        // When we get to a leaf, advance it
        if (node.children.isEmpty()) {
            // Traverse through every possible move
            // May split
            for (DSFAMove move : transitions.get(node.currentState)) {

                // System.out.println("Print tree in traverseConstraintTree (of " + transitions.get(node.currentState).size() + ": ");
                // System.out.println(node.printTree(0));
                // System.out.println("Move: ");
                // System.out.println(move);
                if (move.condition.evaluate(p, constraints, locationList, variableList,
                        node.currentState, rootConstraints)) {
                    // String nofelOutput = "----------------------------------------\ninvariant name: " + name + "\nCurrent State: " + node.currentState.toString() + "\nPacket: " + p.toString() + "\nCondition satisfied: " + move.condition.toString() + "\n Moving to: " + move.to.toString() + "\n";
                    // System.out.println(nofelOutput);
                    node.currentState = move.to;
                    node.lastTime = p.getTime();
                    return checkInFinal(node.currentState);
                }
            }

            throw new RuntimeException("[" + name + "] Did not find a valid move for state "
                    + node.currentState + " and packet " + p);
        }

        // Otherwise, just DFS
        // Use counter iteration so that we catch new children
        boolean inFinal = false;
        for (int i = 0; i < node.children.size(); ++i) {
            inFinal |= traverseConstraintTreeAndCheckFinal(p, node.children.get(i), constraints,
                    index + 1, rootConstraints);
        }

        return inFinal;
    }

    // TODO: Move this in ConstraintTreeNode class
    private static ConstraintTreeNode find(List<ConstraintTreeNode> constraintList, int[] val) {
        for (ConstraintTreeNode c : constraintList) {
            if (c.constraint == val) {
                return c;
            }
        }

        return null;
    }

    public boolean checkInFinal(Integer currentState) {
        return finalStates.contains(currentState);
        
    }

    public static boolean splitIfNew(ArrayList<int[]> currentConstraints, int newIndex,
            int[] newConstraint, Integer currentState, List<ConstraintTreeNode> rootConstraints) {
        int[] searchVal = newIndex == 0 ? newConstraint : currentConstraints.get(0);
        ConstraintTreeNode current = find(rootConstraints, searchVal);
        if (current == null) {
            // duplicate the entire subtree
            rootConstraints.add(rootConstraints.get(0).clone(newConstraint));
            return true;
        }

        for (int i = 1; i < currentConstraints.size(); ++i) {
            // iterate through each level of the constraint tree and determine if it exists
            searchVal = newIndex == i ? newConstraint : currentConstraints.get(i);
            ConstraintTreeNode next = find(current.children, searchVal);
            if (next == null) {
                // duplicate the entire subtree
                current.children.get(0).clone(newConstraint);
                return true;
            }

            current = next;
        }

        return false;
    }
}
