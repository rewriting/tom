/*
 * Gom
 *
 * Copyright (c) 2007-2016, Universite de Lorraine, Inria
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
 * Antoine Reilles    e-mail: Antoine.Reilles@loria.fr
 *
 **/
grammar SimpleBlock;

options {
  output=AST;
}

@header {
  package tom.gom.parser;
}

@lexer::header {
  package tom.gom.parser;
}

@lexer::members {
  public static int nesting = 0;
}

block :
  rawblocklist
  ;

rawblocklist :
  (STRING | LBRACE rawblocklist RBRACE)*
  ;

LBRACE : '{' { nesting++; } ;
RBRACE : '}'
  {
    if ( nesting<=0 ) {
      emit(Token.EOF_TOKEN);
    }
    else {
      nesting--;
    }
  }
  ;

STRING : '"' (ESC | ~('\\'|'"') )* '"'
  ;

fragment
ESC :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
  |   OctalESC
  ;

fragment
OctalESC
  :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
  |   '\\' ('0'..'7') ('0'..'7')
  |   '\\' ('0'..'7')
  ;

//COMMENT : (SL_COMMENT | ML_COMMENT)
//  ;

//fragment
SL_COMMENT :
  '//' (~('\n'|'\r'))* '\r'? '\n' {$channel=HIDDEN;}
  ;

//fragment
ML_COMMENT
  : '/*' ( options {greedy=false;} : . )* '*/' //{$channel=HIDDEN;}
  ;

// the rule for the filter: just append the text to the buffer
TARGET : ( text=~('{'|'}'|'"') )+ ;
