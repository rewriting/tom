// Prepare a (flat) figure list for Book1, listing all the figures and their titles. Preserve the original attributes of each <figure> element, if any.

// Solution in XQuery:

// <figlist>
//   {
//     for $f in doc("book.xml")//figure
//     return
//         <figure>
//             { $f/@* }
//             { $f/title }
//         </figure>
//   }
// </figlist> 

// Expected Result:

// <figlist>
//     <figure  height="400" width="400">
//         <title>Traditional client/server architecture</title>
//     </figure>
//     <figure  height="200" width="500">
//         <title>Graph representations of structures</title>
//     </figure>
//     <figure  height="250" width ="400">
//         <title>Examples of Relations</title>
//     </figure>
// </figlist> 



import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*; 


public class UC2_2 
{

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC2_2 uc1 = new UC2_2();
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
	String result = "<figlist>\n";
	%match (TNode book) {
	  <book>(_*, node, _*)</book> -> 
	   {
		 //		 xtools = new XmlTools();
		 result = result + createBook(node); 
		 //		 xtools.printXMLFromATerm(node);
	   }
	}
	result = result + "</figlist>\n";	
	return result;
  }


  private String createBook(TNode node) 
  {
	String result =""; 
	%match (TNode node) {
	  <figure height=height width=width><title>#TEXT(title)</title></figure> -> 
	   {
		 result = createCascadeXML("<figure height=\"" + height + "\" width=\"" + width + "\">",
								   createXML("<title>",title,"</title>",3),
								   "</figure>",2)  + "\n"; 
	   }
	   <section><figure height=height width=width><title>#TEXT(title)</title></figure></section> -> {
		   result =  createCascadeXML("<figure height=\"" + height + "\" width=\"" + width + "\">",
							   createXML("<title>",title,"</title>",3),
							   "</>",2) + "\n";
	   }
	   <section><section>other</section></section> -> {
		 result = result + createBook(other);
	   }
	   _ -> {
		 result = result ;
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



