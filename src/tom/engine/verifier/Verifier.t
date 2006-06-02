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
import tom.engine.tools.SymbolTable;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
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
  %typeterm Map {
    implement { java.util.Map }
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
      Variable[astName=name] -> {
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
      GetSubterm[variable=Variable[astName=name], number=Number(index)] -> {
        // we will need to find the head symbol
        Term term = termFromTomName(`name);
        return `subterm(fsymbol("empty"),term,index);
      }
      GetSlot[astName=Name(symbolName),slotNameString=slotName,variable=Variable[astName=name]] -> {
        Term term = termFromTomName(`name);
        return `slot(fsymbol(symbolName),term,slotName);
      }
      TomTermToExpression(Variable[astName=name]) -> {
        Term term = termFromTomName(`name);
        return `term;
      }
      Cast[source=expr] -> {
        return termFromExpresssion(`expr);
      }
    }
    System.out.println("termFromExpresssion don't know how to handle this: " + expression);
    return `repr("autre foirade avec " + expression);
  }

  public String extractName(NameList nl) {
    %match(TomNameList nl) {
      (Name(name)) -> {
        return `name;
      }
    }
    return nl.toString();
  }

  public Expr exprFromExpression(Expression expression) {
    %match(Expression expression) {
      TrueTL()  -> { return `iltrue(subs(undefsubs())); }
      FalseTL() -> { return `ilfalse(); }
      EqualFunctionSymbol[exp1=Variable[astName=name],exp2=RecordAppl[nameList=symbolName]] -> {
        Term term = termFromTomName(`name);
        return `isfsym(term,fsymbol(extractName(symbolName)));
      }
      EqualFunctionSymbol[exp1=term1,exp2=RecordAppl[nameList=symbolName]] -> {
        return `isfsym(termFromTomTerm(term1),fsymbol(extractName(symbolName)));
      }
      EqualTerm[kid1=t1,kid2=t2] -> {
        return `eq(termFromTomTerm(t1),termFromTomTerm(t2));
      }
    }
    System.out.println("exprFromExpression don't know how to handle this: " + expression);
    return `ilfalse();
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
      TypedAction[positivePattern=positivePattern,negativePatternList=negativePatternList] -> {
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
      (Let|LetAssign)(UnamedVariable[],_,body) -> {
        return instrFromInstruction(`body);
      }
      CompiledPattern[automataInst=instr] -> {
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
      accept(positive,_) -> {
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

  %strategy substitutionCollector(outsubst:SubstRef) extends `Identity() {
    visit Expr {
      t@iltrue(subs(undefsubs())) -> {
        `Fail().visit(`t);
      }
      iltrue(x) -> {
        outsubst.set(`x);
      }
    }
  }
  public SubstitutionList collectSubstitutionInConstraint(Expr expr) {
    SubstRef output = new SubstRef(`subs());
    try {
      `mu(MuVar("x"),Try(Sequence(substitutionCollector(output),All(MuVar("x"))))).visit(expr);
    } catch (jjtraveler.VisitFailure e) {
      throw new TomRuntimeException("Strategy substitutionCollector failed");
    }
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
      TypedAction[positivePattern=positive,negativePatternList=negative]  -> {
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
      iltrue[subst=substitutionList] -> {
        return `iltrue(reduceSubstitutionWithMappingRules(substitutionList));
      }
      ilfalse[] -> {
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
        while(!reduced.isEmptyconcTerm()) {
          Term head = reduced.getHeadconcTerm();
          if (head.istau()) {
            TermList hl = applyMappingRules(head);
            while(!hl.isEmptyconcTerm()) {
              Term h = hl.getHeadconcTerm();
              res = `concTerm(res*,subterm(s,h,index));
              hl = hl.getTailconcTerm();
            }
          } else {
            res = `concTerm(res*,subterm(s,head,index));
          }
          reduced = reduced.getTailconcTerm();
        }
        return `concTerm(res*);
      }
      slot(s,t@slot[],slotName) -> {
        // first reduce the argument
        TermList reduced = applyMappingRules(`t);
        TermList res = `concTerm(trm);
        while(!reduced.isEmptyconcTerm()) {
          Term head = reduced.getHeadconcTerm();
          if (head.istau()) {
            TermList hl = applyMappingRules(head);
            while(!hl.isEmptyconcTerm()) {
              Term h = hl.getHeadconcTerm();
              res = `concTerm(res*,slot(s,h,slotName));
              hl = hl.getTailconcTerm();
            }
          } else {
            res = `concTerm(res*,slot(s,head,slotName));
          }
          reduced = reduced.getTailconcTerm();
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
        Term reducedl = reverseTermList((TermList)applyMappingRules(`lt)).getHeadconcTerm();
        Term reducedr = reverseTermList((TermList)applyMappingRules(`rt)).getHeadconcTerm();

        ExprList taill = `applyExprRules(eq(reducedl,reducedr));
        ExprList res = `concExpr(ex,taill*);
      }
      isfsym(t,symbol) -> {
        // first reduce the argument
        TermList reduced = applyMappingRules(`t);
        ExprList res = `concExpr(ex);
        while(!reduced.isEmptyconcTerm()) {
          Term head = reduced.getHeadconcTerm();
          res = `concExpr(res*,isfsym(head,symbol));
          reduced = reduced.getTailconcTerm();
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
        while(!exprList.isEmptyconcExpr()) {
          Expr localExpr = exprList.getHeadconcExpr();
          exprList = exprList.getTailconcExpr();
          newExprList = `concExpr(newExprList*,ilnot(localExpr));
        }
        return newExprList;
      }
      (iltrue|ilfalse)[] -> {
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
        if (!`t.isEmptysemicolon()) {
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
          return `iltrue(subs(undefsubs()));
        } else {
          return `ilfalse();
        }
      }
      accept[] -> {
        if (pil == goal) {
          return `iltrue(substitution);
        } else {
          return `ilfalse();
        }
      }
    }
    // default case, should not happen
    return `ilfalse();
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
          c.add(`derivrule(rulename,post,pre,dedexpr(concExpr(cond*,iltrue(subs(undefsubs()))))));
        }

        up = `ebs(env(e,iff),env(subs(undefsubs()),ip));
        rulename = "iffalse";

        pre_list = applySemanticsRules(up);
        it = pre_list.iterator();
        while(it.hasNext()) {
          DerivTree pre = (DerivTree) it.next();
          c.add(`derivrule(rulename,post,pre,dedexpr(concExpr(cond*,ilfalse()))));
        }
      }
      // axioms
      ebs(env(_,accept[]),env(subs(undefsubs()),accept[])) -> {
        c.add(`derivrule("axiom_accept",post,endderiv(),seq()));
      }
      ebs(env(_,refuse[]),env(subs(undefsubs()),refuse[])) -> {
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

  %strategy stratInstructionContains(goal:Instr,c:Collection) extends `Identity() {
    visit Instr {
      x -> {
        if (`x == goal) {
          c.add(goal);
          `Fail().visit(`x);
        }
      }
    }
  }
  protected boolean instructionContains(Instr i, Instr goal) {
    Collection collect = new HashSet();
    try {
      `mu(MuVar("x"),Try(Sequence(stratInstructionContains(goal,collect),All(MuVar("x"))))).visit(i);
    } catch(jjtraveler.VisitFailure e) {
      System.out.println("strategy instructionContains failed");
    }
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
  %strategy replaceVariableByTerm(map:Map) extends `Identity() {
    visit Term {
      t@tau(absvar(v@var[])) -> {
        if (map.containsKey(`v)) {
          return (Term)map.get(`v);
        }
        return `t;
      }
    }
  }

  public Term replaceVariablesInTerm(Term subject) {
    %match(Term subject) {
      appSubsT(sublist,term) -> {
        Map map = buildVariableMap(`sublist, new HashMap());
        Term t = `term;
        try {
          t = (Term) `TopDown(replaceVariableByTerm(map)).visit(`term);
        } catch (jjtraveler.VisitFailure e) {
          throw new TomRuntimeException("Strategy collectProgramVariables failed");
        }
        return t;
      }
    }
    return subject;
  }

  public Expr replaceVariablesInExpr(Expr subject) {
    %match(Expr subject) {
      appSubsE(sublist,term) -> {
        Map map = buildVariableMap(`sublist, new HashMap());
        Expr t = `term;
        try {
          t = (Expr) `TopDown(replaceVariableByTerm(map)).visit(`term);
        } catch (jjtraveler.VisitFailure e) {
          throw new TomRuntimeException("Strategy collectProgramVariables failed");
        }
        return t;
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
    Expr res = `ilfalse();
    try {
      res = (Expr) `InnermostId(booleanSimplifier).visit(expr);
    } catch (jjtraveler.VisitFailure e) {
      System.out.println("humm");
    }
    return res;
  }

  public class BooleanSimplifier extends IlBasicStrategy {
    public BooleanSimplifier() {
      super(`Identity());
    }

    public Expr visit_Expr(Expr arg) throws jjtraveler.VisitFailure {
      %match(Expr arg) {
        iland(ilfalse(),_) -> {
          return `ilfalse();
        }
        iland(_,ilfalse()) -> {
          return `ilfalse();
        }
        ilor(lt@iltrue[],_) -> {
          return `lt;
        }
        ilor(_,lt@iltrue[]) -> {
          return `lt;
        }
        ilor(ilfalse(),right) -> {
          return `right;
        }
        ilor(left,ilfalse()) -> {
          return `left;
        }
        ilnot(iltrue[]) -> {
          return `ilfalse();
        }
        ilnot(ilfalse[]) -> {
          return `iltrue(subs());
        }
      }
      return (Expr) any.visit(arg);
    }
  }

  TermList reverseTermList(TermList l) {
    %match(TermList l) {
      concTerm(h,t*) -> {
        TermList nt = reverseTermList(`t*);
        return `concTerm(nt*,h);
      }
    }
    return l;
  }

}
