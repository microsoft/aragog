
// Generated from SMTLIBv2.g4 by ANTLR 4.8

#pragma once


#include "antlr4-runtime.h"
#include "SMTLIBv2Listener.h"


/**
 * This class provides an empty implementation of SMTLIBv2Listener,
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
class  SMTLIBv2BaseListener : public SMTLIBv2Listener {
public:

  virtual void enterStart(SMTLIBv2Parser::StartContext * /*ctx*/) override { }
  virtual void exitStart(SMTLIBv2Parser::StartContext * /*ctx*/) override { }

  virtual void enterResponse(SMTLIBv2Parser::ResponseContext * /*ctx*/) override { }
  virtual void exitResponse(SMTLIBv2Parser::ResponseContext * /*ctx*/) override { }

  virtual void enterGeneralReservedWord(SMTLIBv2Parser::GeneralReservedWordContext * /*ctx*/) override { }
  virtual void exitGeneralReservedWord(SMTLIBv2Parser::GeneralReservedWordContext * /*ctx*/) override { }

  virtual void enterSimpleSymbol(SMTLIBv2Parser::SimpleSymbolContext * /*ctx*/) override { }
  virtual void exitSimpleSymbol(SMTLIBv2Parser::SimpleSymbolContext * /*ctx*/) override { }

  virtual void enterQuotedSymbol(SMTLIBv2Parser::QuotedSymbolContext * /*ctx*/) override { }
  virtual void exitQuotedSymbol(SMTLIBv2Parser::QuotedSymbolContext * /*ctx*/) override { }

  virtual void enterPredefSymbol(SMTLIBv2Parser::PredefSymbolContext * /*ctx*/) override { }
  virtual void exitPredefSymbol(SMTLIBv2Parser::PredefSymbolContext * /*ctx*/) override { }

  virtual void enterPredefKeyword(SMTLIBv2Parser::PredefKeywordContext * /*ctx*/) override { }
  virtual void exitPredefKeyword(SMTLIBv2Parser::PredefKeywordContext * /*ctx*/) override { }

  virtual void enterSymbol(SMTLIBv2Parser::SymbolContext * /*ctx*/) override { }
  virtual void exitSymbol(SMTLIBv2Parser::SymbolContext * /*ctx*/) override { }

  virtual void enterNumeral(SMTLIBv2Parser::NumeralContext * /*ctx*/) override { }
  virtual void exitNumeral(SMTLIBv2Parser::NumeralContext * /*ctx*/) override { }

  virtual void enterDecimal(SMTLIBv2Parser::DecimalContext * /*ctx*/) override { }
  virtual void exitDecimal(SMTLIBv2Parser::DecimalContext * /*ctx*/) override { }

  virtual void enterHexadecimal(SMTLIBv2Parser::HexadecimalContext * /*ctx*/) override { }
  virtual void exitHexadecimal(SMTLIBv2Parser::HexadecimalContext * /*ctx*/) override { }

  virtual void enterBinary(SMTLIBv2Parser::BinaryContext * /*ctx*/) override { }
  virtual void exitBinary(SMTLIBv2Parser::BinaryContext * /*ctx*/) override { }

  virtual void enterString(SMTLIBv2Parser::StringContext * /*ctx*/) override { }
  virtual void exitString(SMTLIBv2Parser::StringContext * /*ctx*/) override { }

  virtual void enterKeyword(SMTLIBv2Parser::KeywordContext * /*ctx*/) override { }
  virtual void exitKeyword(SMTLIBv2Parser::KeywordContext * /*ctx*/) override { }

  virtual void enterSpec_constant(SMTLIBv2Parser::Spec_constantContext * /*ctx*/) override { }
  virtual void exitSpec_constant(SMTLIBv2Parser::Spec_constantContext * /*ctx*/) override { }

  virtual void enterS_expr(SMTLIBv2Parser::S_exprContext * /*ctx*/) override { }
  virtual void exitS_expr(SMTLIBv2Parser::S_exprContext * /*ctx*/) override { }

  virtual void enterIndex(SMTLIBv2Parser::IndexContext * /*ctx*/) override { }
  virtual void exitIndex(SMTLIBv2Parser::IndexContext * /*ctx*/) override { }

  virtual void enterIdentifier(SMTLIBv2Parser::IdentifierContext * /*ctx*/) override { }
  virtual void exitIdentifier(SMTLIBv2Parser::IdentifierContext * /*ctx*/) override { }

  virtual void enterAttribute_value(SMTLIBv2Parser::Attribute_valueContext * /*ctx*/) override { }
  virtual void exitAttribute_value(SMTLIBv2Parser::Attribute_valueContext * /*ctx*/) override { }

  virtual void enterAttribute(SMTLIBv2Parser::AttributeContext * /*ctx*/) override { }
  virtual void exitAttribute(SMTLIBv2Parser::AttributeContext * /*ctx*/) override { }

  virtual void enterSort(SMTLIBv2Parser::SortContext * /*ctx*/) override { }
  virtual void exitSort(SMTLIBv2Parser::SortContext * /*ctx*/) override { }

  virtual void enterQual_identifer(SMTLIBv2Parser::Qual_identiferContext * /*ctx*/) override { }
  virtual void exitQual_identifer(SMTLIBv2Parser::Qual_identiferContext * /*ctx*/) override { }

  virtual void enterVar_binding(SMTLIBv2Parser::Var_bindingContext * /*ctx*/) override { }
  virtual void exitVar_binding(SMTLIBv2Parser::Var_bindingContext * /*ctx*/) override { }

  virtual void enterSorted_var(SMTLIBv2Parser::Sorted_varContext * /*ctx*/) override { }
  virtual void exitSorted_var(SMTLIBv2Parser::Sorted_varContext * /*ctx*/) override { }

  virtual void enterPattern(SMTLIBv2Parser::PatternContext * /*ctx*/) override { }
  virtual void exitPattern(SMTLIBv2Parser::PatternContext * /*ctx*/) override { }

  virtual void enterMatch_case(SMTLIBv2Parser::Match_caseContext * /*ctx*/) override { }
  virtual void exitMatch_case(SMTLIBv2Parser::Match_caseContext * /*ctx*/) override { }

  virtual void enterTerm(SMTLIBv2Parser::TermContext * /*ctx*/) override { }
  virtual void exitTerm(SMTLIBv2Parser::TermContext * /*ctx*/) override { }

  virtual void enterSort_symbol_decl(SMTLIBv2Parser::Sort_symbol_declContext * /*ctx*/) override { }
  virtual void exitSort_symbol_decl(SMTLIBv2Parser::Sort_symbol_declContext * /*ctx*/) override { }

  virtual void enterMeta_spec_constant(SMTLIBv2Parser::Meta_spec_constantContext * /*ctx*/) override { }
  virtual void exitMeta_spec_constant(SMTLIBv2Parser::Meta_spec_constantContext * /*ctx*/) override { }

  virtual void enterFun_symbol_decl(SMTLIBv2Parser::Fun_symbol_declContext * /*ctx*/) override { }
  virtual void exitFun_symbol_decl(SMTLIBv2Parser::Fun_symbol_declContext * /*ctx*/) override { }

  virtual void enterPar_fun_symbol_decl(SMTLIBv2Parser::Par_fun_symbol_declContext * /*ctx*/) override { }
  virtual void exitPar_fun_symbol_decl(SMTLIBv2Parser::Par_fun_symbol_declContext * /*ctx*/) override { }

  virtual void enterTheory_attribute(SMTLIBv2Parser::Theory_attributeContext * /*ctx*/) override { }
  virtual void exitTheory_attribute(SMTLIBv2Parser::Theory_attributeContext * /*ctx*/) override { }

  virtual void enterTheory_decl(SMTLIBv2Parser::Theory_declContext * /*ctx*/) override { }
  virtual void exitTheory_decl(SMTLIBv2Parser::Theory_declContext * /*ctx*/) override { }

  virtual void enterLogic_attribue(SMTLIBv2Parser::Logic_attribueContext * /*ctx*/) override { }
  virtual void exitLogic_attribue(SMTLIBv2Parser::Logic_attribueContext * /*ctx*/) override { }

  virtual void enterLogic(SMTLIBv2Parser::LogicContext * /*ctx*/) override { }
  virtual void exitLogic(SMTLIBv2Parser::LogicContext * /*ctx*/) override { }

  virtual void enterSort_dec(SMTLIBv2Parser::Sort_decContext * /*ctx*/) override { }
  virtual void exitSort_dec(SMTLIBv2Parser::Sort_decContext * /*ctx*/) override { }

  virtual void enterSelector_dec(SMTLIBv2Parser::Selector_decContext * /*ctx*/) override { }
  virtual void exitSelector_dec(SMTLIBv2Parser::Selector_decContext * /*ctx*/) override { }

  virtual void enterConstructor_dec(SMTLIBv2Parser::Constructor_decContext * /*ctx*/) override { }
  virtual void exitConstructor_dec(SMTLIBv2Parser::Constructor_decContext * /*ctx*/) override { }

  virtual void enterDatatype_dec(SMTLIBv2Parser::Datatype_decContext * /*ctx*/) override { }
  virtual void exitDatatype_dec(SMTLIBv2Parser::Datatype_decContext * /*ctx*/) override { }

  virtual void enterFunction_dec(SMTLIBv2Parser::Function_decContext * /*ctx*/) override { }
  virtual void exitFunction_dec(SMTLIBv2Parser::Function_decContext * /*ctx*/) override { }

  virtual void enterFunction_def(SMTLIBv2Parser::Function_defContext * /*ctx*/) override { }
  virtual void exitFunction_def(SMTLIBv2Parser::Function_defContext * /*ctx*/) override { }

  virtual void enterProp_literal(SMTLIBv2Parser::Prop_literalContext * /*ctx*/) override { }
  virtual void exitProp_literal(SMTLIBv2Parser::Prop_literalContext * /*ctx*/) override { }

  virtual void enterScript(SMTLIBv2Parser::ScriptContext * /*ctx*/) override { }
  virtual void exitScript(SMTLIBv2Parser::ScriptContext * /*ctx*/) override { }

  virtual void enterCmd_assert(SMTLIBv2Parser::Cmd_assertContext * /*ctx*/) override { }
  virtual void exitCmd_assert(SMTLIBv2Parser::Cmd_assertContext * /*ctx*/) override { }

  virtual void enterCmd_checkSat(SMTLIBv2Parser::Cmd_checkSatContext * /*ctx*/) override { }
  virtual void exitCmd_checkSat(SMTLIBv2Parser::Cmd_checkSatContext * /*ctx*/) override { }

  virtual void enterCmd_checkSatAssuming(SMTLIBv2Parser::Cmd_checkSatAssumingContext * /*ctx*/) override { }
  virtual void exitCmd_checkSatAssuming(SMTLIBv2Parser::Cmd_checkSatAssumingContext * /*ctx*/) override { }

  virtual void enterCmd_declareConst(SMTLIBv2Parser::Cmd_declareConstContext * /*ctx*/) override { }
  virtual void exitCmd_declareConst(SMTLIBv2Parser::Cmd_declareConstContext * /*ctx*/) override { }

  virtual void enterCmd_declareDatatype(SMTLIBv2Parser::Cmd_declareDatatypeContext * /*ctx*/) override { }
  virtual void exitCmd_declareDatatype(SMTLIBv2Parser::Cmd_declareDatatypeContext * /*ctx*/) override { }

  virtual void enterCmd_declareDatatypes(SMTLIBv2Parser::Cmd_declareDatatypesContext * /*ctx*/) override { }
  virtual void exitCmd_declareDatatypes(SMTLIBv2Parser::Cmd_declareDatatypesContext * /*ctx*/) override { }

  virtual void enterCmd_declareFun(SMTLIBv2Parser::Cmd_declareFunContext * /*ctx*/) override { }
  virtual void exitCmd_declareFun(SMTLIBv2Parser::Cmd_declareFunContext * /*ctx*/) override { }

  virtual void enterCmd_declareSort(SMTLIBv2Parser::Cmd_declareSortContext * /*ctx*/) override { }
  virtual void exitCmd_declareSort(SMTLIBv2Parser::Cmd_declareSortContext * /*ctx*/) override { }

  virtual void enterCmd_defineFun(SMTLIBv2Parser::Cmd_defineFunContext * /*ctx*/) override { }
  virtual void exitCmd_defineFun(SMTLIBv2Parser::Cmd_defineFunContext * /*ctx*/) override { }

  virtual void enterCmd_defineFunRec(SMTLIBv2Parser::Cmd_defineFunRecContext * /*ctx*/) override { }
  virtual void exitCmd_defineFunRec(SMTLIBv2Parser::Cmd_defineFunRecContext * /*ctx*/) override { }

  virtual void enterCmd_defineFunsRec(SMTLIBv2Parser::Cmd_defineFunsRecContext * /*ctx*/) override { }
  virtual void exitCmd_defineFunsRec(SMTLIBv2Parser::Cmd_defineFunsRecContext * /*ctx*/) override { }

  virtual void enterCmd_defineSort(SMTLIBv2Parser::Cmd_defineSortContext * /*ctx*/) override { }
  virtual void exitCmd_defineSort(SMTLIBv2Parser::Cmd_defineSortContext * /*ctx*/) override { }

  virtual void enterCmd_echo(SMTLIBv2Parser::Cmd_echoContext * /*ctx*/) override { }
  virtual void exitCmd_echo(SMTLIBv2Parser::Cmd_echoContext * /*ctx*/) override { }

  virtual void enterCmd_exit(SMTLIBv2Parser::Cmd_exitContext * /*ctx*/) override { }
  virtual void exitCmd_exit(SMTLIBv2Parser::Cmd_exitContext * /*ctx*/) override { }

  virtual void enterCmd_getAssertions(SMTLIBv2Parser::Cmd_getAssertionsContext * /*ctx*/) override { }
  virtual void exitCmd_getAssertions(SMTLIBv2Parser::Cmd_getAssertionsContext * /*ctx*/) override { }

  virtual void enterCmd_getAssignment(SMTLIBv2Parser::Cmd_getAssignmentContext * /*ctx*/) override { }
  virtual void exitCmd_getAssignment(SMTLIBv2Parser::Cmd_getAssignmentContext * /*ctx*/) override { }

  virtual void enterCmd_getInfo(SMTLIBv2Parser::Cmd_getInfoContext * /*ctx*/) override { }
  virtual void exitCmd_getInfo(SMTLIBv2Parser::Cmd_getInfoContext * /*ctx*/) override { }

  virtual void enterCmd_getModel(SMTLIBv2Parser::Cmd_getModelContext * /*ctx*/) override { }
  virtual void exitCmd_getModel(SMTLIBv2Parser::Cmd_getModelContext * /*ctx*/) override { }

  virtual void enterCmd_getOption(SMTLIBv2Parser::Cmd_getOptionContext * /*ctx*/) override { }
  virtual void exitCmd_getOption(SMTLIBv2Parser::Cmd_getOptionContext * /*ctx*/) override { }

  virtual void enterCmd_getProof(SMTLIBv2Parser::Cmd_getProofContext * /*ctx*/) override { }
  virtual void exitCmd_getProof(SMTLIBv2Parser::Cmd_getProofContext * /*ctx*/) override { }

  virtual void enterCmd_getUnsatAssumptions(SMTLIBv2Parser::Cmd_getUnsatAssumptionsContext * /*ctx*/) override { }
  virtual void exitCmd_getUnsatAssumptions(SMTLIBv2Parser::Cmd_getUnsatAssumptionsContext * /*ctx*/) override { }

  virtual void enterCmd_getUnsatCore(SMTLIBv2Parser::Cmd_getUnsatCoreContext * /*ctx*/) override { }
  virtual void exitCmd_getUnsatCore(SMTLIBv2Parser::Cmd_getUnsatCoreContext * /*ctx*/) override { }

  virtual void enterCmd_getValue(SMTLIBv2Parser::Cmd_getValueContext * /*ctx*/) override { }
  virtual void exitCmd_getValue(SMTLIBv2Parser::Cmd_getValueContext * /*ctx*/) override { }

  virtual void enterCmd_pop(SMTLIBv2Parser::Cmd_popContext * /*ctx*/) override { }
  virtual void exitCmd_pop(SMTLIBv2Parser::Cmd_popContext * /*ctx*/) override { }

  virtual void enterCmd_push(SMTLIBv2Parser::Cmd_pushContext * /*ctx*/) override { }
  virtual void exitCmd_push(SMTLIBv2Parser::Cmd_pushContext * /*ctx*/) override { }

  virtual void enterCmd_reset(SMTLIBv2Parser::Cmd_resetContext * /*ctx*/) override { }
  virtual void exitCmd_reset(SMTLIBv2Parser::Cmd_resetContext * /*ctx*/) override { }

  virtual void enterCmd_resetAssertions(SMTLIBv2Parser::Cmd_resetAssertionsContext * /*ctx*/) override { }
  virtual void exitCmd_resetAssertions(SMTLIBv2Parser::Cmd_resetAssertionsContext * /*ctx*/) override { }

  virtual void enterCmd_setInfo(SMTLIBv2Parser::Cmd_setInfoContext * /*ctx*/) override { }
  virtual void exitCmd_setInfo(SMTLIBv2Parser::Cmd_setInfoContext * /*ctx*/) override { }

  virtual void enterCmd_setLogic(SMTLIBv2Parser::Cmd_setLogicContext * /*ctx*/) override { }
  virtual void exitCmd_setLogic(SMTLIBv2Parser::Cmd_setLogicContext * /*ctx*/) override { }

  virtual void enterCmd_setOption(SMTLIBv2Parser::Cmd_setOptionContext * /*ctx*/) override { }
  virtual void exitCmd_setOption(SMTLIBv2Parser::Cmd_setOptionContext * /*ctx*/) override { }

  virtual void enterCommand(SMTLIBv2Parser::CommandContext * /*ctx*/) override { }
  virtual void exitCommand(SMTLIBv2Parser::CommandContext * /*ctx*/) override { }

  virtual void enterB_value(SMTLIBv2Parser::B_valueContext * /*ctx*/) override { }
  virtual void exitB_value(SMTLIBv2Parser::B_valueContext * /*ctx*/) override { }

  virtual void enterOption(SMTLIBv2Parser::OptionContext * /*ctx*/) override { }
  virtual void exitOption(SMTLIBv2Parser::OptionContext * /*ctx*/) override { }

  virtual void enterInfo_flag(SMTLIBv2Parser::Info_flagContext * /*ctx*/) override { }
  virtual void exitInfo_flag(SMTLIBv2Parser::Info_flagContext * /*ctx*/) override { }

  virtual void enterError_behaviour(SMTLIBv2Parser::Error_behaviourContext * /*ctx*/) override { }
  virtual void exitError_behaviour(SMTLIBv2Parser::Error_behaviourContext * /*ctx*/) override { }

  virtual void enterReason_unknown(SMTLIBv2Parser::Reason_unknownContext * /*ctx*/) override { }
  virtual void exitReason_unknown(SMTLIBv2Parser::Reason_unknownContext * /*ctx*/) override { }

  virtual void enterModel_response(SMTLIBv2Parser::Model_responseContext * /*ctx*/) override { }
  virtual void exitModel_response(SMTLIBv2Parser::Model_responseContext * /*ctx*/) override { }

  virtual void enterInfo_response(SMTLIBv2Parser::Info_responseContext * /*ctx*/) override { }
  virtual void exitInfo_response(SMTLIBv2Parser::Info_responseContext * /*ctx*/) override { }

  virtual void enterValuation_pair(SMTLIBv2Parser::Valuation_pairContext * /*ctx*/) override { }
  virtual void exitValuation_pair(SMTLIBv2Parser::Valuation_pairContext * /*ctx*/) override { }

  virtual void enterT_valuation_pair(SMTLIBv2Parser::T_valuation_pairContext * /*ctx*/) override { }
  virtual void exitT_valuation_pair(SMTLIBv2Parser::T_valuation_pairContext * /*ctx*/) override { }

  virtual void enterCheck_sat_response(SMTLIBv2Parser::Check_sat_responseContext * /*ctx*/) override { }
  virtual void exitCheck_sat_response(SMTLIBv2Parser::Check_sat_responseContext * /*ctx*/) override { }

  virtual void enterEcho_response(SMTLIBv2Parser::Echo_responseContext * /*ctx*/) override { }
  virtual void exitEcho_response(SMTLIBv2Parser::Echo_responseContext * /*ctx*/) override { }

  virtual void enterGet_assertions_response(SMTLIBv2Parser::Get_assertions_responseContext * /*ctx*/) override { }
  virtual void exitGet_assertions_response(SMTLIBv2Parser::Get_assertions_responseContext * /*ctx*/) override { }

  virtual void enterGet_assignment_response(SMTLIBv2Parser::Get_assignment_responseContext * /*ctx*/) override { }
  virtual void exitGet_assignment_response(SMTLIBv2Parser::Get_assignment_responseContext * /*ctx*/) override { }

  virtual void enterGet_info_response(SMTLIBv2Parser::Get_info_responseContext * /*ctx*/) override { }
  virtual void exitGet_info_response(SMTLIBv2Parser::Get_info_responseContext * /*ctx*/) override { }

  virtual void enterGet_model_response(SMTLIBv2Parser::Get_model_responseContext * /*ctx*/) override { }
  virtual void exitGet_model_response(SMTLIBv2Parser::Get_model_responseContext * /*ctx*/) override { }

  virtual void enterGet_option_response(SMTLIBv2Parser::Get_option_responseContext * /*ctx*/) override { }
  virtual void exitGet_option_response(SMTLIBv2Parser::Get_option_responseContext * /*ctx*/) override { }

  virtual void enterGet_proof_response(SMTLIBv2Parser::Get_proof_responseContext * /*ctx*/) override { }
  virtual void exitGet_proof_response(SMTLIBv2Parser::Get_proof_responseContext * /*ctx*/) override { }

  virtual void enterGet_unsat_assump_response(SMTLIBv2Parser::Get_unsat_assump_responseContext * /*ctx*/) override { }
  virtual void exitGet_unsat_assump_response(SMTLIBv2Parser::Get_unsat_assump_responseContext * /*ctx*/) override { }

  virtual void enterGet_unsat_core_response(SMTLIBv2Parser::Get_unsat_core_responseContext * /*ctx*/) override { }
  virtual void exitGet_unsat_core_response(SMTLIBv2Parser::Get_unsat_core_responseContext * /*ctx*/) override { }

  virtual void enterGet_value_response(SMTLIBv2Parser::Get_value_responseContext * /*ctx*/) override { }
  virtual void exitGet_value_response(SMTLIBv2Parser::Get_value_responseContext * /*ctx*/) override { }

  virtual void enterSpecific_success_response(SMTLIBv2Parser::Specific_success_responseContext * /*ctx*/) override { }
  virtual void exitSpecific_success_response(SMTLIBv2Parser::Specific_success_responseContext * /*ctx*/) override { }

  virtual void enterGeneral_response(SMTLIBv2Parser::General_responseContext * /*ctx*/) override { }
  virtual void exitGeneral_response(SMTLIBv2Parser::General_responseContext * /*ctx*/) override { }


  virtual void enterEveryRule(antlr4::ParserRuleContext * /*ctx*/) override { }
  virtual void exitEveryRule(antlr4::ParserRuleContext * /*ctx*/) override { }
  virtual void visitTerminal(antlr4::tree::TerminalNode * /*node*/) override { }
  virtual void visitErrorNode(antlr4::tree::ErrorNode * /*node*/) override { }

};

