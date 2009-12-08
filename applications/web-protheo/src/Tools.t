import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import java.util.*;

import tom.library.sl.*;

public class Tools{

  %include{adt/tnode/TNode.tom}
  %include{sl.tom}

  public Tools(){
}

/**
   * Remove CommentNode from the term
   */
  public TNode removeComments(TNode input) {
    try {
      Strategy ruleId = `RmComments();
      return (TNode)`InnermostId(ruleId).visit(input);
    } catch (VisitFailure e) {
      System.out.println("reduction failed on: " + input);
    }
    return input;
  }

  %strategy RmComments() extends `Identity() {

    visit TNodeList {
      concTNode(before*,CommentNode(_),after*) -> {
        return `concTNode(before*, after*);
      }
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
}
