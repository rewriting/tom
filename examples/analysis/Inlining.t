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

import analysis.inlining.term.types.*;
import java.util.*;
import tom.library.sl.*;

public class Inlining {

  %gom(--withSeparateCongruenceStrategies) {
    module Term
      imports int String
      abstract syntax

      Instruction = Seq(Instruction*)
      | Declare(v:Expression,e:Expression,i:Instruction)
      | Assign(v:Expression,e:Expression)
      | Print(e:Expression)

      Expression = Plus(e1:Expression,e2:Expression)
      | Mult(e1:Expression,e2:Expression)
      | Var(name:String)
      | Cst(n:int)

      Seq : FL() {}
  }

  %include { java/util/types/Collection.tom }
  %include { java/util/HashMap.tom }
  //do not want to import graph.tom but just basic.tom and composed.tom
  //because I redefine the CTL operators at the end of this file
  %include { Strategy.tom }
  %include { sl/basic.tom }
  %include { sl/composed.tom }
  %include { inlining/term/_Term.tom }

  public final static void main(String[] args) {
    try {
      // Inlining with counters
      System.out.println("\nProgram p1:");
      Instruction p1 = `Declare(Var("x"),Plus(Cst(1),Cst(2)),Declare(Var("y"),Mult(Var("x"),Cst(10)),Print(Plus(Var("x"),Var("y")))));
      System.out.println(pretty(p1));
      System.out.println("\nInlining p1:");
      System.out.println(pretty(`OutermostId(Try(Inline())).visit(p1)));

      System.out.println("\nProgram p2:");
      Instruction p2 = `Declare(Var("x"),Plus(Cst(1),Cst(2)),Declare(Var("y"),Mult(Cst(1),Cst(10)),Print(Plus(Var("x"),Var("y")))));
      System.out.println(pretty(p2));
      System.out.println("\nInlining p2:");
      System.out.println(pretty(`OutermostId(Try(Inline())).visit(p2)));

      System.out.println("\nProgram p3:");
      Instruction p3 = `Declare(Var("x"),Plus(Cst(1),Cst(2)),Declare(Var("y"),Mult(Var("x"),Cst(10)),Seq(Assign(Var("x"),Cst(1)),Print(Plus(Var("x"),Var("y"))))));    
      System.out.println(pretty(p3));
      System.out.println("\nInlining p3:");
      System.out.println(pretty(`OutermostId(Try(Inline())).visit(p3)));

      // Inlining with CTL formulae
      /* inline when a variable is used once in an instruction */
      /* but inside the instruction, the variable can be used more than once */
      System.out.println("\nInlining p1 with CTL:");
      System.out.println(pretty(`OutermostId(IfThenElse(IsUsedOnce(),InlineCtl(),Identity())).visit(p1)));

      System.out.println("\nInlining p2 with CTL:");
      System.out.println(pretty(`OutermostId(IfThenElse(IsUsedOnce(),InlineCtl(),Identity())).visit(p2)));

      System.out.println("\nInlining p3 with CTL:");
      System.out.println(pretty(`OutermostId(IfThenElse(IsUsedOnce(),InlineCtl(),Identity())).visit(p3)));

    } catch (VisitFailure e ) {
      System.out.println("Strategy failure!!!");
    }
  }

  %strategy PrintInst(s:String) extends Identity() {
    visit Instruction {
      i -> { System.out.println("printInst nb "+s); System.out.println(`i); }
    }
  }

  %typeterm Info {
    implement { Info }
  }

  static class Info {
    public Info(String varName) {
      this.varName = varName;
    }
    public Info(String varName, Set assignmentVars) {
      this.varName = varName;
      this.assignmentVars = assignmentVars;
    }
    public String varName;
    public Set assignmentVars;
    public int readCount;
  }

  %strategy Replace(varName:String,expression:Expression) extends Identity() {
    visit Expression {
      Var(name) -> {
        if (`name.equals(varName)) {
          return expression;
        }
      }
    }
  }

  %strategy Inline() extends Identity() {
    visit Instruction {
      Declare[v=Var(name),e=expression,i=instruction] -> {
        Set vars = new HashSet();
        `TopDown(AddVar(vars)).visit(`expression);
        Info info = new Info(`name,vars);
        `TopDown(Count(info)).visit(`instruction);
        if (info.readCount==1) {
          return (Instruction) `TopDown(Replace(name,expression)).visit(`instruction);
        }
      }
    }
  }

  %strategy Count(info:Info) extends Identity() {
    visit Instruction {
      Assign(Var(name),_) -> {
        if(info.assignmentVars.contains(`name) || 
            info.varName == `name) {
          throw new VisitFailure();
        }
      }
    }

    visit Expression { 
      Var(name) -> { 
        if(info.varName == `name) {
          info.readCount++;
          if (info.readCount == 2) { throw new VisitFailure(); }
        } 
      } 
    }
  }


  // Inlining with CTL formulae
  %strategy Modified(vars:Collection) extends Fail() {
    visit Instruction {
      i@Assign(Var(name),_) -> {
        if(vars.contains(`name)) {
          return `i;
        }
      }
    }
  }


  %strategy IsVar(varName:String) extends Fail() {
    visit Expression { 
      v@Var(name) -> { 
        if(varName.equals(`name)) {
          return `v;
        } 
      }
    }
  }

  %op Strategy HasVar(varName:String) {
    make(varName) { `Mu(MuVar("x"),Choice(IsVar(varName),One(MuVar("x")))) }
  }

  %op Strategy Used(varName:String) {
    make(varName) { `Choice(
        _Assign(Identity(),HasVar(varName)),
        _Print(HasVar(varName)),
        _Declare(Identity(),HasVar(varName),Identity()))
    }
  }

  %strategy IsUsedOnce() extends Fail() {
    visit Instruction {
      d@Declare(Var(name),e,i) ->  {
        Set vars = new HashSet();
        vars.add(`name);
        `TopDown(AddVar(vars)).visit(`e);
        `AU(And(Not(Used(name)),Not(Modified(vars))),And(Used(name),AX(AG(Not(Used(name)))))).visit(`i);
        return `d;
      }
    }
  }

  %op Strategy AG(s:Strategy) {
    make(s) { `mu(MuVar("x_ag"),Sequence(s,AllCfg(MuVar("x_ag")))) }
  }

  %op Strategy And(s1:Strategy,s2:Strategy) {
    make(s1,s2) { `Sequence(s1,s2) }
  }

  %op Strategy AU(s1:Strategy, s2:Strategy) {
    make(s1,s2) { `mu(MuVar("x_au"),Choice(s2,Sequence(s1,AllCfg(MuVar("x_au")),OneCfg(Identity())))) }
  }

  %op Strategy AX(s:Strategy) {
    make(s) { `AllCfg(s) }
  }

  %op Strategy UpFail(s:Strategy) {
    make(s) { new UpFail(s) }
  }

  %op Strategy AllCfg(s:Strategy) {
    make(s) {`Mu(MuVar("begin"), Choice(
          _Declare(Identity(), Identity(), s),
          _ConsSeq(s, Identity()),
          Sequence( Is_EmptySeq(), ApplyAtTopSeq(ApplyAtOuterSeq(MuVar("begin"))) ),
          Sequence( Is_Print(), ApplyAtOuterSeq(MuVar("begin"))),
          Sequence( Is_Assign(), ApplyAtOuterSeq(MuVar("begin")))))
    }
  }


  %op Strategy OneCfg(s:Strategy) {
    make(s) {`Mu(MuVar("begin"), Choice(
          _Declare(Identity(), Identity(), s),
          _ConsSeq(s, Identity()),
          Sequence( Is_EmptySeq(), ApplyAtTopSeqFail(ApplyAtOuterSeqFail(MuVar("begin"))) ),
          Sequence( Is_Print(), ApplyAtOuterSeqFail(MuVar("begin"))),
          Sequence( Is_Assign(), ApplyAtOuterSeqFail(MuVar("begin")))))
    }
  }


  %op Strategy ApplyAtOuterSeq(s:Strategy) {
    make(s) { `Mu(MuVar("x"),Up(IfThenElse(Is_ConsSeq(),_ConsSeq(Identity(),s),MuVar("x")))) }
  }


  %op Strategy ApplyAtTopSeq(s:Strategy) {
    make(s) { `Mu(MuVar("x"),Up(Choice(Sequence(Is_ConsSeq(),MuVar("x")),s))) }
  }

  %op Strategy ApplyAtOuterSeqFail(s:Strategy) {
    make(s) { `Mu(MuVar("x"),UpFail(IfThenElse(Is_ConsSeq(),_ConsSeq(Identity(),s),MuVar("x")))) }
  }

  %op Strategy ApplyAtTopSeqFail(s:Strategy) {
    make(s) { `Mu(MuVar("x"),UpFail(Choice(Sequence(Is_ConsSeq(),MuVar("x")),s))) }
  }

  %strategy InlineCtl() extends Identity() {
    visit Instruction {
      Declare[v=Var(name),e=expression,i=instruction] -> {
        return (Instruction) `TopDown(Replace(name,expression)).visit(`instruction);

      }
    }
  }

  %strategy AddVar(set:Collection) extends Identity() {
    visit Expression {
      Var(name) -> {
        set.add(`name);
      }
    }
  }

  public static String pretty(Object e) { return pretty(0,e); }

  public static String pretty(int indent, Object e) {
    String w = "";
    for(int i=0;i<indent;i++) w+="  ";
    %match(e) { 
      Declare(var,expr,body) -> { return w+"declare " + pretty(`var) + " <- " + pretty(`expr) + " in\n" + pretty(indent+1,`body); }
      Assign(var,expr) -> { return w+pretty(`var)+" = "+pretty(`expr); }
      Print(expr) -> { return w+"print("+pretty(`expr)+")"; }
      Seq(X*) -> {
        Instruction x = `X;
        String s=""; 
        while(!`x.isEmptySeq()) { 
          s+=pretty(indent,`x.getHeadSeq())+";\n"; 
          `x=`x.getTailSeq();
        }
        return s;
      }
    }
    %match(e) {
      Plus(e1,e2) -> { return "("+pretty(`e1)+" + "+pretty(`e2)+")"; }
      Mult(e1,e2) -> { return "("+pretty(`e1)+" * "+pretty(`e2)+")"; }
      Var(name) -> { return `name; }
      Cst(n) -> { return ""+`n; }
    } 
    return w+e.toString();
  }

}

