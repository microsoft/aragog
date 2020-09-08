package org.msr.mnr.verification.dsfa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import org.msr.mnr.verification.expressions.BoolExpr;

public class DSFAMove implements Serializable {
    private static final long serialVersionUID = -6991744916243331573L;
    BoolExpr condition;
    boolean suppress;
    Integer to;
    boolean toFinal;
    HashSet<String> locationReferences;
    HashMap<String, String> variableComparisons;

    public DSFAMove(BoolExpr condition, boolean suppress, Integer to, boolean toFinal,
            HashSet<String> locationReferences, HashMap<String, String> variableComparisons) {
        this.condition = condition;
        this.suppress = suppress;
        this.to = to;
        this.toFinal = toFinal;
        this.locationReferences = locationReferences;
        this.variableComparisons = variableComparisons;
    }

    @Override
    public String toString() {
        String result = "Condition: " + condition.toString() + "\n";
        result += "To: " + Integer.toString(to) + "\n";
        result += "To Final: " + String.valueOf(toFinal) + "\n";
        result += "locationReferences: "  + String.join(", ", locationReferences) + "\n";
        result += "variableComparisons: " + variableComparisons.toString() + "\n";
        return result;
    }
}
