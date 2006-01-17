import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import tom.library.traversal.*;
import aterm.*;
import java.util.*;
import java.io.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.Identity;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;

public class Tools{

  %include{adt/tnode/TNode.tom}
  %include{mutraveler.tom}

  private String dirC = "content/";
  private String dirWww = "www/";
  private String dirTmp = "tmp/";
  private XmlTools xtools;
  private TNodeFactory getTNodeFactory() {
    xtools = new XmlTools();
    return xtools.getTNodeFactory();
  }

  /**
   * Remove CommentNode from the term
   */
  public TNode removeComments(TNode input) {
    try {
      VisitableVisitor ruleId = new RmComments();
      return (TNode)MuTraveler.init(`InnermostId(ruleId)).visit(input);
    } catch (VisitFailure e) {
      System.out.println("reduction failed on: " + input);
    }
    return input;
  }

  class RmComments extends TNodeVisitableFwd {
    public RmComments() {
      super(`Identity());
    }
    public TNodeList visit_TNodeList(TNodeList arg) throws VisitFailure {
      %match(TNodeList arg) {
        concTNode(before*,CommentNode(_),after*) -> {
          return arg = `concTNode(before*, after*);
        }
      }
      return arg;
    }
  }

  /**
   * Convert a Collection of TNodeList in a unique TNodeList
   * @param list a collection of TNodeList
   */
  public TNodeList mergeTNodeLists(Collection list) {
    TNodeList result = `concTNode();
    Iterator i = list.iterator();
    while(i.hasNext()) {
      TNodeList tmp = (TNodeList)i.next();
      result = `concTNode(result*,tmp*);
    }
    return result;
  }
  public String removeAccents(String s){
    ToolsEncode t = new ToolsEncode();
    return t.removeAccents(s);
  }
}
