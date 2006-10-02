/*
 * Copyright (c) 2004-2006, INRIA
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

//package strategy;

//import strategy.term.*;
//import strategy.term.types.*;

import rewrite1.term.*;
import rewrite1.term.types.*;

import tom.library.sl.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;

public class Rewrite1 {
%include { string.tom }
%include { sl.tom }
%gom {  
  module term
  abstract syntax
  Term = a() | b() | c() | d()
       | f(arg1:Term)
       | g(arg1:Term, arg2:Term)
       | h(arg1:Term, arg2:Term, arg3:Term)
}

  public final static void main(String[] args) {
    jjtraveler.Visitable subject = `f(a());
    GraphStrategy s = `Identity();
    subject = s.gapply(subject);
    System.out.println("root = " + subject);

    s = `Fire(R1());
    subject = `s.gapply(subject);
    System.out.println("root = " + subject);

    s = `One(Fire(R1()));
    subject = `s.gapply(subject);
    System.out.println("root = " + subject);
  
    System.out.println("----------");
    s = `OnceBottomUp(Fire(R2()));
    subject = `s.gapply(`g(f(a()),b()));
    System.out.println("root1 = " + subject);
    subject = `s.gapply(subject);
    System.out.println("root2 = " + subject);
    subject = `s.gapply(subject);
    System.out.println("root3 = " + subject);
  }

  %strategy R1() extends `MuIdentity() {
    visit Term {
      f(a()) -> { return `f(b()); }
      b() -> { return `c(); }
    }
  }

  %strategy R2() extends `MuFail() {
    visit Term {
      f(a()) -> { return `f(b()); }
      b() -> { return `c(); }
    }
  }
} 
