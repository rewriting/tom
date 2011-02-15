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
package antipattern.baralgo;

import aterm.*;
import aterm.pure.*;

import antipattern.*;
import antipattern.term.*;
import antipattern.term.types.*;


import tom.library.sl.*;

public class SimplifySystemRule {

  %include{ ../term/Term.tom }
  %include{ sl.tom }

  %op Constraint AreSymbolsEqual(s1:Term, s2:Term){
    is_fsym(t) { false }
    make(t1,t2) { areSymbolsEqual($t1, $t2) }
  }	

  %op Constraint DecomposeList(l1:TermList, l2:TermList){
    is_fsym(t) { false }
    make(t1,t2) { decomposeList($t1, $t2) }
  }

  %op Constraint ContainsVariable(v:Term, l:AConstraintList){
    is_fsym(t) { false }
    make(t1,t2) { containsVariable($t1, $t2) }
  }

  %op Constraint ApplyReplaceStrategy(var:Term,value:Term,l:AConstraintList){
    is_fsym(t) { false }
    make(t1,t2,t3) { applyReplaceStrategy($t1,$t2,$t3) }
  }

  private Constraint decomposeList(TermList l1, TermList l2){		

    // System.out.println("In decompose");
    AConstraintList l = `concAnd();

    while(!l1.isEmptyconcTerm()) {
      l = `concAnd(Match(l1.getHeadconcTerm(),l2.getHeadconcTerm()),l*);
      l1 = l1.getTailconcTerm();
      l2 = l2.getTailconcTerm();					
    }
    return `And(l/*.reverseConstraintList()*/);
  }

  private Constraint areSymbolsEqual(Term t1, Term t2){

    // System.out.println("In areSymbolsEq");

    %match(Term t1, Term t2){
      Appl(name1,_),Appl(name2,_) -> {
        if (`name1.equals(`name2)){
          return `True();
        }
      }
    }				
    return `False();
  }

  private Constraint containsVariable(Term v, AConstraintList l){

    // not the most efficient method, because
    // it doesn't stop when an occurence is found
    ContainsTerm ct = new ContainsTerm(v,`Identity());
    try{
      `InnermostId(ct).visitLight(l);
    }catch(VisitFailure e){
      System.out.println("Exception:" + e.getMessage());
      System.exit(0);
    }

    System.out.println("FOUND=" + ct.getFound());

    return ct.getFound() == true ? `True():`False();
    // return l.match(v) == null ? `False():`True();
  }

  private Constraint applyReplaceStrategy(Term var, Term value, AConstraintList l){

    Strategy rule,ruleStrategy;            
    rule = new ReplaceSystem(var,value, `Identity());
    ruleStrategy = `InnermostId(rule);

    Constraint res = null;

    try{
      res = (Constraint) ruleStrategy.visitLight(`And(l));
    }catch(VisitFailure e){
      System.out.println("Exception:" + e.getMessage());
      System.exit(0);
    }

    return res;

  }

  public Constraint applyRules(Constraint c){

    Constraint ret = null;

    %match(Constraint c){
      Match(a,b) -> {
        ret = `Match(a,b);
        System.out.println("RET=" + ret);
      }
    }
    //System.out.println("GIGI=" + `Match(Appl("a",concTerm()),Appl("a",concTerm())));
    return ret;
  }	

  public Constraint Match(Term p, Term s) {
    %match(p,s) {
      // NegDef
      Anti(Appl(name1, args1)),Appl(name2, args2) -> { return `Neg(Match(Appl(name1, args1),Appl(name2, args2))); }
      // Decompose
      Appl(name,a1),Appl(name,a2) -> { return `DecomposeList(a1,a2); }
      // SymbolClash
      a1@Appl(name1,args1),a2@Appl(name2,args2) -> { 
        if(`False() == `AreSymbolsEqual(a1,a2)) return `False();
      }
      // Delete
      Appl(name,concTerm()),Appl(name,concTerm()) -> { return `True()		; }
    }
    return null;
  }

  public Constraint And(AConstraintList l) {
    %match(l) {
      // Replace
      concAnd(X*,match@Match(var@Variable(name),s),Y*) -> { 
        if(`True() == `ContainsVariable(var,concAnd(X*,Y*))) {
          return `And(concAnd(match,ApplyReplaceStrategy(var,s,concAnd(X*,Y*))));
        }
      }

        // PropagateClash
        concAnd(_*,False(),_*) -> { return `False(); }
        // PropagateSuccess
        concAnd() -> { return `True(); }
        concAnd(x) -> { return `x; }
        concAnd(X*,True(),Y*) -> { return `And(concAnd(X*,Y*)); }
      }
    return null;
    }

    public Constraint Neg(Constraint c) {
      %match(c) {
        // BooleanSimplification
        Neg(x) -> { return `x; }
        True() -> { return `False(); }
        False() -> { return `True(); }
      }
    return null;
    }
  }

  class ContainsTerm extends AbstractStrategyBasic {

    private boolean found = false;
    private Term objToSearchFor = null;

    public ContainsTerm(Term obj, Strategy visitor) {		
      super(visitor);
      this.objToSearchFor = obj;
      this.found = false;
    }

    public <T> T visitLight(T o, Introspector i) throws VisitFailure { 
      if(o == objToSearchFor) {
        found = true;
        System.out.println("!!FOUND!!");
      } 
      return o;
    }

    public boolean getFound(){
      return found;
    }
  }
