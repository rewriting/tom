/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.system;

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
}
