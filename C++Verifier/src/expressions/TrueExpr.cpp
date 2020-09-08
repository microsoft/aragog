#ifndef TRUEEXPR_H
#define TRUEEXPR_H

#include <vector>
#include "BoolExpr.cpp"
// #include "../utils/Packet.cpp"

using namespace std;

class TrueExpr: public BoolExpr {
public:
        using BoolExpr::BoolExpr;

        void* evaluate(shared_ptr<Packet> p) {
            // cout << "In True evaluateA" << endl;
            bool * ret = new bool;
            *ret = !negated;
            return static_cast<void *> (ret);
        }

        bool evaluate(shared_ptr<Packet> p,  vector<string> constraints, vector<string> locationList, vector<string> variableList, int currentState, vector<shared_ptr<ConstraintTreeNode>> rootConstraints)  {
            // cout << "In True evaluateB" << endl;
            return !negated;
        }

        string toString() {
            string output = (negated ? "!" : "");
            return output + "True";
        }
};

#endif