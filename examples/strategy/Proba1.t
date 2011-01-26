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

import strategy.proba1.piece.*;
import strategy.proba1.piece.types.*;
import tom.library.sl.*;

public class Proba1 {

  %gom {
    // extension of adt syntax
    module Piece
      
    abstract syntax
      Piece = nothing()
            | pile()
            | face()
   }

  %include { sl.tom }

  public final static void main(String[] args) {
    Piece subject = `nothing();

    Strategy pileS = `PileSystem();
    Strategy faceS = new FaceSystem();
    Strategy probS = `Pselect(1,3,pileS,faceS);

    try {
      System.out.println("subject       = " + subject);
      int pile=0;
      int face=0;
      for(int i=0 ; i<100 ; i++) {
        Piece p = (Piece) probS.visitLight(subject);
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
  
  %strategy PileSystem() extends `Fail() {
    visit Piece {
      nothing() -> { return `pile(); }
    }
  }

  %strategy FaceSystem() extends `Fail() { 
    visit Piece {
      nothing() -> { return `face(); }
    }
  }
}
