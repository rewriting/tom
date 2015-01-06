/*
 * Copyright (c) 2010-2015, Universite de Lorraine, Inria
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
import gom.testrulecond.term.types.*;

public class TestRuleCond {

  %gom {
    module Term
    abstract syntax
    Term = a()
         | b()
         | c()
         | f(t:Term)
         | g(t1:Term,t2:Term)
         | h(t1:Term,t2:Term,t3:Term)
   module Term:rules() {
    g(x,y) -> a() if x == a() && y == b()
    g(x,y) -> c() if x == a() && y != b() && y != c()
    f(x) -> a() if x == a() || x == b()
    h(x,y,z) -> a() if ((x == a() && y != b()) || (y != c() && z == a()))
   }
  }
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestRuleCond.class.getName());
  }

  @Test
  public void testRuleFalseCond() {
    Term test = `g(b(),b());
    assertEquals(test,`g(b(),b()));
  }

  @Test
  public void testRuleAnd() {
    Term test = `g(a(),b());
    assertEquals(test,`a());
  }

  @Test
  public void testRuleOra() {
    Term test = `f(a());
    assertEquals(test,`a());
  }

  @Test
  public void testRuleOrb() {
    Term test = `f(b());
    assertEquals(test,`a());
  }

  @Test
  public void testRuleOrc() {
    Term test = `f(c());
    assertEquals(test,`f(c()));
  }

  @Test
  public void testRuleAndAnd() {
    Term test = `g(a(),a());
    assertEquals(test,`c());
  }

  @Test
  public void testRuleParena() {
    Term test = `h(a(),a(),a());
    assertSame(test,`a());
  }
  @Test
  public void testRuleParenb() {
    Term test = `h(a(),c(),c());
    assertSame(test,`a());
  }
  @Test
  public void testRuleParenc() {
    Term test = `h(b(),b(),a());
    assertSame(test,`a());
  }
  @Test
  public void testRuleParend() {
    Term test = `h(b(),b(),b());
    assertNotSame(test,`a());
  }
  @Test
  public void testRuleParene() {
    Term test = `h(b(),a(),c());
    assertNotSame(test,`a());
  }
}
