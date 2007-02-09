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

package termgraph;

import tom.library.sl.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import java.util.*;

import termgraph.term.*;
import termgraph.term.types.*;
import termgraph.term.strategy.term.*;
import termgraph.term.types.term.posTerm;

public class TermGraphRewriting {

  %include {sl.tom }
  %include {util/HashMap.tom}
  %include {term/term.tom}
  %include {term/_term.tom}

  static int i =0;

  %strategy CollectRef(map:HashMap) extends Identity(){
    visit Term {
      p@posTerm(_*) -> {
        Position target =
          getEnvironment().getPosition().getAbsolutePosition(Position.makeRelativePosition(((Reference)`p).toArray()));
        if (map.containsKey(target.toString())){
          String label = (String) map.get(target.toString());
          return `refTerm(label);
        }
        else{
          i++;
          String label = "tom_label"+i;
          map.put(target.toString(),label);
          return `refTerm(label);
        }
      }
    }
  }

  %strategy AddLabel(map:HashMap) extends Identity() {
    visit Term{
      t -> {
        if (map.containsKey(getEnvironment().getPosition().toString())) {
          String label = (String) map.get(getEnvironment().getPosition().toString());
          return `labTerm(label,t);
        }
      }
    }
  }

  %strategy Print() extends Identity() {
    visit Term {
      t -> {
        System.out.println("current subj :"+`t);
      }
    }
  }
  
  %op Strategy UnExpand(map:HashMap) {
    make(map) {
      `Sequence(TopDown(CollectRef(map)),BottomUp(AddLabel(map)))
    }
  }
  

  public static void main(String[] args){
    Term t = `g(g(g(a(),g(posTerm(2,1),posTerm(3,2))),a()),posTerm(1,1,1,2));
    System.out.println("Initial term :"+t);
    HashMap map = new HashMap();
    /* rule g(x,y) -> f(x) at pos 1.1*/
    System.out.println("apply the rule g(x,y) -> f(x) at position 1.1");
    /* term t with labels */
    t = (Term) `UnExpand(map).fire(t);
    /* redex with labels */
    Term redex = (Term) Position.makeAbsolutePosition(new int[]{1,1}).getSubterm().fire(t);
    /* add labels for variables x and y */
    final Term term_x =  (Term) Position.makeAbsolutePosition(new int[]{1}).getSubterm().fire(redex);
    final Term term_y =  (Term) Position.makeAbsolutePosition(new int[]{2}).getSubterm().fire(redex);
    redex = (Term) Position.makeAbsolutePosition(new int[]{1}).getReplace(`labTerm("x",term_x)).fire(redex);
    redex = (Term) Position.makeAbsolutePosition(new int[]{2}).getReplace(`labTerm("y",term_y)).fire(redex);
    /* replace in t the lhs by the rhs */
    t = (Term) Position.makeAbsolutePosition(new int[]{1,1}).getReplace(`f(refTerm("x"))).fire(t);
    /*  duplications + normalization */
    t = `expTerm(substTerm(t,redex));
    t = (Term) Position.makeAbsolutePosition(new int[]{1}).getSubterm().fire(t);
    System.out.println("Final term :"+t);
  }
}
