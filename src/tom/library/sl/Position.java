/*
 *
 * Copyright (c) 2000-2008, INRIA
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

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Object that represents a position in a term
 */

public class Position implements Cloneable,Path {

  private int[] omega;

  private static Position ROOT = null;

  /* construct the root position */
  private Position() {}

  public static Position make() {
    if(ROOT == null) {
      int[] array = new int[0];
      ROOT = makeFromArrayWithoutCopy(array);
    }
    return ROOT;
  }

  private static Position makeFromArrayWithoutCopy(int[] array) {
    Position res = new Position();
    res.omega=array;
    return res;
  }

  public static Position makeFromSubarray(int[] src, int srcIndex, int length) {
    int[] array = new int[length];
    System.arraycopy(src, srcIndex, array, 0, length);
    return makeFromArrayWithoutCopy(array);
  }

  public static Position makeFromArray(int[] array) {
    return makeFromSubarray(array,0,array.length);
  }

  public static Position makeFromPath(Path p) {
    if(p instanceof Position) {
      // efficient implementation
      Position pos = (Position) p;
      return makeFromArray(pos.omega);
    }

    Path pp = p.getCanonicalPath();
    int size = pp.length();
    int[] array = new int[size];
    for(int i=0;i<size;i++) {
      array[i]= pp.getHead();
      pp = pp.getTail();
    }
    return makeFromArrayWithoutCopy(array);
  }
  
  public int[] toArray() {
    int[] array = new int[length()];
    System.arraycopy(omega, 0, array, 0, length());
    return array;
  }

  public Object clone() {
    Position clone = null;
    try {
      clone = (Position) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException("Position cloning error");
    }
    clone.omega = new int[length()];
    System.arraycopy(omega, 0, clone.omega, 0, length());
    return clone;
  }

  public int hashCode() {
    int hashCode = 0;
    for(int i = 0; i < length(); i++) {
      hashCode = hashCode * 31 + omega[i];
    }
    return hashCode;
  }

  /**
   * Tests if two positions are equals
   */
  public boolean equals(Object o) {
    if (o instanceof Position) {
      Position p1  = (Position) this.getCanonicalPath();
      Position p2  = (Position) ((Position)o).getCanonicalPath();
      /* we need to check only the meaningful part of the omega array */
      if (p1.length()==p2.length()) {
        for (int i=0; i<p1.length(); i++) {
          if (p1.omega[i]!=p2.omega[i]) {
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }

  /**
   * Compares two positions
   */
  public int compare(Path path) {
    Position p = Position.makeFromPath(path);
    /* we need to check only the meaningful part of the omega array */
    for(int i=0; i<length(); i++) {
      if(i == p.length() || omega[i]>p.omega[i]) {
        return 1;
      }
      else{
        if ( omega[i]<p.omega[i]) {
          return -1;
        }
      }
    }
    return length()==p.length()?0:-1;
  }

  /**
   * Returns a <code>String</code> object representing the position.
   * The string representation consists of a list of elementary positions
   *
   * @return a string representation of this position
   */
  public String toString() {
    StringBuilder r = new StringBuilder("[");
    for(int i=0 ; i<length() ; i++) {
      r.append(omega[i]);
      if(i<length()-1) {
        r.append(", ");
      }
    }
    r.append("]");
    return r.toString();
  }

  public Path add(Path p) {
    if(p.length()>0) {
      Path result = this.conc(p.getHead());
      return result.add(p.getTail());
    } else {
      return (Path) clone();
    }
  }

  public Path sub(Path p) {
    return Position.makeFromPath(p).inverse().add(this);
  }

  public Path inverse() {
    int[] inverse = new int[length()];
    for(int i=0;i<length();i++) {
      inverse[length()-(i+1)]=-omega[i];
    }
    return makeFromArrayWithoutCopy(inverse);
  }

  /**
   * Get the length of the position in the tree
   * @return length of the path
   */
  public int length() {
    return omega.length;
  }

  public int getHead() {
    return omega[0];
  }

  public Path getTail() {
    if(length()==0) {
      return null;
    }
    return Position.makeFromSubarray(omega,1,length()-1);
  }

  public Path conc(int i) {
    int[] result = new int[length()+1];
    System.arraycopy(omega,0,result,0,length());
    result[length()]=i;
    return makeFromArrayWithoutCopy(result);
  }

  public Position up() {
    return Position.makeFromSubarray(omega,0,length()-1);
  }

  public Position down(int i) {
    int[] array = new int[length()+1];
    System.arraycopy(omega,0,array,0,length());
    array[length()]=i;
    return makeFromArrayWithoutCopy(array);
  }

  public Path getCanonicalPath() {
    if(length()==0) {
      return make();
    }
    //System.out.println("omega = " + Arrays.toString(omega));
    int[] array = new int[length()];
    int j=0;
    for(int i=0 ; i<length() ; i++) {
      if(omega[i]!=0) {
        if(i<length()-1) {
          if(omega[i]!= -omega[i+1]) {
            array[j++] = omega[i];
          } else {
            i++;
          }
        } else {
          array[j++] = omega[i];
        }
      }
    }
    Position pos = makeFromSubarray(array,0,j);

    // we check here that the result is in normal form
    for(int i=0 ; i<j ; i++) {
      if(i<j-1) {
        if(omega[i] == -omega[i+1]) {
          //System.out.println("array = " + Arrays.toString(array));
          //System.out.println("j = " + j);
          return pos.getCanonicalPath();
        }
      }
    }
    return pos;
  }

  public boolean hasPrefix(Position prefix) {
    int[] prefixTab = prefix.omega;
    if(length()<prefixTab.length) {
      return false;
    }
    for(int i=0;i<prefixTab.length;i++) {
      if(prefixTab[i]!=omega[i]) {
        return false;
      }
    }
    return true;
  }

  public Position changePrefix(Position oldprefix,Position prefix) {
    if(!hasPrefix(oldprefix)) {
      return null;
    }

    int delta = oldprefix.length();
    int[] array = new int[this.length()-delta + prefix.length()];
    for(int i=0;i<prefix.length();i++) {
      array[i]=prefix.omega[i];
    }
    for(int i=0 ; delta+i<this.length() ; i++) {
      array[i+prefix.length()]=this.omega[delta+i];
    }
    return makeFromArrayWithoutCopy(array);
  }

  /**
   * create s=omega(v)
   * such that s[subject] returns subject[ s[subject|omega] ]|omega
   *
   * @param v strategy subterm of the omega strategy
   * @return the omega strategy corresponding to the position
   */
  public Strategy getOmega(Strategy v) {
    Strategy res = v;
    for(int i = length()-1 ; i>=0 ; i--) {
      res = new Omega(omega[i],res);
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
  public Strategy getOmegaPath(Strategy v) {
    return getOmegaPathAux(v,0);
  }

  private Strategy getOmegaPathAux(Strategy v, int i) {
    if(i >= length()-1) {
      return v;
    } else {
      return new Sequence(new Omega(omega[i],getOmegaPathAux(v,i+1)),v);
    }
  }

  /**
   * create s=omega(x->t)
   * such that s[subject] returns subject[t]|omega
   *
   * @param t the constant term that should replace the subterm
   * @return the omega strategy the performs the replacement
   */
  public Strategy getReplace(final Object t) {
    return getOmega(
        new Identity() {
          public int visit(Introspector i) {
            environment.setSubject(t);
            return Environment.SUCCESS;
          }
          public Object visitLight(Object x, Introspector i) {
            return t;
          }
        });
  }

  /**
   * create s=x->t|omega
   * such that s[subject] returns subject|omega
   *
   * @return the omega strategy that retrieves the corresponding subterm
   */
  public Strategy getSubterm() {
    return new AbstractStrategy() {
      { initSubterm(); }
      public Object visitLight(Object subject, Introspector i) throws VisitFailure {
        final Object[] ref = new Object[1];
        getOmega(
            new Identity() {
              public int visit(Introspector i) {
                ref[0]=environment.getSubject();
                return Environment.SUCCESS;
              }
              public Object visitLight(Object v, Introspector i) {
                ref[0] = v;
                return v;
              }
            }).visitLight(subject,i);
        return ref[0];
      }
      public int visit(Introspector i) {
        final Object[] ref = new Object[1];
        Strategy s=getOmega(
            new Identity() {
              public int visit(Introspector i) {
                ref[0]=environment.getSubject();
                return Environment.SUCCESS;
              }
              public Object visitLight(Object v, Introspector i) {
                ref[0] = v;
                return v;
              }
            });
        try {
          s.visit(environment.getRoot(),i);
          environment.setSubject(ref[0]);
          return Environment.SUCCESS;
        } catch(VisitFailure e) {
          return Environment.FAILURE;
        }
      }
    };
  }

}
