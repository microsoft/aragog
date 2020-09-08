
// Generated from SMTLIBv2.g4 by ANTLR 4.8

#pragma once


#include "antlr4-runtime.h"
#include "SMTLIBv2Parser.h"


/**
 * This interface defines an abstract listener for a parse tree produced by SMTLIBv2Parser.
 */
class  SMTLIBv2Listener : public antlr4::tree::ParseTreeListener {
public:

  virtual void enterStart(SMTLIBv2Parser::StartContext *ctx) = 0;
  virtual void exitStart(SMTLIBv2Parser::StartContext *ctx) = 0;

  virtual void enterResponse(SMTLIBv2Parser::ResponseContext *ctx) = 0;
  virtual void exitResponse(SMTLIBv2Parser::ResponseContext *ctx) = 0;

  virtual void enterGeneralReservedWord(SMTLIBv2Parser::GeneralReservedWordContext *ctx) = 0;
  virtual void exitGeneralReservedWord(SMTLIBv2Parser::GeneralReservedWordContext *ctx) = 0;

  virtual void enterSimpleSymbol(SMTLIBv2Parser::SimpleSymbolContext *ctx) = 0;
  virtual void exitSimpleSymbol(SMTLIBv2Parser::SimpleSymbolContext *ctx) = 0;

  virtual void enterQuotedSymbol(SMTLIBv2Parser::QuotedSymbolContext *ctx) = 0;
  virtual void exitQuotedSymbol(SMTLIBv2Parser::QuotedSymbolContext *ctx) = 0;

  virtual void enterPredefSymbol(SMTLIBv2Parser::PredefSymbolContext *ctx) = 0;
  virtual void exitPredefSymbol(SMTLIBv2Parser::PredefSymbolContext *ctx) = 0;

  virtual void enterPredefKeyword(SMTLIBv2Parser::PredefKeywordContext *ctx) = 0;
  virtual void exitPredefKeyword(SMTLIBv2Parser::PredefKeywordContext *ctx) = 0;

  virtual void enterSymbol(SMTLIBv2Parser::SymbolContext *ctx) = 0;
  virtual void exitSymbol(SMTLIBv2Parser::SymbolContext *ctx) = 0;

  virtual void enterNumeral(SMTLIBv2Parser::NumeralContext *ctx) = 0;
  virtual void exitNumeral(SMTLIBv2Parser::NumeralContext *ctx) = 0;

  virtual void enterDecimal(SMTLIBv2Parser::DecimalContext *ctx) = 0;
  virtual void exitDecimal(SMTLIBv2Parser::DecimalContext *ctx) = 0;

  virtual void enterHexadecimal(SMTLIBv2Parser::HexadecimalContext *ctx) = 0;
  virtual void exitHexadecimal(SMTLIBv2Parser::HexadecimalContext *ctx) = 0;

  virtual void enterBinary(SMTLIBv2Parser::BinaryContext *ctx) = 0;
  virtual void exitBinary(SMTLIBv2Parser::BinaryContext *ctx) = 0;

  virtual void enterString(SMTLIBv2Parser::StringContext *ctx) = 0;
  virtual void exitString(SMTLIBv2Parser::StringContext *ctx) = 0;

  virtual void enterKeyword(SMTLIBv2Parser::KeywordContext *ctx) = 0;
  virtual void exitKeyword(SMTLIBv2Parser::KeywordContext *ctx) = 0;

  virtual void enterSpec_constant(SMTLIBv2Parser::Spec_constantContext *ctx) = 0;
  virtual void exitSpec_constant(SMTLIBv2Parser::Spec_constantContext *ctx) = 0;

  virtual void enterS_expr(SMTLIBv2Parser::S_exprContext *ctx) = 0;
  virtual void exitS_expr(SMTLIBv2Parser::S_exprContext *ctx) = 0;

  virtual void enterIndex(SMTLIBv2Parser::IndexContext *ctx) = 0;
  virtual void exitIndex(SMTLIBv2Parser::IndexContext *ctx) = 0;

  virtual void enterIdentifier(SMTLIBv2Parser::IdentifierContext *ctx) = 0;
  virtual void exitIdentifier(SMTLIBv2Parser::IdentifierContext *ctx) = 0;

  virtual void enterAttribute_value(SMTLIBv2Parser::Attribute_valueContext *ctx) = 0;
  virtual void exitAttribute_value(SMTLIBv2Parser::Attribute_valueContext *ctx) = 0;

  virtual void enterAttribute(SMTLIBv2Parser::AttributeContext *ctx) = 0;
  virtual void exitAttribute(SMTLIBv2Parser::AttributeContext *ctx) = 0;

  virtual void enterSort(SMTLIBv2Parser::SortContext *ctx) = 0;
  virtual void exitSort(SMTLIBv2Parser::SortContext *ctx) = 0;

  virtual void enterQual_identifer(SMTLIBv2Parser::Qual_identiferContext *ctx) = 0;
  virtual void exitQual_identifer(SMTLIBv2Parser::Qual_identiferContext *ctx) = 0;

  virtual void enterVar_binding(SMTLIBv2Parser::Var_bindingContext *ctx) = 0;
  virtual void exitVar_binding(SMTLIBv2Parser::Var_bindingContext *ctx) = 0;

  virtual void enterSorted_var(SMTLIBv2Parser::Sorted_varContext *ctx) = 0;
  virtual void exitSorted_var(SMTLIBv2Parser::Sorted_varContext *ctx) = 0;

  virtual void enterPattern(SMTLIBv2Parser::PatternContext *ctx) = 0;
  virtual void exitPattern(SMTLIBv2Parser::PatternContext *ctx) = 0;

  virtual void enterMatch_case(SMTLIBv2Parser::Match_caseContext *ctx) = 0;
  virtual void exitMatch_case(SMTLIBv2Parser::Match_caseContext *ctx) = 0;

  virtual void enterTerm(SMTLIBv2Parser::TermContext *ctx) = 0;
  virtual void exitTerm(SMTLIBv2Parser::TermContext *ctx) = 0;

  virtual void enterSort_symbol_decl(SMTLIBv2Parser::Sort_symbol_declContext *ctx) = 0;
  virtual void exitSort_symbol_decl(SMTLIBv2Parser::Sort_symbol_declContext *ctx) = 0;

  virtual void enterMeta_spec_constant(SMTLIBv2Parser::Meta_spec_constantContext *ctx) = 0;
  virtual void exitMeta_spec_constant(SMTLIBv2Parser::Meta_spec_constantContext *ctx) = 0;

  virtual void enterFun_symbol_decl(SMTLIBv2Parser::Fun_symbol_declContext *ctx) = 0;
  virtual void exitFun_symbol_decl(SMTLIBv2Parser::Fun_symbol_declContext *ctx) = 0;

  virtual void enterPar_fun_symbol_decl(SMTLIBv2Parser::Par_fun_symbol_declContext *ctx) = 0;
  virtual void exitPar_fun_symbol_decl(SMTLIBv2Parser::Par_fun_symbol_declContext *ctx) = 0;

  virtual void enterTheory_attribute(SMTLIBv2Parser::Theory_attributeContext *ctx) = 0;
  virtual void exitTheory_attribute(SMTLIBv2Parser::Theory_attributeContext *ctx) = 0;

  virtual void enterTheory_decl(SMTLIBv2Parser::Theory_declContext *ctx) = 0;
  virtual void exitTheory_decl(SMTLIBv2Parser::Theory_declContext *ctx) = 0;

  virtual void enterLogic_attribue(SMTLIBv2Parser::Logic_attribueContext *ctx) = 0;
  virtual void exitLogic_attribue(SMTLIBv2Parser::Logic_attribueContext *ctx) = 0;

  virtual void enterLogic(SMTLIBv2Parser::LogicContext *ctx) = 0;
  virtual void exitLogic(SMTLIBv2Parser::LogicContext *ctx) = 0;

  virtual void enterSort_dec(SMTLIBv2Parser::Sort_decContext *ctx) = 0;
  virtual void exitSort_dec(SMTLIBv2Parser::Sort_decContext *ctx) = 0;

  virtual void enterSelector_dec(SMTLIBv2Parser::Selector_decContext *ctx) = 0;
  virtual void exitSelector_dec(SMTLIBv2Parser::Selector_decContext *ctx) = 0;

  virtual void enterConstructor_dec(SMTLIBv2Parser::Constructor_decContext *ctx) = 0;
  virtual void exitConstructor_dec(SMTLIBv2Parser::Constructor_decContext *ctx) = 0;

  virtual void enterDatatype_dec(SMTLIBv2Parser::Datatype_decContext *ctx) = 0;
  virtual void exitDatatype_dec(SMTLIBv2Parser::Datatype_decContext *ctx) = 0;

  virtual void enterFunction_dec(SMTLIBv2Parser::Function_decContext *ctx) = 0;
  virtual void exitFunction_dec(SMTLIBv2Parser::Function_decContext *ctx) = 0;

  virtual void enterFunction_def(SMTLIBv2Parser::Function_defContext *ctx) = 0;
  virtual void exitFunction_def(SMTLIBv2Parser::Function_defContext *ctx) = 0;

  virtual void enterProp_literal(SMTLIBv2Parser::Prop_literalContext *ctx) = 0;
  virtual void exitProp_literal(SMTLIBv2Parser::Prop_literalContext *ctx) = 0;

  virtual void enterScript(SMTLIBv2Parser::ScriptContext *ctx) = 0;
  virtual void exitScript(SMTLIBv2Parser::ScriptContext *ctx) = 0;

  virtual void enterCmd_assert(SMTLIBv2Parser::Cmd_assertContext *ctx) = 0;
  virtual void exitCmd_assert(SMTLIBv2Parser::Cmd_assertContext *ctx) = 0;

  virtual void enterCmd_checkSat(SMTLIBv2Parser::Cmd_checkSatContext *ctx) = 0;
  virtual void exitCmd_checkSat(SMTLIBv2Parser::Cmd_checkSatContext *ctx) = 0;

  virtual void enterCmd_checkSatAssuming(SMTLIBv2Parser::Cmd_checkSatAssumingContext *ctx) = 0;
  virtual void exitCmd_checkSatAssuming(SMTLIBv2Parser::Cmd_checkSatAssumingContext *ctx) = 0;

  virtual void enterCmd_declareConst(SMTLIBv2Parser::Cmd_declareConstContext *ctx) = 0;
  virtual void exitCmd_declareConst(SMTLIBv2Parser::Cmd_declareConstContext *ctx) = 0;

  virtual void enterCmd_declareDatatype(SMTLIBv2Parser::Cmd_declareDatatypeContext *ctx) = 0;
  virtual void exitCmd_declareDatatype(SMTLIBv2Parser::Cmd_declareDatatypeContext *ctx) = 0;

  virtual void enterCmd_declareDatatypes(SMTLIBv2Parser::Cmd_declareDatatypesContext *ctx) = 0;
  virtual void exitCmd_declareDatatypes(SMTLIBv2Parser::Cmd_declareDatatypesContext *ctx) = 0;

  virtual void enterCmd_declareFun(SMTLIBv2Parser::Cmd_declareFunContext *ctx) = 0;
  virtual void exitCmd_declareFun(SMTLIBv2Parser::Cmd_declareFunContext *ctx) = 0;

  virtual void enterCmd_declareSort(SMTLIBv2Parser::Cmd_declareSortContext *ctx) = 0;
  virtual void exitCmd_declareSort(SMTLIBv2Parser::Cmd_declareSortContext *ctx) = 0;

  virtual void enterCmd_defineFun(SMTLIBv2Parser::Cmd_defineFunContext *ctx) = 0;
  virtual void exitCmd_defineFun(SMTLIBv2Parser::Cmd_defineFunContext *ctx) = 0;

  virtual void enterCmd_defineFunRec(SMTLIBv2Parser::Cmd_defineFunRecContext *ctx) = 0;
  virtual void exitCmd_defineFunRec(SMTLIBv2Parser::Cmd_defineFunRecContext *ctx) = 0;

  virtual void enterCmd_defineFunsRec(SMTLIBv2Parser::Cmd_defineFunsRecContext *ctx) = 0;
  virtual void exitCmd_defineFunsRec(SMTLIBv2Parser::Cmd_defineFunsRecContext *ctx) = 0;

  virtual void enterCmd_defineSort(SMTLIBv2Parser::Cmd_defineSortContext *ctx) = 0;
  virtual void exitCmd_defineSort(SMTLIBv2Parser::Cmd_defineSortContext *ctx) = 0;

  virtual void enterCmd_echo(SMTLIBv2Parser::Cmd_echoContext *ctx) = 0;
  virtual void exitCmd_echo(SMTLIBv2Parser::Cmd_echoContext *ctx) = 0;

  virtual void enterCmd_exit(SMTLIBv2Parser::Cmd_exitContext *ctx) = 0;
  virtual void exitCmd_exit(SMTLIBv2Parser::Cmd_exitContext *ctx) = 0;

  virtual void enterCmd_getAssertions(SMTLIBv2Parser::Cmd_getAssertionsContext *ctx) = 0;
  virtual void exitCmd_getAssertions(SMTLIBv2Parser::Cmd_getAssertionsContext *ctx) = 0;

  virtual void enterCmd_getAssignment(SMTLIBv2Parser::Cmd_getAssignmentContext *ctx) = 0;
  virtual void exitCmd_getAssignment(SMTLIBv2Parser::Cmd_getAssignmentContext *ctx) = 0;

  virtual void enterCmd_getInfo(SMTLIBv2Parser::Cmd_getInfoContext *ctx) = 0;
  virtual void exitCmd_getInfo(SMTLIBv2Parser::Cmd_getInfoContext *ctx) = 0;

  virtual void enterCmd_getModel(SMTLIBv2Parser::Cmd_getModelContext *ctx) = 0;
  virtual void exitCmd_getModel(SMTLIBv2Parser::Cmd_getModelContext *ctx) = 0;

  virtual void enterCmd_getOption(SMTLIBv2Parser::Cmd_getOptionContext *ctx) = 0;
  virtual void exitCmd_getOption(SMTLIBv2Parser::Cmd_getOptionContext *ctx) = 0;

  virtual void enterCmd_getProof(SMTLIBv2Parser::Cmd_getProofContext *ctx) = 0;
  virtual void exitCmd_getProof(SMTLIBv2Parser::Cmd_getProofContext *ctx) = 0;

  virtual void enterCmd_getUnsatAssumptions(SMTLIBv2Parser::Cmd_getUnsatAssumptionsContext *ctx) = 0;
  virtual void exitCmd_getUnsatAssumptions(SMTLIBv2Parser::Cmd_getUnsatAssumptionsContext *ctx) = 0;

  virtual void enterCmd_getUnsatCore(SMTLIBv2Parser::Cmd_getUnsatCoreContext *ctx) = 0;
  virtual void exitCmd_getUnsatCore(SMTLIBv2Parser::Cmd_getUnsatCoreContext *ctx) = 0;

  virtual void enterCmd_getValue(SMTLIBv2Parser::Cmd_getValueContext *ctx) = 0;
  virtual void exitCmd_getValue(SMTLIBv2Parser::Cmd_getValueContext *ctx) = 0;

  virtual void enterCmd_pop(SMTLIBv2Parser::Cmd_popContext *ctx) = 0;
  virtual void exitCmd_pop(SMTLIBv2Parser::Cmd_popContext *ctx) = 0;

  virtual void enterCmd_push(SMTLIBv2Parser::Cmd_pushContext *ctx) = 0;
  virtual void exitCmd_push(SMTLIBv2Parser::Cmd_pushContext *ctx) = 0;

  virtual void enterCmd_reset(SMTLIBv2Parser::Cmd_resetContext *ctx) = 0;
  virtual void exitCmd_reset(SMTLIBv2Parser::Cmd_resetContext *ctx) = 0;

  virtual void enterCmd_resetAssertions(SMTLIBv2Parser::Cmd_resetAssertionsContext *ctx) = 0;
  virtual void exitCmd_resetAssertions(SMTLIBv2Parser::Cmd_resetAssertionsContext *ctx) = 0;

  virtual void enterCmd_setInfo(SMTLIBv2Parser::Cmd_setInfoContext *ctx) = 0;
  virtual void exitCmd_setInfo(SMTLIBv2Parser::Cmd_setInfoContext *ctx) = 0;

  virtual void enterCmd_setLogic(SMTLIBv2Parser::Cmd_setLogicContext *ctx) = 0;
  virtual void exitCmd_setLogic(SMTLIBv2Parser::Cmd_setLogicContext *ctx) = 0;

  virtual void enterCmd_setOption(SMTLIBv2Parser::Cmd_setOptionContext *ctx) = 0;
  virtual void exitCmd_setOption(SMTLIBv2Parser::Cmd_setOptionContext *ctx) = 0;

  virtual void enterCommand(SMTLIBv2Parser::CommandContext *ctx) = 0;
  virtual void exitCommand(SMTLIBv2Parser::CommandContext *ctx) = 0;

  virtual void enterB_value(SMTLIBv2Parser::B_valueContext *ctx) = 0;
  virtual void exitB_value(SMTLIBv2Parser::B_valueContext *ctx) = 0;

  virtual void enterOption(SMTLIBv2Parser::OptionContext *ctx) = 0;
  virtual void exitOption(SMTLIBv2Parser::OptionContext *ctx) = 0;

  virtual void enterInfo_flag(SMTLIBv2Parser::Info_flagContext *ctx) = 0;
  virtual void exitInfo_flag(SMTLIBv2Parser::Info_flagContext *ctx) = 0;

  virtual void enterError_behaviour(SMTLIBv2Parser::Error_behaviourContext *ctx) = 0;
  virtual void exitError_behaviour(SMTLIBv2Parser::Error_behaviourContext *ctx) = 0;

  virtual void enterReason_unknown(SMTLIBv2Parser::Reason_unknownContext *ctx) = 0;
  virtual void exitReason_unknown(SMTLIBv2Parser::Reason_unknownContext *ctx) = 0;

  virtual void enterModel_response(SMTLIBv2Parser::Model_responseContext *ctx) = 0;
  virtual void exitModel_response(SMTLIBv2Parser::Model_responseContext *ctx) = 0;

  virtual void enterInfo_response(SMTLIBv2Parser::Info_responseContext *ctx) = 0;
  virtual void exitInfo_response(SMTLIBv2Parser::Info_responseContext *ctx) = 0;

  virtual void enterValuation_pair(SMTLIBv2Parser::Valuation_pairContext *ctx) = 0;
  virtual void exitValuation_pair(SMTLIBv2Parser::Valuation_pairContext *ctx) = 0;

  virtual void enterT_valuation_pair(SMTLIBv2Parser::T_valuation_pairContext *ctx) = 0;
  virtual void exitT_valuation_pair(SMTLIBv2Parser::T_valuation_pairContext *ctx) = 0;

  virtual void enterCheck_sat_response(SMTLIBv2Parser::Check_sat_responseContext *ctx) = 0;
  virtual void exitCheck_sat_response(SMTLIBv2Parser::Check_sat_responseContext *ctx) = 0;

  virtual void enterEcho_response(SMTLIBv2Parser::Echo_responseContext *ctx) = 0;
  virtual void exitEcho_response(SMTLIBv2Parser::Echo_responseContext *ctx) = 0;

  virtual void enterGet_assertions_response(SMTLIBv2Parser::Get_assertions_responseContext *ctx) = 0;
  virtual void exitGet_assertions_response(SMTLIBv2Parser::Get_assertions_responseContext *ctx) = 0;

  virtual void enterGet_assignment_response(SMTLIBv2Parser::Get_assignment_responseContext *ctx) = 0;
  virtual void exitGet_assignment_response(SMTLIBv2Parser::Get_assignment_responseContext *ctx) = 0;

  virtual void enterGet_info_response(SMTLIBv2Parser::Get_info_responseContext *ctx) = 0;
  virtual void exitGet_info_response(SMTLIBv2Parser::Get_info_responseContext *ctx) = 0;

  virtual void enterGet_model_response(SMTLIBv2Parser::Get_model_responseContext *ctx) = 0;
  virtual void exitGet_model_response(SMTLIBv2Parser::Get_model_responseContext *ctx) = 0;

  virtual void enterGet_option_response(SMTLIBv2Parser::Get_option_responseContext *ctx) = 0;
  virtual void exitGet_option_response(SMTLIBv2Parser::Get_option_responseContext *ctx) = 0;

  virtual void enterGet_proof_response(SMTLIBv2Parser::Get_proof_responseContext *ctx) = 0;
  virtual void exitGet_proof_response(SMTLIBv2Parser::Get_proof_responseContext *ctx) = 0;

  virtual void enterGet_unsat_assump_response(SMTLIBv2Parser::Get_unsat_assump_responseContext *ctx) = 0;
  virtual void exitGet_unsat_assump_response(SMTLIBv2Parser::Get_unsat_assump_responseContext *ctx) = 0;

  virtual void enterGet_unsat_core_response(SMTLIBv2Parser::Get_unsat_core_responseContext *ctx) = 0;
  virtual void exitGet_unsat_core_response(SMTLIBv2Parser::Get_unsat_core_responseContext *ctx) = 0;

  virtual void enterGet_value_response(SMTLIBv2Parser::Get_value_responseContext *ctx) = 0;
  virtual void exitGet_value_response(SMTLIBv2Parser::Get_value_responseContext *ctx) = 0;

  virtual void enterSpecific_success_response(SMTLIBv2Parser::Specific_success_responseContext *ctx) = 0;
  virtual void exitSpecific_success_response(SMTLIBv2Parser::Specific_success_responseContext *ctx) = 0;

  virtual void enterGeneral_response(SMTLIBv2Parser::General_responseContext *ctx) = 0;
  virtual void exitGeneral_response(SMTLIBv2Parser::General_responseContext *ctx) = 0;


};

