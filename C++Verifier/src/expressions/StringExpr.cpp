#ifndef STRINGEXPR_H
#define STRINGEXPR_H

#include "../utils/utils.cpp"
#include "Expr.h"
#include <regex>
// #include <boost/algorithm/string.hpp>
using namespace std;



class StringExpr : public Expr {
    string value;
    std::regex e;
    public:
        StringExpr(string s) {
            if (startsWith(s,"$")) {
                leftType = VARIABLE;
            } else if (startsWith(s,"META_rho")) {
                leftType = RHO;
            } else if (startsWith(s,"TIME")) {
                leftType = TIME;
            }
            value = s;
            e = regex("^.\".+\".$");
        }

        string getValue() {
            return value;
        }

        void trimValue() {
            value = boost::to_lower_copy(value.substr(2,value.length()-4));
        }

        string toString() {
            return value;
        }
};

#endif