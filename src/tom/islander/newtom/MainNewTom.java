package newtom;

import java.io.*;
import java.util.*;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;

import newtom.newtomast.*;
import newtom.newtomast.types.*;

public class MainNewTom {

  public static void main(String[] args) {
    try {
      InputStream input;

      if(args.length!=0) {
        File inputFile = new File(args[0]);
        input=new FileInputStream(inputFile);
      } else {
        input=System.in;
      }

      NewTomLanguageLexer lexer = new NewTomLanguageLexer(new ANTLRInputStream(input));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      NewTomLanguageParser parser = new NewTomLanguageParser(tokens);

      System.out.println("(DEBUG) MainNewTom : init OK");
      Tree tree = (Tree) parser.tomCompilationUnit().getTree(); //orig
      /*
      NewTomLanguageParser.tomCompilationUnit_return tempo = parser.tomCompilationUnit();
      System.out.println("(DEBUG) MainTomJava :  parser.tomCompilationUnit() == tempo == " + tempo);
      Tree tree = (Tree) tempo.getTree();
      */
      System.out.println("(DEBUG) MainNewTom : parsing & getTree() OK / tree =\n" + tree.toStringTree() + "\n");
     
      TomCompilationUnit term = (TomCompilationUnit) NewTomAstAdaptor.getTerm(tree);
      System.out.println("(DEBUG) MainNewTom : getTerm(tree) OK / term = " + term + "\n");
      tom.library.utils.Viewer.toTree(term);
      System.out.println("(DEBUG) MainNewTom : Visualization OK");

    } catch (Exception e) {
      //System.err.println("exception: " + e);
      e.printStackTrace();
    }
  } //main

} // MainNewTom
