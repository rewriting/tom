import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

// <!-- Create a flat list of all the title-author pairs, with each pair enclosed in a "result" element. -->
// <results>
//   {
//     for $b in doc("http://bstore1.example.com/bib.xml")/bib/book,
//         $t in $b/title,
//         $a in $b/author
//     return
//         <result>
//             { $t }    
//             { $a }
//         </result>
//   }
// </results>

public class UC1_2 {

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_2 uc1 = new UC1_2();
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
	System.out.println("<results>");
	%match (TNode booklist) {
	  <bib>book@<book></book></bib> ->
	   {
		 printBook(book);  
	   }
	}
	System.out.println("</results>");
  }
  
  private void printBook(TNode book) 
  {
	%match (TNode book) {
	  <book><title>#TEXT(thetitle)</title>
	  <author><last>#TEXT(lastname)</last><first>#TEXT(firstname)</first></author>
	  </book> -> {
		 System.out.println("<result>");
		 System.out.print("  <title>");
		 System.out.print(thetitle);
		 System.out.println("  </title>");

		 System.out.println("  <author>");
		 System.out.print("    <last>");
		 System.out.print(lastname);
		 System.out.println("</last>");
		 System.out.print("    <first>");
		 System.out.print(firstname);
		 System.out.println("</first>");		 
		 System.out.println("  </author>");
		 //		 xtools = new XmlTools();
		 //		 xtools.printXMLFromATerm(author);
		 
		 System.out.println("</result>\n");
		 // how to create a TNode avec `xml with some variable like theyear and thetitle 
	   }
	}
  }
}
