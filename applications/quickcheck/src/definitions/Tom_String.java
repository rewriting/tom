/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import java.util.HashSet;

/**
 * Represents implementation of string with Typable formalism.
 *
 * @author hubert
 */

@Deprecated
class Tom_String implements Typable {

  private Scope scope;

  public Tom_String(Scope scope) {
    this.scope = scope;
  }

  @Override
  public int getDimension() {
    return 1;
  }

  @Override
  public int dstToLeaf() {
    return 0;
  }

  @Override
  public boolean updateDependences() {
    return false;
  }

  @Override
  public String getName() {
    return "string";
  }

  public Scope getScope() {
    return scope;
  }

//  public String generate_final(Request request) {
//    int taille = (int) (Math.random()*request.getCounter());
//    byte[] tabBytes = new byte[taille];
//    for (int i = 0; i < taille; i++) {
//      tabBytes[i] = (byte) (Math.random()*256);
//    }
//    return new String(tabBytes);
//  }
  @Override
  public Slot generate(int n) {
    Slot res = new Slot(this);
    res.chooseConstructor();
    return res;
  }

  @Override
  public HashSet<Typable> getDependences() {
    return new HashSet<Typable>();
  }
}
