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

import typeinference.examplemanyinnermatchs.examplemanyinnermatchs.types.*;

public class ExampleManyInnerMatchs {
  %gom {
    module ExampleManyInnerMatchs 
      imports int
      abstract syntax
      
      B = b1()
        | b2()
        | f(n:B)
        | g(n:B)
  }

  public static void main(String[] args) {
    int e1 = 5;
    B e2 = `f(b1());
    B e3 = `f(b2());
    B e4 = `g(b2());

    %match { //match1
      x << int e1 -> {
        System.out.println("match1 : x = " + `x);
        %match { //match2
          f(y) << e2 -> { System.out.println("match2 : x = " + `x + " and y = " + `y); }
        }
      
        %match { //match3
          f(y) << e3 && (x < 10) -> { System.out.println("match3 : x = " + `x + " and y = " + `y); }
        }

        int x = 10;
        %match { //match4
          // Le "x" ici est le "x" du match1 et pas du java
          10 << x -> { System.out.println("match4 : x = " + `x); }
          //(x >= 10) -> { System.out.println("match4 : x = " + `x); }
        }
      }

      f(y) << e2 && g(y) << e4 -> { System.out.println("match5 : y = " + `y); }
    }
  }

}
