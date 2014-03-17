package andor;

/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
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

import static org.junit.Assert.*;
import org.junit.Test;

import andor.testandor.m.types.*;

public class TestAndOr {
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
  
  @Test
  public void test1() {
    %match(f(a(),b())) {
      f(_x,_y) && ( _x << a() || _x << b() ) -> {        
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test2() {
    %match(f(b(),b())) {
      f(_x,_y) && ( _x << a() || _x << b() ) -> {        
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test3() {
    %match(f(c(),b())) {
      f(_x,_y) && ( _x << a() || _x << b() ) -> {
        fail();
      }
    }
  }
  
  @Test
  public void test4() {
    %match(f(c(),b())) {
      f(_x,_y) && ( _x << g(a()) || _x << b() ) -> {
        fail();
      }
    }
  }
  
  @Test
  public void test5() {
    %match(f(c(),b())) {
      f(_x,_y) && ( _x << g(a()) || _x << b() ) -> {
        fail();
      }
    }
  }
  
  @Test
  public void test6() {
    %match(f(g(a()),b())) {
      f(_x,_y) && ( _x << g(a()) || _x << b() ) -> {
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test7() {
    Term s = `f(g(a()),b());
    %match(s) {
      f(a(),_y) || f(b(),_y) << s -> {
        fail();
      }
    }
    return;
  }
  
  @Test
  public void test8() {
    Term s = `f(g(a()),b());
    %match(s) {
      f(g(a()),_y) || f(b(),_y) << s -> {
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test9() {
    Term s = `f(g(a()),b());
    %match(s) {
      f(b(),_y) || f(g(a()),_y) << s -> {
        return;
      }
    }
    fail();
  }

  /*
   * completeness tests
   */
  
  @Test
  public void test10() {
    Term s = `f(g(a()),b());
    int counter = 0;
    %match(s) {
      f(_x,_y) && ( a() << a() || b() << b() ) -> {        
        counter++;
      }
    }
    if (counter != 2){
      fail();
    }
  }
  
  @Test
  public void test11() {
    Term s = `f(g(a()),b());
    int counter = 0;
    %match(s) {
      f(_x,_y) && ( g(_x) << g(g(a())) || _x << g(a()) ) -> {
        counter++;
      }
    }
    if (counter != 2){
      fail();
    }
  }
  
  @Test
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

  @Test
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
  
  @Test
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

  @Test
  public void test15() {
    Term t = `f(a(),b());
    %match(t) {
      f(x,_y) && (a() << x || b() << x)  -> {        
        if (`x != `a()){
          fail();
        }
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test16() {
    Term t = `f(g(a()),b());
    %match(t) {
      f(x,_y) && g(z) << x  -> {        
        if (`z != `a()){
          fail();
        }
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test17() {
    Term t = `f(g(a()),b());
    %match(t) {
      _ && g(z) << x && f(x,_y) << t  -> {        
        if (`z != `a()){
          fail();
        }
        return;
      }
    }
    fail();
  }

  @Test
  public void test18() {
    Term l = `list(a(),b(),g(a()),c());
    %match(l) {
      list(X*,b(),Y*) && z << list(X*,Y*)  -> {        
        if (`z != `list(a(),g(a()),c())){
          fail();
        }
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test19() {
    Term l = `list(a(),b(),g(a()),c());
    %match(l) {
      list(X*,b(),Y*) && list(_*,g(z),_*) << list(X*,Y*)  -> {     
        if (`z != `a()){
          fail();
        }
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test20() {
    Term s = `a();
    int cnt = 0;
    %match(s) {
      a() || b() << s  -> { cnt++; }
    }  
    if (cnt > 1) {
      fail();
    }
  }
  
  @Test
  public void test21() {    
    Term s = `a();   
    int cnt = 0;
    %match(Term s) {
      x || x << Term s  -> { 
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
  
  @Test
  public void test22() {
    Term s = `g(a());
    int cnt = 0;
    %match(s) {
      g(x) || g(x) << s -> { 
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
  
  // allow any number of parantheses
  @Test
  public void test23() {
    %match(f(a(),b())) {
      f(_x,_y) && ((((( _x << a() || _x << b() ))))) -> {        
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test24() {
    %match(f(a(),b())) {
      f(_x,_y) && (( _x << a() || _x << b() )) -> {        
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test25() {
    %match(f(a(),b())) {
      f(_x,_y) && (( (_x << a()) || ((_x << b())) )) -> {        
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test26() {
    %match(f(a(),b())) {
      f(_x,_y) && ( (_x << a() && _x << a()) || ( _x << b() && _x << b() ) ) -> {        
        return;
      }
    }
    fail();
  }

  
  @Test
  public void test27() {
    %match(f(a(),b())) {
      f(_x,_y) && ( (((_x << a())) && _x << a()) || ( _x << b() && _x << b() ) ) -> {        
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test28() {
    Term l = `list(a(),b(),g(a()),c());
    %match(l) {
      list(_*,b(),_*) || list(_*,c(),_*) << l -> {
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test29() {
    Term l = `f(a(),a());
    %match(l) {
      f(t1,t2) && t1==t2 -> {
        return;
      }
    }
    fail();
  }

  @Test
  public void test30() {
    Term l = `f(a(),b());
    %match(l) {
      g(_) || f(x,x)<<l -> {
        fail();
      }
    }
    return;
  }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestAndOr.class.getName());
  }

}
