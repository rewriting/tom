import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.parsers.*;

public class TestDom {
	
  private Document dom;

	%include{dom.tom}

	public static void main (String args[]) {
		TestDom test = new TestDom();
		test.run("file.xml");
	}

	public void run(String file) {
		try {
		dom = DocumentBuilderFactory
			.newInstance()
			.newDocumentBuilder()
			.newDocument();
		} catch (Exception e) {
				throw new RuntimeException("Dom parser problem.");
		}

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
		assertTrue(checkAttributeMatch(node) == 15);
	}

	public int checkAttributeMatch(Node eRoot){
		int res = 0;
		%match(TNode eRoot) {
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
		return res;
	}

	static void  assertTrue(boolean condition) {
		if(!condition) {
			throw new RuntimeException("assertion failed.");
		}
	}

}
