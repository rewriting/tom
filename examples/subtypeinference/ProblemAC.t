package subtypeinference;
/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
 */

import subtypeinference.problemac.term.types.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class ProblemAC {

  %include{ int.tom }
  %include{ intarray.tom }
  %gom {
    module Term
      imports 
      abstract syntax

      T = | b0()
      | b1()
      | b2()
      | a1()
      | a2()
      | a3()
      | a4()
      | a5()
      | a6()
      | a7()
      | a8()
      | p(T*)
      | m(T*)
      | and(x:T,y:T)
      | or(x:T,y:T)
      | not(x:T)

      m:AC{}
      p:AC{}

  }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(ProblemAC.class.getName());
  }

  @Test
    public void test1() {
      ProblemAC b3 = new ProblemAC();

      T res1 = b3.e_and(`a1(),`a1());
      T res2 = b3.e_not( b3.e_or( b3.e_not(`a1()), b3.e_not(`a1())));
      assertEquals("and(a1,a1)==not(or(not(a1),not(a1)))",res1,res2);
    }

  @Test
    public void test2() {
      ProblemAC b3 = new ProblemAC();

      /*
         and(and(and(a1, a2), and(a3, a4)),and(a5,a6))
         ==
         not(or(or(or(not(a1), not(a2)), or(not(a3), not(a4))),or(not(a5),not(a6))))
       */
      T res1 = b3.e_and(b3.e_and(b3.e_and(`a1(), `a2()), b3.e_and(`a3(), `a4())),b3.e_and(`a5(),`a6()));
      T res2 = b3.e_not(b3.e_or(b3.e_or(b3.e_or(b3.e_not(`a1()), b3.e_not(`a2())), b3.e_or(b3.e_not(`a3()), b3.e_not(`a4()))),b3.e_or(b3.e_not(`a5()),b3.e_not(`a6()))));

      //System.out.println("res1: " + res1); 
      //System.out.println("res2: " + res2); 
      //System.out.println(res1 == res2); 
      assertEquals("and(a1,...,a6)==not(or(not(a1),...,not(a6)))",res1,res2);
    }

  public T e(T t) {
    %match(t) {
      m??(X,X,X,Z*) -> { 
        return `e(p(e(m(X,Z*))));
      }
    }
    return t;
  }

  public T e_and(T X,T Y) {
    T mX= `e(m(X,X));
    T mY= `e(m(Y,Y));
    T b2= `e_b2();
    return `e(p( 
          e(m(mX, mY)),
          e(m(b2, mX, Y)),
          e(m(b2, mY, X)),
          e(m(b2, X, Y)) 
          ));
  }

  public T e_or(T X, T Y) {
    T mX= `e(m(X,X));
    T mY= `e(m(Y,Y));
    T b2= `e_b2();
    return `e(p(
          e(m(b2,mX, mY)),
          e(m(mX, Y)),
          e(m(mY, X)),
          e(m(X, Y)),
          X, Y));
  }


  public T e_b2() {
    return `e(p(b1(),b1())); 
  }

  public T e_not(T x) {
    T b2= `e_b2();
    return `e(p(
          e(m(b2,x)),
          b1()
          )); 
  }

}
