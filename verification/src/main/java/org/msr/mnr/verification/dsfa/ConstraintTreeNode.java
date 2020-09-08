package org.msr.mnr.verification.dsfa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ConstraintTreeNode {
    // null indicates "other" matches
    int[] constraint;
    ConstraintTreeNode parent;
    ArrayList<ConstraintTreeNode> children;

    Integer currentState;
    long lastTime;

    public ConstraintTreeNode(int[] constraint) {
        this.constraint = constraint;
        this.children = new ArrayList<ConstraintTreeNode>();

        currentState = -1;
        lastTime = -1;
    }

    protected static void adopt(ConstraintTreeNode parent, ConstraintTreeNode child) {
        if (parent != null) {
            parent.children.add(child);
        }
        child.parent = parent;
    }

    public static ConstraintTreeNode makeFreshTree(Integer startState,
            ArrayList<String> locationList, ArrayList<String> variableList) {
        ConstraintTreeNode root = new ConstraintTreeNode(null);

        ConstraintTreeNode current = root;
        for (int i = 1; i < locationList.size() + variableList.size(); ++i) {
            ConstraintTreeNode next = new ConstraintTreeNode(null);
            adopt(current, next);
            current = next;
        }
        current.currentState = startState;
        current.lastTime = 0;
        return root;
    }

    protected ConstraintTreeNode clone(int[] newConstraint) {
        ConstraintTreeNode newNode = new ConstraintTreeNode(newConstraint);
        adopt(parent, newNode);
        newNode.currentState = currentState;
        newNode.lastTime = lastTime;

        for (ConstraintTreeNode child : children) {
            adopt(newNode, child.cloneExact());
        }

        return newNode;
    }

    private ConstraintTreeNode cloneExact() {
        ConstraintTreeNode newNode = new ConstraintTreeNode(constraint);
        newNode.currentState = currentState;
        newNode.lastTime = lastTime;

        for (ConstraintTreeNode child : children) {
            adopt(newNode, child.cloneExact());
        }

        return newNode;
    }

    public Integer getCurrentState() {
        return currentState;
    }

    public String printTree(int depth) {
        String result = "currentState: " + Integer.toString(currentState);
        result += "lastTime: " + Long.toString(lastTime);
        result += " constraint: " + Arrays.toString(constraint);
        result += " depth: " + Integer.toString(depth);
        result += " ==> children: \n" ;
        for (ConstraintTreeNode child : children) {
            result += String.join("", Collections.nCopies(depth, "\n")) + child.printTree(depth + 1); 
        }
        return result;
    }

    @Override
    public String toString() {
        return printTree(0);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof ConstraintTreeNode)) {
            return false;
        }
        ConstraintTreeNode otherTree = (ConstraintTreeNode) other;
        if (currentState != otherTree.currentState) {
            return false;
        }

        if (constraint.length != otherTree.constraint.length) {
            return false;
        }

        if (children.size() != otherTree.children.size()) {
            return false;
        }

        for (int i = 0; i < constraint.length; i++) {
            if (constraint[i] != otherTree.constraint[i]) {
                return false;
            }
        }

        for (int i = 0; i < children.size(); i++) {
            if (!(children.get(i).equals(otherTree.children.get(i)))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + currentState;
        for (int i = 0; i < constraint.length; i++) {
            hash = 53 * hash + constraint[i];
        }
        for (int i = 0; i < children.size(); i++) {
            hash = 53 * hash + children.get(i).hashCode();
        }
        return hash;
    }

    public static ConstraintTreeNode removeDuplicates(ConstraintTreeNode root) {
        // Create original children list
        ArrayList<ConstraintTreeNode> oldChildrenList = new ArrayList<>();
        oldChildrenList.addAll(root.children);

        // Iterate through original children list
        for (ConstraintTreeNode currentChild : oldChildrenList) {

            // If the original child is still present:
            if (root.children.contains(currentChild)) {

                // Get all duplicates of the child
                ArrayList<ConstraintTreeNode> duplicates = new ArrayList<>();
                for (ConstraintTreeNode selectedChild : root.children) {
                    if (selectedChild.equals(currentChild)) {
                        duplicates.add(selectedChild);
                    }
                }

                // Remove all duplicates, and consolidate the children of all duplicates
                for (ConstraintTreeNode selectedChild : duplicates) {
                    currentChild.children.addAll(selectedChild.children);
                    root.children.remove(selectedChild);
                }

                // Add the child back to root.
                root.children.add(currentChild);
            }
            // Remove duplicates from the child.
            ConstraintTreeNode.removeDuplicates(currentChild);
        }
    
        return root;
    }
}
