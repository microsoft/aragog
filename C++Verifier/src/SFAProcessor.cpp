#ifndef SFAPROCESSOR_H
#define SFAPROCESSOR_H

#include "DSFA/Invariant.cpp"

class SFAProcessor {
    private:
        shared_ptr<Invariant> inv;
        string key;
        unordered_map<int, int> currentStateMap;
        unordered_map<int, vector<shared_ptr<ConstraintTreeNode>>>  constraintTreeListMap;

        vector<string> constraintBuffer;

        bool processElementSingle(shared_ptr<Packet> & p, int dsfaNum) {
            // cout << "In single" << endl;
            // Using raw pointer because don't want to remove the dsfa.
            DSFA* dsfa = (&(inv -> dsfas[dsfaNum]));

            int currentState;
            try {
                // cout << "Getting dsfaNum" << dsfaNum << endl;
                currentState = currentStateMap.at(dsfaNum);
            }
            catch(const std::out_of_range& oor) {
                // cout << "Excpetion caught" << endl;
                currentState = dsfa -> startState;
            }
            // cout << "going in advanceAndCheckSuppress" << endl;
            pair<int, bool> res = dsfa -> advanceAndCheckSuppress(p, currentState);
            
            currentStateMap[dsfaNum] = res.first;
            return res.second;
        }

        bool processElementTree(shared_ptr<Packet> & p, int dsfaNum) {
            // cout << "In tree" << endl;
            // Using raw pointer because don't want to remove the dsfa.
            DSFA* dsfa = (&(inv -> dsfas[dsfaNum]));
            // If this is a new run, make a fresh tree
            vector<shared_ptr<ConstraintTreeNode>> constraintTreeList = constraintTreeListMap[dsfaNum];
            if (constraintTreeList.size() == 0) {
                constraintTreeList.push_back(ConstraintTreeNode::makeFreshTree(dsfa -> startState,
                        dsfa -> locationList, dsfa -> variableList));
            }
            assert (!constraintTreeList.empty());

            // Note: don't need to check dsfa.locationList.size() as we are in a local SFA
            if (constraintBuffer.empty()) {
                for (int i = 0; i < dsfa -> variableList.size(); ++i) {
                    constraintBuffer.push_back("");
                }
            } else if (constraintBuffer.size() < dsfa -> variableList.size()) {
                for (int i = constraintBuffer.size(); i < dsfa -> variableList.size(); ++i) {
                    constraintBuffer.push_back("");
                }
            }

            // cout << "going in advanceConstraintTreeAndCheckSuppress" << endl;
            bool suppressible = dsfa -> advanceConstraintTreeAndCheckSuppress(p, constraintBuffer, constraintTreeList);

            constraintTreeListMap[dsfaNum] = constraintTreeList;
            return suppressible;
        }

    public:
        SFAProcessor(shared_ptr<Invariant> _inv, string _key) {
            inv = _inv;
            key = _key;
        };

        bool processPacket(shared_ptr<Packet> & p) {
            // cout << "in processPacket" << endl;
            if (inv -> filter != nullptr && (!simplifyEvaluate(inv -> filter,p))) {
                // cout << "Packet Filtered" << endl;
                return false;
            }
            // cout << "Packet allowed to process" << endl;
            bool suppressible = true;
            if (inv -> dsfas.size() == 0) {
                // cout << "No dsfa so cannot suppress" << endl;
                suppressible = false;
            }
            for (int dsfaNum = 0; dsfaNum < inv -> dsfas.size(); dsfaNum++) {
                // Using raw pointer because don't want to remove the dsfa.
                DSFA* dsfa = (&(inv -> dsfas[dsfaNum]));
                // cout << "Next step" << endl;
                if (dsfa -> hasVariables()) {
                    suppressible &= processElementTree(p, dsfaNum);
                } else {
                    suppressible &= processElementSingle(p, dsfaNum);
                }
            }
            // cout << "After processing. Should I suppress?: " << suppressible << endl;
            suppressible &= simplifyEvaluate(inv -> negatedLocExpr,p);
            // cout << "After Negative filter. Should I suppress?: " << suppressible << endl;
            if (!suppressible) {
                return true;
            } else {
                return false;
            }

        }
};

#endif