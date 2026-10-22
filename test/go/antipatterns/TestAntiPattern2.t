package antipatterns

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

// Port of test/antipatterns/TestAntiPattern2.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import antipatterns.testantipattern2.m.types.*;`.
// port: the *testing.T parameter is named t, so the original's local
// `TwoPath t` is tt.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"
)

  %gom {
    module M
      abstract syntax
      TwoPath = 
      | id()
      | g()
      | c0( TwoPath* )
      | c1( TwoPath* )
  }

func Test1(t *testing.T) {
    var res TwoPath = `c1(c0(g(),id()),c0(id(),g()))
    %match(res) {
      c1(c0(_M*,_f@!id[],_N*), c0(_P*,g@!id[],_Q*), _tail*) -> {
        if(`g.Isid()) {
          //fail();
        }
      }
    }
}

func Test2(t *testing.T) {
    var tt TwoPath = `g()
    %match(tt) {
      !id() -> { return; /* OK */ }
    }
    fail(t)
}

func Test3(t *testing.T) {
    var p1 TwoPath = `c0(g(),id())
    var p2 TwoPath = `c0(id(),g())
    %match(p1,p2) {
      c0(_M*,_f@!id[],_N*), c0(_P*,g@!id[],_Q*) -> {
        if(`g.Isid()) {
          //fail();
        }
      }
    }
}

// org.junit.Assert.fail()
func fail(t *testing.T) {
  t.Helper()
  t.Fatal()
}
