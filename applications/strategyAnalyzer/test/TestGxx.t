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


import gxx.m.*;
import gxx.m.types.*;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class TestGxx {
  %include { gxx/m/m.tom }
	public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(Gxx.class.getName());
	}
 
  @Before
  public void setUp() {
  }

  Object run(Object t) {
    //Object res = findMainStrat(t); // trouver le bon mainStrat en fonction du type dynamique de t
    Object res = `mainStrat_T((T) t);
    return res;
  }

  @Test
  public void testGxx1() {
    assertEquals("mainStrat(g(a)) should be a()",
                 `a(), run(`g(a())));
  }
  
  @Test
  public void testGxx2() {
    assertEquals("mainStrat(g(g(a))) should be a()",
                 `a(), run(`g(g(a()))));
  }

  @Test
  public void testGxx3() {
    assertEquals("mainStrat(g(f(g(a),g(b)))) should be f(a,b)",
                 `f(a(),b()), run(`g(f(g(a()),g(b())))));
  }

}
