package minitom;

import java.io.*;
import java.util.*;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.CommonTree;
import minitom.minitomast.*;
import minitom.minitomast.types.*;
import minitom.minitomast.MiniTomMinitomAstAdaptor;

public class MainMiniTom {

  public static void main(String[] args) {
    try {
      InputStream input;

      if(args.length!=0) {
        File inputFile = new File(args[0]);
        input=new FileInputStream(inputFile);
      } else {
        input=System.in;
      }

      MiniTomLexer lexer = new MiniTomLexer(new ANTLRInputStream(input));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      MiniTomParser parser = new MiniTomParser(tokens);

      Tree tree = (Tree) parser.compilationUnit().getTree();
      //System.out.println("(DEBUG) tree =\n" + tree);
      //System.out.println("(DEBUG) tree.getText() = " + tree.getText() + " / tree.getType() = " + tree.getType());
      //System.out.println("(DEBUG) tree.getChild(0) = " + tree.getChild(0));
      //BlockList term = (BlockList) MiniTomAstAdaptor.getTerm(tree);
      CompilationUnit term = (CompilationUnit) MiniTomMinitomAstAdaptor.getTerm(tree);
      System.out.println("\nterm =\n\n" + term + "\n");
      tom.library.utils.Viewer.toTree(term);

    } catch (Exception e) {
      //System.err.println("exception: " + e);
      e.printStackTrace();
    }
  } //main

}//class
