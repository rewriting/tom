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
import aterm.pure.*;
import java.util.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.il.*;
import tom.engine.adt.il.types.*;
import tom.engine.adt.zenon.*;
import tom.engine.adt.zenon.types.*;
import tom.engine.exception.TomRuntimeException;


public class ZenonOutput {

  // ------------------------------------------------------------
  %include { adt/il/Il.tom }
  %include { adt/zenon/Zenon.tom }
  %include { mutraveler.tom }
  %typeterm Map {
    implement { java.util.Map }
  }
  // ------------------------------------------------------------

  private Verifier verifier;
  private TomIlTools tomiltools;

  public ZenonOutput(Verifier verifier) {
    this.verifier = verifier;
    this.tomiltools = new TomIlTools(verifier);
  }

  public Collection zspecSetFromDerivationTreeSet(Collection derivationSet) {
    Collection resset = new HashSet();
    Iterator it = derivationSet.iterator();
    while(it.hasNext()) {
      DerivTree tree = (DerivTree) it.next();
      ZSpec spec = zspecFromDerivationTree(tree);
      resset.add(spec);
    }
    return resset;
  }

  public Collection zspecSetFromConstraintMap(Map constraintMap) {
    Collection resset = new HashSet();
    Iterator it = constraintMap.entrySet().iterator();
    while(it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      ZSpec spec = zspecFromMapEntry(entry);
      resset.add(spec);
    }
    return resset;
  }

  public ZSpec zspecFromMapEntry(Map.Entry entry) {
    Instr accept = (Instr) entry.getKey();
    Expr constraint = (Expr) entry.getValue();

    List subjectList = new LinkedList();
    ZExpr pattern = null;
    ZExpr negpattern = null;

    // theorem to prove
    %match(Instr accept) {
        accept(positive,negative) -> {
        Pattern positivePattern = (Pattern)`positive;
        PatternList negativePatternList = (PatternList)`negative;
        // we need the substitution to generate the pattern part of the theorem
        SubstitutionList subsList = verifier.collectSubstitutionInConstraint(constraint);
        Map variableMap = ztermVariableMapFromSubstitutionList(subsList,
                                                               new HashMap());
        tomiltools.getZTermSubjectListFromPattern(positivePattern,
                                                  subjectList,
                                                  variableMap);
        pattern = tomiltools.patternToZExpr(positivePattern,variableMap);
        if (verifier.isCamlSemantics()) {
          negpattern = tomiltools.patternToZExpr(negativePatternList,variableMap);
        }
      }
    }

    ZExpr zenonConstraint = zexprFromExpr(constraint);

    ZExpr theorem = null;
    if (pattern != null && zenonConstraint != null) {
      if(verifier.isCamlSemantics() && negpattern != null) {
        theorem = `zequiv(zand(pattern,znot(negpattern)),zenonConstraint);
      } else {
        theorem = `zequiv(pattern,zenonConstraint);
      }
    }

    // now we have to to build the axiom list, starting from the
    // signature. Again, the TomIlTools will be useful, it has access
    // to TomSignature and Zenon signature

    // collects symbols in pattern
    Collection symbols = tomiltools.collectSymbols(pattern);
    // generates the axioms for this set of symbols
    ZAxiomList symbolsAxioms = tomiltools.symbolsDefinition(symbols);
    // generates axioms for all subterm operations
    ZAxiomList subtermAxioms = tomiltools.subtermsDefinition(symbols);

    Iterator iter = subjectList.iterator();
    while(iter.hasNext()) {
      ZTerm input = (ZTerm)iter.next();
      theorem = `zforall(input,ztype("T"),theorem);
    }
    ZSpec spec = `zthm(theorem,zby(symbolsAxioms*,subtermAxioms*));

    return spec;
  }

  public ZSpec zspecFromDerivationTree(DerivTree tree) {

    Map variableset = new HashMap();
    tree = collectProgramVariables(tree,variableset);

    // Use a TreeMap to have the conditions sorted
    Map conditions = new TreeMap();
    collectConstraints(tree,conditions);
    Map conds = new TreeMap();

    List subjectList = new LinkedList();
    ZExpr pattern = null;
    ZExpr negpattern = null;
    // theorem to prove
    %match(DerivTree tree) {
      (derivrule|derivrule2)
        [post=ebs[rhs=env(subsList,accept(positive,negative))]] -> {
        Pattern positivePattern = (Pattern)`positive;
        PatternList negativePatternList = (PatternList)`negative;
        Map variableMap = ztermVariableMapFromSubstitutionList(`subsList,
                                                               new HashMap());
        tomiltools.getZTermSubjectListFromPattern(positivePattern,
                                                  subjectList,
                                                  variableMap);
        pattern = tomiltools.patternToZExpr(positivePattern,variableMap);
        if (verifier.isCamlSemantics()) {
          negpattern = tomiltools.patternToZExpr(negativePatternList,variableMap);
        }
      }
    }

    ZExpr constraints = `ztrue();
    // we consider only the interesting conditions : dedexpr
    Iterator it = conditions.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      Seq value = (Seq) entry.getValue();
      if (value.isDedexpr()) {
        conds.put(((String) entry.getKey()),
                  zexprFromSeq(cleanSeq(value)));
      }
    }
    it = conds.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      ZExpr value = (ZExpr) entry.getValue();
      constraints = `zand(constraints,value);
    }
    ZExpr theorem = null;
    if (pattern != null && constraints != null) {
      if(verifier.isCamlSemantics() && negpattern != null) {
        theorem = `zequiv(zand(pattern,znot(negpattern)),constraints);
      } else {
        theorem = `zequiv(pattern,constraints);
      }
    }

    // now we have to to build the axiom list, starting from the
    // signature.

    // collects symbols in pattern
    Collection symbols = tomiltools.collectSymbols(pattern);
    // generates the axioms for this set of symbols
    ZAxiomList symbolsAxioms = tomiltools.symbolsDefinition(symbols);
    // generates axioms for all subterm operations
    ZAxiomList subtermAxioms = tomiltools.subtermsDefinition(symbols);

    Iterator iter = subjectList.iterator();
    while(iter.hasNext()) {
      ZTerm input = (ZTerm)iter.next();
      theorem = `zforall(input,ztype("T"),theorem);
    }
    ZSpec spec = `zthm(theorem,zby(symbolsAxioms*,subtermAxioms*));

    return spec;
  }


  /**
   * collects all variable names in the DerivTree, and give a name to _'s
   */
  %strategy programVariablesCollector(store:Map) extends `Identity() {
    visit Variable {
      var(name) -> {
        String newname = `name;
        if (store.containsKey(`name)){
          newname = (String) store.get(`name);
        } else {
          if (`name.startsWith("[") && `name.endsWith("]")) {
            newname = "X_" + store.size();
          }
          store.put(`name,newname);
        }
        return `var(newname);
      }
    }
  }

  DerivTree collectProgramVariables(DerivTree tree, Map variables) {
    try {
      tree = (DerivTree) `TopDown(programVariablesCollector(variables)).visit(tree);
    } catch (jjtraveler.VisitFailure e) {
      throw new TomRuntimeException("Strategy collectProgramVariables failed");
    }
    return tree;
  }

  ZTerm ztermFromTerm(Term term) {
    %match(Term term) {
      tau(absTerm) -> {
        return ztermFromAbsTerm(`absTerm);
      }
      repr[] -> {
        return `zvar("Error in ztermFromTerm repr");
      }
      subterm[] -> {
        return `zvar("Error in ztermFromTerm subterm");
      }
      slot[] -> {
        return `zvar("Error in ztermFromTerm "+ term +" slot");
      }
      appSubsT[] -> {
        // probleme: la substitution devrait etre appliquee
        return `zvar("Error in ztermFromTerm appsubsT ");
      }
    }
    return `zvar("match vide dans ztermFromTerm");
  }

  ZExpr zexprFromExpr(Expr expr) {
    %match(Expr expr) {
      true[] -> { return `ztrue();}
      false() -> { return `zfalse();}
      tisfsym(absterm,s) -> {
        return `zisfsym(ztermFromAbsTerm(absterm),zsymbolFromSymbol(s));
      }
      teq(absterml,abstermr) -> {
        return `zeq(ztermFromAbsTerm(absterml),ztermFromAbsTerm(abstermr));
      }
      isfsym[] -> {
        // this should not occur
        return `zisfsym(zvar("Error in zexprFromExpr"),zsymbol("isfsym"));
      }
      eq[] -> {
        // this should not occur
        return `zeq(zvar("Error in zexprFromExpr"),zvar("eq"));
      }
      appSubsE[] -> {
        // this should not occur
        return `zeq(zvar("Error in zexprFromExpr"),zvar("appSubsE"));
      }
      iland(lt,rt) -> {
        return `zand(zexprFromExpr(lt),zexprFromExpr(rt));
      }
      ilor(lt,rt) -> {
        return `zor(zexprFromExpr(lt),zexprFromExpr(rt));
      }
      ilnot(nex) -> {
        return `znot(zexprFromExpr(nex));
      }
    }
    return `zeq(zvar("Error in zexprFromExpr"),zvar("end " + expr.toString()));
  }

  ZSymbol zsymbolFromSymbol(Symbol symb) {
    %match(Symbol symb) {
      fsymbol(name) -> {
        return `zsymbol(name);
      }
    }
    return `zsymbol("random");
  }

  ZExpr zexprFromSeq(Seq seq) {
    %match(Seq seq) {
      seq() -> {
        return `ztrue();
      }
      dedterm(termlist) -> {
        %match(TermList `termlist) {
          concTerm(X*,tl,tr) -> {
            return `zeq(ztermFromTerm(tl),ztermFromTerm(tr));
          }
        }
      }
      dedexpr(exprlist) -> {
        %match(ExprList `exprlist) {
          concExpr(_*,t,true[]) -> {
            return zexprFromExpr(`t);
          }
        }
      }
      dedexpr(exprlist) -> {
        %match(ExprList `exprlist) {
          concExpr(_*,t,false()) -> {
            return `znot(zexprFromExpr(t));
          }
        }
      }
    }
    return `ztrue();
  }

  ZTerm ztermFromAbsTerm(AbsTerm absterm) {
    %match(AbsTerm absterm) {
      absvar(var(name)) -> {
        return `zvar(name);
      }
      st(_,t,index) -> {
        return `zst(ztermFromAbsTerm(t),index);
      }
      sl(_,t,name) -> {
        return `zsl(ztermFromAbsTerm(t),name);
      }
    }
    return `zvar("Error in ztermFromAbsTerm");
  }

  Seq cleanSeq(Seq seq) {
    %match(Seq seq) {
      seq() -> { return seq; }
      dedterm(concTerm(_*,t,v)) -> {
          return `dedterm(concTerm(t,v));
      }
      dedexpr(concExpr(_*,t,v)) -> {
        return `dedexpr(concExpr(t,v));
      }
    }
    return seq;
  }

  private Map ztermVariableMapFromSubstitutionList(SubstitutionList sublist, Map map) {
    %match(SubstitutionList sublist) {
      ()                -> { return map; }
      (undefsubs(),t*)  -> { return ztermVariableMapFromSubstitutionList(`t,map);}
      (is(var(name),term),t*)   -> {
        map.put(`name,ztermFromTerm(`term));
        return ztermVariableMapFromSubstitutionList(`t,map);
      }
    }
    throw new TomRuntimeException("verifier: strange substitution list: "+sublist);
  }

  public void collectConstraints(DerivTree tree, Map conditions) {
    %match(DerivTree tree) {
      derivrule[pre=pre,cond=condition] -> {
        String condname = Integer.toString(conditions.size()+1);
        conditions.put(condname,`condition);
        collectConstraints(`pre,conditions);
      }
      derivrule2[pre=pre,pre2=pre2,cond=condition] -> {
        String condname = Integer.toString(conditions.size()+1);
        conditions.put(condname,`condition);
        collectConstraints(`pre,conditions);
        collectConstraints(`pre2,conditions);
      }
    }
  }

}
