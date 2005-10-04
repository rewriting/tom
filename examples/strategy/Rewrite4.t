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

import aterm.pure.SingletonFactory;
import strategy.term.*;
import strategy.term.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.Identity;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;

import java.util.*;

public class Rewrite4 {
  private termFactory factory;

  public Rewrite4(termFactory factory) {
    this.factory = factory;
  }

  public termFactory getTermFactory() {
    return factory;
  }

  %include { term/term.tom }
  %include { mutraveler.tom }
  
  public final static void main(String[] args) {
    Rewrite4 test = new Rewrite4(termFactory.getInstance(SingletonFactory.getInstance()));
    test.run();
  }

  private Term globalSubject = null;
  public void run() {
    //Term subject = `g(d,d);
    Term subject = `f(g(g(a,b),g(a,a)));
    globalSubject = subject;

    // find all leaf nodes
    Collection leaves = new HashSet();
    try {
      VisitableVisitor getleaves = new FindLeaves();
      MuTraveler.init(`BottomUp(getleaves)).visit(subject);
      leaves = ((FindLeaves)getleaves).getLeaves();
    } catch (VisitFailure e) {
      System.out.println("Failed to get leaves" + subject);
    }
    System.out.println("bag: "+leaves);

    Iterator it = leaves.iterator();
    while(it.hasNext()) {
      Position p = (Position)it.next();

      VisitableVisitor s1 = new S1();
      VisitableVisitor s2 = new S2();
      VisitableVisitor eqPos = new EqPos(p);
      VisitableVisitor subPos = new SubPos(p);

      VisitableVisitor xmastree = `mu(MuVar("x"),
          Sequence(s1,
            All(IfThenElse(eqPos,s2,IfThenElse(subPos,MuVar("x"),s1)))));

      try {
        System.out.println("----------------------");
        System.out.println("subject       = " + subject);
        System.out.println("position      = " + p);
        System.out.println("xmastree = " + MuTraveler.init(xmastree).visit(subject));
      } catch (VisitFailure e) {
        System.out.println("reduction failed on: " + subject);
      }
    }
  }

  class S1 extends strategy.term.termVisitableFwd {
    public S1() {
      super(`Identity());
    }
    public Term visit_Term(Term arg) throws VisitFailure { 
      System.out.println("s1: "+ arg.getName());
      System.out.println("s1: position: "+ MuTraveler.getPosition(this));
      return arg;
    }
  }
  class S2 extends strategy.term.termVisitableFwd {
    public S2() {
      super(`Identity());
    }
    public Term visit_Term(Term arg) throws VisitFailure { 
      System.out.println("s2: "+ arg.getName());
      System.out.println("s2: position: "+ MuTraveler.getPosition(this));
      return arg;
    }
  }
  class FindLeaves extends strategy.term.termVisitableFwd {
    Collection bag;
    public FindLeaves() {
      super(`Identity());
      bag = new HashSet();
    }
    public Collection getLeaves() {
      return bag;
    }
    public Term visit_Term(Term arg) throws VisitFailure { 
      %match(Term arg) {
        a() -> { bag.add(MuTraveler.getPosition(this));}
        b() -> { bag.add(MuTraveler.getPosition(this));}
        c() -> { bag.add(MuTraveler.getPosition(this));}
        d() -> { bag.add(MuTraveler.getPosition(this));}
      }
      return arg;
    }
  }

  class EqPos extends strategy.term.termVisitableFwd {
    Position p;
    public EqPos(Position p) {
      super(`Fail());
      this.p = p;
    }
    public Term visit_Term(Term arg) throws VisitFailure { 
      if (MuTraveler.getPosition(this).equals(p)) {
        return arg;
      } else {
        return (Term)`Fail().visit(arg);
      }
    }
  }
  class SubPos extends strategy.term.termVisitableFwd {
    Position p;
    public SubPos(Position p) {
      super(`Fail());
      this.p = p;
    }
    public Term visit_Term(Term arg) throws VisitFailure { 
      if (MuTraveler.getPosition(this).isPrefix(p)) {
        return arg;
      } else {
        return (Term)`Fail().visit(arg);
      }
    }
  }

}
