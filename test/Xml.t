import jtom.runtime.xml.*;
import jtom.runtime.xml.adt.*;
import aterm.*;
import jtom.runtime.*;
import java.util.*;

public class Xml {
  
  %include{ TNode.tom }
    
  private XmlTools xtools;
  private GenericTraversal traversal = new GenericTraversal();
  private TNodeFactory getTNodeFactory() {
    return xtools.getTNodeFactory();
  }

  public static void main (String args[]) {
    Xml test = new Xml();
    test.run();
  }

  private TNode xml(TNode t) {
    return t;
  }
  
  private void run() {
    xtools = new XmlTools();
    TNode t;
    String x="goo";
    
      //t = ``xml(<A> <B/> </A>);
      //t = ``xml(<A at1="foo" at2=x at3=dd("text")/>);

    LinkedList elements = new LinkedList();
    LinkedList reverseElements = new LinkedList();
    t = ``xml(<IntegerList/>);
    for(int i =1 ; i<5 ; i++) {
      t = ``addInteger(t,i);
      elements.addLast("" + i);
      reverseElements.addFirst("" + i);
    }
    System.out.println("t = " + t);
    System.out.println("checkSorted = " + checkSortedInteger(t));
    System.out.println("swapElements = " + swapElements(t));
    System.out.println("extractElements = " + extractElements(t));

    assertTrue(checkSortedInteger(t));
    assertTrue(extractElements(t).equals(elements));
    assertTrue(extractElements(swapElements(t)).equals(reverseElements));
    
  }

  TNode addInteger(TNode list,int n) {
    %match(TNode list) {
      <IntegerList _*>(integers*)</IntegerList> -> {
        String s = ""+n;
        return ``xml(<IntegerList> integers* <Integer>#TEXT(s)</Integer>
                     </IntegerList>);
      }
    }
    return null;    
  }

  boolean checkSortedInteger(TNode list) {
    %match(TNode list) {
      <IntegerList>[<Integer>(#TEXT(s1))</Integer>,
                    <Integer>(#TEXT(s2))</Integer>]</IntegerList> -> {
        if(s1.compareTo(s2) > 0) { return false; }
      }
    }
    return true;    
  }

  TNode swapElements(TNode list) {
    %match(TNode list) {
      <IntegerList (attr*)>(X1*,
                            n1@<Integer []>#TEXT(s1)</Integer>,
                            n2@<Integer []>#TEXT(s2)</Integer>,
                            X2*)</IntegerList> -> {
        if(s1.compareTo(s2) < 0) {
          return ``swapElements(<IntegerList attr*>X1* n2 n1 X2*</IntegerList>);
        }
      }
    }
    return list;    
  }

  LinkedList extractElements(TNode list) {
    LinkedList res = new LinkedList();
    %match(TNode list) {
      <IntegerList> <Integer>(#TEXT(s1))</Integer> </IntegerList> -> { res.add(s1); }
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
