/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2006, INRIA
 * Nancy, France.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 * Antoine Reilles        e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.engine.verifier;

import tom.engine.*;
import aterm.*;
import java.util.*;
import tom.library.traversal.*;
import tom.engine.tools.SymbolTable;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.il.*;
import tom.engine.adt.il.types.*;
import tom.engine.exception.TomRuntimeException;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

public class Verifier extends TomBase {

  // ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../adt/il/Il.tom }
  %include { mutraveler.tom }
  %typeterm Collection {
    implement { java.util.Collection }
  }
  // ------------------------------------------------------------

  private SymbolTable symbolTable;
  private boolean camlsemantics = false;

  public Verifier(boolean camlsemantics) {
    super();
    this.camlsemantics = camlsemantics;
  }

  public void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  public boolean isCamlSemantics() {
    return camlsemantics;
  }

  public Term termFromTomTerm(TomTerm tomterm) {
    %match(TomTerm tomterm) {
      ExpressionToTomTerm(expr) -> {
        return `termFromExpresssion(expr);
      }
      Variable(options,name,type,constraints) -> {
        return `termFromTomName(name);
      }
    }
    System.out.println("termFromTomTerm don't know how to handle this: " + tomterm);
    return `repr("foirade");
  }

  Variable variableFromTomName(TomName name) {
    %match(TomName name) {
      Name(stringname) -> {
        return `var(stringname);
      }
      PositionName(numberlist) -> {
        return `var(tomNumberListToString(numberlist));
      }
      EmptyName() -> {
        return `var("emptyName");
      }
    }
    return `var("error while building variable name");
  }

  Term termFromTomName(TomName name) {
    return `tau(absvar(variableFromTomName(name)));
  }

  public Term termFromExpresssion(Expression expression) {
    %match(Expression expression) {
      GetSubterm(codomain,Variable[astName=name], Number(index)) -> {
        // we will need to find the head symbol
        Term term = termFromTomName(`name);
        return `subterm(fsymbol("empty"),term,index);
      }
      GetSlot(codomain,Name(symbolName),slotName,Variable[astName=name]) -> {
        Term term = termFromTomName(`name);
        return `slot(fsymbol(symbolName),term,slotName);
      }
      TomTermToExpression(Variable[astName=name]) -> {
        Term term = termFromTomName(`name);
        return `term;
      }
      Cast(type,expr) -> {
        return termFromExpresssion(`expr);
      }
    }
    System.out.println("termFromExpresssion don't know how to handle this: " + expression);
    return `repr("autre foirade avec " + expression);
  }

  public String extractName(NameList nl) {
    %match(NameList nl) {
      (Name(name)) -> {
        return `name;
      }
    }
    return nl.toString();
  }

  public Expr exprFromExpression(Expression expression) {
    %match(Expression expression) {
      TrueTL()  -> { return `true(subs(undefsubs())); }
      FalseTL() -> { return `false(); }
      EqualFunctionSymbol(type,Variable[astName=name],RecordAppl[nameList=symbolName]) -> {
        Term term = termFromTomName(`name);
        return `isfsym(term,fsymbol(extractName(symbolName)));
      }
      EqualFunctionSymbol(type,term1,RecordAppl[nameList=symbolName]) -> {
        return `isfsym(termFromTomTerm(term1),fsymbol(extractName(symbolName)));
      }
      EqualTerm(type,t1,t2) -> {
        return `eq(termFromTomTerm(t1),termFromTomTerm(t2));
      }
    }
    System.out.println("exprFromExpression don't know how to handle this: " + expression);
    return `false();
  }

  public Instr instrFromInstructionList(InstructionList instrlist) {
    InstrList list = `semicolon();
    while (!instrlist.isEmpty()) {
      Instruction i = (Instruction) instrlist.getHead();
      instrlist = instrlist.getTail();
      if (!i.isCheckStamp()) {
        list = `semicolon(list*,instrFromInstruction(i));
      }
    }
    return `sequence(list);
  }

  public Instr instrFromInstruction(Instruction automata) {
    %match(Instruction automata) {
      TypedAction(action,positivePattern,negativePatternList) -> {
        return `accept(positivePattern,negativePatternList);
      }

      If(cond,ift,iff) -> {
        return `ITE(exprFromExpression(cond),
                    instrFromInstruction(ift),
                    instrFromInstruction(iff));
      }
      Let(Variable[astName=avar],expr,body) -> {
        Variable thevar = variableFromTomName(`avar);
        return `ILLet(thevar,
                      termFromExpresssion(expr),
                      instrFromInstruction(body));
      }
      LetAssign(Variable[astName=avar],expr,body) -> {
        Variable thevar = variableFromTomName(`avar);
        return `ILLet(thevar,
                      termFromExpresssion(expr),
                      instrFromInstruction(body));
      }
      (Let|LetAssign)(UnamedVariable[],expr,body) -> {
        return instrFromInstruction(`body);
      }
      CompiledPattern(patterns,instr) -> {
        return instrFromInstruction(`instr);
      }
      AbstractBlock(concInstruction(CheckStamp[],instr)) -> {
        return instrFromInstruction(`instr);
      }
      AbstractBlock(concInstruction(instrlist*)) -> {
        return instrFromInstructionList(`instrlist);
      }
      Nop() -> {
        // tom uses nop in the iffalse part of ITE
        return `refuse();
      }
    }
    System.out.println("instrFromInstruction don't know how to handle this : " + automata);
    return `refuse();
  }

  private SubstitutionList abstractSubstitutionFromAccept(Instr instr) {
    SubstitutionList substitution = `subs();
    %match(Instr instr) {
      accept(positive,negative) -> {
        Pattern positivePattern = (Pattern) `positive;
        %match(Pattern positivePattern) {
          Pattern[subjectList=subjectList] -> {
            TomList sl = `subjectList;
            while(!sl.isEmpty()) {
              TomTerm subject = sl.getHead();
              sl=sl.getTail();
              %match(TomTerm subject) {
                Variable[astName=name] -> {
                  substitution = `subs(substitution*,
                                       is(
                                          variableFromTomName(name),
                                          termFromTomTerm(subject)));
                }
              }
            }
          }
        }
      }
    }
    return substitution;
  }

  public Collection build_tree(Instruction automata) {
    // System.out.println("Build derivation tree for: " + automata);

    // collects the accept in the automata
    Collection localAccepts = collectAccept(automata);

    Iterator iter = localAccepts.iterator();
    Collection treeList = new HashSet();
    while(iter.hasNext()) {
        Instr localAccept = (Instr) iter.next();

        // builds the initial abstract substitution
        SubstitutionList initialsubstitution = abstractSubstitutionFromAccept(localAccept);
        Environment startingenv = `env(initialsubstitution,
                                       instrFromInstruction(automata));

        Deriv startingderiv = `ebs(startingenv,
                                   env(subs(undefsubs()),localAccept));

        Collection treeListPre = applySemanticsRules(startingderiv);
        // replace substitutions in trees
        Iterator it = treeListPre.iterator();
        while(it.hasNext()) {
            DerivTree tree = (DerivTree) it.next();
            SubstitutionList outputsubst = getOutputSubstitution(tree);
            tree = replaceUndefinedSubstitution(tree,outputsubst);
            treeList.add(tree);
        }
    }

    return treeList;
  }

  public Map getConstraints(Instruction automata) {
    // collects the accept in the automata
    Collection localAccepts = collectAccept(automata);

    Iterator iter = localAccepts.iterator();
    Map constraintList = new HashMap();
    while(iter.hasNext()) {
        Instr localAccept = (Instr) iter.next();

        // builds the initial abstract substitution
        SubstitutionList initialsubstitution = abstractSubstitutionFromAccept(localAccept);
        Expr constraints = buildConstraint(initialsubstitution,
                                           instrFromInstruction(automata),
                                           localAccept);
        constraintList.put(localAccept,constraints);
    }
    return constraintList;
  }

  public SubstitutionList collectSubstitutionInConstraint(Expr expr) {
    Collect2 substitutionCollector = new Collect2() {
      public boolean apply(ATerm subject, Object astore) {
        SubstRef outsubst = (SubstRef) astore;
        %match(Expr subject) {
          true(subs(undefsubs())) -> {
            return false;
          }
          true(x) -> {
            outsubst.set(`x);
          }
        }//end match
        return true;
      }//end apply
    }; //end new
    SubstRef output = new SubstRef(`subs());
    traversal().genericCollect(expr,substitutionCollector,output);
    return output.get();
  }

  %strategy outputSubstitutionCollector(outsubst:SubstRef) extends `Identity() {
    visit Deriv {
      ebs(env(e,accept[]),env(subs(undefsubs()),accept[])) -> {
        outsubst.set(`e);
      }
    }
  }

  public SubstitutionList getOutputSubstitution(DerivTree subject) {
    SubstRef output = new SubstRef(`subs());
    try {
      `TopDown(outputSubstitutionCollector(output)).visit(subject);
    } catch (jjtraveler.VisitFailure e) {
      throw new TomRuntimeException("Strategy outputSubstitutionCollector failed");
    }
    return output.get();
  }

  %strategy acceptCollector(store:Collection) extends `Identity() {
    visit Instruction {
      TypedAction(action,positive,negative)  -> {
        store.add(`accept(positive,negative));
      }
    }
  }

  public Collection collectAccept(Instruction subject) {
    Collection result = new HashSet();
    try {
      `TopDown(acceptCollector(result)).visit(subject);
    } catch (jjtraveler.VisitFailure e) {
      throw new TomRuntimeException("Strategy collectAccept failed");
    }
    return result;
  }


  /**
   * The axioms the mapping has to verify
   */
  protected Seq seqFromTerm(Term sp) {
    TermList ded = `concTerm(sp);
    %match(Term sp) {
      appSubsT[] -> {
        TermList follow = applyMappingRules(replaceVariablesInTerm(sp));
        ded = `concTerm(ded*,follow*);
      }
    }
    return `dedterm(concTerm(ded*));
  }

  protected ExprList exprListFromExpr(Expr sp) {
    ExprList ded = `concExpr(sp);
    %match(Expr sp) {
      appSubsE[] -> {
        ExprList follow = applyExprRules(replaceVariablesInExpr(sp));
        ded = `concExpr(ded*,follow*);
      }
    }

    // System.out.println("dedexpr gives: " + ded);
    return `ded;
  }

  protected SubstitutionList reduceSubstitutionWithMappingRules(SubstitutionList subst) {
    %match(SubstitutionList subst) {
      subs() -> {
        return subst;
      }
      subs(is(v,term),t*) -> {
        SubstitutionList tail = reduceSubstitutionWithMappingRules(`t*);
        return `subs(is(v,reduceTermWithMappingRules(replaceVariablesInTerm(appSubsT(tail*,term)))),tail*);
      }
      subs(undefsubs(),t*) -> {
        SubstitutionList tail = reduceSubstitutionWithMappingRules(`t*);
        return `subs(undefsubs(),tail*);
      }
    }
    return subst;
  }
  protected Expr reduceWithMappingRules(Expr ex) {
    %match(Expr ex) {
      eq(tau(tl),tau(tr)) -> {
        return `teq(tl,tr);
      }
      isfsym(tau(t),symbol) -> {
        return `tisfsym(t,symbol);
      }
      eq(lt,rt) -> {
        // first reduce the argument
        return reduceWithMappingRules(`eq(reduceTermWithMappingRules(lt),reduceTermWithMappingRules(rt)));
      }
      isfsym(t,symbol) -> {
        return reduceWithMappingRules(`isfsym(reduceTermWithMappingRules(t),symbol));
      }
      ilnot(e) -> {
        return `ilnot(reduceWithMappingRules(e));
      }
      true[subst=substitutionList] -> {
        return `true(reduceSubstitutionWithMappingRules(substitutionList));
      }
      false[] -> {
        return `ex;
      }
      iland(lt,rt) -> {
        return `iland(reduceWithMappingRules(lt),reduceWithMappingRules(rt));
      }
      ilor(lt,rt) -> {
        return `ilor(reduceWithMappingRules(lt),reduceWithMappingRules(rt));
      }
    }
    System.out.println("reduceWithMappingRules : nothing applies to:" + ex);
    return `ex;
  }

  protected Term reduceTermWithMappingRules(Term trm) {
    %match(Term trm) {
      tau[] -> {
        return `trm;
      }
      subterm(s,t@subterm[],index) -> {
        return `reduceTermWithMappingRules(subterm(s,reduceTermWithMappingRules(t),index));
      }
      slot(s,t@slot[],slotName) -> {
        return `reduceTermWithMappingRules(slot(s,reduceTermWithMappingRules(t),slotName));
      }
      subterm(s,tau(t),index) -> {
        // we shall test if term t has symbol s
        AbsTerm term = `st(s,t,index);
        return `tau(term);
      }
      slot(s,tau(t),slotName) -> {
        // we shall test if term t has symbol s
        AbsTerm term = `sl(s,t,slotName);
        return `tau(term);
      }
    }
    System.out.println("reduceTermWithMappingRules : nothing applies to:" + trm);
    return `trm;
  }

  protected TermList applyMappingRules(Term trm) {
    %match(Term trm) {
      tau[] -> {
        return `concTerm(trm);
      }
      subterm(s,t@subterm[],index) -> {
        // first reduce the argument
        TermList reduced = applyMappingRules(`t);
        TermList res = `concTerm(trm);
        while(!reduced.isEmpty()) {
          Term head = reduced.getHead();
          if (head.isTau()) {
            TermList hl = applyMappingRules(head);
            while(!hl.isEmpty()) {
              Term h = hl.getHead();
              res = `concTerm(res*,subterm(s,h,index));
              hl = hl.getTail();
            }
          } else {
            res = `concTerm(res*,subterm(s,head,index));
          }
          reduced = reduced.getTail();
        }
        return `concTerm(res*);
      }
      slot(s,t@slot[],slotName) -> {
        // first reduce the argument
        TermList reduced = applyMappingRules(`t);
        TermList res = `concTerm(trm);
        while(!reduced.isEmpty()) {
          Term head = reduced.getHead();
          if (head.isTau()) {
            TermList hl = applyMappingRules(head);
            while(!hl.isEmpty()) {
              Term h = hl.getHead();
              res = `concTerm(res*,slot(s,h,slotName));
              hl = hl.getTail();
            }
          } else {
            res = `concTerm(res*,slot(s,head,slotName));
          }
          reduced = reduced.getTail();
        }
        return `concTerm(res*);
      }
      subterm(s,tau(t),index) -> {
        // we shall test if term t has symbol s
        AbsTerm term = `st(s,t,index);
        return `concTerm(trm,tau(term));
      }
      slot(s,tau(t),slotName) -> {
        // we shall test if term t has symbol s
        AbsTerm term = `sl(s,t,slotName);
        return `concTerm(trm,tau(term));
      }
    }
    System.out.println("apply TermRules : nothing applies to:" + trm);
    return `concTerm(trm);
  }

  protected ExprList applyExprRules(Expr ex) {
    %match(Expr ex) {
      eq(tau(tl),tau(tr)) -> {
        return `concExpr(ex,teq(tl,tr));
      }
      isfsym(tau(t),symbol) -> {
        return `concExpr(ex,tisfsym(t,symbol));
      }
      eq(lt,rt) -> {
        // first reduce the argument
        Term reducedl = ((TermList)applyMappingRules(`lt).reverse()).getHead();
        Term reducedr = ((TermList)applyMappingRules(`rt).reverse()).getHead();

        ExprList taill = `applyExprRules(eq(reducedl,reducedr));
        ExprList res = `concExpr(ex,taill*);
      }
      isfsym(t,symbol) -> {
        // first reduce the argument
        TermList reduced = applyMappingRules(`t);
        ExprList res = `concExpr(ex);
        while(!reduced.isEmpty()) {
          Term head = reduced.getHead();
          res = `concExpr(res*,isfsym(head,symbol));
          reduced = reduced.getTail();
        }
        %match(ExprList res) {
          concExpr(hl*,tail) -> {
            ExprList taill = `applyExprRules(tail);
            return `concExpr(hl*,taill*);
          }
        }
      }
      ilnot(e) -> {
        ExprList exprList = `applyExprRules(e);
        ExprList newExprList = `concExpr(ex);
        while(!exprList.isEmpty()) {
          Expr localExpr = exprList.getHead();
          exprList = exprList.getTail();
          newExprList = `concExpr(newExprList*,ilnot(localExpr));
        }
        return newExprList;
      }
      (true|false)[] -> {
        return `concExpr(ex);
      }
    }
    System.out.println("apply ExprRules : nothing applies to:" + ex);
    return `concExpr(ex);
  }

  protected Expr buildConstraint(SubstitutionList substitution, Instr pil,Instr goal) {
    %match(Instr pil) {
      sequence(semicolon(h,t*)) -> {
        Expr goalFromHead = buildConstraint(substitution,`h,goal);
        if (!`t.isEmpty()) {
          Expr refuseFromHead = buildConstraint(substitution,`h,`refuse());
          Expr goalFromTail = buildConstraint(substitution,`sequence(t),goal);
          if(this.isCamlSemantics()) {
            return `ilor(goalFromHead,iland(refuseFromHead,goalFromTail));
          } else {
            return `ilor(goalFromHead,goalFromTail);
          }
        } else {
          return goalFromHead;
        }
      }
      ILLet(x,u,i) -> {
        // update the substitution
        Term t = replaceVariablesInTerm(`appSubsT(substitution,u));
        substitution = `subs(substitution*,is(x,t));
        //return `iland(eq(tau(absvar(x)),u),buildConstraint(substitution,i,goal));
        return `buildConstraint(substitution,i,goal);
      }
      ITE(exp,ift,iff) -> {
        Expr closedExpr = replaceVariablesInExpr(`appSubsE(substitution,exp));
        Expr constraintTrue  = `iland(closedExpr,buildConstraint(substitution,ift,goal));
        Expr constraintFalse = `iland(ilnot(closedExpr),buildConstraint(substitution,iff,goal));
        return `ilor(constraintTrue,constraintFalse);
      }
      refuse[] -> {
        if (pil == goal) {
          return `true(subs(undefsubs()));
        } else {
          return `false();
        }
      }
      accept[] -> {
        if (pil == goal) {
          return `true(substitution);
        } else {
          return `false();
        }
      }
    }
    // default case, should not happen
    return `false();
  }

  protected Collection applySemanticsRules(Deriv post) {
    Collection c = new HashSet();
    %match(Deriv post) {
      ebs(env(e,sequence(semicolon(h,t*))),env(subs(undefsubs()),ip)) -> {
        if(`instructionContains(h,ip)) {
          // ends the derivation
          Deriv up = `ebs(env(e,h),env(subs(undefsubs()),ip));
          Collection pre_list = applySemanticsRules(up);

          Iterator it = pre_list.iterator();
          while(it.hasNext()) {
            DerivTree pre = (DerivTree) it.next();
            c.add(`derivrule("seqa",post,pre,seq()));
          }
        } else {
          // continue the derivation with t
          Deriv up = `ebs(env(e,sequence(t*)),env(subs(undefsubs()),ip));
          Collection post_list = applySemanticsRules(up);

          if(this.isCamlSemantics()) {
            up = `ebs(env(e,h),env(subs(undefsubs()),refuse()));
            Collection pre_list = applySemanticsRules(up);
            Iterator it = pre_list.iterator();
            while(it.hasNext()) {
              DerivTree pre = (DerivTree) it.next();
              Iterator it2 = post_list.iterator();
              while(it2.hasNext()) {
                DerivTree pre2 = (DerivTree) it2.next();
                c.add(`derivrule2("seqb",post,pre,pre2,seq()));
              }
            }
          } else {
            Iterator it = post_list.iterator();
            while(it.hasNext()) {
              DerivTree pre = (DerivTree) it.next();
              c.add(`derivrule2("seqb",post,endderiv(),pre,seq()));
            }
          }
        }
      }
      // let rule
      ebs(env(e,ILLet(x,u,i)),env(subs(undefsubs()),ip)) -> {
        // build condition
        Seq cond = seqFromTerm(`appSubsT(e,u));
        // find "t"
        Term t = null;
        %match(Seq cond) {
          dedterm(concTerm(_*,r)) -> { t = `r; }
            _ -> { if (t == null) {
              System.out.println("seqFromTerm has a problem with " + cond);
            }
          }
        }
        Deriv up = `ebs(
            env(subs(e*,is(x,t)),i),
            env(subs(undefsubs()),ip)
            );
        Collection pre_list = applySemanticsRules(up);
        Iterator it = pre_list.iterator();
        while(it.hasNext()) {
          DerivTree pre = (DerivTree) it.next();
          c.add(`derivrule("let",post,pre,cond));
        }
      }
      // iftrue/iffalse rule
      ebs(env(e,ITE(exp,ift,iff)),env(subs(undefsubs()),ip)) -> {
        // build condition
        ExprList cond = exprListFromExpr(`appSubsE(e,exp));

        Deriv up = `ebs(env(e,ift),env(subs(undefsubs()),ip));
        String rulename = "iftrue";

        Collection pre_list = applySemanticsRules(up);
        Iterator it = pre_list.iterator();
        while(it.hasNext()) {
          DerivTree pre = (DerivTree) it.next();
          c.add(`derivrule(rulename,post,pre,dedexpr(concExpr(cond*,true(subs(undefsubs()))))));
        }

        up = `ebs(env(e,iff),env(subs(undefsubs()),ip));
        rulename = "iffalse";

        pre_list = applySemanticsRules(up);
        it = pre_list.iterator();
        while(it.hasNext()) {
          DerivTree pre = (DerivTree) it.next();
          c.add(`derivrule(rulename,post,pre,dedexpr(concExpr(cond*,false()))));
        }
      }
      // axioms
      ebs(env(e,accept[]),env(subs(undefsubs()),accept[])) -> {
        c.add(`derivrule("axiom_accept",post,endderiv(),seq()));
      }
      ebs(env(e,refuse[]),env(subs(undefsubs()),refuse[])) -> {
        c.add(`derivrule("axiom_refuse",post,endderiv(),seq()));
      }
      _ -> {
        if (c.isEmpty()) {
            //System.out.println("Error " + post);
        }
      }
    }
    return c;
  }

  protected boolean instructionContains(Instr i, Instr goal) {
    Collect3 collect_find = new Collect3() {
      public boolean apply(ATerm subject, Object astore, Object arg) {
        Collection c = (Collection) astore;
        Instr lgoal = (Instr) arg;
        if (subject instanceof Instr) {
          if (subject == lgoal) {
            c.add(lgoal);
            return false;
          }
          return true;
        } else {
          return true;
        }
      }//end apply
    };
    Collection collect = new HashSet();
    traversal().genericCollect(i,collect_find,collect,goal);
    return !collect.isEmpty();
  }

/**
 * To replace undefsubst in tree by the computed value
 * which leads to axiom
 */
  %strategy replaceUndefsubs(arg:SubstitutionList) extends `Identity() {
    visit SubstitutionList {
      (undefsubs()) -> {
        return arg;
      }
    }
  }

  private DerivTree replaceUndefinedSubstitution(DerivTree subject,
                                      SubstitutionList subs) {
    try {
      subject = (DerivTree) `TopDown(replaceUndefsubs(subs)).visit(subject);
    } catch (jjtraveler.VisitFailure e) {
      throw new TomRuntimeException("Strategy collectProgramVariables failed");
    }
    return subject;
  }

  %typeterm SubstRef {
    implement { SubstRef }
  }
  static private class SubstRef {
    private SubstitutionList sublist;
    public SubstRef(SubstitutionList slist) {
      sublist = slist;
    }
    public void set(SubstitutionList ssublist) {
      this.sublist = ssublist;
    }
    public SubstitutionList get() {
      return sublist;
    }
  }

  /**
   * These functions deals with substitution application
   */

  Replace2 replaceVariableByTerm = new Replace2() {
      public ATerm apply(ATerm subject, Object arg1) {
        %match(Term subject) {
          tau(absvar(v@var(name))) -> {
            Map map = (Map) arg1;
            if (map.containsKey(`v)) {
              return (Term)map.get(`v);
            }
            return (Term)subject;
          }
        }
        /* Default case : Traversal */
        return traversal().genericTraversal(subject,this,arg1);
      } // end apply
    };

  public Term replaceVariablesInTerm(Term subject) {
    %match(Term subject) {
      appSubsT(sublist,term) -> {
        Map map = buildVariableMap(`sublist, new HashMap());
        return (Term) replaceVariableByTerm.apply(`term,map);
      }
    }
    return subject;
  }

  public Expr replaceVariablesInExpr(Expr subject) {
    %match(Expr subject) {
      appSubsE(sublist,term) -> {
        Map map = buildVariableMap(`sublist, new HashMap());
        return (Expr) replaceVariableByTerm.apply(`term,map);
      }
    }
    return subject;
  }

  String tomNumberListToString(TomNumberList numberList) {
    String result = "";
    while(!numberList.isEmpty()) {
      TomNumber number = numberList.getHead();
      numberList = numberList.getTail();
      %match(TomNumber number) {
        Number(n) -> {
          result = result + "Number" + Integer.toString(`n);
        }
        MatchNumber(Number(n)) -> {
          result = result + "Match" + Integer.toString(`n);
        }
        PatternNumber(Number(n)) -> {
          result = result + "Pattern" + Integer.toString(`n);
        }
        IndexNumber(Number(n)) -> {
          result = result + "Index" + Integer.toString(`n);
        }
        Begin(Number(n)) -> {
          result = result + "Begin" + Integer.toString(`n);
        }
        End(Number(n)) -> {
          result = result + "End" + Integer.toString(`n);
        }
        AbsVar(Number(n)) -> {
          result = result + "AbsVar" + Integer.toString(`n);
        }
        RenamedVar(tomName) -> {
          String identifier = "Empty";
          %match(TomName tomName) {
            Name(name) -> {
              identifier = `name;
            }
            PositionName(localNumberList) -> {
              identifier = tomNumberListToString(`localNumberList);
            }
          }
          result = result + "RenamedVar" + identifier;
        }
        NameNumber(tomName) -> {
          String identifier = "Empty";
          %match(TomName tomName) {
            Name(name) -> {
              identifier = `name;
            }
            PositionName(localNumberList) -> {
              identifier = tomNumberListToString(`localNumberList);
            }
          }
          result = result + "NameNumber" + identifier;
        }
        RuleVar() -> {
          result = "RuleVar" + result;
        }
      }
    }
    return result;
  }

  private Map buildVariableMap(SubstitutionList sublist, Map map) {
    %match(SubstitutionList sublist) {
      ()                -> { return map; }
      (undefsubs(),t*)  -> { return buildVariableMap(`t,map);}
      (is(v,term),t*)   -> {
        map.put(`v,`term);
        return buildVariableMap(`t,map);
      }
    }
    return null;
  }

  public void mappingReduce(Map input) {
    Iterator it = input.keySet().iterator();
    while(it.hasNext()) {
      Object key = it.next();
      Expr value = (Expr) input.get(key);
      input.put(key,reduceWithMappingRules(value));
    }
  }

  public void booleanReduce(Map input) {
    Iterator it = input.keySet().iterator();
    while(it.hasNext()) {
      Object key = it.next();
      Expr value = (Expr) input.get(key);
      input.put(key,booleanSimplify(value));
    }
  }

  public Expr booleanSimplify(Expr expr) {
    VisitableVisitor booleanSimplifier = new BooleanSimplifier();
    Expr res = `false();
    try {
      res = (Expr) `InnermostId(booleanSimplifier).visit(expr);
    } catch (jjtraveler.VisitFailure e) {
      System.out.println("humm");
    }
    return res;
  }

  public class BooleanSimplifier extends IlVisitableFwd {
    public BooleanSimplifier() {
      super(`Identity());
    }

    public Expr visit_Expr(Expr arg) throws jjtraveler.VisitFailure {
      %match(Expr arg) {
        iland(false(),right) -> {
          return `false();
        }
        iland(left,false()) -> {
          return `false();
        }
        ilor(lt@true[],right) -> {
          return `lt;
        }
        ilor(left,lt@true[]) -> {
          return `lt;
        }
        ilor(false(),right) -> {
          return `right;
        }
        ilor(left,false()) -> {
          return `left;
        }
        ilnot(true[]) -> {
          return `false();
        }
        ilnot(false[]) -> {
          return `true(subs());
        }
      }
      return (Expr) any.visit(arg);
    }
  }

}
