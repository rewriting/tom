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

package xquery.uc2;

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
		 result = result + createNode(`node, true); 
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
			  result = "<section id = \"" + `ids + "\"" + "\n"; 
			}
		 
		 <section difficulty=diffi></section> -> 
			{
			  result = "<section difficulty = \"" + `diffi + "\">"+ "\n"; 
			}
		 <section id=ids  difficulty=diffi></section> -> 
			{
			  result = "<section id = \"" + `ids + "\"" + "difficulty = \"" + `diffi + "\">"+ "\n"; 
			}
	  }

	  //result = result + "<section>";
	}

	%match (TNode node) {
	  <title>#TEXT(title)</title> -> 
	   {
		 if (!firstRun)
		   result = result +  createXML("<title>",`title,"</title>",2) + "\n";
	   }
	   
	   <section>(_*, section, _*)</section> -> {
		 result = result +  createNode(`section,false);
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


