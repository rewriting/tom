
// Prepare a (nested) table of contents for Book1, listing all the sections and their titles. Preserve the original attributes of each <section> element, if any.

// Solution in XQuery:

// declare function local:toc($book-or-section as element()) as element()*
// {
//     for $section in $book-or-section/section
//     return
//       <section>
//          { $section/@* , $section/title , local:toc($section) }                 
//       </section>
// };

// <toc>
//    {
//      for $s in doc("book.xml")/book return local:toc($s)
//    }
// </toc> 

// Expected Result:

// <toc>
//     <section id="intro" difficulty="easy">
//         <title>Introduction</title>
//         <section>
//             <title>Audience</title>
//         </section>
//         <section>
//             <title>Web Data and the Two Cultures</title>
//         </section>
//     </section>
//     <section id="syntax" difficulty="medium">
//         <title>A Syntax For Data</title>
//         <section>
//             <title>Base Types</title>
//         </section>
//         <section>
//             <title>Representing Relational Databases</title>
//         </section>
//         <section>
//             <title>Representing Object Databases</title>
//         </section>
//     </section>
// </toc> 



import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*; 
import java.io.*; 

public class UC2_1 
{

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC2_1 uc1 = new UC2_1();
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
		 //	xtools = new XmlTools();
		 result = result + createNode(node, true); 
		 //		 xtools.printXMLFromATerm(node);
	   }
	}
	result = result + "</toc>\n";	
	return result;
  }


  private boolean isSection(TNode node) 
  {
	%match (TNode node) {
	  <section></section> -> 
	   {
		 return true; 
	   }
	   

	}
	   return false;
  }


  private String createNode(TNode node, boolean firstRun) 
  {
	boolean bsection = isSection(node); 

	String result ="";
	
	if (bsection) {
	  %match (TNode node) {
		<section></section> -> 
		 {
		   result = "<section>"+ "\n"; 
		 }
		 <section id=ids></section> -> 
			{
			  result = "<section id = \"" + ids + "\"" + "\n"; 
			}
		 
		 <section difficulty=diffi></section> -> 
			{
			  result = "<section difficulty = \"" + diffi + "\">"+ "\n"; 
			}
		 <section id=ids  difficulty=diffi></section> -> 
			{
			  result = "<section id = \"" + ids + "\"" + "difficulty = \"" + diffi + "\">"+ "\n"; 
			}
	  }

	  //result = result + "<section>";
	}

	%match (TNode node) {
	  <title>#TEXT(title)</title> -> 
	   {
		 if (!firstRun)
		   result = result +  createXML("<title>",title,"</title>",2) + "\n";
	   }
	   
	   <section>(_*, section, _*)</section> -> {
		 result = result +  createNode(section,false);
	   }
	}
	
	
	if (bsection)
	  result = result + "</section>\n";
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


