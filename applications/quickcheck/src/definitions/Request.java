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

  private int getCounter() {
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

  protected void spreadBetweenHigherDim(HashSet<ATerm> listHigherDim) {
    int size = listHigherDim.size();
    int[] tabSizes = new int[size];
    for (int i = 0; i < tabSizes.length; i++) {
      tabSizes[i] = 0;
    }
    if (size == 0) {
      return;
    }
    int n = this.getCounter() - 1;
    while (n > 0) {
      int index = (int) (Math.random() * size);
      tabSizes[index]++;
      n--;
    }
    int i = 0;
    for (ATerm term : listHigherDim) {
      if (term.getDstToLeaf() < this.getCounter()) {
        term.setRequest(new MakeAllStrategy(tabSizes[i]));
      } else {
        term.setRequest(new MakeLeafStrategy(tabSizes[i]));
      }
      i++;
    }
  }

  abstract HashSet<ATerm> fillATerm(ATerm aTerm);
}
