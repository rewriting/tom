/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
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
 *  - Neither the name of the Inria nor the names of its
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
package typeinference;

import typeinference.examplejavatype.example.types.*;
public class ExampleJavaType {
  %gom {
    module Example
      abstract syntax
      B = b()
        | f(n:B)
        | g(n1:B,n2:B)

      A = h(m1:A,m2:B)
        | a()
  }

  /**
   * This example is to check the evaluation of the mix "tom + java"
  */
  public static void main(String[] args) {
    int x = 10;
    B y = `b();
    B w = `f(b());
    A s = `h(a(),b());
    %match{
      b() << x -> { System.out.println("Line 1: " + x); }
      b() << x -> { System.out.println("Line 2: " + `x); }
      b() << y -> { System.out.println("Line 3: " + `createF(y)); }
      b() << y -> { System.out.println("Line 4: " + createF(`y)); }
      f(a) << w -> { System.out.println("Line 5: " + createG(`a,y)); }
      z@f(b()) << createF(y) -> { System.out.println("Line 6: " + `z); }
      z@f(b()) << createF(b()) -> { System.out.println("Line 7: " + `z); }
      b() << y -> { System.out.println("Line 8: " + `f(createF(w))); }
      b() << y -> { System.out.println("Line 9: " + `f(createG(w,y))); }
      f(b()) << createF(y) -> { System.out.println("Line 10: " + `y); }
      //f(z) << w -> { 
      //  System.out.println("Line 11: " + `g(createF(z),f(createF(x)))); 
      //}
      f(z1) << f(b()) || g(z1,z2) << g(f(b()),b()) -> {
        System.out.println("Line 12: " + `z1);
      }
      f(z) << B s -> { System.out.println("Line 13: " + s); }
      z << B s -> { System.out.println("Line 14: " + s); }
      z << B s -> { System.out.println("Line 15: " + `z); }
      z1 << b() && (f(z1) << f(b()) || g(z1,z2) << g(f(b()),b())) -> {
        System.out.println("Line 16: " + `z1);
      }
    }
  }

  public static B createF(B arg) {
    System.out.println("In createF with '" + arg + "'.");
    return `f(arg);
  }

  public static B createG(B arg1, B arg2) {
    System.out.println("In createG with '" + arg1 + "' and '" + arg2 + "'.");
    return `g(arg1,arg2);
  }
}
