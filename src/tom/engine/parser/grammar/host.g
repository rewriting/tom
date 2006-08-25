 /**
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2006, INRIA
 * Nancy, France.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

grammar Host;

options {
  backtrack=true;
  memoize=true;
  // permit to read every characters without defining them
  filter=TARGET;
}

@header { package tom.engine.parser; }
@lexer::members {
    // this buffer contains the target code
    // we append each read character by lexer
    public StringBuffer target = new StringBuffer("");
}

// The grammar starts here

input
  :blockList t=EOF
    ;

blockList
    :
        (
            // either a tom construct or everything else
            matchConstruct
        |   strategyConstruct
        |   ruleConstruct
        |   gomSignature
        //|   backquoteTerm
        |   operator
        |   operatorList
        |   operatorArray
        |   includeConstruct
        |   typeTerm
        |   code
        |   STRING
        |   '{' blockList '}'
        )*
    ;

// the %strategy construct
strategyConstruct
    :
       '%strategy' 
        {
            tomparser.strategyConstruct();
            AST t = tomparser.getAST();
#strategyConstruct = #(#[STRATEGY,"STRATEGY"],t);
        }
    ;

// the %rule construct
ruleConstruct
    :'%rule'
        {
            tomparser.ruleConstruct();
            AST t = tomparser.getAST();
#ruleConstruct = #(#[RULE,"RULE"],t);
        }
    ;

// the %match construct :
matchConstruct
  : '%match'
{
  tomparser.matchConstruct();
  AST t = tomparser.getAST();
#matchConstruct = #(#[MATCH,"MATCH"],t);
}
;

includeConstruct
: '%include'
{
  tomparser.includeConstruct();
  AST t = tomparser.getAST();
#includeConstruct = #(#[INCLUDE,"INCLUDE"],t);
}
;

gomSignature
: '%gom'
{
  blockparser.block();
  String code = blockparser.block();
#gomSignature.setType(GOM);
#gomSignature.setText(code);
}
;

/* argh...to enable this, the backquote parser must be rewritten for Pom
backquoteTerm
    :
        BACKQUOTE_KEYWORD
{
          bqparser.beginBackquote();
          AST t = bqparser.getAST();
#backquoteTerm = #(#[BACKQUOTE,"BACKQUOTE"],t);
        }
    ;
*/
operator
    : '%op'
{
            tomparser.operator();
  AST t = tomparser.getAST();
#operator = #(#[OPERATOR,"OPERATOR"],t);
        }
    ;

operatorList
    : '%oplist'
{
  tomparser.operatorList();
  AST t = tomparser.getAST();
#operatorList = #(#[OPERATOR_LIST,"OPERATOR_LIST"],t);
}
;

operatorArray
    : '%oparray'
        {
          tomparser.operatorArray();
  AST t = tomparser.getAST();
#operatorArray = #(#[OPERATOR_ARRAY,"OPERATOR_ARRAY"],t);
        }
    ;

code
: CODE
;

typeTerm
    : '%typeterm'
    ;

targetLanguage
    : blockList '}'
    ;

// here begins the lexer

STRING
  : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"'
  ;

protected
ESC
  : '\\'
    ( 'n'
    | 'r'
    | 't'
    | 'b'
    | 'f'
    | '"'
    | '\''
    | '\\'
    | ('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    | '0'..'3'
      (
      : '0'..'7'
        (
        : '0'..'7'
        )?
      )?
    | '4'..'7'
      (
      : '0'..'7'
      )?
    )
  ;

protected
HEX_DIGIT
  : ('0'..'9'|'A'..'F'|'a'..'f')
  ;

// tokens to skip : white spaces
WS  : ( ' '
    | '\t'
    | '\f'
    // handle newlines
    | ( '\r\n'  // Evil DOS
      | '\r'    // Macintosh
      | '\n'    // Unix (the right way)
      )
      { newline(); }
    )
        {channel=99;}
    ;

COMMENT
    :   '/*' ( options {greedy=false;} : . )* '*/' {channel=99;}
    ;

LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' {channel=99;}
    ;

CODE
    :
        '%['
        ( { LA(2)!='%' }? ']'
        |
        )
        (
            options {
                greedy=false;
            }
        : '\r' '\n' {newline();}
        | '\r'    {newline();}
        | '\n'    {newline();}
        | ~('\n'|'\r')
        )*
        ']%'
;
// the rule for the filter: just append the text to the buffer
protected
TARGET
    :
        ( . )
        {target.append($getText);}
    ;
