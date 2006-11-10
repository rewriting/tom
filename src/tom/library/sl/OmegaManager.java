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

public class OmegaManager { 

/**
   * create s=omega(v)
   * such that s[subject] returns subject[ s[subject|omega] ]|omega
   *
   * @param v strategy subterm of the omega strategy
   * @return the omega strategy corresponding to the position
   */
  public static Strategy getOmega(int[] omega, Strategy v) {
    Strategy res = v;
    for(int i = omega.length-1 ; i>=0 ; i--) {
      System.out.println("omega"+i);
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
  public static Strategy getOmegaPath(int[] omega, Strategy v) {
    return getOmegaPathAux(omega, v,1);
  }

  private static Strategy getOmegaPathAux(int[] omega, Strategy v, int i) {
    if(i >= omega.length-1) {
      return v;
    } else {
      return new Sequence(new Omega(omega[i],getOmegaPathAux(omega,v,i+1)),v);
    }
  }

  /**
   * create s=omega(x->t)
   * such that s[subject] returns subject[t]|omega
   *
   * @param t the constant term that should replace the subterm
   * @return the omega strategy the performs the replacement
   */
  public static Strategy getReplace(int[] omega, final Visitable t) {
    return getOmega(omega, new Identity() {public jjtraveler.Visitable visit(jjtraveler.Visitable x){ return t;}});
  }

  /**
   * create s=x->t|omega
   * such that s[subject] returns subject|omega
   *
   * @return the omega strategy that retrieves the corresponding subterm
   */
  public static Strategy getSubterm(final int[] omega) {
    return new AbstractStrategy() {
      { initSubterm(); } 
      public jjtraveler.Visitable visit(jjtraveler.Visitable subject) throws jjtraveler.VisitFailure {
        final jjtraveler.Visitable[] ref = new jjtraveler.Visitable[1];
        getOmega(omega, new Identity() { public void visit() { ref[0]=getSubject(); } }).visit(subject);
        return ref[0];
      }
      public void visit() {
        final Visitable[] ref = new Visitable[1];
        Strategy s =getOmega(omega, new Identity() { public void visit() { ref[0]=getSubject(); }}); 
        s.fire(getEnvironment().getRoot());
        environment.setSubject(ref[0]);
      }
    };
  }

  public static int[] getRelativeOmega(int[] source,int[] target) {
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
    return relative;
  }

  public static int[] getAbsoluteOmega(int[] current,int[] relative) {
    int prefix = current.length-relative[0];
    int absoluteLength = prefix+relative.length-1;
    int[] absolute = new int[absoluteLength];
    for(int i=0 ; i<prefix ; i++) {
      absolute[i]=current[i];
    }
    for(int i=prefix ; i<absoluteLength ; i++){
      absolute[i]=relative[i-prefix+1];
    }
    return absolute;
  }
    }


