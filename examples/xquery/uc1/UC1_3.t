import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

// <!-- For each book in the bibliography, list the title and authors, grouped inside a "result" element.-->

// <results>
// {
//     for $b in doc("http://bstore1.example.com/bib.xml")/bib/book
//     return
//         <result>
//             { $b/title }
//             { $b/author  }
//         </result>
// }
// </results>



public class UC1_3 {

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_3 uc1 = new UC1_3();
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
	  <bib>(_*, book,_*)</bib> ->
	   {
		 printBook(book);  
	   }
	}
	System.out.println("</results>");
  }
  
  
  private void printBook(TNode book) 
  {

	%match (TNode book) {
	  <book><title>#TEXT(thetitle)</title></book> -> {
		 System.out.println("<result>");		 
		 System.out.print("  <title>");
		 System.out.print(thetitle);
		 System.out.println("  </title>");
		 
		 printAuthor(book); 
		 System.out.println("</result>\n");
	   }
	}
	
  }
  
  private void printAuthor(TNode book) 
  {
	%match (TNode book) {
	  <book><author><last>#TEXT(lastname)</last><first>#TEXT(firstname)</first></author></book> -> {
		 System.out.println("  <author>");
		 System.out.print("    <last>");
		 System.out.print(lastname);
		 System.out.println("</last>");
		 System.out.print("    <first>");
		 System.out.print(firstname);
		 System.out.println("</first>");		 
		 System.out.println("  </author>");
	   }
	}
  }
}
