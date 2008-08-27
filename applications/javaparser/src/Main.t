
import java.io.*;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;

import parser.*;
import parser.ast.*;
import parser.ast.types.*;

public class Main {

  %include { sl.tom }
  %include { parser/ast/Ast.tom }

  %strategy Simplify () extends Identity() {
    visit Statement {
      If(Primary(BooleanLiteral(True()), SuffixList()), t, _) -> t
      If(Primary(BooleanLiteral(False()), SuffixList()), _, e) -> e
      For(StandardForControl(_, Primary(BooleanLiteral(False()), SuffixList()), _), _)
        -> EmptyStatement()
      While(Primary(BooleanLiteral(False()), SuffixList()), _)
        -> EmptyStatement()
    }
    visit Expression {
      ConditionalExpression(Primary(BooleanLiteral(True()), SuffixList()), t, _) -> t
      ConditionalExpression(Primary(BooleanLiteral(False()), SuffixList()), _, e) -> e
      UnaryOperation(LogicalNot(), Primary(BooleanLiteral(True()), SuffixList()))
        -> Primary(BooleanLiteral(False()), SuffixList())
      UnaryOperation(LogicalNot(), Primary(BooleanLiteral(False()), SuffixList()))
        -> Primary(BooleanLiteral(True()), SuffixList())
      Primary(ExpressionToPrimary(Primary(p, SuffixList(s1*))), SuffixList(s2*)) -> Primary(p, SuffixList(s1*, s2*))
    }
  }

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
      //System.out.println(term);

      // Term manipulation using the strategy defined above :
      //
      //CompilationUnit simplified = (CompilationUnit) `InnermostId(Simplify()).visit(term);
      //
      //tom.library.utils.Viewer.toTree(simplified);

    } catch (Exception e) {
      //System.err.println("exception: " + e);
      e.printStackTrace();
    }
  }
}
