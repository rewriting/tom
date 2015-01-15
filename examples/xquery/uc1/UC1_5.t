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
import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

// <!-- For each book found at both bstore1.example.com and bstore2.example.com, list the title of the book and its price from each source.-->

// <books-with-prices>
//   {
//     for $b in doc("http://bstore1.example.com/bib.xml")//book,
//         $a in doc("http://bstore2.example.com/reviews.xml")//entry
//     where $b/title = $a/title
//     return
//         <book-with-prices>
//             { $b/title }
//             <price-bstore2>{ $a/price/text() }</price-bstore2>
//             <price-bstore1>{ $b/price/text() }</price-bstore1>
//         </book-with-prices>
//   }
// </books-with-prices>

// result expected

// <books-with-prices>
//     <book-with-prices>
//         <title>TCP/IP Illustrated</title>
//         <price-bstore2>65.95</price-bstore2>
//         <price-bstore1>65.95</price-bstore1>
//     </book-with-prices>
//     <book-with-prices>
//         <title>Advanced Programming in the Unix environment</title>
//         <price-bstore2>65.95</price-bstore2>
//         <price-bstore1>65.95</price-bstore1>
//     </book-with-prices>
//     <book-with-prices>
//         <title>Data on the Web</title>
//         <price-bstore2>34.95</price-bstore2>
//         <price-bstore1>39.95</price-bstore1>
//     </book-with-prices>
// </books-with-prices> 


public class UC1_5 {

  %include {TNode.tom}
  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_5 uc1 = new UC1_5();
	uc1.run("bib.xml","reviews.xml");
  }

  private void run(String xmlfile1, String xmlfile2) 
  {
	xtools = new XmlTools();

	TNode xmldocument1 = (TNode)xtools.convertXMLToATerm(xmlfile1); 
	TNode xmldocument2 = (TNode)xtools.convertXMLToATerm(xmlfile2); 
	executeQuery(xmldocument1.getDocElem(), xmldocument2.getDocElem());

	System.out.println();
  }

  private void executeQuery(TNode booklist, TNode reviewlist) 
  {
	System.out.println("<books-with-prices>");
	%match (TNode booklist) {
	  <bib>(_*, book,_*)</bib> ->
	   {
  		 printBook(`book, reviewlist);  
	
	   }
	}
	System.out.println("</books-with-prices>");
  }
  
  private void printBook(TNode book, TNode reviewlist) 
  {	
	%match (TNode book) {
	  <book><title>#TEXT(thetitle)</title><price>#TEXT(theprice)</price></book> -> {
		 if (bookHasReview(`thetitle, reviewlist)) {
		   System.out.println("<book-with-prices>");
		   System.out.print("  <title>");
		   System.out.print(`thetitle);
		   System.out.println("</title>");
		   
		   `printPrice(thetitle, reviewlist, theprice); 
		   System.out.println("</book-with-prices>\n");
		 }
	   }
	}
  }
  
  private boolean bookHasReview(String title, TNode reviewlist) 
  {

	%match (TNode reviewlist) {
	  <reviews><entry><title>#TEXT(thetitle)</title></entry></reviews> ->{
		 if (`thetitle == title) {
		   return true; 
		 }
	   }
	}
	return false;
  }

  private void printPrice(String title, TNode reviewlist, String price1) 
  {

	%match (TNode reviewlist) {
	  <reviews><entry><title>#TEXT(thetitle)</title><price>#TEXT(theprice)</price></entry></reviews> ->{
		 if (`thetitle == title) {
		   //		 System.out.println("  <author>");
		   System.out.print("  <price-bstore2>");
		   System.out.print(`theprice);
		   System.out.println("</price-bstore2>");
		   System.out.print("  <price-bstore1>");
		   System.out.print(price1);
		   System.out.println("</price-bstore1>");
		   //System.out.println("  </author>");
		 }
	   }
	}
  }


}
