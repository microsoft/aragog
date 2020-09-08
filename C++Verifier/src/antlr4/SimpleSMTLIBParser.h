
// Generated from SimpleSMTLIBParser.g4 by ANTLR 4.8

#pragma once


#include "antlr4-runtime.h"




class  SimpleSMTLIBParser : public antlr4::Parser {
public:
  enum {
    ParOpen = 1, ParClose = 2, PS_And = 3, PS_Or = 4, PS_Not = 5, PS_Eq = 6, 
    PS_Lt = 7, PS_Le = 8, PS_Gt = 9, PS_Ge = 10, PS_Add = 11, PS_Sub = 12, 
    PS_Div = 13, PS_Mul = 14, PIPE = 15, DOUBLE_QUOTE = 16, PS_False = 17, 
    PS_True = 18, GRW_Binary = 19, GRW_Decimal = 20, GRW_Exists = 21, GRW_Hexadecimal = 22, 
    GRW_Forall = 23, GRW_Let = 24, GRW_Match = 25, GRW_Numeral = 26, GRW_Par = 27, 
    GRW_String = 28, GRW_Ite = 29, Numeral = 30, Binary = 31, HexDecimal = 32, 
    Decimal = 33, UndefinedSymbol = 34, WHITESPACE = 35
  };

  enum {
    RuleStartBool = 0, RuleStartArith = 1, RuleBoolExpression = 2, RuleBoolOp = 3, 
    RuleArithExpression = 4, RuleCompOp = 5, RuleArithOp = 6, RuleVarBinding = 7, 
    RuleBoundVar = 8, RuleTerminal = 9
  };

  SimpleSMTLIBParser(antlr4::TokenStream *input);
  ~SimpleSMTLIBParser();

  virtual std::string getGrammarFileName() const override;
  virtual const antlr4::atn::ATN& getATN() const override { return _atn; };
  virtual const std::vector<std::string>& getTokenNames() const override { return _tokenNames; }; // deprecated: use vocabulary instead.
  virtual const std::vector<std::string>& getRuleNames() const override;
  virtual antlr4::dfa::Vocabulary& getVocabulary() const override;


  class StartBoolContext;
  class StartArithContext;
  class BoolExpressionContext;
  class BoolOpContext;
  class ArithExpressionContext;
  class CompOpContext;
  class ArithOpContext;
  class VarBindingContext;
  class BoundVarContext;
  class TerminalContext; 

  class  StartBoolContext : public antlr4::ParserRuleContext {
  public:
    StartBoolContext(antlr4::ParserRuleContext *parent, size_t invokingState);
    virtual size_t getRuleIndex() const override;
    BoolExpressionContext *boolExpression();
    antlr4::tree::TerminalNode *EOF();

    virtual void enterRule(antlr4::tree::ParseTreeListener *listener) override;
    virtual void exitRule(antlr4::tree::ParseTreeListener *listener) override;
   
  };

  StartBoolContext* startBool();

  class  StartArithContext : public antlr4::ParserRuleContext {
  public:
    StartArithContext(antlr4::ParserRuleContext *parent, size_t invokingState);
    virtual size_t getRuleIndex() const override;
    ArithExpressionContext *arithExpression();
    antlr4::tree::TerminalNode *EOF();

    virtual void enterRule(antlr4::tree::ParseTreeListener *listener) override;
    virtual void exitRule(antlr4::tree::ParseTreeListener *listener) override;
   
  };

  StartArithContext* startArith();

  class  BoolExpressionContext : public antlr4::ParserRuleContext {
  public:
    BoolExpressionContext(antlr4::ParserRuleContext *parent, size_t invokingState);
    virtual size_t getRuleIndex() const override;
    std::vector<antlr4::tree::TerminalNode *> ParOpen();
    antlr4::tree::TerminalNode* ParOpen(size_t i);
    BoolOpContext *boolOp();
    std::vector<antlr4::tree::TerminalNode *> ParClose();
    antlr4::tree::TerminalNode* ParClose(size_t i);
    std::vector<BoolExpressionContext *> boolExpression();
    BoolExpressionContext* boolExpression(size_t i);
    CompOpContext *compOp();
    std::vector<ArithExpressionContext *> arithExpression();
    ArithExpressionContext* arithExpression(size_t i);
    antlr4::tree::TerminalNode *GRW_Let();
    std::vector<VarBindingContext *> varBinding();
    VarBindingContext* varBinding(size_t i);
    BoundVarContext *boundVar();
    antlr4::tree::TerminalNode *PS_True();
    antlr4::tree::TerminalNode *PS_False();

    virtual void enterRule(antlr4::tree::ParseTreeListener *listener) override;
    virtual void exitRule(antlr4::tree::ParseTreeListener *listener) override;
   
  };

  BoolExpressionContext* boolExpression();

  class  BoolOpContext : public antlr4::ParserRuleContext {
  public:
    BoolOpContext(antlr4::ParserRuleContext *parent, size_t invokingState);
    virtual size_t getRuleIndex() const override;
    antlr4::tree::TerminalNode *PS_And();
    antlr4::tree::TerminalNode *PS_Or();
    antlr4::tree::TerminalNode *PS_Not();

    virtual void enterRule(antlr4::tree::ParseTreeListener *listener) override;
    virtual void exitRule(antlr4::tree::ParseTreeListener *listener) override;
   
  };

  BoolOpContext* boolOp();

  class  ArithExpressionContext : public antlr4::ParserRuleContext {
  public:
    ArithExpressionContext(antlr4::ParserRuleContext *parent, size_t invokingState);
    virtual size_t getRuleIndex() const override;
    antlr4::tree::TerminalNode *ParOpen();
    antlr4::tree::TerminalNode *GRW_Ite();
    BoolExpressionContext *boolExpression();
    std::vector<ArithExpressionContext *> arithExpression();
    ArithExpressionContext* arithExpression(size_t i);
    antlr4::tree::TerminalNode *ParClose();
    ArithOpContext *arithOp();
    TerminalContext *terminal();

    virtual void enterRule(antlr4::tree::ParseTreeListener *listener) override;
    virtual void exitRule(antlr4::tree::ParseTreeListener *listener) override;
   
  };

  ArithExpressionContext* arithExpression();

  class  CompOpContext : public antlr4::ParserRuleContext {
  public:
    CompOpContext(antlr4::ParserRuleContext *parent, size_t invokingState);
    virtual size_t getRuleIndex() const override;
    antlr4::tree::TerminalNode *PS_Eq();
    antlr4::tree::TerminalNode *PS_Lt();
    antlr4::tree::TerminalNode *PS_Le();
    antlr4::tree::TerminalNode *PS_Gt();
    antlr4::tree::TerminalNode *PS_Ge();

    virtual void enterRule(antlr4::tree::ParseTreeListener *listener) override;
    virtual void exitRule(antlr4::tree::ParseTreeListener *listener) override;
   
  };

  CompOpContext* compOp();

  class  ArithOpContext : public antlr4::ParserRuleContext {
  public:
    ArithOpContext(antlr4::ParserRuleContext *parent, size_t invokingState);
    virtual size_t getRuleIndex() const override;
    antlr4::tree::TerminalNode *PS_Add();
    antlr4::tree::TerminalNode *PS_Sub();
    antlr4::tree::TerminalNode *PS_Mul();
    antlr4::tree::TerminalNode *PS_Div();

    virtual void enterRule(antlr4::tree::ParseTreeListener *listener) override;
    virtual void exitRule(antlr4::tree::ParseTreeListener *listener) override;
   
  };

  ArithOpContext* arithOp();

  class  VarBindingContext : public antlr4::ParserRuleContext {
  public:
    VarBindingContext(antlr4::ParserRuleContext *parent, size_t invokingState);
    virtual size_t getRuleIndex() const override;
    antlr4::tree::TerminalNode *ParOpen();
    antlr4::tree::TerminalNode *UndefinedSymbol();
    BoolExpressionContext *boolExpression();
    antlr4::tree::TerminalNode *ParClose();

    virtual void enterRule(antlr4::tree::ParseTreeListener *listener) override;
    virtual void exitRule(antlr4::tree::ParseTreeListener *listener) override;
   
  };

  VarBindingContext* varBinding();

  class  BoundVarContext : public antlr4::ParserRuleContext {
  public:
    BoundVarContext(antlr4::ParserRuleContext *parent, size_t invokingState);
    virtual size_t getRuleIndex() const override;
    antlr4::tree::TerminalNode *UndefinedSymbol();

    virtual void enterRule(antlr4::tree::ParseTreeListener *listener) override;
    virtual void exitRule(antlr4::tree::ParseTreeListener *listener) override;
   
  };

  BoundVarContext* boundVar();

  class  TerminalContext : public antlr4::ParserRuleContext {
  public:
    TerminalContext(antlr4::ParserRuleContext *parent, size_t invokingState);
    virtual size_t getRuleIndex() const override;
    antlr4::tree::TerminalNode *UndefinedSymbol();
    std::vector<antlr4::tree::TerminalNode *> PIPE();
    antlr4::tree::TerminalNode* PIPE(size_t i);
    std::vector<antlr4::tree::TerminalNode *> DOUBLE_QUOTE();
    antlr4::tree::TerminalNode* DOUBLE_QUOTE(size_t i);
    antlr4::tree::TerminalNode *Numeral();

    virtual void enterRule(antlr4::tree::ParseTreeListener *listener) override;
    virtual void exitRule(antlr4::tree::ParseTreeListener *listener) override;
   
  };

  TerminalContext* terminal();


private:
  static std::vector<antlr4::dfa::DFA> _decisionToDFA;
  static antlr4::atn::PredictionContextCache _sharedContextCache;
  static std::vector<std::string> _ruleNames;
  static std::vector<std::string> _tokenNames;

  static std::vector<std::string> _literalNames;
  static std::vector<std::string> _symbolicNames;
  static antlr4::dfa::Vocabulary _vocabulary;
  static antlr4::atn::ATN _atn;
  static std::vector<uint16_t> _serializedATN;


  struct Initializer {
    Initializer();
  };
  static Initializer _init;
};

