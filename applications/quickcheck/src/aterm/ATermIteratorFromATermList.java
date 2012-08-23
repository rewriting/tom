/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aterm;

import java.util.NoSuchElementException;

/**
 *
 * @author hubert
 */
public class ATermIteratorFromATermList extends ATermIterator {

  private ATermList state;
  
  public ATermIteratorFromATermList(ATermList list){
    this.state = list;
  }

  @Override
  public boolean hasNext() {
    return !state.isEmpty();
  }

  @Override
  public ATerm next() {
    if(hasNext()) {
      ATerm res = state.getFirst();
      state = state.getNext();
      return res;
    } else {
      throw new NoSuchElementException();
    }
  }
}
