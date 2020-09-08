#ifndef NUMEXPR_H
#define NUMEXPR_H

#include "Expr.h"

using namespace std;

class NumExpr : public Expr {
    long value;
    public:
        NumExpr(long s) {
            // cout << "Creating NumExpr with value: " << s << endl;
            value = s;
        }

        string getValue() {

            return to_string(value);
        }

        string toString() {
            // cout << "Current value is " << value << endl;
            return getValue();
        }
};


#endif