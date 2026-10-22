package commonstrat

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

// Port of test/sl/CommonStrat.t, the strategy that sl/TestImportStrat.t
// imports.
// port: package commonstrat instead of sl: two Tom-generated Go files
// cannot share a package (each defines the tom_* helpers of the mappings
// it includes), so the port harness compiles this file into its own
// package (sub-directory commonstrat of the test module).
// port: `import sl.testsl.types.*;` is the dot import of the Go package
// that gomgo generates from testsl.gom (sub-directory testsl).

import (
  . "fixture/testsl"
  "tom/library/sl"
)

  %include { sl.tom }
  %include { testsl/testsl.tom}

  %strategy R() extends Fail() {
    visit Term {
      a() -> b()
    }
  }

// port: Java's `new CommonStrat.R()` reaches the public nested class R;
// in Go the strategy's constructor is unexported, so NewR exports it.
func NewR() sl.Strategy { return `R() }

// port: Term is otherwise only used by the generated code.
var _ Term
