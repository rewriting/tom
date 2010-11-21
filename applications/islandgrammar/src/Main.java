import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.ANTLRInputStream;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;

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

    CharStream charStream = new ANTLRInputStream(input);
    parsematchLexer lexer = new parsematchLexer(charStream);
    CommonTokenStream tokens = new CommonTokenStream();
    tokens.setTokenSource(lexer);
    parsematchParser parser = new parsematchParser(tokens);
    
// And now what are we supposed to do with that parser ?

    } catch (Exception e) {
      //System.err.println("exception: " + e);
      e.printStackTrace();
    }

  }
}
