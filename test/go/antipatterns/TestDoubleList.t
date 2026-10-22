package antipatterns

/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
 *  - Neither the name of the Inria nor the names of its
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

// Port of test/antipatterns/TestDoubleList.t.
// port: java.util.LinkedList is Go's *list.List (the Go mapping
// util/LinkedList.tom); new LinkedList(Arrays.asList(…)) is newLinkedList(…),
// result.add(x) is result.PushBack(x), result.contains(i) is contains(result, i).
// port: Java autoboxes the int literals to Integer; the Go elements are int,
// and (Integer)`x is `x.(int).
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "container/list"
  "testing"
)

  %include{ util/LinkedList.tom }
  
func Test1(t *testing.T) {
    var l *list.List = newLinkedList(1,2,3,1,4,3,1,1)
    //System.out.println("list = " + l);
    var result *list.List = list.New()
    %match(LinkedList l, LinkedList l) {
      !concLinkedList(_*,x,_*,x,_*),concLinkedList(_*,x,_*) -> { result.PushBack(`x.(int)) }
    }
    if (!(contains(result, 2) && contains(result, 4))) { fail(t) }
}
  
func Test2(t *testing.T) {
    var l *list.List = newLinkedList(1,2,3,1,4,3,1,1)
    //System.out.println("list = " + l);
    var result *list.List = list.New()
    %match(LinkedList l, LinkedList l) {
      concLinkedList(_*,x,_*),!concLinkedList(_*,x,_*,x,_*) -> { result.PushBack(`x.(int)) }
    }
    if (!(contains(result, 2) && contains(result, 4))) { fail(t) }
}

// new LinkedList(Arrays.asList(xs…))
func newLinkedList(xs ...int) *list.List {
  l := list.New()
  for _, x := range xs {
    l.PushBack(x)
  }
  return l
}

// java.util.LinkedList.contains
func contains(l *list.List, x any) bool {
  for e := l.Front(); e != nil; e = e.Next() {
    if e.Value == x {
      return true
    }
  }
  return false
}

// org.junit.Assert.fail()
func fail(t *testing.T) {
  t.Helper()
  t.Fatal()
}
