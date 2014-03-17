/*
 * Copyright (c) 2006-2014, Universite de Lorraine, Inria
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
 * Antoine Reilles   e-mail: Antoine.Reilles@loria.fr
 */
package gom;

import static org.junit.Assert.*;
import org.junit.Test;
import gom.testpeanorule.peano.types.*;

public class TestPeanoRule {

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestPeanoRule.class.getName());
  }

  %gom {
    module peano
    abstract syntax
    Term = | Zero()
           | Suc(pred:Term)
           | Plus(s1:Term,s2:Term)
           | Fib1(t:Term)
           | Fib5(t:Term)
           | Fib6(t:Term)
           | NonZ(t:Term)

    module peano:rules() {
      Plus(x,Zero()) -> x
      Plus(x,Suc(y)) -> Suc(Plus(x,y))
    }

    module peano:rules() {
      Fib1(Zero())        -> Suc(Zero())
      Fib1(res@Suc(Zero()))   -> res
      Fib1(Suc(Suc(x)))   -> Plus(Fib1(x),Fib1(Suc(x)))
    }

    module peano:rules() {
      Fib5(Zero())      -> Suc(Zero())
      Fib5(x)           -> x if eq(x,Suc(Zero()))==true
      Fib5(Suc(Suc(x))) -> Plus(Fib5(x),Fib5(Suc(x)))
    }
    Fib5:block() {
      static boolean eq(Object o1, Object o2) {
        return o1 == o2;
      }
    }
    module peano:rules() {
      Fib6(Zero())      -> Suc(Zero())
      Fib6(x)           -> x if x == Suc(Zero())
      Fib6(Suc(Suc(x))) -> Plus(Fib6(x),Fib6(Suc(x)))
    }
    module peano:rules() {
      NonZ(x) -> x if Zero() != x
      NonZ(x) -> Suc(x)
    }
  }

  @Test
  public void testPlus() {
    for(int i=0 ; i<100 ; i++) {
      Term N = int2peano(i);
      assertTrue("Testing Plus with N ="+N+": ",
          peano2int(`Plus(N,N)) == (i+i) );
    }
  }

  @Test
  public void testFib1() {
    for(int i=0 ; i<15 ; i++) {
      Term N = int2peano(i);
      assertTrue("Testing Fib1 with N ="+N+": ",
          peano2int(`Fib1(N)) == fibint(i) );
    }
  }

  @Test
  public void testFib5() {
    for(int i=0 ; i<15 ; i++) {
      Term N = int2peano(i);
      assertTrue("Testing fib5 with N ="+N+": ",
          peano2int(`Fib5(N)) == fibint(i) );
    }
  }

  @Test
  public void testFib5bis() {
    Term N = `Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Zero()))))))))));
    assertTrue("Testing fib5 with N =" + peano2int(N) + ": ",
               peano2int(`Fib5(N)) == fibint(peano2int(N)));
  }

  @Test
  public void testFib5ter() {
    Term N = `Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Zero()))))))))));
    assertTrue("Testing fib5 with N =" + peano2int(N)+1 + ": ",
               peano2int(`Fib5(Suc(N))) == fibint(peano2int(N)+1));
  }

  @Test
  public void testFib6() {
    for(int i=0 ; i<15 ; i++) {
      Term N = int2peano(i);
      assertTrue("Testing fib6 with N ="+N+": ",
          peano2int(`Fib6(N)) == fibint(i) );
    }
  }

  @Test
  public void testFib6bis() {
    Term N = `Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Zero()))))))))));
    assertTrue("Testing fib6 with N =" + peano2int(N) + ": ",
               peano2int(`Fib6(N)) == fibint(peano2int(N)));
  }

  @Test
  public void testFib6ter() {
    Term N = `Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Zero()))))))))));
    assertTrue("Testing fib6 with N =" + peano2int(N)+1 + ": ",
               peano2int(`Fib6(Suc(N))) == fibint(peano2int(N)+1));
  }

  @Test
  public void testnonZ1() {
    Term N = `Zero();
    assertEquals("NonZ is not zero", `NonZ(N), `Suc(Zero()));
  }

  @Test
  public void testnonZ2() {
    Term N = `Suc(Zero());
    assertEquals("NonZ is id for non Zero", `NonZ(N), N);
  }

  public int fibint(int n) {
    if(n<=1) {
      return 1;
    } else {
      return fibint(n-1)+fibint(n-2);
    }
  }

  @Test
  public void testInt2p() {
    assertEquals("int2peano should return sssss(tzero) for 5",
        int2peano(5),`Suc(Suc(Suc(Suc(Suc(Zero()))))));
  }

  public Term int2peano(int n) {
    Term N = `Zero();
    for(int i=0 ; i<n ; i++) {
      N = `Suc(N);
    }
    return N;
  }

  @Test
  public void testPeano2int() {
    assertEquals("peano2int should return 5 for sssss(tzero)",
        peano2int(`Suc(Suc(Suc(Suc(Suc(Zero())))))),5);
  }

  public int peano2int(Term N) {
    %match(Term N) {
      Zero() -> { return 0; }
      Suc(x) -> {return 1+peano2int(`x); }
    }
    return 0;
  }
}
