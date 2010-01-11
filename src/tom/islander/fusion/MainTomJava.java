package fusion;

import java.io.*;
import java.util.*;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;

import fusion.javatom.*;
import fusion.minijavaast.*;
import fusion.minijavaast.types.*;
import fusion.minitomast.*;
import fusion.minitomast.types.*;

public class MainTomJava {

  public static void main(String[] args) {
    try {
      InputStream input;

      if(args.length!=0) {
        File inputFile = new File(args[0]);
        input=new FileInputStream(inputFile);
      } else {
        input=System.in;
      }

      JavaTomLexer lexer = new JavaTomLexer(new ANTLRInputStream(input));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      JavaTomParser parser = new JavaTomParser(tokens);

      System.out.println("(DEBUG) MainTomJava : init OK");
      Tree tree = (Tree) parser.tomCompilationUnit().getTree(); //orig
      /*
      JavaTomParser.tomCompilationUnit_return tempo = parser.tomCompilationUnit();
      System.out.println("(DEBUG) MainTomJava :  parser.tomCompilationUnit() == tempo == " + tempo);
      Tree tree = (Tree) tempo.getTree();
      */
      System.out.println("(DEBUG) MainTomJava : parsing & getTree() OK / tree = " + tree);
      TomCompilationUnit term = (TomCompilationUnit) JavaTomAdaptor.getTerm(tree);
      //TomCompilationUnit term = (TomCompilationUnit) JavaTomJavaTomAdaptor.getTerm(tree);
      System.out.println("(DEBUG) MainTomJava : getTerm(tree) OK / term = " + term + "\n");
      tom.library.utils.Viewer.toTree(term);
      System.out.println("(DEBUG) MainTomJava : Visualization OK");

    } catch (Exception e) {
      //System.err.println("exception: " + e);
      e.printStackTrace();
    }
  } //main

} // MainTomJava
