/*
 *
 * GOM
 *
 * Copyright (C) 2007, INRIA
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
import java.util.logging.Level;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import tom.gom.adt.gom.types.*;
import tom.gom.adt.rule.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class RuleExpander {

  %include { ../../adt/gom/Gom.tom}
  %include { ../../adt/rule/Rule.tom }
  %include { sl.tom}

  private ModuleList moduleList;

  public RuleExpander(ModuleList data) {
    this.moduleList = data;
  }

  public HookDeclList expandRules(String ruleCode) {
    RuleLexer lexer = new RuleLexer(new ANTLRStringStream(ruleCode));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    RuleParser parser = new RuleParser(tokens);
    parser.setTreeAdaptor(new ASTAdaptor());
    RuleList rulelist = `RuleList();
    try {
      ASTTree ast = (ASTTree)parser.ruleset().getTree();
      rulelist = (RuleList) ast.getTerm();
    } catch (org.antlr.runtime.RecognitionException e) {
      getLogger().log(Level.SEVERE, "Cannot parse rules",
          new Object[]{});
      return `concHookDecl();
    }
    return expand(rulelist);
  }

  protected HookDeclList expand(RuleList rulelist) {
    HookDeclList hookList = `concHookDecl();
    /* collect all rules for a given symbol */
    Map rulesForOperator = new HashMap();
    %match(rulelist) {
      RuleList(_*,rl@(Rule|ConditionalRule)[lhs=Appl[symbol=symbol]],_*) -> {
        OperatorDecl decl = getOperatorDecl(`symbol);
        if(null!=decl) {
          RuleList rules = (RuleList) rulesForOperator.get(decl);
          if (null == rules) {
            rulesForOperator.put(decl,`RuleList(rl));
          } else {
            rulesForOperator.put(decl,`RuleList(rules*,rl));
          }
        } else {
          getLogger().log(Level.WARNING, "Discard rule \"{0}\"",
              new Object[]{/*XXX:prettyprint*/`(rl)});
        }
      }
    }
    /* Generate a construction hook for each constructor */
    Iterator it = rulesForOperator.keySet().iterator();
    while (it.hasNext()) {
      OperatorDecl opDecl = (OperatorDecl) it.next();
      TypedProduction prod = opDecl.getProd();
      %match(prod) {
        /* Syntactic operator */
        Slots[Slots=slotList] -> {
          SlotList args = opArgs(`slotList,1);
          String hookCode =
            generateHookCode(args, (RuleList) rulesForOperator.get(opDecl));
          hookList =
            `concHookDecl(hookList*,
                MakeHookDecl(CutOperator(opDecl),args,Code(hookCode)));
        }
        /* Variadic operator */
        Variadic[Sort=sort] -> {
          RuleList rules = (RuleList) rulesForOperator.get(opDecl);
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
                generateHookCode(`concSlot(),`RuleList(rule));
              hookList =
                `concHookDecl(hookList*,
                    MakeHookDecl(CutOperator(opDecl),concSlot(),Code(hookCode)));
            }
          }
          if (count>1) {
            getLogger().log(Level.WARNING, "Multiple rules for empty {0}",
                new Object[]{ opDecl.getName() });
          }
          /* Then handle rules for insert */
          if (!nonEmptyRules.isEmptyRuleList()) {
            SlotList args = `concSlot(Slot("head",sort),Slot("tail",opDecl.getSort()));
            String hookCode =
              generateVariadicHookCode(args, nonEmptyRules);
            hookList =
              `concHookDecl(hookList*,
                  MakeHookDecl(CutOperator(opDecl),args,Code(hookCode)));
          }
        }
      }
    }
    return hookList;
  }

  private String generateHookCode(SlotList slotList, RuleList ruleList) {
    StringBuffer output = new StringBuffer();
    if(slotList.isEmptyconcSlot()) {
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
            output.append("    if `(");
            genCondition(`cond,output);
            output.append(") { return `");
            genTerm(`rhs,output);
            output.append("; }\n");
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
            output.append(" -> { if `(");
            genCondition(`cond,output);
            output.append(") { return `");
            genTerm(`rhs,output);
            output.append("; } }\n");
          }
        }
      }
      output.append("    }\n");
    }
    return output.toString();
  }

  private String generateVariadicHookCode(SlotList slotList, RuleList ruleList) {
    StringBuffer output = new StringBuffer();
    output.append("    %match(realMake(head,tail)) {\n");
    while(!ruleList.isEmptyRuleList()) {
      Rule rule = ruleList.getHeadRuleList();
      ruleList = ruleList.getTailRuleList();
      %match(rule) {
        Rule(lhs,rhs) -> {
          genTerm(`lhs,output);
          output.append(" -> { return `");
          genTerm(`rhs,output);
          output.append("; }\n");
        }
        ConditionalRule(lhs,rhs,cond) -> {
          genTerm(`lhs,output);
          output.append(" -> { if `(");
          genCondition(`cond,output);
          output.append(") { return `");
          genTerm(`rhs,output);
          output.append("; } }\n");
        }
      }
    }
    output.append("    }\n");
    return output.toString();
  }
  private void genTermList(TermList list, StringBuffer output) {
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

  private void genTerm(Term term, StringBuffer output) {
    %match(term) {
      Appl(symbol,args) -> {
        output.append(`symbol);
        output.append("(");
        genTermList(`args, output);
        output.append(")");
      }
      Var(name) -> {
        output.append(`name);
      }
      BuiltinInt(i) -> {
        output.append(`i);
      }
      BuiltinString(s) -> {
        output.append(`s);
      }
    }
  }

  private void genCondition(Condition cond, StringBuffer output) {
    %match(cond) {
      CondTerm[t=term] -> {
        genTerm(`term,output);
      }
      CondEquals[t1=term1,t2=term2] -> {
        output.append("(");
        genTerm(`term1,output);
        output.append(" == ");
        genTerm(`term2,output);
        output.append(")");
      }
      CondNotEquals[t1=term1,t2=term2] -> {
        output.append("(");
        genTerm(`term1,output);
        output.append(" != ");
        genTerm(`term2,output);
        output.append(")");
      }
    }
  }

  private void matchArgs(SlotList sl, StringBuffer output, int count) {
    %match(sl) {
      concSlot() -> { return; }
      concSlot(Slot[Sort=sort],t*) -> {
        %match(sort) {
          (SortDecl|BuiltinSortDecl)[Name=sName] -> {
            output.append(`sName);
            output.append(" arg_"+count);
          }
        }
        if (!`t.isEmptyconcSlot()) {
          output.append(", ");
        }
        matchArgs(`t*,output,count+1);
      }
    }
  }

  private SlotList opArgs(SlotList slots, int count) {
    %match(slots) {
      concSlot() -> {
        return `concSlot();
      }
      concSlot(Slot[Sort=slotSort],ts*) -> {
        SlotList tail = opArgs(`ts,count+1);
        return `concSlot(Slot("arg_"+count,slotSort),tail*);
      }
    }
    throw new GomRuntimeException("RuleExpander:opArgs failed "+slots);
  }
  protected OperatorDecl getOperatorDecl(String name) {
    OperatorDecl decl = null;
    OpRef ref = new OpRef();
    `TopDown(GetOperatorDecl(ref,name)).fire(moduleList);
    if (ref.val == null) {
      getLogger().log(Level.SEVERE, "Unknown constructor {0}",
          new Object[]{name});
    }
    return ref.val;
  }
  static class OpRef { OperatorDecl val; }
  %typeterm OpRef { implement { OpRef } }
  %strategy GetOperatorDecl(opref:OpRef,opName:String) extends Identity() {
    visit OperatorDecl {
      op@OperatorDecl[Name=name] -> {
        if (`name == opName) {
          opref.val = `op;
        }
      }
    }
  }

  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
