/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aterm;

import aterm.pure.PureFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.model.ShrinkIterator;

/**
 *
 * @author hubert
 */
public abstract class ATermIterator implements Cloneable{

  abstract public boolean hasNext();

  abstract public ATerm next();
  
  @Override
  public ATermIterator clone(){
    try {
      return (ATermIterator) super.clone();
    } catch (CloneNotSupportedException ex) {
      Logger.getLogger(ATermIterator.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
  
  public boolean equals(ATermIterator other){
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
  
  @Override
  public String toString(){
    return ShrinkIterator.toATermList(this.clone(), new PureFactory()).toString();
  }

}
