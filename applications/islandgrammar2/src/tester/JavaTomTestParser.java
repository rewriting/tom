package tester;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.Tree;

import newparser.HostParser;

public class JavaTomTestParser extends TestParser{

  public JavaTomTestParser() {
    
  }
  
  @Override
  public void parse(String stringInput) {
    
    this.setSuccess(false);
    
    HostParser parser = new HostParser();
    CharStream input = new ANTLRStringStream(stringInput);
    Tree tree = parser.parse(input);
    
    this.setSuccess(true);
    this.setTree(tree);
  }

}
