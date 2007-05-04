/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2007, INRIA
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
import tom.engine.adt.zenon.*;
import tom.engine.adt.zenon.types.*;

import tom.engine.exception.TomRuntimeException;


public class ZenonOutput {

  // ------------------------------------------------------------
  %include { adt/il/Il.tom }
  %include { adt/zenon/Zenon.tom }
  
  %typeterm Map {
    implement { java.util.Map }
    is_sort(t) { t instanceof java.util.Map }
  }
  // ------------------------------------------------------------

  private Verifier verifier;
  private TomIlTools tomiltools;

  public ZenonOutput(Verifier verifier) {
    this.verifier = verifier;
    this.tomiltools = new TomIlTools(verifier);
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
        Pattern positivePattern = Pattern.fromTerm(`positive);
        PatternList negativePatternList = PatternList.fromTerm(`negative);
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
      iltrue[] -> { return `ztrue();}
      ilfalse() -> { return `zfalse();}
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
          concTerm(_*,tl,tr) -> {
            return `zeq(ztermFromTerm(tl),ztermFromTerm(tr));
          }
        }
      }
      dedexpr(exprlist) -> {
        %match(ExprList `exprlist) {
          concExpr(_*,t,iltrue[]) -> {
            return zexprFromExpr(`t);
          }
        }
      }
      dedexpr(exprlist) -> {
        %match(ExprList `exprlist) {
          concExpr(_*,t,ilfalse()) -> {
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

  private Map ztermVariableMapFromSubstitutionList(SubstitutionList sublist, Map map) {
    %match(SubstitutionList sublist) {
      ()                -> { return map; }
      (undefsubs(),t*)  -> {
        return ztermVariableMapFromSubstitutionList(`t,map);
      }
      (is(var(name),term),t*)   -> {
        map.put(`name,ztermFromTerm(`term));
        return ztermVariableMapFromSubstitutionList(`t,map);
      }
    }
    throw new TomRuntimeException(
        "verifier: strange substitution list: " + sublist);
  }
}
