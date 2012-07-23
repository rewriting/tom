/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author hubert
 */
public class Exists extends Formula {

  private Formula f;
  private Variable x;

  public Exists(Formula f, Variable x) {
    this.f = f;
    this.x = x;
  }

  @Override
  public String toString() {
    return "∃" + x + f;
  }
}
