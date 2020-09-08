#ifndef ARITHEXPR_H
#define ARITHEXPR_H

#include <vector>
#include "Expr.h"

using namespace std;

class ArithExpr: public virtual Expr {
public:
    using Expr::Expr;

    virtual void* evaluate(shared_ptr<Packet> p) = 0;

    virtual string evaluate(shared_ptr<Packet> p,  vector<string> constraints, vector<string> locationList, vector<string> variableList) = 0;

    static string applyArithOp(string leftValue, string rightValue, string operand) {
        long leftVal = stol(leftValue);
        long rightVal = stol(rightValue);
        if (operand == "+") {
            return to_string(leftVal + rightVal);
        } else if (operand == "-") {
            return to_string(leftVal - rightVal);
        } else if (operand == "*") {
            return to_string(leftVal * rightVal);
        } else if (operand == "/") {
            return to_string(leftVal / rightVal);
        }
        throw std::runtime_error("ArithExpr operand not supported");

        return "0";
    }
    virtual ~ArithExpr() {};

};

#endif