package xml;

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;
import jtom.runtime.*;
 
public class Action {
  
  %include{ TNode.tom }
    
  private XmlTools xtools;
  private GenericTraversal traversal = new GenericTraversal();
  private Factory getTNodeFactory() {
      return xtools.getTNodeFactory();
  }

  public static void main (String args[]) {
    Action action = new Action();
    action.run("action.xml");
  }

  private void run(String filename){
    xtools = new XmlTools();
    ATerm term = xtools.convertXMLToATerm(filename);
    test1(term);
    test2(term);
  }
    
  private void test1(ATerm subject) {
    TNode t = (TNode) subject;
    t = t.getDocElem();
    %match(TNode t) {
      <Actions><Action>
         comp@<Comp Label="busy" Type=type Index=i />
         </Action></Actions>
         -> {
           System.out.println("Action 1 Localisee ! " + type + "  " + i);
           System.out.println("Comp: " + comp);
         }
      
      _ -> {
          //System.out.println("do not match: " + t);
      }
    } // match
    
  }

  private void test2(ATerm subject) {
    TNode t = (TNode) subject;
    t = t.getDocElem();
    %match(TNode t) {
      <Actions>a</Actions> -> {
        %match(TNode a,TNode a) {
          <Action><Comp Index=i2 Label=l Type="Wait"/></Action>,
          <Action><Comp Index=i1 Label=l Type="Send"/></Action> -> {
            System.out.println("Synchronisation sur le label "+l +" entre "+i1+"(!) et "+i2+"(?)");				
          }	
        }
      }
    } // match
    
  }

  
  
}

