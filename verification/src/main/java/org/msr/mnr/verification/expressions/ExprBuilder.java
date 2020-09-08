package org.msr.mnr.verification.expressions;

import java.util.HashMap;
import java.util.HashSet;

import org.antlr.v4.runtime.tree.ParseTree;
import org.msr.mnr.verification.utils.ParseIntArray;
import org.msr.verification.SimpleSMTLIBParser.ArithExpressionContext;
import org.msr.verification.SimpleSMTLIBParser.BoolExpressionContext;
import org.msr.verification.SimpleSMTLIBParser.StartBoolContext;
import org.msr.verification.SimpleSMTLIBParser.VarBindingContext;


public class ExprBuilder {
    private static boolean isValid(ParseTree ctx) {
        return ctx != null;
    }

    public static BoolExpr buildBoolExpr(StartBoolContext ctx, HashSet<String> locationReferences,
            HashMap<String, String> variableComparisons) {
        return buildBoolExpr(ctx.boolExpression(), new HashMap<String, BoolExpr>(), locationReferences,
                variableComparisons);
    }

    /**
     * boolExpression
     *      : ParOpen boolOp boolExpression+ ParClose
     *      | ParOpen compOp arithExpression arithExpression ParClose
     *      | ParOpen GRW_Let ParOpen var_binding+ ParClose term ParClose
     *      | boundVar
     *      | PS_True
     *      | PS_False
     *      ;
     */

    public static BoolExpr buildBoolExpr(BoolExpressionContext ctx,
            HashMap<String, BoolExpr> bindings, HashSet<String> locationReferences,
            HashMap<String, String> variableComparisons) {
        BoolExpr ret = null;
        if (isValid(ctx.boolOp())) {
            // ParOpen boolOp term+ ParClose
            ret = buildBoolExpr(ctx.boolExpression(0), bindings, locationReferences,
                    variableComparisons);
            if (ctx.boolOp().getText().equals("and")) {
                for (int i = 1; i < ctx.boolExpression().size(); ++i) {
                    ret = new AndExpr(ret, buildBoolExpr(ctx.boolExpression(i), bindings,
                            locationReferences, variableComparisons));
                }
            } else if (ctx.boolOp().getText().equals("or")) {
                for (int i = 1; i < ctx.boolExpression().size(); ++i) {
                    ret = new OrExpr(ret, buildBoolExpr(ctx.boolExpression(i), bindings,
                            locationReferences, variableComparisons));
                }
            } else if (ctx.boolOp().getText().equals("not")) {
                assert (ctx.boolExpression().size() == 1);
                ret.negated = true;
            }
        } else if (isValid(ctx.compOp())) {
            // ParOpen compOp expression expression ParClose
            Object left = buildArithExpr(ctx.arithExpression(0));
            Object right = buildArithExpr(ctx.arithExpression(1));

            switch (ctx.compOp().getText()) {
            case "=":
                // Pass constraints in case there are any
                ret = new EqExpr(left, right, locationReferences, variableComparisons);
                break;
            case "<":
                ret = new LtExpr(left, right);
                break;
            case "<=":
                ret = new LeExpr(left, right);
                break;
            case ">":
                ret = new GtExpr(left, right);
                break;
            case ">=":
                ret = new GeExpr(left, right);
            }
        } else if (isValid(ctx.GRW_Let())) {
            // ParOpen GRW_Let ParOpen var_binding+ ParClose term ParClose
            @SuppressWarnings("unchecked")
            HashMap<String, BoolExpr> newBindings = (HashMap<String, BoolExpr>) bindings.clone();
            for (VarBindingContext vb: ctx.varBinding()) {
                buildVarBinding(vb, bindings, newBindings, locationReferences, variableComparisons);
            }
            
            assert(ctx.boolExpression().size() == 1);
            ret = buildBoolExpr(ctx.boolExpression(0), newBindings, locationReferences, variableComparisons);
        } else if (isValid(ctx.boundVar())) {
            // boundVar
            ret = bindings.get(ctx.boundVar().getText());
        } else if (isValid(ctx.PS_True())) {
            // PS_True
            ret = new TrueExpr();
        } else if (isValid(ctx.PS_False()) ) {
            // PS_False
            ret = new TrueExpr();
            ret.toggleNegated();
        } else {
            throw new RuntimeException("Unknown boolExpression syntax");
        }

        // System.out.print("Return Boool: ");
        // System.out.println(ret);
        
        return ret;
    }
    
    /**
     *   arithExpression
     *      : ParOpen GRW_Ite boolExpression arithExpression arithExpression ParClose
     *      | ParOpen arithOp arithExpression+ ParClose
     *      | terminal
     *      ;
     */
    public static Object buildArithExpr(ArithExpressionContext ctx) {
        Object ret;
        if (isValid(ctx.GRW_Ite())) {
            // ParOpen GRW_Ite boolExpression arithExpression arithExpression ParClose
            BoolExpr condition = buildBoolExpr(ctx.boolExpression(), new HashMap<String, BoolExpr>(), null, null);
            Object left = buildArithExpr(ctx.arithExpression(0));
            Object right = buildArithExpr(ctx.arithExpression(1));

            ret = new IteExpr(condition, left, right);

        } else if (isValid(ctx.arithOp())) {
            // ParOpen arithOp arithExpression+ ParClose
            if (ctx.arithExpression().size() == 1) {
                assert(ctx.arithOp().getText().equals("-"));
                Object only = buildArithExpr(ctx.arithExpression(0));
                ret = new NegativeContext(only);
            } else if (ctx.arithExpression().size() == 2) {
                ret = buildArithExpr(ctx.arithExpression(0));

                for (int i = 1; i < ctx.arithExpression().size(); ++i) {
                    Object right = buildArithExpr(ctx.arithExpression(i));

                    if (ctx.arithOp().getText().equals("+")) {
                        if (right instanceof NegativeContext) {
                            NegativeContext negatedRight = (NegativeContext)right;
                            ret = new SubExpr(ret, negatedRight.value);
                        } else {
                            ret = new AddExpr(ret, right);
                        }
                    } else if (ctx.arithOp().getText().equals("-")) {
                        ret = new SubExpr(ret, right);
                    } else if (ctx.arithOp().getText().equals("*")) {
                        if (ret instanceof NegativeContext) {
                            NegativeContext negatedLeft = (NegativeContext)ret;
                            assert(negatedLeft.value instanceof int[]);
                            int[] val = (int[])negatedLeft.value;
                            assert(ParseIntArray.printString(val).equals("1"));
                            ret = new NegativeContext(right);
                        } else {
                            ret = new MulExpr(ret, right);
                        }
                    } else if (ctx.arithOp().getText().equals("/")) {
                        ret = new DivExpr(ret, right);
                    } else {
                        throw new RuntimeException("Unknown arithExpression syntax");
                    }
                }
            } else {
                throw new RuntimeException("Unknown arithExpression syntax");
            }
        } else if (isValid(ctx.terminal())) {
            if (isValid(ctx.terminal().UndefinedSymbol())) {
                ret = ctx.terminal().getText();
            } else {
                ret = ParseIntArray.fromLong(Long.parseLong(ctx.terminal().getText()));
            }
        } else {
            throw new RuntimeException("Unknown arithExpression syntax");
        }

        return ret;
    }

    /**
     *   varBinding
     *      : ParOpen UndefinedSymbol boolExpression ParClose
     *      ;
     */
    private static void buildVarBinding(VarBindingContext ctx,
            HashMap<String, BoolExpr> oldBindings, HashMap<String, BoolExpr> newBindings,
            HashSet<String> locationReferences, HashMap<String, String> variableComparisons) {
        newBindings.put(ctx.UndefinedSymbol().getText(), buildBoolExpr(ctx.boolExpression(),
                oldBindings, locationReferences, variableComparisons));
    }
}