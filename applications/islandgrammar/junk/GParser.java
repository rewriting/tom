import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.RuleReturnScope;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import java.io.FileInputStream;

public class GParser {

  private MyminiTomParser.program_return truc;

  public GParser(CharStream input) {
    try {
      MyminiTomLexer lexer = new MyminiTomLexer(input);
      CommonTokenStream tokens = new CommonTokenStream();
      tokens.setTokenSource(lexer);
      MyminiTomParser parser = new MyminiTomParser(tokens);
      truc = parser.program();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void print() {
    System.out.println(((Tree)truc.getTree()).toStringTree());
  }

} 

    
