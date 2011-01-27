/*
 * Copyright (c) 2004-2011, INPL, INRIA
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
import java.util.*;

import termgraph.term.*;
import termgraph.term.types.*;
import termgraph.term.strategy.term.*;
import termgraph.term.types.term.*;

public class TermGraphRewriting {

  %include {sl.tom }
  %include {util/HashMap.tom}
  %include {term/term.tom}
  //%include {term/_term.tom}

  static int i =0;

  %strategy CollectRef(map:HashMap) extends Identity(){
    visit Term {
      p@PathTerm(_*) -> {
        Path target =
          getEnvironment().getPosition().add((Path)`p).getCanonicalPath();
        if (map.containsKey(target.toString())){
          String label = (String) map.get(target.toString());
          return `RefTerm(label);
        }
        else{
          i++;
          String label = "tom_label"+i;
          map.put(target.toString(),label);
          return `RefTerm(label);
        }
      }
    }
  }


  %strategy CollectSubterm(label:String,info:Info) extends Identity(){
    visit Term {
      LabTerm[labelTerm=label,termTerm=subterm] -> {
        if(label.equals(`label)){
          info.term = `subterm;
          info.omega = getEnvironment().getPosition();
          return `RefTerm(label);
        }
      }
    }
  }

  %strategy AddLabel(map:HashMap) extends Identity() {
    visit Term{
      t -> {
        if (map.containsKey(getEnvironment().getPosition().toString())) {
          String label = (String) map.get(getEnvironment().getPosition().toString());
          return `LabTerm(label,t);
        }
      }
    }
  }

  %strategy Print() extends Identity() {
    visit Term {
      t -> {
        System.out.println("current env :"+getEnvironment());
      }
    }
  }

  %typeterm Info{
    implement {Info}
    is_sort(t)     { t instanceof Info }
  }

  static class Info{
    public Position omega;
    public Term term;
  }


  // In this strategy, the failure is Identity
  %strategy NormalizeLabel(map:HashMap) extends Identity(){
    visit Term {
      RefTerm[labelTerm=label] -> {
        if (! map.containsKey(`label)){
          Info info = new Info();
          Position pos = Position.makeFromArray(new int[]{});
          Position old = getEnvironment().getPosition();
          Position rootpos = Position.makeFromArray(new int[]{});
          map.put(`label,old);
          getEnvironment().followPath(rootpos.sub(getEnvironment().getPosition()));
          `Try(TopDown(CollectSubterm(label,info))).visit(getEnvironment());
          getEnvironment().followPath(old.sub(getEnvironment().getPosition()));
          return `LabTerm(label,info.term);
        }
      }
      LabTerm[labelTerm=label] -> {
        map.put(`label,getEnvironment().getPosition());
      }
    }
  }

  // In this strategy, the failure is Identity
  %strategy NormalizePos() extends Identity(){
    visit Term {
      p@PathTerm(_*) -> {
        Position current = getEnvironment().getPosition(); 
        Position dest = (Position) current.add((Path)`p).getCanonicalPath();
        if(current.compare(dest)== -1) {
          getEnvironment().followPath((Path)`p);
          Position realDest = getEnvironment().getPosition(); 
          if(!realDest.equals(dest)) {
            //the subterm pointed was a pos (in case of previous switch) 
            //and we must only update the relative position
            getEnvironment().followPath(current.sub(getEnvironment().getPosition()));
            return PathTerm.make(realDest.sub(current));
          } else {
            //we must switch the rel position and the pointed subterm

            // 1. we construct the new relative position
            Term relref = PathTerm.make(current.sub(dest));

            // 2. we update the part we want to change 
            `TopDown(UpdatePos(dest,current)).visit(getEnvironment());

            // 3. we save the subterm updated 
            Term subterm = (Term) getEnvironment().getSubject(); 

            // 4. we replace at dest  the subterm by the new relative pos
            getEnvironment().setSubject(relref);
            getEnvironment().followPath(current.sub(getEnvironment().getPosition()));
            return subterm; 
          }
        }
      }
    }
  }


  %strategy UpdatePos(source:Position,target:Position) extends Identity() {
    visit Term {
      p@PathTerm(_*) -> {
        Position current = getEnvironment().getPosition(); 
        Position dest = (Position) current.add((Path)`p).getCanonicalPath();
        if(current.hasPrefix(source) && !dest.hasPrefix(source)){
          //we must update this relative pos from the redex to the external
          current = current.changePrefix(source,target);
          return PathTerm.make(dest.sub(current));
        }

        if (dest.hasPrefix(source) && !current.hasPrefix(source)){
          //we must update this relative pos from the external to the redex
          dest = dest.changePrefix(source,target); 
          return PathTerm.make(dest.sub(current));
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

    Term t = `g(g(g(a(),g(PathTerm(-1,-2,1),a())),PathTerm(-2,1,2,2)),PathTerm(-2,1,1,2));
    System.out.println("Initial term :\n"+t);
    HashMap map = new HashMap();

    /* rule g(x,y) -> f(x) at pos 1.1*/
    System.out.println("apply the rule g(x,y) -> f(x) at position 1.1");

    /*  three methods for rewriting */

    /*  two based on labels */
    /************************************************************/

    /* position of the final subject */
    Position posFinal = Position.makeFromArray(new int[]{1});
    try {
      /* term t with labels */
      Term tt = (Term) `UnExpand(map).visit(t);

      /* redex with labels */
      Term redex = (Term) Position.makeFromArray(new int[]{1,1}).getSubterm().visit(tt);

      /* add labels for variables x and y */
      Position _x = Position.makeFromArray(new int[]{1});
      Position _y = Position.makeFromArray(new int[]{2});
      final Term term_x =  (Term) _x.getSubterm().visit(redex);
      final Term term_y =  (Term) _y.getSubterm().visit(redex);
      redex = (Term) _x.getReplace(`LabTerm("x",term_x)).visit(redex);
      redex = (Term) _y.getReplace(`LabTerm("y",term_y)).visit(redex);
      /* replace in t the lhs by the rhs */
      tt = (Term) Position.makeFromArray(new int[]{1,1}).getReplace(`f(RefTerm("x"))).visit(tt);

      /* concat the redex on top of the term */
      tt = `SubstTerm(tt,redex);

      /* normalization by point fix */
      Term t1 = (Term) tt.expand();
      t1 = (Term) posFinal.getSubterm().visit(t1);
      System.out.println("Canonical term obtained by a point fix:\n"+t1);

      /* normalization by innermost strategy */
      map.clear();
      Term t2 = (Term) `InnermostIdSeq(NormalizeLabel(map)).visit(tt);
      t2 = (Term) t2.expand();
      t2 = (Term) posFinal.getSubterm().visit(t2);
      System.out.println("Canonical term obtained by Innermost strategy + a map:\n"+t2);

      /*  one directly based on positions */
      /************************************************************/

      /* redex */
      Position posSubst = Position.makeFromArray(new int[]{2});
      Position posRedex = Position.makeFromArray(new int[]{1,1,1});

      /* concat a constant on top of the term */
      Term t3 = `SubstTerm(t,a());

      /* update positions on phi(rhs) and on the redex */
      t3 = (Term) `TopDown(UpdatePos(posRedex,posSubst)).visit(t3);

      /* concat the redex on top of the term */
      redex = (Term) posRedex.getSubterm().visit(t3);
      t3 = (Term) posSubst.getReplace(redex).visit(t3);

      /* replace in t the lhs by the rhs */
      t3 = (Term) posRedex.getReplace(`f(PathTerm(-1,-1,-1,-1,2,1))).visit(t3);

      /* normalization by innermost strategy */
      t3 = (Term) `InnermostIdSeq(NormalizePos()).visit(t3);
      t3 = (Term) posFinal.getSubterm().visit(t3);
      System.out.println("Canonical term obtained by Innermost strategy directly on positions:\n"+t3);

      System.out.println("Canonical term obtained using graphrules hooks in the Gom signature:\n"+(Position.makeFromArray(new int[]{1,1})).getOmega(Term.GraphRule()).visit(t));

      t = `g(g(g(f(a()),g(PathTerm(-1,-2,1),a())),PathTerm(-2,1,2,2)),PathTerm(-2,1,1,1,1));
      System.out.println("\nMore complex initial term :\n"+t);
      /* rule g(x,y) -> f(x) at pos 1.1*/
      System.out.println("apply the rule g(x,y) -> f(x) at position 1.1");

      /* concat a constant on top of the term */
      Term t4 = `SubstTerm(t,a());

      /* update positions on phi(rhs) and on the redex */
      t4 = (Term) `TopDown(UpdatePos(posRedex,posSubst)).visit(t4);

      /* concat the redex on top of the term */
      redex = (Term) posRedex.getSubterm().visit(t4);
      t4 = (Term) posSubst.getReplace(redex).visit(t4);

      /* replace in t the lhs by the rhs */
      t4 = (Term) posRedex.getReplace(`f(PathTerm(-1,-1,-1,-1,2,1))).visit(t4);

      /* normalization by innermost strategy */
      t4 = (Term) `InnermostIdSeq(NormalizePos()).visit(t4);
      t4 = (Term) posFinal.getSubterm().visit(t4);
      System.out.println("Canonical term obtained by Innermost strategy directly on positions:\n"+t4);
      System.out.println("Canonical term obtained using graphrules hooks in the Gom signature:\n"+(Position.makeFromArray(new int[]{1,1})).getOmega(Term.GraphRule()).visit(t));

    } catch(VisitFailure e) {
      System.out.println("Unexcepted failure");
    }
  }

}//class TermGraphRewriting
