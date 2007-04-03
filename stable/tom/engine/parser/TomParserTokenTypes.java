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
	int QMARK = 28;
	int XML_START = 29;
	int XML_CLOSE_SINGLETON = 30;
	int XML_CLOSE = 31;
	int XML_START_ENDING = 32;
	int XML_TEXT = 33;
	int XML_COMMENT = 34;
	int XML_PROC = 35;
	int LBRACKET = 36;
	int RBRACKET = 37;
	int EQUAL = 38;
	int UNDERSCORE = 39;
	int STAR = 40;
	int IMPLEMENT = 41;
	int VISITOR_FWD = 42;
	int EQUALS = 43;
	int IS_SORT = 44;
	int GET_HEAD = 45;
	int GET_TAIL = 46;
	int IS_EMPTY = 47;
	int GET_ELEMENT = 48;
	int GET_SIZE = 49;
	int IS_FSYM = 50;
	int GET_IMPLEMENTATION = 51;
	int GET_SLOT = 52;
	int MAKE = 53;
	int MAKE_EMPTY = 54;
	int MAKE_INSERT = 55;
	int MAKE_APPEND = 56;
	int DOULEARROW = 57;
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
