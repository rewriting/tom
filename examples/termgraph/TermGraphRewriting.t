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
import termgraph.term.types.term.*;

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
          getEnvironment().getPosition().getAbsolutePosition(((Reference)`p).toPos());
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
          getEnvironment().goTo(rootpos);
          Strategy s =`Try(TopDown(CollectSubterm(label,info)));
          AbstractStrategy.init(s,getEnvironment()); 
          s.visit();
          getEnvironment().goTo(old);
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
        Position current = (Position) getEnvironment().getPosition(); 
        Position relPos = ((Reference)`p).toPos();
        Position dest = current.getAbsolutePosition(relPos);
        if(current.compare(dest)== -1){
          //we must switch the rel position and the pointed subterm
          Position rootpos = Position.makeAbsolutePosition(new int[]{});
          getEnvironment().goTo(rootpos);
          Info info = new Info();
          Strategy update =`mu(MuVar("x"),Choice(UpdatePos(dest,current),All(MuVar("x"))));
          Strategy getSubterm = dest.getSubterm();
          info.term = (Term) getEnvironment().getSubject();
          Term relref = ConsposTerm.fromPos(dest.getRelativePosition(current));
          Strategy replace = dest.getReplace(relref);
          AbstractStrategy.init(update,getEnvironment());
          AbstractStrategy.init(getSubterm,getEnvironment()); 
          AbstractStrategy.init(replace,getEnvironment()); 
          update.visit();
          Term subterm = (Term) getSubterm.fire(getEnvironment().getSubject()); 
          replace.visit(); 
          getEnvironment().goTo(current);
          return subterm; 
        }
      }
    }
  }

/*
 * what are the good names for getAbsolutePosition and getRelativePosition ?
 * do we switch receiver and argument
 * introduce 2 subclasses AbsolutePosition and RelativePosition ?
 * distinction between pos and ref ?
 */

  // In this strategy, the failure is Identity
  %strategy NormalizePosOpt() extends Identity(){
    visit Term {
      p@posTerm(_*) -> {
        Position current = (Position) getEnvironment().getPosition(); 
        Position relPos = ((Reference)`p).toPos();
        Position dest = current.getAbsolutePosition(relPos);
        if(current.compare(dest)== -1) {
          getEnvironment().followRef();
          Position realDest = getEnvironment().getPosition(); 
          if(!realDest.equals(dest)) {
            //the subterm pointed was a pos (in case of previous switch) 
            //and we must only update the relative position
            getEnvironment().goTo(current);
            return ConsposTerm.fromPos(current.getRelativePosition(realDest));
          } else {
            //we must switch the rel position and the pointed subterm

            // 1. we construct the new relative position
            Term relref = ConsposTerm.fromPos(dest.getRelativePosition(current));

            // 2. we update the part we want to change 
            Strategy update =`mu(MuVar("x"),Choice(UpdatePos(dest,current),All(MuVar("x"))));
            AbstractStrategy.init(update,getEnvironment());
            update.visit(); 

            // 3. we save the subterm updated 
            Term subterm = (Term) getEnvironment().getSubject(); 

            // 4. we replace at dest  the subterm by the new relative pos
            getEnvironment().setSubject(relref);

            getEnvironment().goTo(current);
            return subterm; 
          }
        }
      }
    }
  }


  %strategy UpdatePos(source:Position,target:Position) extends Fail() {
    visit Term {
      p@posTerm(_*) -> {
        Position current = getEnvironment().getPosition(); 
        Position relPos = ((Reference)`p).toPos();
        Position dest = current.getAbsolutePosition(relPos);
        if(current.hasPrefix(source) && !dest.hasPrefix(source)){
          //we must update this relative pos from the redex to the external
          current = current.changePrefix(source,target);
          return ConsposTerm.fromPos(current.getRelativePosition(dest));
        }

        if (dest.hasPrefix(source) && !current.hasPrefix(source)){
          //we must update this relative pos from the external to the redex
          dest = dest.changePrefix(source,target); 
          return ConsposTerm.fromPos(current.getRelativePosition(dest));
        }
        return `p;
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

    /*  three methods for rewriting */

    /*  two based on labels */
    /************************************************************/

    /* position of the final subject */
    Position posFinal = Position.makeAbsolutePosition(new int[]{1});

    /* term t with labels */
    Term tt = (Term) `UnExpand(map).fire(t);

    /* redex with labels */
    Term redex = (Term) Position.makeAbsolutePosition(new int[]{1,1}).getSubterm().fire(tt);

    /* add labels for variables x and y */
    Position _x = Position.makeAbsolutePosition(new int[]{1});
    Position _y = Position.makeAbsolutePosition(new int[]{2});
    final Term term_x =  (Term) _x.getSubterm().fire(redex);
    final Term term_y =  (Term) _y.getSubterm().fire(redex);
    redex = (Term) _x.getReplace(`labTerm("x",term_x)).fire(redex);
    redex = (Term) _y.getReplace(`labTerm("y",term_y)).fire(redex);
    /* replace in t the lhs by the rhs */
    tt = (Term) Position.makeAbsolutePosition(new int[]{1,1}).getReplace(`f(refTerm("x"))).fire(tt);

    /* concat the redex on top of the term */
    tt = `substTerm(tt,redex);

    /* normalization by point fix */
    Term t1 = (Term) termAbstractType.expand(tt);
    t1 = (Term) posFinal.getSubterm().fire(t1);
    System.out.println("Canonical term obtained by a point fix: "+t1);

    /* normalization by innermost strategy */
    map.clear();
    Term t2 = (Term) `InnermostIdSeq(NormalizeLabel(map)).fire(tt);
    t2 = (Term) termAbstractType.label2pos(t2);
    t2 = (Term) posFinal.getSubterm().fire(t2);
    System.out.println("Canonical term obtained by Innermost strategy + a map: "+t2);

    /*  one directly based on positions */
    /************************************************************/

    /* redex */
    Position posSubst = Position.makeAbsolutePosition(new int[]{2});
    Position posRedex = Position.makeAbsolutePosition(new int[]{1,1,1});

    /* concat a constant on top of the term */
    Term t3 = `substTerm(t,a());

    /* update positions on phi(rhs) and on the redex */
    Strategy update =`mu(MuVar("x"),Choice(UpdatePos(posRedex,posSubst),All(MuVar("x"))));
    t3 = (Term) update.fire(t3);

    /* concat the redex on top of the term */
    redex = (Term) posRedex.getSubterm().fire(t3);
    t3 = (Term) posSubst.getReplace(redex).fire(t3);

    /* replace in t the lhs by the rhs */
    t3 = (Term) posRedex.getReplace(`f(posTerm(4,2,1))).fire(t3);

    /* normalization by innermost strategy */
    t3 = (Term) `InnermostIdSeq(NormalizePos()).fire(t3);
    t3 = (Term) posFinal.getSubterm().fire(t3);
    System.out.println("Canonical term obtained by Innermost strategy directly on positions: "+t3);

    /*  an optimized version (no update during the innermost strat) */
    /************************************************************/

    t = `g(g(g(f(a()),g(posTerm(2,1),a())),posTerm(1,1,2,2)),posTerm(1,1,1,1,1));
    System.out.println("\nMore complex initial term :"+t);

    /* concat a constant on top of the term */
    Term t4 = `substTerm(t,a());

    /* update positions on phi(rhs) and on the redex */
    t4 = (Term) update.fire(t4);

    /* concat the redex on top of the term */
    redex = (Term) posRedex.getSubterm().fire(t4);
    t4 = (Term) posSubst.getReplace(redex).fire(t4);

    /* replace in t the lhs by the rhs */
    t4 = (Term) posRedex.getReplace(`f(posTerm(4,2,1))).fire(t4);

    /* normalization by innermost strategy */
    t4 = (Term) `InnermostIdSeq(NormalizePosOpt()).fire(t4);
    t4 = (Term) posFinal.getSubterm().fire(t4);
    System.out.println("Canonical term obtained by an optimized version: "+t4);


  }

}//class TermGraphRewriting
