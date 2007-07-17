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

import tom.gom.adt.rule.RuleAbstractType;
import tom.gom.adt.rule.types.*;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.*;

public class ASTTree extends CommonTree {

  %include { ../../adt/rule/Rule.tom }
  private RuleAbstractType inAstTerm = null;
  private int childIndex = 0;

	public ASTTree(CommonTree node) {
		super(node);
		this.token = node.token;
    initAstTerm(node.token);
	}

	public ASTTree(Token t) {
		this.token = t;
    initAstTerm(t);
	}

  public RuleAbstractType getTerm() {
    return inAstTerm;
  }

  private void initAstTerm(Token t) {
    if(null==t) {
      return;
    }
    switch (t.getType()) {
      case RuleParser.RULELIST:
        inAstTerm = `RuleList();
        break;
      case RuleParser.RULE:
        inAstTerm = `Rule(BuiltinInt(-1),BuiltinInt(-1));
        break;
      case RuleParser.CONDRULE:
        inAstTerm =
          `ConditionalRule(
              BuiltinInt(-1),BuiltinInt(-1),CondTerm(BuiltinInt(-1)));
        break;
      case RuleParser.CONDTERM:
        inAstTerm =
          `CondTerm(BuiltinInt(-1));
        break;
      case RuleParser.CONDEQUALS:
        inAstTerm =
          `CondEquals(BuiltinInt(-1),BuiltinInt(-1));
        break;
      case RuleParser.CONDNOTEQUALS:
        inAstTerm =
          `CondNotEquals(BuiltinInt(-1),BuiltinInt(-1));
        break;
      case RuleParser.CONDLEQ:
        inAstTerm =
          `CondLessEquals(BuiltinInt(-1),BuiltinInt(-1));
        break;
      case RuleParser.CONDLT:
        inAstTerm =
          `CondLessThan(BuiltinInt(-1),BuiltinInt(-1));
        break;
      case RuleParser.CONDGEQ:
        inAstTerm =
          `CondGreaterEquals(BuiltinInt(-1),BuiltinInt(-1));
        break;
      case RuleParser.CONDGT:
        inAstTerm =
          `CondGreaterThan(BuiltinInt(-1),BuiltinInt(-1));
        break;
      case RuleParser.CONDMETHOD:
        inAstTerm =
          `CondMethod(BuiltinInt(-1),"-1",BuiltinInt(-1));
        break;
      case RuleParser.INT:
        inAstTerm = `BuiltinInt(Integer.parseInt(token.getText()));
        break;
      case RuleParser.APPL:
        inAstTerm = `Appl(token.getText(),TermList());
        break;
      case RuleParser.LAB:
        inAstTerm = `LabTerm(token.getText(),BuiltinInt(-1));
        break;
      case RuleParser.REF:
        inAstTerm = `RefTerm(token.getText());
        break;
      case RuleParser.STRING:
        inAstTerm = `BuiltinString(token.getText());
        break;
      case RuleParser.ID:
        inAstTerm = `Var(token.getText());
        break;
      case RuleParser.RPAR:
      case RuleParser.IF:
      case RuleParser.WS:
      case RuleParser.LPAR:
      case RuleParser.EOF:
      case RuleParser.ARROW:
      case RuleParser.COMA:
      case RuleParser.ESC:
    }
  }

  public void addChild(Tree t) {
    super.addChild(t);
    if (null==t) {
      return;
    }
    ASTTree tree = (ASTTree) t;
    RuleAbstractType trm = tree.getTerm();
    if (null==trm || inAstTerm==null) {
      return;
    }
    %match(inAstTerm) {
      RuleList(X*) -> {
        Rule rl = (Rule) trm;
        inAstTerm = `RuleList(X*,rl);
      }
    }
    %match(inAstTerm) {
      Rule[] -> {
        inAstTerm = (RuleAbstractType) inAstTerm.setChildAt(childIndex,trm);
        childIndex++;
      }
      ConditionalRule[] -> {
        inAstTerm = (RuleAbstractType) inAstTerm.setChildAt(childIndex,trm);
        childIndex++;
      }
    }
    %match(inAstTerm) {
      (CondTerm|CondEquals|CondNotEquals|CondLessEquals|
       CondLessThan|CondGreaterEquals|CondGreaterThan)[] -> {
        inAstTerm = (RuleAbstractType) inAstTerm.setChildAt(childIndex,trm);
        childIndex++;
      }
      CondMethod(t1,n,t2) -> {
        if(childIndex == 1) {
          Term tm = (Term) trm;
          inAstTerm = `CondMethod(t1,tm.getname(),t2);
        } else {
          int idx = childIndex;
          if(childIndex >= 2)
            idx--;
          inAstTerm = (RuleAbstractType) inAstTerm.setChildAt(idx,trm);
        }
        childIndex++;
      }
    }
    %match(inAstTerm) {
      LabTerm(l,t) -> {
        Term tm = (Term) trm;
        if (childIndex == 0 ) {
          inAstTerm = `LabTerm(tm.getname(),t);
        } else {
          inAstTerm = `LabTerm(l,tm);
        }
        childIndex++;
      }
      RefTerm(l) -> {
        Term tm = (Term) trm;
        inAstTerm = `RefTerm(tm.getname());
      }
      Appl(s,TermList(args*)) -> {
        Term tm = (Term) trm;
        if (childIndex == 0 ) {
          /* This first child has to be an ID, which is the symbol name */
          inAstTerm = `Appl(tm.getname(),TermList());
        } else {
          inAstTerm = `Appl(s,TermList(args*,tm));
        }
        childIndex++;
      }
    }
  }
}
