package sl

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

// Port of test/sl/TestRef.t.
// port: unsupported testRef1, testRef2, testRef3, testRef4: they need the
// term pointers of Gom's --termpointer option (operators RefTerm, LabTerm,
// PathTerm, VarTerm, the expand() method of the generated terms) and the
// term-graph strategies of sl/graph.tom (DeRef, StrictDeRef, AllSeq),
// which gomgo and the Go sl library do not implement. The tests, the
// %include of sl/graph.tom and the TopDownSeq operator (defined with
// AllSeq) are kept below in comments.
// port: `import sl.testsl.types.*;` is the dot import of the Go package
// that gomgo generates from testsl.gom (sub-directory testsl).
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  . "fixture/testsl"
  "tom/library/sl"
)

  %include { sl.tom }
  //%include { sl/graph.tom }
  //%include { testsl/_testsl.tom }
  %include { testsl/testsl.tom }

  //%op Strategy TopDownSeq(s1:Strategy) {
  //  make(v) { `mu(MuVar("_x"),Sequence(v,AllSeq(MuVar("_x")))) }
  //}

  %strategy AB() extends `Identity() {
    visit Term {
      a() -> { return `b(); }
      b() -> { return `c(); }
    }
  }

  // port: AB is otherwise only used by the unsupported tests.
  var _ sl.Strategy = `AB()

  //func TestRef1(t *testing.T) {
  //  var subject Term = `g(g(a(),RefTerm("l")),LabTerm("l",a())).expand()
  //  res := visit(t, `TopDown(StrictDeRef(AB())), subject)
  //  res = visit(t, `TopDown(DeRef(AB())), subject)
  //  res = visit(t, `TopDownSeq(StrictDeRef(AB())), subject)
  //  assertEquals(t, `g(g(a(),RefTerm("n")),LabTerm("n",b())).expand(),res)
  //}

  //func TestRef2(t *testing.T) {
  //  var subject Term = `g(g(a(),RefTerm("l")),LabTerm("l",a())).expand()
  //  res := visit(t, `TopDown(StrictDeRef(AB())), subject)
  //  res = visit(t, `TopDown(DeRef(AB())), subject)
  //  res = visit(t, `TopDownSeq(StrictDeRef(AB())), subject)
  //  res = visit(t, `TopDownSeq(DeRef(AB())), subject)
  //  assertEquals(t, `g(g(b(),RefTerm("n")),LabTerm("n",c())).expand(),res)
  //}

  //func TestRef3(t *testing.T) {
  //  var subject2 Term = `g(g(a(),LabTerm("l",a())),RefTerm("l")).expand()
  //  res2 := visit(t, `TopDown(StrictDeRef(AB())), subject2)
  //  res2 = visit(t, `TopDown(DeRef(AB())), subject2)
  //  res2 = visit(t, `TopDownSeq(StrictDeRef(AB())), subject2)
  //  assertEquals(t, `g(g(a(),LabTerm("n",b())),RefTerm("n")).expand(),res2)
  //}

  //func TestRef4(t *testing.T) {
  //  var subject2 Term = `g(g(a(),LabTerm("l",a())),RefTerm("l")).expand()
  //  res2 := visit(t, `TopDown(StrictDeRef(AB())), subject2)
  //  res2 = visit(t, `TopDown(DeRef(AB())), subject2)
  //  res2 = visit(t, `TopDownSeq(StrictDeRef(AB())), subject2)
  //  res2 = visit(t, `TopDownSeq(DeRef(AB())), subject2)
  //  assertEquals(t, `g(g(b(),LabTerm("n",c())),RefTerm("n")).expand(),res2)
  //}
