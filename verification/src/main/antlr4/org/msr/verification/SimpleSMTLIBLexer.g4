/**
 * SMT-LIB (v2.6) grammar
 *
 * Grammar is baesd on the following specification:
 * http://smtlib.cs.uiowa.edu/papers/smt-lib-reference-v2.6-r2017-07-18.pdf
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Julian Thome <julian.thome.de@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/

lexer grammar SimpleSMTLIBLexer;



ParOpen     : '(';
ParClose    : ')';

PS_And      : 'and';
PS_Or       : 'or';
PS_Not      : 'not';
PS_Eq       : '=';
PS_Lt       : '<';
PS_Le       : '<=';
PS_Gt       : '>';
PS_Ge       : '>=';
PS_Add       : '+';
PS_Sub       : '-';
PS_Div       : '/';
PS_Mul       : '*';

PIPE        : '|';
DOUBLE_QUOTE    : '"' ;


PS_False
    : 'false'
    ;
PS_True
    : 'true'
    ;

GRW_Binary
    : 'BINARY'
    ;
GRW_Decimal
    : 'DECIMAL'
    ;
GRW_Exists
    : 'exists'
    ;
GRW_Hexadecimal
    : 'HEXADECIMAL'
    ;
GRW_Forall
    : 'forall'
    ;
GRW_Let
    : 'let'
    ;
GRW_Match
    : 'match'
    ;
GRW_Numeral
    : 'NUMERAL'
    ;
GRW_Par
    : 'par'
    ;
GRW_String
    : 'string'
    ;
GRW_Ite
    : 'ite'
    ;

Numeral
    : '0'
    | [1-9] Digit*
    ;

Binary:
    BinaryDigit+
    ;

HexDecimal
    : '#x' HexDigit HexDigit HexDigit HexDigit
    ;

Decimal
    : Numeral '.' '0'* Numeral
    ;

fragment HexDigit
    : [0-9a-fA-F]
    ;

fragment Digit
    : [0-9]
    ;

fragment Sym
    : [a-zA-Z_]
    | '!'
    | '$'
    ;

fragment BinaryDigit
    : [01]
    ;

UndefinedSymbol:
    (Digit | Sym | '.')+;


WHITESPACE: [ \t\f\r\n] -> channel(HIDDEN); // Ignore whitespaces.