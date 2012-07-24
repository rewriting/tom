/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.system;

import java.util.Map;

/**
 *
 * @author hubert
 */
public class Forall extends Formula {

  private Formula f;
  private Variable x;

  public Forall(Formula f, Variable x) {
    this.f = f;
    this.x = x;
  }

  @Override
  public String toString() {
    return "∀" + x + f;
  }

  @Override
  protected boolean interprete(Map<Variable, Object> valuation) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
