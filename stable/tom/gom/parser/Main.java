/*
 * Gom
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
 * Antoine Reilles        e-mail: Antoine.Reilles@loria.fr
 * 
 **/

package tom.gom.parser;

import java.io.*;

import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;

public class Main {
  public static void main(String[] args) throws FileNotFoundException {
    if (args[0].equals("block")) {
      // we will test the block parser on System.in
      runBlockParser();
      return;
    }
    File file = new File(args[0]);

    DataInputStream input = new DataInputStream(new FileInputStream(file));

    // attach lexer to the input stream
    GomLexer lexer = new GomLexer(input); // Create parser attached to lexer
    GomParser parser = new GomParser(lexer);
    // start up the parser by calling the rule
    // at which you want to begin parsing.
    try {
      GomModule mod = parser.module();
      System.out.println(mod);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static void runBlockParser() {
    BlockLexer lexer = new BlockLexer(System.in);
    BlockParser parser = new BlockParser(lexer,"BlockParser");
    try {
      String code = parser.block();
      System.out.println("------\n");
      System.out.println(code);
      System.out.println("------\n");
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
