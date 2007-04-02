// $ANTLR 2.7.6 (20060516): "TomLanguage.g" -> "TomParser.java"$

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
	int WHEN = 19;
	int ARROW = 20;
	int EXTENDS = 21;
	int WHERE = 22;
	int AFFECT = 23;
	int IF = 24;
	int DOUBLEEQ = 25;
	int AT = 26;
	int ANTI_SYM = 27;
	int XML_START = 28;
	int XML_CLOSE_SINGLETON = 29;
	int XML_CLOSE = 30;
	int XML_START_ENDING = 31;
	int XML_TEXT = 32;
	int XML_COMMENT = 33;
	int XML_PROC = 34;
	int LBRACKET = 35;
	int RBRACKET = 36;
	int EQUAL = 37;
	int UNDERSCORE = 38;
	int STAR = 39;
	int IMPLEMENT = 40;
	int VISITOR_FWD = 41;
	int EQUALS = 42;
	int IS_SORT = 43;
	int GET_HEAD = 44;
	int GET_TAIL = 45;
	int IS_EMPTY = 46;
	int GET_ELEMENT = 47;
	int GET_SIZE = 48;
	int IS_FSYM = 49;
	int CHECK_STAMP = 50;
	int SET_STAMP = 51;
	int GET_IMPLEMENTATION = 52;
	int GET_SLOT = 53;
	int MAKE = 54;
	int MAKE_EMPTY = 55;
	int MAKE_INSERT = 56;
	int MAKE_APPEND = 57;
	int STAMP = 58;
	int DOULEARROW = 59;
	int DOUBLE_QUOTE = 60;
	int WS = 61;
	int SLCOMMENT = 62;
	int ML_COMMENT = 63;
	int ESC = 64;
	int HEX_DIGIT = 65;
	int LETTER = 66;
	int DIGIT = 67;
	int ID = 68;
	int ID_MINUS = 69;
	int MINUS = 70;
	int PLUS = 71;
	int QUOTE = 72;
	int EXPONENT = 73;
	int DOT = 74;
	int FLOAT_SUFFIX = 75;
}
