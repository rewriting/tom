/*
 *
 * Copyright (c) 2000-2006, Pierre-Etienne Moreau
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 **/
package tom.library.strategy.mutraveler;

import tom.library.strategy.mutraveler.AbstractMuStrategy;
import jjtraveler.Visitable;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;
import java.util.*;

/**
 * Object that represents a position in a term
 */

public class Position {
  private static final int DEFAULT_LENGTH = 8;
  protected int size;
  protected int[] data;

  public Position() {
    this(DEFAULT_LENGTH);
  }

  private Position(int length) {
    data = new int[length];
    size = 0;
  }

  public Position(int[] data) {
    size = data.length;
    this.data = new int[size];
    System.arraycopy(data, 0, this.data, 0, size);
  }

  private void ensureLength(int minLength) {
    int current = data.length;
    if (minLength > current) {
      int[] newData = new int[Math.max(current * 2, minLength)];
      System.arraycopy(data, 0, newData, 0, size);
      data = newData;
    }
  }

  public int[] toArray(){
    int[] copy  = new int[size];
    System.arraycopy(data, 0, copy, 0, size);
    return copy;
  }

  public void init(int[] data) {
    size = data.length;
    this.data = new int[size];
    System.arraycopy(data, 0, this.data, 0, size);
  }

  public void init(Position pos) {
    size = pos.data.length;
    data = new int[size];
    System.arraycopy(pos.data, 0, data, 0, size);
  }

  public Object clone() {
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
      /* we need to check only the meaningful part of the data array */
      if (size==p.size) {
        for(int i=0; i<size; i++) {
          if (data[i]!=p.data[i]) {
            return false;
          }
        }
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * Compares two positions
   */
  public int compare(Object o) {
    if (o instanceof Position) {
      Position p = (Position)o;
      /* we need to check only the meaningful part of the data array */
      if (size==p.size) {
        for(int i=0; i<size; i++) {
          if (data[i]<p.data[i]) {
            return -1;
          }
          else{
            if(data[i]>p.data[i]) {
              return 1;
            }
          }
        }
        return 0;
      } else {
        return size<p.size?-1:1;
      }
    } else {
      return -2;
    }
  }


  public int hashCode() {
    /* Hash only the interesting part of the array */
    int[] hashedData = new int[size];
    System.arraycopy(data,0,hashedData,0,size);
    return size * hashedData.hashCode();
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
   * create s=omegaPath(v)
   * such that s[subject] applies s to all nodes in the path of omega
   * in a bottom-up way
   *
   * @param v strategy subterm of the omega strategy
   * @return the omegaPath strategy corresponding to the position
   */
  public VisitableVisitor getOmegaPath(VisitableVisitor v) {
    return getOmegaPathAux(v,0);
  }

  private VisitableVisitor getOmegaPathAux(VisitableVisitor v, int i) {
    if(i >= size-1) {
      return v;
    } else {
     return new Sequence(new Omega(data[i],getOmegaPathAux(v,i+1)),v);
    }
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
  public MuStrategy getSubterm() {
   return new AbstractMuStrategy() {
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
