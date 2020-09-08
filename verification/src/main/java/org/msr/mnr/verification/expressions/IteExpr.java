package org.msr.mnr.verification.expressions;

import org.msr.mnr.verification.utils.Packet;

public class IteExpr extends Expr {
    private static final long serialVersionUID = 7L;

    private BoolExpr condition;

    public IteExpr(BoolExpr condition, Object left, Object right) {
        super(left,right);
        this.condition = condition;
    }

    @Override
    public int[] evaluate(Packet p) {
        // System.out.println("input packet: ");
        // System.out.println(p);
        boolean branch = condition.evaluate(p);
        // System.out.print("branch output: ");
        // System.out.println(branch);
        if (branch) {
            // System.out.println("going in left");
            return evalSingle(left, leftType, p);
        } else {
            // System.out.println("going in right");
            return evalSingle(right, rightType, p);
        }
    }


    @Override
    public String toString() {
        return "( ITE " + condition.toString() + " ? " + left.toString() + " : " + right.toString() + ")";
    }
}
