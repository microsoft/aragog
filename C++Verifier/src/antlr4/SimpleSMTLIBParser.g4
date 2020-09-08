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

parser grammar SimpleSMTLIBParser;

options {
	tokenVocab = SimpleSMTLIBLexer;
}

startBool
    : boolExpression EOF
    ;

startArith
    : arithExpression EOF
    ;

boolExpression
	: ParOpen boolOp boolExpression+ ParClose
	| ParOpen compOp arithExpression arithExpression ParClose
	| ParOpen GRW_Let ParOpen varBinding+ ParClose boolExpression ParClose
	| boundVar
	| PS_True
	| PS_False
	;
	
boolOp
	: PS_And
	| PS_Or
	| PS_Not
	;

arithExpression
	: ParOpen GRW_Ite boolExpression arithExpression arithExpression ParClose
	| ParOpen arithOp arithExpression+ ParClose
	| terminal
	;

compOp
	: PS_Eq
	| PS_Lt
	| PS_Le
	| PS_Gt
	| PS_Ge
	;

arithOp
	: PS_Add
	| PS_Sub
	| PS_Mul
	| PS_Div
	;

varBinding
    : ParOpen UndefinedSymbol boolExpression ParClose
    ;

boundVar
	: UndefinedSymbol
	;

terminal
	: UndefinedSymbol
	| PIPE DOUBLE_QUOTE UndefinedSymbol DOUBLE_QUOTE PIPE
	| Numeral
	;
