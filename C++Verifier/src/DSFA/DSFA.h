#ifndef DSFA_H
#define DSFA_H

#include "DSFAMove.cpp"
#include <stdlib.h>

using namespace std;

class DSFA {
    bool variables;
    
    unordered_map<int, vector<DSFAMove>> transitions;
    // stateId -> {EventIndex -> nextStateId}
    
    // int name;
    public:
        int startState;
        vector<int> finalStates;

        vector<string> locationList;
        vector<string> variableList;

        DSFA(int _startState, vector<int> _finalStates, unordered_map<int, vector<DSFAMove>> & _transitions, vector<string> _locationList, vector<string> _variableList) {
            startState = _startState;
            finalStates = move(_finalStates);
            transitions = move(_transitions);
            locationList = move(_locationList);
            variableList = move(_variableList);

            variables = (variableList.size() > 0) ? true : false;
            // cout << "variables is set to: " << variables << endl;
            // name = rand() % 100;
        }
        // string getKey();
        bool hasVariables();
        string mergeRecursive(shared_ptr<ConstraintTreeNode> current);
        void merge(vector<shared_ptr<ConstraintTreeNode>> rootConstraints);
        pair<int,bool> advanceAndCheckSuppress(shared_ptr<Packet> p, int currentState);
        int advance(shared_ptr<Packet> p, int currentState);
        bool advanceConstraintTreeAndCheckSuppress(shared_ptr<Packet> p, vector<string> constraints,
            vector<shared_ptr<ConstraintTreeNode>> rootConstraints);
        bool advanceConstraintTreeAndCheckFinal(shared_ptr<Packet> p, vector<string> constraints,
            vector<shared_ptr<ConstraintTreeNode>> rootConstraints);
        bool traverseConstraintTreeAndCheckSuppress(shared_ptr<Packet> p, shared_ptr <ConstraintTreeNode> node,
            vector<string> constraints, int index, vector<shared_ptr<ConstraintTreeNode>> rootConstraints);
        bool traverseConstraintTreeAndCheckFinal(shared_ptr<Packet> p, shared_ptr <ConstraintTreeNode> node,
            vector<string> constraints, int index, vector<shared_ptr<ConstraintTreeNode>> rootConstraints);
        bool checkInFinal(int currentState);
        static bool splitIfNew(vector<string> currentConstraints, int newIndex,
            string newConstraint, int currentState, vector<shared_ptr<ConstraintTreeNode>> rootConstraints);
        static shared_ptr<ConstraintTreeNode> find(vector<shared_ptr<ConstraintTreeNode>> constraintList, string val);
};

#endif