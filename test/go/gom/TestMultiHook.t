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

// Port of test/gom/TestMultiHook.t.
// port: `import gom.multihook.types.*;` is the dot import of the Go
// package that gomgo generates from MultiHook.gom (sub-directory
// multihook); its make hooks are installed by the included mapping.
// port: t.geti() is the field I of the Int/OInt term (Java's int is
// int64, the Go type of Gom's int).
// port: the *testing.T parameter is named tt because the original's
// local `Wrapper t` would shadow it.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"

  . "fixture/multihook"
)

  %include { multihook/MultiHook.tom }

func TestInt(tt *testing.T) {
    var t Wrapper = `Int(10)
    assertEquals(tt, t.(*IntWrapper).I,int64(22))
}

func TestOInt(tt *testing.T) {
    var t Wrapper = `OInt(10)
    assertEquals(tt, t.(*OIntWrapper).I,int64(22))
}

// org.junit.Assert.assertEquals(expected, actual)
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
