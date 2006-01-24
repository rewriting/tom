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

import strategy.proba2.state.*;
import strategy.proba2.state.types.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

public class Proba2 {
  private StateFactory factory;

  public Proba2(StateFactory factory) {
    this.factory = factory;
  }

  %vas {
    // extension of adt syntax
    module State
    imports 
    public
      sorts State
      
    abstract syntax
      a -> State
      b -> State
      c -> State
      puits -> State
   }

  %include { mutraveler.tom }

  public StateFactory getStateFactory() {
    return factory;
  }

  public final static void main(String[] args) {
    Proba2 test = new Proba2(StateFactory.getInstance(SingletonFactory.getInstance()));
    test.run();
  }

  public void run() {
    State subject = `a();

    VisitableVisitor AB = new Transition(`a(),`b());
    VisitableVisitor AC = new Transition(`a(),`c());

    VisitableVisitor BA = new Transition(`b(),`a());
    VisitableVisitor BC = new Transition(`b(),`c());;
    VisitableVisitor BPuits = new Transition(`b(),`puits());

    VisitableVisitor CA = new Transition(`c(),`a());
    VisitableVisitor CB = new Transition(`c(),`b());
    VisitableVisitor CPuits = new Transition(`c(),`puits());

    VisitableVisitor transitA = `Pselect(5,10,AC,AB);
    VisitableVisitor transitB = `Pselect(8,10,BC,Pselect(1,50,BPuits,BA));
    VisitableVisitor transitC = `Pselect(9,10,CA,Pselect(1,60,CPuits,CB));

    //VisitableVisitor transitC = `Pchoice(concStrat(Pstrat(4,CA),Pstrat(1,CPuits),Pstrat(5,CB)));

    VisitableVisitor transit  = `Repeat(Choice(transitA,Choice(transitB,transitC)));
    

    try {
      System.out.println("subject       = " + subject);
      State s = (State) transit.visit(subject);
      System.out.println("s = " + s);
    } catch(VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }

  }

  class Transition extends strategy.proba2.state.StateVisitableFwd {
    State s1;
    State s2;
    public Transition(State s1, State s2) { 
      super(`Fail()); 
      this.s1 = s1;
      this.s2 = s2;
    }

    public State visit_State(State state) throws VisitFailure { 
      if(state == s1) {
        System.out.println(s1 + " -> " + s2); 
        return s2; 
      }
      throw new VisitFailure();
    }
  }

}

 




