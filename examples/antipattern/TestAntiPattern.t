/*
 * Copyright (c) 2004-2011, INPL, INRIA
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

package antipattern;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.HashSet;

import antipattern.baralgo.*;
import antipattern.term.*;
import antipattern.term.types.*;

public class TestAntiPattern {

  private Matching4 testM4;
  private Tools tools;
  private Constraint cons;

  %include{ term/Term.tom }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestAntiPattern.class.getName());
  }

  @Before
  public void setUp() {
    testM4 = new Matching4();
    tools = new Tools();
  }

  @Test
  public void testMatching4_1() {

    final String testString = "Simple match";

    cons = tools.atermToConstraint("match(a, a)");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());

    cons = tools.atermToConstraint("match(a, b)");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());

    cons = tools.atermToConstraint("match(anti(a), a)");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());

    cons = tools.atermToConstraint("match(anti(a), b)");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());
  }

  @Test
  public void testMatching4_2() {

    final String testString = "match(f(a,anti(b))";

    cons = tools.atermToConstraint("match(f(a,anti(b)), f(a,a))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());

    cons = tools.atermToConstraint("match(f(a,anti(b)), f(a,c))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());

    cons = tools.atermToConstraint("match(f(a,anti(b)), f(a,b))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());

    cons = tools.atermToConstraint("match(f(a,anti(b)), f(b,c))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());
  }

  @Test
  public void testMatching4_3() {

    final String testString = "match(anti(f(a,anti(b)))";

    cons = tools.atermToConstraint("match(anti(f(a,anti(b))), f(a,a))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());

    cons = tools.atermToConstraint("match(anti(f(a,anti(b))), f(a,c))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());

    cons = tools.atermToConstraint("match(anti(f(a,anti(b))), f(a,b))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());

    cons = tools.atermToConstraint("match(anti(f(a,anti(b))), f(b,c))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());

    cons = tools.atermToConstraint("match(anti(f(a,anti(b))), g(b))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());

    cons = tools.atermToConstraint("match(anti(f(a,anti(b))), f(a,b))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());

    cons = tools.atermToConstraint("match(anti(f(a,anti(b))), f(b,b))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());
  }

  @Test
  public void testMatching4_4() {

    final String testString = "match(f(X,X), f(a,a))";

    cons = tools.atermToConstraint("match(f(X,X), f(a,a))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `Match(Variable("X"),Appl("a",concTerm())));

    cons = tools.atermToConstraint("match(f(X,X), f(a,b))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());
  }

  @Test
  public void testMatching4_5() {

    final String testString = "match(anti(f(X,X))";

    cons = tools.atermToConstraint("match(anti(f(X,X)), f(a,a))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());

    cons = tools.atermToConstraint("match(anti(f(X,X)), f(a,b))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());

    cons = tools.atermToConstraint("match(anti(f(X,X)), g(a))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());
  }

  @Test
  public void testMatching4_6() {

    final String testString = "match(f(X,anti(g(X)))";

    cons = tools.atermToConstraint("match(f(X,anti(g(X))), f(a,b))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `Match(Variable("X"),Appl("a",concTerm())));

    cons = tools.atermToConstraint("match(f(X,anti(g(X))), f(a,g(b)))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `Match(Variable("X"),Appl("a",concTerm())));

    cons = tools.atermToConstraint("match(f(X,anti(g(X))), f(b,g(b)))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());

    cons = tools.atermToConstraint("match(f(X,anti(g(X))), g(b))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());
  }

  @Test
  public void testMatching4_7() {

    final String testString = "match(anti(f(X,anti(g(X))))";

    cons = tools.atermToConstraint("match(anti(f(X,anti(g(X)))), f(a,b))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());

    cons = tools.atermToConstraint("match(anti(f(X,anti(g(X)))), f(a,g(b)))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());

    cons = tools.atermToConstraint("match(anti(f(X,anti(g(X)))), f(b,g(b)))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());

    cons = tools.atermToConstraint("match(anti(f(X,anti(g(X)))), g(b))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());

    cons = tools.atermToConstraint("match(anti(f(X,anti(g(Y)))), f(b,g(b)))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());

    cons = tools.atermToConstraint("match(f(a,anti(g(Y))), f(b,g(b)))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());

    cons = tools.atermToConstraint("match(f(a,anti(g(Y))), f(a,b))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());
  }

  @Test
  public void testMatching4_8() {

    final String testString = "match(f(X,anti(X))";

    cons = tools.atermToConstraint("match(f(X,anti(X)), f(a,a))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());

    cons = tools.atermToConstraint("match(f(X,anti(X)), f(a,b))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `Match(Variable("X"),Appl("a",concTerm())));
  }

  @Test
  public void testMatching4_9() {

    final String testString = "match(f(anti(g(X)),anti(g(X)))";

    cons = tools.atermToConstraint("match(f(anti(g(X)),anti(g(X))),f(g(a),g(b)))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());

    cons = tools.atermToConstraint("match(anti(f(anti(g(X)),anti(g(X)))),f(g(a),g(b)))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `True());
  }

  @Test
  public void testMatching4_10() {

    final String testString = "match(f(anti(X),anti(X))";

    cons = tools.atermToConstraint("match(f(anti(X),anti(X)), f(a,a))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());

    cons = tools.atermToConstraint("match(f(anti(X),anti(X)), f(a,b))");
    assertTrue(testString, testM4.simplifyAndSolve(cons,null) == `False());

  }
}
