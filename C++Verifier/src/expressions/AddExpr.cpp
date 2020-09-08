#ifndef ADDEXPR_H
#define ADDEXPR_H

#include <vector>
#include "ArithExpr.cpp"

using namespace std;

class AddExpr: public ArithExpr {
    public:
        using ArithExpr::ArithExpr;

        void* evaluate(shared_ptr<Packet> p) {
            string leftValue = evalSingle(left, leftType, p);
            string rightValue = evalSingle(right, rightType, p);
            string *ret = nullptr;
            *ret = applyArithOp(leftValue, rightValue, "+");
            return static_cast<void*>(ret);
        }

        string evaluate(shared_ptr<Packet> p, vector<string> constraints, vector<string> locationList, vector<string> variableList) {
            string leftValue = evalSingle(left, leftType, p, constraints, locationList, variableList);
            string rightValue = evalSingle(right, rightType, p, constraints, locationList, variableList);
            return applyArithOp(leftValue, rightValue, "+");
        }
        string toString() {
            return "(" + (left -> toString()) + " + " + (right -> toString()) + ")";
        }
};

#endif