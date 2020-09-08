parser grammar InvariantParser;

options {
	tokenVocab = InvariantLexer;
}


policy
	:	(transformation)+ TILDA events_sequence EOF
	;

transformation
	: GROUPBY LPAREN fields RPAREN
	| FILTER LPAREN filter_matches RPAREN
	| MAP LPAREN field_expression COMMA name RPAREN
	;

fields
	: name (COMMA name)*
	;

filter_matches
	: LPAREN filter_matches RPAREN
	| filter_matches OR filter_matches
	| filter_matches AND filter_matches
	| filter_match
	;

filter_match
	: name compare_op name
	| name compare_op value
	| value compare_op name
	;

field_expression
	: (PLUS | MINUS)? field_expression2 ((PLUS | MINUS) field_expression2)*
    | field_expression compare_op field_expression QUESTION field_expression COLON field_expression 
	;

field_expression2
	: field_term ((STAR | DIVIDE) field_term)*
	;

field_term
	: name
	| value
	;

events_sequence
	: (events)+
	;

events
	: events_term (regex_op)?
	;

events_term
	: event
	| LPAREN events_sequence RPAREN
	| SHUFFLE LPAREN events_list RPAREN
	| CHOICE LPAREN events_list RPAREN
	;

events_list
	: events_sequence (COMMA events_sequence)*
	;

event
	: BANG event
	| DOT AT location_match
	| LPAREN event_match RPAREN AT location_match
;

location_match
	: ANY
	| (NOT)? name
	;

event_match
	: field_match (COMMA field_match)*
	;
	
field_match
	: node_match compare_op node_match
	;

value
	: INT_NUMBER
	| HEX_NUMBER
	| BIN_NUMBER
	| DOUBLE_QUOTED_TEXT
	;

equality_op
	: EQUALS
	| NOT_EQUAL
	;

compare_op
	: EQUALS
	| NOT_EQUAL
	| LT
	| GT
	| LE
	| GE
	;

node_match
	: midnode_match ((PLUS | MINUS) midnode_match)*
	;

midnode_match
	: leaf_match ((STAR | DIVIDE) leaf_match)*
	;

leaf_match
	: name
	| value
	| var_ref
	;

regex_op
	: STAR
	| PLUS
	| QUESTION
	;

var_ref
	: VARIABLE
	| TIME
	;

name
 	: IDENTIFIER
 	;