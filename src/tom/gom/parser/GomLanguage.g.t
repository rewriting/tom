/*
 * Gom
 *
 * Copyright (c) 2007, INRIA
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
grammar GomLanguage;

options {
  output=AST;
}

tokens {
  %include { gom/GomTokenList.txt }
}

@header {
  package tom.gom.parser;
}

@lexer::header {
  package tom.gom.parser;
}

module: MODULE^ modulename (imports)? section ;

modulename: (mod=ID DOT)* moduleName=ID ;

imports: IMPORTS^ (importedModuleName=ID)* ;

section : (PUBLIC)? adtgrammar ;

adtgrammar : (sortdef | syntax)+ ;

sortdef: SORTS^ (type)* ;

type: i=ID ;

syntax: ABSTRACT! SYNTAX^ (production | hookConstruct |  typedecl)* ;

production: id=ID fieldlist ARROW^ type ;

typedecl : id=ID EQUALS^ alternatives ;

alternatives : (ALT)? id=ID fieldlist (ALT altid=ID fieldlist)* (SEMI)? ;

fieldlist: LPAREN! (field (COMMA field)* )? RPAREN! ;

arglist: (LPAREN! (arg=ID(COMMA supplarg=ID)* )? RPAREN!)? ;

hookConstruct :
  (hookScope)? pointCut=ID COLON^ hook LBRACE
  /* The LBRACE should contain the code */
  ;

hook: hookType=ID arglist;

hookScope : SORT | MODULE | OPERATOR;

field: type STAR^ | id=ID COLON^ type ;

MODULE   : 'module';
IMPORTS  : 'imports';
PUBLIC   : 'public';
PRIVATE  : 'private';
SORTS    : 'sorts';
ABSTRACT : 'abstract';
SYNTAX   : 'syntax';
SORT     : 'sort';
OPERATOR : 'operator';

ARROW    : '->';
COLON    : ':';
COMMA    : ',';
DOT      : '.';
LPAREN   : '(';
RPAREN   : ')';
STAR     : '*';
EQUALS   : '=';
ALT      : '|';
SEMI     : ';;';

LBRACE: '{'
  {
    SimpleBlockLexer lex = new SimpleBlockLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lex);
    SimpleBlockParser parser = new SimpleBlockParser(tokens);
    parser.block();
  }
  ;

RBRACE: '}';

WS : ( ' '
       | '\t'
       | ( '\r\n' // DOS
           | '\n'   // Unix
           | '\r'   // Macintosh
           )
       )
  {$channel=HIDDEN;}
  ;

SLCOMMENT :
  '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
  {$channel=HIDDEN;}
  ;

MLCOMMENT :
  '/*' .* '*/' {$channel=HIDDEN;}
  ;

ID : ('a'..'z' | 'A'..'Z')
     ('a'..'z' | 'A'..'Z' | '0'..'9' | '_' | '-')* ;
