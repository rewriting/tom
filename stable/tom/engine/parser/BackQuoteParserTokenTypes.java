// $ANTLR 2.7.4: "BackQuoteLanguage.g" -> "BackQuoteParser.java"$
/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
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
  

public interface BackQuoteParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int BQ_ID = 4;
	int BQ_STAR = 5;
	int BQ_LPAREN = 6;
	int BQ_RPAREN = 7;
	int BQ_DOT = 8;
	int XML = 9;
	int BQ_COMMA = 10;
	int BQ_STRING = 11;
	int BQ_WS = 12;
	int BQ_INTEGER = 13;
	int BQ_MINUS = 14;
	int DOUBLE_QUOTE = 15;
	int XML_START = 16;
	int XML_EQUAL = 17;
	int XML_CLOSE = 18;
	int ANY = 19;
	int XML_START_ENDING = 20;
	int XML_CLOSE_SINGLETON = 21;
	int XML_TEXT = 22;
	int XML_COMMENT = 23;
	int XML_PROC = 24;
	int XML_SKIP = 25;
	int BQ_SIMPLE_ID = 26;
	int BQ_MINUS_ID = 27;
	int BQ_DIGIT = 28;
	int BQ_UNDERSCORE = 29;
	int BQ_ESC = 30;
	int BQ_HEX_DIGIT = 31;
}
