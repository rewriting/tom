package xml;

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import aterm.*;
import java.util.*;

public class Xml {
  
  %include{ adt/tnode/TNode.tom }
    
  private XmlTools xtools;

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
    
      //t = `XML(<A> <B/> </A>);
      //t = `XML(<A at1="foo" at2=x at3=dd("text")/>);

    LinkedList elements = new LinkedList();
    LinkedList reverseElements = new LinkedList();
    t = `xml(<IntegerList/>);
    for(int i =1 ; i<5 ; i++) {
      t = `addInteger(t,i);
      elements.addLast("" + i);
      reverseElements.addFirst("" + i);
    }
    //     System.out.println("t = " + t);
    //       System.out.println("checkSorted = " + checkSortedInteger(t));
    //       System.out.println("swapElements = " + swapElements(t));
    //       System.out.println("extractElements = " + extractElements(t));

    assertTrue(checkSortedInteger(t));
    assertTrue(extractElements(t).equals(elements));
    assertTrue(extractElements(swapElements(t)).equals(reverseElements));

    testAttributeMatch();  
  }

  TNode addInteger(TNode list,int n) {
    %match(list) {
      <IntegerList _*>(integers*)</IntegerList> -> {
        String s = ""+n;
        if(n%2 == 0) {
          return `xml(<IntegerList> integers* <Integer>#TEXT(s)</Integer>
                       </IntegerList>);
        } else {
          return `xml(<IntegerList> integers* <Int>#TEXT(s)</Int>
                       </IntegerList>);
        }
      }
    }
    return null;    
  }

  boolean checkSortedInteger(TNode list) {
    %match(list) {
      <IntegerList>[<(Int|Integer)>(#TEXT(s1))</(Int|Integer)>,
                    <(Integer|Int)>(#TEXT(s2))</(Integer|Int)>]</IntegerList> -> {
        if(`s1.compareTo(`s2) > 0) { return false; }
      }
    }
    return true;    
  }

  TNode swapElements(TNode list) {
    %match(list) {
      <IntegerList (attr*)>(X1*,
                            n1@<_ []>#TEXT(s1)</_>,
                            n2@<(Integer|Int) []>#TEXT(s2)</(Integer|Int)>,
                            X2*)</IntegerList> -> {
        if(`s1.compareTo(`s2) < 0) {
          return `xml(swapElements(<IntegerList attr*>X1* n2 n1 X2*</IntegerList>));
        }
      }
    }
    return list;    
  }

  LinkedList extractElements(TNode list) {
    LinkedList res = new LinkedList();
    %match(list) {
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

  public void testAttributeMatch() {
    TNode node = `xml(
        <Configuration>
          <Cellule>
            <Defaut R1="23" V1="34" B1="45"/>
            <Selection R="0" V="0" B="255"/>
            <VolumeSensible R="255" V="0" B="0"/>
          </Cellule>
        </Configuration>
        );
    int res = 0;
    %match(node) {
      <Configuration>
        <Cellule>
          a @ <Defaut R1=iR />
        </Cellule>
      </Configuration> -> {
         System.out.println("Match R "+`a);
        res++;
      }
      <Configuration>
        <Cellule>
          a @ <Defaut V1=iV />
        </Cellule>
      </Configuration> -> {
        System.out.println("Match V "+`a);
        res++;
      }
      <Configuration>
        <Cellule>
          a @ <Defaut B1=iB />
        </Cellule>
      </Configuration> -> {
        System.out.println("Match B "+`a);
        res++;
      } 
      <Configuration>
        <Cellule>
          a @ <Defaut B1=iB R1=iR></Defaut>
        </Cellule>
      </Configuration> -> {
        System.out.println("Match BR "+`a);
        res++;
      }
      <Configuration>
        <Cellule>
          a @ <Defaut R1=iR B1=iB></Defaut>
        </Cellule>
      </Configuration> -> {
        System.out.println("Match RB "+`a);
        res++;
      }
      <Configuration>
        <Cellule>
          a @ <Defaut R1=iR V1=iV></Defaut>
        </Cellule>
      </Configuration> -> {
        System.out.println("Match RV "+`a);
        res++;
      }
      <Configuration>
        <Cellule>
          a @ <Defaut V1=iV R1=iR></Defaut>
        </Cellule>
      </Configuration> -> {
        System.out.println("Match VR "+`a);
        res++;
      }
      <Configuration>
        <Cellule>
          a @ <Defaut B1=iR V1=iV></Defaut>
        </Cellule>
      </Configuration> -> {
        System.out.println("Match BV "+`a);
        res++;
      }
      <Configuration>
        <Cellule>
          a @ <Defaut V1=iV B1=iR></Defaut>
        </Cellule>
      </Configuration> -> {
        System.out.println("Match VB "+`a);
        res++;
      }
      <Configuration>
        <Cellule>
          a @ <Defaut B1=iB R1=iR V1=iV></Defaut>
        </Cellule>
      </Configuration> -> {
        System.out.println("Match BRV "+`a);
        res++;
      }
      <Configuration>
        <Cellule>
          a @ <Defaut B1=iB V1=iV R1=iR></Defaut>
        </Cellule>
      </Configuration> -> {
        System.out.println("Match BVR "+`a);
        res++;
      }
      <Configuration>
        <Cellule>
          a @ <Defaut V1=iV R1=iR B1=iB></Defaut>
        </Cellule>
      </Configuration> -> {
        System.out.println("Match VRB "+`a);
        res++;
      }
      <Configuration>
        <Cellule>
          a @ <Defaut V1=iV B1=iB R1=iR></Defaut>
        </Cellule>
      </Configuration> -> {
        System.out.println("Match VBR "+`a);
        res++;
      }
      <Configuration>
        <Cellule>
          a @ <Defaut R1=iR B1=iB V1=iV></Defaut>
        </Cellule>
      </Configuration> -> {
        System.out.println("Match RBV "+`a);
        res++;
      }
      <Configuration>
        <Cellule>
            a @ <Defaut R1=iR V1=iV B1=iB></Defaut>
        </Cellule>
      </Configuration> -> {
        System.out.println("Match RVB "+`a);
        res++;
      }
    }
    assertTrue(res == 15);
  }

}
