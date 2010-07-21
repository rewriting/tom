package xml.rss;

import tom.library.xml.*;
import tom.library.adt.tnode.types.*;
import tom.library.sl.*;

public class RssFeedTransformer {

  %include{ adt/tnode/TNode.tom }
  %include{ sl.tom }

  public static void main (String args[]) {
    RssFeedTransformer trans = new RssFeedTransformer();
    trans.run("rss.xml");
    //trans.run("/home/radu/workspace/jtomSVN/examples/xml/rss/rss2.xml");
  }

  private void run(String filename){
    XmlTools xtools = new XmlTools();
    TNode term = (TNode)xtools.convertXMLToTNode(filename);
    try {
      term = (TNode)`BottomUp(RemoveNonDico()).visit(term);
      // To print the result back
      xtools.printXMLFromTNode(term);
    } catch(VisitFailure e) {
      System.out.println("Failure");
    }
  }

  %strategy RemoveNonDico() extends `Identity(){
    visit TNode {
      <item><title>!#TEXT(concString(_*,'Legea',_*))</title></item> -> {    	  
        return `EmptyNode();
      }
    }
  }

}
