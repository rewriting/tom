package newtom;
//package islander.newtom;

import java.io.*;
import java.util.*;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;

import newtom.tom.*;
import newtom.tom.types.*;
import newtom.backquote.*;
import newtom.backquote.types.*;

/*
import islander.newtom.tom.*;
import islander.newtom.tom.types.*;
import islander.newtom.backquote.*;
import islander.newtom.backquote.types.*;
*/

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

      NewHostLanguageLexer lexer = new NewHostLanguageLexer(new ANTLRInputStream(input));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      NewHostLanguageParser parser = new NewHostLanguageParser(tokens);

      System.out.println("(DEBUG) MainNewHost : init OK");
//      Tree tree = (Tree) parser.program().getTree();
      System.out.println("(DEBUG) *** parser.program() ***");
      NewHostLanguageParser.program_return tempo = parser.program();
      System.out.println("(DEBUG) *** before getTree() ***");
      Tree tree = (Tree) tempo.getTree();
      System.out.println("(DEBUG) MainNewHost : parsing & getTree() OK / tree =\n" + tree.toStringTree() + "\n");
     
      Program term = (Program) TomAdaptor.getTerm(tree);
      System.out.println("(DEBUG) MainNewTom : getTerm(tree) OK / term = " + term + "\n");
      tom.library.utils.Viewer.toTree(term);
      System.out.println("(DEBUG) MainNewTom : Visualization OK");

    } catch (Exception e) {
      //System.err.println("exception: " + e);
      e.printStackTrace();
    }
  } //main

} // MainNewTom
