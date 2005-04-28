// $ANTLR 2.7.5 (20050128): "HostLanguage.g" -> "HostParser.java"$
/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2005, INRIA
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
  
package jtom.parser;


public interface HostParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int STRING = 4;
	int LBRACE = 5;
	int RBRACE = 6;
	int RULE = 7;
	int MATCH = 8;
	int VAS = 9;
	int BACKQUOTE = 10;
	int OPERATOR = 11;
	int OPERATORLIST = 12;
	int OPERATORARRAY = 13;
	int INCLUDE = 14;
	int TYPETERM = 15;
	int TYPE = 16;
	int ESC = 17;
	int HEX_DIGIT = 18;
	int WS = 19;
	int COMMENT = 20;
	int SL_COMMENT = 21;
	int ML_COMMENT = 22;
	int TARGET = 23;
}
