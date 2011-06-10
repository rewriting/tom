// $ANTLR 2.7.7 (20060906): "BackQuoteLanguage.g" -> "BackQuoteParser.java"$
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

package tom.engine.parser;
  

public interface BackQuoteParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int BQ_BACKQUOTE = 4;
	int BQ_UNDERSCORE = 5;
	int BQ_ID = 6;
	int BQ_STAR = 7;
	int BQ_LPAREN = 8;
	int BQ_RPAREN = 9;
	int BQ_LBRACE = 10;
	int BQ_RBRACE = 11;
	int BQ_DOT = 12;
	int XML = 13;
	int BQ_COMMA = 14;
	int BQ_STRING = 15;
	int BQ_WS = 16;
	int BQ_INTEGER = 17;
	int BQ_MINUS = 18;
	int DOUBLE_QUOTE = 19;
	int XML_START = 20;
	int XML_EQUAL = 21;
	int XML_CLOSE = 22;
	int ANY = 23;
	int XML_START_ENDING = 24;
	int XML_CLOSE_SINGLETON = 25;
	int XML_TEXT = 26;
	int XML_COMMENT = 27;
	int XML_PROC = 28;
	int XML_SKIP = 29;
	int BQ_SIMPLE_ID = 30;
	int BQ_MINUS_ID = 31;
	int BQ_MINUS_ID_PART = 32;
	int BQ_DIGIT = 33;
	int BQ_ESC = 34;
	int BQ_HEX_DIGIT = 35;
}
