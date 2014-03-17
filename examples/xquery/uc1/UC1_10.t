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


public class UC1_10 
{

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_10 uc1 = new UC1_10();
	uc1.run("prices.xml");
  }

  private void run(String xmlfile1) 
  {
	xtools = new XmlTools();

	TNode xmldocument1 = (TNode)xtools.convertXMLToATerm(xmlfile1); 

	String result = executeQuery(xmldocument1.getDocElem());
	System.out.println(result);	
  }

  private String executeQuery(TNode pricelist) 
  {
	String result = "<results>\n";
	Vector vecTitle = new Vector(); 
	Vector vecPrice = new Vector(); // Float

	%match (TNode pricelist) {
	  <prices><book><title>#TEXT(title)</title><price>#TEXT(price)</price></book></prices> -> 
	   {
		 if(`entryExiste(title, vecTitle)) {
		   //		   System.out.println("entry existing");
		   if(`(Float.parseFloat(price) < getPrice(title, vecPrice, vecTitle))) {
         vecPrice = `replacePrice(Float.parseFloat(price), title, vecPrice, vecTitle); 
       }
		 }
		 else {
		   vecTitle.add(`title); 
		   vecPrice.add(Float.valueOf(`price));
		 }
	   }
	}

	Enumeration enumTitle = vecTitle.elements(); 
	Enumeration enumPrice = vecPrice.elements(); 
	while (enumTitle.hasMoreElements()) {
	  String title = (String)enumTitle.nextElement();
	  Float price = (Float)enumPrice.nextElement();
	  result = result + createCascadeXML("<minprice title=\"" + title + "\"",
										 createXML("<price>", 
												   ""+ price ,
												   "</price>",2),
										 "</minprice>",
										 1) + "\n";
	}
	result = result + "</results>\n";	
	return result;
  }

  
  private float getPrice(String title, Vector vecPrice, Vector vecTitle) 
  {
	Enumeration enumtitle = vecTitle.elements();
	int count=0;
	while (enumtitle.hasMoreElements()) {
	  String t= (String)enumtitle.nextElement();
	  if (t==title) {
		return ((Float)vecPrice.elementAt(count)).floatValue(); 
	  }
	  count++; 
	}
	return -1;
  }

  private Vector replacePrice(float newprice, String title, Vector vecPrice, Vector vecTitle) 
  {
	Enumeration enumtitle = vecTitle.elements();
	int count=0;
	while (enumtitle.hasMoreElements()) {
	  String t= (String)enumtitle.nextElement();
	  if (t==title) {
		Vector result=(Vector)vecPrice.clone(); 
		result.setElementAt(new Float(newprice), count);
		return result; 
	  }
	  count++; 
	}
	return vecPrice;
  }


  private boolean entryExiste(String title, Vector vectitle)
  {
	Enumeration enumtitle = vectitle.elements();  
	while (enumtitle.hasMoreElements()) {
	  String onetitle = (java.lang.String)enumtitle.nextElement();
	  if (title == onetitle)
		return true; 
	}
	return false;
  }


  private String createBook(TNode node) 
  {
	String result =""; 
	%match (TNode node) {
	  <title>#TEXT(title)</title> -> 
	   {
		 if (`title.indexOf("XML") >=0) 
		   result = createXML("<title>",`title,"</title>",2)  + "\n"; 
	   }
	   <section><title>#TEXT(title)</title></section> -> {
		 if (`title.indexOf("XML") >=0) 
		   result =  createXML("<title>",`title,"</title>",2) + "\n";
	   }
	   <section><section>other</section></section> -> {
		 result = result + createBook(`other);
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



