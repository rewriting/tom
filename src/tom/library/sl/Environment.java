/*
 *
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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
 * 	- Neither the name of the Inria nor the names of its
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
import java.util.List;
import java.util.ArrayList;

/**
 * Object that represents an environment of a strategy
 * the position where the strategy is applied
 * a pointer to subterm
 * the root is stored in the first cell
 */

public final class Environment implements Cloneable {
  private static final int DEFAULT_LENGTH = 8;
  public static final int SUCCESS = 0;
  public static final int FAILURE = 1;
  public static final int IDENTITY = 2;
  /*
   * number of elements-1 in the arrays
   * the current position is int omega[current]
   * the current subterm is subterm[current]
   * */
  protected int current;
  protected int[] omega;
  protected Object[] subterm;
  protected Introspector introspector;
  protected int status = Environment.SUCCESS;

  public Environment() {
    this(DEFAULT_LENGTH,VisitableIntrospector.getInstance());
  }

  public Environment(Introspector introspector) {
    this(DEFAULT_LENGTH,introspector);
  }

  private Environment(int length, Introspector introspector) {
    omega = new int[length+1];
    subterm = new Object[length+1];
    current = 0; // root is in subterm[0]
    omega[0]=0; // the first cell is not used
    this.introspector = introspector;
  }

  private void ensureLength(int minLength) {
    if(minLength > omega.length) {
      int max = Math.max(omega.length * 2, minLength);
      int[] newOmega = new int[max];
      Object[] newSubterm = new Object[max];
      System.arraycopy(omega, 0, newOmega, 0, omega.length);
      System.arraycopy(subterm, 0, newSubterm, 0, omega.length);
      omega = newOmega;
      subterm = newSubterm;
    }
  }

  public Object clone() throws CloneNotSupportedException {
    Environment clone = (Environment) super.clone();
    clone.omega = new int[omega.length];
    clone.subterm = new Object[omega.length];
    System.arraycopy(omega, 0, clone.omega, 0, omega.length);
    System.arraycopy(subterm, 0, clone.subterm, 0, omega.length);
    clone.current = current;
    return clone;
  }

  /**
   * Tests if two environments are equals
   */
  public boolean equals(Object o) {
    if(o instanceof Environment) {
      Environment p = (Environment)o;
      /* we need to check only the meaningful part of the omega and subterm arrays */
      if(current==p.current) {
        for(int i=0; i<current+1; i++) {
          if(omega[i]!=p.omega[i] || subterm[i]!=p.subterm[i]) {
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
    int[] hashedOmega = new int[current+1];
    Object[] hashedSubterm = new Object[current+1];
    // TODO: remove arraycopy
    System.arraycopy(omega,0,hashedOmega,0,current+1);
    System.arraycopy(subterm,0,hashedSubterm,0,current+1);
    return (current+1) * Arrays.hashCode(hashedOmega) * Arrays.hashCode(hashedSubterm);
  }

  public int getStatus() { 
    return status; 
  } 

  public void setStatus(int s) {
    this.status = s; 
  }

  /**
   * get the current root
   * @return the current root
   */
  public Object getRoot() {
    return subterm[0];
  }

  /**
   * set the current root
   */
  public void setRoot(Object root) {
    this.subterm[0] = root;
  }

  /**
   * get the current stack
   */
  public List<Object> getCurrentStack() {
    List<Object> v = new ArrayList<Object>();
    for(int i=0;i<depth();i++) {
      v.add(subterm[i]);
    }
    return v;
  }

  /**
   * get the term that corresponds to the ancestor of current position
   * @return the ancestor of the current term
   */
  public Object getAncestor() {
    return subterm[current-1];
  }

  /**
   * get the term that corresponds to the current position
   * @return the current term
   */
  public Object getSubject() {
    return subterm[current];
  }

  /**
   * set the current term
   */
  public void setSubject(Object root) {
    //System.out.println("setsubject "+root);
    //System.out.println("in the env "+this);
    this.subterm[current] = root;
  }

  public Introspector getIntrospector() {
    return introspector;
  }

  public void setIntrospector(Introspector i) {
    introspector = i;
  }

  /**
   * get the current sub-position
   * @return the current sub-position
   */
  public int getSubOmega() {
    return omega[current];
  }

  /**
   * get the current position
   * @return the current position
   */
  public Position getPosition() {
    return Position.makeFromSubarray(omega,1,depth());
  }

  /**
   * Get the depth of the position in the tree
   * @return depth on the position
   */
  public int depth() {
    return current;
  }

  /**
   * remove the last sub-position
   * Up and down are made public to allow to write compiled strategies by hand
   * This may be a BAD idea
   */
  public void up() {
    //System.out.println("before up: " + this);
    int childIndex = omega[current]-1;
    Object child = subterm[current];
    current--;
    subterm[current] = introspector.setChildAt(subterm[current],childIndex,child);
    //System.out.println("after up: " + this);
  }

  /**
   * package private
   * remove the last sub-position but does not update the subject
   */
  public void upLocal() {
    current--;
  }

  /**
   * add a sub-position n
   *
   * @param n sub-position number. 1 is the first possible sub-position
   */
  public void down(int n) {
    //System.out.println("before down: " + this);
    if(n>0) {
      Object child = subterm[current];
      current++;
      if(current == omega.length) {
        ensureLength(current+1);
      }
      omega[current] = n;
      subterm[current] = introspector.getChildAt(child,n-1);
    }
    //System.out.println("after down: " + this);
  }

  public void goToPosition(Position p) {
    followPath(p.sub(getPosition()));
  }

  public void followPath(Path path) {
    genericFollowPath(path,false);
  }

  public void followPathLocal(Path path) {
    genericFollowPath(path,true);
  }

  private void genericFollowPath(Path path, boolean local) {
    int[] normalizedPathArray = path.getCanonicalPath().toIntArray();
    int length = normalizedPathArray.length;
    for(int i=0;i<length;i++) {
      if(normalizedPathArray[i]>0) {
        down(normalizedPathArray[i]);
        if(subterm[current] instanceof Path && i+1<length) {
          // we do not want to follow the last reference
          genericFollowPath((Path)subterm[current],local);
        }
      } else {
        //verify that getsubomega() = -normalizedPathArray[i]
        if(local) {
          upLocal();
        } else {
          up();
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
    StringBuilder r = new StringBuilder("[");
    for(int i=0 ; i<current+1 ; i++) {
      r.append(omega[i]);
      if(i<current) {
        r.append(", ");
      }
    }
    r.append("]");

    r.append("\n[");

    for(int i=0 ; i<current+1 ; i++) {
      r.append(subterm[i]);
      if(i<current) {
        r.append(", ");
      }
    }
    r.append("]");
    return r.toString();
  }
}
