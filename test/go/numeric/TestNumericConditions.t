package numeric

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

// Port of test/numeric/TestNumericConditions.t.

import "testing"

  %gom {
    module M
      imports int
      abstract syntax
      Term = 
      | a()
      | b()
      | c()
      | list( Term* )
      | f(t1:Term,t2:int)
      | h( l:TermList)
      | g(t:Term)

      TermList = termList(Term*)
}

func Test1(t *testing.T) {
    %match(f(a(),6)) {
      f(_x,y) && y > 5 -> {        
        return
      }
    }
  t.FailNow()
}

func Test2(t *testing.T) {
    %match(f(a(),6)) {
      f(_x,y) && y >= 6 -> {        
        return
      }
    }
  t.FailNow()
}

func Test3(t *testing.T) {
    %match(f(a(),3)) {
      f(_x,y) && y < 6 -> {        
        return
      }
    }
  t.FailNow()
}

func Test4(t *testing.T) {
    %match(f(a(),4)) {
      f(_x,y) && y <= 3 -> {        
        t.FailNow()
      }
    }
  return
}

func Test5(t *testing.T) {
    %match(f(a(),4)) {
      f(_x,y) && y != 4 -> {        
        t.FailNow()
      }
    }
  return
}

func Test6(t *testing.T) {
    %match(f(a(),4)) {
      f(_x,y) && y == 4 -> {        
        return
      }
    }
  t.FailNow()
}

func Test7(t *testing.T) {
    %match(f(a(),4)) {
      f(_x,y) && y > 2 && y < 5 -> {        
        return
      }
    }
  t.FailNow()
}

func Test8(t *testing.T) {
    %match(f(a(),6)) {
      f(_x,y) && y > 2 && y < 5 -> {        
        t.FailNow()
      }
    }
  return
}

func Test9(t *testing.T) {
    %match(f(a(),6)) {
      f(_x,y) && ( y > 2 || y < 5 ) -> {        
        return
      }
    }
  t.FailNow()
}

func Test10(t *testing.T) {
    %match(f(a(),6)) {
      f(_x,y) && ( y !=6 || y < 5 ) -> {        
        t.FailNow()
      }
    }
  return
}

func Test11(t *testing.T) {
    %match(f(a(),6)) {
      f(_x,y) && ( y ==6 || y < 5 ) -> {        
        return
      }
    }
  t.FailNow()
}

func Test12(t *testing.T) {
    %match(f(a(),6)) {
      f(_x,y) && ( y !=6 || y >= 5 ) -> {        
        return
      }
    }
  t.FailNow()
}

func Test13(t *testing.T) {
    %match(f(a(),6)) {
      //f(_x,y) && y > int getIntegerValue() -> { 
      // Without explicity type declaration "int" for getIntegerValue
      f(_x,y) && y > getIntegerValue() -> {        
        return
      }
    }
  t.FailNow()
}

func Test14(t *testing.T) {
    %match(f(a(),6)) {
      //f(_x,y) && y == int getIntegerValue() -> {
      // Without explicity type declaration "int" for getIntegerValue
      f(_x,y) && y == getIntegerValue()-> {        
        t.FailNow()
      }
    }
  return
}
  
func Test15(t *testing.T) {
  var m int64 = 6 // Java int is the Tom int, int64 in Go
    %match(f(a(),6)) {
      //f(_x,y) && y == int m -> {        
      // Without explicity type declaration "int" for m 
      f(_x,y) && y == m -> {        
        return
      }
    }
  t.FailNow()
}

func getIntegerValue() int64 {
  return 5
}
