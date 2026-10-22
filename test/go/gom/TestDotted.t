package gom

/*
 * Copyright (c) 2006-2015, Universite de Lorraine, Inria
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

// Port of test/gom/TestDotted.t.
// port: `import gom.dotted.types.*;` is the dot import of the Go package
// that gomgo generates from Dotted.gom (sub-directory dotted).
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"

  . "fixture/dotted"
)

  %include{ dotted/Dotted.tom }

func Test1(t *testing.T) {
    var n Nop = `BinaryNop(UnaryNop(EmptyNop()),UnaryNop(EmptyNop()))
    %match(n) {
      BinaryNop(UnaryNop(EmptyNop()),UnaryNop(EmptyNop())) -> {
        return
      }
    }
    fail(t, "Should have matched")
}

// org.junit.Assert.fail(message)
func fail(t *testing.T, message string) {
  t.Helper()
  t.Fatal(message)
}
