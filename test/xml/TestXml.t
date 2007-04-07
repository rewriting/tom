package xml;

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import java.util.*;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestXml extends TestCase {
  
  %include{ adt/tnode/TNode.tom }
    
  private XmlTools xtools;
	private LinkedList elements;
	private LinkedList reverseElements;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestXml.class));
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

	public void testExtractElements() {
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

  public void testInOut() {
    TNode node = `xml(
				<Configuration>
					<Cellule>
						<Defaut R1="23" V1="34" B1="45"/>
						<Selection R="0" V="0" B="255"/>
						<VolumeSensible R="255" V="0" B="0"/>
					</Cellule>
				</Configuration>
				);
    StringWriter sw = new StringWriter();
    xtools.writeXMLFileFromTNode(sw,node);
    TNode renode = xtools.nodeToTNode(
        xtools.convertToNode(
          new ByteArrayInputStream(sw.toString().getBytes()))
        );
    assertEquals(node,renode.getDocElem());
  }

	public void testAttributeMatch(){
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
		%match(TNode node) {
			<Configuration>
				<Cellule>
					a @ <Defaut R1=iR />
				</Cellule>
			</Configuration> -> {
				//System.out.println("Match R"+a);                  
				res++;
			}
			<Configuration>
				<Cellule>
					a @ <Defaut V1=iV />
				</Cellule>
			</Configuration> -> {
				//System.out.println("Match V"+a);                  
				res++;
			}
			<Configuration>
				<Cellule>
					a @ <Defaut B1=iB />
				</Cellule>
			</Configuration> -> {
				//System.out.println("Match B"+a);                  
				res++;
			} 
			<Configuration>
				<Cellule>
					a @ <Defaut B1=iB R1=iR></Defaut>
				</Cellule>
			</Configuration> -> {
				//System.out.println("Match BR"+a);                  
				res++;
			}
			<Configuration>
				<Cellule>
					a @ <Defaut R1=iR B1=iB></Defaut>
				</Cellule>
			</Configuration> -> {
				//System.out.println("Match RB"+a);                  
				res++;
			}
			<Configuration>
				<Cellule>
					a @ <Defaut R1=iR V1=iV></Defaut>
				</Cellule>
			</Configuration> -> {
				//System.out.println("Match RV"+a);                  
				res++;
			}
			<Configuration>
				<Cellule>
					a @ <Defaut V1=iV R1=iR></Defaut>
				</Cellule>
			</Configuration> -> {
				//System.out.println("Match VR"+a);                  
				res++;
			}
			<Configuration>
				<Cellule>
					a @ <Defaut B1=iR V1=iV></Defaut>
				</Cellule>
			</Configuration> -> {
				//System.out.println("Match BV"+a);                  
				res++;
			}
			<Configuration>
				<Cellule>
					a @ <Defaut V1=iV B1=iR></Defaut>
				</Cellule>
			</Configuration> -> {
				//System.out.println("Match VB"+a);                  
				res++;
			}
			<Configuration>
				<Cellule>
					a @ <Defaut B1=iB R1=iR V1=iV></Defaut>
				</Cellule>
			</Configuration> -> {
				//System.out.println("Match BRV"+a);                  
				res++;
			}
			<Configuration>
				<Cellule>
					a @ <Defaut B1=iB V1=iV R1=iR></Defaut>
				</Cellule>
			</Configuration> -> {
				//System.out.println("Match BVR"+a);                  
				res++;
			}
			<Configuration>
				<Cellule>
					a @ <Defaut V1=iV R1=iR B1=iB></Defaut>
				</Cellule>
			</Configuration> -> {
				//System.out.println("Match VRB"+a);                  
				res++;
			}
			<Configuration>
				<Cellule>
					a @ <Defaut V1=iV B1=iB R1=iR></Defaut>
				</Cellule>
			</Configuration> -> {
				//System.out.println("Match VBR"+a);                  
				res++;
			}
			<Configuration>
				<Cellule>
					a @ <Defaut R1=iR B1=iB V1=iV></Defaut>
				</Cellule>
			</Configuration> -> {
				//System.out.println("Match RBV"+a);                  
				res++;
			}
			<Configuration>
				<Cellule>
						a @ <Defaut R1=iR V1=iV B1=iB></Defaut>
				</Cellule>
			</Configuration> -> {
				//System.out.println("Match RVB"+a);                  
				res++;
			}
		}
		assertEquals(
			"XML attibute matching should not depend on the order of the attibutes", 
			res, 15);
	}

}
