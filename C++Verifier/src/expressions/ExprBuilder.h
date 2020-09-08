#ifndef EXPRBUILDER_H
#define EXPRBUILDER_H

#include "../expressions/IteExpr.cpp"

#include "../expressions/AndExpr.cpp"
#include "../expressions/OrExpr.cpp"
#include "../expressions/EqExpr.cpp"
#include "../expressions/LtExpr.cpp"
#include "../expressions/LeExpr.cpp"
#include "../expressions/GtExpr.cpp"
#include "../expressions/GeExpr.cpp"
#include "../expressions/TrueExpr.cpp"

#include "../expressions/SubExpr.cpp"
#include "../expressions/AddExpr.cpp"
#include "../expressions/MulExpr.cpp"
#include "../expressions/DivExpr.cpp"
#include "../expressions/StringExpr.cpp"
#include "../expressions/NumExpr.cpp"
#include "../expressions/NegativeContext.cpp"

#include "../antlr4/SimpleSMTLIBParser.h"


namespace ExprBuilder {
    bool isValid(shared_ptr<antlr4::tree::ParseTree> ctx);

    void buildVarBinding(SimpleSMTLIBParser::VarBindingContext *ctx,
           const shared_ptr<std::unordered_map<std::string, shared_ptr<BoolExpr>>> oldBindings, shared_ptr<std::unordered_map<std::string, shared_ptr<BoolExpr>>> newBindings,
            const shared_ptr<vector<string>> locationReferences, const shared_ptr<std::unordered_map<std::string, std::string>> variableComparisons);



    std::shared_ptr<BoolExpr> buildBoolExpr(SimpleSMTLIBParser::BoolExpressionContext *ctx, 
        const shared_ptr<std::unordered_map<std::string, shared_ptr<BoolExpr>>> bindings,
        const shared_ptr<std::vector<std::string>> locationReferences, 
        const shared_ptr<std::unordered_map<std::string, std::string>> variableComparisons);


    std::shared_ptr<Expr> buildArithExpr(SimpleSMTLIBParser::ArithExpressionContext *ctx);

    std::shared_ptr<BoolExpr> buildBoolExpr(SimpleSMTLIBParser::StartBoolContext *ctx, 
        const shared_ptr<std::vector<std::string>> locationReferences,
        const shared_ptr<std::unordered_map<std::string, std::string>> variableComparisons);
    

}



#endif