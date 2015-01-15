/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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
 * Gregoire Brenon, Nabil Dhiba, and Hichem Mokrani (Mines de Nancy)
 *
 **/

grammar miniTom;

options {
	output=AST;
	ASTLabelType=Tree;
	backtrack=true;
}

tokens {
	HOSTBLOCK;
	MATCH;
	MATCHARG;
	TYPED;
	UNTYPED;
	TYPETERM;
	PATTERNACTION;
}

@parser::header {
package newparser;

import org.antlr.runtime.tree.*;
}

@lexer::header {
package newparser;

import java.util.Queue;
import java.util.LinkedList;
import org.antlr.runtime.tree.*;
}

@lexer::members{
int levelcounter=-1;
int currentToken = 0;
public static Queue<Tree> SubTrees = new LinkedList<Tree>();
}

/* Parser rules */

typetermconstruct :
  STRING LEFTBR implementKword (issortKword)? (equalsKword)? -> ^(TYPETERM implementKword (issortKword)? (equalsKword)?)
  ;

implementKword :
  IMPLEMENT s1=innerBlock -> ^(IMPLEMENT $s1)
  ;

issortKword : 
  ISSORT LEFTPAR s1=STRING RIGHTPAR s2=innerBlock -> ^(ISSORT $s2) 
  ;

equalsKword : 
  EQUALS LEFTPAR s1=STRING COMMA s2=STRING LEFTBR s3=innerBlock -> ^(EQUALS $s3)
  ;

matchconstruct :
  LEFTPAR RIGHTPAR LEFTBR patternaction*  -> ^(MATCH patternaction*)
  ;

patternaction :
  (s1=STRING) RARROW s2=innerBlock  -> ^(PATTERNACTION $s1 $s2)
  ;

innerBlock :
  VOUCHER -> { miniTomLexer.SubTrees.poll()}
  ;

/* Lexer rules */
VOUCHER    : ;
LEFTPAR    : '(' ;
RIGHTPAR   : ')' ;
LEFTBR     : '{' {
  if(currentToken == 0) {
    levelcounter++;
  } else {
    HostParser switcher = new HostParser(input,"{","}");
    if(!SubTrees.offer((Tree) switcher.getTree())) {
      System.out.println("Achtung ! Could not queue '{' tree");
    }
    emit(new ClassicToken(miniTomLexer.VOUCHER));
  }
} ;
RIGHTBR    : '}' {
  if(levelcounter<=0) { emit(Token.EOF_TOKEN); } else { levelcounter-=1; }
} ;
SEMICOLUMN : ';' ;
COMMA      : ',' ;
RARROW     : '->' {
  currentToken = miniTomLexer.RARROW ;
} ;
IMPLEMENT  : 'implement' {
  currentToken = miniTomLexer.IMPLEMENT ;
} ;
ISSORT     : 'is_sort' {
  currentToken = miniTomLexer.ISSORT ;
} ;
EQUALS     : 'equals' {
  currentToken = miniTomLexer.EQUALS ;
};
LETTER     : 'A'..'Z' | 'a'..'z';
DIGIT      : '0'..'9';
STRING     : LETTER (LETTER | DIGIT)*;
WS	: ('\r' | '\n' | '\t' | ' ' )* { $channel = HIDDEN; };
SL_COMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
  { $channel=HIDDEN; } ;
ML_COMMENT : '/*' ( options {greedy=false;} : . )* '*/'
  { $channel=HIDDEN; } ;

