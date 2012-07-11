/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

/**
 *
 * @author hubert
 */
public class Tom_String implements Typable {
  
  private Scope scope;
  
  public Tom_String(Scope scope){
    this.scope = scope;
  }

  @Override
  public boolean isRec() {
    return false;
  }

  @Override
  public int getDimention() {
    return 1;
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
    return "string";
  }

  @Override
  public Scope getScope() {
    return scope;
  }

  public String generate_final(Request request) {
    int taille = (int) (Math.random()*request.getCounter());
    byte[] tabBytes = new byte[taille];
    for (int i = 0; i < taille; i++) {
      tabBytes[i] = (byte) (Math.random()*256);
    }
    return new String(tabBytes);
  }
  
  @Override
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
