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

package strategy;

import aterm.pure.SingletonFactory;
import strategy.term.*;
import strategy.term.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.Identity;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;

public class MuStrategy2 {
  private termFactory factory;

  public MuStrategy2(termFactory factory) {
    this.factory = factory;
  }

  public termFactory getTermFactory() {
    return factory;
  }

  %include { term/term.tom }
  %include { mutraveler.tom }
  
  public final static void main(String[] args) {
    MuStrategy2 test = new MuStrategy2(termFactory.getInstance(SingletonFactory.getInstance()));
    test.run();
  }

  private Term globalSubject = null;
  public void run() {
    //Term subject = `g(d,d);
    Term subject = `g(g(a,a),g(a,b));
    globalSubject = subject;

    VisitableVisitor rule = new RewriteSystem();

    try {
      System.out.println("subject          = " + subject);
      System.out.println("innermost        = " + MuTraveler.init(`Innermost(rule)).visit(subject));
      System.out.println("innermostSlow    = " + MuTraveler.init(`Repeat(OnceBottomUp(rule))).visit(subject));
      System.out.println("Repeat(BottomUp) = " + MuTraveler.init(`Repeat(BottomUp(rule))).visit(subject));
    } catch (VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }

  }
  
  class RewriteSystem extends strategy.term.termVisitableFwd {
    public RewriteSystem() {
      super(`Fail());
    }
    
    public Term visit_Term(Term arg) throws VisitFailure { 


      %match(Term arg) {
        a() -> { return `b(); }
        g(b(),a()) -> { return `c(); }
      }
      return (Term)`Fail().visit(arg);
      //throw new VisitFailure();
    }
  }
}

 




