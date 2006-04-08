package parsingtests;

import java.io.DataInputStream;
import java.io.*;

import antlr.collections.*;

import antlr.*;
import java.util.*;

import parsingtests.seq.*;
import parsingtests.seq.types.*;

public class Main {

  %include { seq/Seq.tom }

  public static void main(String[] args) {

    Sequent treeWalkerResult = null;

    try {
      //query = args[0];
      SeqLexer lexer = new SeqLexer(new DataInputStream(
            new FileInputStream(args[0])));
      SeqParser parser = new SeqParser(lexer);
      // Parse the input expression
      parser.setASTNodeClass("parsingtests.ATermAST");
      parser.seq();
      // walk the input
      ATermAST t = (ATermAST)parser.getAST();

      antlr.debug.misc.ASTFrame frame = new antlr.debug.misc.ASTFrame("AST JTree Example", t);
      frame.setVisible(true);

      HashMap hm = new HashMap();
      hm.put(new Integer(4),"SEQ");
      hm.put(new Integer(5),"END");
      hm.put(new Integer(6),"LIST");
      hm.put(new Integer(7),"IMPL");
      hm.put(new Integer(8),"AND");
      hm.put(new Integer(9),"OR");
      hm.put(new Integer(12),"NOT");
      hm.put(new Integer(13),"ID");


      System.out.println("TTT:" + t.genATermFromAST(hm));

      Variant1Gom v1g = new Variant1Gom();
      System.out.println("Result from variant 1 GOM : " + v1g.genFinalTree(t));

      /*			SeqTreeParser treeParser = new SeqTreeParser();
              treeWalkerResult = treeParser.seq(t);

              System.out.println("Result from tree walker __: " + treeWalkerResult);

              Variant1Gom v1g = new Variant1Gom();
              System.out.println("Result from variant 1 GOM : " + v1g.genFinalTree(t));

              Writer w = new OutputStreamWriter(System.out);
              t.xmlSerialize(w);
              w.write("\n");
              w.flush();
       */

    } catch (Exception e) {
      System.err.println("exception: " + e);
      e.printStackTrace();
    }
  }

}
