/*
 * Copyright (c) 2004, INRIA
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

public class Rewrite1 {
  private Factory factory;
  public Rewrite1(Factory factory) {
    this.factory = factory;
  }
  public Factory getTermFactory() {
    return factory;
  }

  %include { term.tom }

  public final static void main(String[] args) {
    Rewrite1 test = new Rewrite1(Factory.getInstance(new PureFactory()));
    test.run();
  }

  public void run() {
    Term subject = `f(g(a,b));
    jjtraveler.Visitor rule = new RewriteSystem();
    jjtraveler.Visitor bottomUp = new jjtraveler.BottomUp(new jjtraveler.Try(rule));
    jjtraveler.Visitor onceBottomUp = new jjtraveler.OnceBottomUp(rule);

    try {
      System.out.println("subject        = " + subject);
      System.out.println("oneceBottomUp  = " + onceBottomUp.visit(subject));
      System.out.println("bottomUp       = " + bottomUp.visit(subject));
    } catch (jjtraveler.VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }

  }
  
  class RewriteSystem extends strategy.term.Fwd {
    public RewriteSystem() {
      //super(new jjtraveler.Identity());
      super(new jjtraveler.Fail());
    }
    
    public Term visit_Term(Term arg) throws jjtraveler.VisitFailure { 
      %match(Term arg) {
        a() -> { return `b(); }
        b() -> { return `c(); }
      }
      throw new jjtraveler.VisitFailure();
    }
    


  }


}

 




