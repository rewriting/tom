/*
 *
 * GOM
 *
 * Copyright (c) 2007-2014, Universite de Lorraine, Inria
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
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.expander.rule;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.tree.Tree;
import tom.gom.adt.rule.RuleAdaptor;
import java.util.logging.Level;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import tom.gom.GomMessage;
import tom.gom.adt.gom.types.*;
import tom.gom.adt.rule.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class RuleExpander {

  %include { ../../adt/gom/Gom.tom}
  %include { ../../adt/rule/Rule.tom }
  %include { ../../../library/mapping/java/sl.tom}

  private ModuleList moduleList;

  public RuleExpander(ModuleList data) {
    this.moduleList = data;
  }

  public HookDeclList expandRules(String ruleCode) {
    RuleLexer lexer = new RuleLexer(new ANTLRStringStream(ruleCode));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    RuleParser parser = new RuleParser(tokens);
    RuleList rulelist = `RuleList();
    try {
      Tree ast = (Tree) parser.ruleset().getTree();
      rulelist = (RuleList) RuleAdaptor.getTerm(ast);
    } catch (org.antlr.runtime.RecognitionException e) {
      GomMessage.error(getLogger(), null, 0, GomMessage.rulesParsingFailure);
      return `ConcHookDecl();
    }
    return expand(rulelist);
  }

  protected HookDeclList expand(RuleList rulelist) {
    HookDeclList hookList = `ConcHookDecl();
    /* collect all rules for a given symbol */
    Map<OperatorDecl,RuleList> rulesForOperator =
      new HashMap<OperatorDecl,RuleList>();
    %match(rulelist) {
      RuleList(_*,rl@(Rule|ConditionalRule)[lhs=Appl[symbol=symbol]],_*) -> {
        OperatorDecl decl = getOperatorDecl(`symbol);
        if (null != decl) {
          RuleList rules = rulesForOperator.get(decl);
          if (null == rules) {
            rulesForOperator.put(decl,`RuleList(rl));
          } else {
            rulesForOperator.put(decl,`RuleList(rules*,rl));
          }
        } else {
          GomMessage.warning(getLogger(), null, 0, 
              GomMessage.discardRuleWarning, /*XXX:prettyprint*/`(rl));
        }
      }
    }
    /* Generate a construction hook for each constructor */
    for (OperatorDecl opDecl : rulesForOperator.keySet()) {
      TypedProduction prod = opDecl.getProd();
      %match(prod) {
        /* Syntactic operator */
        Slots[Slots=slotList] -> {
          SlotList args = opArgs(`slotList,1);
          String hookCode =
            generateHookCode(args, rulesForOperator.get(opDecl));
          hookList =
            `ConcHookDecl(hookList*,
                MakeHookDecl(CutOperator(opDecl),args,Code(hookCode),HookKind("rules"),true()));
        }
        /* Variadic operator */
        Variadic[Sort=sort] -> {
          RuleList rules = rulesForOperator.get(opDecl);
          /* Handle rules for empty: there should be at least one */
          int count = 0;
          RuleList nonEmptyRules = rules;
          %match(rules) {
            RuleList(R1*,
                rule@(Rule|ConditionalRule)[lhs=Appl[args=TermList()]],
                R2*) -> {
              count++;
              nonEmptyRules = `RuleList(R1*,R2*);
              String hookCode =
                generateHookCode(`ConcSlot(),`RuleList(rule));
              hookList =
                `ConcHookDecl(hookList*,
                    MakeHookDecl(CutOperator(opDecl),ConcSlot(),Code(hookCode),HookKind("rules"),true()));
            }
          }
          if (count>1) {
            GomMessage.warning(getLogger(), null, 0, 
                GomMessage.multipleRulesForEmpty, opDecl.getName());
          }
          /* Then handle rules for insert */
          if (!nonEmptyRules.isEmptyRuleList()) {
            SlotList args = `ConcSlot(Slot("head",sort),Slot("tail",opDecl.getSort()));
            String hookCode =
              generateVariadicHookCode(args, nonEmptyRules);
            hookList =
              `ConcHookDecl(hookList*,
                  MakeHookDecl(CutOperator(opDecl),args,Code(hookCode),HookKind("rules"),true()));
          }
        }
      }
    }
    return hookList;
  }

  private String generateHookCode(SlotList slotList, RuleList ruleList) {
    StringBuilder output = new StringBuilder();
    if(slotList.isEmptyConcSlot()) {
      while(!ruleList.isEmptyRuleList()) {
        Rule rule = ruleList.getHeadRuleList();
        ruleList = ruleList.getTailRuleList();
        %match(rule) {
          Rule(Appl[],rhs) -> {
            output.append("    return `");
            genTerm(`rhs,output);
            output.append(";\n");
          }
          ConditionalRule(Appl[],rhs,cond) -> {
            output.append("    %match{\n");
            genCondition(`cond,output);
            output.append(" -> { return `");
            genTerm(`rhs,output);
            output.append("; }\n");
            output.append("}\n");
          }
        }
      }

    } else {
      output.append("    %match(");
      matchArgs(slotList,output,1);
      output.append(") {\n");
      while(!ruleList.isEmptyRuleList()) {
        Rule rule = ruleList.getHeadRuleList();
        ruleList = ruleList.getTailRuleList();
        %match(rule) {
          Rule(Appl[args=argList],rhs) -> {
            genTermList(`argList,output);
            output.append(" -> { return `");
            genTerm(`rhs,output);
            output.append("; }\n");
          }
          ConditionalRule(Appl[args=argList],rhs,cond) -> {
            genTermList(`argList,output);
            output.append(" && ");
            genCondition(`cond,output);
            output.append(" -> { return `");
            genTerm(`rhs,output);
            output.append("; }\n");
          }
        }
      }
      output.append("    }\n");
    }
    return output.toString();
  }

  private String generateVariadicHookCode(SlotList slotList, RuleList ruleList) {
    StringBuilder output = new StringBuilder();
    output.append("    %match(realMake(head,tail)) {\n");
    while(!ruleList.isEmptyRuleList()) {
      Rule rule = ruleList.getHeadRuleList();
      ruleList = ruleList.getTailRuleList();
      %match(rule) {
        (Rule|ConditionalRule)[lhs=Appl[symbol=listOp,args=TermList(var@(UnnamedVarStar|VarStar)[],_*)]] -> {
            String varname = "_";
            %match(var) {
                VarStar(name) -> { varname = `name; }
            }
            GomMessage.warning(getLogger(),null,0,
                GomMessage.variadicRuleStartingWithStar,
                new Object[]{`(listOp),varname});
        }
      }
      %match(rule) {
        Rule(lhs,rhs) -> {
          genTerm(`lhs,output);
          output.append(" -> { return `");
          genTerm(`rhs,output);
          output.append("; }\n");
        }
        ConditionalRule(lhs,rhs,cond) -> {
          genTerm(`lhs,output);
          output.append(" && ");
          genCondition(`cond,output);
          output.append(" -> { return `");
          genTerm(`rhs,output);
          output.append("; }\n");
        }
      }
    }
    output.append("    }\n");
    return output.toString();
  }
  private void genTermList(TermList list, StringBuilder output) {
    %match(list) {
      TermList() -> { return; }
      TermList(h,t*) -> {
        genTerm(`h,output);
        if (!`t.isEmptyTermList()) {
          output.append(", ");
        }
        genTermList(`t*,output);
      }
    }
  }

  private void genTerm(Term termArg, StringBuilder output) {
    %match(termArg) {
      Appl(symbol,args) -> {
        output.append(`symbol);
        output.append("(");
        genTermList(`args, output);
        output.append(")");
      }
      At(name,term) -> {
        output.append(`name);
        output.append("@");
        genTerm(`term,output);
      }
      Anti(term) -> {
        output.append("!");
        genTerm(`term,output);
      }
      UnnamedVar() -> {
        output.append("_");
      }
      UnnamedVarStar() -> {
        output.append("_*");
      }
      Var(name) -> {
        output.append(`name);
      }
      VarStar(name) -> {
        output.append(`name);
        output.append("*");
      }
      BuiltinInt(i) -> {
        output.append(`i);
      }
      BuiltinString(s) -> {
        output.append(`s);
      }
    }
  }

  private void genCondition(Condition cond, StringBuilder output) {
    %match(cond) {
      CondEquals[t1=term1,t2=term2] -> {
        genTerm(`term1,output);
        output.append(" == ");
        genTerm(`term2,output);
      }
      CondNotEquals[t1=term1,t2=term2] -> {
        genTerm(`term1,output);
        output.append(" != ");
        genTerm(`term2,output);
      }
      CondLessEquals[t1=term1,t2=term2] -> {
        genTerm(`term1,output);
        output.append(" <= ");
        genTerm(`term2,output);
      }
      CondLessThan[t1=term1,t2=term2] -> {
        genTerm(`term1,output);
        output.append(" < ");
        genTerm(`term2,output);
      }
      CondGreaterEquals[t1=term1,t2=term2] -> {
        genTerm(`term1,output);
        output.append(" >= ");
        genTerm(`term2,output);
      }
      CondGreaterThan[t1=term1,t2=term2] -> {
        genTerm(`term1,output);
        output.append(" > ");
        genTerm(`term2,output);
      }
      CondMatch[t1=term1,t2=term2] -> {
        genTerm(`term1,output);
        output.append(" << ");
        genTerm(`term2,output);
      }
      CondAnd(head,tail*) -> {
        if(`tail* != `CondAnd()) {
          output.append("(");
          genCondition(`head,output);
          output.append(" && ");
          genCondition(`tail*,output);
          output.append(")");
        } else {
          genCondition(`head,output);
        }
      }
      CondOr(head,tail*) -> {
        if(`tail* != `CondOr()) {
          output.append("(");
          genCondition(`head,output);
          output.append(" || ");
          genCondition(`tail*,output);
          output.append(")");
        } else {
          genCondition(`head,output);
        }
      }
    }
  }

  private void matchArgs(SlotList sl, StringBuilder output, int count) {
    %match(sl) {
      ConcSlot() -> { return; }
      ConcSlot(Slot[Sort=sort],t*) -> {
        %match(sort) {
          (SortDecl|BuiltinSortDecl)[Name=sName] -> {
            output.append(`sName);
            output.append(" arg_"+count);
          }
        }
        if (!`t.isEmptyConcSlot()) {
          output.append(", ");
        }
        matchArgs(`t*,output,count+1);
      }
    }
  }

  private SlotList opArgs(SlotList slots, int count) {
    %match(slots) {
      ConcSlot() -> {
        return `ConcSlot();
      }
      ConcSlot(Slot[Sort=slotSort],ts*) -> {
        SlotList tail = opArgs(`ts,count+1);
        return `ConcSlot(Slot("arg_"+count,slotSort),tail*);
      }
    }
    throw new GomRuntimeException("RuleExpander:opArgs failed "+slots);
  }
  protected OperatorDecl getOperatorDecl(String name) {
    OperatorDecl decl = null;
    OpRef ref = new OpRef();
    try {
      `TopDown(GetOperatorDecl(ref,name)).visit(moduleList);
    } catch (tom.library.sl.VisitFailure e) {
      throw new GomRuntimeException("Unexpected strategy failure!");
    }
    if (ref.val == null) {
      GomMessage.error(getLogger(), null, 0, GomMessage.unknownConstructor, name);
    }
    return ref.val;
  }
  static class OpRef { OperatorDecl val; }
  %typeterm OpRef {
    implement { OpRef }
    is_sort(t) { ($t instanceof OpRef) }
  }
  %strategy GetOperatorDecl(opref:OpRef,opName:String) extends Identity() {
    visit OperatorDecl {
      op@OperatorDecl[Name=name] -> {
        if (`name.equals(opName)) {
          opref.val = `op;
        }
      }
    }
  }

  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
