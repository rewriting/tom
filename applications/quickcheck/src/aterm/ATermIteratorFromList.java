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
  private List<ATerm> list;
  
  public ATermIteratorFromList(List<ATerm> list){
    this.ite = list.iterator();
    this.list = list;
  }

  @Override
  public boolean hasNext() {
    return ite.hasNext();
  }

  @Override
  public ATerm next() {
    return ite.next();
  }
  
  @Override
  public ATermIterator clone(){
    return new ATermIteratorFromList(list);
  }
  
}
