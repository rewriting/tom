import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;
import jtom.runtime.*;
import java.util.*;
import junit.framework.TestCase;

public class TestXml extends TestCase {
  
  %include{ TNode.tom }
    
  private XmlTools xtools;
  private GenericTraversal traversal = new GenericTraversal();
	private LinkedList elements;
	private LinkedList reverseElements;

	private Factory getTNodeFactory() {
    return xtools.getTNodeFactory();
  }

  private TNode xml(TNode t) {
    return t;
  }

	protected void setUp() {
    xtools = new XmlTools();
	}

	private TNode getXmldoc() {
    TNode t;
    
      //t = `XML(<A> <B/> </A>);
      //t = `XML(<A at1="foo" at2=x at3=dd("text")/>);

    elements = new LinkedList();
    reverseElements = new LinkedList();
    t = `xml(<IntegerList/>);
    for(int i =1 ; i<5 ; i++) {
      t = `addInteger(t,i);
      elements.addLast("" + i);
      reverseElements.addFirst("" + i);
    }
		return t;
	}
  
  TNode addInteger(TNode list,int n) {
    %match(TNode list) {
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

  public void testSortedInteger() {
		TNode list = getXmldoc();
    %match(TNode list) {
      <IntegerList>[<(Int|Integer)>(#TEXT(s1))</(Int|Integer)>,
                    <(Integer|Int)>(#TEXT(s2))</(Integer|Int)>]</IntegerList> -> {
				 //if(`s1.compareTo(`s2) > 0) { return false; }
				 assertFalse("Expects the matched integers to be ordered",
										 `s1.compareTo(`s2) > 0);
			 }
		}
	}

  TNode swapElements(TNode list) {
    %match(TNode list) {
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

	public void testSwapElements() {
		TNode list = getXmldoc();
    LinkedList res = extractElements(swapElements(list));
    assertEquals("ExtractElement extract elements in order",
								 reverseElements, res);
  }

	public void textExtractElements() {
		TNode list = getXmldoc();
		LinkedList res = extractElements(list);
		assertEquals("ExtractElement extract elements in order",
								 elements, res);
	}

  public LinkedList extractElements(TNode list) {
    LinkedList res = new LinkedList();
    %match(TNode list) {
      <IntegerList>
         <(Integer|Int)>(#TEXT(s1))</(Integer|Int)>
      </IntegerList> -> { res.add(`s1); }
    }
		return res;
  }
  
  public String dd(String x) {
      return x+x;
    }
}
