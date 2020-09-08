
// Generated from SimpleSMTLIBParser.g4 by ANTLR 4.8

#pragma once


#include "antlr4-runtime.h"
#include "SimpleSMTLIBParser.h"


/**
 * This interface defines an abstract listener for a parse tree produced by SimpleSMTLIBParser.
 */
class  SimpleSMTLIBParserListener : public antlr4::tree::ParseTreeListener {
public:

  virtual void enterStartBool(SimpleSMTLIBParser::StartBoolContext *ctx) = 0;
  virtual void exitStartBool(SimpleSMTLIBParser::StartBoolContext *ctx) = 0;

  virtual void enterStartArith(SimpleSMTLIBParser::StartArithContext *ctx) = 0;
  virtual void exitStartArith(SimpleSMTLIBParser::StartArithContext *ctx) = 0;

  virtual void enterBoolExpression(SimpleSMTLIBParser::BoolExpressionContext *ctx) = 0;
  virtual void exitBoolExpression(SimpleSMTLIBParser::BoolExpressionContext *ctx) = 0;

  virtual void enterBoolOp(SimpleSMTLIBParser::BoolOpContext *ctx) = 0;
  virtual void exitBoolOp(SimpleSMTLIBParser::BoolOpContext *ctx) = 0;

  virtual void enterArithExpression(SimpleSMTLIBParser::ArithExpressionContext *ctx) = 0;
  virtual void exitArithExpression(SimpleSMTLIBParser::ArithExpressionContext *ctx) = 0;

  virtual void enterCompOp(SimpleSMTLIBParser::CompOpContext *ctx) = 0;
  virtual void exitCompOp(SimpleSMTLIBParser::CompOpContext *ctx) = 0;

  virtual void enterArithOp(SimpleSMTLIBParser::ArithOpContext *ctx) = 0;
  virtual void exitArithOp(SimpleSMTLIBParser::ArithOpContext *ctx) = 0;

  virtual void enterVarBinding(SimpleSMTLIBParser::VarBindingContext *ctx) = 0;
  virtual void exitVarBinding(SimpleSMTLIBParser::VarBindingContext *ctx) = 0;

  virtual void enterBoundVar(SimpleSMTLIBParser::BoundVarContext *ctx) = 0;
  virtual void exitBoundVar(SimpleSMTLIBParser::BoundVarContext *ctx) = 0;

  virtual void enterTerminal(SimpleSMTLIBParser::TerminalContext *ctx) = 0;
  virtual void exitTerminal(SimpleSMTLIBParser::TerminalContext *ctx) = 0;


};

