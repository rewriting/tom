import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;


public class UC1 {

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1 uc1 = new UC1();
	uc1.run("bib.xml");
  }

  private void run(String xmlfile) 
  {
	xtools = new XmlTools();

	TNode xmldocument = (TNode)xtools.convertXMLToATerm(xmlfile);
	executeQuery(xmldocument.getDocElem());

	System.out.println();
  }

  private void executeQuery(TNode booklist) 
  {
	System.out.println("<bib>");
	%match (TNode booklist) {
	  <bib>book</bib> ->
	   {
		 if (bookMatch(book)) {
		   printBook(book);
		 }
	   }
	}
	System.out.println("</bib>");
  }
  
  private boolean bookMatch(TNode book) {
	%match (TNode book) {
	  <book year=theyear><publisher>#TEXT(thepublisher)</publisher></book> ->
		 {
		   if ((Integer.parseInt(theyear) > 1991) && (thepublisher=="Addison-Wesley")) {
			 return true; 
		   }
		 }
	}
	return false; 
  }

  private void printBook(TNode book) 
  {
	%match (TNode book) {
	  <book year=theyear><title>#TEXT(thetitle)</title></book> ->
	   {
// 		 TNode a = `xml(<book year="`thetile"/>);
// 		 xtools = new XmlTools();
// 		 xtools.printXMLFromATerm(a);
		 System.out.print("<book year = \"");
		 System.out.print(theyear);
		 System.out.println("\">");
		 System.out.print("  <title>");
		 System.out.print(thetitle);
		 System.out.println("</title>");
		 System.out.println("</book>");
		 // how to create a TNode avec `xml with some variable like theyear and thetitle 
	   }
	}
  }
}
