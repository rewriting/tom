/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this list of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce:w
 the above copyright
 *	notice, this list of conditions and the following disclaimer in the
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

package analysis;

import analysis.cfg.*;
import analysis.cfg.types.*;
import analysis.ast.*;
import analysis.ast.types.*;

import tom.library.sl.*;

import java.util.*;


public class Analyser{
  
  %include {sl.tom }
  %include {util/HashMap.tom}
  %include {util/ArrayList.tom}
  %include {cfg/Cfg.tom}
  
  /**
    Definition des predicats 	 	 
   */

  %typeterm VariableRef {
    implement {VariableRef}
    is_sort(t) { t instanceof VariableRef } 
  }

  %strategy IsNotUsed(ref:VariableRef) extends `Identity() {
    visit Term {
      t@Var(var) -> {
        if(`var.equals(ref.getvariable())) return (Term) `Fail().visit(`t);
      }
    }

    visit Cfg {
        Affect(ast,_) -> {
        `TopDown(this).visitLight(`ast.getChildAt(1)); //find use in the term affected
      }
         BeginIf(ast,_,_) -> {
        `TopDown(this).visitLight(`ast.getChildAt(0)); //find us in the boolean expression
      }
         BeginWhile(ast,_) -> {
        `TopDown(this).visitLight(`ast.getChildAt(0)); //find us in the boolean expression
      }
    }
  }

  %strategy IsFree(ref:VariableRef) extends `Fail() {
    visit Cfg{
      n@Free(var,_) -> {
        if(`var.equals(ref.getvariable())) {
          return `n;
        }
      }
    }
  }



  %strategy FindAffect(var:VariableRef) extends `Fail() {
    visit Cfg{
      x@Affect(ast,_) -> {
        var.setvariable(((Variable) `ast.getChildAt(0)));
        return `x;
      }
    }
  }

  %strategy IsAffect(var:VariableRef) extends `Fail() {
    visit Cfg{
      x@Affect(ast,_) -> {
        if (var.getvariable().equals(`ast.getChildAt(0))){
          return `x;
        }
      }
    }
  }

  %strategy Collect(list:ArrayList) extends `Identity() {
    visit Cfg{
      x -> {
        list.add(`x); 
      }
    }
  }

   %strategy PrintVar(ref:VariableRef) extends `Identity() {
    visit Cfg{
      _ -> {
        System.out.println("---print---"+ref.getvariable());      
      }
    }
  }

 %strategy Print() extends `Identity() {
    visit Cfg{
      inst@_ -> {
        System.out.println("---print---"+`inst);      
      }
    }
  }


  public ArrayList collectNotUsedAffectations(Cfg cfg){
    ArrayList list = new ArrayList();
    VariableRef var = new VariableRef();
    try {
    `TopDown(StrictDeRef(Try(Sequence(Sequence(FindAffect(var),AXCtl(StrictDeRef(AUCtl(StrictDeRef(IsNotUsed(var)),StrictDeRef(OrCtl(IsAffect(var),IsFree(var))))))),Collect(list))))).visit(cfg);
    } catch (VisitFailure f) {
      return null;
    }
    return list;
  }

  public ArrayList collectOnceUsedAffectations(Cfg cfg){
    ArrayList list = new ArrayList();
    VariableRef var = new VariableRef();

    //s1 = not(modified(var))
    Strategy s1 = `Not(IsAffect(var));

    //s2 = (used(var) and AX(notUsedCond(var)
    Strategy s2 =  
      `AndCtl(
          Not(IsNotUsed(var)),
          All(StrictDeRef(mu(MuVar("x"),
              Choice(
                OrCtl(IsAffect(var),IsFree(var)),
                Sequence(IsNotUsed(var),All(StrictDeRef(MuVar("x"))))
                )
              )
            )
          ));
    //onceUsedCond AX(A(s1 U s2))  
    Strategy onceUsed = `AXCtl(StrictDeRef(AUCtl(StrictDeRef(s1),StrictDeRef(s2))));

    try{
      `TopDown(StrictDeRef(Try(Sequence(Sequence(FindAffect(var),onceUsed),Collect(list))))).visit(cfg);
    } catch (VisitFailure e) {
      return null;
    }
    return list;
  }


  public static void main(String[] args){
    Variable var_x = `Name("x");
    Variable var_y = `Name("y");
    Variable var_z = `Name("z");

    Ast letassignx = `LetAssign(var_x,f(a(),b()));
    Ast lety = `Let(var_y,g(Var(var_x)),concAst());
    Ast letrefx = `LetRef(var_x,g(a()),concAst(letassignx,lety));


    Ast success = `concAst();
    Ast failure = `concAst(letrefx);

    Ast cond = `If(True(),success,failure);
    Ast letz = `Let(var_z,f(a(),b()),concAst());
    Ast subject = `concAst(cond,letz);


    Cfg cfg = (Cfg) `ConcCfg(
        BeginIf(cond,RefCfg("success"),RefCfg("failure")), 
        LabCfg("success",Nil(RefCfg("letz"))),
        LabCfg("failure",Affect(letrefx,RefCfg("letassignx"))),
        LabCfg("letassignx",Affect(letassignx,RefCfg("lety"))),
        LabCfg("lety",Affect(lety,RefCfg("freey"))),
        LabCfg("freey",Free(var_y,RefCfg("freex"))),
        LabCfg("freex",Free(var_x,RefCfg("letz"))),
        LabCfg("letz",Affect(letz,RefCfg("freez"))),
        LabCfg("freez",Free(var_z,RefCfg("end"))),
        LabCfg("end",End())
        ).expand();

    Analyser analyser = new Analyser();
    try{
      VariableRef var= new VariableRef();
      System.out.println("\nCfg with positions:\n" + cfg);
      List l1 = analyser.collectNotUsedAffectations(cfg);
      List l2 = analyser.collectOnceUsedAffectations(cfg);
      Iterator it1 = l1.listIterator();
      System.out.println("\nNot Used Affectations:");
      while(it1.hasNext()){
        Cfg affectation = (Cfg) it1.next();
        System.out.println(affectation.getChildAt(0).getChildAt(0)+"-"+affectation.getChildAt(0).getChildAt(1));
      }
      Iterator it2 = l2.listIterator();
      System.out.println("\nOnce Used Affectations:");
      while(it2.hasNext()){
        Cfg affectation = (Cfg) it2.next();
        System.out.println(affectation.getChildAt(0).getChildAt(0)+"-"+affectation.getChildAt(0).getChildAt(1));
      }

    }catch(Exception e){
      e.printStackTrace();
    }
  }
}//class Analyser

class VariableRef{

  private Variable var;

  public VariableRef(){
  }

  public VariableRef(Variable var){
    this.var = var;
  }

  public Variable getvariable(){
    return var;
  }

  public void setvariable(Variable var){
    this.var=var;
  }

}//class VariableRef

