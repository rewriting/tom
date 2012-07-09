/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import tom.library.sl.Strategy;

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
  public Strategy makeGenerator(Request request) {
    throw new UnsupportedOperationException("Not supported yet.");
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
}
