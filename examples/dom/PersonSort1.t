import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class PersonSort1 {
  private Document dom;  

  %include{ dom.tom }
    

  public static void main (String args[]) {
    PersonSort1 person = new PersonSort1();
    person.run("person.xml");
  }

  private void run(String filename){
    try {
      dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
		Element e = dom.getDocumentElement();
		dom.replaceChild(sort(e),e);
	
      Transformer transform = TransformerFactory.newInstance().newTransformer();
      StreamResult result = new StreamResult(new File("Sorted.xml"));
      transform.transform(new DOMSource(dom), result);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Node sort(Node subject) {
    %match(TNode subject) {
     <Persons>(X1*,p1,X2*,p2,X3*)</Persons> -> {
        if(compare(p1,p2) > 0) {
          return sort(`xml(dom,<Persons>X1* p2 X2* p1 X3*</Persons>));
        }	
      }
      
      _ -> { return subject; }     
    }
  }

  
  private int compare(Node t1, Node t2) {
    %match(TNode t1, TNode t2) {
      <_ Age=a1></_>, <_ Age=a2></_> -> { return a1.compareTo(a2); }
    }
    return 0;
  }
  
  
}

