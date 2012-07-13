/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import java.util.HashSet;

/**
 *
 * @author hubert
 */
public class Tom_Integer implements Typable {

  private Scope scope;

  public Tom_Integer(Scope scope) {
    this.scope = scope;
    scope.addType(this);
  }

  @Override
  public int getDimension() {
    return 0;
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
    return "integer";
  }

  public Scope getScope() {
    return scope;
  }

//  public Integer generate_final(Request request) {
//    return (int) (Math.random() * request.getCounter());
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
