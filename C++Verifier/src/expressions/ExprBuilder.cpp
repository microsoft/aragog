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
            // cout << "in boolop" << endl;
            // ParOpen boolOp boolExpression+ ParClose
            // cout << "Number of boolExpression: " << ctx ->boolExpression().size() << endl;
            // shared_ptr<SimpleSMTLIBParser::BoolExpressionContext> firstBool();
            ret = buildBoolExpr(ctx -> boolExpression(0), bindings, locationReferences,
                        variableComparisons);
            // cout << "boolop created" << endl;
            // cout << "checking text for: " << exprRaw << endl;
            if (ctx -> boolOp() -> getText()  == "and") {
                // cout << "creating and of size: " << endl;
                // if (ctx == nullptr) {
                    // cout << "ctx is nullptr" << endl;
                // }
                // else if ((ctx -> boolExpression()).empty()) {
                //     cout << "boolExpression is empty" << endl;
                // }
                // else if ((ctx -> boolExpression(0)) == nullptr) {
                //     cout << "0 is nullptr" << endl;
                // } else if ((ctx -> boolExpression(1)) == nullptr) {
                //     cout << "1 is nullptr" << endl;
                // } 
                vector<SimpleSMTLIBParser::BoolExpressionContext *> bringingOutVector = ctx -> boolExpression();
                // cout << "vector is out" << endl;
                // cout << bringingOutVector.size() << endl;
                for (int i = 1; i < bringingOutVector.size(); ++i) {
                    // cout << "Creating ith object pointer:" << i << endl;
                    // shared_ptr<SimpleSMTLIBParser::BoolExpressionContext> ithObj();
                    // cout << "going to buildBoolExpr for ith object" << endl;
                    // cout << ithObj -> getText() << endl;
                    std::shared_ptr<BoolExpr> right = buildBoolExpr(ctx -> boolExpression(i), bindings,
                                locationReferences, variableComparisons);
                    // cout << "going in and constructor" << endl;
                    // cout << "creating AndExpr: " << endl;
                    // cout << "with left: " << ret -> toString() << endl;
                    // cout << "and right: " << right -> toString() << endl;
                    ret = shared_ptr<AndExpr>(new AndExpr(ret, right));
                    // cout << "and created" << endl;
                }
                // cout << "out of and loop" << endl;

            } else if (ctx -> boolOp() -> getText() == "or") {
                // cout << "creating or" << endl;
                for (int i = 1; i < (ctx -> boolExpression()).size(); ++i) {
                    std::shared_ptr<BoolExpr> right = buildBoolExpr(ctx -> boolExpression(i), bindings,
                                locationReferences, variableComparisons);
                    // cout << "creating OrExpr: " << endl;
                    // cout << "with left: " << ret -> toString() << endl;
                    // cout << "and right: " << right -> toString() << endl;
                    ret = shared_ptr<OrExpr>(new OrExpr(ret, right));
                }
                // cout << "or created" << endl;
            } else if (ctx -> boolOp() -> getText() == "not") {
                // cout << "creating not" << endl;
                assert (ctx -> boolExpression().size() == 1);
                ret -> negated = true;
            }

        } else if (isValid(ctx -> compOp())) {
            // cout << "in compop with size: " << ctx->arithExpression().size() << endl;
            // shared_ptr<SimpleSMTLIBParser::ArithExpressionContext> arithPtr(());
            shared_ptr<Expr> left = buildArithExpr(ctx -> arithExpression(0));
            // cout << "left created" << endl;

            // arithPtr = shared_ptr<SimpleSMTLIBParser::ArithExpressionContext>(ctx -> arithExpression(1));
            // cout << "going in right now" << endl;
            shared_ptr<Expr> right = buildArithExpr(ctx -> arithExpression(1));
            // cout << "left and right created" << endl;
            // cout << (ctx -> compOp() -> getText()) << endl;
            
            if (ctx -> compOp() -> getText() == "=") {
                // cout << "creating EqExpr: " << endl;
                // cout << "with left: " << left -> toString() << endl;
                // cout << "and right: " << right -> toString() << endl;
                ret = shared_ptr<EqExpr>(new EqExpr(left, right, locationReferences, variableComparisons));
                // cout << "EqExpr created" << endl;
            } else if (ctx -> compOp() -> getText() == "<") {
                ret = shared_ptr<LtExpr>(new LtExpr(left, right));
            } else if (ctx -> compOp() -> getText() == "<=") {
                ret = shared_ptr<LeExpr>(new LeExpr(left, right));
            } else if (ctx -> compOp() -> getText() == ">") {
                ret = shared_ptr<GtExpr>(new GtExpr(left, right));
            } else if (ctx -> compOp() -> getText() == ">=") {
                ret = shared_ptr<GeExpr>(new GeExpr(left, right));
            }
            // cout << "compop created " << endl;
        } else if (isValid(ctx -> GRW_Let())) {
            // cout << "in let" << endl;
            shared_ptr<unordered_map<string, shared_ptr<BoolExpr>>> newBindings(new unordered_map<string, shared_ptr<BoolExpr>>(*bindings));
            for (SimpleSMTLIBParser::VarBindingContext* vb: ctx -> varBinding()) {
                buildVarBinding(vb, bindings, newBindings, locationReferences, variableComparisons);
            }
            assert(ctx -> boolExpression().size() == 1);
            ret = buildBoolExpr(ctx -> boolExpression(0), newBindings, locationReferences, variableComparisons);

        } else if (isValid(ctx -> boundVar())) {
            // cout << "in boundVar" << endl;
            ret = bindings -> at(ctx -> boundVar() -> getText()); 
            // ret = move(test);
            // ret = bindings -> at(test);
        } else if (isValid(ctx -> PS_True())) {
        //     // PS_True
            // cout << "true" << endl;
            ret = shared_ptr<TrueExpr>(new TrueExpr());
        } else if (isValid(ctx -> PS_False())) {
        //     // PS_False
            // cout << "false" << endl;
            ret = shared_ptr<TrueExpr>(new TrueExpr());
            ret -> toggleNegated();
        }
        // cout << "buildBoolExpr complete: " << exprRaw << endl;
        // cout << "returning" << endl;
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
            // cout << "in arithop " << endl;
            if (ctx -> arithExpression().size() == 1) {
                assert(ctx -> arithOp() -> getText() == "-");
                shared_ptr<Expr> only = buildArithExpr(ctx -> arithExpression(0));
                ret = shared_ptr<NegativeContext>(new NegativeContext(only));
            } else if (ctx -> arithExpression().size() == 2) {
                // cout << "in in muti arith " << endl;
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
            // cout << "in terminal " << endl;
            if (isValid(ctx -> terminal() -> UndefinedSymbol())) {
                ret = shared_ptr<StringExpr>(new StringExpr(ctx -> terminal() -> getText()));
            } else {
                ret = shared_ptr<NumExpr>(new NumExpr(stol(ctx -> terminal() -> getText())));
            }
        } else {
            throw std::runtime_error("Unknown arithExpression syntax");
        }
        // cout << "buildArithExpr complete" << endl;
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
