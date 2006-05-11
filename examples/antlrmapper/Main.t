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
  %include { Mapping.tom }

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

      System.out.println("Result from variant 1 GOM : " + genFinalTree(t));

    } catch (Exception e) {
      System.err.println("exception: " + e);
      e.printStackTrace();
    }
  }

  public static Sequent genFinalTree(ATermAST t){
    return getFinalGomTree(t.genATermFromAST(TokenTable.getTokenMap()));
  }

  public static Sequent getFinalGomTree(ATerm n) {
    %match(ATerm n){
      SEQ(_,(N1,N2)) ->{
        return `seq(getListGom(N1),getListGom(N2));
      }
    }
    throw new RuntimeException("Unable to translate SEQ " + n);
  }

  private static ListPred getListGom(ATerm n){
    %match(ATerm n){
      LIST(_,(N1,N2)) -> {
        ListPred l = getListGom(`N2);
        return `concPred(getNodeGom(N1),l*);
      }
      _ -> {
        return `concPred(getNodeGom(n));
      }
    }
    throw new RuntimeException("Unable to translate LIST " + n);
  }

  private static Pred getNodeGom(ATerm n){
    %match(ATerm n){
      IMPL(_,(N1,N2)) ->{
        return `impl(getNodeGom(N1),getNodeGom(N2));
      }
      AND(_,(N1,N2)) ->{
        return `wedge(getNodeGom(N1),getNodeGom(N2));
      }
      OR(_,(N1,N2)) ->{
        return `vee(getNodeGom(N1),getNodeGom(N2));
      }
      NOT(_,(N1,_*)) ->{
        return `neg(getNodeGom(N1));
      }
      ID(NodeInfo[text=text],_) ->{
        return Pred.fromTerm(
            aterm.pure.SingletonFactory.getInstance().parse(`text));
      }
    }
    throw new RuntimeException("Unable to translate " + n);
  }
}
