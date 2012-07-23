/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.system;

/**
 *
 * @author hubert
 */
public class And extends Formula {

  private Formula f1;
  private Formula f2;

  public And(Formula f1, Formula f2) {
    this.f1 = f1;
    this.f2 = f2;
  }

  @Override
  public String toString() {
    return f1.toString() + "⋏" + f2.toString();
  }
}
