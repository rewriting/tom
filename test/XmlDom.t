import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.parsers.*;
import java.util.*;

public class XmlDom {
  private Document dom;
  %include{ dom.tom }
    
  public static void main (String args[]) {
    XmlDom test = new XmlDom();
    test.run();
  }

  private void run() {
		try {
		dom = DocumentBuilderFactory
			.newInstance()
			.newDocumentBuilder()
			.newDocument();
		} catch (Exception e) {
				throw new RuntimeException("Dom parser problem.");
		}
    String x="goo";
    LinkedList elements = new LinkedList();
    LinkedList reverseElements = new LinkedList();
    Node t = `xml(dom,<IntegerList/>);
    for(int i =1 ; i<5 ; i++) {
      t = `addInteger(t,i);
      elements.addLast("" + i);
      reverseElements.addFirst("" + i);
    }
      //System.out.println("t = " + t);
      //System.out.println("checkSorted = " + checkSortedInteger(t));
      //System.out.println("swapElements = " + swapElements(t));
      //System.out.println("extractElements = " + extractElements(t));

    assertTrue(checkSortedInteger(t));
    assertTrue(extractElements(t).equals(elements));
    assertTrue(extractElements(swapElements(t)).equals(reverseElements));
    
  }

  Node addInteger(Node list,int n) {
    %match(TNode list) {
      <IntegerList _*>(integers*)</IntegerList> -> {
        String s = ""+n;
        if(n%2 == 0) {
          return `xml(dom,<IntegerList> integers* <Integer>#TEXT(s)</Integer>
                       </IntegerList>);
        } else {
          return `xml(dom,<IntegerList> integers* <Int>#TEXT(s)</Int>
                       </IntegerList>);
        }
      }
    }
    return null;    
  }

  boolean checkSortedInteger(Node list) {
    %match(TNode list) {
      <IntegerList>[<(Int|Integer)>(#TEXT(s1))</(Int|Integer)>,
                    <(Integer|Int)>(#TEXT(s2))</(Integer|Int)>]</IntegerList> -> {
        if(`s1.compareTo(`s2) > 0) { return false; }
      }
    }
    return true;    
  }

  Node swapElements(Node list) {
    %match(TNode list) {
      <IntegerList (attr*)>(X1*,
                            n1@<_ []>#TEXT(s1)</_>,
                            n2@<(Integer|Int) []>#TEXT(s2)</(Integer|Int)>,
                            X2*)</IntegerList> -> {
        if(`s1.compareTo(`s2) < 0) {
          return `xml(dom,swapElements(<IntegerList attr*>X1* n2 n1 X2*</IntegerList>));
        }
      }
    }
    return list;    
  }

  LinkedList extractElements(Node list) {
    LinkedList res = new LinkedList();
    %match(TNode list) {
      <IntegerList>
         <(Integer|Int)>(#TEXT(s1))</(Integer|Int)>
      </IntegerList> -> { res.add(`s1); }
    }
    return res;
  }
  
  static void  assertTrue(boolean condition) {
    if(!condition) {
      throw new RuntimeException("assertion failed.");
    }
  }

  public String dd(String x) {
      return x+x;
    }
}
