import jtom.runtime.xml.*;
import jtom.runtime.xml.adt.*;
import aterm.*;
import jtom.runtime.*;

public class Person {
  
  %include{ TNode.tom }
    
  private XmlTools xtools;
  private GenericTraversal traversal = new GenericTraversal();
  private TNodeFactory getTNodeFactory() {
      return xtools.getTNodeFactory();
  }

  public static void main (String args[]) {
    Person person = new Person();
    person.run("person.xml");
  }

  private void run(String filename){
    xtools = new XmlTools();
    TNode term = (TNode)xtools.convertXMLToATerm(filename);
    
    term = sortAge(term.getDocElem());
    xtools.printXMLFromATerm(term);
  }
    
  private TNode sort(TNode subject) {
    %match(TNode subject) {
    	<Persons []>
    	X1*
      p1@<Person []>[<FirstName>[#TEXT(n1)]</FirstName>]</Person>
      X2*
      p2@<Person []>[<FirstName>[#TEXT(n2)]</FirstName>]</Person>
      X3*
      </Persons> -> {
         if(n1.compareTo(n2) > 0) {
          return ``sort(<Persons>X1* p2 X2* p1 X3*</Persons>);	
        }	
      }
      
      _ -> { return subject; }     
    }
  }
  
	private TNode sortAge(TNode subject) {
			%match(TNode subject) {
				<Persons []>
				X1*
				p1@<Person [Age=#TEXT(a1)]>[]</Person>
				X2*
				p2@<Person [Age=#TEXT(a2)]>[]</Person>
				X3*
				</Persons> -> {
					 if(a1.compareTo(a2) > 0) {
						return ``sortAge(<Persons>X1* p2 X2* p1 X3*</Persons>);	
					}	
				}
      
				_ -> { return subject; }     
			}
		}
  
}
