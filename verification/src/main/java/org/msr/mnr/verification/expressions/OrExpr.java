package org.msr.mnr.verification.expressions;

import java.util.ArrayList;
import java.util.List;

import org.msr.mnr.verification.dsfa.ConstraintTreeNode;
import org.msr.mnr.verification.utils.Packet;

public class OrExpr extends BoolExpr {
    private static final long serialVersionUID = -2030011970325054536L;
    private BoolExpr e1, e2;

    public OrExpr(Object left, Object right) {
        super(left, right);
        if (left instanceof BoolExpr) {
            e1 = (BoolExpr) left;
        } else {
            throw new RuntimeException("Unexpected expression left OrExpr.");
        }

        if (right instanceof BoolExpr) {
            e2 = (BoolExpr) right;
        } else {
            throw new RuntimeException("Unexpected expression right OrExpr.");
        }

    }

    @Override
    public Boolean evaluate(Packet p, ArrayList<int[]> constraints, ArrayList<String> locationList,
            ArrayList<String> variableList, Integer currentState,
            List<ConstraintTreeNode> rootConstraints) {
        Boolean ret = e1.evaluate(p, constraints, locationList, variableList, currentState,
                rootConstraints)
                || e2.evaluate(p, constraints, locationList, variableList, currentState,
                        rootConstraints);
        return negated ? !ret : ret;
    }

    @Override
    public Boolean evaluate(Packet p) {
        Boolean ret = e1.evaluate(p) || e2.evaluate(p);
        return negated ? !ret : ret;
    }

    @Override
    public String toString() {
        return (negated ? "!" : "") + "(" + e1.toString() + " || " + e2.toString() + ")";
    }
}