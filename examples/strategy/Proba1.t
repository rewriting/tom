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

import aterm.*;
import aterm.pure.PureFactory;

import strategy.proba1.piece.*;
import strategy.proba1.piece.types.*;

import tom.library.strategy.mutraveler.TravelerFactory;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

public class Proba1 {
  private PieceFactory factory;
  private TravelerFactory travelerFactory;

  public Proba1(PieceFactory factory) {
    this.factory = factory;
  }

  %vas {
    // extension of adt syntax
    module Piece
    imports 
    public
      sorts Piece
      
    abstract syntax
      nothing -> Piece
      pile -> Piece
      face -> Piece
   }

  %include { mutraveler.tom }

  public PieceFactory getPieceFactory() {
    return factory;
  }

  public VisitableVisitor mu(VisitableVisitor var, VisitableVisitor v) {
    return tom.library.strategy.mutraveler.MuVar.mu(var,v);
  }

  public final static void main(String[] args) {
    Proba1 test = new Proba1(PieceFactory.getInstance(new PureFactory()));
    test.run();
  }

  public void run() {
    Piece subject = `nothing();

    VisitableVisitor pileS = new PileSystem();
    VisitableVisitor faceS = new FaceSystem();
    VisitableVisitor probS = `Pselect(1,3,pileS,faceS);

    try {
      System.out.println("subject       = " + subject);
      int pile=0;
      int face=0;
      for(int i=0 ; i<100 ; i++) {
        Piece p = (Piece) probS.visit(subject);
        if(p == `pile()) {
          pile++;
        } else {
          face++;
        }
      }
      System.out.println("pile = " + pile + " face = " + face);
    } catch(VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }

  }
  
  class PileSystem extends strategy.proba1.piece.PieceVisitableFwd {
    public PileSystem() {
      super(new tom.library.strategy.mutraveler.Fail());
    }
    
    public Piece visit_Piece(Piece arg) throws VisitFailure { 
      %match(Piece arg) {
        nothing() -> { return `pile(); }
      }
      throw new VisitFailure();
    }
  }

  class FaceSystem extends strategy.proba1.piece.PieceVisitableFwd {
    public FaceSystem() {
      super(new tom.library.strategy.mutraveler.Fail());
    }
    
    public Piece visit_Piece(Piece arg) throws VisitFailure { 
      %match(Piece arg) {
        nothing() -> { return `face(); }
      }
      throw new VisitFailure();
    }
  }

}

 




