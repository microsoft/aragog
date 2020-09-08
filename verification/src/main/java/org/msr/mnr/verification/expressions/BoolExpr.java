package org.msr.mnr.verification.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;

import org.msr.mnr.verification.dsfa.ConstraintTreeNode;
import org.msr.mnr.verification.utils.Packet;

public abstract class BoolExpr extends Expr {
    private static final long serialVersionUID = 2806048939951936622L;
    protected boolean negated = false;

    public BoolExpr(Object left, Object right) {
        super(left, right);
    }

    public BoolExpr() {
        super();
    }

    public BoolExpr(Object left, Object right, HashSet<String> locationReferences,
            HashMap<String, String> variableComparisons) {
        super(left, right, locationReferences, variableComparisons);
    }

    public void toggleNegated() {
        negated = !negated;
    }

    public abstract Boolean evaluate(Packet p, ArrayList<int[]> constraints,
            ArrayList<String> locationList, ArrayList<String> variableList, Integer currentState,
            List<ConstraintTreeNode> rootConstraints);

    @Override
    public abstract Boolean evaluate(Packet p);
}