/*
 * Copyright (c) 2004-2006, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this conc of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce the above copyright
 *	notice, this conc of conditions and the following disclaimer in the
 *	documentation and/or other materials provided with the distribution.
 *	- Neither the name of the INRIA nor the names of its
 *	contributors may be used to endorse or promote products derived from
 *	this software without specific prior written permission.
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

import tom.library.strategy.mutraveler.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import java.util.*;

import strategy.graphterm.*;
import strategy.graphterm.types.*;

public class GraphTest{

  %include {mutraveler.tom }
  %include {graphterm/GraphTerm.tom}



  %strategy DummyStrat() extends Identity(){
    visit GraphTerm {
      node("toto") -> {return `node("titi");}
      node("titi") -> {return `node("toto");}
    }
  }

%typeterm Visitable {
  implement      { jjtraveler.Visitable}
  equals(l1,l2)  { l1.equals(l2) }
  check_stamp(l) {}
  set_stamp(l)   { l }  
}

 %op Strategy AllPos(s:Visitable,v:Strategy) {
    is_fsym(t) { (t instanceof AllRefSensitive) }
    make(s,v) { new AllRefSensitive(s,v) }
  }

  %op Strategy BottomUpRefSensitive(s:Visitable,v:Strategy) {
    make(s,v) { `mu(MuVar("_x"),Sequence(AllPos(s,MuVar("_x")),v)) }
  }

 %op Strategy TopDownRefSensitive(s:Visitable,v:Strategy) {
    make(s,v) { `mu(MuVar("_x"),Sequence(v,AllPos(s,MuVar("_x")))) }
  }

  static GraphTerm root = null;
  %op GraphTerm Ref(ptr:GraphTerm) {
    is_fsym(t) { (t!=null) 
      && (t instanceof strategy.graphterm.types.graphterm.refGraphTerm) }
    get_slot(ptr, t) { (GraphTerm)(new Position(((MuReference)t).toArray())).getSubterm().apply(root) }
  }

  public static void main(String[] args){
    GraphTerm g = `graph("titi",
        conc(node("titi"),
          graph("toto",conc(refGraphTerm(1,2,1))),
          graph("titi",conc(refGraphTerm(1,2,1))),
          node("toto"))
        );
    try{
     g = (GraphTerm) `TopDownRefSensitive(g,DummyStrat()).visit(g);
    }catch(VisitFailure e){
      System.out.println("Failure");
    }
    System.out.println(g);
    root = `graph("titi",
        conc(node("titi"),
          graph("toto",conc(refGraphTerm(1,1))),
          graph("titi",conc(refGraphTerm(1,1))),
          node("toto"))
        );
    System.out.println("truc?" + (new Position(new int[]{1,1})).getSubterm().apply(root));
    %match(GraphTerm root) {
       graph(_,conc(_*,graph(c,conc(Ref(x))),_*)) -> {
        System.out.println("chose: "+`x);
       }
    }
    GraphTerm s = `node("titi");
    %match(GraphTerm s, GraphTerm root) {
       x, graph(_,conc(_*,graph(c,conc(Ref(x))),_*)) -> {
        System.out.println("truc: "+`c);
       }
    }
  }
}

