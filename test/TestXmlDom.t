import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.parsers.*;
import java.util.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestXmlDom extends TestCase {
  private Document dom;
	private LinkedList elements;
	private LinkedList reverseElements;

  %include{ dom.tom }

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestXmlDom.class));
	}

	public void setUp() {
		try {
		dom = DocumentBuilderFactory
			.newInstance()
			.newDocumentBuilder()
			.newDocument();
		} catch (Exception e) {
				throw new RuntimeException("Dom parser problem.");
		}
	}

	private Node getXmldoc() {
    elements = new LinkedList();
    reverseElements = new LinkedList();
    Node t = `xml(dom,<IntegerList/>);
    for(int i =1 ; i<5 ; i++) {
      t = `addInteger(t,i);
      elements.addLast("" + i);
      reverseElements.addFirst("" + i);
    }
		return t;
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

  public void testSortedInteger() {
		Node list = getXmldoc();
    %match(TNode list) {
      <IntegerList>[<(Int|Integer)>(#TEXT(s1))</(Int|Integer)>,
                    <(Integer|Int)>(#TEXT(s2))</(Integer|Int)>]</IntegerList> -> {
				 if(`s1.compareTo(`s2) > 0) { System.out.println("testSortedInteger"); }
				 assertFalse("Expects the matched integers to be ordered",
										 `s1.compareTo(`s2) > 0);
			 }
    }
  }

	public void testSwapElements() {
		Node list = getXmldoc();
    LinkedList res = extractElements(swapElements(list));
    if(reverseElements!= res) { System.out.println("testSwapElements"); }
    assertEquals("ExtractElement extract elements in order",
								 reverseElements, res);
  }

	public void testExtractElements() {
		Node list = getXmldoc();
		LinkedList res = extractElements(list);
    if(elements!= res) { System.out.println("testExtractElements"); }
		assertEquals("ExtractElement extract elements in order",
								 elements, res);
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
      </IntegerList> -> { 
         res.add(`s1); 
       }
    }
    return res;
  }
  
  public String dd(String x) {
      return x+x;
    }

	public void testAttributeMatch(){
		Node node = `xml(dom,
				<?xml version="1.0" encoding="UTF-8" ?>
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
    if(15!= res) { System.out.println("testAttributeMatch"); }
		assertEquals(
			"XML attibute matching should not depend on the order of the attibutes", 
			res, 15);
	}

}
