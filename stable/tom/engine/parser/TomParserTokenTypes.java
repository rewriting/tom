// $ANTLR 2.7.4: "TomLanguage.g" -> "TomParser.java"$
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


public interface TomParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int NUM_INT = 4;
	int CHARACTER = 5;
	int STRING = 6;
	int NUM_FLOAT = 7;
	int NUM_LONG = 8;
	int NUM_DOUBLE = 9;
	int LPAREN = 10;
	int RPAREN = 11;
	int LBRACE = 12;
	int RBRACE = 13;
	int COMMA = 14;
	int ALL_ID = 15;
	int BACKQUOTE = 16;
	int COLON = 17;
	int ALTERNATIVE = 18;
	int ARROW = 19;
	int WHERE = 20;
	int AFFECT = 21;
	int IF = 22;
	int DOUBLEEQ = 23;
	int AT = 24;
	int XML_START = 25;
	int XML_CLOSE_SINGLETON = 26;
	int XML_CLOSE = 27;
	int XML_START_ENDING = 28;
	int XML_TEXT = 29;
	int XML_COMMENT = 30;
	int XML_PROC = 31;
	int LBRACKET = 32;
	int RBRACKET = 33;
	int EQUAL = 34;
	int UNDERSCORE = 35;
	int STAR = 36;
	int IMPLEMENT = 37;
	int GET_FUN_SYM = 38;
	int GET_SUBTERM = 39;
	int CMP_FUN_SYM = 40;
	int EQUALS = 41;
	int GET_HEAD = 42;
	int GET_TAIL = 43;
	int IS_EMPTY = 44;
	int GET_ELEMENT = 45;
	int GET_SIZE = 46;
	int FSYM = 47;
	int IS_FSYM = 48;
	int CHECK_STAMP = 49;
	int SET_STAMP = 50;
	int GET_IMPLEMENTATION = 51;
	int GET_SLOT = 52;
	int MAKE = 53;
	int MAKE_EMPTY = 54;
	int MAKE_INSERT = 55;
	int MAKE_APPEND = 56;
	int STAMP = 57;
	int DOULEARROW = 58;
	int DOUBLE_QUOTE = 59;
	int WS = 60;
	int SLCOMMENT = 61;
	int ML_COMMENT = 62;
	int ESC = 63;
	int HEX_DIGIT = 64;
	int LETTER = 65;
	int DIGIT = 66;
	int ID = 67;
	int ID_MINUS = 68;
	int MINUS = 69;
	int PLUS = 70;
	int QUOTE = 71;
	int EXPONENT = 72;
	int DOT = 73;
	int FLOAT_SUFFIX = 74;
}
