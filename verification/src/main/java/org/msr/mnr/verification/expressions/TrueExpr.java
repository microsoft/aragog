package org.msr.mnr.verification.expressions;

import java.util.ArrayList;
import java.util.List;

import org.msr.mnr.verification.dsfa.ConstraintTreeNode;
import org.msr.mnr.verification.utils.Packet;

public class TrueExpr extends BoolExpr {
    private static final long serialVersionUID = -5278681248335478957L;

    @Override
    public Boolean evaluate(Packet p, ArrayList<int[]> constraints, ArrayList<String> locationList,
            ArrayList<String> variableList, Integer currentState,
            List<ConstraintTreeNode> rootConstraints) {
        return !negated; // normally true
    }

    @Override
    public Boolean evaluate(Packet p) {
        return !negated; // normally true
    }
    
    @Override
    public String toString() {
        return (negated ? "!" : "") + "True";
    }
}