
// Generated from SimpleSMTLIBLexer.g4 by ANTLR 4.8

#pragma once


#include "antlr4-runtime.h"




class  SimpleSMTLIBLexer : public antlr4::Lexer {
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

  SimpleSMTLIBLexer(antlr4::CharStream *input);
  ~SimpleSMTLIBLexer();

  virtual std::string getGrammarFileName() const override;
  virtual const std::vector<std::string>& getRuleNames() const override;

  virtual const std::vector<std::string>& getChannelNames() const override;
  virtual const std::vector<std::string>& getModeNames() const override;
  virtual const std::vector<std::string>& getTokenNames() const override; // deprecated, use vocabulary instead
  virtual antlr4::dfa::Vocabulary& getVocabulary() const override;

  virtual const std::vector<uint16_t> getSerializedATN() const override;
  virtual const antlr4::atn::ATN& getATN() const override;

private:
  static std::vector<antlr4::dfa::DFA> _decisionToDFA;
  static antlr4::atn::PredictionContextCache _sharedContextCache;
  static std::vector<std::string> _ruleNames;
  static std::vector<std::string> _tokenNames;
  static std::vector<std::string> _channelNames;
  static std::vector<std::string> _modeNames;

  static std::vector<std::string> _literalNames;
  static std::vector<std::string> _symbolicNames;
  static antlr4::dfa::Vocabulary _vocabulary;
  static antlr4::atn::ATN _atn;
  static std::vector<uint16_t> _serializedATN;


  // Individual action functions triggered by action() above.

  // Individual semantic predicate functions triggered by sempred() above.

  struct Initializer {
    Initializer();
  };
  static Initializer _init;
};

