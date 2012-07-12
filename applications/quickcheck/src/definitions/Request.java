/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hubert
 */
public abstract class Request {

  private int counter;

  public Request(int initialValue) {
    counter = initialValue;
  }

  protected int getCounter() {
    return counter;
  }

  public Request copy() {
    try {
      return (Request) this.clone();
    } catch (CloneNotSupportedException ex) {
      Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
      throw new UnsupportedOperationException("clone method fails.");
    }
  }

  abstract HashSet<Hole> fillATerm(Hole aTerm);
}
