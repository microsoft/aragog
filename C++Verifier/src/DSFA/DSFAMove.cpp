#ifndef DSFAMOVE_H
#define DSFAMOVE_H

#include "../expressions/BoolExpr.cpp"
#include "../utils/utils.cpp"

using namespace std;

class DSFAMove {
    bool toFinal;

    std::vector<std::string> locationReferences;
    unordered_map<std::string, std::string> variableComparisons;

    public:
        shared_ptr<BoolExpr> condition;
        int to;
        bool suppress;
        // DSFAMove() {}
        DSFAMove(shared_ptr<BoolExpr> _condition, bool _suppress, int _to, bool _toFinal,
            std::vector<std::string> _locationReferences, unordered_map<std::string, std::string> _variableComparisons) {
            condition = move(_condition);
            suppress = _suppress;
            to = _to;
            toFinal = _toFinal;
            locationReferences = move(_locationReferences);
            variableComparisons = move(_variableComparisons);
        }
        friend ostream& operator<< (ostream& out, const DSFAMove& obj); 
};

inline ostream& operator<< (ostream& out, const DSFAMove& obj) {
    string output = "Condition: " + obj.condition -> toString() + "\n";
    output +=  "To: " + to_string(obj.to) + 
    "\n";

    output +=  "To Final: ";
    if (obj.toFinal) {
        output += "True\n";    
    } else {
        output += "False\n";   
    }

    output +=  "suppress: ";
    if (obj.suppress) {
        output += "True\n";    
    } else {
        output += "False\n";   
    }
    
    output +=  "locationReferences: " + joinVectorString(obj.locationReferences, ",") + 
    "\n";
    output +=  "variableComparisons: ";
    for (auto x : obj.variableComparisons) 
        output += x.first + " " + x.second + ",";

    output += "\n";

    out << output << endl;
    return out;
}

#endif