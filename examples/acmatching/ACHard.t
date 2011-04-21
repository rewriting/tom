/*
 * Copyright (c) 2004-2008, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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
 */
package acmatching;

import acmatching.peano.types.*;

public class ACHard {

  %include{ peano/Peano.tom }  
  %include{ int.tom }
  %include{ intarray.tom }

  private static int nbCall=0;
  private Nat eval(Nat t) {
    nbCall++;
    //System.out.println("t = " + t);
    %match(t) {
      f??(T1*,X*,g(Y)) -> {
        //System.out.println("T1=" + `T1 + " X=" + `X);
        //System.out.println("eval(X) = " + eval(`X));
        //System.out.println("eval(Y) = " + eval(`Y));
        if(eval(`X)==eval(`Y)) {
          return `b();
        }
      }
    }
    return t;
  }

  public final static void main(String[] args) {
    ACHard problem = new ACHard();
    Nat t = `f(a(),a(),a(),a(),a(),a(), a(),a(),a(),a(),a(),a(),
               g(a()),g(a()),g(a()),g(a()),g(a()),g(a()),g(a()),g(a()),g(a()),g(a()) ); //,g(a()),g(a())); 
    Nat res = problem.eval(t);
    System.out.println("res = " + res + " in " + nbCall + " calls");

  } 
}   

