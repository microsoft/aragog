#ifndef ITEEXPR_H
#define ITEEXPR_H

#include <vector>
// #include "../utils/Packet.cpp"
#include "BoolExpr.cpp"

using namespace std;

class IteExpr: public Expr {
public:
    IteExpr(shared_ptr<BoolExpr> _condition, shared_ptr<Expr> left, shared_ptr<Expr> right) : Expr(move(left), move(right)) {
        condition = move(_condition);
    }

    void* evaluate(shared_ptr<Packet> p) {

        bool branch = (!!static_cast<int>(reinterpret_cast<std::uintptr_t>(condition -> evaluate(p))));
        string* ret = nullptr;
        if (branch) {
            *ret = evalSingle(left, leftType, p);
        } else {
            *ret = evalSingle(right, rightType, p);
        }
        return static_cast<void*>(ret);
    }

    string toString() {
        return "( ITE " + (condition -> toString()) + " ? " + (left -> toString()) + " : " + (right -> toString()) + ")";
    }

private:
    shared_ptr<BoolExpr> condition;
};

#endif