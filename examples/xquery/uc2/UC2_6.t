
// Make a nested list of the section elements in Book1, preserving their original attributes and hierarchy. Inside each section element, include the title of the section and an element that includes the number of figures immediately contained in the section.

// Solution in XQuery:

// declare function local:section-summary($book-or-section as element())
//   as element()*
// {
//   for $section in $book-or-section
//   return
//     <section>
//        { $section/@* }
//        { $section/title }       
//        <figcount>         
//          { count($section/figure) }
//        </figcount>                
//        { local:section-summary($section/section) }                      
//     </section>
// };

// <toc>
//   {
//     for $s in doc("book.xml")/book/section
//     return local:section-summary($s)
//   }
// </toc> 

// Editorial note 	 
// This solution was provided by Michael Wenger, a student at the University of Würzburg.

// Expected Result:

// <toc>
//     <section id="intro" difficulty="easy">
//         <title>Introduction</title>
//         <figcount>0</figcount>
//         <section>
//             <title>Audience</title>
//             <figcount>0</figcount>
//         </section>
//         <section>
//             <title>Web Data and the Two Cultures</title>
//             <figcount>1</figcount>
//         </section>
//     </section>
//     <section id="syntax" difficulty="medium">
//         <title>A Syntax For Data</title>
//         <figcount>1</figcount>
//         <section>
//             <title>Base Types</title>
//             <figcount>0</figcount>
//         </section>
//         <section>
//             <title>Representing Relational Databases</title>
//             <figcount>1</figcount>
//         </section>
//         <section>
//             <title>Representing Object Databases</title>
//             <figcount>0</figcount>
//         </section>
//     </section>
// </toc> 



import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*; 


public class UC2_6 
{

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC2_6 uc1 = new UC2_6();
	uc1.run("book.xml");
  }

  private void run(String xmlfile1) 
  {
	xtools = new XmlTools();

	TNode xmldocument1 = (TNode)xtools.convertXMLToATerm(xmlfile1); 

	String result = executeQuery(xmldocument1.getDocElem());
	System.out.println(result);	
  }

  private String executeQuery(TNode book) 
  {
	String result = "<toc>\n";
	%match (TNode book) {
	  <book>(_*, node, _*)</book> -> 
	   {
		 //		 xtools = new XmlTools();
		 result = result + createBook(node); 
		 //		 xtools.printXMLFromATerm(node);
	   }
	}
	result = result + "</toc>\n";	
	return result;
  }

  private int countFigure(TNode node) 
  {
	int result =0; 
	%match (TNode node) {
	  <section><figure></figure></section> -> 
	   {
		 result++;
	   }
	}
	//	System.out.println("deo hieu the nao");
	return result;
  }

  private String createBook(TNode node) 
  {
	String result =""; 
	%match (TNode node) {

	   <section id=id difficulty=diffi><title>#TEXT(title)</title>anything*</section> -> 
	   {
		 int figcount=countFigure(node);
		 result = result  + "\n"+ createCascadeXML("<section id=\"" + id + "\" difficulty=\"" + diffi + "\">",
											createXML("<title>", title, "</title>",2) + "\n" 
											+ createXML("<figcount>", "" + figcount, "</figcount>",2)
											+ createBook(`xml(<hehe> anything* </hehe>)),
											"</section>",
											1);
		 return result;
	   }

	   <section><title>#TEXT(title)</title>anything*</section> -> 
	   {
		 int figcount=countFigure(node);
		 result = result  + "\n"+ createCascadeXML("<section>",
											createXML("<title>", title, "</title>",2) + "\n" 
											+ createXML("<figcount>", "" + figcount, "</figcount>",2)
											+ createBook(`xml(<hehe> anything* </hehe>)),
											"</section>",
											1);
		 return result; 
	   }

	  <hehe>nestedsection@<section>anything*</section></hehe> -> 
	   {
		 result = result + createBook(nestedsection);
	   }

	}
	//	System.out.println("deo hieu the nao");
	return result;
  }

  private String calculIndent(int indentlevel)
  {
	String indent = "";
	for (int i=0; i<indentlevel; i++) {
	  indent = indent + "  ";
	}
	return indent; 
  }

  private String createCascadeXML(String openClause, String data, String closeClause, int indentLevel)
  {
	String indent = calculIndent(indentLevel); 
	String xmlString = "";
	xmlString = openClause + "\n";
	xmlString = xmlString + "  " + data + "\n"; 
	xmlString = xmlString + closeClause;
	xmlString = indentXMLBlock(xmlString, indentLevel);
	return xmlString; 
  }

  private String indentXMLBlock(String xml, int indent) 
  {
	return xml;
  }

  private String createXML(String openClause, String data, String closeClause, int indentLevel) 
  {
	String indent = calculIndent(indentLevel); 
	String xmlString = "";
	xmlString = openClause;
	xmlString = xmlString + data; 
	xmlString = xmlString + closeClause ;
	xmlString = indentXMLBlock(xmlString, indentLevel);
	return xmlString; 
  }
}



