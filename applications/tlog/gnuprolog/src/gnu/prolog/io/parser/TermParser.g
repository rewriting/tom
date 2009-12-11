/* GNU Prolog for Java
 * Copyright (C) 1997-2000  Constantine Plotnikov
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA. The text ol license can be also found 
 * at http://www.gnu.org/copyleft/lgpl.html
 */
// this file is in progress conversion of JavaCC grammar to antlr

class TermLexer extends Lexer;

NAME_TOKEN
  : IDENTIFIER_TOKEN
  | GRAPHIC_TOKEN
  | QUOTED_TOKEN
  | SEMICOLON_TOKEN>
  | CUT_TOKEN;


private IDENTIFIER_TOKEN
  : SMALL_LETTER_CHAR (ALPHA_NUMERIC_CHAR)*;
 
private GRAPHIC_TOKEN
  : "." (GRAPHIC_TOKEN_CHAR)+ 
  | GRAPHIC_TOKEN_CHAR (<RAPHIC_TOKEN_CHAR|".")*;

private GRAPHIC_TOKEN_CHAR
  : GRAPHIC_CHAR | BACKSLASH_CHAR;

private QUOTED_TOKEN  /*6.5.5*/
  : SINGLE_QUOTE_CHAR  (SINGLE_QUOTED_ITEM)* SINGLE_QUOTE_CHAR;

private SINGLE_QUOTED_ITEM /*6.4.2.1*/
  : SINGLE_QUOTED_CHAR 
  | CONTINUATION_ESCAPE_SEQUENCE;


private CONTINUATION_ESCAPE_SEQUENCE
  : BACKSLASH_CHAR /*6.5.5*/ NEW_LINE_CHAR /*6.5.4*/;

private SEMICOLON_TOKEN: SEMICOLON_CHAR /*6.5.3*/ ;

private CUT_TOKEN: CUT_CHAR;

// 6.4.2.1 QUOTED CHARACTERS
private SINGLE_QUOTED_CHAR
  : NON_QUOTE_CHAR
  | SINGLE_QUOTE_CHAR/*6.5.5*/ SINGLE_QUOTE_CHAR /*6.5.5*/
  | DOUBLE_QUOTE_CHAR/*6.5.5*/
  | BACK_QUOTE_CHAR/*6.5.5*/;

private DOUBLE_QUOTED_CHAR
  : NON_QUOTE_CHAR
  | SINGLE_QUOTE_CHAR/*6.5.5*/
  | DOUBLE_QUOTE_CHAR/*6.5.5*/ DOUBLE_QUOTE_CHAR/*6.5.5*/
  | BACK_QUOTE_CHAR/*6.5.5*/;

private BACK_QUOTED_CHAR
  : NON_QUOTE_CHAR
  | SINGLE_QUOTE_CHAR/*6.5.5*/
  | DOUBLE_QUOTE_CHAR/*6.5.5*/
  | BACK_QUOTE_CHAR/*6.5.5*/ BACK_QUOTE_CHAR/*6.5.5*/;

private NON_QUOTE_CHAR
  : GRAPHIC_CHAR
  | ALPHA_NUMERIC_CHAR
  | SOLO_CHAR
  | SPACE_CHAR
  | META_ESCAPE_SEQUENCE
  | CONTROL_ESCAPE_SEQUENCE
  | OCTAL_ESCAPE_SEQUENCE
  | HEXADECIMAL_ESCAPE_SEQUENCE
  | ".";


private META_ESCAPE_SEQUENCE
  : BACKSLASH_CHAR /* 6.5.5 */ META_CHAR /* 6.5.5 */ ;

private CONTROL_ESCAPE_SEQUENCE
  : BACKSLASH_CHAR /* 6.5.5 */ SYMOLIC_CONTROL_CHAR /* 6.5.5 */;


private SYMOLIC_CONTROL_CHAR
  : SYMOLIC_ALERT_CHAR
  | SYMOLIC_BACKSPACE_CHAR
  | SYMOLIC_FORM_FEED_CHAR
  | SYMOLIC_NEW_LINE_CHAR
  | SYMOLIC_HORIZONTAL_TAB_CHAR
  | SYMOLIC_VERTICAL_TAB_CHAR
  | SYMOLIC_CARRIAGE_RETURN_CHAR;


private SYMOLIC_ALERT_CHAR:          "a";
private SYMOLIC_BACKSPACE_CHAR:      "b";
private SYMOLIC_FORM_FEED_CHAR:      "f";
private SYMOLIC_NEW_LINE_CHAR:       "n";
private SYMOLIC_HORIZONTAL_TAB_CHAR: "t";
private SYMOLIC_VERTICAL_TAB_CHAR:   "v";
private SYMOLIC_CARRIAGE_RETURN_CHAR:"r";
private SYMOLIC_HEXADECIMAL_CHAR:    "x";

private OCTAL_ESCAPE_SEQUENCE
  : BACKSLASH_CHAR /* 6.5.5 */ 
    (OCTAL_DIGIT_CHAR)+ /* 6.5.2 */
    BACKSLASH_CHAR /* 6.5.5 */;

private HEXADECIMAL_ESCAPE_SEQUENCE
  : BACKSLASH_CHAR /* 6.5.5 */
    SYMOLIC_HEXADECIMAL_CHAR
    (HEXADECIMAL_DIGIT_CHAR)+ /* 6.5.2 */
    BACKSLASH_CHAR /* 6.5.5 */;

// 6.4.3 Variables
private VARIABLE_TOKEN
  : ANONYMOUS_VARIABLE
  | NAMED_VARIABLE;

private ANONYMOUS_VARIABLE: VARIABLE_INDICATOR_CHAR;

private NAMED_VARIABLE
  : VARIABLE_INDICATOR_CHAR (ALPHA_NUMERIC_CHAR)+ /* 6.5.2*/
  | CAPITAL_LETTER_CHAR /* 6.5.2*/ (ALPHA_NUMERIC_CHAR)*/* 6.5.2*/;

private VARIABLE_INDICATOR_CHAR: UNDERSCORE_CHAR/* 6.5.2*/;

// 6.4.4 INTEGER NUMBERS + 6.3.2.1 Negative numbers
INTEGER_TOKEN
  : ("-")? 
    ( INTEGER_CONSTANT 
    | CHARACTER_CODE_CONSTANT
    | BINARY_CONSTANT
    | OCTAL_CONSTANT
    | HEXADECIMAL_CONSTANT);


private INTEGER_CONSTANT: (DECIMAL_DIGIT_CHAR)+ /* 6.5.2 */ ;
private CHARACTER_CODE_CONSTANT
  : "0" SINGLE_QUOTE_CHAR /* 6.5.2 */ SINGLE_QUOTED_CHAR /*6.4.2.1*/;
private BINARY_CONSTANT
  : "0b" (BINARY_DIGIT_CHAR)+ /* 6.5.2 */;
private OCTAL_CONSTANT
  : "0o" (OCTAL_DIGIT_CHAR)+ /* 6.5.2 */;
private HEXADECIMAL_CONSTANT
  : "0x" (HEXADECIMAL_DIGIT_CHAR)+ /* 6.5.2 */;
// 6.4.5 FLOATING POINT NUMBERS
FLOAT_NUMBER_TOKEN
  : (["-"])? INTEGER_CONSTANT /* 6.4.4 */FRACTION (EXPONENT)? ;

private FRACTION
  : "." (DECIMAL_DIGIT_CHAR)+ /* 6.5.2 */ ;

private EXPONENT
  : ("E"|"e") ("+"|"-")? INTEGER_CONSTANT /*6.4.4*/;

// 6.4.6 CHARACTER CODE LIST
CHAR_CODE_LIST_TOKEN
  : DOUBLE_QUOTE_CHAR> /* 6.5.5 */
    (DOUBLE_QUOTED_ITEM)*
    DOUBLE_QUOTE_CHAR /* 6.5.5 */;

private DOUBLE_QUOTED_ITEM
  : DOUBLE_QUOTED_CHAR /*6.4.2.1*/
  | CONTINUATION_ESCAPE_SEQUENCE /*6.4.2*/;

// 6.4.7 BACK QUOTED STRING
private BACK_QUOTED_STRING
  : BACK_QUOTE_CHAR  /* 6.5.5 */
    BACK_QUOTED_ITEM
    BACK_QUOTE_CHAR /* 6.5.5 */;

private BACK_QUOTED_ITEM
  : BACK_QUOTED_CHAR  /*6.4.2.1*/
  | CONTINUATION_ESCAPE_SEQUENCE /*6.4.2*/ ;

// 6.4.8 OTHER TOKENS
OPEN_TOKEN: OPEN_CHAR/*6.5.3*/ ;
CLOSE_TOKEN: CLOSE_CHAR> /*6.5.3*/;
OPEN_LIST_TOKEN: OPEN_LIST_CHAR /*6.5.3*/;
CLOSE_LIST_TOKEN: CLOSE_LIST_CHAR /*6.5.3*/;
OPEN_CURLY_TOKEN: OPEN_CURLY_CHAR /*6.5.3*/;
CLOSE_CURLY_TOKEN: CLOSE_CURLY_CHAR /*6.5.3*/;
HEAD_TAIL_SEPARATOR_TOKEN: HEAD_TAIL_SEPARATOR_CHAR /*6.5.3*/;
COMMA_TOKEN: COMMA_CHAR /*6.5.3*/;
END_TOKEN: END_CHAR;

private END_CHAR: ".";
// 6.5 PROCESSOR CHARACTER SET
private CHARARCTER
  : GRAPHIC_CHAR       /* 6.5.1 */
  | ALPHA_NUMERIC_CHAR /* 6.5.2 */
  | SOLO_CHAR          /* 6.5.3 */
  | LAYOUT_CHAR        /* 6.5.4 */
  | META_CHAR         /* 6.5.5 */;
// 6.5.1 GRAPHIC CHARACTERS
private GRAPHIC_CHAR
  : ("#"|"$"|"&"|"*"|"+"|"-"|"/"
    |":"|"<"|"="|">"|"?"|"@"|"^"|"~");

private GRAPHIC_CHAR_PERIOD: "." | GRAPHIC_CHAR;
// 6.5.2 ALPHA NUMRIC CHARACTERS
private ALPHA_NUMERIC_CHAR: ALPHA_CHAR | DECIMAL_DIGIT_CHAR;
private ALPHA_CHAR
  : UNDERSCORE_CHAR
  | LETTER_CHAR;
private LETTER_CHAR
  : CAPITAL_LETTER_CHAR
  | SMALL_LETTER_CHAR;
private CAPITAL_LETTER_CHAR:    ["A"-"Z"];
private SMALL_LETTER_CHAR:      ["a"-"z"];
private DECIMAL_DIGIT_CHAR:     ["0"-"9"];
private BINARY_DIGIT_CHAR:      ["0","1"];
private OCTAL_DIGIT_CHAR:       ["0"-"7"];
private HEXADECIMAL_DIGIT_CHAR: ["0"-"9","a"-"f","A"-"F"];
private UNDERSCORE_CHAR: "_";
// 6.5.3 Solo characters
private SOLO_CHAR
  : CUT_CHAR
  | OPEN_CHAR
  | CLOSE_CHAR
  | COMMA_CHAR
  | SEMICOLON_CHAR
  | OPEN_LIST_CHAR
  | CLOSE_LIST_CHAR
  | OPEN_CURLY_CHAR
  | CLOSE_CURLY_CHAR
  | HEAD_TAIL_SEPARATOR_CHAR
  | END_LINE_COMMENT_CHAR;
private CUT_CHAR:                "!";
private OPEN_CHAR:               "(";
private CLOSE_CHAR:              ")";
private COMMA_CHAR:              ",";
private SEMICOLON_CHAR:          ";";
private OPEN_LIST_CHAR:          "[";
private CLOSE_LIST_CHAR:         "]";
private OPEN_CURLY_CHAR:         "{";
private CLOSE_CURLY_CHAR:        "}";
private HEAD_TAIL_SEPARATOR_CHAR:"|";
private END_LINE_COMMENT_CHAR:   "%";
// 6.5.4 LAYOUT CHARACTERS
private LAYOUT_CHAR : SPACE_CHAR | NEW_LINE_CHAR;
private SPACE_CHAR :" " | "\t";
private NEW_LINE_CHAR:  "\r\n" | "\n\r" | "\n" | "\r";
// 6.5.5 Meta characters
private META_CHAR
  : BACKSLASH_CHAR
  | SINGLE_QUOTE_CHAR
  | DOUBLE_QUOTE_CHAR
  | BACK_QUOTE_CHAR;
private BACKSLASH_CHAR:"\\";
private SINGLE_QUOTE_CHAR:"\'";
private DOUBLE_QUOTE_CHAR:"\"";
private BACK_QUOTE_CHAR:"`";

