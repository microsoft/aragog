package org.msr.mnr.verification.expressions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;

import org.msr.mnr.verification.utils.ParseIntArray;

public abstract class TerminalExpr extends BoolExpr {
    private static final long serialVersionUID = 8363964636619957082L;

    protected enum TerminalType {
        PACKET, VARIABLE, RHO, LOCATION_VAR, TIME, VALUE;
    }

    protected TerminalType leftType, rightType;
    protected Object left, right;

    private void init(Object left, Object right) {
        // System.out.println("left")
        this.leftType = parseType(left);
        if (leftType == TerminalType.VALUE && left instanceof String) {
            String s = ((String) left).toLowerCase();
            s = s.replace("\"", "");
            this.left = ParseIntArray.fromBytes(s.getBytes(), 0, s.length());
            // System.out.println("Left: " + Arrays.toString((int[]) this.left));
        } else {
            this.left = left;
            // System.out.println("Left: " + left);
        }
        this.rightType = parseType(right);
        if (rightType == TerminalType.VALUE && right instanceof String) {
            String s = ((String) right).toLowerCase();
            s = s.replace("\"", "");
            this.right = ParseIntArray.fromBytes(s.getBytes(), 0, s.length());
            // System.out.println("Right: " + Arrays.toString((int []) this.right));
        } else {
            this.right = right;
            // System.out.println("Right: " + right);
        }
    }

    private TerminalType parseType(Object o) {
        if (!(o instanceof String)) {
            return TerminalType.VALUE;
        }

        String s = (String) o;
        
        if (s.startsWith("VAR_")) {
            return TerminalType.VARIABLE;
        } else if (s.equals("META_rho")) {
            return TerminalType.RHO;
        } else if (s.equals("META_deltat")) {
            return TerminalType.TIME;
        }

        if (((String) o).contains("\"")) {
            return TerminalType.VALUE;
        }

        return TerminalType.PACKET;
    }

    public TerminalExpr(Object left, Object right) {
        init(left, right);
    }

    public TerminalExpr() {
        left = null;
        right = null;
        leftType = null;
        rightType = null;
    }

    public TerminalExpr(Object left, Object right, HashSet<String> locationReferences,
            HashMap<String, String> variableComparisons) {
        // TODO: Pass fields here
        init(left, right);

        if (leftType == TerminalType.RHO) {
            String s = right instanceof String ? (String) right : ParseIntArray.printString((int[]) right);
            locationReferences.add(s);
            rightType = TerminalType.LOCATION_VAR;
        } else if (leftType == TerminalType.VARIABLE) {
            String s = right instanceof String ? (String) right : ParseIntArray.printString((int[]) right);
            variableComparisons.put((String) left, s);
        }

        if (rightType == TerminalType.RHO) {
            String s = left instanceof String ? (String) left : ParseIntArray.printString((int[]) left);
            locationReferences.add(s);
            leftType = TerminalType.LOCATION_VAR;
        } else if (rightType == TerminalType.VARIABLE) {
            // System.out.print("right: ");
            // System.out.println(right);
            // System.out.print("left: ");
            // System.out.println(left);
            // System.out.println(variableComparisons);
            String s = left instanceof String ? (String) left : ParseIntArray.printString((int[]) left);
            variableComparisons.put((String) right, s);
        }
    }
}
