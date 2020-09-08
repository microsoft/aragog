package org.msr.mnr.verification.expressions;

import java.util.ArrayList;

import org.msr.mnr.verification.utils.Packet;
import org.msr.mnr.verification.utils.ParseIntArray;

public abstract class ArithExpr extends Expr {
    private static final long serialVersionUID = 202005082232L;

    public ArithExpr(Object left, Object right) {
        super(left, right);
    }

    public ArithExpr(Object left) {
        super(left);
    }

    @Override
    public abstract int[] evaluate(Packet p);

    public abstract int[] evaluate(Packet p, ArrayList<int[]> constraints,
            ArrayList<String> locationList, ArrayList<String> variableList);

    public static int[] applyArithOp(int[] leftValue, int[] rightValue, String operand) {
        long leftVal = ParseIntArray.getLong(leftValue);
        long rightVal = ParseIntArray.getLong(rightValue);
        switch(operand) {
        case "+":
            return ParseIntArray.fromLong(leftVal + rightVal);
        case "-":
            return ParseIntArray.fromLong(leftVal - rightVal);
        case "*":
            return ParseIntArray.fromLong(leftVal * rightVal);
        case "/":
            return ParseIntArray.fromLong(leftVal / rightVal);
        default:
            break;
        }
        throw new RuntimeException("Arith Operation not found: " + operand);
    }

}