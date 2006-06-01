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

  %include { adt/tnode/TNode.tom }
  %include { mutraveler.tom }

  %typeterm Collection {
    implement { java.util.Collection }
  }

  %typeterm Position {
    implement { tom.library.strategy.mutraveler.Position }
  }

  public final static void main(String[] args) {
    RewriteXml test = new RewriteXml();
    test.run();
  }

  public void run() {

    xtools = new XmlTools();
    TNode subject = (TNode)xtools.convertXMLToTNode("strategy/minimenu.xml");
    subject = subject.getdocElem();
    Collection leaves = new HashSet();

    try {
      VisitableVisitor findLeaves = `FindLeaves(leaves);
      MuTraveler.init(`BottomUp(findLeaves)).visit(subject);
    } catch (VisitFailure e) {
      System.out.println("Failed to get leaves" + subject);
    }
    System.out.println("bag: "+leaves);

    Iterator it = leaves.iterator();
    while(it.hasNext()) {
      Position p = (Position)it.next();

      VisitableVisitor s1 = `S1();
      VisitableVisitor s2 = `S2();
      VisitableVisitor eqPos = `EqPos(p);
      VisitableVisitor subPos = `SubPos(p);

      VisitableVisitor xmastree = `mu(MuVar("x"),
          All(IfThenElse(eqPos,s2,IfThenElse(subPos,MuVar("x"),s1))));

      try {
        System.out.println("----------------------");
        System.out.println("position      = " + p);
        xtools.printXMLFromTNode((TNode)MuTraveler.init(xmastree).visit(subject));
        System.out.println("-----------------------");
      } catch (VisitFailure e) {
        System.out.println("reduction failed on: " + subject);
      }
    }
  }

  %strategy S1() extends `Identity() {

    visit TNode {
      <section><title_fr>#TEXT(title)</title_fr></section> -> {
        // prune the sub-lists: we keep only the title
        return `xml(<section><title_fr>#TEXT(title)</title_fr></section>);
      }
      <subsection><title_fr>#TEXT(title)</title_fr></subsection> -> {
        // prune the sub-lists: we keep only the title
        return `xml(<subsection><title_fr>#TEXT(title)</title_fr></subsection>);
      }
    }
  }

  %strategy S2() extends `Identity() {

    visit TNode {
      arg -> { return `xml(<hilight>`arg</hilight>); }
    }
  }

  %strategy FindLeaves(c:Collection) extends `Identity() {

    visit TNode {
      <subsection></subsection> -> { c.add(MuTraveler.getPosition(this));}
    }
  }

  %strategy EqPos(p:Position) extends `Fail(){

    visit TNode {
      arg -> {
        if (MuTraveler.getPosition(this).equals(p)) {
          return `arg;
        }
      }
    }

    visit TNodeList {
      arg -> {
        if (MuTraveler.getPosition(this).equals(p)) {
          return `arg;
        } 
      }
    }
  }

  %strategy SubPos(p:Position) extends `Fail() {

    visit TNode {
      arg -> {
        if (MuTraveler.getPosition(this).isPrefix(p)) {
          return `arg;
        } 
      }
    }

    visit TNodeList {
      arg -> {
        if (MuTraveler.getPosition(this).isPrefix(p)) {
          return `arg;
        } 
      }
    }
  }
}

