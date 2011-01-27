/*
 * Copyright (c) 2004-2011, INPL, INRIA
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
package analysis;

import analysis.attrgrammar.term.types.*;
import analysis.attrgrammar.term.TermAbstractType;
import tom.library.sl.*;
import java.util.*;

public class AttrGrammar {

  %include { sl.tom }

  %op Strategy AllTraversal(s1:Strategy) {
    is_fsym(t) {( ($t instanceof analysis.AllTraversal) )}
    make(v) {( new analysis.AllTraversal($v) )}
    get_slot(s1, t) {( (analysis.Strategy)$t.getChildAt(analysis.AllTravsersal.ARG) )}
  }

  %op Strategy TopDownTraversal(s1:Strategy) {
    make(v) {( `mu(MuVar("_x"),Sequence(v,AllTraversal(MuVar("_x")))) )}
  }

  %op Strategy BottomUpTraversal(s1:Strategy) {
    make(v) {( `mu(MuVar("_x"),Sequence(AllTraversal(MuVar("_x")),v)) )}
  }

  %gom(--nosharing) {
    module Term
      imports int String

      abstract syntax
      Bool = True() 
      | False() 
      | Neg(b:Bool)
      | Or(b1:Bool, b2:Bool) 
      | And(b1:Bool, b2:Bool) 
      | Eq(e1:Expr, e2:Expr) 

      Expr = Var(name:String) 
      | Cst(val:int) 
      | Let(name:String, type:Type, e:Expr, body:Expr) 
      | Seq( Expr* )
      | If(cond:Bool, e1:Expr, e2:Expr) 
      | Plus(e1:Expr, e2:Expr)

      Type = Int() | Boolean() | Void()

      module Term : block() {
        public java.util.HashMap<String,analysis.attrgrammar.term.types.Type> envt;
        public analysis.attrgrammar.term.types.Type type;
      }

  }


  %strategy Typing() extends Identity() {
    visit Expr {
      v@Var(name) -> {
        Type t = `v.envt.get(`name);
        if (t == null) {
          System.out.println("The variable "+`v+" is not declared");
          throw new VisitFailure();
        } else {
          `v.type = `v.envt.get(`name);
        }
      }
      i@(Cst|Plus)[] -> {
        `i.type = `Int();
      }
      p@Plus(e1,e2) -> {
        if( !`e1.type.isInt() || !`e2.type.isInt()) {
          System.out.println("The two expressions in "+`p+" must be of type integer");
          throw new VisitFailure();
        }
        `p.type=`Int();
      }
      seq@Seq(_*) -> {
        `seq.type = `Void();
      }
      i@If(cond, e1, e2) -> {
        if(!`cond.type.isBoolean()) {
          System.out.println("The expression "+`cond+" must be of type boolean");
          throw new VisitFailure();
        }
        if(! `e1.type.deepEquals(`e2.type)) {
          System.out.println("The two expressions "+`e1+" and "+`e2+" must be of the same type");
          throw new VisitFailure();
        }
        `i.type=`e1.type;
      }
      let@Let(_,type,expr,body) -> {
        if(! `expr.type.deepEquals(`type)) {
          System.out.println("The expression "+`expr+" has type "+`expr.type);
          System.out.println("The expression "+`expr+" must have the type "+`type);
          throw new VisitFailure();
        }
        `let.type=`body.type;
      }
    }

    visit Bool {
      b -> {
        `b.type = `Boolean();
      }
    }
  }

  %strategy Envt() extends Identity() {
    visit Expr {
      e -> {
        if (getPosition().equals(Position.make())) {
          `e.envt = new HashMap();
        } else {
          `e.envt = ((TermAbstractType) getAncestor()).envt;
        }
      }
      let@Let(name,type,_,_) -> {
        `let.envt.put(`name,`type);
      }
    }

    visit Bool {
      b -> {
        if (getPosition().equals(Position.make())) {
          `b.envt = new HashMap();
        } else {
          `b.envt = ((TermAbstractType) getAncestor()).envt;
        }
      }
    }
  }

  %strategy Print() extends Identity() {
    visit Expr {
      e -> {
        System.out.println("exp "+`e+" ,envt "+`e.envt);
      }
    }
    visit Bool {
      b -> {
        System.out.println("bool "+`b+" ,envt "+`b.envt);
      }
    }
  }

  public final static void main(String[] args) {
    Expr p1 = `Let("a",Int(),Cst(1), Var("a"));
    System.out.println("p1 : "+`p1);
    try {
      `TopDownTraversal(Envt()).visit(`p1);
      //`TopDownTraversal(Print()).visit(`p1);
      `BottomUpTraversal(Typing()).visit(`p1);
      System.out.println("type : "+`p1.type);
    } catch(VisitFailure e) {}

    Expr p2 = `Let("b",Int(),Plus(Var("a"),Cst(2)), Var("b"));
    System.out.println("p2 : "+`p2);
    try {
      `TopDownTraversal(Envt()).visit(`p2);
      `BottomUpTraversal(Typing()).visit(`p2);
      System.out.println("type : "+`p2.type);
    } catch(VisitFailure e) {}

    Expr p3 = `Let("i",Int(),Cst(0),
        If(Neg(Eq(Var("i"),Cst(10))),
          Let("i",Int(),Plus(Var("i"),Cst(1)),Var("i")),Let("i",Int(),Cst(2),Seq())));
    System.out.println("p3 : "+`p3);
    try {
      `TopDownTraversal(Envt()).visit(`p3);
      `BottomUpTraversal(Typing()).visit(`p3);
      System.out.println("type : "+`p3.type);
    } catch(VisitFailure e) {}
  }

}
