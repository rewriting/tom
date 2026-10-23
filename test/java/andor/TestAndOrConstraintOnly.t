package andor;

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

import static org.junit.Assert.*;
import org.junit.Test;

import andor.testandorconstraintonly.m.types.*;

public class TestAndOrConstraintOnly {
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
      | g(t1:Term)

      TermList = termList(Term*)
  }
 
  @Test
  public void test1() {
    %match {
      f(_x,_y) << f(a(),b())  && ( _x << a() || _x << b() ) -> {        
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test2() {
    %match {
      f(_x,_y) << f(b(),b()) && ( _x << a() || _x << b() ) -> {        
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test3() {
    %match {
      f(_x,_y) << f(c(),b()) && ( _x << a() || _x << b() ) -> {
        fail();
      }
    }
  }
  
  @Test
  public void test4() {
    %match {
      f(_x,_y) << f(c(),b()) && ( _x << g(a()) || _x << b() ) -> {
        fail();
      }
    }
  }
  
  @Test
  public void test5() {
    %match {
      f(_x,_y) << f(c(),b()) && ( _x << g(a()) || _x << b() ) -> {
        fail();
      }
    }
  }
  
  @Test
  public void test6() {
    %match {
      f(_x,_y) << f(g(a()),b()) && ( _x << g(a()) || _x << b() ) -> {
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test7() {
    Term s = `f(g(a()),b());
    %match {
      f(a(),_y) << s || f(b(),_y) << s -> {
        fail();
      }
    }
    return;
  }
  
  @Test
  public void test8() {
    Term s = `f(g(a()),b());
    %match {
      f(g(a()),_y) << s || f(b(),_y) << s -> {
        return;
      }
    }
    fail();
  }
  
  @Test
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
  
  @Test
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
  
  @Test
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
  
  @Test
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

  @Test
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
  
  @Test
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

  @Test
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
  
  @Test
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
  
  @Test
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

  @Test
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
  
  @Test
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
  
  @Test
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
  
  @Test
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
  
  @Test
  public void test22() {
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
  
//operators' priority
  @Test
  public void test23() {
    %match {
      a() << a() || a() << a() && a() << b()  -> {        
        return;
      }
    }
    fail();
  }

  @Test
  public void test24() {
    %match {
      (a() << a() || a() << a()) && a() << b()  -> {        
        fail();
      }
    }    
  }
  
  @Test
  public void test25() {
    %match {
      (a() << a() && a() << b()) || (a() << a() && b()<<b())   -> {        
        return;
      }
    }
    fail();
  }

  @Test
  public void test26() {
    int cpt = 0;
    %match {
      (a|b)() << a() -> { cpt++; }
      (f|g)[t1=_] << g(a()) -> { cpt++; }
      ((a|b)() << a()) -> { cpt++; }
      ((f|g)[t1=_] << g(a())) -> { cpt++; }
    }
    if(cpt!=4) {
      fail();
    }
  }

  @Test
  public void test27() {
    int cpt = 0;
    %match {
      termList(_*,a(),_*) << termList(a(),b()) -> { cpt++; }
      (termList(_*,a(),_*) << termList(a(),b())) -> { cpt++; }
      ((termList(_*,a(),_*) << termList(a(),b()))) -> { cpt++; }
    }
    if(cpt!=3) {
      fail();
    }
  }
  
//tests for free vars in disjunctions
  @Test
  public void test28() {
    %match {
      f(x,_) << f(a(),b()) || x << a()  -> {
        Object tmp = `x;
        return;
      }
    }
    fail();
  }

  @Test
  public void test29() {
    %match {
      ( f(x,y) << f(a(),b()) && y << b() ) || x << a()  -> {
        Object tmp = `x;
        return;
      }
    }
    fail();
  }
  
  @Test
  public void test30() {
    %match {
      ( f(x,y) << f(a(),b()) && y << b() ) || x << a()  -> {
        Object tmp = `x;
        return;
      }
      ( f(_,y) << f(a(),b()) && y << b() ) || a() << a()  -> {        
        return;
      }
      ( f(_,y) << f(a(),b()) && y << b() ) || y << a()  -> {
        Object tmp = `y;
        return;
      }
    }
    fail();
  }

  @Test
  public void test31() {
    %match {
      f(t1,t2) << f(a(),a()) && t1==t2 -> {
        return;
      }
    }
    fail();
  }
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestAndOrConstraintOnly.class.getName());
  }

}
