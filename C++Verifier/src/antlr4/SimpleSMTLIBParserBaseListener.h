
// Generated from SimpleSMTLIBParser.g4 by ANTLR 4.8

#pragma once


#include "antlr4-runtime.h"
#include "SimpleSMTLIBParserListener.h"


/**
 * This class provides an empty implementation of SimpleSMTLIBParserListener,
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
class  SimpleSMTLIBParserBaseListener : public SimpleSMTLIBParserListener {
public:

  virtual void enterStartBool(SimpleSMTLIBParser::StartBoolContext * /*ctx*/) override { }
  virtual void exitStartBool(SimpleSMTLIBParser::StartBoolContext * /*ctx*/) override { }

  virtual void enterStartArith(SimpleSMTLIBParser::StartArithContext * /*ctx*/) override { }
  virtual void exitStartArith(SimpleSMTLIBParser::StartArithContext * /*ctx*/) override { }

  virtual void enterBoolExpression(SimpleSMTLIBParser::BoolExpressionContext * /*ctx*/) override { }
  virtual void exitBoolExpression(SimpleSMTLIBParser::BoolExpressionContext * /*ctx*/) override { }

  virtual void enterBoolOp(SimpleSMTLIBParser::BoolOpContext * /*ctx*/) override { }
  virtual void exitBoolOp(SimpleSMTLIBParser::BoolOpContext * /*ctx*/) override { }

  virtual void enterArithExpression(SimpleSMTLIBParser::ArithExpressionContext * /*ctx*/) override { }
  virtual void exitArithExpression(SimpleSMTLIBParser::ArithExpressionContext * /*ctx*/) override { }

  virtual void enterCompOp(SimpleSMTLIBParser::CompOpContext * /*ctx*/) override { }
  virtual void exitCompOp(SimpleSMTLIBParser::CompOpContext * /*ctx*/) override { }

  virtual void enterArithOp(SimpleSMTLIBParser::ArithOpContext * /*ctx*/) override { }
  virtual void exitArithOp(SimpleSMTLIBParser::ArithOpContext * /*ctx*/) override { }

  virtual void enterVarBinding(SimpleSMTLIBParser::VarBindingContext * /*ctx*/) override { }
  virtual void exitVarBinding(SimpleSMTLIBParser::VarBindingContext * /*ctx*/) override { }

  virtual void enterBoundVar(SimpleSMTLIBParser::BoundVarContext * /*ctx*/) override { }
  virtual void exitBoundVar(SimpleSMTLIBParser::BoundVarContext * /*ctx*/) override { }

  virtual void enterTerminal(SimpleSMTLIBParser::TerminalContext * /*ctx*/) override { }
  virtual void exitTerminal(SimpleSMTLIBParser::TerminalContext * /*ctx*/) override { }


  virtual void enterEveryRule(antlr4::ParserRuleContext * /*ctx*/) override { }
  virtual void exitEveryRule(antlr4::ParserRuleContext * /*ctx*/) override { }
  virtual void visitTerminal(antlr4::tree::TerminalNode * /*node*/) override { }
  virtual void visitErrorNode(antlr4::tree::ErrorNode * /*node*/) override { }

};

