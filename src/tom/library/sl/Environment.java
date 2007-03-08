/*
 *
 * Copyright (c) 2000-2007, Pierre-Etienne Moreau
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
package tom.library.sl;
import java.util.Arrays;

/**
 * Object that represents an environment of a strategy
 * the position where the strategy is applied
 * a pointer to subterm
 * the root is stored in the first cell
 */

public class Environment implements Cloneable {
  private static final int DEFAULT_LENGTH = 8;
  public static final int SUCCESS = 0;
  public static final int FAILURE = 1;
  public static final int IDENTITY = 2;
  /*
   * number of elements in the arrays
   * the current position is int omega[size-1]
   * */
  protected int size;
  protected int[] omega;
  protected Visitable[] subterm;
  protected int status = 0;

  public Environment() {
    this(DEFAULT_LENGTH);
  }

  private Environment(int length) {
    omega = new int[length+1];
    subterm = new Visitable[length+1];
    size = 1; // root is in subterm[0]
    omega[0]=0; // the first cell is not used
  }

  private void ensureLength(int minLength) {
    int current = omega.length;
    if (minLength > current) {
      int max = Math.max(current * 2, minLength);
      int[] newOmega = new int[max];
      Visitable[] newSubterm = new Visitable[max];
      System.arraycopy(omega, 0, newOmega, 0, size);
      System.arraycopy(subterm, 0, newSubterm, 0, size);
      omega = newOmega;
      subterm = newSubterm;
    }
  }

  public Object clone() throws CloneNotSupportedException {
    Environment clone = (Environment) super.clone();
    clone.omega = new int[omega.length];
    clone.subterm = new Visitable[omega.length];
    System.arraycopy(omega, 0, clone.omega, 0, size);
    System.arraycopy(subterm, 0, clone.subterm, 0, size);
    clone.size = size;
    return clone;
  }

  /**
   * Tests if two environments are equals
   */
  public boolean equals(Object o) {
    if (o instanceof Environment) {
      Environment p = (Environment)o;
      /* we need to check only the meaningful part of the omega and subterm arrays */
      if (size==p.size) {
        for(int i=0; i<size; i++) {
          if (omega[i]!=p.omega[i] || subterm[i]!=p.subterm[i]) {
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

  public int hashCode() {
    /* Hash only the interesting part of the array */
    int[] hashedOmega = new int[size];
    Visitable[] hashedSubterm = new Visitable[size];
    System.arraycopy(omega,0,hashedOmega,0,size);
    System.arraycopy(subterm,0,hashedSubterm,0,size);
    return size * Arrays.hashCode(hashedOmega) * Arrays.hashCode(hashedSubterm);
  }

  /**
   * Tests is prefix
   */
  public boolean isPrefix(Environment p) {
    if(p.size < size) {
      return false;
    }
    for(int i=0 ; i<size ; i++) {
      if(omega[i]!=p.omega[i] || subterm[i]!=p.subterm[i]) {
        return false;
      }
    }
    return true;
  }

  /**
   * get the current root
   * @return the current root
   */
  public Visitable getRoot() {
    return subterm[0];
  }

  /**
   * set the current root
   */
  public void setRoot(Visitable root) {
    this.subterm[0] = root;
  }

  /**
   * get the current status
   * @return the current status
   */
  public int getStatus() {
    return status;
  }

  /**
   * set the current status
   */
  public void setStatus(int status) {
    this.status = status;
  }

  /**
   * get the term that corresponds to the current position
   * @return the current term
   */
  public Visitable getSubject() {
    return subterm[size-1];
  }

  /**
   * set the current term
   */
  public void setSubject(Visitable root) {
    this.subterm[size-1] = root;
  }
  /**
   * get the current sub-position
   * @return the current sub-position
   */
  public int getSubOmega() {
    return omega[size-1];
  }

  /**
   * get the current position
   * @return the current position
   */
  public Position getPosition() {
    int[] reducedOmega = new int[depth()];
    System.arraycopy(omega,1,reducedOmega,0,depth());
    return Position.makeAbsolutePosition(reducedOmega);
  }



  /**
   * Get the depth of the position in the tree
   * @return depth on the position
   */
  public int depth() {
    return size-1;
  }

  /**
   * remove the last sub-position
   * Up and down are made public to allow to write compiled strategies by hand
   * This may be a BAD idea
   */
  public void up() {
    //System.out.println("before up: " + this);
    int childIndex = getSubOmega()-1;
    Visitable child = getSubject();
    size--;
    setSubject((Visitable)(getSubject().setChildAt(childIndex,child)));
    //System.out.println("after up: " + this);
  }

  /**
   * add a sub-position n
   *
   * @param n sub-position number. 1 is the first possible sub-position
   */
  public void down(int n) {
    //System.out.println("before down: " + this);
    if(n>0) {
      if (size == omega.length) {
        ensureLength(size+1);
      }
      omega[size] = n;
      subterm[size] = (Visitable) getSubject().getChildAt(n-1);
      size++;
    }
    //System.out.println("after down: " + this);
  }


  public void goTo(Position posRelative) {
    if(posRelative.isRelative()){
      int[] pos = posRelative.toArray();
      int pos_back = pos[0];
      int pos_length = pos.length;
      for(int i=0;i<pos_back;i++){
        up();
      }
      if(pos_length>1){
        for(int i=1;i<pos_length;i++){
          down(pos[i]);
        }
      }
    }
  }

  public void followRef() {
    if (getSubject() instanceof Reference){
      int[] pos= ((Reference) getSubject()).toArray();
      int pos_back = pos[0];
      int pos_length = pos.length;
      for(int i=0;i<pos_back;i++){
        up();
      }
      if(pos_length>1){
        for(int i=1;i<pos_length;i++){
          down(pos[i]);
          if (getSubject() instanceof Reference){
            followRef();
          }
        }
      }
    }
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
        r.append(omega[i]);
        if(i<size-1) {
          r.append(", ");
        }
      }
      r.append("]");

    r.append("\n[");

    for(int i=0 ; i<size ; i++) {
      r.append(subterm[i]);
      if(i<size-1) {
        r.append(", ");
      }
    }
    r.append("]");
    return r.toString();
  }
}
