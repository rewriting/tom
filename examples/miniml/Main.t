package miniml;

import miniml.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class Main {

  %include { lambda/Lambda.tom }

  public static void main(String[] args) {
    try{
      LambdaLexer lexer = new LambdaLexer(new ANTLRInputStream(System.in));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      LambdaParser parser = new LambdaParser(tokens);
      LTerm t = parser.lterm().convert();
      RawTyped raw_typed_t = Typer.typeOf(t).export();
      System.err.println(Printer.pretty(raw_typed_t.getty()));
      System.out.println(Compiler.compile(raw_typed_t));
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
