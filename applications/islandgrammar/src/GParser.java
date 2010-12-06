import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.RuleReturnScope;
import org.antlr.runtime.tree.CommonTree;
import java.io.FileInputStream;

public class GParser {

  private RuleReturnScope scope;

  public GParser(FileInputStream input) {
    try {
      CharStream charStream = new ANTLRInputStream(input);
      miniTomLexer lexer = new miniTomLexer(charStream);
      CommonTokenStream tokens = new CommonTokenStream();
      tokens.setTokenSource(lexer);
      miniTomParser parser = new miniTomParser(tokens);
      scope = parser.program();
    } catch (Exception e) {
      e.printStackTrace();
    }
}
 
  public void print() {
    CommonTree tree=(CommonTree)scope.getTree();
    printTree(tree,1);
  }

  public static void printTree(CommonTree t, int indent) {
    if ( t != null ) {
      StringBuffer sb = new StringBuffer(indent);
      for ( int i = 0; i < indent; i++ )
        sb = sb.append("   ");
      for ( int i = 0; i < t.getChildCount(); i++ ) {
        System.out.println(sb.toString() + t.getChild(i).toString());
        printTree((CommonTree)t.getChild(i), indent+1);
      }
    }
  }
} 

    
