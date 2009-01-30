package parser;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;
import parser.term.TermAdaptor;

public class GraphMain {

  public static void main(String[] args) {
    try {
      GraphLexer lexer = new GraphLexer(new ANTLRInputStream(System.in));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      GraphParser parser = new GraphParser(tokens);
      // Parse the input expression
      Tree b = (Tree) parser.node().getTree();
      System.out.println("Result = " + TermAdaptor.getTerm(b));
    } catch (Exception e) {
      System.err.println("exception: " + e);
      e.printStackTrace();
      return;
    }
  }
}

