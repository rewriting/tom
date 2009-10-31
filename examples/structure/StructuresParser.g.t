/*
 * Copyright (c) 2004-2009, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
header {
package structure;

import structure.structures.*;
import structure.structures.types.*;
}

class StructuresParser extends Parser;
options {
    buildAST = true;
    k = 1;
}
{
  %include { structures/Structures.tom }
}

struc returns [Struc s]
{
	StrucPar pl = null;
	StrucCop cl = null;
	StrucSeq sl = null;
	Struc n = null;
	s = null;
}
: (
  LPAR^ pl = list_par RPAR
	{
		s = `Par(pl*);
	}
	| LCOP^ cl = list_cop RCOP
	{
		s = `Cop(cl*);
	}
	| LSEQ^ sl = list_seq RSEQ
	{
		s = `Seq(sl*);
	}
	| NEG n = struc
	{
		s = `Neg(n);
	}
	| n = atom
	{
		s = n;
	}
	)
	;

list_par returns [StrucPar s]
{
	Struc lhs = null;
	StrucPar listOfLhs = null;
	s = `ConcPar();
}
: 
lhs = struc {listOfLhs = `ConcPar(lhs);}
(SEP^ lhs = struc
	{listOfLhs = `ConcPar(listOfLhs*,lhs);}
	)*
{
	s = listOfLhs;
}
;

list_cop returns [StrucCop s]
{
	Struc lhs = null;
	StrucCop listOfLhs = null;
	s = `ConcCop();
}
: lhs = struc {listOfLhs = `ConcCop(lhs);}
(SEP^ lhs = struc
	{listOfLhs = `ConcCop(listOfLhs*,lhs);}
	)*
{
	s = listOfLhs;
}
;

list_seq returns [StrucSeq s]
{
	Struc lhs = null;
	StrucSeq listOfLhs = null;
	s = `ConcSeq();
}
: lhs = struc {listOfLhs = `ConcSeq(lhs);}
(SEQ^ lhs = struc
	{listOfLhs = `ConcSeq(listOfLhs*,lhs);}
	)*
{
	s = listOfLhs;
}
;

atom returns [Struc s]
{
	s = null;
}
: i:ID
{
  String name = i.getText();
  if ("O".equals(name)) {
    s = `O();
  } else {
    s = `Atom(name);
  }
}
;

class StructuresLexer extends Lexer;

options {
	k=2; // for newline
}

// Single-line comments
SL_COMMENT
: "#"
(~('\n'|'\r'))* ('\n'|'\r'('\n')?)
{$setType(Token.SKIP); newline();}
;
WS
: (' '
	 | '\t'
	 | '\n'
	 | '\r')
{ $setType(Token.SKIP); }
;
LCOP: '(';
RCOP: ')';
LSEQ: '<';
RSEQ: '>';
LPAR: '[';
RPAR: ']';
NEG:  '-';
SEP:  ',';
SEQ:  ';';
ID : ('A'..'Z'
			|'a'..'z'
			|'0'..'9'
	)+
;
