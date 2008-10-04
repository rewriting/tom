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

  /* construct the root position */
  public Position() {
    omega = new int[0];
  }

  public Position(int[] omega) {
    setValue(omega);
  }

  public Position(Position p) {
    setValue(p);
  }

  public Position(Position prefix, Position suffix) {
    int[] prefixArray = prefix.toArray();
    int[] suffixArray = suffix.toArray();
    omega = new int[suffixArray.length+prefixArray.length];
    for( int i=0;i<prefixArray.length;i++) {
      omega[i]=prefixArray[i];
    }
    for( int i=0;i<suffixArray.length;i++) {
      omega[i+prefixArray.length]=suffixArray[i];
    }
  }

  public int[] toArray() {
    int[] copy  = new int[depth()];
    System.arraycopy(omega, 0, copy, 0, depth());
    return copy;
  }

  public void setValue(int[] omega) {
    this.omega = new int[omega.length];
    System.arraycopy(omega, 0, this.omega, 0, omega.length);
  }

  public void setValue(Position p) {
    int size = p.length();
    omega = new int[size];
    for(int i=0;i<size;i++) {
      omega[i]= p.getHead();
      p = (Position) p.getTail();
    }
  }

  public Object clone() {
    Position clone = null;
    try {
      clone = (Position) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException("Position cloning error");
    }
    clone.omega = new int[omega.length];
    System.arraycopy(omega, 0, clone.omega, 0, depth());
    return clone;
  }

  public int hashCode() {
    int hashCode = 0;
    for(int i = 0; i < depth(); i++) {
      hashCode = hashCode * 31 + omega[i];
    }
    return hashCode;
  }

  /**
   * Get the depth of the position in the tree
   * @return depth on the position
   */
  public int depth() {
    return omega.length;
  }

  /**
   * Tests if two positions are equals
   */
  public boolean equals(Object o) {
    if (o instanceof Position) {
      Position p1  = (Position) this.getCanonicalPath();
      Position p2  = (Position) ((Position)o).getCanonicalPath();
      /* we need to check only the meaningful part of the omega array */
      if (p1.depth()==p2.depth()) {
        for (int i=0; i<p1.depth(); i++) {
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
    Position p = Position.make(path);
    /* we need to check only the meaningful part of the omega array */
    for(int i=0; i<depth(); i++) {
      if(i == p.depth() || omega[i]>p.omega[i]) {
        return 1;
      }
      else{
        if ( omega[i]<p.omega[i]) {
          return -1;
        }
      }
    }
    return depth()==p.depth()?0:-1;
  }

  /**
   * Returns a <code>String</code> object representing the position.
   * The string representation consists of a list of elementary positions
   *
   * @return a string representation of this position
   */
  public String toString() {
    StringBuilder r = new StringBuilder("[");
    for(int i=0 ; i<depth() ; i++) {
      r.append(omega[i]);
      if(i<depth()-1) {
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
    return (make(p).inverse()).add(this);
  }

  public Path inverse() {
    int[] inverse = new int[omega.length];
    for(int i=0;i<omega.length;i++) {
      inverse[omega.length-(i+1)]=-omega[i];
    }
    return new Position(inverse);
  }

  public static Position make(Path p) {
    Path pp = p.getCanonicalPath();
    int size = pp.length();
    int[] omega = new int[size];
    for(int i=0;i<size;i++) {
      omega[i]= pp.getHead();
      pp = pp.getTail();
    }
    return new Position(omega);
  }

  public int length() {
    return omega.length;
  }

  public int getHead() {
    return omega[0];
  }

  public Path getTail() {
    if (length()==0) {
      return null;
    }
    int[] tail = new int[omega.length-1];
    System.arraycopy(omega, 1, tail, 0, tail.length);
    return new Position(tail);
  }

  public Path conc(int i) {
    int[] result = new int[length()+1];
    System.arraycopy(omega,0,result,0,length());
    result[length()]=i;
    return new Position(result);
  }

  public Position up() {
    int [] result = new int[length()-1];
    System.arraycopy(omega,0,result,0,length()-1);
    return new Position(result);
  }

  public Position down(int i) {
    int[] result = new int[length()+1];
    System.arraycopy(omega,0,result,0,length());
    result[length()]=i;
    return new Position(result);
  }


  public Path getCanonicalPath() {
    if(length()==0) return (Path) clone();
    int[] normalizedTail = ((Position)(getTail().getCanonicalPath())).toArray();
    if (normalizedTail.length==0 || omega[0]!=-normalizedTail[0]) {
      int[] result = new int[1+normalizedTail.length];
      result[0]=omega[0];
      System.arraycopy(normalizedTail,0,result,1,normalizedTail.length);
      return new Position(result);
    }
    else {
      int[] result = new int[normalizedTail.length-1];
      System.arraycopy(normalizedTail,1,result,0,normalizedTail.length-1);
      return new Position(result);
    }
  }

  public boolean hasPrefix(Position prefix) {
    int[] prefixTab = prefix.toArray();
    if (omega.length<prefixTab.length) {
      return false;
    }
    for (int i=0;i<prefixTab.length;i++) {
      if(prefixTab[i]!=omega[i]) return false;
    }
    return true;
  }

  public Position getSuffix(Position prefix) {
    if(! hasPrefix(prefix)) return null;
    int[] suffixTab = new int[depth()-prefix.depth()];
    for (int i=0;i<suffixTab.length;i++) {
      suffixTab[i]=omega[i+prefix.depth()];
    }
    return new Position(suffixTab);
  }

  public Position changePrefix(Position oldprefix,Position newprefix) {
    if(! hasPrefix(oldprefix)) return null;
    Position suffix = getSuffix(oldprefix);
    return new Position(newprefix,suffix);
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
    for(int i = depth()-1 ; i>=0 ; i--) {
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
    if(i >= depth()-1) {
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
