import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

public class PersonSort {
  %include{ TNode.tom }
  //%include{ charlist.tom }
         
  private XmlTools xtools;
  private Factory getTNodeFactory() {
      return xtools.getTNodeFactory();
  }
  
  public static void main (String args[]) {
    PersonSort person = new PersonSort();
    person.run("person.xml");
  }

 private void run(String filename){
    xtools = new XmlTools();
    TNode term = (TNode)xtools.convertXMLToATerm(filename);
    
//    searchJu(term.getDocElem());
    term = sort(term.getDocElem());
    xtools.printXMLFromATerm(term);
  }
   
  private TNode sort(TNode subject) {
    %match(TNode subject) {
     <Persons>(X1*,p1,X2*,p2,X3*)</Persons> -> {
        if(compare(p1,p2) > 0) {
          return sort(`xml(<Persons>X1* p2 X2* p1 X3*</Persons>));
        }	
      }
      
      _ -> { return subject; }     
    } 
  }
  	 
  private int compare(TNode t1, TNode t2) {
    %match(TNode t1, TNode t2) {
      <Person Age=a1><FirstName>#TEXT(n1)</FirstName></Person>,
      <Person Age=a2><FirstName>#TEXT(n2)</FirstName></Person>
      -> { return n1.compareTo(n2); }
    }
    return 0;
  }

/*
  private void searchJu(TNode subject) {
    %match(TNode subject) {
      <Persons><Person><FirstName>#TEXT((_*,"J","u",X*))</FirstName></Person></Persons> -> {
        //<_><_><_>#TEXT((_*,"J","u",X*))</_></_></_> -> {
        System.out.println("Hello Mr Ju" + `X); 
      }
    }
  }
  */
}

