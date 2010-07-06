//package islander.proto;

import java.io.*;
import java.util.*;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;

import host.*;
import host.types.*;
import tom.*;
import tom.types.*;
import backquote.*;
import backquote.types.*;

public class Main {

  public static void main(String[] args) {
    try {
      InputStream input;

      if(args.length!=0) {
        File inputFile = new File(args[0]);
        input=new FileInputStream(inputFile);
      } else {
        input=System.in;
      }

      HostLanguageLexer lexer = new HostLanguageLexer(new ANTLRInputStream(input));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      HostLanguageParser parser = new HostLanguageParser(tokens);

      System.out.println("\n(DEBUG) MainNewTom : init OK");
      Tree tree = (Tree) parser.program().getTree();
      System.out.println("\n(DEBUG) MainNewTom : parsing & getTree() OK / tree =\n" + tree.toStringTree());
      System.out.println("\n(DEBUG) before getTerm()");
      Program term = (Program) HostAdaptor.getTerm(tree);
      System.out.println("\n(DEBUG) MainNewTom : getTerm(tree) OK / term =\n" + term + "\n");
      tom.library.utils.Viewer.toTree(term);
      System.out.println("\n(DEBUG) MainNewTom : Visualization OK");

    } catch (Exception e) {
      //System.err.println("exception: " + e);
      e.printStackTrace();
    }
  } //main

} // Main
