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
public class Imply extends Formula {

  private Formula f1;
  private Formula f2;

  public Imply(Formula f1, Formula f2) {
    this.f1 = f1;
    this.f2 = f2;
  }

  @Override
  public String toString() {
    return f1.toString() + "⇒" + f2.toString();
  }

  @Override
  protected boolean interprete(Map<Variable, Object> valuation) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
