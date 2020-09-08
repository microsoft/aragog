package org.msr.mnr.verification.expressions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;

import org.msr.mnr.verification.dsfa.ConstraintTreeNode;
import org.msr.mnr.verification.dsfa.DSFA;
import org.msr.mnr.verification.utils.Packet;
import org.msr.mnr.verification.utils.ParseIntArray;


public class EqExpr extends BoolExpr {
    private static final long serialVersionUID = 3139893660332363038L;

    public EqExpr(Object left, Object right, HashSet<String> locationReferences,
            HashMap<String, String> variableComparisons) {
        super(left, right, locationReferences, variableComparisons);
    }

    @Override
    public Boolean evaluate(Packet p, ArrayList<int[]> constraints, ArrayList<String> locationList,
            ArrayList<String> variableList, Integer currentState, List<ConstraintTreeNode> rootConstraints) {

        // instantiate any packet fields. That way locations and variables are only
        // dealing with int[]s
        // System.out.println("ConditionA: " + this.toString());
        int[] leftValue = evalSingle(left, leftType, p, constraints, locationList, variableList);
        int[] rightValue = evalSingle(right, rightType, p, constraints, locationList, variableList);
        // System.out.println("Comparing: " + ParseIntArray.getString(leftValue) + " with: " + ParseIntArray.getString(rightValue));
        
        // Now parse variables.  Need to do this after the above so that we have the concrete int[] values
        if (leftType == ExprType.VARIABLE || leftType == ExprType.LOCATION_VAR) {
            int level;
            if (leftType == ExprType.VARIABLE) {
                level = locationList.size() + variableList.indexOf(left);
            } else {
                level = locationList.indexOf(left);
            }

            if (constraints.get(level) == null) {
                // Unconstrained, we may need to split, but only if we don't have an identical
                // set of constraints already
                assert(rightValue != null);
                DSFA.splitIfNew(constraints, 0, rightValue, currentState, rootConstraints);
                return negated; // normally false
            } else {
                // We have a constraint
                leftValue = constraints.get(level);
            }
        } else if (rightType == ExprType.VARIABLE || rightType == ExprType.LOCATION_VAR) {
            int level;
            if (rightType == ExprType.VARIABLE) {
                level = locationList.size() + variableList.indexOf(right);
            } else {
                level = locationList.indexOf(right);
            }

            if (constraints.get(level) == null) {
                // Unconstrained, we may need to split, but only if we don't have an identical
                // set of constraints already
                assert(leftValue != null);
                DSFA.splitIfNew(constraints, 0, leftValue, currentState, rootConstraints);
                return negated; // normally false
            } else {
                // We have a constraint
                rightValue = constraints.get(level);
            }
        } else if (leftValue == null || rightValue == null) {
            throw new RuntimeException("Unknown Eq Expression fields: " + left + " and " + right);
        }

        // equal ^ negated = false
        // equal ^ not negated = true
        // not equal ^ negated = true
        // not equal ^ not negated = false
        
        return (ParseIntArray.compare(leftValue, rightValue) == 0) ^ (negated);
    }

    @Override
    public Boolean evaluate(Packet p) {
        // instantiate any packet fields. That way locations and variables are only
        // dealing with int[]s
        // System.out.println("ConditionB: " + this.toString());
        int[] leftValue = evalSingle(left, leftType, p);
        int[] rightValue = evalSingle(right, rightType, p);
        // System.out.println("Comparing: " + Arrays.toString(leftValue) + " with: " + Arrays.toString(rightValue));
        // equal ^ negated = false
        // equal ^ not negated = true
        // not equal ^ negated = true
        // not equal ^ not negated = false
        return (ParseIntArray.compare(leftValue, rightValue) == 0) ^ (negated);
    }

    @Override
    public String toString() {
        if (rightType == ExprType.VALUE) {
            return (negated ? "!" : "") + "(" + left + " == " + Arrays.toString((int[]) right) + ")";
        } else {
            return (negated ? "!" : "") + "(" + left + " == " + right + ")";
        }
    }
}