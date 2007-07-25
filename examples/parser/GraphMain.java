package parser;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import parser.term.TermTree;
import parser.term.TermAdaptor;

public class GraphMain {

  public static void main(String[] args) {
    try {
      GraphLexer lexer = new GraphLexer(new ANTLRInputStream(System.in));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      GraphParser parser = new GraphParser(tokens);
      parser.setTreeAdaptor(new TermAdaptor());
      // Parse the input expression
      TermTree b = (TermTree) parser.node().getTree();
      System.out.println("Result = " + b.getTerm());
    } catch (Exception e) {
      System.err.println("exception: " + e);
      e.printStackTrace();
      return;
    }
  }
}

