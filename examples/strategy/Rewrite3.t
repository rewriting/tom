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

public class Rewrite3 {
  private termFactory factory;
  private TravelerFactory travelerFactory;

  public Rewrite3(termFactory factory, TravelerFactory travelerFactory) {
    this.factory = factory;
    this.travelerFactory = travelerFactory;
  }

  public termFactory getTermFactory() {
    return factory;
  }

  %include { term/term.tom }
  %include { string.tom }
  %include { mutraveler.tom }

  public VisitableVisitor mu(VisitableVisitor var, VisitableVisitor v) {
    return tom.library.strategy.mutraveler.MuVar.mu(var,v);
  }

  public final static void main(String[] args) {
    Rewrite3 test = new Rewrite3(termFactory.getInstance(new PureFactory()),new TravelerFactory());
    test.run();
  }

  public void run() {
    //Term subject = `g(c,c);
    Term subject = `f(g(g(a,b),g(c,c)));

    System.out.println("subject = " + subject);
    System.out.println("occurs  = " + occurs(subject));

  }
  
  private boolean occurs(Term subject) {
    VisitableVisitor rule = new RewriteSystem();
    VisitableVisitor onceBottomUp = travelerFactory.OnceBottomUp(rule);
    try {
      onceBottomUp.visit(subject);
      return true;
    } catch(VisitFailure e) {
      return false;
    }
  }

  class RewriteSystem extends strategy.term.termVisitableFwd {
    public RewriteSystem() {
      super(new tom.library.strategy.mutraveler.Fail());
    }
    
    public Term visit_Term(Term arg) throws VisitFailure { 
      %match(Term arg) {
        t@g(c(),c()) -> { return `t; }
      }
      throw new VisitFailure();
    }
    


  }


}

 




