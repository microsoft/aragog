#ifndef EQEXPR_H
#define EQEXPR_H

#include <vector>
#include "../utils/utils.cpp"
#include "../DSFA/DSFA.h"

using namespace std;

class EqExpr: public BoolExpr {
    public:
        using BoolExpr::BoolExpr;

        void* evaluate(shared_ptr<Packet> p) {
            // cout << "In EqExpr evaluateA: " << toString() << endl;
            string leftValue = evalSingle(left, leftType, p);
            string rightValue = evalSingle(right, rightType, p);
            // cout << "Comparing: " << leftValue << " with: " << rightValue << endl;
            bool* ret = new bool;
            *ret = (leftValue == rightValue) ^ negated;
            // cout << "returned value: " << *ret << endl;
            return static_cast<void*>(ret);
        }

        bool evaluate(shared_ptr<Packet> p,  vector<string> constraints, vector<string> locationList, vector<string> variableList, int currentState, vector<shared_ptr<ConstraintTreeNode>> rootConstraints) {
            // cout << "In EqExpr evaluateB: " << toString() << endl;
            string leftValue = evalSingle(left, leftType, p, constraints, locationList, variableList);
            string rightValue = evalSingle(right, rightType, p, constraints, locationList, variableList);
            // cout << "Comparing: " << leftValue << " with: " << rightValue << endl;


            // Now parse variables.  Need to do this after the above so that we have the concrete int[] values
            if (leftType == VARIABLE || leftType == LOCATION_VAR) {
                int level;
                if (leftType == VARIABLE) {
                    level = locationList.size() + getIndexInVector(variableList, leftValue);
                } else {
                    level = getIndexInVector(locationList, leftValue);
                }

                if (level >= constraints.size() || constraints[level] == "") {
                    assert(rightValue != "");
                    DSFA::splitIfNew(constraints, 0, rightValue, currentState, rootConstraints);

                    return negated;
                } else {
                    leftValue = constraints[level];
                }

            } else if (rightType == VARIABLE || rightType == LOCATION_VAR) {
                int level;
                if (rightType == VARIABLE) {
                    level = locationList.size() + getIndexInVector(variableList, rightValue);
                } else {
                    level = getIndexInVector(locationList, rightValue);
                }

                if (level >= constraints.size() || constraints[level] == "") {
                    assert(leftValue != "");
                    DSFA::splitIfNew(constraints, 0, leftValue, currentState, rootConstraints);
                    return negated;
                } else {
                    rightValue = constraints[level];
                }
            } else if (leftValue == "" || rightValue == "") {
                throw std::runtime_error("Unknown Eq Expression fields");
            }

            return (leftValue == rightValue) ^ negated;
        } 

        string toString() {
            string output = (negated ? "!" : "");
            return output + "(" + (left -> toString()) + " == " + (right -> toString()) + ")";
        }     
};

#endif