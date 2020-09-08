package org.msr.mnr.verification.expressions;

import java.util.ArrayList;

import org.msr.mnr.verification.utils.Packet;

public class SubExpr extends ArithExpr {
    private static final long serialVersionUID = 5L;

    public SubExpr(Object left, Object right) {
        super(left, right);
    }

    @Override
    public int[] evaluate(Packet p) {
        int[] leftValue = evalSingle(left, leftType, p);
        int[] rightValue = evalSingle(right, rightType, p);
        
        return applyArithOp(leftValue, rightValue, "-");
    }

    @Override
    public int[] evaluate(Packet p, ArrayList<int[]> constraints,
            ArrayList<String> locationList, ArrayList<String> variableList) {

        int[] leftValue = evalSingle(left, leftType, p, constraints, locationList, variableList);
        int[] rightValue = evalSingle(right, rightType, p, constraints, locationList, variableList);

        return applyArithOp(leftValue, rightValue, "-");
    }


    @Override
    public String toString() {
        return "(" + left.toString() + " - " + right.toString() + ")";
    }
}