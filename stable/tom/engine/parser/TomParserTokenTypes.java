// $ANTLR 2.7.6 (20060516): "TomLanguage.g" -> "TomParser.java"$

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
	int EXTENDS = 21;
	int WHERE = 22;
	int AFFECT = 23;
	int IF = 24;
	int DOUBLEEQ = 25;
	int AT = 26;
	int XML_START = 27;
	int XML_CLOSE_SINGLETON = 28;
	int XML_CLOSE = 29;
	int XML_START_ENDING = 30;
	int XML_TEXT = 31;
	int XML_COMMENT = 32;
	int XML_PROC = 33;
	int LBRACKET = 34;
	int RBRACKET = 35;
	int EQUAL = 36;
	int UNDERSCORE = 37;
	int STAR = 38;
	int IMPLEMENT = 39;
	int VISITOR_FWD = 40;
	int EQUALS = 41;
	int GET_HEAD = 42;
	int GET_TAIL = 43;
	int IS_EMPTY = 44;
	int GET_ELEMENT = 45;
	int GET_SIZE = 46;
	int IS_FSYM = 47;
	int CHECK_STAMP = 48;
	int SET_STAMP = 49;
	int GET_IMPLEMENTATION = 50;
	int GET_SLOT = 51;
	int MAKE = 52;
	int MAKE_EMPTY = 53;
	int MAKE_INSERT = 54;
	int MAKE_APPEND = 55;
	int STAMP = 56;
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
