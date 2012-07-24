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
public class Not extends Formula {

  private Formula f;

  public Not(Formula f) {
    this.f = f;
  }

  @Override
  public String toString() {
    return "¬" + f.toString();
  }

  @Override
  protected boolean interprete(Map<Variable, Object> valuation) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
