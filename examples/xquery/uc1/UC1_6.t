/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
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
import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

// Solution in XQuery:
// <bib>
//   {
//     for $b in doc("http://bstore1.example.com/bib.xml")//book
//     where count($b/author) > 0
//     return
//         <book>
//             { $b/title }
//             {
//                 for $a in $b/author[position()<=2]  
//                 return $a
//             }
//             {
//                 if (count($b/author) > 2)
//                  then <et-al/>
//                  else ()
//             }
//         </book>
//   }
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
//         <et-al/>
//     </book>
// </bib> 


public class UC1_6 
{

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_6 uc1 = new UC1_6();
	uc1.run("bib.xml");
  }

  private void run(String xmlfile1) 
  {
	xtools = new XmlTools();

	TNode xmldocument1 = (TNode)xtools.convertXMLToATerm(xmlfile1); 
	executeQuery(xmldocument1.getDocElem());

	System.out.println();
  }

  private void executeQuery(TNode booklist) 
  {
	String result = "<bib>\n";
	%match (TNode booklist) {
	  <bib>(_*, book,_*)</bib> ->
	   {
		 if (bookHasAuthor(`book)) {
		   result = result + createBook(`book);
		 }
	   }
	}
	result = result + "</bib>";

	System.out.println(result);	
  }
  

  private boolean bookHasAuthor(TNode book) 
  {	
	%match (TNode book) {
	  <book><title>#TEXT(thetitle)</title><author></author></book> -> 
	   { // has author
		 return true; 
	   }
	}
	return false;
  }


  private String createBook(TNode book) 
  {
	String result = ""; 
	%match (TNode book) {
	  <book><title>#TEXT(thetitle)</title></book> -> 
	   {
		 result = createCascadeXML("<book>", 
								 createXML("<title>", 
										   `thetitle, 
										   "</title>", 
										   2) + 
								 createAuthor(book) , 
								 "</book>", 
								 1);
	   }
	}
	return result;
  }
  
  
  private String createAuthor(TNode book) 
  {
	int counter = 0; 
	String result = "";
	%match (TNode book) {
	  <book><author><last>#TEXT(lastname)</last><first>#TEXT(firstname)</first></author></book> -> {
		 counter++; 
		 if (counter<=2) {
		   result = result + createCascadeXML("<author>", 
											  createXML("<last>", `lastname, "</last>", 3) +
											  createXML("<first>", `firstname, "</first>", 3), 
											  "</author>",
											  2);
		 }
		 else {
		   result = result + calculIndent(2) + "<et-al/>" + "\n";	
		 }
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
	xmlString = xmlString + closeClause + "\n";
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
	xmlString = indent + openClause;
	xmlString = xmlString + data; 
	xmlString = xmlString + closeClause + "\n";
	return xmlString; 
  }
}



