package minijava;

import java.io.*;
import java.util.*;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;

import minijava.ast.*;
import minijava.ast.types.*;
import minijava.ast.AstAdaptor;

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

      Tree tree = (Tree) parser.compilationUnit().getTree();
      CompilationUnit term = (CompilationUnit) AstAdaptor.getTerm(tree);
      tom.library.utils.Viewer.toTree(term);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
