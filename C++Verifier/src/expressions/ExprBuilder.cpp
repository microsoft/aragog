#include "ExprBuilder.h"

using namespace std;

namespace ExprBuilder {


    bool isValid(antlr4::tree::ParseTree *ctx) {
        return ctx != nullptr;
    }



    /**
     *   varBinding
     *      : ParOpen UndefinedSymbol boolExpression ParClose
     *      ;
     */
    void buildVarBinding(SimpleSMTLIBParser::VarBindingContext *ctx,
           const shared_ptr<std::unordered_map<std::string, shared_ptr<BoolExpr>>> oldBindings, shared_ptr<std::unordered_map<std::string, shared_ptr<BoolExpr>>> newBindings,
            const shared_ptr<vector<string>> locationReferences, const shared_ptr<std::unordered_map<std::string, std::string>> variableComparisons) {
        (*newBindings)[ctx -> UndefinedSymbol() -> getText()] = buildBoolExpr(ctx ->boolExpression(),
                oldBindings, locationReferences, variableComparisons);
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


    std::shared_ptr<BoolExpr> buildBoolExpr(SimpleSMTLIBParser::BoolExpressionContext *ctx,
        const shared_ptr<std::unordered_map<std::string, shared_ptr<BoolExpr>>> bindings,
        const shared_ptr<std::vector<std::string>> locationReferences, 
        const shared_ptr<std::unordered_map<std::string, std::string>> variableComparisons) {
        string exprRaw = ctx -> getText() ;
        // cout << "in buildBoolExpr: " << exprRaw << endl;
        shared_ptr<BoolExpr> ret = nullptr;

        if (isValid(ctx -> boolOp())) {
            // ParOpen boolOp boolExpression+ ParClose
            ret = buildBoolExpr(ctx -> boolExpression(0), bindings, locationReferences,
                        variableComparisons);
            if (ctx -> boolOp() -> getText()  == "and") {
                vector<SimpleSMTLIBParser::BoolExpressionContext *> bringingOutVector = ctx -> boolExpression();
                
                for (int i = 1; i < bringingOutVector.size(); ++i) {
                    std::shared_ptr<BoolExpr> right = buildBoolExpr(ctx -> boolExpression(i), bindings,
                                locationReferences, variableComparisons);
                    ret = shared_ptr<AndExpr>(new AndExpr(ret, right));
                }

            } else if (ctx -> boolOp() -> getText() == "or") {
                for (int i = 1; i < (ctx -> boolExpression()).size(); ++i) {
                    std::shared_ptr<BoolExpr> right = buildBoolExpr(ctx -> boolExpression(i), bindings,
                                locationReferences, variableComparisons);
                    ret = shared_ptr<OrExpr>(new OrExpr(ret, right));
                }
            } else if (ctx -> boolOp() -> getText() == "not") {
                assert (ctx -> boolExpression().size() == 1);
                ret -> negated = true;
            }

        } else if (isValid(ctx -> compOp())) {
            shared_ptr<Expr> left = buildArithExpr(ctx -> arithExpression(0));
            shared_ptr<Expr> right = buildArithExpr(ctx -> arithExpression(1));
            
            if (ctx -> compOp() -> getText() == "=") {
                ret = shared_ptr<EqExpr>(new EqExpr(left, right, locationReferences, variableComparisons));
            } else if (ctx -> compOp() -> getText() == "<") {
                ret = shared_ptr<LtExpr>(new LtExpr(left, right));
            } else if (ctx -> compOp() -> getText() == "<=") {
                ret = shared_ptr<LeExpr>(new LeExpr(left, right));
            } else if (ctx -> compOp() -> getText() == ">") {
                ret = shared_ptr<GtExpr>(new GtExpr(left, right));
            } else if (ctx -> compOp() -> getText() == ">=") {
                ret = shared_ptr<GeExpr>(new GeExpr(left, right));
            }
        } else if (isValid(ctx -> GRW_Let())) {
            shared_ptr<unordered_map<string, shared_ptr<BoolExpr>>> newBindings(new unordered_map<string, shared_ptr<BoolExpr>>(*bindings));
            for (SimpleSMTLIBParser::VarBindingContext* vb: ctx -> varBinding()) {
                buildVarBinding(vb, bindings, newBindings, locationReferences, variableComparisons);
            }
            assert(ctx -> boolExpression().size() == 1);
            ret = buildBoolExpr(ctx -> boolExpression(0), newBindings, locationReferences, variableComparisons);

        } else if (isValid(ctx -> boundVar())) {
            ret = bindings -> at(ctx -> boundVar() -> getText()); 
        } else if (isValid(ctx -> PS_True())) {
            ret = shared_ptr<TrueExpr>(new TrueExpr());
        } else if (isValid(ctx -> PS_False())) {
        //  PS_False
            ret = shared_ptr<TrueExpr>(new TrueExpr());
            ret -> toggleNegated();
        }
        return ret;

    }

     /**
     *   arithExpression
     *      : ParOpen GRW_Ite boolExpression arithExpression arithExpression ParClose
     *      | ParOpen arithOp arithExpression+ ParClose
     *      | terminal
     *      ;
     */

    std::shared_ptr<Expr> buildArithExpr(SimpleSMTLIBParser::ArithExpressionContext *ctx) {
        shared_ptr<Expr> ret = nullptr;
        // cout << "in buildArithExpr" << endl;
        if (isValid(ctx -> GRW_Ite())) {
            // cout << "in ite " << endl;
            shared_ptr<BoolExpr> condition = buildBoolExpr(ctx -> boolExpression(), shared_ptr<unordered_map<string, shared_ptr<BoolExpr>>>(new unordered_map<string, shared_ptr<BoolExpr>>()), nullptr, nullptr);

            shared_ptr<Expr> left = buildArithExpr(ctx -> arithExpression(0));
            shared_ptr<Expr> right = buildArithExpr(ctx -> arithExpression(1));

            ret = std::shared_ptr<IteExpr>( new IteExpr(condition, left, right) );

        } else if (isValid(ctx -> arithOp())) {
            if (ctx -> arithExpression().size() == 1) {
                assert(ctx -> arithOp() -> getText() == "-");
                shared_ptr<Expr> only = buildArithExpr(ctx -> arithExpression(0));
                ret = shared_ptr<NegativeContext>(new NegativeContext(only));
            } else if (ctx -> arithExpression().size() == 2) {
                ret = buildArithExpr(ctx -> arithExpression(0));
                for (int i = 1; i < ctx -> arithExpression().size(); ++i) {
                    shared_ptr<Expr> right = buildArithExpr(ctx -> arithExpression(i));
                    if (ctx -> arithOp() -> getText() == "+") {
                        if (shared_ptr<NegativeContext> negatedRight = dynamic_pointer_cast<NegativeContext>(right)) {
                            ret = shared_ptr<SubExpr>(new SubExpr(ret, negatedRight -> left));
                        } else {
                            ret = shared_ptr<AddExpr>(new AddExpr(ret, right));
                        }
                    } else if (ctx -> arithOp() -> getText() == "-") {
                        ret = shared_ptr<SubExpr>(new SubExpr(ret, right));
                    } else if (ctx -> arithOp() -> getText() == "*") {
                        if (shared_ptr<NegativeContext> negatedLeft = dynamic_pointer_cast<NegativeContext>(ret)) {
                            // todo: assert left's vaue is 1.
                            ret = shared_ptr<NegativeContext>(new NegativeContext(right));
                        } else {
                            ret = shared_ptr<MulExpr>(new MulExpr(ret, right));
                        }
                    } else if (ctx -> arithOp() -> getText() == "/") {
                        ret = shared_ptr<DivExpr>(new DivExpr(ret, right));
                    } else {
                        throw std::runtime_error("Unknown arithExpression syntax");
                    }
                }
            } else {
                throw std::runtime_error("Unknown arithExpression syntax");
            }
        } else if (isValid(ctx -> terminal())) {
            if (isValid(ctx -> terminal() -> UndefinedSymbol())) {
                ret = shared_ptr<StringExpr>(new StringExpr(ctx -> terminal() -> getText()));
            } else {
                ret = shared_ptr<NumExpr>(new NumExpr(stol(ctx -> terminal() -> getText())));
            }
        } else {
            throw std::runtime_error("Unknown arithExpression syntax");
        }
        return ret;
    }

    std::shared_ptr<BoolExpr> buildBoolExpr(SimpleSMTLIBParser::StartBoolContext *ctx, 
        const shared_ptr<std::vector<std::string>> locationReferences, const shared_ptr<std::unordered_map<std::string, std::string>> variableComparisons) {
        return buildBoolExpr(
            ctx -> boolExpression(),
            shared_ptr<std::unordered_map<std::string, shared_ptr<BoolExpr>>> (new unordered_map<string, shared_ptr<BoolExpr>>()),
            locationReferences,
            variableComparisons);
    }
}
