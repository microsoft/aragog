#ifndef NEGCONTEXT_H
#define NEGCONTEXT_H

#include "Expr.h"

class NegativeContext : public Expr {
public:
    using Expr::Expr;
    string toString() {
        return "NegativeContext";
    }
};

#endif
