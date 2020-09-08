#ifndef EXPR_H
#define EXPR_H

#include "../utils/Packet.h"

using namespace std;

class Expr {
    public:
        enum ExprType{
            PACKET, VALUE, ARITH, VARIABLE, BOOL, TIME, RHO, LOCATION_VAR
        };
        shared_ptr<Expr> left;
        shared_ptr<Expr> right;
    protected:
        
        ExprType leftType;
        ExprType rightType;
        

    private:
        void init(shared_ptr<Expr> _left, shared_ptr<Expr> _right) {
            left = _left;
            leftType = parseType(_left);
            right = _right;
            rightType = parseType(_right);
        }
        void init(shared_ptr<Expr> _left) {
            left = _left;
            leftType = parseType(_left);
            right = nullptr;
            // rightType = NULL;
        }

        static Expr::ExprType parseType(shared_ptr<Expr> o);


    public:

        
        Expr(shared_ptr<Expr> _left, shared_ptr<Expr> _right) {
            // cout << "In Expr constructor2" << endl;
            // cout << "In Expr Constructor2" << endl;
            // cout << "Left: " << _left -> toString() << endl;
            // cout << "Right: " << _right -> toString() << endl;
            init(_left, _right);
            // cout << "Left Type: " << leftType << endl;
            // cout << "Right Type: " << rightType << endl;
            // cout << "Out Expr constructor" << endl;
        };
        Expr(shared_ptr<Expr> left) {
            // cout << "In Expr constructor1" << endl;
            init(left);
        };
        Expr(){
            // cout << "In Expr constructor0" << endl;
        };
        Expr(shared_ptr<Expr> _left, shared_ptr<Expr> _right, shared_ptr<vector<string>> locationReferences, 
            shared_ptr<unordered_map<string, string>> variableComparisons);

        string evalSingle(shared_ptr<Expr> val, ExprType valType, shared_ptr<Packet> p);

        string evalSingle(shared_ptr<Expr> val, ExprType valType, shared_ptr<Packet> p, 
            vector<string> constraints, vector<string> locationList, vector<string> variableList);
        // virtual void* evaluate(Packet p) {};
        virtual ~Expr() {};

        virtual string toString() = 0;
};

#endif