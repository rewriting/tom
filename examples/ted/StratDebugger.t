/*
 * Copyright (c) 2004-2007, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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
package ted;

import tom.library.strategy.mutraveler.*;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import tom.library.strategy.mutraveler.MuStrategy;

import ted.stratdebugger.entier.types.*;


public class StratDebugger {

  %gom {
    module Entier 
    imports String
    abstract syntax

    Exp = Zero()
        | S(n:Exp)
        | plus(n:Exp,m:Exp)
  }

  %include{ mustrategy.tom }
//  %include{ ref.tom }
//  %include{ visitable.tom }

  %typeterm DebugStrategyObserver {
    implement { ted.DebugStrategyObserver }
    is_sort(t)     { t instanceof ted.DebugStrategyObserver }
  }

  %op Strategy DebugStrategy(obs:DebugStrategyObserver, s:Strategy) {
    is_fsym(t) { (t instanceof ted.DebugStrategy) }
    make(obs,s) { new DebugStrategy(obs,s) }
    get_slot(obs, t) { (DebugStrategyObserver) t.getObserver() }
    get_slot(s, t) { (MuStrategy) t.getStrat() }
  }


  %strategy DecorateStrategy(obs:DebugStrategyObserver) extends `Identity() {
    visit Strategy {
      s@MuVar[] -> { return `s; }
      s -> { return  `DebugStrategy(obs,s); }
    }
  }

  public static MuStrategy decorateStrategy(DebugStrategyObserver obs, MuStrategy s) {
    try {
      return (MuStrategy) `BottomUp(DecorateStrategy(obs)).visitLight(s);
    } catch(VisitFailure e) { return s; }
  }

  %strategy RS() extends `Identity() {
    visit Exp {
      plus(S(n),m) -> { return `S(plus(n,m)); }
      plus(Zero(),n) -> { return `n; }
    }
  }
 

  public static Visitable applyDebug(Visitable subject, MuStrategy strat) {
    DummyObserver observer = new DummyObserver();
    strat = decorateStrategy(observer, strat);
    try {
      return strat.visitLight(subject);
    } catch(VisitFailure e) { return subject; }
  }

  public static Visitable applyGraphicalDebug(Visitable subject, MuStrategy strat) {
    GraphicalObserver observer = new GraphicalObserver(subject,strat);
    strat = decorateStrategy(observer, strat);
    try {
      return strat.visitLight(subject);
    } catch(VisitFailure e) { return subject; }
  }


  public static void main(String[] argv) {
    MuStrategy s = `InnermostId(RS());
    Exp n = `plus(S(Zero()),S(Zero()));

    n = (Exp) applyGraphicalDebug(n,s);
    //n = (Exp) applyDebug(n,s);
    System.out.println("final result = " + n);
  }
}





