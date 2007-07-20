// $ANTLR 2.7.7 (20060906): "TomLanguage.g" -> "TomParser.java"$

/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2007, INRIA
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
	int BACKQUOTE = 15;
	int ALL_ID = 16;
	int COLON = 17;
	int ALTERNATIVE = 18;
	int ARROW = 19;
	int EXTENDS = 20;
	int AT = 21;
	int ANTI_SYM = 22;
	int QMARK = 23;
	int XML_START = 24;
	int XML_CLOSE_SINGLETON = 25;
	int XML_CLOSE = 26;
	int XML_START_ENDING = 27;
	int XML_TEXT = 28;
	int XML_COMMENT = 29;
	int XML_PROC = 30;
	int LBRACKET = 31;
	int RBRACKET = 32;
	int EQUAL = 33;
	int UNDERSCORE = 34;
	int STAR = 35;
	int IMPLEMENT = 36;
	int VISITOR_FWD = 37;
	int EQUALS = 38;
	int IS_SORT = 39;
	int GET_HEAD = 40;
	int GET_TAIL = 41;
	int IS_EMPTY = 42;
	int GET_ELEMENT = 43;
	int GET_SIZE = 44;
	int IS_FSYM = 45;
	int GET_IMPLEMENTATION = 46;
	int GET_SLOT = 47;
	int MAKE = 48;
	int MAKE_EMPTY = 49;
	int MAKE_INSERT = 50;
	int MAKE_APPEND = 51;
	int WHERE = 52;
	int IF = 53;
	int WHEN = 54;
	int DOULEARROW = 55;
	int AFFECT = 56;
	int DOUBLEEQ = 57;
	int DOUBLE_QUOTE = 58;
	int WS = 59;
	int SLCOMMENT = 60;
	int ML_COMMENT = 61;
	int ESC = 62;
	int HEX_DIGIT = 63;
	int LETTER = 64;
	int DIGIT = 65;
	int ID = 66;
	int ID_MINUS = 67;
	int MINUS = 68;
	int PLUS = 69;
	int QUOTE = 70;
	int EXPONENT = 71;
	int DOT = 72;
	int FLOAT_SUFFIX = 73;
}
