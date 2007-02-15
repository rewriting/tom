/*
 * Copyright (c) 2004-2007, INRIA
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

package zenon;

import aterm.*;
import aterm.pure.*;
import zenon.gxb.abg.*;
import zenon.gxb.abg.types.*;

import java.io.*;

class Gxb {
  %gom {
    module Abg  
    abstract syntax
    Hop = a()
        | b()
        | f(argf:Hop)
        | h(argg:Hop)
        | g(left:Hop,right:Hop)
  }

  public static void main(String[] args) {
    Gxb essai = new Gxb();
    essai.test();
  }

  void test() {
    Hop test = `g(a(),b());
    Hop test2 = `g(a(),b());
    Hop test3 = `g(a(),b());
    %match(Hop test) {
      g(a(),b()) -> { System.out.println("un a et un b"); }
      g(x,b()) -> { System.out.println(`x); }
      /*g[left=x] -> { System.out.println(`x); }*/
    }
    /*
    %match(Hop test2, Hop test3) {
      g(a(),b()),f(x) -> { System.out.println("un a et un b"); }
      g(x,b()),f(f(x)) -> { 
        
        %match(Hop x) {
          f(f(f(y))) -> { System.out.println(`y); }
        }
        
        System.out.println(`x); }
      g(a(),x),f(f(x)) -> { System.out.println(`x); }
    }
    */
    /*
    %match(Hop test2) {
      f(f(a())) -> { System.out.println("1"); }
      f(h(x))   -> { System.out.println("2"); }
      f(f(b())) -> { System.out.println("3"); }
    }
    */
    /*
    %match(Hop test2) {
      f(a) -> { System.out.println(`a()); }
    }
    %match(Hop test2) {
      f(x) -> { System.out.println(`x); }
    }
    Hop essai = `f(f(f(a())));
    %match(Hop essai) {
      f(f(f(x))) -> { System.out.println(`x); }
    }
    Hop essai2 = `g(f(f(a)),b);
    %match(Hop essai2) {
      g(x,b()) -> { System.out.println(`x); }
    }
    %match(Hop essai2) {
      g(f(f(x)),y) -> { System.out.println(`x + " " + `y); }
    }
    */
  }
}
