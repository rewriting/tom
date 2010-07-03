package backquote;

import java.io.*;
import java.util.*;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;

import backquote.newbackquoteast.*;
import backquote.newbackquoteast.types.*;

public class MainNewBackQuote {

  public static void main(String[] args) {
    try {
      InputStream input;

      if(args.length!=0) {
        File inputFile = new File(args[0]);
        input=new FileInputStream(inputFile);
      } else {
        input=System.in;
      }

      NewBackQuoteLanguageLexer lexer = new NewBackQuoteLanguageLexer(new ANTLRInputStream(input));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      NewBackQuoteLanguageParser parser = new NewBackQuoteLanguageParser(tokens);
      System.out.println("(DEBUG) MainNewBackQuote : init OK");
      Tree tree = (Tree) parser.backQuoteTerm().getTree();
      System.out.println("(DEBUG) MainNewBackQuote : parsing & getTree() OK / tree =\n" + tree.toStringTree() + "\n");
      BackQuoteTerm term = (BackQuoteTerm) NewBackQuoteAstAdaptor.getTerm(tree);
      System.out.println("(DEBUG) MainNewBackQuote : getTerm(tree) OK / term = " + term + "\n");
      tom.library.utils.Viewer.toTree(term);
      System.out.println("(DEBUG) MainNewBackQuote : Visualization OK");
    } catch (Exception e) {
      //System.err.println("exception: " + e);
      e.printStackTrace();
    }
  } //main

} // MainNewBackQuote
