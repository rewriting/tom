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

package antipatterns;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import antipatterns.testantipattern.antipattern.types.*;

public class TestAntiPattern {	  

  %gom {
    module AntiPattern
    imports String int
    abstract syntax
    Term = a()
    | b()
    | c()
    | f(x1:Term, x2:Term) 
    | g(pred:Term)
    | ff(x1:Term, x2:Term)
    | i(val:int)
    | j(val:int)

    Result = True()
    | False()
    | And(Result*)
    | Or(Result*)
    | Equal(x1:String,x2:Term)
    | NEqual(x1:String,x2:Term)
  }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestAntiPattern.class.getName());
  }

  @Before
  public void setUp() {

  }

  private Result match1(Term subject){
    %match(Term subject){
      a() ->{
        return `True();
      }
    }
    return `False();
  }

  @Test
  public void testAp1() {		
    assertTrue(match1(`a()) == `True());
    assertTrue(match1(`b()) == `False());
  }

  private Result match2(Term subject){
    %match(Term subject){
      !a() ->{
        return `True();
      }
    }
    return `False();
  }


  @Test
  public void testAp2() {		
    assertTrue(match2(`a()) == `False());
    assertTrue(match2(`b()) == `True());
  }

  private Result match3(Term subject){
    %match(Term subject){
      f(a(),!b()) ->{
        return `True();
      }
    }
    return `False();
  }

  @Test
  public void testAp3() {
    assertTrue(match3(`f(a(),a())) == `True());
    assertTrue(match3(`f(a(),c())) == `True());
    assertTrue(match3(`f(a(),b())) == `False());
    assertTrue(match3(`f(b(),c())) == `False());
  }

  private Result match3_1(Term subject){
    %match(Term subject){
      f(!a(),!b()) ->{
        return `True();
      }
    }
    return `False();
  }

  @Test
  public void testAp3_1() {
    assertTrue(match3_1(`f(a(),a())) == `False());
    assertTrue(match3_1(`f(a(),c())) == `False());
    assertTrue(match3_1(`f(a(),b())) == `False());
    assertTrue(match3_1(`f(b(),c())) == `True());
    assertTrue(match3_1(`f(b(),b())) == `False());
  }

  private Result match3_2(Term subject){
    %match(Term subject){
      !f[] ->{
        return `True();
      }
    }
    return `False();
  }

  @Test
  public void testAp3_2() {
    assertTrue(match3_2(`f(a(),a())) == `False());
    assertTrue(match3_2(`a()) == `True());		
  }

  private Result match3_3(Term subject){
    %match(Term subject){
      !f(_,_) ->{
        return `True();
      }
    }
    return `False();
  }

  @Test
  public void testAp3_3() {
    assertTrue(match3_3(`f(a(),a())) == `False());
    assertTrue(match3_3(`a()) == `True());		
  }

  private Result match3_4(Term subject){
    %match(Term subject){
      !(f|ff)(a(),!b()) ->{
        return `True();
      }
    }
    return `False();
  }

  @Test
  public void testAp3_4() {
    assertTrue(match3_4(`f(a(),a())) == `False());
    assertTrue(match3_4(`ff(a(),a())) == `False());
    assertTrue(match3_4(`f(a(),b())) == `True());
    assertTrue(match3_4(`ff(a(),b())) == `True());
    assertTrue(match3_4(`f(b(),b())) == `True());
    assertTrue(match3_4(`ff(b(),b())) == `True());
    assertTrue(match3_4(`a()) == `True());		
  }


  private Result match4(Term subject){
    %match(Term subject){
      !f(a(),!b()) ->{
        return `True();
      }
    }
    return `False();
  }

  @Test
  public void testAp4() {
    assertTrue(match4(`f(a(),a())) == `False());
    assertTrue(match4(`f(a(),c())) == `False());
    assertTrue(match4(`f(a(),b())) == `True());
    assertTrue(match4(`f(b(),c())) == `True());
    assertTrue(match4(`g(b())) == `True());
    assertTrue(match4(`f(b(),b())) == `True());
  }

  private Result match5(Term subject){
    %match(Term subject){
      f(x,x) ->{
        return `Equal("x",x);
      }
    }
    return `False();
  }

  @Test
  public void testAp5() {
    assertTrue(match5(`f(a(),a())) == `Equal("x",a()));
    assertTrue(match5(`f(a(),b())) == `False());		
  }

  private Result match7(Term subject){
    %match(Term subject){
      !f(x,x) ->{
        return `True();
      }
    }
    return `False();
  }

  @Test
  public void testAp7() {
    assertTrue(match7(`f(a(),a())) == `False());
    assertTrue(match7(`f(a(),b())) == `True());
    assertTrue(match7(`g(a())) == `True());
  }


  private Result match8(Term subject){
    %match(Term subject){
      f(x,!g(x)) ->{
        return `Equal("x",x);
      }
    }
    return `False();
  }

  @Test
  public void testAp8() {
    assertTrue(match8(`f(a(),b())) == `Equal("x",a()));
    assertTrue(match8(`f(a(),g(b()))) == `Equal("x",a()));
    assertTrue(match8(`f(b(),g(b()))) == `False());
    assertTrue(match8(`g(b())) == `False());
  }

  private Result match8_1(Term subject){
    %match(Term subject){
      f(!g(x),x) ->{
        return `Equal("x",x);
      }
    }
    return `False();
  }

  @Test
  public void testAp8_1() {
    assertTrue(match8_1(`f(b(),a())) == `Equal("x",a()));
    assertTrue(match8_1(`f(g(b()),a())) == `Equal("x",a()));
    assertTrue(match8_1(`f(g(b()),b())) == `False());
    assertTrue(match8_1(`g(b())) == `False());
  }

  private Result match9(Term subject){
    %match(Term subject){
      !f(x,!g(x)) ->{
        return `True();
      }
    }
    return `False();
  }

  @Test
  public void testAp9() {
    assertTrue(match9(`f(a(),b())) == `False());
    assertTrue(match9(`f(a(),g(b()))) == `False());
    assertTrue(match9(`f(b(),g(b()))) == `True());
    assertTrue(match9(`g(b())) == `True());
  }

  private Result match9_1(Term subject){
    %match(Term subject){
      !f(_x,!g(_y)) ->{
        return `True();
      }
    }
    return `False();
  }

  @Test
  public void testAp9_1() {
    assertTrue(match9_1(`f(b(),g(b()))) == `True());
    assertTrue(match9_1(`f(a(),g(b()))) == `True());
    assertTrue(match9_1(`f(a(),a())) == `False());		
  }

  private Result match10(Term subject){
    %match(Term subject){
      f(x,!x) ->{
        return `Equal("x",x);
      }
    }
    return `False();
  }

  @Test
  public void testAp10() {
    assertTrue(match10(`f(a(),a())) == `False());
    assertTrue(match10(`f(a(),b())) == `Equal("x",a()));				
  }

  private Result match11(Term subject){
    %match(Term subject){
      f(!g(x),!g(x)) ->{
        return `True();
      }
    }
    return `False();
  }

  @Test
  public void testAp11() {
    assertTrue(match11(`f(g(a()),g(b()))) == `False());						
  }	

  private Result match12(Term subject){
    %match(Term subject){
      !f(!g(x),!g(x)) ->{
        return `True();
      }
    }
    return `False();
  }

  @Test
  public void testAp12() {		
    assertTrue(match12(`f(g(a()),g(b()))) == `True());						
  }

  private Result match13(Term subject){
    %match(Term subject){
      f(!x,!x) ->{
        return `True();
      }
    }
    return `False();
  }

  @Test
  public void testAp13() {		
    assertTrue(match13(`f(a(),a())) == `False());
    assertTrue(match13(`f(a(),b())) == `False());						
  }

  private Result match14(Term subject){
    %match(Term subject){
      !!_x ->{
        return `True();
      }
    }
    return `False();
  }

  private Result match15(Term subject){
    %match(Term subject){
      f(_x,!_y) ->{
        return `True();
      }
    }
    return `False();
  }

  @Test
  public void testAp15() {		
    assertTrue(match15(`f(a(),a())) == `False());
    assertTrue(match15(`f(a(),b())) == `False());
    assertTrue(match15(`b()) == `False());
  }

  private Result match16(Term subject){
    %match(Term subject){
      f(x,f(a(),f(y,!g(x)))) ->{
        return `And(Equal("x",x),Equal("y",y));
      }
    }
    return `False();
  }


  @Test
  public void testAp16() {
    assertTrue(match16(`f(b(),f(a(),f(a(),g(b()))))) == `False());
    assertTrue(match16(`f(b(),f(a(),f(a(),g(a()))))) == `And(Equal("x",b()),Equal("y",a())));
    assertTrue(match16(`f(a(),f(a(),f(b(),b())))) == `And(Equal("x",a()),Equal("y",b())));
    assertTrue(match16(`f(b(),f(a(),b()))) == `False());
  }

  private Result match17(Term subject){
    %match(Term subject){
      i(!1) ->{
        return `True(); 
      }
    }
    return `False();
  }

  @Test
  public void testAp17() {		
    assertTrue(match17(`i(1)) == `False());
    assertTrue(match17(`i(2)) == `True());
  }

  private Result match18(Term subject){
    %match(Term subject){
      i(x@!1) ->{
        return `Equal("j(x)",j(x)); 
      }
    }
    return `False();
  }

  @Test
  public void testAp18() {		
    assertTrue(match18(`i(1)) == `False());
    assertTrue(match18(`i(2)) == `Equal("j(x)",j(2)));
  }

  private Result match19(Term subject){
    %match(Term subject){
      ff(i(x@!1),i(x)) ->{
        return `Equal("j(x)",j(x)); 
      }
    }
    return `False();
  }

  @Test
  public void testAp19() {		
    assertTrue(match19(`ff(i(1),i(2))) == `False());
    assertTrue(match19(`ff(i(2),i(2))) == `Equal("j(x)",j(2)));
  }

  private Result match20(Term subject){
    %match(Term subject){
      ff(x@!a(),x) ->{
        return `Equal("x",x); 
      }
    }
    return `False();
  }

  @Test
  public void testAp20() {		
    assertTrue(match20(`ff(a(),a())) == `False());
    assertTrue(match20(`ff(a(),b())) == `False());
    assertTrue(match20(`ff(b(),b())) == `Equal("x",b()));
  }
}
