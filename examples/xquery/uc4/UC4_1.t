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

package xquery.uc4;
// List the item number and description of all bicycles that currently have an auction in progress, ordered by item number.

// Solution in XQuery:

// <result>
//   {
//     for $i in doc("items.xml")//item_tuple
//     where $i/start_date <= current-date()
//       and $i/end_date >= current-date() 
//       and contains($i/description, "Bicycle")
//     order by $i/itemno
//     return
//         <item_tuple>
//             { $i/itemno }
//             { $i/description }
//         </item_tuple>
//   }
// </result>

// Note:

// This solution assumes that the current date is 1999-01-31.

// Expected Result:

// <result>
//     <item_tuple>
//         <itemno>1003</itemno>
//         <description>Old Bicycle</description>
//     </item_tuple>
//     <item_tuple>
//         <itemno>1007</itemno>
//         <description>Racing Bicycle</description>
//     </item_tuple>
// </result>




import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*; 


public class UC4_1 
{

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC4_1 uc1 = new UC4_1();
	uc1.run("items.xml");
  }

  private void run(String xmlfile1) 
  {
	xtools = new XmlTools();

	TNode xmldocument1 = (TNode)xtools.convertXMLToATerm(xmlfile1); 

	String result = executeQuery(xmldocument1.getDocElem(), new Date());
	System.out.println(result);	

	getDate("1999-11-11"); 
  }

  private String executeQuery(TNode book, Date date) 
  {
	String result = "<result>";
	int count=0;
	Collection a = new ArrayList();

	%match (TNode book) {
	  <items>(_*, item, _*)</items> -> 
	   {
		 a.add(createItem(`item, date));
		 //System.out.println(book.text());
     System.out.println(book);
	   }
	}

	result = result + "</result>";

	return result;
  }

  private Date getDate(String s) {
	%match (String s) {
	  (year*, 'a', month* ,'a',day*) -> {
		System.out.println(`year); 
	  }
	}
	return null;
  }

  private TNode createItem(TNode item, Date date) 
  {
	String result = "";

	%match (TNode item) {
	  <item_tuple><itemno>#TEXT(itemno)</itemno><description>#TEXT(description)</description>
		 <start_date>#TEXT(startdate)</start_date><end_date>#TEXT(enddate)</end_date></item_tuple> -> 
	   {
		 return null;
	   }
	}
	
	return null; 
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



