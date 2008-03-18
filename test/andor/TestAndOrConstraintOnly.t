package andor;

/*
 * Copyright (c) 2004-2008, INRIA
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

import andor.testandorconstraintonly.m.types.*;

public class TestAndOrConstraintOnly extends TestCase {
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
    %match {
      f(_x,_y) << f(a(),b())  && ( _x << a() || _x << b() ) -> {        
        return;
      }
    }
    fail();
  }
  
  public void test2() {
    %match {
      f(_x,_y) << f(b(),b()) && ( _x << a() || _x << b() ) -> {        
        return;
      }
    }
    fail();
  }
  
  public void test3() {
    %match {
      f(_x,_y) << f(c(),b()) && ( _x << a() || _x << b() ) -> {
        fail();
      }
    }
  }
  
  public void test4() {
    %match {
      f(_x,_y) << f(c(),b()) && ( _x << g(a()) || _x << b() ) -> {
        fail();
      }
    }
  }
  
  public void test5() {
    %match {
      f(_x,_y) << f(c(),b()) && ( _x << g(a()) || _x << b() ) -> {
        fail();
      }
    }
  }
  
  public void test6() {
    %match {
      f(_x,_y) << f(g(a()),b()) && ( _x << g(a()) || _x << b() ) -> {
        return;
      }
    }
    fail();
  }
  
  public void test7() {
    Term s = `f(g(a()),b());
    %match {
      f(a(),_y) << s || f(b(),_y) << s -> {
        fail();
      }
    }
    return;
  }
  
  public void test8() {
    Term s = `f(g(a()),b());
    %match {
      f(g(a()),_y) << s || f(b(),_y) << s -> {
        return;
      }
    }
    fail();
  }
  
  public void test9() {
    Term s = `f(g(a()),b());
    %match {
      f(b(),_y) << s || f(g(a()),_y) << s -> {
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
    %match {
      f(_x,_y) << s && ( a() << a() || b() << b() ) -> {        
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
    %match {
      f(_x,_y) << s && ( g(_x) << g(g(a())) || _x << g(a()) ) -> {
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
    %match {
      f(_,_) << s || f(g(_),g(_)) << s -> {
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
    %match {
      f(_,_) << s || f(g(a()),g(b())) << s || f(g(_),g(_)) << s -> {
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
    %match {
      // used to generate an error with the optimizer
      f(_,_) << s && (z << a() || z << b()) -> {
        if (`z != `a() && `z != `b()){
          fail();
        }        
      }
    }    
  }

  public void test15() {
    Term t = `f(a(),b());
    %match {
      f(x,_y) << t && (a() << x || b() << x)  -> {        
        if (`x != `a()){
          fail();
        }
        return;
      }
    }
    fail();
  }
  
  public void test16() {
    Term t = `f(g(a()),b());
    %match {
      f(x,_y) << t && g(z) << x  -> {        
        if (`z != `a()) {
          fail();
        }
        return;
      }
    }
    fail();
  }
  
  public void test17() {
    Term t = `f(g(a()),b());
    %match {
      _ << t && g(z) << x && f(x,_y) << t  -> {        
        if (`z != `a()){
          fail();
        }
        return;
      }
    }
    fail();
  }

  public void test18() {
    Term l = `list(a(),b(),g(a()),c());
    %match {
      list(X*,b(),Y*) << l && z << list(X*,Y*)  -> {        
        if (`z != `list(a(),g(a()),c())){
          fail();
        }
        return;
      }
    }
    fail();
  }
  
  public void test19() {
    Term l = `list(a(),b(),g(a()),c());
    %match {
      list(X*,b(),Y*) << l && list(_*,g(z),_*) << list(X*,Y*)  -> {     
        if (`z != `a()){
          fail();
        }
        return;
      }
    }
    fail();
  }
  
  public void test20() {
    Term s = `a();
    int cnt = 0;
    %match {
      a() << s || b() << s  -> { cnt++; }
    }  
    if (cnt > 1) {
      fail();
    }
  }
  
  public void test21() {    
    Term s = `a();   
    int cnt = 0;
    %match {
      x << Term s || x << Term s  -> { 
        if (`x != `a()) {
          fail();
        }
        cnt++;        
      }
    }
    if (cnt != 2) {
      fail();
    }
  }
  
  public static void test22() {
    Term s = `g(a());
    int cnt = 0;
    %match {
      g(x) << s || g(x) << s -> { 
        if (`x != `a()) {
          fail();
        }
        cnt++;
      }      
    }
    if (cnt != 2) {
      fail();
    }
  }

  public static void main(String[] args) {
   junit.textui.TestRunner.run(new TestSuite(TestAndOrConstraintOnly.class));
  }

}
