import jtom.runtime.xml.*;
import jtom.runtime.xml.adt.*;
import aterm.*;
import jtom.runtime.*;

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

    t = ``xml(<IntegerList/>);
    for(int i =1 ; i<5 ; i++) {
      t = ``addInteger(t,i);
    }

    System.out.println("t = " + t);
    System.out.println("checkSorted = " + checkSortedInteger(t));
    System.out.println("swapElements = " + swapElements(t));
    
  }

  TNode addInteger(TNode list,int n) {
    %match(TNode list) {
      <IntegerList _*>(integers*)</IntegerList> -> {
        String s = ""+n;
        return ``xml(<IntegerList> integers*
                                   <Integer>#TEXT(s)</Integer>
                     </IntegerList>);
      }
    }
    return null;    
  }

  boolean checkSortedInteger(TNode list) {
    %match(TNode list) {
      <IntegerList _*>[<Integer []>#TEXT(s1)</Integer>,
                       <Integer []>#TEXT(s2)</Integer>]</IntegerList> -> {
        if(s1.compareTo(s2) > 0) { return false; }
      }
    }
    return true;    
  }

  TNode swapElements(TNode list) {
    %match(TNode list) {
      <IntegerList attr@_*>X1*
                         n1@<Integer []>#TEXT(s1)</Integer>
                         n2@<Integer []>#TEXT(s2)</Integer>
                         X2*</IntegerList> -> {
        if(s1.compareTo(s2) < 0) {
          return ``swapElements(<IntegerList attr*>X1* n2 n1 X2*</IntegerList>);
        }
      }
    }
    return list;    
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
