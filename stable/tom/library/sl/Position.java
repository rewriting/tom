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

package tom.library.sl;

/**
 * Object that represents a position in a term
 */

public class Position implements Cloneable {

  private boolean isRelative;
  private int[] omega;

  private Position(int[] omega, boolean isRelative) {
    this.isRelative = isRelative;
    this.omega = new int[omega.length];
    System.arraycopy(omega, 0, this.omega, 0, omega.length);
  }

  public static Position makeAbsolutePosition(int[] omega){
    return new Position(omega,false);
  }

  public static Position makeRelativePosition(int[] omega){
    return new Position(omega,true);
  }

  public Position getRelativePosition(Position targetPos) {
    int[] target = targetPos.toArray();
    int[] source = toArray();
    int min_length =Math.min(source.length,target.length);
    int commonPrefixLength=0;
    while(commonPrefixLength<min_length && source[commonPrefixLength]==target[commonPrefixLength]){
      commonPrefixLength++;
    }
    int[] relative = new int[target.length-commonPrefixLength+1];
    relative[0]=source.length-commonPrefixLength;
    for(int j=1;j<relative.length;j++){
      relative[j] = target[commonPrefixLength+j-1];
    }
    return Position.makeRelativePosition(relative);
  }

  public Position getAbsolutePosition(Position relativePos) {
    int[] relative = relativePos.toArray();
    int[] current = this.toArray();
    int prefix = depth()-relative[0];
    int absoluteLength = prefix+relative.length-1;
    int[] absolute = new int[absoluteLength];
    for(int i=0 ; i<prefix ; i++) {
      absolute[i]=current[i];
    }
    for(int i=prefix ; i<absoluteLength ; i++){
      absolute[i]=relative[i-prefix+1];
    }
    return Position.makeAbsolutePosition(absolute);
  }

  public int[] toArray(){
    int[] copy  = new int[depth()];
    System.arraycopy(omega, 0, copy, 0, depth());
    return copy;
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
    /* Hash only the interesting part of the array */
    int[] hashedData = new int[depth()];
    System.arraycopy(omega,0,hashedData,0,depth());
    return depth() * hashedData.hashCode();
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
      Position p = (Position)o;
      /* we need to check only the meaningful part of the omega array */
      if (depth()==p.depth() & isRelative()==p.isRelative()) {
        for(int i=0; i<depth(); i++) {
          if (omega[i]!=p.omega[i]) {
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
      /* we need to check only the meaningful part of the omega array */
      if (depth()==p.depth()) {
        for(int i=0; i<depth(); i++) {
          if (omega[i]<p.omega[i]) {
            return -1;
          }
          else{
            if(omega[i]>p.omega[i]) {
              return 1;
            }
          }
        }
        return 0;
      } else {
        return depth()<p.depth()?-1:1;
      }
    } else {
      return -2;
    }
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
  public Strategy getReplace(final Visitable t) {
    return getOmega(new Identity() {public jjtraveler.Visitable visit(jjtraveler.Visitable x){ return t;}});
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
      public jjtraveler.Visitable visit(jjtraveler.Visitable subject) throws jjtraveler.VisitFailure {
        final jjtraveler.Visitable[] ref = new jjtraveler.Visitable[1];
        getOmega(new Identity() { public void visit() { ref[0]=getSubject(); } }).visit(subject);
        return ref[0];
      }
      public void visit() {
        final Visitable[] ref = new Visitable[1];
        Strategy s =getOmega(new Identity() { public void visit() { ref[0]=getSubject(); }}); 
        s.fire(getEnvironment().getRoot());
        environment.setSubject(ref[0]);
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
    for(int i=0 ; i<depth() ; i++) {
      r.append(omega[i]);
      if(i<depth()-1) {
        r.append(", ");
      }
    }
    r.append("]");
    return r.toString();
  }

  public boolean isRelative(){
    return isRelative;
  }

}
