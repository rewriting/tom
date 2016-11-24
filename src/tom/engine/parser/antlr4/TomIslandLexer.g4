/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2016-2016, Universite de Lorraine
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

lexer grammar TomIslandLexer;
@members {
public static final int WHITESPACE = 1; 
public static final int COMMENTS = 2;
}


MATCH : '%match' ;
STRATEGY : '%strategy' ;
EXTENDS : 'extends' ;
VISIT : 'visit' ;
INCLUDE : '%include' ;
GOM : '%gom' ;
OP : '%op' ;
OPARRAY : '%oparray' ;
OPLIST : '%oplist' ;
TYPETERM : '%typeterm' ;
IS_FSYM : 'is_fsym' ;
IS_SORT : 'is_sort' ;
MAKE : 'make' ;
MAKE_EMPTY : 'make_empty' ;
MAKE_APPEND : 'make_append' ;
MAKE_INSERT : 'make_insert' ;
GET_SLOT : 'get_slot' ;
GET_DEFAULT : 'get_default' ;
GET_ELEMENT : 'get_element' ;
GET_HEAD : 'get_head' ;
GET_TAIL : 'get_tail' ;
GET_SIZE : 'get_size' ;
IS_EMPTY : 'is_empty' ;
IMPLEMENT : 'implement' ;
EQUALS : 'equals' ;


MATCH_SYMBOL : '<<' ;
EQUAL : '=' ;
LBRACE : '{' ;
RBRACE : '}' ;
LPAREN : '(' ;
RPAREN : ')' ;
LSQUAREBR : '[' ;
RSQUAREBR : ']' ;
ARROW : '->' ;
COMMA : ',' ;
COLON : ':' ;
STAR : '*' ;
UNDERSCORE : '_' ;
ANTI : '!' ;
AT : '@' ;
METAQUOTE : '%[' .*? ']%';
//LMETAQUOTE : '%[' ;
//RMETAQUOTE : ']%' ;
ATAT : '@@' ;

GREATEROREQ : '>=';
LOWEROREQ : '<=';
GREATERTHAN : '>';
LOWERTHAN : '<';
DOUBLEEQ : '==';
DIFFERENT : '!=';
AND : '&&';
OR : '||';
PIPE : '|';
QMARK : '?';
DQMARK : '??';
SLASH : '/';
BACKSLASH : '\\';
DOT : '.';
BQUOTE : '`' ;

ID : ('_')? LETTER (LETTER | DIGIT | '_' )*;
DMINUSID : '--' LETTER* ;

INTEGER : ('-')? (DIGIT)+;
DOUBLE  : ('-')? UNSIGNED_DOUBLE;
LONG    : ('-')? (DIGIT)+ LONG_SUFFIX;
STRING  : '"'  ( ESC | ~('"' |'\\'|'\n'|'\r') )* '"';
CHAR    : '\'' ( ESC | ~('\''|'\\'|'\n'|'\r') )+ '\'';

fragment UNSIGNED_DOUBLE : (DIGIT)+'.'(DIGIT)* | '.' (DIGIT)+;
fragment LONG_SUFFIX : 'l'|'L' ; 

fragment LETTER	: 'A'..'Z' | 'a'..'z';
fragment DIGIT	: '0'..'9';
fragment HEX_DIGIT : ('0'..'9'|'A'..'F'|'a'..'f');

fragment ESC :
  '\\'
    ( 'n' | 'r' | 't' | 'b' | 'f' | '"' | '\'' | '\\'
    | ('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    | '0'..'3' ( '0'..'7' ( '0'..'7')?)?
    | '4'..'7' ( '0'..'7')?
    )
  ;


ACTION_ESCAPE :   '\\' .  ;
ACTION_STRING_LITERAL :	'"' (ACTION_ESCAPE | ~["\\])* '"' ;
MLCOMMENT : '/*' .*? '*/' -> skip ;
SLCOMMENT : '//' ~[\r\n]* -> skip;

WS : [ \r\t]+ -> channel(HIDDEN) ;
NL : [\n]+  -> channel(HIDDEN) ;
ANY : . ; 
