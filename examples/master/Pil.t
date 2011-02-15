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
package master;
import master.pil.term.types.*;
import tom.library.sl.VisitFailure;

import java.util.*;

class Pil {
  %include { sl.tom }

  %gom {
    module Term
    imports int String
    abstract syntax
    Bool =
         | True()
         | False() 
         | Eq(e1:Expr, e2:Expr) 

    Expr = 
         | Var(name:String) 
         | Let(var:Expr, e:Expr, body:Expr) 
         | Seq(i1:Expr, i2:Expr)
         | If(cond:Bool, e1:Expr, e2:Expr) 
         | a()
         | b()
  }
  
  public final static void main(String[] args) {
    Expr p1 = `Let(Var("x"),a(), Let(Var("y"),b(),Var("x")));
    System.out.println("p1 = " + p1);
    //System.out.println(pretty(p1));
    
    //System.out.println(`RenameVar("x","z").visit(p1));

    //System.out.println("renamed p1   = " + `OnceBottomUp(RenameVar("x","z")).visit(p1));
    //System.out.println("optimized p1 = " + `BottomUp(RemoveLet()).visit(p1));

  }

  %strategy RenameVar(n1:String,n2:String) extends Identity() {
    visit Expr {
      Var(n) -> { if(`n==n1) return `Var(n2); }
    }
  }

  %strategy RemoveLet() extends Identity() {
    visit Expr {
      Let(Var(n),_,body) -> { 
        if(`body == `TopDown(RenameVar(n,"_"+n)).visit(`body)) {
          // if Var(n) is not used
          return `body;
        }
      }
    }
  }

  public static String pretty(Object e) {
    %match(e) {
      Var(name) -> { 
        return `name; 
      }
      Let(var,expr,body) -> {
        return "let " + pretty(`var) + "<-" + pretty(`expr) + " in " + pretty(`body);
      }
      Seq(i1,i2) -> {
        return pretty(`i1) + " ; " + pretty(`i2);
      }
      If(c,i1,i2) -> {
        return "if(" + pretty(`c) + ") " + pretty(`i1) + " else " + pretty(`i2) + " end";
      }
    }
    %match(e) {
      Eq(e1,e2) -> {
        return pretty(`e1) + " = " + pretty(`e2);
      }
    }
    return e.toString();
  }

}
