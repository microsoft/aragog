#ifndef INVARIANT_H
#define INVARIANT_H

#include "SFAParser.cpp"

using namespace std;;

class Invariant {
    public:
        shared_ptr<BoolExpr> negatedLocExpr;
        shared_ptr<BoolExpr> filter;
        std::vector<std::string> groupByFields;
        std::vector<DSFA> dsfas;
        string name;

        Invariant(std::vector<std::string> filenames) {
            dsfas = std::vector<DSFA>();
            for (string filename : filenames) {
                // cout << "Pushing back: " << filename << endl;
                if (endsWith(filename,".sm.g")) {
                    name = getNameWOExt(filename);
                    parseGlobalStateMachine(negatedLocExpr, filter, groupByFields, filename);
                } else {
                    // cout << "Pushing back: " << filename << endl;
                    dsfas.push_back(std::move(*parseLocalStateMachine(filename)));
                }
            }
            // Add node as groupby
        }

        std::string getKeyString(shared_ptr<Packet> p) {
            string output = "";
            for (string key : groupByFields) {
                output += p -> get(key) + ",";
            }
            // cout << "returning key: " << output << endl;
            return output;
        }

        friend ostream& operator<< (ostream& out, const Invariant& obj); 
};

inline ostream& operator<< (ostream& out, const Invariant& obj) {
    out << obj.name << endl;
    return out;
}

#endif