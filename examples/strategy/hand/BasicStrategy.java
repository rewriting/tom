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
package strategy.hand;
import tom.library.strategy.mutraveler.Position;

public class BasicStrategy implements tom.library.strategy.mutraveler.MuStrategy {
  protected jjtraveler.Visitor any;
  private Position position;

  public BasicStrategy(jjtraveler.Visitor v) {
    this.any = v;
  }

  public void setPosition(Position pos) {
    this.position = pos;
  }

  public Position getPosition() {
    if(hasPosition()) {
      return position;
    } else {
      throw new RuntimeException("position not initialized");
    }
  }

  public boolean hasPosition() {
    return position!=null;
  }

  public jjtraveler.Visitable visit(jjtraveler.Visitable v) throws jjtraveler.VisitFailure {
    if (v instanceof strategy.hand.Term) {
      return ((strategy.hand.Term) v).accept(this);
    } else if (v instanceof strategy.hand.Slot) {
      return ((strategy.hand.Slot) v).accept(this);
    } else {
      return any.visit(v);
    }
  }

  public strategy.hand.Term visit_Term(strategy.hand.Term arg) throws jjtraveler.VisitFailure {
    return (strategy.hand.Term) any.visit(arg);
  }

  public strategy.hand.Slot visit_Slot(strategy.hand.Slot arg) throws jjtraveler.VisitFailure {
    return (strategy.hand.Slot) any.visit(arg);
  }

  public int getChildCount() { return 1; }

  public jjtraveler.Visitable getChildAt(int index) {
    if(index == 0) {
      return (jjtraveler.Visitable) any;
    }
    throw new IndexOutOfBoundsException();
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    if(index == 0) {
      any = ((jjtraveler.Visitor) v);
      return this;
    }
    throw new IndexOutOfBoundsException();
  }

  public jjtraveler.Visitable apply(jjtraveler.Visitable any) {
    try {
      return tom.library.strategy.mutraveler.MuTraveler.init(this).visit(any);
    } catch (jjtraveler.VisitFailure f) {
      return any;
    }
  }

  public tom.library.strategy.mutraveler.MuStrategy accept(tom.library.strategy.mutraveler.reflective.StrategyVisitorFwd v) throws jjtraveler.VisitFailure {
    return v.visit_Strategy(this);
  }
}
