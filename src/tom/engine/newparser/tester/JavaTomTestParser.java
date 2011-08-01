package tom.engine.newparser.tester;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.Tree;

import tom.engine.newparser.parser.HostParser;

public class JavaTomTestParser extends TestParser{

  public JavaTomTestParser() {
    
  }
  
  @Override
  public void parse(String stringInput) {
    
    this.setSuccess(false);
    
    HostParser parser = new HostParser(null, null); //XXX
    CharStream input = new ANTLRStringStream(stringInput);
    Tree tree = parser.parseProgram(input);
    
    this.setSuccess(true);
    this.setTree(tree);
  }

}
