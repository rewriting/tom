package lazyml;

import lazyml.lambda.types.*;
import org.antlr.runtime.*;
import java.util.*;

public class Main {

  %include { lambda/lambda.tom }

  public static final Context prelude = 
    `Context(
        RangeOf("Unit",Range(Ra(BVarList(),Domain(),Atom("Unit")))),
        RangeOf("True",Range(Ra(BVarList(),Domain(),Atom("Bool")))),
        RangeOf("False",Range(Ra(BVarList(),Domain(),Atom("Bool")))));

  public static void main(String[] args) {
    boolean lazy = false;
    if(args.length > 0)
      lazy = args[0].equals("--lazy");
    try{
      LambdaLexer lexer = new LambdaLexer(new ANTLRInputStream(System.in));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      LambdaParser parser = new LambdaParser(tokens);
      LTerm t = null;
      RawContext rctx = parser.tydecs();
      Context ctx = rctx.convert();
      ctx = `Context(prelude*,ctx*);
      RawLTerm rt = parser.lterm();
      LTerm crt = rt.convert();
      crt = PreProc.unfoldCases(crt);
      //crt = PreProc.deforest(crt);
      //System.err.println("after deforestation:\n" + Printer.pretty(crt.export()));
      TypeOfResult res = Typer.`typeOf(ctx,crt);
      %match(res) {
        Pair(ft,ty) -> {
          System.err.println(Printer.`pretty(ty.export()));
          //System.err.println(Printer.`pretty(ft.export()));
          System.out.println(lazy ? LazyCompiler.`compile(ft.export()) : Compiler.`compile(ft.export()));
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
