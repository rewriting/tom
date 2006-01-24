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

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.Identity;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;

import java.util.*;

public class RewriteXml {

  private XmlTools xtools;
  private TNode globalSubject;

  private TNodeFactory getTNodeFactory() {
    return xtools.getTNodeFactory();
  }

  %include { adt/tnode/TNode.tom }
  %include { mutraveler.tom }
  
  public final static void main(String[] args) {
    RewriteXml test = new RewriteXml();
    test.run();
  }

  public void run() {

    xtools = new XmlTools();
    TNode subject = (TNode)xtools.convertXMLToATerm("strategy/minimenu.xml");
    subject = subject.getDocElem();

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
          All(IfThenElse(eqPos,s2,IfThenElse(subPos,MuVar("x"),s1))));

      try {
        System.out.println("----------------------");
        System.out.println("position      = " + p);
        xtools.printXMLFromATerm((TNode)MuTraveler.init(xmastree).visit(subject));
        System.out.println("-----------------------");
      } catch (VisitFailure e) {
        System.out.println("reduction failed on: " + subject);
      }
    }
  }
  class S1 extends TNodeVisitableFwd {
    public S1() {
      super(`Identity());
    }
    public TNode visit_TNode(TNode arg) throws VisitFailure { 
      %match(TNode arg) {
        <section><title_fr>#TEXT(title)</title_fr></section> -> {
          // prune the sub-lists: we keep only the title
          return `xml(<section><title_fr>#TEXT(title)</title_fr></section>);
        }
        <subsection><title_fr>#TEXT(title)</title_fr></subsection> -> {
          // prune the sub-lists: we keep only the title
          return `xml(<subsection><title_fr>#TEXT(title)</title_fr></subsection>);
        }
      }
      return arg;
    }
  }
  class S2 extends TNodeVisitableFwd {
    public S2() {
      super(`Identity());
    }
    public TNode visit_TNode(TNode arg) throws VisitFailure { 
      return `xml(<hilight>arg</hilight>);
    }
  }

   class FindLeaves extends TNodeVisitableFwd {
    Collection bag;
    public FindLeaves() {
      super(`Identity());
      bag = new HashSet();
    }
    public Collection getLeaves() {
      return bag;
    }
    public TNode visit_TNode(TNode arg) throws VisitFailure { 
      %match(TNode arg) {
        <subsection></subsection> -> { bag.add(MuTraveler.getPosition(this));}
      }
      return arg;
    }
  }
 
  class EqPos extends TNodeVisitableFwd {
    Position p;
    public EqPos(Position p) {
      super(`Fail());
      this.p = p;
    }
    public TNode visit_TNode(TNode arg) throws VisitFailure { 
      if (MuTraveler.getPosition(this).equals(p)) {
        return arg;
      } else {
        return (TNode)`Fail().visit(arg);
      }
    }
    public TNodeList visit_TNodeList(TNodeList arg) throws VisitFailure { 
      if (MuTraveler.getPosition(this).equals(p)) {
        return arg;
      } else {
        return (TNodeList)`Fail().visit(arg);
      }
    }
  }
  class SubPos extends TNodeVisitableFwd {
    Position p;
    public SubPos(Position p) {
      super(`Fail());
      this.p = p;
    }
    public TNode visit_TNode(TNode arg) throws VisitFailure { 
      if (MuTraveler.getPosition(this).isPrefix(p)) {
        return arg;
      } else {
        return (TNode)`Fail().visit(arg);
      }
    }
    public TNodeList visit_TNodeList(TNodeList arg) throws VisitFailure { 
      if (MuTraveler.getPosition(this).isPrefix(p)) {
        return arg;
      } else {
        return (TNodeList)`Fail().visit(arg);
      }
    }
  }

}
