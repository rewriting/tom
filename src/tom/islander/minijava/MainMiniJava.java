package minijava;

import java.io.*;
import java.util.*;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;

import minijava.minijavaast.*;
import minijava.minijavaast.types.*;
import minijava.minijavaast.MiniJavaMinijavaAstAdaptor;

public class MainMiniJava {

  public static void main(String[] args) {
    try {
      InputStream input;

      if(args.length!=0) {
        File inputFile = new File(args[0]);
        input=new FileInputStream(inputFile);
      } else {
        input=System.in;
      }

      MiniJavaLexer lexer = new MiniJavaLexer(new ANTLRInputStream(input));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      MiniJavaParser parser = new MiniJavaParser(tokens);

      Tree tree = (Tree) parser.javaCompilationUnit().getTree();
      JavaCompilationUnit term = (JavaCompilationUnit) /*MiniJava*/ MinijavaAstAdaptor.getTerm(tree);
      tom.library.utils.Viewer.toTree(term);

    } catch (Exception e) {
      //System.err.println("exception: " + e);
      e.printStackTrace();
    }
  } //main

}//class
