lexer grammar InvariantLexer;


FILTER : 'FILTER' ;
GROUPBY : 'GROUPBY' ;
MAP : 'MAP' ;

TILDA : '~' ;

SHUFFLE : 'SHUFFLE' ;
CHOICE : 'CHOICE' ;
NOT : 'NOT' ;
ANY : 'ANY' ;

fragment DIGIT:    [0-9];
fragment DIGITS:   DIGIT+;
fragment HEXDIGIT: [0-9a-fA-F];

// Only lower case 'x' and 'b' count for hex + bin numbers. Otherwise it's an identifier.
HEX_NUMBER: ('0x' HEXDIGIT+) | ('x\'' HEXDIGIT+ '\'');
BIN_NUMBER: ('0b' [01]+) | ('b\'' [01]+ '\'');

// -------------------------
// Punctuation

COLON			: ':' ;
COMMA			: ',' ;
SEMI			: ';' ;
LPAREN			: '(' ;
RPAREN			: ')' ;
LBRACE			: '[' ;
RBRACE			: ']' ;
DOT				: '.' ;
DOUBLE_QUOTE	: '"' ;

VARIABLE_PRE    : '$' ;
TIME            : 'TIME';

QUESTION	: '?' ;
STAR		: '*' ;
DIVIDE		: '/' ;
PLUS		: '+' ;
MINUS		: '-' ;
BANG		: '!' ;

AT			: '@' ;

OR			: '||' ;
AND			: '&&' ;
EQUALS		: '==' ;
NOT_EQUAL	: '!=' ;

LT			: '<' ;
GT			: '>' ;
LE			: '<=' ;
GE			: '>=' ;

INT_NUMBER: MINUS? DIGITS;

DOUBLE_QUOTED_TEXT: (
        DOUBLE_QUOTE (('\\' .)? .)*? DOUBLE_QUOTE
    )+
;

// -------------------------
// Identifiers

fragment LETTER_WHEN_UNQUOTED: DIGIT | LETTER_WHEN_UNQUOTED_NO_DIGIT | DOT;
fragment LETTER_WHEN_UNQUOTED_NO_DIGIT: [a-zA-Z_$\u0080-\uffff];

VARIABLE:
    VARIABLE_PRE IDENTIFIER
;

IDENTIFIER:
    LETTER_WHEN_UNQUOTED+
;

WHITESPACE: [ \t\f\r\n] -> channel(HIDDEN); // Ignore whitespaces.
