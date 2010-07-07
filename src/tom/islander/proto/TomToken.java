import org.antlr.runtime.CommonToken;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.Tree;

public class TomToken extends CommonToken {
  private Tree tree; // custom field
  public TomToken(CharStream input, int type, int channel, int start, int stop,
      Tree tree) {
    super(input, type, channel, start, stop);
    this.tree = tree;
  }

  public void setTree(Tree tree) {
    this.tree = tree;
  }

  public Tree getTree() {
    return this.tree;
  }
    
  public String toString() { return super.toString()+",tree="+tree; }
}
