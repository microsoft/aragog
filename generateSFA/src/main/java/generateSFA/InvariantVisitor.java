package main.java.generateSFA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.apache.commons.lang3.NotImplementedException;
import org.sat4j.specs.TimeoutException;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.Expr;

import automata.sfa.SFA;
import automata.sfa.SFAInputMove;
import automata.sfa.SFAMove;
import generateSFA.InvariantParser.Compare_opContext;
import generateSFA.InvariantParser.Equality_opContext;
import generateSFA.InvariantParser.EventContext;
import generateSFA.InvariantParser.Event_matchContext;
import generateSFA.InvariantParser.EventsContext;
import generateSFA.InvariantParser.Events_sequenceContext;
import generateSFA.InvariantParser.Events_termContext;
import generateSFA.InvariantParser.Field_matchContext;
import generateSFA.InvariantParser.Filter_matchContext;
import generateSFA.InvariantParser.Field_termContext;
import generateSFA.InvariantParser.Filter_matchesContext;
import generateSFA.InvariantParser.Location_matchContext;
import generateSFA.InvariantParser.NameContext;
import generateSFA.InvariantParser.Regex_opContext;
import generateSFA.InvariantParser.TransformationContext;
import generateSFA.InvariantParser.ValueContext;
import generateSFA.InvariantParser.Field_expressionContext;
import generateSFA.InvariantParser.Field_expression2Context;
import generateSFA.InvariantParser.Var_refContext;
import generateSFA.InvariantParser.Node_matchContext;
import generateSFA.InvariantParser.Midnode_matchContext;
import generateSFA.InvariantParser.Leaf_matchContext;
import generateSFA.InvariantParser;
import generateSFA.InvariantParserBaseVisitor;

public class InvariantVisitor extends InvariantParserBaseVisitor<SFA<BoolExpr, HashMap<String, Integer>>> {
    private EventSolver eventSolver;
    private HashMap<String, Integer> constantMap;

    public ArrayList<String> groupBy;
    public BoolExpr filter;
    public HashSet<String> locationSet;
    public HashMap<String, HashSet<VarValues>> variableMap;
    public HashMap<String, Expr> functionMap;
    private int flag;
    private int valType;

    public InvariantVisitor(EventSolver eventSolver, HashMap<String, Integer> constantMap) {
        super();
        this.eventSolver = eventSolver;
        this.constantMap = constantMap;

        locationSet = new HashSet<String>();
        variableMap = new HashMap<String, HashSet<VarValues>>();

        groupBy = new ArrayList<String>();
        // Start with the predicate True so we can just AND any encountered filters
        filter = eventSolver.True();
        functionMap = new HashMap<>();
        flag = 0;
        valType = -1;
    }

    /**
     * policy
     *      :   (transformation)+ TILDA events_sequence EOF
     *      ;
     */
    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitPolicy(InvariantParser.PolicyContext ctx) {
        for (TransformationContext t : ctx.transformation()) {
            visitTransformation(t);
        }
        return visitEvents_sequence(ctx.events_sequence());
    }

    /**
     * transformation
     *      : GROUPBY LPAREN fields RPAREN
     *      | FILTER LPAREN filter_matches RPAREN
     *      | MAP LPAREN field_expression COMMA name RPAREN
     *      ;
     */
    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitTransformation(TransformationContext ctx) {
        if (ctx.GROUPBY() != null) {
            for (NameContext n : ctx.fields().name()) {
                groupBy.add(n.getText());
            }
        } else if (ctx.FILTER() != null) {
            BoolExpr newFilter = visitFilter_matchesExpr(ctx.filter_matches());
            filter = (BoolExpr) eventSolver.MkAnd(filter, newFilter).simplify();
        } else if (ctx.MAP() != null) {
            Expr newMap = visitField_expressionExpr(ctx.field_expression());
            functionMap.put(ctx.getChild(4).getText(), newMap);
            System.out.println(newMap);
        } else {
            throw new NotImplementedException("visitTransformation");
        }
        
        return null;
    }

    /**
     * field_expression
     *      : (PLUS | MINUS)? field_expression2 ((PLUS | MINUS) field_expression2)*
     *      | field_expression compare_op field_expression QUESTION field_expression COLON field_expression 
     *      ;
     */

    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitField_expression(Field_expressionContext ctx) {
        throw new NotImplementedException("visitField_expression");
    }

    public Expr visitField_expressionExpr(Field_expressionContext ctx) {
        if (ctx.compare_op() == null) {
            // System.out.println("going in if");
            ArithExpr expression;
            int opPos = 1;
            if (ctx.getChild(0).getText().equals("-")) {
                expression = eventSolver.MkUnaryMinus(visitField_expression2Expr(ctx.field_expression2(0)));
                opPos++;
            } else if (ctx.getChild(0).getText().equals("+")) {
                expression = visitField_expression2Expr(ctx.field_expression2(0));
                opPos++;
            } else {
                expression = visitField_expression2Expr(ctx.field_expression2(0));
            }
            for (int i = 1; i < ctx.field_expression2().size(); i++) {
                expression = eventSolver.MkBinOp(expression, ctx.getChild(opPos).getText(), (ArithExpr)visitField_expression2Expr(ctx.field_expression2(i)));
                opPos += 2;
            }
            return expression;

        } else if (ctx.getChild(0) instanceof Field_expressionContext) {
            ArithExpr leftExpr = (ArithExpr) visitField_expressionExpr(ctx.field_expression(0));
            String op = ctx.getChild(1).getText();
            ArithExpr rightExpr = (ArithExpr) visitField_expressionExpr(ctx.field_expression(1));

            // Symbols are already added in eventSolver as this branch cannot be a leaf.
            BoolExpr condition = eventSolver.MkMatch(leftExpr, op, rightExpr);
            Expr ifExpr = visitField_expressionExpr(ctx.field_expression(2));
            Expr elseExpr = visitField_expressionExpr(ctx.field_expression(3));

            return eventSolver.MkIte(condition, ifExpr, elseExpr);
        }
        throw new NotImplementedException("visitField_expressionExpr");
    }

    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitField_expression2(Field_expression2Context ctx) {
        throw new NotImplementedException("visitField_expression2");
    }

    /**
     * field_expression2
     *      : field_term ((STAR | DIVIDE) field_term)*
     *      ;
     */
    public ArithExpr visitField_expression2Expr(Field_expression2Context ctx) {

        ArithExpr ret = visitField_termExpr(ctx.field_term(0));
        int opPos = 1;
        for (int i = 1; i < ctx.field_term().size(); ++i) {
            ret = eventSolver.MkBinOp(ret, ctx.getChild(opPos).getText() ,visitField_termExpr(ctx.field_term(i)));
            opPos += 2;
        }
        return ret;
    }

    /**
     * field_term
     *      : name
     *      | value
     *      ;
     */
    public ArithExpr visitField_termExpr(Field_termContext ctx) {
        String val = ctx.getChild(0).getText();
        if (ctx.getChild(0) instanceof ValueContext) {
            return eventSolver.MkSingleArith(Integer.parseInt(val));
        } else if (ctx.getChild(0) instanceof NameContext) {
            return eventSolver.MkSingleArith(val);
        } else {
            throw new NotImplementedException("visitField_termExpr");
        }
    }

    /**
     * filter_matches
     *      : LPAREN filter_matches RPAREN
     *      | filter_matches OR filter_matches
     *      | filter_matches AND filter_matches
     *      | filter_match
     *      ;
     */
    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitFilter_matches(Filter_matchesContext ctx) {
        throw new NotImplementedException("visitFilter_matches");
    }
    public BoolExpr visitFilter_matchesExpr(Filter_matchesContext ctx) {
        if (ctx.LPAREN() != null) {
            return visitFilter_matchesExpr(ctx.filter_matches(0));
        } else if (ctx.OR() != null) {
            BoolExpr left = visitFilter_matchesExpr(ctx.filter_matches(0));
            BoolExpr right = visitFilter_matchesExpr(ctx.filter_matches(1));
            return eventSolver.MkOr(left, right);
        } else if (ctx.AND() != null) {
            BoolExpr left = visitFilter_matchesExpr(ctx.filter_matches(0));
            BoolExpr right = visitFilter_matchesExpr(ctx.filter_matches(1));
            return eventSolver.MkAnd(left, right);
        } else {
            return visitFilter_matchExpr(ctx.filter_match());
        }
    }
    
    /**
     * filter_match
     *      : name compare_op name
     *      | name compare_op value
     *      | value compare_op name
     *      ;
     */
    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitFilter_match(Filter_matchContext ctx) {
        throw new NotImplementedException("visitFilter_match");
    }

    public BoolExpr visitFilter_matchExpr(Filter_matchContext ctx) {
        String left = ctx.getChild(0).getText();
        String op = ctx.getChild(1).getText();
        String right = ctx.getChild(2).getText();

        if (ctx.getChild(0) instanceof ValueContext) {
            // right must be non-value
            return eventSolver.MkMatch(Integer.parseInt(left), op, right);
        } else if (constantMap.containsKey(left)) {
            // left is constant.  right must be non-value.
            assert(!constantMap.containsKey(right));
            return eventSolver.MkMatch(constantMap.get(left), op, right);
        } else if (ctx.getChild(2) instanceof ValueContext) {
            // left must be non-value
            if (right.contains("\"")) {
                return eventSolver.MkMatch(left, op, right);
            } else {
                return eventSolver.MkMatch(left, op, Integer.parseInt(right)); 
            }
            
        } else if (constantMap.containsKey(right)) {
            // right is constant. left must be non-value
            assert(!constantMap.containsKey(left));
            return eventSolver.MkMatch(left, op, constantMap.get(right));
        } else {
            return eventSolver.MkMatch(left, op, right);
        }
    }

    /**
     * events_sequence
     *      : (events)+
     *      ;
     */
    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitEvents_sequence(Events_sequenceContext ctx) {
        SFA<BoolExpr, HashMap<String, Integer>> ret = visitEvents(ctx.events(0));

        try {
            for (int i = 1; i < ctx.events().size(); ++i) {
                ret = SFA.concatenate(ret, visitEvents(ctx.events(i)), eventSolver);
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
            return null;
        }

        return ret;
    }

    /**
     * events
     *      : events_term (regex_op)?
     *      ;
     */
    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitEvents(EventsContext ctx) {
        SFA<BoolExpr, HashMap<String, Integer>> ret = visitEvents_term(ctx.events_term());

        try {
            if (ctx.regex_op() != null) {
                switch(ctx.regex_op().getText()) {
                case "*":
                    ret = SFA.star(ret, eventSolver);
                    break;
                case "+":
                    ret = SFA.concatenate(ret, SFA.star(ret, eventSolver), eventSolver);
                    break;
                case "?":
                    // build an SFA that only accepts the empty string
                    Collection<SFAMove<BoolExpr, HashMap<String, Integer>>> transitions = new LinkedList<>();
                    ret = SFA.union(ret, SFA.MkSFA(transitions, 0, Arrays.asList(0), eventSolver), eventSolver);
                }
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return ret;
    }

    // Shuffle events helpers
    private void shuffleSwap(ArrayList<SFA<BoolExpr, HashMap<String, Integer>>> input, int a, int b) {
        SFA<BoolExpr, HashMap<String, Integer>> tmp = input.get(a);
        input.set(a, input.get(b));
        input.set(b, tmp);
    }

    private ArrayList<SFA<BoolExpr, HashMap<String, Integer>>> shuffleRecursive(
            int n, ArrayList<SFA<BoolExpr, HashMap<String, Integer>>> elements) throws TimeoutException {
        ArrayList<SFA<BoolExpr, HashMap<String, Integer>>> ret = new ArrayList<>();
        if (n == 1) {
            SFA<BoolExpr, HashMap<String, Integer>> sfa = null;
            for (SFA<BoolExpr, HashMap<String, Integer>> e : elements) {
                if (sfa == null) {
                    sfa = e;
                } else {
                    sfa = SFA.concatenate(sfa, e, eventSolver);
                }
            }
            ret.add(sfa);
        } else {
            for(int i = 0; i < n-1; i++) {
                ret.addAll(shuffleRecursive(n - 1, elements));
                if(n % 2 == 0) {
                    shuffleSwap(elements, i, n-1);
                } else {
                    shuffleSwap(elements, 0, n-1);
                }
            }
            ret.addAll(shuffleRecursive(n - 1, elements));
        }

        return ret;
    }

    /**
     * Main visitor function to get an SFA for an events_term token
     * events_term
     *      : event
     *      | LPAREN events_sequence RPAREN
     *      | SHUFFLE LPAREN events_list RPAREN
     *      | CHOICE LPAREN events_list RPAREN
     *      ;
     */
    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitEvents_term(Events_termContext ctx) {
        SFA<BoolExpr, HashMap<String, Integer>> ret = null;

        try {
            if (ctx.SHUFFLE() != null) {
                // SHUFFLE LPAREN events_list RPAREN
                // events_list
                //      : events_sequence (COMMA events_sequence)*
                //      ;
                ArrayList<SFA<BoolExpr, HashMap<String, Integer>>> shuffleList = new ArrayList<>();
                for (Events_sequenceContext child : ctx.events_list().events_sequence()) {
                    shuffleList.add(visitEvents_sequence(child));
                }

                ArrayList<SFA<BoolExpr, HashMap<String, Integer>>> shuffleResult = shuffleRecursive(shuffleList.size(), shuffleList);

                for (SFA<BoolExpr, HashMap<String, Integer>> next : shuffleResult) {
                    if (ret == null) {
                        ret = next;
                    } else {
                        ret = SFA.union(ret, next, eventSolver);
                    }
                }
            } else if (ctx.CHOICE() != null) {
                // CHOICE LPAREN events_list RPAREN
                // events_list
                //      : events_sequence (COMMA events_sequence)*
                //      ;
                for (Events_sequenceContext child : ctx.events_list().events_sequence()) {
                    if (ret == null) {
                        ret = visitEvents_sequence(child);
                    } else {
                        SFA<BoolExpr, HashMap<String, Integer>> next = visitEvents_sequence(child);
                        ret = SFA.union(ret, next, eventSolver);
                    }
                }
            } else if (ctx.LPAREN() != null) {
                // LPAREN events_sequence RPAREN
                ret = visitEvents_sequence(ctx.events_sequence());
            } else {
                // event
                ret = visitEvent(ctx.event());
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * Main visitor function to get an SFA for an event token
     * event
     *      : BANG event
     *      | DOT AT location_match
     *      | LPAREN event_match RPAREN AT location_match
     *      ;
     */
    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitEvent(EventContext ctx) {
        // BoolExpr is is your transition conditions (event)
        BoolExpr eventExpr;

        if (ctx.BANG() != null) {
            eventExpr = eventSolver.MkNot(visitEventExpr(ctx.event()));
        } else {
            eventExpr = visitEventExpr(ctx);
        }

        try {
            // Create an SFA that only accepts the event
            Collection<SFAMove<BoolExpr, HashMap<String, Integer>>> transitions = new LinkedList<>();
            transitions.add(new SFAInputMove<BoolExpr, HashMap<String,Integer>>(0, 1, eventExpr));
            return SFA.MkSFA(transitions, 0, Arrays.asList(1), eventSolver);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BoolExpr visitEventExpr(EventContext ctx) {
        // get boolean expression for location
        BoolExpr ret = visitLocation_matchExpr(ctx.location_match());

        // if it's '.', then just and with true...
        if (ctx.DOT() != null) {
            return ret;
        }

        // get boolean expression for the actual event
        ret = eventSolver.MkAnd(ret, visitEvent_matchExpr(ctx.event_match()));
        return (BoolExpr) ret.simplify();
    }

    /**
     * BoolExpr visitor function for location_matches
     * event
     *      : ANY
     *      | (NOT)? name
     *      ;
     */
    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitLocation_match(Location_matchContext ctx) {
        throw new NotImplementedException("visitLocation_match");
    }
    private BoolExpr visitLocation_matchExpr(Location_matchContext ctx) {
        if (ctx.ANY() != null) {
            return eventSolver.True();
        }

        locationSet.add(ctx.name().IDENTIFIER().getText());

        String op = ctx.NOT() == null ? "==" : "!=";
        return eventSolver.MkMatch("META_rho", op, ctx.name().IDENTIFIER().getText());
    }

    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitEvent_match(Event_matchContext ctx) {
        throw new NotImplementedException("visitEvent_match");
    }
    private BoolExpr visitEvent_matchExpr(Event_matchContext ctx) {
        // field_match (COMMA field_match)*
        BoolExpr ret = eventSolver.True();
        for (Field_matchContext m : ctx.field_match()) {
            ret = eventSolver.MkAnd(ret, visitField_matchExpr(m));
        }

        return ret;
    }

    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitField_match(Field_matchContext ctx) {
        throw new NotImplementedException("visitField_match");
    }


    private BoolExpr visitField_matchExpr(Field_matchContext ctx) {
        String op = ctx.getChild(1).getText();
        ArithExpr left = visitNode_matchExpr(ctx.node_match(0));
        ArithExpr right = visitNode_matchExpr(ctx.node_match(1));
        if (flag == 1 && op.equals("==")) {
            HashSet<VarValues> currentVal = variableMap.get(left.toString());
            VarValues newVal = new VarValues();
            if (valType == 1) {
                newVal.valType = 1;
                newVal.strValue = right.toString();
            } else if (valType == 0) {
                newVal.valType = 0;
                newVal.intValue = Integer.parseInt(right.toString());
            }
            flag = 0;
        } else if (flag == 2) {
            flag = 0;
            valType = -1;
        }
        return eventSolver.MkMatch(left, op, right);
    }

    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitNode_match(Node_matchContext ctx) {
        throw new NotImplementedException("visitNode_match");
    }

    private ArithExpr visitNode_matchExpr(Node_matchContext ctx) {
        ArithExpr expression = visitMidnode_matchExpr(ctx.midnode_match(0));
        int opPos = 1;
        for (int i = 1; i < ctx.midnode_match().size(); i++) {
            expression = eventSolver.MkBinOp(expression, ctx.getChild(opPos).getText(), visitMidnode_matchExpr(ctx.midnode_match(i)));
            opPos += 2;
        }
        return expression;
    }

    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitMidnode_match(Midnode_matchContext ctx) {
        throw new NotImplementedException("visitMidnode_match");
    }

    private ArithExpr visitMidnode_matchExpr(Midnode_matchContext ctx) {
        ArithExpr expression = visitLeaf_matchExpr(ctx.leaf_match(0));
        int opPos = 1;
        for (int i = 1; i < ctx.leaf_match().size(); i++) {
            expression = eventSolver.MkBinOp(expression, ctx.getChild(opPos).getText(), visitLeaf_matchExpr(ctx.leaf_match(i)));
            opPos += 2;
        }
        return expression;
    }

    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitLeaf_match(Leaf_matchContext ctx) {
        throw new NotImplementedException("visitLeaf_match");
    }

    private ArithExpr visitLeaf_matchExpr(Leaf_matchContext ctx) {
        String val = ctx.getChild(0).getText();
        if (ctx.getChild(0) instanceof ValueContext) {
            if (val.contains("\"")) {
                valType = 1;
                return eventSolver.MkSingleArith(val);
            } else {
                valType = 0;
                return eventSolver.MkSingleArith(Integer.parseInt(val));
            }

        } else if (ctx.getChild(0) instanceof Var_refContext) {
            if (!variableMap.containsKey(val)) {
                variableMap.put(val, new HashSet<VarValues>());
            }
            // variableSet.add(val);
            flag++;
            return eventSolver.MkSingleArith(val);
        } else if (constantMap.containsKey(val)) {
            valType = 0;
            return eventSolver.MkSingleArith(constantMap.get(val));

        } else if (ctx.getChild(0) instanceof NameContext) {
            valType = 1;
            return eventSolver.MkSingleArith(val);

        } else {
            throw new NotImplementedException("visitLeaf_matchExpr");
        }
    }

    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitValue(ValueContext ctx) {
        throw new NotImplementedException("visitValue");
    }

    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitEquality_op(Equality_opContext ctx) {
        throw new NotImplementedException("visitEquality_op");
    }

    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitCompare_op(Compare_opContext ctx) {
        throw new NotImplementedException("visitCompare_op");
    }

    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitRegex_op(Regex_opContext ctx) {
        throw new NotImplementedException("visitRegex_op");
    }

    @Override
    public SFA<BoolExpr, HashMap<String, Integer>> visitName(NameContext ctx) {
        throw new NotImplementedException("visitName");
    }

}