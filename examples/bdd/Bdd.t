/*
 * Copyright (c) 2008-2011, INPL, INRIA
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

package bdd;

import bdd.bdd.types.*;
import tom.library.sl.*;
import java.util.*;

public class Bdd {

  %include{ bdd/Bdd.tom }
  %include{ sl.tom }

  public final static Node ZERO = `Cst(0);
  public final static Node ONE = `Cst(1);
  public static int max_var = 0;

  public final static void main(String[] args) {
    Bdd bdd = new Bdd();
    bdd.run();
  }

  private void run() {
    Node n0 = ZERO;
    Node n1 = ONE;
    Node n2 = `Var(2,n0,n1);
    Node n3 = `Var(2,n2,n2);

    System.out.println("n2 = " + n2);
    System.out.println("n3 = " + n3);

    Node f1 = build(`FOr(FIff(FVar(1),FVar(2)),FVar(3)),1,3);
    System.out.println("f1 = " + f1);
    Node f2 = `apply(OpOr(),apply(OpIff(),Var(1,ZERO,ONE),Var(2,ZERO,ONE)),Var(3,ZERO,ONE));
    System.out.println("f2 = " + f2);
    Node f3 = restrict(f2,2,ONE);
    System.out.println("f3 = " + f3);
    System.out.println("anySat(f3) = " + anySat(f3));
    System.out.println("allSat(f3) = " + allSat(f3));
    max_var = 3;
    System.out.println("countSat(f3) = " + countSat(f3));

    System.out.println("allSat(f2) = " + allSat(f2));
    System.out.println("countSat(f2) = " + countSat(f2));
  }

  %strategy Replace(varIndex:int,value:Formula) extends Identity() {
    visit Formula {
      FVar(index) -> {
        if(varIndex==`index) {
          return `value;
        }
      }
    }
  }

  private Formula subst(Formula t, int varIndex, Formula value) {
    Formula res = null;
    //System.out.println(%[subst(@t@,@varIndex@,@value@)]%);
    try {
      res = (Formula) `TopDown(Replace(varIndex,value)).visit(t);
    } catch(VisitFailure e) {
      System.out.println("subst: failure on " + t);
    }
    //System.out.println("res = " + res);
    return res;
  }

  public Node build(Formula t, int varIndex, int varNumber) {
    if(varIndex>varNumber) {
      %match(t) {
        FFalse() -> { return ZERO; }
        FTrue() -> { return ONE; }
      }
      throw new RuntimeException("varIndex should not be < 1: " + varIndex);
    }

    Node v0 = build(subst(t,varIndex,`FFalse()),varIndex+1,varNumber);
    Node v1 = build(subst(t,varIndex,`FTrue()),varIndex+1,varNumber);
    return `Var(varIndex,v0,v1);
  }

  public Node apply(Operator op, Node u1, Node u2) {
    HashMap<Pair,Node> map = new HashMap<Pair,Node>();
    return applyAux(`Apply(op,u1,u2),map);
  }

  private Node applyAux(Node t, HashMap<Pair,Node> map) {
    %match(t) {
      Cst[] -> { return t; }
      Var[] -> { return t; }
      Apply(op,u1,u2) -> {
        Pair u12 = `Pair(u1,u2);
        Node res = map.get(u12);
        if(res!=null) {
          return res;
        }
        //System.out.println("t = " + t);

        %match(u1,u2) {
          Var(x,l1,h1), Var(x,l2,h2) -> {
            res = `Var(x, applyAux(Apply(op,l1,l2),map), applyAux(Apply(op,h1,h2),map)); 
          }
          Var(x,l1,h1), Var(y,l2,h2) -> {
            if(`x<`y) { 
              res = `Var(x, applyAux(Apply(op,l1,u2),map), applyAux(Apply(op,h1,u2),map)); 
            } else if(`x>`y) {
              res = `Var(y, applyAux(Apply(op,u1,l2),map), applyAux(Apply(op,u1,h2),map)); 
            }
          }
          Var(x,l1,h1), Cst[] -> {
              res = `Var(x, applyAux(Apply(op,l1,u2),map), applyAux(Apply(op,h1,u2),map)); 
          }
          Cst[], Var(y,l2,h2) -> {
            res = `Var(y, applyAux(Apply(op,u1,l2),map), applyAux(Apply(op,u1,h2),map)); 
          }
        }
        map.put(u12,res);
        //System.out.println("res = " + res);
        return res;
      }
    }
    throw new RuntimeException("applyAux: " + t);
  }

  public Node restrict(Node t, int varIndex, Node value) {
    %match(t) {
      Cst[] -> { return t; }
      Var(x,l,h) -> { 
        if(`x==varIndex) {
          %match(value) {
            Cst(0) -> { return `l; }
            Cst(1) -> { return `h; }
          } 
        } else {
          return `Var(x,restrict(l,varIndex,value),restrict(h,varIndex,value));
        }
      }
    }
    throw new RuntimeException("restrict: " + t);
  }

  private int var(Node t) {
    %match(t) {
      Cst(0) -> { return max_var+1; }
      Cst(1) -> { return max_var+1; }
      Var(x,_,_) -> { return `x; }
    }
    throw new RuntimeException("var: " + t);
  }

  public int countSat(Node t) {
    %match(t) {
      Cst(0) -> { return 0; }
      Cst(1) -> { return 1; }
      Var(x,l,h) -> { 
        return 
          (2^(var(`l)-`x-1))*countSat(`l) +
          (2^(var(`h)-`x-1))*countSat(`h);
      }
    }
    throw new RuntimeException("countSat: " + t);
  }


  public Solution anySat(Node t) {
    %match(t) {
      Cst(0) -> { return `NoSolution(); }
      Cst(1) -> { return `concAssignment(); }
      Var(x,Cst(0),h) -> { 
        Solution s = anySat(`h);
        return `concAssignment(Assignment(x,1),s*); 
      }
      Var(x,l,_) -> { 
        Solution s = anySat(`l);
        return `concAssignment(Assignment(x,0),s*); 
      }
    }
    throw new RuntimeException("anySat: " + t);
  }

  public SolutionList allSat(Node t) {
    %match(t) {
      Cst(0) -> { return `concSolution(); }
      Cst(1) -> { return `concSolution(concAssignment()); }
      Var(x,l,h) -> { 
        SolutionList sl = `addAssignment(x,0,allSat(l));
        SolutionList sh = `addAssignment(x,1,allSat(h));
        return `concSolution(sl*,sh*);
      }
    }
    throw new RuntimeException("allSat: " + t);
  }
  private SolutionList addAssignment(int x, int v, SolutionList sol) {
    %match(sol) {
      concSolution(concAssignment()) -> { return `concSolution(concAssignment(Assignment(x,v))); }
      concSolution(head,tail*) -> { 
        SolutionList s = `addAssignment(x,v,tail*);
        return `concSolution(concAssignment(Assignment(x,v),head),s*);
      }
    }
    return `concSolution();
  }

}
