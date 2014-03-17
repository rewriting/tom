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
// List the titles and years of all books published by Addison-Wesley after 1991, in alphabetic order.

import java.util.*; 

// Solution in XQuery:

// <bib>
//   {
//     for $b in doc("http://bstore1.example.com/bib.xml")//book
//     where $b/publisher = "Addison-Wesley" and $b/@year > 1991
//     order by $b/title
//     return
//         <book>
//             { $b/@year }
//             { $b/title }
//         </book>
//   }
// </bib> 

// Expected Result:

// <bib>
//     <book year="1992">
//         <title>Advanced Programming in the Unix environment</title>
//     </book>
//     <book year="1994">
//         <title>TCP/IP Illustrated</title>
//     </book>
// </bib>     



public class UC1_7 
{

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_7 uc1 = new UC1_7();
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
// 	xtools = new XmlTools();
	TNode sortedBookList=sortBook(booklist); 
// 	xtools.printXMLFromATerm(sortedBookList);
	result = result + createBook(sortedBookList);
	result = result + "</bib>\n";	
	return result;
  }
  

  private TNode sortBook(TNode booklist) {
    %match(TNode booklist) {
     <bib>(X1*,p1,X2*,p2,X3*)</bib> -> {
		 if(compare(`p1,`p2) > 0) {
		   return sortBook(`xml(<bib>X1* p2 X2* p1 X3*</bib>));
		 }	
	   }
	   
	   _ -> 
	   { 
		 return booklist; 
	   }     
    } 
  }
  
  private int compare(TNode book1, TNode book2) {
    %match(TNode book1, TNode book2) {
	  <book><title>#TEXT(title1)</title></book>, 
	  <book><title>#TEXT(title2)</title></book> -> 
	   { 
		 return `title1.compareTo(`title2);
	   }
    }
    return 0;
  }


  private String createBook(TNode book) 
  {
	String result = ""; 
	%match (TNode book) {
	  <bib><book year=theyear><title>#TEXT(thetitle)</title><publisher>#TEXT("Addison-Wesley")</publisher></book></bib> -> 
	   {
		 if (Integer.parseInt(`theyear) > 1991) {
		   result = result + createCascadeXML("<book year=" + `theyear + ">", 
											  createXML("<title>", 
														`thetitle, 
														"</title>", 
														2) , 
											  "</book>", 
											  1);
		   result =result +"\n";
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



