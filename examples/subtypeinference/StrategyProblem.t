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
package subtypeinference;
import tom.library.sl.*;

public class StrategyProblem {
	%include { sl.tom }

  static class A {
    public String getOp() { return ""; }
  }

  static class Javaa extends A {
    public Javaa() { }
    public String getOp() { return "a"; }
    public String toString() { return "a()"; }
    public boolean equals(Object o) {
      if(o instanceof Javaa) {
        return true;
      }
      return false;
    }
  }

  static class Javaf extends A {
    public A num1;
    public Javaf(A num1) { this.num1 = num1; }
    public String getOp() { return "f"; }
    public String toString() { return "f(" + num1 + ")"; }
    public boolean equals(Object o) {
      if(o instanceof Javaf) {
        Javaf f = (Javaf) o;
        return num1.equals(f.num1);
      }
      return false;
    }
  }

  static class B extends A {
    public String getOp() { return ""; }
  }
  
  static class Javab extends B {
    public Javab() { }
    public String getOp() { return "b"; }
    public String toString() { return "b()"; }
    public boolean equals(Object o) {
      if(o instanceof Javab) {
        return true;
      }
      return false;
    }
  }

  static class Javag extends B {
    public B num2;
    public Javag(B num2) { this.num2 = num2; }
    public String getOp() { return "g"; }
    public String toString() { return "g(" + num2 + ")"; }
    public boolean equals(Object o) {
      if(o instanceof Javag) {
        Javag g = (Javag) o;
        return num2.equals(g.num2);
      }
      return false;
    }
  }
  
// ------------------------------------------------------------
  %typeterm TomA {
    implement { A }
    is_sort(t) { $t instanceof A }
    equals(t1,t2) { $t1.equals($t2) }
  }

  %typeterm TomB extends TomA {
    implement { B }
    is_sort(t) { $t instanceof B }
    equals(t1,t2) { $t1.equals($t2) }
  }

// ------------------------------------------------------------
  %op TomA a() {
    is_fsym(t) { $t instanceof Javaa }
    make() { new Javaa() }
  }

  %op TomA f(num1:TomA) {
    is_fsym(t) { $t instanceof Javaf }
    make(t) { new Javaf($t) }
    get_slot(num1,t) { ((Javaf)$t).num1 }
  }

  %op TomB b() {
    is_fsym(t) { $t instanceof Javab }
    make() { new Javab() }
  }

  %op TomB g(num2:TomB) {
    is_fsym(t) { $t instanceof Javag }
    make(t) { new Javag($t) }
    get_slot(num2,t) { ((Javag)$t).num2 }
  }

// ------------------------------------------------------------
  public final static void main(String[] args) {
    StrategyProblem test = new StrategyProblem();
    test.testSubtype();
  }


  public void testSubtype() {
    A y = `b();
    B x = `b();
    A z = `f(b());
    A w = `g(b());
    %match {
      (y == a()) && (g(x) == g(b())) -> { System.out.println("Test 1!"); }
      f(a()) << z -> { 
        System.out.println("Test 2!"); 
        A test = propagate(`g(b()));
        System.out.println("test = " + test); 
      }
      g(x) << w && (f(a()) == createA(x)) -> { System.out.println("Test 3!"); }
    }
  }

  public A createA(B arg) {
    return `f(arg);
  }

  
  public A propagate(A elem) {
		try {
			return (A) `TopDown(Try(testSubtype1())).visit(elem, new LocalIntrospector());
		} catch (VisitFailure e) {
			System.out.println("strategy failed");
		}
		return elem;
	}


  %strategy testSubtype1() extends `Identity() {
    visit TomA {
      f(x) -> { return `g(x); }
      //g(x) -> { return `x; }
    }
  } 

  %strategy testSubtype2() extends `Identity() {
    visit TomB {
      g(x) -> { return `x; }
      //g(x) -> { return `f(x); }
    }
  }
}

