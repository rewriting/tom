/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

/**
 *
 * @author hubert
 */
public class Tom_Integer implements Typable {
  private Scope scope;
  
  public Tom_Integer(Scope scope){
    this.scope = scope;
    scope.addType(this);
  }
  
  @Override
  public boolean isRec() {
    return false;
  }

  @Override
  public int getDimention() {
    return 0;
  }

  @Override
  public boolean dependsOn(Typable t) {
    return false;
  }

  @Override
  public int dstToLeaf() {
    return 0;
  }

  @Override
  public boolean isDstToLeafDefined() {
    return true;
  }

  @Override
  public boolean updateDependances() {
    return false;
  }

  @Override
  public String getName() {
    return "integer";
  }

  @Override
  public Scope getScope() {
    return scope;
  }

  public Integer generate_final(Request request) {
    return (int) (Math.random()*request.getCounter());
  }

  @Override
  @Deprecated
  public Object makeLeaf(Request request) {
    return generate(request);
  }

  @Override
  public ATerm generate(Request request) {
    ATerm res = new ATerm(this);
    res.chooseConstructor();
    return res;
  }
}
