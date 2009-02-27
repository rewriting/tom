// $ANTLR 2.7.7 (20060906): "TomLanguage.g" -> "TomParser.java"$

/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2009, INRIA
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
	int AND_CONNECTOR = 18;
	int OR_CONNECTOR = 19;
	int ARROW = 20;
	int MATCH_CONSTRAINT = 21;
	int XML_START = 22;
	int LESSOREQUAL_CONSTRAINT = 23;
	int XML_CLOSE = 24;
	int GREATEROREQUAL_CONSTRAINT = 25;
	int DOUBLEEQ = 26;
	int DIFFERENT_CONSTRAINT = 27;
	int EXTENDS = 28;
	int AT = 29;
	int ANTI_SYM = 30;
	int QMARK = 31;
	int XML_CLOSE_SINGLETON = 32;
	int XML_START_ENDING = 33;
	int XML_TEXT = 34;
	int XML_COMMENT = 35;
	int XML_PROC = 36;
	int LBRACKET = 37;
	int RBRACKET = 38;
	int EQUAL = 39;
	int UNDERSCORE = 40;
	int ALTERNATIVE = 41;
	int STAR = 42;
	int IMPLEMENT = 43;
	int EQUALS = 44;
	int IS_SORT = 45;
	int GET_HEAD = 46;
	int GET_TAIL = 47;
	int IS_EMPTY = 48;
	int GET_ELEMENT = 49;
	int GET_SIZE = 50;
	int IS_FSYM = 51;
	int GET_IMPLEMENTATION = 52;
	int GET_SLOT = 53;
	int MAKE = 54;
	int MAKE_EMPTY = 55;
	int MAKE_INSERT = 56;
	int MAKE_APPEND = 57;
	int WHERE = 58;
	int IF = 59;
	int WHEN = 60;
	int DOULEARROW = 61;
	int AFFECT = 62;
	int DOUBLE_QUOTE = 63;
	int WS = 64;
	int SLCOMMENT = 65;
	int ML_COMMENT = 66;
	int LESS_CONSTRAINT = 67;
	int CONSTRAINT_GROUP_START = 68;
	int CONSTRAINT_GROUP_END = 69;
	int ESC = 70;
	int HEX_DIGIT = 71;
	int LETTER = 72;
	int DIGIT = 73;
	int ID = 74;
	int ID_MINUS = 75;
	int MINUS = 76;
	int PLUS = 77;
	int QUOTE = 78;
	int EXPONENT = 79;
	int DOT = 80;
	int FLOAT_SUFFIX = 81;
}
