/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author hubert
 */
public class Variable extends Term {

  private String name;

  public Variable(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
