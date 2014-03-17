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

// <!-- For each book in the bibliography, list the title and authors, grouped inside a "result" element.-->

// <results>
// {
//     for $b in doc("http://bstore1.example.com/bib.xml")/bib/book
//     return
//         <result>
//             { $b/title }
//             { $b/author  }
//         </result>
// }
// </results>



public class UC1_3 {

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_3 uc1 = new UC1_3();
	uc1.run("bib.xml");
  }

  private void run(String xmlfile) 
  {
	xtools = new XmlTools();

	TNode xmldocument = (TNode)xtools.convertXMLToATerm(xmlfile);
	executeQuery(xmldocument.getDocElem());

	System.out.println();
  }

  private void executeQuery(TNode booklist) 
  {
	System.out.println("<results>");
	%match (TNode booklist) {
	  <bib>(_*, book,_*)</bib> ->
	   {
		 printBook(`book);  
	   }
	}
	System.out.println("</results>");
  }
  
  
  private void printBook(TNode book) 
  {

	%match (TNode book) {
	  <book><title>#TEXT(thetitle)</title></book> -> {
		 System.out.println("<result>");		 
		 System.out.print("  <title>");
		 System.out.print(`thetitle);
		 System.out.println("  </title>");
		 
		 printAuthor(book); 
		 System.out.println("</result>\n");
	   }
	}
	
  }
  
  private void printAuthor(TNode book) 
  {
	%match (TNode book) {
	  <book><author><last>#TEXT(lastname)</last><first>#TEXT(firstname)</first></author></book> -> {
		 System.out.println("  <author>");
		 System.out.print("    <last>");
		 System.out.print(`lastname);
		 System.out.println("</last>");
		 System.out.print("    <first>");
		 System.out.print(`firstname);
		 System.out.println("</first>");		 
		 System.out.println("  </author>");
	   }
	}
  }
}
