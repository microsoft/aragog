package org.msr.mnr.verification.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.msr.mnr.verification.dsfa.ConstraintTreeNode;
import org.msr.mnr.verification.utils.Packet;
import org.msr.mnr.verification.utils.ParseIntArray;

public class LeExpr extends BoolExpr {
    private static final long serialVersionUID = 8156224424081359464L;

    public LeExpr(Object left, Object right) {
        super(left, right);
    }

    @Override
    public Boolean evaluate(Packet p, ArrayList<int[]> constraints, ArrayList<String> locationList,
            ArrayList<String> variableList, Integer currentState,
            List<ConstraintTreeNode> rootConstraints) {

        int[] leftValue = evalSingle(left, leftType, p, constraints, locationList, variableList);
        int[] rightValue = evalSingle(right, rightType, p, constraints, locationList, variableList);

        return (ParseIntArray.compare(leftValue, rightValue) <= 0) ^ (negated);
    }

    @Override
    public Boolean evaluate(Packet p) {
        // instantiate any packet fields. That way locations and variables are only
        // dealing with int[]s
        int[] leftValue = evalSingle(left, leftType, p);
        int[] rightValue = evalSingle(right, rightType, p);

        return (ParseIntArray.compare(leftValue, rightValue) <= 0) ^ (negated);
    }

    @Override
    public String toString() {
        if (leftType == ExprType.VALUE) {
            return (negated ? "!" : "") + "(" + Arrays.toString((int[]) left) + " <= " + right + ")";
        }
        return (negated ? "!" : "") + "(" + left + " <= " + right + ")";
    }
}