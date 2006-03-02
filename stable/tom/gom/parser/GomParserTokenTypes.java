// $ANTLR 2.7.5 (20050128): "GomParser.g" -> "GomParser.java"$

  /*
   * Gom
   * 
   * Copyright (c) 2005-2006, INRIA
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
  package tom.gom.parser;

public interface GomParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int MODULE = 4;
	int IDENTIFIER = 5;
	int IMPORTS = 6;
	int PUBLIC = 7;
	int SORTS = 8;
	int ABSTRACT = 9;
	int SYNTAX = 10;
	int LEFT_BRACE = 11;
	int COMMA = 12;
	int RIGHT_BRACE = 13;
	int ARROW = 14;
	int STAR = 15;
	int COLON = 16;
	int PRIVATE = 17;
	int WS = 18;
	int SLCOMMENT = 19;
	int ML_COMMENT = 20;
}
