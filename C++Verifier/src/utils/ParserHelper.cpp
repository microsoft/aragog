#include <memory>
#include <map>
#include <vector>
#include "utils.cpp"

using namespace std;

class ParseNode {
public:
    unique_ptr<ParseNode> next;

    ParseNode(ParseNode* last) {
        if (last != nullptr) {
            last -> next = unique_ptr<ParseNode> (this);
        }
    }
    virtual ~ParseNode(){};
};

class Field: public ParseNode {
public:
    string name;
    int length;
        
    Field(string _name, int _length, ParseNode* last) 
    : ParseNode(last) {
        name = _name;
        length = _length;
    }
    ~Field(){};
};


class Condition {
public:
    string field;
    string value;

    Condition(string condition) {
        if (iequals(condition,"default")) {
            field = "";
            value = "";
        } else {
            std::vector<std::string> conditionTokens = split(condition, '=');
            assert (conditionTokens.size() == 2);
            field = conditionTokens[0];
            value = conditionTokens[1];
        }
    }
    ~Condition() {};
};

class ConditionalNode: public ParseNode {
public:
    std::map<Condition*, unique_ptr<ParseNode>> children; 
    ConditionalNode(ParseNode* last) : ParseNode(last) {
    }

    ParseNode* getChildNode(shared_ptr<Packet> p) {
        for(std::map<Condition*, unique_ptr<ParseNode>>::iterator iter = children.begin(); iter != children.end(); ++iter) {
            Condition* c =  iter->first;
            if (c -> field == "") {
                return children[c].get();
            }
            if (p -> get(c -> field) == c -> value) {
                return children[c].get();
            }
        }
        return nullptr;
   }
   ~ConditionalNode(){};
};


