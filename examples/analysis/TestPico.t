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

import analysis.testpico.term.types.*;
import tom.library.sl.VisitFailure;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestPico {

  %include { sl.tom }

  %gom(--termpointer) {
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
      | Let(name:String, e:Expr, body:Expr) 
      | Seq( Expr* )
      | If(cond:Bool, e1:Expr, e2:Expr) 
      | Print(e:Expr)
      | Plus(e1:Expr, e2:Expr)

      Inst = Assign(name:String, e:Expr)
      | Free(name:String)
      | PrintInst(e:Expr)
      | JumpCond(cond:Bool,ref:Inst)
      | SeqInst(Inst*)

      SeqInst: FL() {}

  }

  static int i=0;

  public Inst AstToCfg(Expr ast)  {
    %match(ast) {
      Let(name, e, body) -> {
        Inst newbody = `AstToCfg(body);
        return `SeqInst(Assign(name,e),newbody*,Free(name));
      } 

      Seq(head,TAIL*) -> {
        Inst newhead = `AstToCfg(head);
        Inst newtail = `AstToCfg(TAIL);
        return `SeqInst(newhead*,newtail*);
      }

      Seq() -> {
        return `SeqInst();
      }
 
      If(cond, e1, e2) -> {
        Inst newe1 = `AstToCfg(e1);
        String freshlabel = "l"+i;
        i++;
        Inst newe2 = `LabInst(freshlabel,AstToCfg(e2));
        return `SeqInst(JumpCond(Neg(cond),RefInst(freshlabel)),newe1*,newe2*);
      } 
      
      Print(e) -> {
        return `SeqInst(PrintInst(e));
      }
    }
   throw new RuntimeException("cannot translate into cfg this expression "+ast);
  }

  @Test
  public void testAstToCfg() {

    Expr p1 = `Seq(Let("a",Cst(1), Print(Var("a"))));
    assertEquals(`SeqInst(Assign("a",Cst(1)),PrintInst(Var("a")),Free("a")),AstToCfg(p1));
    
    Expr p2 = `Seq(Let("b",Plus(Var("a"),Cst(2)), Print(Var("b"))));
    assertEquals(`SeqInst(Assign("b",Plus(Var("a"),Cst(2))),PrintInst(Var("b")),Free("b")),AstToCfg(p2));
    
    Expr p3 = `Let("i",Cst(0),
        If(Neg(Eq(Var("i"),Cst(10))),
          Seq(Print(Var("i")), Let("j",Plus(Var("i"),Cst(1)),Print(Var("j")))),Print(Var("i"))));
    assertEquals(`SeqInst(Assign("i",Cst(0)),JumpCond(Neg(Neg(Eq(Var("i"),Cst(10)))),RefInst("l0")),PrintInst(Var("i")),Assign("j",Plus(Var("i"),Cst(1))),PrintInst(Var("j")),Free("j"),LabInst("l0",SeqInst(PrintInst(Var("i")))),Free("i")),AstToCfg(p3));

  }

  public final static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestPico.class.getName());
  }

}
