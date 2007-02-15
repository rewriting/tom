/*
 * Copyright (c) 2004-2007, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package antlrmapper;

import java.io.DataInputStream;
import java.io.*;

import tom.antlrmapper.ATermAST;

import antlr.collections.*;

import antlr.*;
import java.util.*;

import aterm.*;
import aterm.pure.*;

import antlrmapper.seq.*;
import antlrmapper.seq.types.*;

public class Main {

  %include { seq/Seq.tom }

  public static void main(String[] args) {

    try {
      SeqLexer lexer = new SeqLexer(new DataInputStream(
            new FileInputStream(args[0])));
      SeqParser parser = new SeqParser(lexer);
      // Parse the input expression
      parser.setASTNodeClass("tom.antlrmapper.ATermAST");
      parser.seq();
      // walk the input
      ATermAST t = (ATermAST)parser.getAST();

      antlr.debug.misc.ASTFrame frame = new antlr.debug.misc.ASTFrame("AST JTree Example", t);
      frame.setVisible(true);

      System.out.println("TTT:" + t.genATermFromAST(TokenTable.getTokenMap()));

      System.out.println("Result from variant 1 GOM : " + AST2Gom.genFinalTree(t));

    } catch (Exception e) {
      System.err.println("exception: " + e);
      e.printStackTrace();
    }
  }
}
