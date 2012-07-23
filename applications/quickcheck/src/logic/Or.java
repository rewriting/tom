/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author hubert
 */
public class Or extends Formula{

  private Formula f1;
  private Formula f2;

  public Or(Formula f1, Formula f2) {
    this.f1 = f1;
    this.f2 = f2;
  }

  @Override
  public String toString() {
    return f1.toString() + "⋎" + f2.toString();
  }
}
