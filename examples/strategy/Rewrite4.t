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

import strategy.term.*;
import strategy.term.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.Identity;
import tom.library.strategy.mutraveler.MuStrategy;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;

import java.util.*;

public class Rewrite4 {

  %include { term/term.tom }
  %include { mustrategy.tom }
  %include { java/util/types/Collection.tom }

  %typeterm Position {
    implement {Position}
  }

  public final static void main(String[] args) {
    Rewrite4 test = new Rewrite4();
    test.run();
  }

  private Term globalSubject = null;
  public void run() {
    //Term subject = `g(d(),d());
    //Term subject = `f(g(g(a(),b()),g(a(),a())));
    Term subject = `h(h(a(),a(),a()),h(a(),b(),c()),h(g(c(),d()),a(),f(a())));
    globalSubject = subject;

    // find all leaf nodes positions
    Collection leaves = new HashSet();
    try {
      MuStrategy getleaves = `FindLeaves(leaves);
      MuTraveler.init(`BottomUp(getleaves)).visit(subject);
    } catch (VisitFailure e) {
      System.out.println("Failed to get leaves" + subject);
    }

    Iterator it = leaves.iterator();
    while(it.hasNext()) {
      Position p = (Position)it.next();

      MuStrategy s1 = `S1();
      MuStrategy s2 = `S2();
      MuStrategy eqPos = `EqPos(p);
      MuStrategy subPos = `SubPos(p);

      MuStrategy xmastree = `mu(MuVar("x"),
          Sequence(s1,
            All(IfThenElse(eqPos,s2,IfThenElse(subPos,MuVar("x"),s1)))));

      MuStrategy useOmegaPath = (MuStrategy)p.getOmegaPath(`Sequence(s2,All(s1)));

      System.out.println("----------------------");
      System.out.println("subject       = " + subject);
      System.out.println("position      = " + p);
      System.out.println("xmastree = " + xmastree.apply(subject));
      System.out.println("omegapath = " + useOmegaPath.apply(subject));
    }
  }

  %strategy S1() extends `Identity() { 
    visit Term {
      subject -> {
        int depth = getPosition().depth();
        String offset = "";
        for (int i = 0; i<depth; i++){
          offset += "  ";
        }
        System.out.println(offset + "s1: "+ `subject.symbolName() + " position: "+ getPosition());
      }
    }
  }

  %strategy S2() extends `Identity() { 

    visit Term {
      subject -> {
        int depth = getPosition().depth();
        String offset = "";
        for (int i = 0; i<depth; i++){
          offset += "--";
        }
        System.out.println(offset + "> s2: "+ `subject.symbolName() + " position: "+ getPosition());
      }
    }
  }

  %strategy FindLeaves(bag:Collection) extends `Identity() { 

    visit Term {
      subject -> {
        if (`subject.getChildCount() == 0) {
          bag.add(getPosition());
        }
      }
    }
  }

  %strategy EqPos(p:Position) extends `Fail() { 

    visit Term {
      subject -> {
        if (getPosition().equals(p)) {
          return `subject;
        }
      }
    }
  }

  %strategy SubPos(p:Position) extends `Fail() {

    visit Term {
      subject-> {
        if (getPosition().isPrefix(p)) {
          return `subject;
        } 
      }
    }
  }
}
