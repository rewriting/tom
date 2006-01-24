// $ANTLR 2.7.5 (20050128): "TomLanguage.g" -> "TomParser.java"$

/*
 * 
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

package tom.engine.parser;


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
	int WHEN = 19;
	int ARROW = 20;
	int WHERE = 21;
	int AFFECT = 22;
	int IF = 23;
	int DOUBLEEQ = 24;
	int AT = 25;
	int XML_START = 26;
	int XML_CLOSE_SINGLETON = 27;
	int XML_CLOSE = 28;
	int XML_START_ENDING = 29;
	int XML_TEXT = 30;
	int XML_COMMENT = 31;
	int XML_PROC = 32;
	int LBRACKET = 33;
	int RBRACKET = 34;
	int EQUAL = 35;
	int UNDERSCORE = 36;
	int STAR = 37;
	int IMPLEMENT = 38;
	int EQUALS = 39;
	int GET_HEAD = 40;
	int GET_TAIL = 41;
	int IS_EMPTY = 42;
	int GET_ELEMENT = 43;
	int GET_SIZE = 44;
	int IS_FSYM = 45;
	int CHECK_STAMP = 46;
	int SET_STAMP = 47;
	int GET_IMPLEMENTATION = 48;
	int GET_SLOT = 49;
	int MAKE = 50;
	int MAKE_EMPTY = 51;
	int MAKE_INSERT = 52;
	int MAKE_APPEND = 53;
	int EXTENDS = 54;
	int STAMP = 55;
	int DOULEARROW = 56;
	int DOUBLE_QUOTE = 57;
	int WS = 58;
	int SLCOMMENT = 59;
	int ML_COMMENT = 60;
	int ESC = 61;
	int HEX_DIGIT = 62;
	int LETTER = 63;
	int DIGIT = 64;
	int ID = 65;
	int ID_MINUS = 66;
	int MINUS = 67;
	int PLUS = 68;
	int QUOTE = 69;
	int EXPONENT = 70;
	int DOT = 71;
	int FLOAT_SUFFIX = 72;
}
