//import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;

public class TestAntlr {

  public static void main(String[] args) {
    try {
      InputStream input;

      if(args.length!=0) {
        File inputFile = new File(args[0]);
        input=new FileInputStream(inputFile);
      } else {
        input=System.in;
      }

      MyminiTomLexer lexer = new MyminiTomLexer(new ANTLRInputStream(input));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      MyminiTomParser parser = new MyminiTomParser(tokens);

//      System.out.println("(DEBUG) *** parser.program() ***");
      MyminiTomParser.program_return tmp = parser.program();
//      System.out.println("(DEBUG) *** before getTree() ***");
      Tree tree = (Tree) tmp.getTree();
      System.out.println("(DEBUG) TestAntr - tree =\n" + tree.toStringTree() + "\n");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

} //class
