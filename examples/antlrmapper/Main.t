package pom;

import java.io.DataInputStream;
import java.io.*;

import tom.pom.ATermAST;

import antlr.collections.*;

import antlr.*;
import java.util.*;

import aterm.*;
import aterm.pure.*;

import pom.seq.*;
import pom.seq.types.*;

public class Main {

  %include { seq/Seq.tom }

  public static void main(String[] args) {

    try {
      SeqLexer lexer = new SeqLexer(new DataInputStream(
            new FileInputStream(args[0])));
      SeqParser parser = new SeqParser(lexer);
      // Parse the input expression
      parser.setASTNodeClass("tom.pom.ATermAST");
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
