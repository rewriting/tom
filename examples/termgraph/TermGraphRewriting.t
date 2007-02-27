/*
 * Copyright (c) 2004-2007, INRIA
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


  %strategy CollectSubterm(label:String,info:Info) extends Identity(){
    visit Term {
      labTerm[label=label,term=subterm] -> {
        if(label.equals(`label)){
          info.term = `subterm;
          info.omega = getEnvironment().getPosition();
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
 

  %typeterm Info{
    implement {Info}
  }

  %typeterm Position{
    implement {Position}
  }


  static class Info{
    public Position omega;
    public Term term;
  }


  // In this strategy, the failure is Identity
  %strategy NormalizeLabel(map:HashMap) extends Identity(){
    visit Term {
     refTerm[label=label] -> {
        if (! map.containsKey(`label)){
          Info info = new Info();
          Position pos = Position.makeAbsolutePosition(new int[]{});
          Position old = getEnvironment().getPosition();
          Position rootpos = Position.makeAbsolutePosition(new int[]{});
          map.put(`label,old);
          getEnvironment().goTo(old.getRelativePosition(rootpos));
          Strategy s =`Try(TopDown(CollectSubterm(label,info)));
          AbstractStrategy.init(s,getEnvironment()); 
          s.visit();
          getEnvironment().goTo(rootpos.getRelativePosition(old));
          return `labTerm(label,info.term);
        }
     }
     labTerm[label=label] -> {
       map.put(`label,getEnvironment().getPosition());
     }
    }
  }

  // In this strategy, the failure is Identity
  %strategy NormalizePos() extends Identity(){
    visit Term {
      p@posTerm(_*) -> {
        Position current = (Position) getEnvironment().getPosition().clone(); 
        int subomega = getEnvironment().getSubOmega();
        getEnvironment().up();
        //verify that we are not inside a position
        if(! (getEnvironment().getSubject() instanceof posTerm)){  
          getEnvironment().down(subomega);
          Position relPos = Position.makeRelativePosition(((Reference)`p).toArray());
          Position dest = current.getAbsolutePosition(relPos);
          if(current.compare(dest)==-1){
            //we must switch the rel position and the pointed subterm
            Position rootpos = Position.makeAbsolutePosition(new int[]{});
            getEnvironment().goTo(current.getRelativePosition(rootpos));
            Info info = new Info();
            Strategy update =`mu(MuVar("x"),Choice(UpdatePos(dest,current),All(MuVar("x"))));
            Strategy swap1 =`TopDown(Swap1(dest,current,info));
            Strategy swap2 =`TopDown(Swap2(dest,current,info));
            AbstractStrategy.init(update,getEnvironment()); 
            AbstractStrategy.init(swap1,getEnvironment()); 
            AbstractStrategy.init(swap2,getEnvironment()); 
            update.visit();
            swap1.visit();
            swap2.visit();
            getEnvironment().goTo(rootpos.getRelativePosition(current));
            return (Term) getEnvironment().getSubject();
          }
        }
        else{
          getEnvironment().down(subomega);
        }
      }
    }
  }


  %strategy UpdatePos(source:Position,target:Position) extends Fail() {
    visit Term {
      p@posTerm(_*) -> {
        Position current = getEnvironment().getPosition(); 
        Position relPos = Position.makeRelativePosition(((Reference)`p).toArray());
        Position dest = current.getAbsolutePosition(relPos);
        if(current.hasPrefix(source) && !dest.hasPrefix(source)){
          //we must update this relative pos from the redex to the external
          current = current.changePrefix(source,target);
          int[] relarray = current.getRelativePosition(dest).toArray();
          Term relref = `posTerm();
          for(int i=0;i<relarray.length;i++){
            relref = `posTerm(relref*,relarray[i]);
          }
          return relref;
       }

        if (dest.hasPrefix(source) && !current.hasPrefix(source)){
          //we must update this relative pos from the external to the redex
          dest = dest.changePrefix(source,dest); 
          int[] relarray = current.getRelativePosition(dest).toArray();
          Term relref = `posTerm();
          for(int i=0;i<relarray.length;i++){
            relref = `posTerm(relref*,relarray[i]);
          }
          return relref;
        }
        return `p;
      }
    }
  }


  %strategy Swap1(source:Position,target:Position,info:Info) extends Identity() {
    visit Term {
      t -> {
        Position current = getEnvironment().getPosition(); 
        if(current.equals(source)){
          int[] relarray = current.getRelativePosition(target).toArray();
          info.term = (Term) getEnvironment().getSubject();
          Term relref = `posTerm();
          for(int i=0;i<relarray.length;i++){
            relref = `posTerm(relref*,relarray[i]);
          }
          return relref;
        }
      }
    }
  }

  %strategy Swap2(source:Position,target:Position,info:Info) extends Identity() {
    visit Term {
      t -> {
        Position current = getEnvironment().getPosition(); 
        if(current.equals(target)){
          return info.term;
        }
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
    /* concat the redex on top of the term */
    t = `substTerm(t,redex);

    /*  three methods for normalization */
    Term t1 = (Term) termAbstractType.expand(t);
    t1 = (Term) Position.makeAbsolutePosition(new int[]{1}).getSubterm().fire(t1);
    System.out.println("Canonical term obtained by a point fix: "+t1);

    map.clear();
    Term t2 = (Term) `InnermostIdSeq(NormalizeLabel(map)).fire(t);
    t2 = (Term) termAbstractType.label2pos(t2);
    t2 = (Term) Position.makeAbsolutePosition(new int[]{1}).getSubterm().fire(t2);
    System.out.println("Canonical term obtained by Innermost strategy + a map: "+t2);

    Term t3 = (Term) termAbstractType.label2pos(t);
    t3 = (Term) `InnermostIdSeq(NormalizePos()).fire(t3);
    t3 = (Term) Position.makeAbsolutePosition(new int[]{1}).getSubterm().fire(t3);
    System.out.println("Canonical term obtained by Innermost strategy directly on positions: "+t3);

  }

}//class TermGraphRewriting
