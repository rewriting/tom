package tom.engine.parserv3.tester;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.Tree;

import tom.engine.parserv3.parser.HostParser;

public class JavaTomTestParser extends TestParser{

  public JavaTomTestParser() {
    
  }
  
  @Override
  public void parse(String stringInput) {
    
    this.setSuccess(false);
    
    HostParser parser = new HostParser();
    CharStream input = new ANTLRStringStream(stringInput);
    Tree tree = parser.parseProgram(input);
    
    this.setSuccess(true);
    this.setTree(tree);
  }

}
