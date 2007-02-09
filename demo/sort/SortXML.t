import tom.library.xml.*;
import tom.library.adt.tnode.types.*;

public class SortXML {

  %include{ adt/tnode/TNode.tom }

  public static void main (String args[]) {
      XmlTools xtools = new XmlTools();
      TNode term = (TNode)xtools.convertXMLToTNode("person.xml");
      TNode result = sort(term.getDocElem());
      xtools.printXMLFromTNode(result);
  }

  private static TNode sort(TNode subject) {
    %match(TNode subject) {
     <Persons>(X1*,p1,X2*,p2,X3*)</Persons> -> {
        if(`compare(p1,p2) > 0) {
          return sort(`xml(<Persons>X1* p2 X2* p1 X3*</Persons>));
        }
      }
    }
    return subject;
  }

  private static int compare(TNode t1, TNode t2) {
    %match(TNode t1, TNode t2) {
      <Person Age=a1><FirstName>#TEXT(n1)</FirstName></Person>,
      <Person Age=a2><FirstName>#TEXT(n2)</FirstName></Person>
      -> {
          return `n1.compareTo(`n2);
      }
    }
    return 0;
  }

}
