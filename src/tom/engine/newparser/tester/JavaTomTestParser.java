package tom.engine.newparser.tester;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.Tree;

import tom.engine.newparser.parser.HostParser;

public class JavaTomTestParser extends TestParser{

  public JavaTomTestParser() {
    
  }
  
  @Override
  public void parse(String stringInput, String fileName) {
    
    this.setSuccess(false);
    
    HostParser parser = new HostParser(null, null); //XXX
    ANTLRStringStream input = new ANTLRStringStream(stringInput);
    input.name = fileName;

    Tree tree = parser.parseProgram(input);
    
    this.setSuccess(true);
    this.setTree(tree);
  }

}
