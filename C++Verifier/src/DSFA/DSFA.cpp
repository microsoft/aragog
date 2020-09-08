#include "DSFA.h"
#include "../utils/utils.cpp"

bool DSFA::advanceConstraintTreeAndCheckSuppress(shared_ptr<Packet> p, vector<string> constraints,
            vector<shared_ptr<ConstraintTreeNode>> rootConstraints) {
    bool suppressible = true;
    for (int i = 0; i < rootConstraints.size(); ++i) {
        suppressible &= traverseConstraintTreeAndCheckSuppress(p, rootConstraints[i],
                constraints, 0, rootConstraints);
    }

    merge(rootConstraints);
        
    return suppressible;
}

pair<int,bool> DSFA::advanceAndCheckSuppress(shared_ptr<Packet> p, int currentState) {
    cout << "currentStateA: " << currentState << endl;
    for (auto move : transitions.at(currentState)) {
        if (simplifyEvaluate(move.condition, p)) {
            return make_pair(move.to, move.suppress);
        }
    }
    throw std::runtime_error("Did not find a valid move for state");
    // return make_pair(0,true);
}

bool DSFA::hasVariables() {
    return variables;
}

string DSFA::mergeRecursive(shared_ptr<ConstraintTreeNode> current) {
    if (current -> children.empty()) {
        return to_string(current -> currentState);
    }

    std::vector<string> subtreeStrings;
    for (shared_ptr<ConstraintTreeNode> child : current -> children) {
        subtreeStrings.push_back(mergeRecursive(child));
    }

    for (int i = subtreeStrings.size() - 1; i > 0; --i) {
        int freq = count(subtreeStrings.begin(), subtreeStrings.end(), subtreeStrings[i]);
        if (freq > 1) {
            current ->children.erase(current -> children.begin() + i);
            subtreeStrings.erase(subtreeStrings.begin() + i);
        }
    }

    string ret = "";
    for (int i = 0; i < current -> children.size(); i++) {
        ret += "(";
        ret += current -> children[i] -> constraint;
        ret += " ";
        ret += subtreeStrings[i];
        ret += ")";
    }

    return ret;
}

void DSFA::merge(vector<shared_ptr<ConstraintTreeNode>>rootConstraints) {
    vector<string> subtreeStrings;
    for (shared_ptr<ConstraintTreeNode> roots : rootConstraints) {
        subtreeStrings.push_back(mergeRecursive(roots));
    }

    // Deduplicate
    for (int i = subtreeStrings.size() - 1; i > 0; --i) {
        int freq = count(subtreeStrings.begin(), subtreeStrings.end(), subtreeStrings[i]);
        if (freq > 1) {
            rootConstraints.erase(rootConstraints.begin() + i);
            subtreeStrings.erase(subtreeStrings.begin() + i);
        }
    }

    for (int i = rootConstraints.size() - 1; i >= 0; --i) {
        if (rootConstraints[i] -> currentState == startState) {
            rootConstraints.erase(rootConstraints.begin() + i);
        }
    }
}

int DSFA::advance(shared_ptr<Packet> p, int currentState) {
    // for (unique_ptr<DSFAMove> &move : transitions[currentState]) {
    cout << "currentStateB: " << currentState << endl;
    for (auto move : transitions.at(currentState)) {
        if (simplifyEvaluate(move.condition, p)) {
            cout << "Moving from: " << currentState << endl;
            cout << "Moving to: " << move.to << endl;
            return move.to;
        }
    }
    throw std::runtime_error("Did not find a valid move for state");
    // return 0;
}

bool DSFA::advanceConstraintTreeAndCheckFinal(shared_ptr<Packet> p, vector<string> constraints,
            vector<shared_ptr<ConstraintTreeNode>>rootConstraints) {
    bool inFinal = false;

    for (int i = 0; i < rootConstraints.size(); ++i) {

        inFinal |= traverseConstraintTreeAndCheckFinal(p, rootConstraints[i], constraints,
                0, rootConstraints);
    }
    merge(rootConstraints);

    return inFinal;
}


bool DSFA::traverseConstraintTreeAndCheckSuppress(shared_ptr<Packet> p, shared_ptr<ConstraintTreeNode> node, vector<string> constraints, int index,
            vector<shared_ptr<ConstraintTreeNode>>rootConstraints) {
    // DFS through the constraint tree, assembling the constraints as we go
    constraints[index] = node -> constraint;

    // When we get to a leaf, advance it
    if (node -> children.empty()) {
        // Traverse through every possible move. May split.
        // cout << "currentStateC: " << node->currentState << endl;
        for (auto move : transitions.at(node -> currentState)) {
        // for (unique_ptr<DSFAMove> &move : transitions[node -> currentState]) {
            if (move.condition -> evaluate(p, constraints, locationList, variableList,
                    node -> currentState, rootConstraints)) {
                // cout << "Moving from: " << node -> currentState << endl;
                // cout << "Moving to: " << move.to << endl;

                node -> currentState = move.to;
                // node -> lastTime = p -> getTime();
                return move.suppress;
            }
        }
        throw std::runtime_error("Did not find a valid move for state");
    }

    // Otherwise, just DFS
    // Use counter iteration so that we catch new children
    bool suppressible = true;
    for (int i = 0; i < node -> children.size(); ++i) {
        suppressible &= traverseConstraintTreeAndCheckSuppress(p, node -> children[i],
                constraints, index + 1, rootConstraints);
    }

    return suppressible;
}

bool DSFA::traverseConstraintTreeAndCheckFinal(shared_ptr<Packet> p, shared_ptr<ConstraintTreeNode> node, vector<string> constraints, int index,
            vector<shared_ptr<ConstraintTreeNode>>rootConstraints) {

    constraints[index] = node -> constraint;
    if (node -> children.empty()) {
        // Traverse through every possible move. May split.
        // for (unique_ptr<DSFAMove> &move : transitions[node -> currentState]) {
        // cout << "currentStateD: " << node -> currentState << endl;
        for (auto move : transitions.at(node -> currentState)) {
            if (move.condition -> evaluate(p, constraints, locationList, variableList,
                    node -> currentState, rootConstraints)) {
                // cout << "Moving from: " << node -> currentState << endl;
                // cout << "Moving to: " << move.to << endl;
                node -> currentState = move.to;
                // node -> lastTime = p -> getTime();
                return checkInFinal(node -> currentState);
            }
        }
        // TODO : Throw error
    }
    bool inFinal = false;
    for (int i = 0; i < node -> children.size(); ++i) {
        inFinal |= traverseConstraintTreeAndCheckFinal(p, node -> children[i], constraints,
                index + 1, rootConstraints);
    }

    return inFinal;
}


shared_ptr<ConstraintTreeNode> DSFA::find(vector<shared_ptr<ConstraintTreeNode>> constraintList, string val) {
    for (shared_ptr<ConstraintTreeNode> c : constraintList) {
        if (c -> constraint == val) {
            return c;
        }
    }

    return nullptr;
}

bool DSFA::checkInFinal(int currentState) {
    return contains(finalStates, currentState);
}

bool DSFA::splitIfNew(vector<string> currentConstraints, int newIndex,
            string newConstraint, int currentState, vector<shared_ptr<ConstraintTreeNode>> rootConstraints) {
    string searchVal = newIndex == 0 ? newConstraint : currentConstraints[0];
    shared_ptr<ConstraintTreeNode> current = find(rootConstraints, searchVal);
    if (current == nullptr) {
        // duplicate the entire subtree
        rootConstraints.push_back(rootConstraints[0] -> clone(newConstraint));
        return true;
    }

    for (int i = 1; i < currentConstraints.size(); ++i) {
        // iterate through each level of the constraint tree and determine if it exists
        searchVal = newIndex == i ? newConstraint : currentConstraints[i];
        shared_ptr<ConstraintTreeNode> next = find(current -> children, searchVal);
        if (next == nullptr) {
            // duplicate the entire subtree
            current -> children[0] -> clone(newConstraint);
            return true;
        }
        current = next;
    }

    return false;
}

