package andor;

/*
 * Copyright (c) 2004-2007, INRIA
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
 * 	- Neither the name of the INRIA nor the names of its
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

import junit.framework.TestCase;
import junit.framework.TestSuite;

import andor.testandor.m.types.*;

public class TestAndOr extends TestCase {
  %gom {
    module M
      abstract syntax
      Term = 
      | a()
      | b()
      | c()
      | list( Term* )
      | f(t1:Term,t2:Term)
      | h( l:TermList)
      | g(t:Term)

      TermList = termList(Term*)
  }
  
  public void test1() {
    %match(f(a(),b())) {
      f(x,y) && ( x << a() || x << b() ) -> {        
        return;
      }
    }
    fail();
  }
  
  public void test2() {
    %match(f(b(),b())) {
      f(x,y) && ( x << a() || x << b() ) -> {        
        return;
      }
    }
    fail();
  }
  
  public void test3() {
    %match(f(c(),b())) {
      f(x,y) && ( x << a() || x << b() ) -> {
        fail();
      }
    }
  }
  
  public void test4() {
    %match(f(c(),b())) {
      f(x,y) && ( x << g(a()) || x << b() ) -> {
        fail();
      }
    }
  }
  
  public void test5() {
    %match(f(c(),b())) {
      f(x,y) && ( x << g(a()) || x << b() ) -> {
        fail();
      }
    }
  }
  
  public void test6() {
    %match(f(g(a()),b())) {
      f(x,y) && ( x << g(a()) || x << b() ) -> {
        return;
      }
    }
    fail();
  }
  
  public void test7() {
    Term s = `f(g(a()),b());
    %match(s) {
      f(a(),y) || f(b(),y) << s -> {
        fail();
      }
    }
    return;
  }
  
  public void test8() {
    Term s = `f(g(a()),b());
    %match(s) {
      f(g(a()),y) || f(b(),y) << s -> {
        return;
      }
    }
    fail();
  }
  
  public void test9() {
    Term s = `f(g(a()),b());
    %match(s) {
      f(b(),y) || f(g(a()),y) << s -> {
        return;
      }
    }
    fail();
  }

  /*
   * completeness tests
   */
  
  public void test10() {
    Term s = `f(g(a()),b());
    int counter = 0;
    %match(s) {
      f(x,y) && ( a() << a() || b() << b() ) -> {        
        counter++;
      }
    }
    if (counter != 2){
      fail();
    }
  }
  
  public void test11() {
    Term s = `f(g(a()),b());
    int counter = 0;
    %match(s) {
      f(x,y) && ( g(x) << g(g(a())) || x << g(a()) ) -> {
        counter++;
      }
    }
    if (counter != 2){
      fail();
    }
  }
  
  public void test12() {
    Term s = `f(g(a()),g(b()));
    int counter = 0;
    %match(s) {
      f(_,_) || f(g(_),g(_)) << s -> {
        counter++;
      }
    }
    if (counter != 2){
      fail();
    }
  }

  public void test13() {
    Term s = `f(g(a()),g(b()));
    int counter = 0;
    %match(s) {
      f(_,_) || f(g(a()),g(b())) << s || f(g(_),g(_)) << s -> {
        counter++;
      }
    }
    if (counter != 3){
      fail();
    }
  }
  
  public void test14() {
    Term s = `f(g(a()),g(b()));
    int counter = 0;
    %match(s) {
      // used to generate an error with the optimizer
      f(_,_) && (z << a() || z << b()) -> {
        if (`z != `a() && `z != `b()){
          fail();
        }        
      }
    }    
  }

  public void Test15() {
    %match(a()) {
      x || x << b() || x << c()  -> {        
        Term res = `x;
      }
    }
  }

  public static void main(String[] args) {
   junit.textui.TestRunner.run(new TestSuite(TestAndOr.class));
  }

}
