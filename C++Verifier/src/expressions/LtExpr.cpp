#ifndef LTEXPR_H
#define LTEXPR_H

#include <vector>
#include "BoolExpr.cpp"
// #include "../utils/Packet.h"

using namespace std;

class LtExpr: public BoolExpr {
    public:
        using BoolExpr::BoolExpr;

        void* evaluate(shared_ptr<Packet> p) {
            string leftValue = evalSingle(left, leftType, p);
            string rightValue = evalSingle(right, rightType, p);
            bool* ret = nullptr;
            *ret = applyBoolOp(leftValue, rightValue, "<") ^ negated;
            return static_cast<void*>(ret);
        }

        bool evaluate(shared_ptr<Packet> p,  vector<string> constraints, vector<string> locationList, vector<string> variableList, int currentState, vector<shared_ptr<ConstraintTreeNode>> rootConstraints) {
            string leftValue = evalSingle(left, leftType, p, constraints, locationList, variableList);
            string rightValue = evalSingle(right, rightType, p, constraints, locationList, variableList);
            return applyBoolOp(leftValue, rightValue, "<") ^ negated;
        }

        string toString() {
            string output = (negated ? "!" : "");
            return output + "(" + left -> toString() + " < " + right -> toString() + ")";
        }
};

#endif