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

public class Rewrite3 {
  private Factory factory;
  public Rewrite3(Factory factory) {
    this.factory = factory;
  }
  public Factory getTermFactory() {
    return factory;
  }

  %include { term.tom }
  %include { string.tom }
 
  %typeterm Visitor {
    implement { jjtraveler.Visitor}
    get_fun_sym(t) {null}
    cmp_fun_sym(s1,s2) { false}
    get_subterm(t,n) {null}
    equals(t1,t2) {t1.equals(t2)}
  }

  %op Visitor Identity {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableIdentity) }
    make() { new jjtraveler.reflective.VisitableIdentity() }
  }

  %op Visitor Fail {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableFail) }
    make() { new jjtraveler.reflective.VisitableFail() }
  }

  %op Visitor Not(v:Visitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableNot) }
    make(v) { new jjtraveler.reflective.VisitableNot((jjtraveler.reflective.VisitableVisitor)v) }
  }

  %op Visitor Sequence(first:Visitor, then:Visitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableSequence) }
    make(v1,v2) { new jjtraveler.reflective.VisitableSequence((jjtraveler.reflective.VisitableVisitor)v1,(jjtraveler.reflective.VisitableVisitor)v2) }
  }

  %op Visitor Choice(first:Visitor, then:Visitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableChoice) }
    make(v1,v2) { new jjtraveler.reflective.VisitableChoice((jjtraveler.reflective.VisitableVisitor)v1,(jjtraveler.reflective.VisitableVisitor)v2) }
  }

  %op Visitor All(v:Visitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableAll) }
    make(v) { new jjtraveler.reflective.VisitableAll((jjtraveler.reflective.VisitableVisitor)v) }
  }

  %op Visitor One(v:Visitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableOne) }
    make(v) { new jjtraveler.reflective.VisitableOne((jjtraveler.reflective.VisitableVisitor)v) }
  }

  %op Visitor IfThenElse(condition:Visitor, trueCase:Visitor, falseCase:Visitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableIfThenElse) }
    make(v1,v2,v3) { new jjtraveler.reflective.VisitableIfThenElse((jjtraveler.reflective.VisitableVisitor)v1,(jjtraveler.reflective.VisitableVisitor)v2,(jjtraveler.reflective.VisitableVisitor)v3) }
  }

  %op Visitor MuVar(name:String) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableMuVar) }
    make(name) { new jjtraveler.reflective.VisitableMuVar(name) }
  }


  jjtraveler.Visitor Try(jjtraveler.Visitor v) {
    return `Choice(v,Identity);
  }

  jjtraveler.Visitor BottomUp(jjtraveler.Visitor v) {
    // BottomUp(v) = Sequence(All(BottomUp(v)),v)
    // BottomUp(v) = mu(x,Sequence(All(x),v))
    jjtraveler.Visitor x = `MuVar(genName());
    return `mu(x,Sequence(All(x),v));
  }

  jjtraveler.Visitor OnceBottomUp(jjtraveler.Visitor v) {
    // BottomUp(v) = mu(x,Choice(One(x),v))
    jjtraveler.Visitor x = `MuVar(genName());
    return `mu(x,Choice(One(x),v));
  }

  jjtraveler.Visitor Innermost(jjtraveler.Visitor v) {
    // Innermost(v) = mu(x,Sequence(All(x),Choice(Sequence(v,x),Identity)))
    jjtraveler.Visitor x = `MuVar(genName());
    return `mu(x,Sequence(All(x),Choice(Sequence(v,x),Identity)));
  }

  jjtraveler.Visitor Repeat(jjtraveler.Visitor v) {
    // Repeat(v) = mu(x,Choice(Sequence(v,x),Identity))
    jjtraveler.Visitor x = `MuVar(genName());
    return `mu(x,Choice(Sequence(v,x),Identity()));
  }

  jjtraveler.Visitor mu(jjtraveler.Visitor var, jjtraveler.Visitor v) {
    try {
      jjtraveler.Visitor muExpander = new jjtraveler.BottomUp(new MuExpander((jjtraveler.reflective.VisitableVisitor)var,(jjtraveler.reflective.VisitableVisitor)v));
      return (jjtraveler.Visitor) muExpander.visit((jjtraveler.reflective.VisitableVisitor)v);
    } catch (jjtraveler.VisitFailure e) {
      System.out.println("mu reduction failed");
    }
    return v;
  }
  
  class MuExpander extends jjtraveler.reflective.VisitorFwd {
    jjtraveler.reflective.VisitableVisitor muVar;
    jjtraveler.reflective.VisitableVisitor instance;
    public MuExpander(jjtraveler.reflective.VisitableVisitor muVar, jjtraveler.reflective.VisitableVisitor instance) {
      super(new jjtraveler.reflective.VisitableFail());
      this.muVar = muVar;
      this.instance = instance;
    }
    
    public jjtraveler.reflective.VisitableVisitor visitVisitor(jjtraveler.reflective.VisitableVisitor v) throws jjtraveler.VisitFailure { 
      if(v instanceof jjtraveler.reflective.VisitableMuVar) {
        jjtraveler.reflective.VisitableMuVar visitableV = (jjtraveler.reflective.VisitableMuVar)v;
        jjtraveler.reflective.VisitableMuVar visitableMuVar = (jjtraveler.reflective.VisitableMuVar)muVar;
        if(visitableV.equals(visitableMuVar)) {
          visitableV.setInstance(instance);
        } 
      }
      return v;
    }
  }
  
  private int nameCounter=0;
  String genName() {
    nameCounter++;
    return getName();
  }
  String getName() {
    return "x" + nameCounter;
  }
  public final static void main(String[] args) {
    Rewrite3 test = new Rewrite3(Factory.getInstance(new PureFactory()));
    test.run();
  }

  public void run() {
    //Term subject = `f(g(a,b));
    Term subject = `f(g(g(a,b),g(a,a)));

    jjtraveler.Visitor rule = new RewriteSystem();
    jjtraveler.Visitor onceBottomUp = new jjtraveler.OnceBottomUp(rule);
    jjtraveler.Visitor bottomUp = `BottomUp(Try(rule));
    jjtraveler.Visitor innermost = `Innermost(rule);
    jjtraveler.Visitor innermostSlow = `Repeat(OnceBottomUp(rule));

    try {
      System.out.println("subject       = " + subject);
      System.out.println("onceBottomUp  = " + onceBottomUp.visit(subject));
      System.out.println("bottomUp      = " + bottomUp.visit(subject));
      System.out.println("innermost     = " + innermost.visit(subject));
      System.out.println("innermostSlow = " + innermostSlow.visit(subject));
    } catch (jjtraveler.VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }

  }
  
  class RewriteSystem extends strategy.term.VisitableFwd {
    public RewriteSystem() {
      super(new jjtraveler.reflective.VisitableFail());
    }
    
    public Term visit_Term(Term arg) throws jjtraveler.VisitFailure { 
      %match(Term arg) {
        a() -> { return `b(); }
        b() -> { return `c(); }
        g(c(),c()) -> { return `c(); }
      }
      throw new jjtraveler.VisitFailure();
    }
    


  }


}

 




