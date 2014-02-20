// $ANTLR 2.7.7 (20060906): "TomLanguage.g" -> "TomParser.java"$

/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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
	int STRING = 24;
	int ELEMENTARY = 25;
	int TRAVERSAL = 26;
	int AT = 27;
	int ANTI_SYM = 28;
	int QQMARK = 29;
	int QMARK = 30;
	int STAR = 31;
	int NUM_INT = 32;
	int XML_CLOSE_SINGLETON = 33;
	int XML_START_ENDING = 34;
	int XML_TEXT = 35;
	int XML_COMMENT = 36;
	int XML_PROC = 37;
	int LBRACKET = 38;
	int RBRACKET = 39;
	int EQUAL = 40;
	int UNDERSCORE = 41;
	int ALTERNATIVE = 42;
	int CHARACTER = 43;
	int NUM_FLOAT = 44;
	int NUM_LONG = 45;
	int NUM_DOUBLE = 46;
	int IMPLEMENT = 47;
	int EQUALS = 48;
	int IS_SORT = 49;
	int GET_HEAD = 50;
	int GET_TAIL = 51;
	int IS_EMPTY = 52;
	int GET_ELEMENT = 53;
	int GET_SIZE = 54;
	int IS_FSYM = 55;
	int GET_SLOT = 56;
	int GET_DEFAULT = 57;
	int MAKE = 58;
	int MAKE_EMPTY = 59;
	int MAKE_INSERT = 60;
	int MAKE_APPEND = 61;
	int RARROW = 62;
	int DOULEARROW = 63;
	int AFFECT = 64;
	int POUNDSIGN = 65;
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
	int EXPONENT = 80;
	int DOT = 81;
	int FLOAT_SUFFIX = 82;
}
