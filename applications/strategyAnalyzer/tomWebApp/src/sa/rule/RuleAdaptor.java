
package sa.rule;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;


public class RuleAdaptor {
  public static shared.SharedObject getTerm(Tree tree) {
    shared.SharedObject res = null;
    if (tree.isNil()) {
      throw new RuntimeException("nil term");
    }
    if (tree.getType()==Token.INVALID_TOKEN_TYPE) {
      throw new RuntimeException("bad type");
    }

    switch (tree.getType()) {
      case 25:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = sa.rule.types.condition.CondTrue.make();
          break;
        }
      case 43:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          res = sa.rule.types.param.Param.make(field0);
          break;
        }
      case 40:
        {
          res = sa.rule.types.rulelist.EmptyConcRule.make();
          for(int i = 0; i < tree.getChildCount(); i++) {
            sa.rule.types.Rule elem = (sa.rule.types.Rule)RuleAdaptor.getTerm(tree.getChild(i));
            sa.rule.types.rulelist.ConcRule list = (sa.rule.types.rulelist.ConcRule) res;
            res = list.append(elem);
          }
          break;
        }
      case 14:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          int field1 = Integer.parseInt(tree.getChild(1).getText());
          res = sa.rule.types.symbol.Symbol.make(field0, field1);
          break;
        }
      case 42:
        {
          res = sa.rule.types.addlist.EmptyConcAdd.make();
          for(int i = 0; i < tree.getChildCount(); i++) {
            sa.rule.types.Term elem = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(i));
            sa.rule.types.addlist.ConcAdd list = (sa.rule.types.addlist.ConcAdd) res;
            res = list.append(elem);
          }
          break;
        }
      case 27:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Condition field0 = (sa.rule.types.Condition)RuleAdaptor.getTerm(tree.getChild(0));
          sa.rule.types.Condition field1 = (sa.rule.types.Condition)RuleAdaptor.getTerm(tree.getChild(1));
          res = sa.rule.types.condition.CondAnd.make(field0, field1);
          break;
        }
      case 13:
        {

          if (tree.getChildCount()!=3) {
            throw new RuntimeException("Node " + tree + ": 3 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          sa.rule.types.FieldList field1 = (sa.rule.types.FieldList)RuleAdaptor.getTerm(tree.getChild(1));
          sa.rule.types.GomType field2 = (sa.rule.types.GomType)RuleAdaptor.getTerm(tree.getChild(2));
          res = sa.rule.types.alternative.Alternative.make(field0, field1, field2);
          break;
        }
      case 35:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Term field0 = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(0));
          sa.rule.types.Term field1 = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(1));
          res = sa.rule.types.term.At.make(field0, field1);
          break;
        }
      case 60:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.GomType field0 = (sa.rule.types.GomType)RuleAdaptor.getTerm(tree.getChild(0));
          res = sa.rule.types.field.UnamedField.make(field0);
          break;
        }
      case 46:
        {
          res = sa.rule.types.stratdecllist.EmptyConcStratDecl.make();
          for(int i = 0; i < tree.getChildCount(); i++) {
            sa.rule.types.StratDecl elem = (sa.rule.types.StratDecl)RuleAdaptor.getTerm(tree.getChild(i));
            sa.rule.types.stratdecllist.ConcStratDecl list = (sa.rule.types.stratdecllist.ConcStratDecl) res;
            res = list.append(elem);
          }
          break;
        }
      case 51:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Strat field0 = (sa.rule.types.Strat)RuleAdaptor.getTerm(tree.getChild(0));
          res = sa.rule.types.strat.StratAll.make(field0);
          break;
        }
      case 61:
        {

          if (tree.getChildCount()!=3) {
            throw new RuntimeException("Node " + tree + ": 3 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          sa.rule.types.ParamList field1 = (sa.rule.types.ParamList)RuleAdaptor.getTerm(tree.getChild(1));
          sa.rule.types.Strat field2 = (sa.rule.types.Strat)RuleAdaptor.getTerm(tree.getChild(2));
          res = sa.rule.types.stratdecl.StratDecl.make(field0, field1, field2);
          break;
        }
      case 28:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Term field0 = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(0));
          sa.rule.types.Term field1 = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(1));
          res = sa.rule.types.condition.CondEquals.make(field0, field1);
          break;
        }
      case 38:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          res = sa.rule.types.term.Var.make(field0);
          break;
        }
      case 34:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.AddList field0 = (sa.rule.types.AddList)RuleAdaptor.getTerm(tree.getChild(0));
          res = sa.rule.types.term.Add.make(field0);
          break;
        }
      case 26:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Condition field0 = (sa.rule.types.Condition)RuleAdaptor.getTerm(tree.getChild(0));
          res = sa.rule.types.condition.CondNot.make(field0);
          break;
        }
      case 47:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          sa.rule.types.StratList field1 = (sa.rule.types.StratList)RuleAdaptor.getTerm(tree.getChild(1));
          res = sa.rule.types.strat.StratAppl.make(field0, field1);
          break;
        }
      case 55:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Strat field0 = (sa.rule.types.Strat)RuleAdaptor.getTerm(tree.getChild(0));
          sa.rule.types.Strat field1 = (sa.rule.types.Strat)RuleAdaptor.getTerm(tree.getChild(1));
          res = sa.rule.types.strat.StratSequence.make(field0, field1);
          break;
        }
      case 30:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = sa.rule.types.term.TrueMatch.make();
          break;
        }
      case 44:
        {

          if (tree.getChildCount()!=3) {
            throw new RuntimeException("Node " + tree + ": 3 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          sa.rule.types.GomType field1 = (sa.rule.types.GomType)RuleAdaptor.getTerm(tree.getChild(1));
          sa.rule.types.TypeEnvironment field2 = (sa.rule.types.TypeEnvironment)RuleAdaptor.getTerm(tree.getChild(2));
          res = sa.rule.types.typeenvironment.PushEnvironment.make(field0, field1, field2);
          break;
        }
      case 54:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Strat field0 = (sa.rule.types.Strat)RuleAdaptor.getTerm(tree.getChild(0));
          sa.rule.types.Strat field1 = (sa.rule.types.Strat)RuleAdaptor.getTerm(tree.getChild(1));
          res = sa.rule.types.strat.StratChoice.make(field0, field1);
          break;
        }
      case 59:
        {
          res = sa.rule.types.paramlist.EmptyConcParam.make();
          for(int i = 0; i < tree.getChildCount(); i++) {
            sa.rule.types.Param elem = (sa.rule.types.Param)RuleAdaptor.getTerm(tree.getChild(i));
            sa.rule.types.paramlist.ConcParam list = (sa.rule.types.paramlist.ConcParam) res;
            res = list.append(elem);
          }
          break;
        }
      case 21:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.RuleList field0 = (sa.rule.types.RuleList)RuleAdaptor.getTerm(tree.getChild(0));
          res = sa.rule.types.trs.Otrs.make(field0);
          break;
        }
      case 48:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          sa.rule.types.Strat field1 = (sa.rule.types.Strat)RuleAdaptor.getTerm(tree.getChild(1));
          res = sa.rule.types.strat.StratMu.make(field0, field1);
          break;
        }
      case 15:
        {
          res = sa.rule.types.alternativelist.EmptyConcAlternative.make();
          for(int i = 0; i < tree.getChildCount(); i++) {
            sa.rule.types.Alternative elem = (sa.rule.types.Alternative)RuleAdaptor.getTerm(tree.getChild(i));
            sa.rule.types.alternativelist.ConcAlternative list = (sa.rule.types.alternativelist.ConcAlternative) res;
            res = list.append(elem);
          }
          break;
        }
      case 20:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.RuleList field0 = (sa.rule.types.RuleList)RuleAdaptor.getTerm(tree.getChild(0));
          res = sa.rule.types.trs.Trs.make(field0);
          break;
        }
      case 24:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = sa.rule.types.condition.CondFalse.make();
          break;
        }
      case 16:
        {
          res = sa.rule.types.fieldlist.EmptyConcField.make();
          for(int i = 0; i < tree.getChildCount(); i++) {
            sa.rule.types.Field elem = (sa.rule.types.Field)RuleAdaptor.getTerm(tree.getChild(i));
            sa.rule.types.fieldlist.ConcField list = (sa.rule.types.fieldlist.ConcField) res;
            res = list.append(elem);
          }
          break;
        }
      case 23:
        {
          res = sa.rule.types.termlist.EmptyTermList.make();
          for(int i = 0; i < tree.getChildCount(); i++) {
            sa.rule.types.Term elem = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(i));
            sa.rule.types.termlist.TermList list = (sa.rule.types.termlist.TermList) res;
            res = list.append(elem);
          }
          break;
        }
      case 56:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Rule field0 = (sa.rule.types.Rule)RuleAdaptor.getTerm(tree.getChild(0));
          res = sa.rule.types.strat.StratRule.make(field0);
          break;
        }
      case 11:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.GomType field0 = (sa.rule.types.GomType)RuleAdaptor.getTerm(tree.getChild(0));
          sa.rule.types.AlternativeList field1 = (sa.rule.types.AlternativeList)RuleAdaptor.getTerm(tree.getChild(1));
          res = sa.rule.types.production.SortType.make(field0, field1);
          break;
        }
      case 50:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Strat field0 = (sa.rule.types.Strat)RuleAdaptor.getTerm(tree.getChild(0));
          res = sa.rule.types.strat.StratOne.make(field0);
          break;
        }
      case 19:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = sa.rule.types.trs.EmptyTrs.make();
          break;
        }
      case 49:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Trs field0 = (sa.rule.types.Trs)RuleAdaptor.getTerm(tree.getChild(0));
          res = sa.rule.types.strat.StratTrs.make(field0);
          break;
        }
      case 12:
        {
          res = sa.rule.types.productionlist.EmptyConcProduction.make();
          for(int i = 0; i < tree.getChildCount(); i++) {
            sa.rule.types.Production elem = (sa.rule.types.Production)RuleAdaptor.getTerm(tree.getChild(i));
            sa.rule.types.productionlist.ConcProduction list = (sa.rule.types.productionlist.ConcProduction) res;
            res = list.append(elem);
          }
          break;
        }
      case 29:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = sa.rule.types.term.Empty.make();
          break;
        }
      case 37:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          int field0 = Integer.parseInt(tree.getChild(0).getText());
          res = sa.rule.types.term.BuiltinInt.make(field0);
          break;
        }
      case 52:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = sa.rule.types.strat.StratFail.make();
          break;
        }
      case 33:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Term field0 = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(0));
          sa.rule.types.Term field1 = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(1));
          res = sa.rule.types.term.Sub.make(field0, field1);
          break;
        }
      case 39:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          sa.rule.types.TermList field1 = (sa.rule.types.TermList)RuleAdaptor.getTerm(tree.getChild(1));
          res = sa.rule.types.term.Appl.make(field0, field1);
          break;
        }
      case 57:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          res = sa.rule.types.strat.StratName.make(field0);
          break;
        }
      case 10:
        {

          if (tree.getChildCount()!=4) {
            throw new RuntimeException("Node " + tree + ": 4 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.ProductionList field0 = (sa.rule.types.ProductionList)RuleAdaptor.getTerm(tree.getChild(0));
          sa.rule.types.ProductionList field1 = (sa.rule.types.ProductionList)RuleAdaptor.getTerm(tree.getChild(1));
          sa.rule.types.StratDeclList field2 = (sa.rule.types.StratDeclList)RuleAdaptor.getTerm(tree.getChild(2));
          sa.rule.types.Trs field3 = (sa.rule.types.Trs)RuleAdaptor.getTerm(tree.getChild(3));
          res = sa.rule.types.program.Program.make(field0, field1, field2, field3);
          break;
        }
      case 22:
        {
          res = sa.rule.types.stratlist.EmptyConcStrat.make();
          for(int i = 0; i < tree.getChildCount(); i++) {
            sa.rule.types.Strat elem = (sa.rule.types.Strat)RuleAdaptor.getTerm(tree.getChild(i));
            sa.rule.types.stratlist.ConcStrat list = (sa.rule.types.stratlist.ConcStrat) res;
            res = list.append(elem);
          }
          break;
        }
      case 17:
        {

          if (tree.getChildCount()!=3) {
            throw new RuntimeException("Node " + tree + ": 3 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Term field0 = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(0));
          sa.rule.types.Term field1 = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(1));
          sa.rule.types.Condition field2 = (sa.rule.types.Condition)RuleAdaptor.getTerm(tree.getChild(2));
          res = sa.rule.types.rule.ConditionalRule.make(field0, field1, field2);
          break;
        }
      case 41:
        {
          res = sa.rule.types.gomtypelist.EmptyConcGomType.make();
          for(int i = 0; i < tree.getChildCount(); i++) {
            sa.rule.types.GomType elem = (sa.rule.types.GomType)RuleAdaptor.getTerm(tree.getChild(i));
            sa.rule.types.gomtypelist.ConcGomType list = (sa.rule.types.gomtypelist.ConcGomType) res;
            res = list.append(elem);
          }
          break;
        }
      case 58:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          res = sa.rule.types.gomtype.GomType.make(field0);
          break;
        }
      case 36:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Term field0 = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(0));
          res = sa.rule.types.term.Anti.make(field0);
          break;
        }
      case 31:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Term field0 = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(0));
          sa.rule.types.Term field1 = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(1));
          res = sa.rule.types.term.Match.make(field0, field1);
          break;
        }
      case 18:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Term field0 = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(0));
          sa.rule.types.Term field1 = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(1));
          res = sa.rule.types.rule.Rule.make(field0, field1);
          break;
        }
      case 45:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = sa.rule.types.typeenvironment.EmptyEnvironment.make();
          break;
        }
      case 53:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = sa.rule.types.strat.StratIdentity.make();
          break;
        }
      case 32:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          sa.rule.types.Term field0 = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(0));
          sa.rule.types.Term field1 = (sa.rule.types.Term)RuleAdaptor.getTerm(tree.getChild(1));
          res = sa.rule.types.term.Inter.make(field0, field1);
          break;
        }

    }
    return res;
  }
}
