package tom.engine.parserv3.tester;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;


public abstract class TestParser {

  enum Type{
    JAVATOM,
    TOMPATTERN,
    TOMCONSTRAINT;
  }
  
  public static TestParser getInstance(Type parserType){
    switch(parserType){
    case JAVATOM :
      return new JavaTomTestParser();
    case TOMPATTERN :
      return new PatternTestParser();
    case TOMCONSTRAINT :
      return new ConstraintTestParser();
    default :
      throw new RuntimeException("very stupid exception");
    }
  }
  
  private Tree tree = null;
  private boolean success = false;
  
  public abstract void parse(String input);

  public boolean isParsingASuccess(){
    return success;
  }
  
  /**
   * Precondition : call parse before this, and parsing is a success
   * @return
   * @throws IllegalStateException
   */
  public String getParsingResultAsString(){
    if(tree==null || success==false){
      throw new IllegalStateException();
    }else{
      return ((CommonTree)tree).toStringTree();
    }
  }
  
  /**
   * Pleas don't mess with that tree...
   * @return
   */
  public Tree getTree(){
    return tree;
  }
  
  protected void setTree(Tree tree){
    this.tree = tree;
  }
  
  protected void setSuccess(boolean success){
    this.success = success;
  }
}
