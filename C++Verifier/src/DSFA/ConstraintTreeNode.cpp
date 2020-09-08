#ifndef CONSTRAINTTREE_H
#define CONSTRAINTTREE_H
#include <string>
#include <vector>
#include <memory>

using namespace std;

class ConstraintTreeNode {
    private:
        shared_ptr<ConstraintTreeNode> parent;

        shared_ptr<ConstraintTreeNode> cloneExact() {
            shared_ptr<ConstraintTreeNode> newNode(new ConstraintTreeNode(constraint));
            newNode -> currentState = currentState;
            // newNode -> lastTime = lastTime;

            for (shared_ptr<ConstraintTreeNode> child : children) {
                adopt(newNode, child -> cloneExact());
            }

            return newNode;
        }
    public:
        std::vector<shared_ptr<ConstraintTreeNode>> children;
        int currentState;
        string constraint;
        // long lastTime;
        ConstraintTreeNode(string _constraint) {
            constraint = _constraint;
            children = vector<shared_ptr<ConstraintTreeNode>>();
            currentState = -1;
            // lastTime = -1;
        }

        int getCurrentState() {
            return currentState;
        }

        string printTree(int depth) {
            string result = "currentState: " + to_string(currentState);
            // result += "lastTime: " + to_string(lastTime);
            result += " constraint: " + constraint;
            result += " depth: " + to_string(depth);
            result += " ==> children: \n" ;
            for (shared_ptr<ConstraintTreeNode> child : children) {
                result += string(depth, '\n') + child -> printTree(depth + 1); 
            }
            return result;
        }

        static shared_ptr<ConstraintTreeNode> makeFreshTree(int startState, std::vector<string> locationList, std::vector<string> variableList) {

            shared_ptr<ConstraintTreeNode> root (new ConstraintTreeNode(""));

            shared_ptr<ConstraintTreeNode> current = root;

            for (int i = 1; i < locationList.size() + variableList.size(); ++i) {
                shared_ptr<ConstraintTreeNode> next(new ConstraintTreeNode(""));
                adopt(current, next);
                current = next;
            }
            current -> currentState = startState;
            // current -> lastTime = 0;
            return root;
        }

        shared_ptr<ConstraintTreeNode> clone(string newConstraint) {
            shared_ptr<ConstraintTreeNode> newNode(new ConstraintTreeNode(""));
            adopt(parent, newNode);
            newNode -> currentState = currentState;
            // newNode -> lastTime = lastTime;

            for (shared_ptr<ConstraintTreeNode> child : children) {
                adopt(newNode, child -> cloneExact());
            }
            return newNode;
        }

    protected:
        static void adopt(shared_ptr<ConstraintTreeNode> parent, shared_ptr<ConstraintTreeNode> child) {
            if (parent != nullptr) {
                parent -> children.push_back(child);
            }
            child -> parent = parent;
        }

};

#endif