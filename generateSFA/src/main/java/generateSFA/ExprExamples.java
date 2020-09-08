package main.java.generateSFA;


import org.apache.commons.lang3.NotImplementedException;

import com.microsoft.z3.Context;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.Solver;



public class ExprExamples {
    public ExprExamples() {
    }
    
    public void base_case() {
        System.out.println("Running Example base_case: Simple case\n");
        Context z3ctx = new Context();
        IntExpr field_name = (IntExpr) z3ctx.mkConst("event_type", z3ctx.getIntSort());
        IntNum e1 = z3ctx.mkInt(1);
        IntNum e2 = z3ctx.mkInt(2);

        BoolExpr equalityExpr = MkMatch(z3ctx, field_name, "==", e1);
        BoolExpr unEqualityExpr = MkMatch(z3ctx, field_name, "!=", e2);

        BoolExpr baseCase = z3ctx.mkAnd(equalityExpr, unEqualityExpr);
        System.out.print("Expr: ");
        System.out.println(baseCase);
        System.out.print("Expr Simplified: ");
        System.out.println(baseCase.simplify());
        System.out.println("\nEnd of Example base_case\n");

    }

    public void equal_test_case() {
        System.out.println("Running Example equal_test_case \n");
        Context z3ctx = new Context();
        IntExpr field_name = (IntExpr) z3ctx.mkConst("event_type", z3ctx.getIntSort());
        IntNum e1 = z3ctx.mkInt(1);
        IntNum e2 = z3ctx.mkInt(2);

        BoolExpr firstExpr = MkMatch(z3ctx, field_name, "==", e1);
        BoolExpr secExpr = MkMatch(z3ctx, field_name, "==", e1);

        Expr[] argList = firstExpr.getArgs();

        for (Expr a : argList) {
            System.out.println(a);
        }

        if (firstExpr.equals(secExpr)) {
            System.out.println("They are same");
        } else {
            System.out.println("They are different");
        }

        System.out.println("\nEnd of Example equal_test_case\n");

    }

    public void time_simplified_case() {
        System.out.println("Running Example time_simplified_case: Time simplification case\n");
        Context z3ctx = new Context();
        IntExpr field_name = (IntExpr) z3ctx.mkConst("META_deltat", z3ctx.getIntSort());
        IntNum minVal = z3ctx.mkInt(0);
        IntNum maxVal = z3ctx.mkInt(10);

        BoolExpr gtBool = z3ctx.mkGe(field_name, minVal);
        BoolExpr ltBool = z3ctx.mkLt(field_name, maxVal);

        BoolExpr baseCase = z3ctx.mkAnd(gtBool, ltBool);
        System.out.print("Expr: ");
        System.out.println(baseCase);
        System.out.print("Expr Simplified: ");
        System.out.println(baseCase.simplify());
        System.out.println("\nEnd of Example base_case\n");

    }

    public void location_base_case() {
        System.out.println("Running Example location_base_case: \n");
        Context z3ctx = new Context();
        IntExpr location_name = (IntExpr) z3ctx.mkConst("META_rho", z3ctx.getIntSort());

        BoolExpr l1 = MkMatch(z3ctx, location_name, "==", (IntExpr) z3ctx.mkConst("X", z3ctx.getIntSort()));
        BoolExpr l2 = MkMatch(z3ctx, location_name, "!=", (IntExpr) z3ctx.mkConst("X", z3ctx.getIntSort()));

        IntExpr field_name = (IntExpr) z3ctx.mkConst("event_type", z3ctx.getIntSort());
        IntNum e1 = z3ctx.mkInt(1);
        IntNum e2 = z3ctx.mkInt(2);

        BoolExpr equalityExprFirst = MkMatch(z3ctx, field_name, "==", e1);
        BoolExpr unEqualityExprFirst = MkMatch(z3ctx, field_name, "!=", e2);

        BoolExpr equalityExprSecond = MkMatch(z3ctx, field_name, "==", e2);
        BoolExpr unEqualityExprSecond = MkMatch(z3ctx, field_name, "!=", e1);

        BoolExpr firstExpr = z3ctx.mkNot(z3ctx.mkAnd(l2, equalityExprFirst));
        BoolExpr secondExpr = z3ctx.mkNot(z3ctx.mkAnd(l2, equalityExprSecond));
        BoolExpr thirdExpr = z3ctx.mkNot(z3ctx.mkAnd(l1, equalityExprFirst));

        BoolExpr baseCase = z3ctx.mkAnd(z3ctx.mkAnd(firstExpr, secondExpr), thirdExpr);
        System.out.print("Expr: ");
        System.out.println(baseCase);
        System.out.print("Expr Simplified: ");
        System.out.println(baseCase.simplify());

        System.out.println("\nEnd of Example location_base_case\n");

    }

    public void location_implication_case() {
        System.out.println("Running Example location_implication_case: If we add antecedent ((event_type == 1) or (event_type == 2) = True) \n");
        Context z3ctx = new Context();
        IntExpr location_name = (IntExpr) z3ctx.mkConst("META_rho", z3ctx.getIntSort());

        BoolExpr l1 = MkMatch(z3ctx, location_name, "==", (IntExpr) z3ctx.mkConst("X", z3ctx.getIntSort()));
        BoolExpr l2 = MkMatch(z3ctx, location_name, "!=", (IntExpr) z3ctx.mkConst("X", z3ctx.getIntSort()));

        IntExpr field_name = (IntExpr) z3ctx.mkConst("event_type", z3ctx.getIntSort());
        IntNum e1 = z3ctx.mkInt(1);
        IntNum e2 = z3ctx.mkInt(2);

        BoolExpr equalityExprFirst = MkMatch(z3ctx, field_name, "==", e1);
        BoolExpr unEqualityExprFirst = MkMatch(z3ctx, field_name, "!=", e2);

        BoolExpr equalityExprSecond = MkMatch(z3ctx, field_name, "==", e2);
        BoolExpr unEqualityExprSecond = MkMatch(z3ctx, field_name, "!=", e1);

        BoolExpr firstExpr = z3ctx.mkNot(z3ctx.mkAnd(l2, equalityExprFirst));
        BoolExpr secondExpr = z3ctx.mkNot(z3ctx.mkAnd(l2, equalityExprSecond));
        BoolExpr thirdExpr = z3ctx.mkNot(z3ctx.mkAnd(l1, equalityExprFirst));

        BoolExpr implication = z3ctx.mkEq(z3ctx.mkOr(equalityExprFirst, equalityExprSecond), z3ctx.mkTrue());

        BoolExpr baseCase = z3ctx.mkImplies(implication, z3ctx.mkAnd(z3ctx.mkAnd(firstExpr, secondExpr), thirdExpr));
        System.out.print("Expr: ");
        System.out.println(baseCase);
        System.out.print("Expr Simplified: ");
        System.out.println(baseCase.simplify());

        System.out.println("\nEnd of Example location_implication_case\n");

    }

    public void location_sat_case() {
        System.out.println("Running Example location_sat_case: If we check location is X using SAT solver\n");
        Context z3ctx = new Context();
        IntExpr location_name = (IntExpr) z3ctx.mkConst("META_rho", z3ctx.getIntSort());

        BoolExpr l1 = MkMatch(z3ctx, location_name, "==", (IntExpr) z3ctx.mkConst("X", z3ctx.getIntSort()));
        BoolExpr l2 = MkMatch(z3ctx, location_name, "!=", (IntExpr) z3ctx.mkConst("X", z3ctx.getIntSort()));

        IntExpr field_name = (IntExpr) z3ctx.mkConst("event_type", z3ctx.getIntSort());
        IntNum e1 = z3ctx.mkInt(1);
        IntNum e2 = z3ctx.mkInt(2);

        BoolExpr equalityExprFirst = MkMatch(z3ctx, field_name, "==", e1);
        BoolExpr unEqualityExprFirst = MkMatch(z3ctx, field_name, "!=", e2);

        BoolExpr equalityExprSecond = MkMatch(z3ctx, field_name, "==", e2);
        BoolExpr unEqualityExprSecond = MkMatch(z3ctx, field_name, "!=", e1);

        BoolExpr firstExpr = z3ctx.mkNot(z3ctx.mkAnd(l2, equalityExprFirst));
        BoolExpr secondExpr = z3ctx.mkNot(z3ctx.mkAnd(l2, equalityExprSecond));
        BoolExpr thirdExpr = z3ctx.mkNot(z3ctx.mkAnd(l1, equalityExprFirst));

        BoolExpr baseCase = z3ctx.mkAnd(z3ctx.mkAnd(firstExpr, secondExpr), thirdExpr);
        System.out.print("Expr: ");
        System.out.println(baseCase);
        System.out.print("Expr Simplified: ");
        System.out.println(baseCase.simplify());

        Solver s = z3ctx.mkSolver();
        s.add(l1);
        s.add(baseCase);
        System.out.print("Checking if location could be X: ");
        System.out.println(s.check());

        s = z3ctx.mkSolver();
        s.add(l2);
        s.add(baseCase);
        System.out.print("Checking if location is NOT X: ");
        System.out.println(s.check());

        System.out.println("\nEnd of Example location_sat_case\n");

    }

    public void ryan_case() {
        System.out.println("Running Example ryan_case: \n");
        Context z3ctx = new Context();
        IntExpr location_name = (IntExpr) z3ctx.mkConst("META_rho", z3ctx.getIntSort());

        BoolExpr l1 = MkMatch(z3ctx, location_name, "==", (IntExpr) z3ctx.mkConst("X", z3ctx.getIntSort()));
        BoolExpr l2 = MkMatch(z3ctx, location_name, "!=", (IntExpr) z3ctx.mkConst("X", z3ctx.getIntSort()));

        IntExpr field_name = (IntExpr) z3ctx.mkConst("event_type", z3ctx.getIntSort());
        IntNum e1 = z3ctx.mkInt(1); // ADD
        IntNum e2 = z3ctx.mkInt(2); // REMOVE

        BoolExpr addExpr = MkMatch(z3ctx, field_name, "==", e1);
        BoolExpr notAddExpr = MkMatch(z3ctx, field_name, "!=", e1);

        BoolExpr removeExpr = MkMatch(z3ctx, field_name, "==", e2);
        BoolExpr notRemoveExpr = MkMatch(z3ctx, field_name, "!=", e2);


        BoolExpr notRemoveNotX = z3ctx.mkNot(z3ctx.mkAnd(removeExpr, l2));
        BoolExpr notAddX = z3ctx.mkNot(z3ctx.mkAnd(addExpr, l1));
        BoolExpr notAddNotX = z3ctx.mkNot(z3ctx.mkAnd(addExpr, l2));

        System.out.print("NOT (Add X): ");
        System.out.println(notAddX);

        System.out.print(" NOT (Add NOT X): ");
        System.out.println(notAddNotX);

        System.out.print("NOT (Remove NOT X): ");
        System.out.println(notRemoveNotX);


        BoolExpr transition = z3ctx.mkAnd(z3ctx.mkAnd(notAddX, notAddNotX), notRemoveNotX);

        System.out.print("\nTransition: ");
        System.out.println(transition);

        // BoolExpr filter = z3ctx.mkAnd(addExpr, removeExpr);

        BoolExpr filter = z3ctx.mkEq(z3ctx.mkOr(addExpr, removeExpr), z3ctx.mkTrue());

        System.out.print("\nFilter: ");
        System.out.println(filter);

        BoolExpr implicationX = z3ctx.mkImplies(z3ctx.mkAnd(transition, filter), l1);

        System.out.print("\nImplication location is X: ");
        System.out.println(implicationX.simplify());

        Solver s1 = z3ctx.mkSolver();
        s1.add(implicationX);
        System.out.print("\nFind satisfying solution location could be X: ");
        System.out.println(s1.check());

        Solver s2 = z3ctx.mkSolver();
        s2.add(z3ctx.mkNot(implicationX));
        System.out.print("Find satisfying solution with NOT implicationX: ");
        System.out.println(s2.check());

        BoolExpr implicationNotX = z3ctx.mkImplies(z3ctx.mkAnd(transition, filter), l2);

        Solver s3 = z3ctx.mkSolver();
        s3.add(implicationNotX);
        System.out.print("Find satisfying solution location could be NOT X: ");
        System.out.println(s3.check());

        Solver s4 = z3ctx.mkSolver();
        s4.add(z3ctx.mkNot(implicationNotX));
        System.out.print("Find satisfying solution with NOT implicationNotX: ");
        System.out.println(s4.check());

        System.out.println("\nEnd of Example ryan_case\n");

    }

    public void run_all_cases() {
        // base_case();
        // location_base_case();
        // location_implication_case();
        // location_sat_case();
        // ryan_case();
        // time_simplified_case();
        equal_test_case();
    }

    private static BoolExpr MkMatch(Context z3ctx, ArithExpr left, String op, ArithExpr right) {
        BoolExpr pred;
        switch (op) {
        case "==":
            pred = z3ctx.mkEq(left, right);
            return pred;
        case "!=":
            pred = z3ctx.mkNot(z3ctx.mkEq(left, right));
            return pred;
        case "<":
            pred = z3ctx.mkLt(left, right);
            return pred;
        case ">":
            pred = z3ctx.mkGt(left, right);
            return pred;
        case "<=":
            pred = z3ctx.mkLe(left, right);
            return pred;
        case ">=":
            pred = z3ctx.mkGe(left, right);
            return pred;
        case ":=":
            // Ignore these as they are always true
            return z3ctx.mkTrue();
        }
        
        throw new NotImplementedException("Unknown operator: " + op);
    }

}