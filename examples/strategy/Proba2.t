/*
 * Copyright (c) 2004-2011, INPL, INRIA
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

import strategy.proba2.state.*;
import strategy.proba2.state.types.*;
import tom.library.sl.*;

public class Proba2 {
  %gom {
    // extension of adt syntax
    module State
    abstract syntax
      State = a()
            | b()
            | c()
            | puits()
   }

  %include { sl.tom }

  public final static void main(String[] args) {
    State subject = `a();

    Strategy AB = new Transition(`a(),`b());
    Strategy AC = new Transition(`a(),`c());

    Strategy BA = new Transition(`b(),`a());
    Strategy BC = new Transition(`b(),`c());;
    Strategy BPuits = new Transition(`b(),`puits());

    Strategy CA = new Transition(`c(),`a());
    Strategy CB = new Transition(`c(),`b());
    Strategy CPuits = new Transition(`c(),`puits());

    Strategy transitA = `Pselect(5,10,AC,AB);
    Strategy transitB = `Pselect(8,10,BC,Pselect(1,50,BPuits,BA));
    Strategy transitC = `Pselect(9,10,CA,Pselect(1,60,CPuits,CB));

    Strategy transit  = `Repeat(Choice(transitA,Choice(transitB,transitC)));
    

    try {
      System.out.println("subject       = " + subject);
      State s = (State) transit.visitLight(subject);
      System.out.println("s = " + s);
    } catch(VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }
  }

  %strategy Transition(s1:State,s2:State) extends `Fail() { 
    visit State {
      state -> {
        if(`state == s1) {
          System.out.println(s1 + " -> " + s2); 
          return s2; 
        }      
      }
    }
  }
}
