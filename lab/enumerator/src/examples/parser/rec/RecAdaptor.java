
package examples.parser.rec;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;


public class RecAdaptor {
  public static shared.SharedObject getTerm(Tree tree) {
    shared.SharedObject res = null;
    if (tree.isNil()) {
      throw new RuntimeException("nil term");
    }
    if (tree.getType()==Token.INVALID_TOKEN_TYPE) {
      throw new RuntimeException("bad type");
    }

    switch (tree.getType()) {
      case 10:
        {
          res = examples.parser.rec.types.explist.EmptyExpList.make();
          for(int i = 0; i < tree.getChildCount(); i++) {
            examples.parser.rec.types.Exp elem = (examples.parser.rec.types.Exp)RecAdaptor.getTerm(tree.getChild(i));
            examples.parser.rec.types.explist.ExpList list = (examples.parser.rec.types.explist.ExpList) res;
            res = list.append(elem);
          }
          break;
        }
      case 13:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = examples.parser.rec.types.exp.True.make();
          break;
        }
      case 14:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          examples.parser.rec.types.Stm field0 = (examples.parser.rec.types.Stm)RecAdaptor.getTerm(tree.getChild(0));
          examples.parser.rec.types.Exp field1 = (examples.parser.rec.types.Exp)RecAdaptor.getTerm(tree.getChild(1));
          res = examples.parser.rec.types.exp.SeqExp.make(field0, field1);
          break;
        }
      case 23:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = examples.parser.rec.types.table.EmptyTable.make();
          break;
        }
      case 15:
        {

          if (tree.getChildCount()!=3) {
            throw new RuntimeException("Node " + tree + ": 3 child(s) expected, but " + tree.getChildCount() + " found");
          }
          examples.parser.rec.types.Exp field0 = (examples.parser.rec.types.Exp)RecAdaptor.getTerm(tree.getChild(0));
          examples.parser.rec.types.Op field1 = (examples.parser.rec.types.Op)RecAdaptor.getTerm(tree.getChild(1));
          examples.parser.rec.types.Exp field2 = (examples.parser.rec.types.Exp)RecAdaptor.getTerm(tree.getChild(2));
          res = examples.parser.rec.types.exp.OpExp.make(field0, field1, field2);
          break;
        }
      case 29:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = examples.parser.rec.types.op.Div.make();
          break;
        }
      case 28:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = examples.parser.rec.types.op.And.make();
          break;
        }
      case 22:
        {
          res = examples.parser.rec.types.stm.EmptySeq.make();
          for(int i = 0; i < tree.getChildCount(); i++) {
            examples.parser.rec.types.Stm elem = (examples.parser.rec.types.Stm)RecAdaptor.getTerm(tree.getChild(i));
            examples.parser.rec.types.stm.Seq list = (examples.parser.rec.types.stm.Seq) res;
            res = list.append(elem);
          }
          break;
        }
      case 25:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          int field0 = Integer.parseInt(tree.getChild(0).getText());
          examples.parser.rec.types.Table field1 = (examples.parser.rec.types.Table)RecAdaptor.getTerm(tree.getChild(1));
          res = examples.parser.rec.types.pair.Pair.make(field0, field1);
          break;
        }
      case 24:
        {

          if (tree.getChildCount()!=3) {
            throw new RuntimeException("Node " + tree + ": 3 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          int field1 = Integer.parseInt(tree.getChild(1).getText());
          examples.parser.rec.types.Table field2 = (examples.parser.rec.types.Table)RecAdaptor.getTerm(tree.getChild(2));
          res = examples.parser.rec.types.table.Table.make(field0, field1, field2);
          break;
        }
      case 12:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = examples.parser.rec.types.exp.False.make();
          break;
        }
      case 11:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          examples.parser.rec.types.Exp field0 = (examples.parser.rec.types.Exp)RecAdaptor.getTerm(tree.getChild(0));
          res = examples.parser.rec.types.exp.NotExp.make(field0);
          break;
        }
      case 21:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          examples.parser.rec.types.Exp field1 = (examples.parser.rec.types.Exp)RecAdaptor.getTerm(tree.getChild(1));
          res = examples.parser.rec.types.stm.Assign.make(field0, field1);
          break;
        }
      case 32:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = examples.parser.rec.types.op.Plus.make();
          break;
        }
      case 31:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = examples.parser.rec.types.op.Minus.make();
          break;
        }
      case 17:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          res = examples.parser.rec.types.exp.Id.make(field0);
          break;
        }
      case 20:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          examples.parser.rec.types.ExpList field0 = (examples.parser.rec.types.ExpList)RecAdaptor.getTerm(tree.getChild(0));
          res = examples.parser.rec.types.stm.Print.make(field0);
          break;
        }
      case 30:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = examples.parser.rec.types.op.Times.make();
          break;
        }
      case 26:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = examples.parser.rec.types.op.Equal.make();
          break;
        }
      case 16:
        {

          if (tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          int field0 = Integer.parseInt(tree.getChild(0).getText());
          res = examples.parser.rec.types.exp.Num.make(field0);
          break;
        }
      case 19:
        {

          if (tree.getChildCount()!=3) {
            throw new RuntimeException("Node " + tree + ": 3 child(s) expected, but " + tree.getChildCount() + " found");
          }
          examples.parser.rec.types.Exp field0 = (examples.parser.rec.types.Exp)RecAdaptor.getTerm(tree.getChild(0));
          examples.parser.rec.types.Stm field1 = (examples.parser.rec.types.Stm)RecAdaptor.getTerm(tree.getChild(1));
          examples.parser.rec.types.Stm field2 = (examples.parser.rec.types.Stm)RecAdaptor.getTerm(tree.getChild(2));
          res = examples.parser.rec.types.stm.If.make(field0, field1, field2);
          break;
        }
      case 27:
        {

          if (tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = examples.parser.rec.types.op.Or.make();
          break;
        }
      case 18:
        {

          if (tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          examples.parser.rec.types.Exp field0 = (examples.parser.rec.types.Exp)RecAdaptor.getTerm(tree.getChild(0));
          examples.parser.rec.types.Stm field1 = (examples.parser.rec.types.Stm)RecAdaptor.getTerm(tree.getChild(1));
          res = examples.parser.rec.types.stm.While.make(field0, field1);
          break;
        }

    }
    return res;
  }
}
