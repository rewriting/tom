import java.io.*;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;

import parser.*;
import parser.ast.*;
import parser.ast.types.*;

public class Main {

  %include { sl.tom }
  %include { parser/ast/Ast.tom }
/*
  %strategy Simplify () extends `Identity () {
    visit Statement {
      If(Primary(BooleanLiteral(True()), EmptySuffixList()), t, _) -> t
    }
  }
*/
  public static void main(String args[]) {
    try {
      InputStream input;

      if(args.length!=0) {
        File inputFile = new File(args[0]);
        input=new FileInputStream(inputFile);
      } else {
        input=System.in;
      }

      // parse tree
      JavaLexer lexer = new JavaLexer(new ANTLRInputStream(input));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      JavaParser parser = new JavaParser(tokens);
      parser.setTreeAdaptor(new AstAdaptor());

      AstTree tree = (AstTree) parser.compilationUnit().getTree();
      CompilationUnit term = (CompilationUnit) tree.getTerm();

      tom.library.utils.Viewer.toTree(term);

      //CompilationUnit simplified = `RepeatId(Simplify()).visit(term);

      //tom.library.utils.Viewer.toTree(simplified);

    } catch (Exception e) {
      //System.err.println("exception: " + e);
      e.printStackTrace();
    }
  }
}
