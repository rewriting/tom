import jtom.runtime.xml.*;
import jtom.runtime.xml.adt.*;
import aterm.*;
import jtom.runtime.*;

public class Action {
  
  %include{ TNode.tom }
    
  private XmlTools xtools;
  private GenericTraversal traversal = new GenericTraversal();
  private TNodeFactory getTNodeFactory() {
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
           //<Comp Label="busy" Type=#TEXT(type) Index=i/>
         <Comp Label="busy" Type=#TEXT(type) Index=i />
         </Action></Actions>
         
         -> {
           System.out.println("Action 1 Localisee ! " + type + "  " + i);
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
      <Actions><Action>
         <Comp Index=#TEXT(i) Label=l Type="Wait"/>
         <Comp Index=#TEXT(j) Label=l Type="Send"/>
      </Action></Actions>
    | <Actions><Action>
         <Comp Index=#TEXT(j) Label=l Type="Send"/>
         <Comp Index=#TEXT(i) Label=l Type="Wait"/>
      </Action></Actions>
         -> {
           System.out.println("Synchronisation entre "+i+" et "+j+" sur le label "+l.getData());
         }

    } // match
    
  }

  
  
}

