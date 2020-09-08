package main.java.generateSFA;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.Model;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;

import theory.BooleanAlgebra;
import utilities.Pair;

public class EventSolver extends BooleanAlgebra<BoolExpr, HashMap<String, Integer>> {
    private Context z3ctx;
    private HashMap<String, IntExpr> symbols;
    private HashMap<String, IntExpr> locations;
    private HashSet<BoolExpr> predicates;
    private BoolExpr disjointLocationExpr;

    public EventSolver() {
        z3ctx = new Context();
        symbols = new HashMap<>();
        locations = new HashMap<>();
        predicates = new HashSet<>();
        disjointLocationExpr = z3ctx.mkTrue();

        // Special symbol for location
        IntExpr rho = (IntExpr) z3ctx.mkConst("META_rho", z3ctx.getIntSort());
        symbols.put("META_rho", rho);
        // Special symbol for time
        IntExpr deltat = (IntExpr) z3ctx.mkConst("META_deltat", z3ctx.getIntSort());
        symbols.put("META_deltat", deltat);

    }

    public IntExpr getLocationExpr() {
        return symbols.get("META_rho");
    }

    public BoolExpr specifyLocation(IntExpr location, BoolExpr e, boolean opposite) {
        Expr ret = e;
        for (IntExpr ie : locations.values()) {
            if (ie == location && !opposite) {
                ret = ret.substitute(z3ctx.mkEq(getLocationExpr(), ie), z3ctx.mkTrue());
            } else if (ie != location && !opposite) {
                ret = ret.substitute(z3ctx.mkEq(getLocationExpr(), ie), z3ctx.mkFalse());
            } else if (ie == location && opposite) {
                ret = ret.substitute(z3ctx.mkEq(getLocationExpr(), ie), z3ctx.mkFalse());
            } else {
                ret = ret.substitute(z3ctx.mkEq(getLocationExpr(), ie), z3ctx.mkTrue());
            }
        }

        return (BoolExpr) ret;
    }

    public BoolExpr MkMatch(ArithExpr left, String op, ArithExpr right) {
        BoolExpr pred;
        switch (op) {
        case "==":
            pred = z3ctx.mkEq(left, right);
            predicates.add(pred);
            return pred;
        case "!=":
            pred = z3ctx.mkNot(z3ctx.mkEq(left, right));
            predicates.add(pred);
            return pred;
        case "<":
            pred = z3ctx.mkLt(left, right);
            predicates.add(pred);
            return pred;
        case ">":
            pred = z3ctx.mkGt(left, right);
            predicates.add(pred);
            return pred;
        case "<=":
            pred = z3ctx.mkLe(left, right);
            predicates.add(pred);
            return pred;
        case ">=":
            pred = z3ctx.mkGe(left, right);
            predicates.add(pred);
            return pred;
        }
        
        throw new NotImplementedException("Unknown operator: " + op);
    }

    public Context getContext() {
        return z3ctx;
    }

    public HashMap<String, IntExpr> getLocations() {
        return locations;
    }

    public BoolExpr getOther() {
        BoolExpr ret = z3ctx.mkTrue();
        for (BoolExpr pred : predicates) {
            ret = z3ctx.mkAnd(ret, z3ctx.mkNot(pred));
        }
        return ret;
    }

    public BoolExpr MkMatch(String left, String op, String right) {
        IntExpr e1;

        if (symbols.containsKey(left)) {
            e1 = symbols.get(left);
        } else {
            e1 = (IntExpr) z3ctx.mkConst(left, z3ctx.getIntSort());
            symbols.put(left, e1);
        }

        IntExpr e2;


        if (symbols.containsKey(right)) {
            e2 = symbols.get(right);
        } else {
            e2 = (IntExpr) z3ctx.mkConst(right, z3ctx.getIntSort());
            symbols.put(right, e2);
        }

        if (left.equals("META_rho") && (!(locations.containsKey(right)))) {
            locations.put(right, e2);
        }

        return MkMatch(e1, op, e2);
    }
    
    public BoolExpr MkMatch(Integer left, String op, String right) {
        IntNum e1 = z3ctx.mkInt(left);

        IntExpr e2;
        if (symbols.containsKey(right)) {
            e2 = symbols.get(right);
        } else {
            e2 = (IntExpr) z3ctx.mkConst(right, z3ctx.getIntSort());
            symbols.put(right, e2);
        }

        return MkMatch(e1, op, e2);
    }
    
    public BoolExpr MkMatch(String left, String op, Integer right) {
        IntExpr e1;
        if (symbols.containsKey(left)) {
            e1 = symbols.get(left);
        } else {
            e1 = (IntExpr) z3ctx.mkConst(left, z3ctx.getIntSort());
            symbols.put(left, e1);
        }
        IntNum e2 = z3ctx.mkInt(right);
        return MkMatch(e1, op, e2);
    }

    public ArithExpr MkBinOp(ArithExpr left, String op, ArithExpr right) {
        switch (op) {
        case "+":
            return z3ctx.mkAdd(left, right);
        case "-":
            return z3ctx.mkSub(left, right);
        case "*":
            return z3ctx.mkMul(left, right);
        case "/":
            return z3ctx.mkDiv(left, right);

        }
        
        throw new NotImplementedException("Unknown operator: " + op);
    }

    public ArithExpr MkBinOp(String left, String op, String right) {
        IntExpr e1;

        if (symbols.containsKey(left)) {
            e1 = symbols.get(left);
        } else {
            e1 = (IntExpr) z3ctx.mkConst(left, z3ctx.getIntSort());
            symbols.put(left, e1);
        }

        IntExpr e2;


        if (symbols.containsKey(right)) {
            e2 = symbols.get(right);
        } else {
            e2 = (IntExpr) z3ctx.mkConst(right, z3ctx.getIntSort());
            symbols.put(right, e2);
        }

        if (left.equals("META_rho") && (!(locations.containsKey(right)))) {
            locations.put(right, e2);
        }

        return MkBinOp(e1, op, e2);
    }
    
    public ArithExpr MkBinOp(Integer left, String op, String right) {
        IntNum e1 = z3ctx.mkInt(left);

        IntExpr e2;
        if (symbols.containsKey(right)) {
            e2 = symbols.get(right);
        } else {
            e2 = (IntExpr) z3ctx.mkConst(right, z3ctx.getIntSort());
            symbols.put(right, e2);
        }

        return MkBinOp(e1, op, e2);
    }
    
    public ArithExpr MkBinOp(String left, String op, Integer right) {
        IntExpr e1;
        if (symbols.containsKey(left)) {
            e1 = symbols.get(left);
        } else {
            e1 = (IntExpr) z3ctx.mkConst(left, z3ctx.getIntSort());
            symbols.put(left, e1);
        }
        IntNum e2 = z3ctx.mkInt(right);
        return MkBinOp(e1, op, e2);
    }

    public ArithExpr MkSingleArith(Integer val) {
        IntNum e1 = z3ctx.mkInt(val);
        return e1;
    }

    public ArithExpr MkSingleArith(String val) {
        IntExpr e2;
        if (symbols.containsKey(val)) {
            e2 = symbols.get(val);
        } else {
            e2 = (IntExpr) z3ctx.mkConst(val, z3ctx.getIntSort());
            symbols.put(val, e2);
        }
        return e2;
    }

    public void registerLocation(String s) {
        if (!locations.containsKey(s)) {
            IntExpr e = (IntExpr) z3ctx.mkConst(s, z3ctx.getIntSort());
            symbols.put(s, e);

            for (IntExpr le : locations.values()) {
                BoolExpr notequal = z3ctx.mkNot(z3ctx.mkEq(le, e));
                disjointLocationExpr = z3ctx.mkAnd(disjointLocationExpr, notequal);
            }

            locations.put(s, e);
        }
    }

    public Expr MkIte(BoolExpr condition, Expr e1, Expr e2) {
        return z3ctx.mkITE(condition, e1, e2);
    }

    /**
     * @return the predicate accepting only <code>s</code>
     */
    @Override
    public BoolExpr MkAtom(HashMap<String, Integer> s) {
        BoolExpr ret = z3ctx.mkTrue();
        for (Map.Entry<String, Integer> e : s.entrySet()) {
            IntExpr x = symbols.get(e.getKey());
            IntNum val = z3ctx.mkInt(e.getValue());
            BoolExpr equality = z3ctx.mkEq(x, val);
            ret = z3ctx.mkAnd(ret, equality);
        }

        return ret;
    }

    /**
     * @return the complement of <code>p</code>
     */
    @Override
    public BoolExpr MkNot(BoolExpr p) {
        return z3ctx.mkNot(p);
    }

    public BoolExpr MkEq(Expr i1, Expr i2) {
        return z3ctx.mkEq(i1, i2);
    }
    
    /**
     * @return the disjunction of the predicates in <code>pset</code>
     */
    @Override
    public BoolExpr MkOr(Collection<BoolExpr> pset) {
        BoolExpr ret = z3ctx.mkFalse();
        for (BoolExpr e : pset) {
            ret = z3ctx.mkOr(ret, e);
        }

        return ret;
    }

    /**
     * @return the predicate <code>p1</code> or <code>p2</code>
     */
    @Override
    public BoolExpr MkOr(BoolExpr p1, BoolExpr p2) {
        return z3ctx.mkOr(p1, p2);
    }

    /**
     * @return the conjunction of the predicates in <code>pset</code>
     */
    @Override
    public BoolExpr MkAnd(Collection<BoolExpr> pset) {
        BoolExpr ret = z3ctx.mkTrue();
        for (BoolExpr e : pset) {
            ret = z3ctx.mkAnd(ret, e);
        }

        return ret;
    }

    /**
     * @return the predicate <code>p1</code> and <code>p2</code>
     */
    @Override
    public BoolExpr MkAnd(BoolExpr p1, BoolExpr p2) {
        return z3ctx.mkAnd(p1, p2);
    }

    /**
    * @return <code>p1</code> implies <code>p2</code>
    */
    public BoolExpr MkImplies(BoolExpr p1, BoolExpr p2) {
        return z3ctx.mkImplies(p1, p2);
    }

    /**
     * @return <code>p1</code> iff <code>p2</code>
     */
     public BoolExpr MkIff(BoolExpr p1, BoolExpr p2) {
         return z3ctx.mkIff(p1, p2);
     }

    /**
     * @return the predicate true
     */
    @Override
    public BoolExpr True() {
        return z3ctx.mkTrue();
    }

    /**
     * @return the predicate false
     */
    @Override
    public BoolExpr False() {
        return z3ctx.mkFalse();
    }

    public ArithExpr MkUnaryMinus(ArithExpr a1) {
        return z3ctx.mkUnaryMinus(a1);
    }

    /**
     * @return true iff <code>p1</code> and <code>p2</code> are equivalent
     */
    @Override
    public boolean AreEquivalent(BoolExpr p1, BoolExpr p2) {
        return !IsSatisfiable(z3ctx.mkNot(z3ctx.mkIff(p1, p2)));
    }

    /**
     * @return true iff <code>p1</code> is satisfiable
     */
    @Override
    public boolean IsSatisfiable(BoolExpr p1) {
        Solver s = z3ctx.mkSolver();
        s.add(p1);
        s.add(disjointLocationExpr);
        return s.check() == Status.SATISFIABLE;
    }


    public boolean IsUnSAT(BoolExpr p1) {
        Solver s = z3ctx.mkSolver();
        s.add(p1);
        s.add(disjointLocationExpr);
        return s.check() == Status.UNSATISFIABLE;
    }

    /**
     * @return true iff <code>el</code> is a model of <code>p1</code>
     */
    @Override
    public boolean HasModel(BoolExpr p1, HashMap<String, Integer> el) {
        BoolExpr model = MkAtom(el);
        Solver s = z3ctx.mkSolver();
        s.add(p1);
        s.add(disjointLocationExpr);
        s.add(model);
        return s.check() == Status.SATISFIABLE; // TODO: original returned true if not possible. why?
    }

    /**
     * @return true iff <code>(el1,el2)</code> is a model of a binary predicate <code>p1</code> (used for SVPA)
     */
    @Override
    public boolean HasModel(BoolExpr p1, HashMap<String, Integer> el1, HashMap<String, Integer> el2) {
        throw new UnsupportedOperationException("SATBooleanAlgebra.HasModel(_,_,_) is not implemented");
    }

    /**
     * @return a witness of the predicate <code>p1</code> if satisfiable, null otherwise
     */
    @Override
    public HashMap<String, Integer> generateWitness(BoolExpr p1) {
        Solver s = z3ctx.mkSolver();
        s.add(p1);

        if (s.check() != Status.SATISFIABLE) {
            return null;
        }

        Model m = s.getModel();
        HashMap<String, Integer> witness = new HashMap<>();
        for (Map.Entry<String, IntExpr> e : symbols.entrySet()) {
            Expr val = m.evaluate(e.getValue(), false);
            System.out.println("VIN" + val + "|" + val.toString());
            // TODO
            witness.put(e.getKey(), Integer.parseInt(val.toString()));
        }

        return witness;
    }

    /**
     * @return a pair witness of the binary predicate <code>p1</code> if satisfiable, null otherwise
     */
    @Override
    public Pair<HashMap<String, Integer>, HashMap<String, Integer>> generateWitnesses(BoolExpr p1) {
        throw new UnsupportedOperationException("SATBooleanAlgebra.generateWitnesses is not implemented");
    }
}