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

public class Rewrite2 {
  private Factory factory;
  public Rewrite2(Factory factory) {
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
    is_fsym(t) { (t instanceof jjtraveler.Identity) }
    make() { new jjtraveler.Identity() }
  }

  %op Visitor Fail {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.Fail) }
    make() { new jjtraveler.Fail() }
  }

  %op Visitor Not(v:Visitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.Not) }
    make(v) { new jjtraveler.Not(v) }
  }

  %op Visitor Sequence(first:Visitor, then:Visitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.Sequence) }
    make(v1,v2) { new jjtraveler.Sequence(v1,v2) }
  }

  %op Visitor Choice(first:Visitor, then:Visitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.Choice) }
    make(v1,v2) { new jjtraveler.Choice(v1,v2) }
  }

  %op Visitor All(v:Visitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.All) }
    make(v) { new jjtraveler.All(v) }
  }

  %op Visitor One(v:Visitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.One) }
    make(v) { new jjtraveler.One(v) }
  }

  %op Visitor IfThenElse(condition:Visitor, trueCase:Visitor, falseCase:Visitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.IfThenElse) }
    make(v1,v2,v3) { new jjtraveler.IfThenElse(v1,v2,v3) }
  }

  %op Visitor MuVar(name:String) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.MuVar) }
    make(name) { new jjtraveler.MuVar(name) }
  }
  // ------------------------------------------------------------

  %typeterm VisitableVisitor {
    implement { jjtraveler.reflective.VisitableVisitor}
    get_fun_sym(t) {null}
    cmp_fun_sym(s1,s2) { false}
    get_subterm(t,n) {null}
    equals(t1,t2) {t1.equals(t2)}
  }

  %op VisitableVisitor VIdentity {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableIdentity) }
    make() { new jjtraveler.reflective.VisitableIdentity() }
  }

  %op VisitableVisitor VFail {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableFail) }
    make() { new jjtraveler.reflective.VisitableFail() }
  }

  %op VisitableVisitor VNot(v:VisitableVisitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableNot) }
    make(v) { new jjtraveler.reflective.VisitableNot(v) }
  }

  %op VisitableVisitor VSequence(first:VisitableVisitor, then:VisitableVisitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableSequence) }
    make(v1,v2) { new jjtraveler.reflective.VisitableSequence(v1,v2) }
  }

  %op VisitableVisitor VChoice(first:VisitableVisitor, then:VisitableVisitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableChoice) }
    make(v1,v2) { new jjtraveler.reflective.VisitableChoice(v1,v2) }
  }

  %op VisitableVisitor VAll(v:VisitableVisitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableAll) }
    make(v) { new jjtraveler.reflective.VisitableAll(v) }
  }

  %op VisitableVisitor VOne(v:VisitableVisitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableOne) }
    make(v) { new jjtraveler.reflective.VisitableOne(v) }
  }

  %op VisitableVisitor VIfThenElse(condition:VisitableVisitor, trueCase:VisitableVisitor, falseCase:VisitableVisitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableIfThenElse) }
    make(v1,v2,v3) { new jjtraveler.reflective.VisitableIfThenElse(v1,v2,v3) }
  }

  %op VisitableVisitor VMuVar(name:String) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.reflective.VisitableMuVar) }
    make(name) { new jjtraveler.reflective.VisitableMuVar(name) }
  }



 
  /*
  %op Visitor Try(first:Visitor) {
    fsym {}
    is_fsym(t) { (t instanceof jjtraveler.Try) }
    make(v) { new jjtraveler.Try(v) }
  }

  %rule{
    Try(v) -> VChoice(v,VIdentity)
  }
  */

  jjtraveler.Visitor Try(jjtraveler.Visitor v) {
    return `Choice(v,Identity);
  }

  jjtraveler.reflective.VisitableVisitor VTry(jjtraveler.reflective.VisitableVisitor v) {
    return `VChoice(v,VIdentity);
  }

  jjtraveler.reflective.VisitableVisitor VBottomUp(jjtraveler.reflective.VisitableVisitor v) {
    // BottomUp(v) = Sequence(All(BottomUp(v)),v)
    // BottomUp(v) = mu(x,Sequence(All(x),v))
    genName();
    return `mu(VMuVar("x"),VSequence(VAll(VMuVar("x")),v));
  }

  jjtraveler.reflective.VisitableVisitor VOnceBottomUp(jjtraveler.reflective.VisitableVisitor v) {
    // BottomUp(v) = mu(x,Choice(One(x),v))
    genName();
    return `mu(VMuVar("x"),VChoice(VOne(VMuVar("x")),v));
  }

  jjtraveler.reflective.VisitableVisitor VInnermost(jjtraveler.reflective.VisitableVisitor v) {
    // Innermost(v) = mu(x,Sequence(All(x),Choice(Sequence(v,x),Identity)))
    genName();
    return `mu(VMuVar("x"),VSequence(VAll(VMuVar("x")),VChoice(VSequence(v,VMuVar("x")),VIdentity())));
  }

  jjtraveler.reflective.VisitableVisitor VRepeat(jjtraveler.reflective.VisitableVisitor v) {
    // Repeat(v) = mu(x,Choice(Sequence(v,x),Identity))
    genName();
    return `mu(VMuVar("x"),VChoice(VSequence(v,VMuVar("x")),VIdentity()));
  }

  jjtraveler.reflective.VisitableVisitor mu(jjtraveler.reflective.VisitableVisitor var, jjtraveler.reflective.VisitableVisitor v) {
    try {
      jjtraveler.Visitor muExpander = new jjtraveler.BottomUp(new MuExpander(var,v));
      return (jjtraveler.reflective.VisitableVisitor) muExpander.visit(v);
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
        jjtraveler.reflective.VisitableMuVar vmv1 = (jjtraveler.reflective.VisitableMuVar)v;
        jjtraveler.reflective.VisitableMuVar vmv2 = (jjtraveler.reflective.VisitableMuVar)muVar;
        if(vmv1.equals(vmv2)) {
          return instance;
        } 
      }
      return v;
    }
  }
  
  private int nameCounter=0;
  void genName() {
    nameCounter++;
  }
  String getName() {
    return "x" + nameCounter;
  }
  public final static void main(String[] args) {
    Rewrite2 test = new Rewrite2(Factory.getInstance(new PureFactory()));
    test.run();
  }

  public void run() {
    //Term subject = `f(g(a,b));
    Term subject = `f(g(g(a,b),g(a,a)));

    jjtraveler.reflective.VisitableVisitor rule = new RewriteSystem();
    jjtraveler.Visitor onceBottomUp = new jjtraveler.OnceBottomUp(rule);
    jjtraveler.Visitor bottomUp = `VBottomUp(VTry(rule));
    jjtraveler.Visitor innermost = `VInnermost(rule);

    /*
     * cannot expand nested mu construct
     */
    //jjtraveler.Visitor innermostSlow = `VRepeat(VOnceBottomUp(rule));

    try {
      System.out.println("subject       = " + subject);
      System.out.println("onceBottomUp  = " + onceBottomUp.visit(subject));
      System.out.println("bottomUp      = " + bottomUp.visit(subject));
      System.out.println("innermost     = " + innermost.visit(subject));
      //System.out.println("innermostSlow = " + innermostSlow.visit(subject));
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

 




