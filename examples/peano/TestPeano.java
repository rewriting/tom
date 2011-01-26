/*
 * Copyright (c) 2004-2011, INPL, INRIA
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
 */

package peano;

import peano.peano.*;
import peano.peano.types.*;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class TestPeano {
  private Nat ten;
  private Nat fibten;

	public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestPeano.class.getName());
	}
 
  @Before
  public void setUp() {
    Nat N = zero();
    for(int i=0 ; i<10 ; i++) {
      N = suc(N);
    }
    ten = N;
    N = zero();
    for(int i=0 ; i<89 ; i++) {
      N = suc(N);
    }
    fibten = N;

  }

  Nat zero() {
    return peano.peano.types.nat.zero.make();
  }

  Nat suc(Nat n) {
    return peano.peano.types.nat.suc.make(n);
  }

  @Test
  public void testPeano1() {
    assertEquals("fib(10) should be 89",
                 fibten,Peano1.fib(ten));
  }

  @Test
  public void testPeano2() {
    assertEquals("fib(10) should be 89",
                 fibten,Peano2.fib(ten));
  }

}
