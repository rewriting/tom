package tom.library.strategy.mutraveler;

import tom.library.strategy.mutraveler.reflective.AbstractVisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;
import java.util.*;

/**
 * Object that represents a position in a term
 */

public class Position {
  private static final int DEFAULT_LENGTH = 8;
  private int size;
  private int[] data;

  public Position() {
    this(DEFAULT_LENGTH);
  }

  private Position(int length) {
    data = new int[length];
    size = 0;
  }

  private void ensureLength(int minLength) {
    int current = data.length;
    if (minLength > current) {
      int[] newData = new int[Math.max(current * 2, minLength)];
      System.arraycopy(data, 0, newData, 0, size);
      data = newData;
    }
  }

  protected Object clone() {
    Position clone = new Position(data.length);
    clone.size = size;
    System.arraycopy(data, 0, clone.data, 0, size);
    return clone;
  }

  /**
    * check if the position is empty
    *
    * @return true when the position is empty
    */
  protected boolean isEmpty() {
    return size == 0;
  }

  /**
   * Tests if two positions are equals
   */
  public boolean equals(Object o) {
    if (o instanceof Position) {
      Position p = (Position)o;
      return size==p.size && Arrays.equals(data,p.data);
    } else {
      return false;
    }
  }

  /**
   * Tests is prefix
   */
  public boolean isPrefix(Position p) {
    if(p.size < size) {
      return false;
    }
    for(int i=0 ; i<size ; i++) {
      if(data[i] != p.data[i]) {
        return false;
      }
    }
    return true;
  }

  /**
   * get the current sub-position
   * @return the current sub-position
   */
  protected int getSubPosition() {
    return data[size];
  }

  /**
   * Get the depth of the position in the tree
   * @return depth on the position
   */
  public int depth() {
    return size;
  }

  /**
   * remove the last sub-position
   * Up and down are made public to allow to write compiled strategies by hand
   * This may be a BAD idea
   */
  public void up() {
    size--;
  }

  /**
   * add a sub-position n
   *
   * @param n sub-position number. 1 is the first possible sub-position
   */
  public void down(int n) {
    if(n>0) {
      if (size == data.length) {
        ensureLength(size+1);
      }
      data[size++] = n;
    }
  }

  /**
   * create s=omega(v)
   * such that s[subject] returns subject[ s[subject|omega] ]|omega
   *
   * @param v strategy subterm of the omega strategy
   * @return the omega strategy corresponding to the position
   */
  public VisitableVisitor getOmega(VisitableVisitor v) {
    VisitableVisitor res = v;
    for(int i = size-1 ; i>=0 ; i--) {
     res = new Omega(data[i],res);
    }
    return res;
  }

  /**
   * create s=omega(x->t)
   * such that s[subject] returns subject[t]|omega
   *
   * @param t the constant term that should replace the subterm
   * @return the omega strategy the performs the replacement
   */
  public VisitableVisitor getReplace(final Visitable t) {
   return this.getOmega(new Identity() { public Visitable visit(Visitable x) { return t; }});
  }

  /**
   * create s=x->t|omega
   * such that s[subject] returns subject|omega
   *
   * @return the omega strategy that retrieves the corresponding subterm
   */
  public VisitableVisitor getSubterm() {
   return new AbstractVisitableVisitor() {
     { initSubterm(); }
     public Visitable visit(Visitable subject) throws VisitFailure {
       final Visitable[] ref = new Visitable[1];
       getOmega(new Identity() { public Visitable visit(Visitable x) { ref[0]=x; return x; }}).visit(subject);
       return ref[0];
     }
   };
  }

  /**
    * Returns a <code>String</code> object representing the position.
    * The string representation consists of a list of elementary positions
    *
    * @return a string representation of this position
    */
  public String toString() {
    StringBuffer r = new StringBuffer("[");
    for(int i=0 ; i<size ; i++) {
      r.append(data[i]);
      if(i<size-1) {
        r.append(", ");
      }
    }
    r.append("]");
    return r.toString();
  }
}
