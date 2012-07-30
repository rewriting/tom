/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.ATerm;
import aterm.ATermList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Prototype of tom code.
 * @author hubert
 */
public class Shrink {

  private static class ATermSameTypeIterator implements Iterator<ATerm> {

    private ATerm current;
    private Stack<ATermList> stack;
    private DomainInterpretation domain;

    private ATermSameTypeIterator(ATerm term, DomainInterpretation domain) {
      this.domain = domain;
    }

    @Override
    public boolean hasNext() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ATerm next() {
      if (current != null) {
        ATerm res = current;
        current = null;
        return res;
      } else if (hasNext()) {
        ATerm res = current;
        current = null;
        return res;
      } else {
        throw new NoSuchElementException();
      }
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  public static Iterator<ATerm> getSameTypeFields(ATerm term, DomainInterpretation domain) {
    return new ATermSameTypeIterator(term, domain);
  }
}
