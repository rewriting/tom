// $ANTLR 2.7.7 (20060906): "TomLanguage.g" -> "TomParser.java"$

/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2011, INPL, INRIA
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

package tom.engine.parser.antlr2;


public interface TomParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int LPAREN = 4;
	int RPAREN = 5;
	int LBRACE = 6;
	int RBRACE = 7;
	int COMMA = 8;
	int BACKQUOTE = 9;
	int ALL_ID = 10;
	int COLON = 11;
	int AND_CONNECTOR = 12;
	int OR_CONNECTOR = 13;
	int ARROW = 14;
	int MATCH_CONSTRAINT = 15;
	int XML_START = 16;
	int LESSOREQUAL_CONSTRAINT = 17;
	int XML_CLOSE = 18;
	int GREATEROREQUAL_CONSTRAINT = 19;
	int DOUBLEEQ = 20;
	int DIFFERENT_CONSTRAINT = 21;
	int EXTENDS = 22;
	int LITERAL_visit = 23;
	int AT = 24;
	int ANTI_SYM = 25;
	int QQMARK = 26;
	int QMARK = 27;
	int STAR = 28;
	int NUM_INT = 29;
	int STRING = 30;
	int XML_CLOSE_SINGLETON = 31;
	int XML_START_ENDING = 32;
	int XML_TEXT = 33;
	int XML_COMMENT = 34;
	int XML_PROC = 35;
	int LBRACKET = 36;
	int RBRACKET = 37;
	int EQUAL = 38;
	int UNDERSCORE = 39;
	int ALTERNATIVE = 40;
	int CHARACTER = 41;
	int NUM_FLOAT = 42;
	int NUM_LONG = 43;
	int NUM_DOUBLE = 44;
	int IMPLEMENT = 45;
	int EQUALS = 46;
	int IS_SORT = 47;
	int GET_HEAD = 48;
	int GET_TAIL = 49;
	int IS_EMPTY = 50;
	int GET_ELEMENT = 51;
	int GET_SIZE = 52;
	int IS_FSYM = 53;
	int GET_SLOT = 54;
	int GET_DEFAULT = 55;
	int MAKE = 56;
	int MAKE_EMPTY = 57;
	int MAKE_INSERT = 58;
	int MAKE_APPEND = 59;
	int WHERE = 60;
	int IF = 61;
	int WHEN = 62;
	int DOULEARROW = 63;
	int AFFECT = 64;
	int DOUBLE_QUOTE = 65;
	int WS = 66;
	int SLCOMMENT = 67;
	int ML_COMMENT = 68;
	int CONSTRAINT_GROUP_START = 69;
	int CONSTRAINT_GROUP_END = 70;
	int ESC = 71;
	int HEX_DIGIT = 72;
	int LETTER = 73;
	int DIGIT = 74;
	int ID = 75;
	int ID_MINUS = 76;
	int MINUS = 77;
	int PLUS = 78;
	int EXPONENT = 79;
	int DOT = 80;
	int FLOAT_SUFFIX = 81;
}
