#ifndef BOOLEXPR_H
#define BOOLEXPR_H

#include "Expr.h"
#include "../DSFA/ConstraintTreeNode.cpp"

class BoolExpr: public virtual Expr {
    public:
        using Expr::Expr;

        virtual bool evaluate(shared_ptr<Packet> p,  vector<string> constraints, vector<string> locationList, vector<string> variableList, int currentState, vector<shared_ptr<ConstraintTreeNode>> rootConstraints) = 0;

        virtual void* evaluate(shared_ptr<Packet> p) = 0;

        void toggleNegated() {
            negated = !negated;
        }
        static bool applyBoolOp(string leftValue, string rightValue, string operand) {
            long leftVal = stol(leftValue);
            long rightVal = stol(rightValue);
            if (operand == ">") {
                return leftVal > rightVal;
            } else if (operand == ">=") {
                return leftVal >= rightVal;
            } else if (operand == "<") {
                return leftVal < rightVal;
            } else if (operand == "<=") {
                return leftVal <= rightVal;
            }
            // TODO: Throw exception heres

            return false;
        }
        virtual ~BoolExpr() {};
        bool negated = false;
};

#endif