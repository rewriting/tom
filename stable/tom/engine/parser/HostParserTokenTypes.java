// $ANTLR 2.7.6 (20060516): "HostLanguage.g" -> "HostParser.java"$
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

public interface HostParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int STRING = 4;
	int LBRACE = 5;
	int RBRACE = 6;
	int STRATEGY = 7;
	int RULE = 8;
	int MATCH = 9;
	int GOM = 10;
	int BACKQUOTE = 11;
	int OPERATOR = 12;
	int OPERATORLIST = 13;
	int OPERATORARRAY = 14;
	int INCLUDE = 15;
	int CODE = 16;
	int TYPETERM = 17;
	int ESC = 18;
	int HEX_DIGIT = 19;
	int WS = 20;
	int COMMENT = 21;
	int SL_COMMENT = 22;
	int ML_COMMENT = 23;
	int TARGET = 24;
}
