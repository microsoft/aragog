#include "StringExpr.cpp"
#include "BoolExpr.cpp"
#include "NumExpr.cpp"
#include "ArithExpr.cpp"
#include "NegativeContext.cpp"
#include <regex>
#include "../utils/utils.cpp"


Expr::ExprType Expr::parseType(shared_ptr<Expr> o) {
    // cout << "Expr parsing" << endl;
    if (ArithExpr* c = dynamic_cast<ArithExpr*>(o.get())) {
        // cout << "Expr parser done1" << endl;
        return Expr::ExprType::ARITH;
    } else if (BoolExpr* c = dynamic_cast<BoolExpr*>(o.get())) {
        // cout << "Expr parser done2" << endl;
        return Expr::ExprType::BOOL;
    } else if (NumExpr *c = dynamic_cast<NumExpr*>(o.get())) {
        // cout << "Expr parser done3" << endl;
        return Expr::ExprType::VALUE;
    } else if (NegativeContext *c = dynamic_cast<NegativeContext*>(o.get())) {
        throw std::runtime_error("NegativeContexts should not get to this point");
    } else if (StringExpr *c = dynamic_cast<StringExpr*>(o.get())) {
        string s = c -> getValue();
        std::regex e ("^.\".+\".$");
        if (startsWith(s,"$")) {
            return Expr::ExprType::VARIABLE;
        } else if (s == "META_rho") {
            return Expr::ExprType::RHO;
        } else if (s == "TIME") {
            return Expr::ExprType::TIME;
        } else if (std::regex_match(s,e)) {
            c -> trimValue();
            return Expr::ExprType::VALUE;
        }
    }

    return Expr::ExprType::PACKET;
}



Expr::Expr(shared_ptr<Expr> _left, shared_ptr<Expr> _right, shared_ptr<vector<string>> locationReferences, 
    shared_ptr<unordered_map<string, string>> variableComparisons) {
    // cout << "In Expr Constructor" << endl;
    init(_left, _right);
    // cout << "Left: " << _left -> toString() << " Left Type: " << leftType << endl;
    // cout << "Right: " << _right -> toString() << " Right Type: " << rightType << endl;
    // exit(0);
    if (leftType == Expr::ExprType::RHO) {
        locationReferences -> push_back(dynamic_cast<StringExpr&>(*right).getValue());
        rightType = Expr::ExprType::LOCATION_VAR;
    } else if (leftType == Expr::ExprType::VARIABLE) {
        (*variableComparisons)[dynamic_cast<StringExpr&>(*left).getValue()] = dynamic_cast<StringExpr&>(*right).getValue();
    }
    if (rightType == Expr::ExprType::RHO) {
        locationReferences -> push_back(dynamic_cast<StringExpr&>(*left).getValue());
        leftType = Expr::ExprType::LOCATION_VAR;
    } else if (rightType == Expr::ExprType::VARIABLE) {
        (*variableComparisons)[dynamic_cast<StringExpr&>(*right).getValue()] = dynamic_cast<StringExpr&>(*left).getValue();
    } 
    // cout << "Successfully created Expr" << endl;
}

string Expr::evalSingle(shared_ptr<Expr> val, ExprType valType, shared_ptr<Packet> p) {
    string outValue = "";
    // cout << "In evalSingleA: " << val -> toString() << endl;
    switch(valType) {
        case PACKET:
            // cout << "Type is packet" << endl;
            if (StringExpr* c = dynamic_cast<StringExpr*>(val.get())) {
                // cout << "Type is StringExpr" << endl;
                // cout << "Getting from packet: " << c -> getValue() << endl;
                outValue = p -> get(c -> getValue());
            } else if (NumExpr* c = dynamic_cast<NumExpr*>(val.get()))  {
                // cout << "Type is NumExpr" << endl;
                outValue = p -> get(c -> getValue());
            }
            break;
        case VALUE:
            // cout << "Type is value" << endl;
            if (StringExpr* c = dynamic_cast<StringExpr*>(val.get())) {
                // cout << "It is a string value" << endl;
                outValue = c -> getValue();
            } else if (NumExpr* c = dynamic_cast<NumExpr*>(val.get()))  {
                // cout << "It is a num value" << endl;
                outValue = c -> getValue();
            } else {
                throw std::runtime_error("Unexpected expression simple evalSingle. Got Value Type, but niether string nor num");
            }
            break;
        case ARITH:
            // cout << "Type is arith" << endl;
            outValue = *(static_cast<std::string*> (dynamic_pointer_cast<ArithExpr>(val) -> evaluate(p)));
            break;
        default:
            cout << valType << endl;
            throw std::runtime_error("Unexpected expression simple evalSingle. Got Unknown Type");
            break;

    }
    // cout << "returning: " << outValue << endl;
    return outValue;
}

string Expr::evalSingle(shared_ptr<Expr> val, ExprType valType, shared_ptr<Packet> p, 
    vector<string> constraints, vector<string> locationList, vector<string> variableList) {
    string outValue = "";
    // cout << "In evalSingleB" << endl;
    switch(valType) {
        case PACKET:
            if (StringExpr* c = dynamic_cast<StringExpr*>(val.get())) {
                outValue = p -> get(c -> getValue());
            } else if (NumExpr* c = dynamic_cast<NumExpr*>(val.get()))  {
                outValue = p -> get(c -> getValue());
            }
            break;
        case VALUE:
            if (StringExpr* c = dynamic_cast<StringExpr*>(val.get())) {
                // cout << "It is a string value" << endl;
                outValue = c -> getValue();
            } else if (NumExpr* c = dynamic_cast<NumExpr*>(val.get()))  {
                // cout << "It is a num value" << endl;
                outValue = c -> getValue();
            } else {
                throw std::runtime_error("Unexpected expression simple evalSingle. Got Value Type, but niether string nor num");
            }
            break;
        case TIME:
    //         // Convert to string
            outValue = p -> getTime();
            break;
        case RHO:
            outValue = p -> getLocation();
            break;
        case VARIABLE:
            if (StringExpr* c = dynamic_cast<StringExpr*>(val.get())) {
                auto it = std::find (variableList.begin(), variableList.end(), c -> getValue()); 
                int level = locationList.size() + (it - variableList.begin());
                outValue = constraints[level];
            } else {
                outValue = "WRONG VALUE, throw error here";
                throw std::runtime_error("Unexpected expression simple evalSingle. Got Unknown Type in variable");
            }
            
            break;
        case ARITH:
            outValue = dynamic_cast<ArithExpr&>(*val).evaluate(p, constraints, locationList, variableList);
            break;
        default:
            cout << valType << endl;
            throw std::runtime_error("Unexpected expression simple evalSingle. Got Unknown Type in second evalSingle");
            break;
    }
    return outValue;
}