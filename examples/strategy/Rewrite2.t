/*
 * Copyright (c) 2004-2005, INRIA
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

import aterm.*;
import aterm.pure.PureFactory;
import strategy.term.*;
import strategy.term.types.*;

import tom.library.strategy.mutraveler.TravelerFactory;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

public class Rewrite2 {
  private Factory factory;
  private TravelerFactory travelerFactory;

  public Rewrite2(Factory factory) {
    this.factory = factory;
  }

  public Factory getTermFactory() {
    return factory;
  }

  %include { term.tom }
  %include { string.tom }
  %include { mutraveler.tom }

  public VisitableVisitor mu(VisitableVisitor var, VisitableVisitor v) {
    return tom.library.strategy.mutraveler.MuVar.mu(var,v);
  }

  public final static void main(String[] args) {
    Rewrite2 test = new Rewrite2(Factory.getInstance(new PureFactory()));
    test.run();
  }

  public void run() {
    //Term subject = `g(c,c);
    Term subject = `f(g(g(a,b),g(a,a)));

    VisitableVisitor rule = new RewriteSystem();
    VisitableVisitor onceBottomUp = `mu(MuVar("x"),Choice(One(MuVar("x")),rule));
    VisitableVisitor innermostSlow = `mu(MuVar("y"),Choice(Sequence(onceBottomUp,MuVar("y")),Identity()));
    VisitableVisitor innermost = `mu(MuVar("x"),Sequence(All(MuVar("x")),Choice(Sequence(rule,MuVar("x")),Identity)));
    try {
      System.out.println("subject       = " + subject);
      System.out.println("onceBottomUp  = " + onceBottomUp.visit(subject));
      System.out.println("innermostSlow = " + innermostSlow.visit(subject));
      System.out.println("innermost     = " + innermost.visit(subject));
    } catch(VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }


  }
  
  class RewriteSystem extends strategy.term.VisitableFwd {
    public RewriteSystem() {
      super(new tom.library.strategy.mutraveler.Fail());
    }
    
    public Term visit_Term(Term arg) throws VisitFailure { 
      %match(Term arg) {
        a() -> { return `b(); }
        b() -> { return `c(); }
        g(c(),c()) -> { return `c(); }
      }
      throw new VisitFailure();
    }
    


  }


}

 




