/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aterm;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author hubert
 */
public class ATermIteratorFromList extends ATermIterator{
  private final Iterator<ATerm> ite;
  
  public ATermIteratorFromList(List<ATerm> list){
    this.ite = list.iterator();
  }

  @Override
  public boolean hasNext() {
    return ite.hasNext();
  }

  @Override
  public ATerm next() {
    return ite.next();
  }
  
}
