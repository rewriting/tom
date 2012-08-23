/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aterm;

import aterm.pure.PureFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.model.LazyShrink;

/**
 * This class makes it possible to iterate over an list of ATerm. This
 * implementation is different of Iterator's one because Iterator cannot
 * implements Clonable interface and thus can be used only once.
 *
 * @author hubert
 */
public abstract class ATermIterator implements Cloneable {

  /**
   * Returns true if the iteration has more elements. (In other words, returns
   * true if next would return an element rather than throwing an exception.)
   *
   * @return true if the iterator has more elements.
   */
  abstract public boolean hasNext();

  /**
   * Returns the next element in the iteration.
   *
   * @return the next element in the iteration.
   */
  abstract public ATerm next();

  /**
   * Clones an iterator.
   *
   * @return a new iterator initialised at the first element of the cloned
   * iterator
   */
  @Override
  public ATermIterator clone() {
    try {
      return (ATermIterator) super.clone();
    } catch (CloneNotSupportedException ex) {
      Logger.getLogger(ATermIterator.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Tells if to iterators are equal, that is, iterate overs the same éléments
   * in the same order.
   *
   * @param other iterator to be compared with.
   * @return true if the iterators are equals
   */
  public boolean equals(ATermIterator other) {
    ATermIterator c1 = this.clone();
    ATermIterator c2 = other.clone();
    while(c1.hasNext()) {
      if(!c2.hasNext()) {
        return false;
      }
      if(!c1.next().equals(c2.next())) {
        return false;
      }
    }
    if(c2.hasNext()) {
      return false;
    } else {
      return true;
    }
  }
  
  private ATermList toATermList(ATermIterator iterator, ATermFactory factory) {
    if(!iterator.hasNext()) {
      return factory.makeList();
    }
    ATerm term = iterator.next();
    return toATermList(iterator, factory).insert(term);
  }
  
  /**
   * Gives a representation of an iterator in ATermList.
   * @param factory factory used to build the ATermList.
   * @return ATermList representation.
   */
  public ATermList toATermList(ATermFactory factory) {
    return toATermList(this.clone(), factory);
  }

  /**
   * Gives a representation of an iterator in String.
   * @return String representation.
   */
  @Override
  public String toString() {
    return this.toATermList(new PureFactory()).toString();
//    return LazyShrink.toATermList(this.clone(), new PureFactory()).toString();
  }
}
