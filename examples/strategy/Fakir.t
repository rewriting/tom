/*
 * Copyright (c) 2010, INPL, INRIA
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

import strategy.fakir.table.*;
import strategy.fakir.table.types.*;
import tom.library.sl.*;

public class Fakir {

  %gom(--withCongruenceStrategies) {
    module Table
    imports int
    abstract syntax
    Element = Nail(left:Element,right:Element)
            | Bag(position:int, count:int)
   }

  %include { sl.tom }

  public final static Element buildTable(int level, int position) {
    if (0 == level) {
      return `Bag(position, 0);
    } else {
      return `Nail(buildTable(level-1, position-1),buildTable(level-1, position+1));
    }
  }

  public final static void printResults(Element elem) {
    int[] table = new int[TBLREC*2+1];
    collectResults(elem,table);
    for (int i = 0; i < table.length; i++) {
        String row = "";
        for (int j = 0; j < table[i]; j++) {
            row += "#";
        }
        System.out.println(row);
    }
  }

  public final static void collectResults(Element elem, int[] table) {
    %match(elem) {
      Nail(l,r) -> {
        collectResults(`l, table);
        collectResults(`r, table);
      }
      Bag(pos, count) -> {
        table[`pos+TBLREC]+=`count;
      }
    }
  }

  private final static int TBLREC = 10;
  private final static int BALLCNT = 200;

  public final static void main(String[] args) {
    Element table = buildTable(TBLREC,0);

    Strategy balldrop = `mu(MuVar("x"),Choice(
        When_Nail(Pselect(1,2,_Nail(MuVar("x"),Identity()),_Nail(Identity(),MuVar("x")))),
        Ball()
    ));

    try {
      for(int i=0 ; i<BALLCNT ; i++) {
        table = balldrop.visitLight(table);
      }
      printResults(table);
    } catch(VisitFailure e) {
      System.out.println("reduction failed on: " + table);
    }
  }

  %strategy Ball() extends `Identity() {
    visit Element {
      Bag(pos,i) -> { return `Bag(pos,i+1); }
    }
  }
}
