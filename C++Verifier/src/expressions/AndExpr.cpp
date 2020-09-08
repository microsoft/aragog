#ifndef ANDEXPR_H
#define ANDEXPR_H

#include <vector>
#include "BoolExpr.cpp"
// #include "../utils/Packet.cpp"

using namespace std;

class AndExpr: public BoolExpr {
    public:
        using BoolExpr::BoolExpr;

        void* evaluate(shared_ptr<Packet> p) {
            bool* leftValue = static_cast<bool*>(dynamic_pointer_cast<BoolExpr>(left) -> evaluate(p));

            bool* rightValue = static_cast<bool*>(dynamic_pointer_cast<BoolExpr>(right) -> evaluate(p));
            bool* ret = new bool;
            *ret = *leftValue && *rightValue;
            delete leftValue;
            delete rightValue;
            *ret = negated ? !(*ret) : (*ret);
            return (static_cast<void*>(ret));
        }

        bool evaluate(shared_ptr<Packet> p,  vector<string> constraints, vector<string> locationList, vector<string> variableList, int currentState, vector<shared_ptr<ConstraintTreeNode>> rootConstraints) {
            bool ret = dynamic_pointer_cast<BoolExpr>(left) -> evaluate(p, constraints, locationList, variableList, currentState, rootConstraints) && dynamic_pointer_cast<BoolExpr>(right) -> evaluate(p, constraints, locationList, variableList, currentState, rootConstraints);
            return negated ? !ret : ret;
        }

        string toString() {
            string output = (negated ? "!" : "");
            return output + "(" + (left -> toString()) + " && " + (right -> toString()) + ")";
        }
};

#endif