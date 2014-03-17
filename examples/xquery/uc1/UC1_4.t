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

import java.util.*; 

// <!-- For each author in the bibliography, list the author's name and the titles 
// of all books by that author, grouped inside a "result" element. -->

// <results>
//   {
//     let $a := doc("http://bstore1.example.com/bib/bib.xml")//author
//     for $last in distinct-values($a/last),
//         $first in distinct-values($a[last=$last]/first)
//     order by $last, $first
//     return
//         <result>
//             <author>
//                <last>{ $last }</last>
//                <first>{ $first }</first>
//             </author>
//             {
//                 for $b in doc("http://bstore1.example.com/bib.xml")/bib/book
//                 where some $ba in $b/author 
//                       satisfies ($ba/last = $last and $ba/first=$first)
//                 return $b/title
//             }
//         </result>
//   }
// </results> 


// result expected
//
// <results>
//     <result>
//         <author>
//             <last>Abiteboul</last>
//             <first>Serge</first>
//         </author>
//         <title>Data on the Web</title>
//     </result>
//     <result>
//         <author>
//             <last>Buneman</last>
//             <first>Peter</first>
//         </author>
//         <title>Data on the Web</title>
//     </result>
//     <result>
//         <author>
//             <last>Stevens</last>
//             <first>W.</first>
//         </author>
//         <title>TCP/IP Illustrated</title>
//         <title>Advanced Programming in the Unix environment</title>
//     </result>
//     <result>
//         <author>
//             <last>Suciu</last>
//             <first>Dan</first>
//         </author>
//         <title>Data on the Web</title>
//     </result>
// </results>


public class UC1_4 {

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_4 uc1 = new UC1_4();
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
	System.out.println("<results>");
	printAuthor(booklist);  	
	System.out.println("</results>");
  }
  
  private void printAuthor(TNode booklist) 
  {	
	Vector lastnamelist = new Vector(); 
	Vector firstnamelist = new Vector();
	
	// get distinct author
	%match (TNode booklist) {
	  <bib><book><author><last>#TEXT(thelast)</last><first>#TEXT(thefirst)</first></author></book></bib> -> {
		 if (!`entryExiste(thelast, thefirst, lastnamelist, firstnamelist)) {  // not existe,  add to list
		   lastnamelist.add(`thelast); 
		   firstnamelist.add(`thefirst);
		 }
	   }
	}

	// find books
	Enumeration elast = lastnamelist.elements();  
	Enumeration efirst = firstnamelist.elements();  
	while (elast.hasMoreElements()) {
	  String last = (java.lang.String)elast.nextElement();
	  String first = (java.lang.String)efirst.nextElement();
	  System.out.print("<result>\n");
	  System.out.print("  <author>\n");
	  System.out.print("    <last>");
	  System.out.print(last);
	  System.out.print("</last>\n");
	  System.out.print("    <first>");
	  System.out.print(first);
	  System.out.print("</first>\n");
	  System.out.print("  </author>\n");	  
	  printBookList(last, first, booklist);
	  System.out.print("</result>\n");
	}	
  }

  private boolean entryExiste(String lastname, String firstname, Vector lastnamelist, Vector firstnamelist)
  {
	Enumeration elast = lastnamelist.elements();  
	Enumeration efirst = firstnamelist.elements();  
	while (elast.hasMoreElements()) {
	  String last = (java.lang.String)elast.nextElement();
	  String first = (java.lang.String)efirst.nextElement();
	  if ((lastname == last) && (firstname==first))
		return true; 
	}
	return false;
  }


  private void printBookList(String last, String first, TNode booklist)
  {
	%match (TNode booklist) {
	  <bib><book><title>#TEXT(title)</title><author><last>#TEXT(lastname)</last><first>#TEXT(firstname)</first></author></book></bib> ->{
		 if ((`firstname==first) && (`lastname==last)) {
		   System.out.print("    <title>");
		   System.out.print(`title);
		   System.out.print("</title>\n");
		 }
	   }
	}
  }


}
