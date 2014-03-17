// $ANTLR 2.7.7 (20060906): "BlockParser.g" -> "BlockLexer.java"$

  /*
   * Tom
   *
   * Copyright (c) 2010-2014, Universite de Lorraine, Inria
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
   * Antoine Reilles    e-mail: Antoine.Reilles@loria.fr
   *
   **/
  package tom.engine.parser.antlr2;

public interface BlockParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int LBRACE = 4;
	int RBRACE = 5;
	int STRING = 6;
	int ESC = 7;
	int HEX_DIGIT = 8;
	int WS = 9;
	int SL_COMMENT = 10;
	int ML_COMMENT = 11;
	int TARGET = 12;
}
