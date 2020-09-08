
// Generated from SimpleSMTLIBParser.g4 by ANTLR 4.8


#include "SimpleSMTLIBParserListener.h"

#include "SimpleSMTLIBParser.h"


using namespace antlrcpp;
using namespace antlr4;

SimpleSMTLIBParser::SimpleSMTLIBParser(TokenStream *input) : Parser(input) {
  _interpreter = new atn::ParserATNSimulator(this, _atn, _decisionToDFA, _sharedContextCache);
}

SimpleSMTLIBParser::~SimpleSMTLIBParser() {
  delete _interpreter;
}

std::string SimpleSMTLIBParser::getGrammarFileName() const {
  return "SimpleSMTLIBParser.g4";
}

const std::vector<std::string>& SimpleSMTLIBParser::getRuleNames() const {
  return _ruleNames;
}

dfa::Vocabulary& SimpleSMTLIBParser::getVocabulary() const {
  return _vocabulary;
}


//----------------- StartBoolContext ------------------------------------------------------------------

SimpleSMTLIBParser::StartBoolContext::StartBoolContext(ParserRuleContext *parent, size_t invokingState)
  : ParserRuleContext(parent, invokingState) {
}

SimpleSMTLIBParser::BoolExpressionContext* SimpleSMTLIBParser::StartBoolContext::boolExpression() {
  return getRuleContext<SimpleSMTLIBParser::BoolExpressionContext>(0);
}

tree::TerminalNode* SimpleSMTLIBParser::StartBoolContext::EOF() {
  return getToken(SimpleSMTLIBParser::EOF, 0);
}


size_t SimpleSMTLIBParser::StartBoolContext::getRuleIndex() const {
  return SimpleSMTLIBParser::RuleStartBool;
}

void SimpleSMTLIBParser::StartBoolContext::enterRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->enterStartBool(this);
}

void SimpleSMTLIBParser::StartBoolContext::exitRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->exitStartBool(this);
}

SimpleSMTLIBParser::StartBoolContext* SimpleSMTLIBParser::startBool() {
  StartBoolContext *_localctx = _tracker.createInstance<StartBoolContext>(_ctx, getState());
  enterRule(_localctx, 0, SimpleSMTLIBParser::RuleStartBool);

  auto onExit = finally([=] {
    exitRule();
  });
  try {
    enterOuterAlt(_localctx, 1);
    setState(20);
    boolExpression();
    setState(21);
    match(SimpleSMTLIBParser::EOF);
   
  }
  catch (RecognitionException &e) {
    _errHandler->reportError(this, e);
    _localctx->exception = std::current_exception();
    _errHandler->recover(this, _localctx->exception);
  }

  return _localctx;
}

//----------------- StartArithContext ------------------------------------------------------------------

SimpleSMTLIBParser::StartArithContext::StartArithContext(ParserRuleContext *parent, size_t invokingState)
  : ParserRuleContext(parent, invokingState) {
}

SimpleSMTLIBParser::ArithExpressionContext* SimpleSMTLIBParser::StartArithContext::arithExpression() {
  return getRuleContext<SimpleSMTLIBParser::ArithExpressionContext>(0);
}

tree::TerminalNode* SimpleSMTLIBParser::StartArithContext::EOF() {
  return getToken(SimpleSMTLIBParser::EOF, 0);
}


size_t SimpleSMTLIBParser::StartArithContext::getRuleIndex() const {
  return SimpleSMTLIBParser::RuleStartArith;
}

void SimpleSMTLIBParser::StartArithContext::enterRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->enterStartArith(this);
}

void SimpleSMTLIBParser::StartArithContext::exitRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->exitStartArith(this);
}

SimpleSMTLIBParser::StartArithContext* SimpleSMTLIBParser::startArith() {
  StartArithContext *_localctx = _tracker.createInstance<StartArithContext>(_ctx, getState());
  enterRule(_localctx, 2, SimpleSMTLIBParser::RuleStartArith);

  auto onExit = finally([=] {
    exitRule();
  });
  try {
    enterOuterAlt(_localctx, 1);
    setState(23);
    arithExpression();
    setState(24);
    match(SimpleSMTLIBParser::EOF);
   
  }
  catch (RecognitionException &e) {
    _errHandler->reportError(this, e);
    _localctx->exception = std::current_exception();
    _errHandler->recover(this, _localctx->exception);
  }

  return _localctx;
}

//----------------- BoolExpressionContext ------------------------------------------------------------------

SimpleSMTLIBParser::BoolExpressionContext::BoolExpressionContext(ParserRuleContext *parent, size_t invokingState)
  : ParserRuleContext(parent, invokingState) {
}

std::vector<tree::TerminalNode *> SimpleSMTLIBParser::BoolExpressionContext::ParOpen() {
  return getTokens(SimpleSMTLIBParser::ParOpen);
}

tree::TerminalNode* SimpleSMTLIBParser::BoolExpressionContext::ParOpen(size_t i) {
  return getToken(SimpleSMTLIBParser::ParOpen, i);
}

SimpleSMTLIBParser::BoolOpContext* SimpleSMTLIBParser::BoolExpressionContext::boolOp() {
  return getRuleContext<SimpleSMTLIBParser::BoolOpContext>(0);
}

std::vector<tree::TerminalNode *> SimpleSMTLIBParser::BoolExpressionContext::ParClose() {
  return getTokens(SimpleSMTLIBParser::ParClose);
}

tree::TerminalNode* SimpleSMTLIBParser::BoolExpressionContext::ParClose(size_t i) {
  return getToken(SimpleSMTLIBParser::ParClose, i);
}

std::vector<SimpleSMTLIBParser::BoolExpressionContext *> SimpleSMTLIBParser::BoolExpressionContext::boolExpression() {
  return getRuleContexts<SimpleSMTLIBParser::BoolExpressionContext>();
}

SimpleSMTLIBParser::BoolExpressionContext* SimpleSMTLIBParser::BoolExpressionContext::boolExpression(size_t i) {
  return getRuleContext<SimpleSMTLIBParser::BoolExpressionContext>(i);
}

SimpleSMTLIBParser::CompOpContext* SimpleSMTLIBParser::BoolExpressionContext::compOp() {
  return getRuleContext<SimpleSMTLIBParser::CompOpContext>(0);
}

std::vector<SimpleSMTLIBParser::ArithExpressionContext *> SimpleSMTLIBParser::BoolExpressionContext::arithExpression() {
  return getRuleContexts<SimpleSMTLIBParser::ArithExpressionContext>();
}

SimpleSMTLIBParser::ArithExpressionContext* SimpleSMTLIBParser::BoolExpressionContext::arithExpression(size_t i) {
  return getRuleContext<SimpleSMTLIBParser::ArithExpressionContext>(i);
}

tree::TerminalNode* SimpleSMTLIBParser::BoolExpressionContext::GRW_Let() {
  return getToken(SimpleSMTLIBParser::GRW_Let, 0);
}

std::vector<SimpleSMTLIBParser::VarBindingContext *> SimpleSMTLIBParser::BoolExpressionContext::varBinding() {
  return getRuleContexts<SimpleSMTLIBParser::VarBindingContext>();
}

SimpleSMTLIBParser::VarBindingContext* SimpleSMTLIBParser::BoolExpressionContext::varBinding(size_t i) {
  return getRuleContext<SimpleSMTLIBParser::VarBindingContext>(i);
}

SimpleSMTLIBParser::BoundVarContext* SimpleSMTLIBParser::BoolExpressionContext::boundVar() {
  return getRuleContext<SimpleSMTLIBParser::BoundVarContext>(0);
}

tree::TerminalNode* SimpleSMTLIBParser::BoolExpressionContext::PS_True() {
  return getToken(SimpleSMTLIBParser::PS_True, 0);
}

tree::TerminalNode* SimpleSMTLIBParser::BoolExpressionContext::PS_False() {
  return getToken(SimpleSMTLIBParser::PS_False, 0);
}


size_t SimpleSMTLIBParser::BoolExpressionContext::getRuleIndex() const {
  return SimpleSMTLIBParser::RuleBoolExpression;
}

void SimpleSMTLIBParser::BoolExpressionContext::enterRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->enterBoolExpression(this);
}

void SimpleSMTLIBParser::BoolExpressionContext::exitRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->exitBoolExpression(this);
}

SimpleSMTLIBParser::BoolExpressionContext* SimpleSMTLIBParser::boolExpression() {
  BoolExpressionContext *_localctx = _tracker.createInstance<BoolExpressionContext>(_ctx, getState());
  enterRule(_localctx, 4, SimpleSMTLIBParser::RuleBoolExpression);
  size_t _la = 0;

  auto onExit = finally([=] {
    exitRule();
  });
  try {
    setState(56);
    _errHandler->sync(this);
    switch (getInterpreter<atn::ParserATNSimulator>()->adaptivePredict(_input, 2, _ctx)) {
    case 1: {
      enterOuterAlt(_localctx, 1);
      setState(26);
      match(SimpleSMTLIBParser::ParOpen);
      setState(27);
      boolOp();
      setState(29); 
      _errHandler->sync(this);
      _la = _input->LA(1);
      do {
        setState(28);
        boolExpression();
        setState(31); 
        _errHandler->sync(this);
        _la = _input->LA(1);
      } while ((((_la & ~ 0x3fULL) == 0) &&
        ((1ULL << _la) & ((1ULL << SimpleSMTLIBParser::ParOpen)
        | (1ULL << SimpleSMTLIBParser::PS_False)
        | (1ULL << SimpleSMTLIBParser::PS_True)
        | (1ULL << SimpleSMTLIBParser::UndefinedSymbol))) != 0));
      setState(33);
      match(SimpleSMTLIBParser::ParClose);
      break;
    }

    case 2: {
      enterOuterAlt(_localctx, 2);
      setState(35);
      match(SimpleSMTLIBParser::ParOpen);
      setState(36);
      compOp();
      setState(37);
      arithExpression();
      setState(38);
      arithExpression();
      setState(39);
      match(SimpleSMTLIBParser::ParClose);
      break;
    }

    case 3: {
      enterOuterAlt(_localctx, 3);
      setState(41);
      match(SimpleSMTLIBParser::ParOpen);
      setState(42);
      match(SimpleSMTLIBParser::GRW_Let);
      setState(43);
      match(SimpleSMTLIBParser::ParOpen);
      setState(45); 
      _errHandler->sync(this);
      _la = _input->LA(1);
      do {
        setState(44);
        varBinding();
        setState(47); 
        _errHandler->sync(this);
        _la = _input->LA(1);
      } while (_la == SimpleSMTLIBParser::ParOpen);
      setState(49);
      match(SimpleSMTLIBParser::ParClose);
      setState(50);
      boolExpression();
      setState(51);
      match(SimpleSMTLIBParser::ParClose);
      break;
    }

    case 4: {
      enterOuterAlt(_localctx, 4);
      setState(53);
      boundVar();
      break;
    }

    case 5: {
      enterOuterAlt(_localctx, 5);
      setState(54);
      match(SimpleSMTLIBParser::PS_True);
      break;
    }

    case 6: {
      enterOuterAlt(_localctx, 6);
      setState(55);
      match(SimpleSMTLIBParser::PS_False);
      break;
    }

    }
   
  }
  catch (RecognitionException &e) {
    _errHandler->reportError(this, e);
    _localctx->exception = std::current_exception();
    _errHandler->recover(this, _localctx->exception);
  }

  return _localctx;
}

//----------------- BoolOpContext ------------------------------------------------------------------

SimpleSMTLIBParser::BoolOpContext::BoolOpContext(ParserRuleContext *parent, size_t invokingState)
  : ParserRuleContext(parent, invokingState) {
}

tree::TerminalNode* SimpleSMTLIBParser::BoolOpContext::PS_And() {
  return getToken(SimpleSMTLIBParser::PS_And, 0);
}

tree::TerminalNode* SimpleSMTLIBParser::BoolOpContext::PS_Or() {
  return getToken(SimpleSMTLIBParser::PS_Or, 0);
}

tree::TerminalNode* SimpleSMTLIBParser::BoolOpContext::PS_Not() {
  return getToken(SimpleSMTLIBParser::PS_Not, 0);
}


size_t SimpleSMTLIBParser::BoolOpContext::getRuleIndex() const {
  return SimpleSMTLIBParser::RuleBoolOp;
}

void SimpleSMTLIBParser::BoolOpContext::enterRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->enterBoolOp(this);
}

void SimpleSMTLIBParser::BoolOpContext::exitRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->exitBoolOp(this);
}

SimpleSMTLIBParser::BoolOpContext* SimpleSMTLIBParser::boolOp() {
  BoolOpContext *_localctx = _tracker.createInstance<BoolOpContext>(_ctx, getState());
  enterRule(_localctx, 6, SimpleSMTLIBParser::RuleBoolOp);
  size_t _la = 0;

  auto onExit = finally([=] {
    exitRule();
  });
  try {
    enterOuterAlt(_localctx, 1);
    setState(58);
    _la = _input->LA(1);
    if (!((((_la & ~ 0x3fULL) == 0) &&
      ((1ULL << _la) & ((1ULL << SimpleSMTLIBParser::PS_And)
      | (1ULL << SimpleSMTLIBParser::PS_Or)
      | (1ULL << SimpleSMTLIBParser::PS_Not))) != 0))) {
    _errHandler->recoverInline(this);
    }
    else {
      _errHandler->reportMatch(this);
      consume();
    }
   
  }
  catch (RecognitionException &e) {
    _errHandler->reportError(this, e);
    _localctx->exception = std::current_exception();
    _errHandler->recover(this, _localctx->exception);
  }

  return _localctx;
}

//----------------- ArithExpressionContext ------------------------------------------------------------------

SimpleSMTLIBParser::ArithExpressionContext::ArithExpressionContext(ParserRuleContext *parent, size_t invokingState)
  : ParserRuleContext(parent, invokingState) {
}

tree::TerminalNode* SimpleSMTLIBParser::ArithExpressionContext::ParOpen() {
  return getToken(SimpleSMTLIBParser::ParOpen, 0);
}

tree::TerminalNode* SimpleSMTLIBParser::ArithExpressionContext::GRW_Ite() {
  return getToken(SimpleSMTLIBParser::GRW_Ite, 0);
}

SimpleSMTLIBParser::BoolExpressionContext* SimpleSMTLIBParser::ArithExpressionContext::boolExpression() {
  return getRuleContext<SimpleSMTLIBParser::BoolExpressionContext>(0);
}

std::vector<SimpleSMTLIBParser::ArithExpressionContext *> SimpleSMTLIBParser::ArithExpressionContext::arithExpression() {
  return getRuleContexts<SimpleSMTLIBParser::ArithExpressionContext>();
}

SimpleSMTLIBParser::ArithExpressionContext* SimpleSMTLIBParser::ArithExpressionContext::arithExpression(size_t i) {
  return getRuleContext<SimpleSMTLIBParser::ArithExpressionContext>(i);
}

tree::TerminalNode* SimpleSMTLIBParser::ArithExpressionContext::ParClose() {
  return getToken(SimpleSMTLIBParser::ParClose, 0);
}

SimpleSMTLIBParser::ArithOpContext* SimpleSMTLIBParser::ArithExpressionContext::arithOp() {
  return getRuleContext<SimpleSMTLIBParser::ArithOpContext>(0);
}

SimpleSMTLIBParser::TerminalContext* SimpleSMTLIBParser::ArithExpressionContext::terminal() {
  return getRuleContext<SimpleSMTLIBParser::TerminalContext>(0);
}


size_t SimpleSMTLIBParser::ArithExpressionContext::getRuleIndex() const {
  return SimpleSMTLIBParser::RuleArithExpression;
}

void SimpleSMTLIBParser::ArithExpressionContext::enterRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->enterArithExpression(this);
}

void SimpleSMTLIBParser::ArithExpressionContext::exitRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->exitArithExpression(this);
}

SimpleSMTLIBParser::ArithExpressionContext* SimpleSMTLIBParser::arithExpression() {
  ArithExpressionContext *_localctx = _tracker.createInstance<ArithExpressionContext>(_ctx, getState());
  enterRule(_localctx, 8, SimpleSMTLIBParser::RuleArithExpression);
  size_t _la = 0;

  auto onExit = finally([=] {
    exitRule();
  });
  try {
    setState(77);
    _errHandler->sync(this);
    switch (getInterpreter<atn::ParserATNSimulator>()->adaptivePredict(_input, 4, _ctx)) {
    case 1: {
      enterOuterAlt(_localctx, 1);
      setState(60);
      match(SimpleSMTLIBParser::ParOpen);
      setState(61);
      match(SimpleSMTLIBParser::GRW_Ite);
      setState(62);
      boolExpression();
      setState(63);
      arithExpression();
      setState(64);
      arithExpression();
      setState(65);
      match(SimpleSMTLIBParser::ParClose);
      break;
    }

    case 2: {
      enterOuterAlt(_localctx, 2);
      setState(67);
      match(SimpleSMTLIBParser::ParOpen);
      setState(68);
      arithOp();
      setState(70); 
      _errHandler->sync(this);
      _la = _input->LA(1);
      do {
        setState(69);
        arithExpression();
        setState(72); 
        _errHandler->sync(this);
        _la = _input->LA(1);
      } while ((((_la & ~ 0x3fULL) == 0) &&
        ((1ULL << _la) & ((1ULL << SimpleSMTLIBParser::ParOpen)
        | (1ULL << SimpleSMTLIBParser::PIPE)
        | (1ULL << SimpleSMTLIBParser::Numeral)
        | (1ULL << SimpleSMTLIBParser::UndefinedSymbol))) != 0));
      setState(74);
      match(SimpleSMTLIBParser::ParClose);
      break;
    }

    case 3: {
      enterOuterAlt(_localctx, 3);
      setState(76);
      terminal();
      break;
    }

    }
   
  }
  catch (RecognitionException &e) {
    _errHandler->reportError(this, e);
    _localctx->exception = std::current_exception();
    _errHandler->recover(this, _localctx->exception);
  }

  return _localctx;
}

//----------------- CompOpContext ------------------------------------------------------------------

SimpleSMTLIBParser::CompOpContext::CompOpContext(ParserRuleContext *parent, size_t invokingState)
  : ParserRuleContext(parent, invokingState) {
}

tree::TerminalNode* SimpleSMTLIBParser::CompOpContext::PS_Eq() {
  return getToken(SimpleSMTLIBParser::PS_Eq, 0);
}

tree::TerminalNode* SimpleSMTLIBParser::CompOpContext::PS_Lt() {
  return getToken(SimpleSMTLIBParser::PS_Lt, 0);
}

tree::TerminalNode* SimpleSMTLIBParser::CompOpContext::PS_Le() {
  return getToken(SimpleSMTLIBParser::PS_Le, 0);
}

tree::TerminalNode* SimpleSMTLIBParser::CompOpContext::PS_Gt() {
  return getToken(SimpleSMTLIBParser::PS_Gt, 0);
}

tree::TerminalNode* SimpleSMTLIBParser::CompOpContext::PS_Ge() {
  return getToken(SimpleSMTLIBParser::PS_Ge, 0);
}


size_t SimpleSMTLIBParser::CompOpContext::getRuleIndex() const {
  return SimpleSMTLIBParser::RuleCompOp;
}

void SimpleSMTLIBParser::CompOpContext::enterRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->enterCompOp(this);
}

void SimpleSMTLIBParser::CompOpContext::exitRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->exitCompOp(this);
}

SimpleSMTLIBParser::CompOpContext* SimpleSMTLIBParser::compOp() {
  CompOpContext *_localctx = _tracker.createInstance<CompOpContext>(_ctx, getState());
  enterRule(_localctx, 10, SimpleSMTLIBParser::RuleCompOp);
  size_t _la = 0;

  auto onExit = finally([=] {
    exitRule();
  });
  try {
    enterOuterAlt(_localctx, 1);
    setState(79);
    _la = _input->LA(1);
    if (!((((_la & ~ 0x3fULL) == 0) &&
      ((1ULL << _la) & ((1ULL << SimpleSMTLIBParser::PS_Eq)
      | (1ULL << SimpleSMTLIBParser::PS_Lt)
      | (1ULL << SimpleSMTLIBParser::PS_Le)
      | (1ULL << SimpleSMTLIBParser::PS_Gt)
      | (1ULL << SimpleSMTLIBParser::PS_Ge))) != 0))) {
    _errHandler->recoverInline(this);
    }
    else {
      _errHandler->reportMatch(this);
      consume();
    }
   
  }
  catch (RecognitionException &e) {
    _errHandler->reportError(this, e);
    _localctx->exception = std::current_exception();
    _errHandler->recover(this, _localctx->exception);
  }

  return _localctx;
}

//----------------- ArithOpContext ------------------------------------------------------------------

SimpleSMTLIBParser::ArithOpContext::ArithOpContext(ParserRuleContext *parent, size_t invokingState)
  : ParserRuleContext(parent, invokingState) {
}

tree::TerminalNode* SimpleSMTLIBParser::ArithOpContext::PS_Add() {
  return getToken(SimpleSMTLIBParser::PS_Add, 0);
}

tree::TerminalNode* SimpleSMTLIBParser::ArithOpContext::PS_Sub() {
  return getToken(SimpleSMTLIBParser::PS_Sub, 0);
}

tree::TerminalNode* SimpleSMTLIBParser::ArithOpContext::PS_Mul() {
  return getToken(SimpleSMTLIBParser::PS_Mul, 0);
}

tree::TerminalNode* SimpleSMTLIBParser::ArithOpContext::PS_Div() {
  return getToken(SimpleSMTLIBParser::PS_Div, 0);
}


size_t SimpleSMTLIBParser::ArithOpContext::getRuleIndex() const {
  return SimpleSMTLIBParser::RuleArithOp;
}

void SimpleSMTLIBParser::ArithOpContext::enterRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->enterArithOp(this);
}

void SimpleSMTLIBParser::ArithOpContext::exitRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->exitArithOp(this);
}

SimpleSMTLIBParser::ArithOpContext* SimpleSMTLIBParser::arithOp() {
  ArithOpContext *_localctx = _tracker.createInstance<ArithOpContext>(_ctx, getState());
  enterRule(_localctx, 12, SimpleSMTLIBParser::RuleArithOp);
  size_t _la = 0;

  auto onExit = finally([=] {
    exitRule();
  });
  try {
    enterOuterAlt(_localctx, 1);
    setState(81);
    _la = _input->LA(1);
    if (!((((_la & ~ 0x3fULL) == 0) &&
      ((1ULL << _la) & ((1ULL << SimpleSMTLIBParser::PS_Add)
      | (1ULL << SimpleSMTLIBParser::PS_Sub)
      | (1ULL << SimpleSMTLIBParser::PS_Div)
      | (1ULL << SimpleSMTLIBParser::PS_Mul))) != 0))) {
    _errHandler->recoverInline(this);
    }
    else {
      _errHandler->reportMatch(this);
      consume();
    }
   
  }
  catch (RecognitionException &e) {
    _errHandler->reportError(this, e);
    _localctx->exception = std::current_exception();
    _errHandler->recover(this, _localctx->exception);
  }

  return _localctx;
}

//----------------- VarBindingContext ------------------------------------------------------------------

SimpleSMTLIBParser::VarBindingContext::VarBindingContext(ParserRuleContext *parent, size_t invokingState)
  : ParserRuleContext(parent, invokingState) {
}

tree::TerminalNode* SimpleSMTLIBParser::VarBindingContext::ParOpen() {
  return getToken(SimpleSMTLIBParser::ParOpen, 0);
}

tree::TerminalNode* SimpleSMTLIBParser::VarBindingContext::UndefinedSymbol() {
  return getToken(SimpleSMTLIBParser::UndefinedSymbol, 0);
}

SimpleSMTLIBParser::BoolExpressionContext* SimpleSMTLIBParser::VarBindingContext::boolExpression() {
  return getRuleContext<SimpleSMTLIBParser::BoolExpressionContext>(0);
}

tree::TerminalNode* SimpleSMTLIBParser::VarBindingContext::ParClose() {
  return getToken(SimpleSMTLIBParser::ParClose, 0);
}


size_t SimpleSMTLIBParser::VarBindingContext::getRuleIndex() const {
  return SimpleSMTLIBParser::RuleVarBinding;
}

void SimpleSMTLIBParser::VarBindingContext::enterRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->enterVarBinding(this);
}

void SimpleSMTLIBParser::VarBindingContext::exitRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->exitVarBinding(this);
}

SimpleSMTLIBParser::VarBindingContext* SimpleSMTLIBParser::varBinding() {
  VarBindingContext *_localctx = _tracker.createInstance<VarBindingContext>(_ctx, getState());
  enterRule(_localctx, 14, SimpleSMTLIBParser::RuleVarBinding);

  auto onExit = finally([=] {
    exitRule();
  });
  try {
    enterOuterAlt(_localctx, 1);
    setState(83);
    match(SimpleSMTLIBParser::ParOpen);
    setState(84);
    match(SimpleSMTLIBParser::UndefinedSymbol);
    setState(85);
    boolExpression();
    setState(86);
    match(SimpleSMTLIBParser::ParClose);
   
  }
  catch (RecognitionException &e) {
    _errHandler->reportError(this, e);
    _localctx->exception = std::current_exception();
    _errHandler->recover(this, _localctx->exception);
  }

  return _localctx;
}

//----------------- BoundVarContext ------------------------------------------------------------------

SimpleSMTLIBParser::BoundVarContext::BoundVarContext(ParserRuleContext *parent, size_t invokingState)
  : ParserRuleContext(parent, invokingState) {
}

tree::TerminalNode* SimpleSMTLIBParser::BoundVarContext::UndefinedSymbol() {
  return getToken(SimpleSMTLIBParser::UndefinedSymbol, 0);
}


size_t SimpleSMTLIBParser::BoundVarContext::getRuleIndex() const {
  return SimpleSMTLIBParser::RuleBoundVar;
}

void SimpleSMTLIBParser::BoundVarContext::enterRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->enterBoundVar(this);
}

void SimpleSMTLIBParser::BoundVarContext::exitRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->exitBoundVar(this);
}

SimpleSMTLIBParser::BoundVarContext* SimpleSMTLIBParser::boundVar() {
  BoundVarContext *_localctx = _tracker.createInstance<BoundVarContext>(_ctx, getState());
  enterRule(_localctx, 16, SimpleSMTLIBParser::RuleBoundVar);

  auto onExit = finally([=] {
    exitRule();
  });
  try {
    enterOuterAlt(_localctx, 1);
    setState(88);
    match(SimpleSMTLIBParser::UndefinedSymbol);
   
  }
  catch (RecognitionException &e) {
    _errHandler->reportError(this, e);
    _localctx->exception = std::current_exception();
    _errHandler->recover(this, _localctx->exception);
  }

  return _localctx;
}

//----------------- TerminalContext ------------------------------------------------------------------

SimpleSMTLIBParser::TerminalContext::TerminalContext(ParserRuleContext *parent, size_t invokingState)
  : ParserRuleContext(parent, invokingState) {
}

tree::TerminalNode* SimpleSMTLIBParser::TerminalContext::UndefinedSymbol() {
  return getToken(SimpleSMTLIBParser::UndefinedSymbol, 0);
}

std::vector<tree::TerminalNode *> SimpleSMTLIBParser::TerminalContext::PIPE() {
  return getTokens(SimpleSMTLIBParser::PIPE);
}

tree::TerminalNode* SimpleSMTLIBParser::TerminalContext::PIPE(size_t i) {
  return getToken(SimpleSMTLIBParser::PIPE, i);
}

std::vector<tree::TerminalNode *> SimpleSMTLIBParser::TerminalContext::DOUBLE_QUOTE() {
  return getTokens(SimpleSMTLIBParser::DOUBLE_QUOTE);
}

tree::TerminalNode* SimpleSMTLIBParser::TerminalContext::DOUBLE_QUOTE(size_t i) {
  return getToken(SimpleSMTLIBParser::DOUBLE_QUOTE, i);
}

tree::TerminalNode* SimpleSMTLIBParser::TerminalContext::Numeral() {
  return getToken(SimpleSMTLIBParser::Numeral, 0);
}


size_t SimpleSMTLIBParser::TerminalContext::getRuleIndex() const {
  return SimpleSMTLIBParser::RuleTerminal;
}

void SimpleSMTLIBParser::TerminalContext::enterRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->enterTerminal(this);
}

void SimpleSMTLIBParser::TerminalContext::exitRule(tree::ParseTreeListener *listener) {
  auto parserListener = dynamic_cast<SimpleSMTLIBParserListener *>(listener);
  if (parserListener != nullptr)
    parserListener->exitTerminal(this);
}

SimpleSMTLIBParser::TerminalContext* SimpleSMTLIBParser::terminal() {
  TerminalContext *_localctx = _tracker.createInstance<TerminalContext>(_ctx, getState());
  enterRule(_localctx, 18, SimpleSMTLIBParser::RuleTerminal);

  auto onExit = finally([=] {
    exitRule();
  });
  try {
    setState(97);
    _errHandler->sync(this);
    switch (_input->LA(1)) {
      case SimpleSMTLIBParser::UndefinedSymbol: {
        enterOuterAlt(_localctx, 1);
        setState(90);
        match(SimpleSMTLIBParser::UndefinedSymbol);
        break;
      }

      case SimpleSMTLIBParser::PIPE: {
        enterOuterAlt(_localctx, 2);
        setState(91);
        match(SimpleSMTLIBParser::PIPE);
        setState(92);
        match(SimpleSMTLIBParser::DOUBLE_QUOTE);
        setState(93);
        match(SimpleSMTLIBParser::UndefinedSymbol);
        setState(94);
        match(SimpleSMTLIBParser::DOUBLE_QUOTE);
        setState(95);
        match(SimpleSMTLIBParser::PIPE);
        break;
      }

      case SimpleSMTLIBParser::Numeral: {
        enterOuterAlt(_localctx, 3);
        setState(96);
        match(SimpleSMTLIBParser::Numeral);
        break;
      }

    default:
      throw NoViableAltException(this);
    }
   
  }
  catch (RecognitionException &e) {
    _errHandler->reportError(this, e);
    _localctx->exception = std::current_exception();
    _errHandler->recover(this, _localctx->exception);
  }

  return _localctx;
}

// Static vars and initialization.
std::vector<dfa::DFA> SimpleSMTLIBParser::_decisionToDFA;
atn::PredictionContextCache SimpleSMTLIBParser::_sharedContextCache;

// We own the ATN which in turn owns the ATN states.
atn::ATN SimpleSMTLIBParser::_atn;
std::vector<uint16_t> SimpleSMTLIBParser::_serializedATN;

std::vector<std::string> SimpleSMTLIBParser::_ruleNames = {
  "startBool", "startArith", "boolExpression", "boolOp", "arithExpression", 
  "compOp", "arithOp", "varBinding", "boundVar", "terminal"
};

std::vector<std::string> SimpleSMTLIBParser::_literalNames = {
  "", "'('", "')'", "'and'", "'or'", "'not'", "'='", "'<'", "'<='", "'>'", 
  "'>='", "'+'", "'-'", "'/'", "'*'", "'|'", "'\"'", "'false'", "'true'", 
  "'BINARY'", "'DECIMAL'", "'exists'", "'HEXADECIMAL'", "'forall'", "'let'", 
  "'match'", "'NUMERAL'", "'par'", "'string'", "'ite'"
};

std::vector<std::string> SimpleSMTLIBParser::_symbolicNames = {
  "", "ParOpen", "ParClose", "PS_And", "PS_Or", "PS_Not", "PS_Eq", "PS_Lt", 
  "PS_Le", "PS_Gt", "PS_Ge", "PS_Add", "PS_Sub", "PS_Div", "PS_Mul", "PIPE", 
  "DOUBLE_QUOTE", "PS_False", "PS_True", "GRW_Binary", "GRW_Decimal", "GRW_Exists", 
  "GRW_Hexadecimal", "GRW_Forall", "GRW_Let", "GRW_Match", "GRW_Numeral", 
  "GRW_Par", "GRW_String", "GRW_Ite", "Numeral", "Binary", "HexDecimal", 
  "Decimal", "UndefinedSymbol", "WHITESPACE"
};

dfa::Vocabulary SimpleSMTLIBParser::_vocabulary(_literalNames, _symbolicNames);

std::vector<std::string> SimpleSMTLIBParser::_tokenNames;

SimpleSMTLIBParser::Initializer::Initializer() {
	for (size_t i = 0; i < _symbolicNames.size(); ++i) {
		std::string name = _vocabulary.getLiteralName(i);
		if (name.empty()) {
			name = _vocabulary.getSymbolicName(i);
		}

		if (name.empty()) {
			_tokenNames.push_back("<INVALID>");
		} else {
      _tokenNames.push_back(name);
    }
	}

  _serializedATN = {
    0x3, 0x608b, 0xa72a, 0x8133, 0xb9ed, 0x417c, 0x3be7, 0x7786, 0x5964, 
    0x3, 0x25, 0x66, 0x4, 0x2, 0x9, 0x2, 0x4, 0x3, 0x9, 0x3, 0x4, 0x4, 0x9, 
    0x4, 0x4, 0x5, 0x9, 0x5, 0x4, 0x6, 0x9, 0x6, 0x4, 0x7, 0x9, 0x7, 0x4, 
    0x8, 0x9, 0x8, 0x4, 0x9, 0x9, 0x9, 0x4, 0xa, 0x9, 0xa, 0x4, 0xb, 0x9, 
    0xb, 0x3, 0x2, 0x3, 0x2, 0x3, 0x2, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 
    0x4, 0x3, 0x4, 0x3, 0x4, 0x6, 0x4, 0x20, 0xa, 0x4, 0xd, 0x4, 0xe, 0x4, 
    0x21, 0x3, 0x4, 0x3, 0x4, 0x3, 0x4, 0x3, 0x4, 0x3, 0x4, 0x3, 0x4, 0x3, 
    0x4, 0x3, 0x4, 0x3, 0x4, 0x3, 0x4, 0x3, 0x4, 0x3, 0x4, 0x6, 0x4, 0x30, 
    0xa, 0x4, 0xd, 0x4, 0xe, 0x4, 0x31, 0x3, 0x4, 0x3, 0x4, 0x3, 0x4, 0x3, 
    0x4, 0x3, 0x4, 0x3, 0x4, 0x3, 0x4, 0x5, 0x4, 0x3b, 0xa, 0x4, 0x3, 0x5, 
    0x3, 0x5, 0x3, 0x6, 0x3, 0x6, 0x3, 0x6, 0x3, 0x6, 0x3, 0x6, 0x3, 0x6, 
    0x3, 0x6, 0x3, 0x6, 0x3, 0x6, 0x3, 0x6, 0x6, 0x6, 0x49, 0xa, 0x6, 0xd, 
    0x6, 0xe, 0x6, 0x4a, 0x3, 0x6, 0x3, 0x6, 0x3, 0x6, 0x5, 0x6, 0x50, 0xa, 
    0x6, 0x3, 0x7, 0x3, 0x7, 0x3, 0x8, 0x3, 0x8, 0x3, 0x9, 0x3, 0x9, 0x3, 
    0x9, 0x3, 0x9, 0x3, 0x9, 0x3, 0xa, 0x3, 0xa, 0x3, 0xb, 0x3, 0xb, 0x3, 
    0xb, 0x3, 0xb, 0x3, 0xb, 0x3, 0xb, 0x3, 0xb, 0x5, 0xb, 0x64, 0xa, 0xb, 
    0x3, 0xb, 0x2, 0x2, 0xc, 0x2, 0x4, 0x6, 0x8, 0xa, 0xc, 0xe, 0x10, 0x12, 
    0x14, 0x2, 0x5, 0x3, 0x2, 0x5, 0x7, 0x3, 0x2, 0x8, 0xc, 0x3, 0x2, 0xd, 
    0x10, 0x2, 0x67, 0x2, 0x16, 0x3, 0x2, 0x2, 0x2, 0x4, 0x19, 0x3, 0x2, 
    0x2, 0x2, 0x6, 0x3a, 0x3, 0x2, 0x2, 0x2, 0x8, 0x3c, 0x3, 0x2, 0x2, 0x2, 
    0xa, 0x4f, 0x3, 0x2, 0x2, 0x2, 0xc, 0x51, 0x3, 0x2, 0x2, 0x2, 0xe, 0x53, 
    0x3, 0x2, 0x2, 0x2, 0x10, 0x55, 0x3, 0x2, 0x2, 0x2, 0x12, 0x5a, 0x3, 
    0x2, 0x2, 0x2, 0x14, 0x63, 0x3, 0x2, 0x2, 0x2, 0x16, 0x17, 0x5, 0x6, 
    0x4, 0x2, 0x17, 0x18, 0x7, 0x2, 0x2, 0x3, 0x18, 0x3, 0x3, 0x2, 0x2, 
    0x2, 0x19, 0x1a, 0x5, 0xa, 0x6, 0x2, 0x1a, 0x1b, 0x7, 0x2, 0x2, 0x3, 
    0x1b, 0x5, 0x3, 0x2, 0x2, 0x2, 0x1c, 0x1d, 0x7, 0x3, 0x2, 0x2, 0x1d, 
    0x1f, 0x5, 0x8, 0x5, 0x2, 0x1e, 0x20, 0x5, 0x6, 0x4, 0x2, 0x1f, 0x1e, 
    0x3, 0x2, 0x2, 0x2, 0x20, 0x21, 0x3, 0x2, 0x2, 0x2, 0x21, 0x1f, 0x3, 
    0x2, 0x2, 0x2, 0x21, 0x22, 0x3, 0x2, 0x2, 0x2, 0x22, 0x23, 0x3, 0x2, 
    0x2, 0x2, 0x23, 0x24, 0x7, 0x4, 0x2, 0x2, 0x24, 0x3b, 0x3, 0x2, 0x2, 
    0x2, 0x25, 0x26, 0x7, 0x3, 0x2, 0x2, 0x26, 0x27, 0x5, 0xc, 0x7, 0x2, 
    0x27, 0x28, 0x5, 0xa, 0x6, 0x2, 0x28, 0x29, 0x5, 0xa, 0x6, 0x2, 0x29, 
    0x2a, 0x7, 0x4, 0x2, 0x2, 0x2a, 0x3b, 0x3, 0x2, 0x2, 0x2, 0x2b, 0x2c, 
    0x7, 0x3, 0x2, 0x2, 0x2c, 0x2d, 0x7, 0x1a, 0x2, 0x2, 0x2d, 0x2f, 0x7, 
    0x3, 0x2, 0x2, 0x2e, 0x30, 0x5, 0x10, 0x9, 0x2, 0x2f, 0x2e, 0x3, 0x2, 
    0x2, 0x2, 0x30, 0x31, 0x3, 0x2, 0x2, 0x2, 0x31, 0x2f, 0x3, 0x2, 0x2, 
    0x2, 0x31, 0x32, 0x3, 0x2, 0x2, 0x2, 0x32, 0x33, 0x3, 0x2, 0x2, 0x2, 
    0x33, 0x34, 0x7, 0x4, 0x2, 0x2, 0x34, 0x35, 0x5, 0x6, 0x4, 0x2, 0x35, 
    0x36, 0x7, 0x4, 0x2, 0x2, 0x36, 0x3b, 0x3, 0x2, 0x2, 0x2, 0x37, 0x3b, 
    0x5, 0x12, 0xa, 0x2, 0x38, 0x3b, 0x7, 0x14, 0x2, 0x2, 0x39, 0x3b, 0x7, 
    0x13, 0x2, 0x2, 0x3a, 0x1c, 0x3, 0x2, 0x2, 0x2, 0x3a, 0x25, 0x3, 0x2, 
    0x2, 0x2, 0x3a, 0x2b, 0x3, 0x2, 0x2, 0x2, 0x3a, 0x37, 0x3, 0x2, 0x2, 
    0x2, 0x3a, 0x38, 0x3, 0x2, 0x2, 0x2, 0x3a, 0x39, 0x3, 0x2, 0x2, 0x2, 
    0x3b, 0x7, 0x3, 0x2, 0x2, 0x2, 0x3c, 0x3d, 0x9, 0x2, 0x2, 0x2, 0x3d, 
    0x9, 0x3, 0x2, 0x2, 0x2, 0x3e, 0x3f, 0x7, 0x3, 0x2, 0x2, 0x3f, 0x40, 
    0x7, 0x1f, 0x2, 0x2, 0x40, 0x41, 0x5, 0x6, 0x4, 0x2, 0x41, 0x42, 0x5, 
    0xa, 0x6, 0x2, 0x42, 0x43, 0x5, 0xa, 0x6, 0x2, 0x43, 0x44, 0x7, 0x4, 
    0x2, 0x2, 0x44, 0x50, 0x3, 0x2, 0x2, 0x2, 0x45, 0x46, 0x7, 0x3, 0x2, 
    0x2, 0x46, 0x48, 0x5, 0xe, 0x8, 0x2, 0x47, 0x49, 0x5, 0xa, 0x6, 0x2, 
    0x48, 0x47, 0x3, 0x2, 0x2, 0x2, 0x49, 0x4a, 0x3, 0x2, 0x2, 0x2, 0x4a, 
    0x48, 0x3, 0x2, 0x2, 0x2, 0x4a, 0x4b, 0x3, 0x2, 0x2, 0x2, 0x4b, 0x4c, 
    0x3, 0x2, 0x2, 0x2, 0x4c, 0x4d, 0x7, 0x4, 0x2, 0x2, 0x4d, 0x50, 0x3, 
    0x2, 0x2, 0x2, 0x4e, 0x50, 0x5, 0x14, 0xb, 0x2, 0x4f, 0x3e, 0x3, 0x2, 
    0x2, 0x2, 0x4f, 0x45, 0x3, 0x2, 0x2, 0x2, 0x4f, 0x4e, 0x3, 0x2, 0x2, 
    0x2, 0x50, 0xb, 0x3, 0x2, 0x2, 0x2, 0x51, 0x52, 0x9, 0x3, 0x2, 0x2, 
    0x52, 0xd, 0x3, 0x2, 0x2, 0x2, 0x53, 0x54, 0x9, 0x4, 0x2, 0x2, 0x54, 
    0xf, 0x3, 0x2, 0x2, 0x2, 0x55, 0x56, 0x7, 0x3, 0x2, 0x2, 0x56, 0x57, 
    0x7, 0x24, 0x2, 0x2, 0x57, 0x58, 0x5, 0x6, 0x4, 0x2, 0x58, 0x59, 0x7, 
    0x4, 0x2, 0x2, 0x59, 0x11, 0x3, 0x2, 0x2, 0x2, 0x5a, 0x5b, 0x7, 0x24, 
    0x2, 0x2, 0x5b, 0x13, 0x3, 0x2, 0x2, 0x2, 0x5c, 0x64, 0x7, 0x24, 0x2, 
    0x2, 0x5d, 0x5e, 0x7, 0x11, 0x2, 0x2, 0x5e, 0x5f, 0x7, 0x12, 0x2, 0x2, 
    0x5f, 0x60, 0x7, 0x24, 0x2, 0x2, 0x60, 0x61, 0x7, 0x12, 0x2, 0x2, 0x61, 
    0x64, 0x7, 0x11, 0x2, 0x2, 0x62, 0x64, 0x7, 0x20, 0x2, 0x2, 0x63, 0x5c, 
    0x3, 0x2, 0x2, 0x2, 0x63, 0x5d, 0x3, 0x2, 0x2, 0x2, 0x63, 0x62, 0x3, 
    0x2, 0x2, 0x2, 0x64, 0x15, 0x3, 0x2, 0x2, 0x2, 0x8, 0x21, 0x31, 0x3a, 
    0x4a, 0x4f, 0x63, 
  };

  atn::ATNDeserializer deserializer;
  _atn = deserializer.deserialize(_serializedATN);

  size_t count = _atn.getNumberOfDecisions();
  _decisionToDFA.reserve(count);
  for (size_t i = 0; i < count; i++) { 
    _decisionToDFA.emplace_back(_atn.getDecisionState(i), i);
  }
}

SimpleSMTLIBParser::Initializer SimpleSMTLIBParser::_init;
