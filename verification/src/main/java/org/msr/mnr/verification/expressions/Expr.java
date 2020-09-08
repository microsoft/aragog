package org.msr.mnr.verification.expressions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

import org.msr.mnr.verification.utils.Packet;
import org.msr.mnr.verification.utils.ParseIntArray;

public abstract class Expr implements Serializable {
    private static final long serialVersionUID = 20200508215300L;

    public abstract Object evaluate(Packet p);

    protected enum ExprType {
        PACKET, VALUE, ARITH, VARIABLE, BOOL, TIME, RHO, LOCATION_VAR;
    }

    protected ExprType leftType, rightType;
    protected Object left, right;

    private void init(Object _left, Object _right) {
        this.leftType = parseType(_left);
        this.left = correctValueType(_left, leftType);

        if (_right != null) {
            this.rightType = parseType(_right);
            this.right = correctValueType(_right,rightType);
        } else {
            this.rightType = null;
            this.right = null;
        }

    }

    private static Object correctValueType(Object obj, ExprType objType) {
        if (objType == ExprType.VALUE && obj instanceof String) {
            String s = ((String) obj).toLowerCase();
            s = s.replace("\"", "").replace("|","");
            return ParseIntArray.fromBytes(s.getBytes(), 0, s.length());
        } else {
            return obj;
        }
    }

    private ExprType parseType(Object o) {
        if (o instanceof ArithExpr) {
            return ExprType.ARITH;
        } else if (o instanceof BoolExpr) {
            return ExprType.BOOL;
        } else if (!(o instanceof String)) {
            return ExprType.VALUE;
        } else if (o instanceof NegativeContext) {
            throw new RuntimeException("NegativeContexts should not get to this point");
        }

        String s = (String) o;
        if (s.startsWith("$")) {
            return ExprType.VARIABLE;
        } else if (s.equals("META_rho")) {
            return ExprType.RHO;
        } else if (s.equals("TIME")) {
            return ExprType.TIME;
        } else if (s.contains("|\"") && s.contains("\"|")) {
            return ExprType.VALUE;
        } 

        return ExprType.PACKET;
    }

    public Expr(Object left, Object right) {
        init(left, right);
    }

    public Expr(Object only) {
        init(only, null);
    }

    public Expr() {
        left = null;
        right = null;
        leftType = null;
        rightType = null;
    }

    public static int[] evalSingle(Object val, ExprType valType, Packet p) {
        int[] outValue = null;
        switch (valType) {
        case PACKET:
            outValue = p.get(val);
            break;
        case VALUE:
            outValue = (int[]) val;
            break;
        case ARITH:
            outValue = ((ArithExpr) val).evaluate(p);
            break;
        default:
            System.out.println(valType);
            throw new RuntimeException("Unexpected expression simple evalSingle. Got Unknown Type");
        }
        return outValue;
    }

    public static int[] evalSingle(Object val, ExprType valType, Packet p,  ArrayList<int[]> constraints, ArrayList<String> locationList,
            ArrayList<String> variableList) {
        int[] outValue = null;
        int level;
        // System.out.println(valType);
        switch (valType) {
        case PACKET:
            outValue = p.get(val);
            break;
        case VALUE:
            outValue = (int[]) val;
            break;
        case TIME:
            outValue = ParseIntArray.fromLong(p.getTime());
            break;
        case RHO:
            outValue = p.getLocation();
            break;
        case VARIABLE:
            level = locationList.size() + variableList.indexOf(val);
            outValue = constraints.get(level);
            break;
        case LOCATION_VAR:
            level = locationList.indexOf(val);
            outValue = constraints.get(level);
            break;
        case ARITH:
            outValue = ((ArithExpr) val).evaluate(p, constraints, locationList, variableList);
            break;
        default:
            System.out.println(valType);
            throw new RuntimeException("Unexpected expression variable evalSingle.");
        }
        return outValue;
    }


    public Expr(Object left, Object right, HashSet<String> locationReferences,
            HashMap<String, String> variableComparisons) {
        init(left, right);

        if (leftType == ExprType.RHO) {
            String s = right instanceof String ? (String) right : ParseIntArray.printString((int[]) right);
            locationReferences.add(s);
            rightType = ExprType.LOCATION_VAR;
        } else if (leftType == ExprType.VARIABLE) {
            String s = right instanceof String ? (String) right : ParseIntArray.printString((int[]) right);
            variableComparisons.put((String) left, s);
        }

        if (rightType == ExprType.RHO) {
            String s = left instanceof String ? (String) left : ParseIntArray.printString((int[]) left);
            locationReferences.add(s);
            leftType = ExprType.LOCATION_VAR;
        } else if (rightType == ExprType.VARIABLE) {
            String s = left instanceof String ? (String) left : ParseIntArray.printString((int[]) left);
            variableComparisons.put((String) right, s);
        }

    }

    @Override
    public abstract String toString();
}