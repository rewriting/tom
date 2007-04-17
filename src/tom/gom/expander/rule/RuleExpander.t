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

import tom.gom.adt.gom.types.*;

public class RuleExpander {

  %include { ../../adt/gom/Gom.tom}

  private ModuleList moduleList;
  public RuleExpander(ModuleList data) {
    this.moduleList = data;
  }

  public HookDeclList expandRules(String ruleCode) {
    System.out.println("Rules to parse : " + ruleCode);
    RuleLexer lexer = new RuleLexer(new ANTLRStringStream(ruleCode));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    RuleParser parser = new RuleParser(tokens);
    try {
      Tree ast = (Tree)parser.ruleset().getTree();
      System.out.println("Rules : " + ast.toStringTree());
    } catch (org.antlr.runtime.RecognitionException e) {
      System.out.println("Got RecognitionException while parsing ruleset");
    }
    return `concHookDecl();
  }
}
