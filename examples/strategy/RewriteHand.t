/*
 * Copyright (c) 2006, INRIA
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

import strategy.hand.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.Identity;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;

public class RewriteHand {

  %include { hand/Hand.tom }
  %include { mutraveler.tom }
  
  public final static void main(String[] args) {
    RewriteHand test = new RewriteHand();
    test.run();
  }

  private Term globalSubject = null;
  public void run() {
    Term subject = `F(F(F(A(),B()),F(G(Name("a")),A())),B());
    globalSubject = subject;

    System.out.println(subject);
    try {
      MuTraveler.init(`TopDown(Print())).visit(subject);
      System.out.println();

      VisitableVisitor rule = `RewriteSystem();
      MuTraveler.init(
          `Sequence(OnceBottomUp(rule),TopDown(Print()))
          ).visit(subject);
      System.out.println();
      System.out.println();
      MuTraveler.init(
          `Sequence(BottomUp(Try(rule)),TopDown(Print()))
          ).visit(subject);
      System.out.println();
      MuTraveler.init(
          `Sequence(Innermost(rule),TopDown(Print()))
          ).visit(subject);
      System.out.println();
      MuTraveler.init(
          `Sequence(Repeat(OnceBottomUp(rule)),TopDown(Print()))
          ).visit(subject);
      System.out.println();
    } catch (VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }
  }

  %strategy Print() extends `Identity() {
    visit Term {
      A[] -> { System.out.print("A "); }
      B[] -> { System.out.print("B "); }
      G[] -> { System.out.print("G "); }
      F[] -> { System.out.print("F "); }
    }
    visit Slot {
      Name(n) -> { System.out.print("Name("+`n+") "); }
    }
  }

  %strategy RewriteSystem() extends `Fail() {
    visit Term {
      A() -> { 
        Position pos = MuTraveler.getPosition(this);
        System.out.println("A -> B at " + pos);
        System.out.println(globalSubject + " at " + pos + " = " + pos.getSubterm().visit(globalSubject));
        System.out.println("rwr into: " + pos.getReplace(`B()).visit(globalSubject));

        return `B();
      }
      B() -> {
        System.out.println("B -> C at " + MuTraveler.getPosition(this));
        return `C();
      }
      F(C(),C()) -> {
        System.out.println("G(C,C) -> C at " + MuTraveler.getPosition(this));
        return `C(); }
    }
  }

} 
