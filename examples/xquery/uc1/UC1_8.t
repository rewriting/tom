
// Find books in which the name of some element ends with the string "or" and the same element contains the string "Suciu" somewhere in its content. For each such book, return the title and the qualifying element.

// Solution in XQuery:

// for $b in doc("http://bstore1.example.com/bib.xml")//book
// let $e := $b/*[contains(string(.), "Suciu") 
//                and ends-with(local-name(.), "or")]
// where exists($e)
// return
//     <book>
//         { $b/title }
//         { $e }
//     </book> 

// In the above solution, string(), local-name() and ends-with() are functions defined in the Functions and Operators document.

// Expected Result:

// <book>
//         <title>Data on the Web</title>
//         <author>
//             <last>Suciu</last>
//             <first>Dan</first>
//         </author>
//  </book>



import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*; 


public class UC1_8 
{

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_8 uc1 = new UC1_8w();
	uc1.run("bib.xml");
  }

  private void run(String xmlfile1) 
  {
	xtools = new XmlTools();

	TNode xmldocument1 = (TNode)xtools.convertXMLToATerm(xmlfile1); 
	String result = executeQuery(xmldocument1.getDocElem());

	System.out.println(result);	
  }

  private String executeQuery(TNode booklist) 
  {
	String result = "<bib>\n";
	%match (TNode booklist) {
	  <bib>(_*,book,_*)</bib> -> 		 
	   {
		 result = result + createBook(booklist);		 
	   }
	}
	result = result + "</bib>\n";	
	return result;
  }
  

  private String createBook(TNode booklist)
  {
	String result = ""; 
	%match (TNode book) {
	  <book><title>#TEXT(title)</title><or)></(or)></book> -> 
					{
					result = result + title + "\n";
					}
	}
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



