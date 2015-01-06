/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the Inria nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package xquery.uc1;

// Solution in XQuery:

// <bib>
// {
//         for $b in doc("http://bstore1.example.com/bib.xml")//book[author]
//         return
//             <book>
//                 { $b/title }
//                 { $b/author }
//             </book>
// }
// {
//         for $b in doc("http://bstore1.example.com/bib.xml")//book[editor]
//         return
//           <reference>
//             { $b/title }
//             {$b/editor/affiliation}
//           </reference>
// }
// </bib>  

// Expected Result:

// <bib>
//     <book>
//         <title>TCP/IP Illustrated</title>
//         <author>
//             <last>Stevens</last>
//             <first>W.</first>
//         </author>
//     </book>
//     <book>
//         <title>Advanced Programming in the Unix environment</title>
//         <author>
//             <last>Stevens</last>
//             <first>W.</first>
//         </author>
//     </book>
//     <book>
//         <title>Data on the Web</title>
//         <author>
//             <last>Abiteboul</last>
//             <first>Serge</first>
//         </author>
//         <author>
//             <last>Buneman</last>
//             <first>Peter</first>
//         </author>
//         <author>
//             <last>Suciu</last>
//             <first>Dan</first>
//         </author>
//     </book>
//     <reference>
//         <title>The Economics of Technology and Content for Digital TV</title>
//         <affiliation>CITI</affiliation>
//     </reference>
// </bib> 



import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*; 
import java.io.*; 

public class UC1_11 
{

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_11 uc1 = new UC1_11();
	uc1.run("bib.xml");
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
	String result = "<results>\n";
	%match (TNode book) {
	  <bib>(_*, node, _*)</bib> -> 
	   {
		 result = result + createBook(`node) + "\n"; 
	   }
	}
	result = result + "</results>\n";	
	return result;
  }


  private String createBook(TNode book) 
  {
	String result =""; 
	%match (TNode book) {
	  <book><title>#TEXT(title)</title></book> -> 
	   {
		 if (hasAuthor(book)) {
		   result = createCascadeXML("<book>",
									 createXML("<title>", `title,"</title>",2) 
									 + createAuthor(book), 
									 "</book>", 1);
		 }
		 else if (hasEditor(book)) {
		   result= createCascadeXML("<reference>",
									createXML("<title>", `title,"</title>",2) 
									+ createEditor(book), 
									"</reference>", 1);
		 }
		 else { // has editor
		   result = "";
		 }
	   }
	}
	//	System.out.println("deo hieu the nao");
	return result;
  }


  private boolean hasAuthor(TNode book) 
  {
	String result  ="";
	%match (TNode book) {
	  <book><title>#TEXT(title)</title><author></author></book> -> 
	   {
		 return true;
	   }
	}
	return false; 
  }

  private boolean hasEditor(TNode book) 
  {
	String result  ="";
	%match (TNode book) {
	  <book><title>#TEXT(title)</title><editor></editor></book> -> 
	   {
		 return true;
	   }
	}
	return false; 
  }
  

  private String createEditor(TNode book) 
  {
	String result  = "\n";
	%match (TNode book) {
	  <book><title>#TEXT(title)</title><editor><affiliation>#TEXT(affl)</affiliation></editor></book> -> 
	   {
		 result=result + createXML("<affiliation>", `affl, "</affiliation>",2) ;
	   }
	}	
	return result; 
  }

  private String createAuthor(TNode book) 
  {
	//First, redirect System.out so we can retrieve it as a string.
	PrintStream defaultOut = System.out;
	ByteArrayOutputStream outArray = new ByteArrayOutputStream();
	
	System.setOut(new PrintStream(outArray));
 
	String result  = "";
	%match (TNode book) {
	  <book><title>#TEXT(title)</title>author<publisher></publisher><price></price></book> -> 
	   {
		 xtools = new XmlTools();
		 xtools.printXMLFromATerm(`author);
		 result=result + outArray.toString();
		 
 		 outArray.reset();
	   }
	}	
	// Get the test output and restore System.out.
	//	result = outArray.toString();
 	System.setOut(defaultOut);
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



