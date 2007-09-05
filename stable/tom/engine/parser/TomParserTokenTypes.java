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
	int ARROW = 18;
	int AND_CONNECTOR = 19;
	int OR_CONNECTOR = 20;
	int MATCH_CONSTRAINT = 21;
	int LESS_CONSTRAINT = 22;
	int LESSOREQUAL_CONSTRAINT = 23;
	int GREATER_CONSTRAINT = 24;
	int GREATEROREQUAL_CONSTRAINT = 25;
	int DOUBLEEQ = 26;
	int DIFFERENT_CONSTRAINT = 27;
	int EXTENDS = 28;
	int AT = 29;
	int ANTI_SYM = 30;
	int QMARK = 31;
	int XML_START = 32;
	int XML_CLOSE_SINGLETON = 33;
	int XML_CLOSE = 34;
	int XML_START_ENDING = 35;
	int XML_TEXT = 36;
	int XML_COMMENT = 37;
	int XML_PROC = 38;
	int LBRACKET = 39;
	int RBRACKET = 40;
	int EQUAL = 41;
	int UNDERSCORE = 42;
	int ALTERNATIVE = 43;
	int STAR = 44;
	int IMPLEMENT = 45;
	int VISITOR_FWD = 46;
	int EQUALS = 47;
	int IS_SORT = 48;
	int GET_HEAD = 49;
	int GET_TAIL = 50;
	int IS_EMPTY = 51;
	int GET_ELEMENT = 52;
	int GET_SIZE = 53;
	int IS_FSYM = 54;
	int GET_IMPLEMENTATION = 55;
	int GET_SLOT = 56;
	int MAKE = 57;
	int MAKE_EMPTY = 58;
	int MAKE_INSERT = 59;
	int MAKE_APPEND = 60;
	int WHERE = 61;
	int IF = 62;
	int WHEN = 63;
	int DOULEARROW = 64;
	int AFFECT = 65;
	int DOUBLE_QUOTE = 66;
	int WS = 67;
	int SLCOMMENT = 68;
	int ML_COMMENT = 69;
	int CONSTRAINT_GROUP_START = 70;
	int CONSTRAINT_GROUP_END = 71;
	int ESC = 72;
	int HEX_DIGIT = 73;
	int LETTER = 74;
	int DIGIT = 75;
	int ID = 76;
	int ID_MINUS = 77;
	int MINUS = 78;
	int PLUS = 79;
	int QUOTE = 80;
	int EXPONENT = 81;
	int DOT = 82;
	int FLOAT_SUFFIX = 83;
}
