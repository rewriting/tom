// $ANTLR 2.7.7 (20060906): "BackQuoteLanguage.g" -> "BackQuoteParser.java"$
/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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
  

public interface BackQuoteParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int BQ_BACKQUOTE = 4;
	int BQ_UNDERSCORE = 5;
	int BQ_ID = 6;
	int BQ_STAR = 7;
	int BQ_LPAREN = 8;
	int BQ_RPAREN = 9;
	int BQ_LBRACKET = 10;
	int BQ_RBRACKET = 11;
	int BQ_LBRACE = 12;
	int BQ_RBRACE = 13;
	int BQ_DOT = 14;
	int BQ_COMMA = 15;
	int EQUAL = 16;
	int BQ_WS = 17;
	int BQ_INTEGER = 18;
	int BQ_STRING = 19;
	int BQ_MINUS = 20;
	int DOUBLE_QUOTE = 21;
	int ANY = 22;
	int BQ_SIMPLE_ID = 23;
	int BQ_MINUS_ID = 24;
	int BQ_MINUS_ID_PART = 25;
	int BQ_DIGIT = 26;
	int BQ_ESC = 27;
	int BQ_HEX_DIGIT = 28;
}
