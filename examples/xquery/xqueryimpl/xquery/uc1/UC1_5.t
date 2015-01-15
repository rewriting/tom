/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the Inria nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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
import jtom.runtime.*;
import java.io.*;

import java.util.*; 

import xquery.util.*;



public class UC1_5 {

  %include {TNode.tom}

  private XmlTools xtools=new XmlTools();
  private GenericTraversal traversal = new GenericTraversal();

  private TNode _b=null;
  private TNode _a=null;

  private TNodeList _bList=null;
  private TNodeList _aList=null;

  private TNode _xmlDocument01 = null;
  private TNode _xmlDocument02 = null;
		
		
	
  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  TNodeList _tt=new TNodeList(getTNodeFactory());

  public static void main(String args[]) 
  {
	UC1_5 uc = new UC1_5 ();
	String filename01;
	String filename02;

	if (args.length == 0) {
	  filename01="xquery/data/bib.xml"; 
	  filename02="xquery/data/reviews.xml"; 
	}
	else {
	  filename01=args[0];
	  filename02=args[1];
	}

	uc.run(filename01,filename02);
  }

  private void run(String filename01, String filename02) 
  {
				

	_xmlDocument01 = (TNode)xtools.convertXMLToATerm(filename01); 
	_xmlDocument02 = (TNode)xtools.convertXMLToATerm(filename02); 
				
	_bList=_collectData(_xmlDocument01.getDocElem(),"book");
	_aList=_collectData(_xmlDocument02.getDocElem(),"entry");

	
	try {
	  executeQuery(_bList,_aList);
	}
	catch (ObjectIncompatibleException e) {
	  System.out.println("error message here");
	}
  }

  
  private void executeQuery(TNodeList nodelist1, TNodeList nodelist2) 
	throws ObjectIncompatibleException 
  {
	try {
	  System.out.println("<books-with-prices>");

	  _forLoop01( nodelist1, nodelist2);

	  System.out.println("</books-with-prices>");
	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }


  private void _forLoop01(TNodeList nodelist1, TNodeList nodelist2) 
	throws ObjectIncompatibleException 
  {
	try {

	  %match (TNodeList nodelist1) {
		(_*,aBook@<book></book>, _*) -> {
		   _b=aBook;
		   
		   _forLoop02(nodelist2);
		 }
	  }

	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }


  private void _forLoop02(TNodeList nodelist) 
	throws ObjectIncompatibleException 
  {
	try {

	  %match (TNodeList nodelist) {
		(_*, anEntry@<entry></entry>, _*) -> {
		   _a=anEntry;

		   if (_isMatch01(_b,_a)) {
			 _createResult01(_b,_a);
		   }
		 }
	  }
	  
	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }


  
  private boolean _isMatch01(TNode node1, TNode node2) 
	throws ObjectIncompatibleException 
  {
	ObjectComparison comparor = new ObjectComparison(); 
	
	%match (TNode node1, TNode node2) {
	  <book>title1@<title></title></book>
		 , <entry>title2@<title></title></entry> -> {

	
		 if (title1.isEqual(title2)) {
		   return true; 
		 }
	   }
	}
	return false;
  }

  private void _createResult01(TNode b, TNode a)
  {
	System.out.println("<book-with-prices>");
	%match (TNode b) {
	  <_><title>#TEXT(title)</title></_> -> {
		 System.out.println("<title>" + title + "</title>");
	   }
	}

	%match (TNode b, TNode a) {
	  <_><price>#TEXT(price1)</price></_>,
		 <_><price>#TEXT(price2)</price></_> -> {
		 System.out.println("<price-stores2>" + price2 + "</price-stores2>");
		 System.out.println("<price-stores1>" + price2 + "</price-stores1>");
	   }
	}
  
	
	System.out.println("</book-with-prices>");
  }
   
  protected TNodeList _collectData(TNode subject,final String clauseName) { 
	final Vector vector=new Vector(); 

	Collect1 collect = new Collect1() { 
		public boolean apply(ATerm t) 
		{ 
		  if(t instanceof TNode) { 
			// %match(TNode t) { 
// 			  <author> </author> -> {
// 				 vector.add(t); 
// 				 return true; 
// 			   } 
// 			}
			TNode tnode=(TNode)t; 
			
			
			if (tnode.hasName() && (tnode.getName()==clauseName)) {
			  vector.add(t);
			  return true;
			}
		  } 
		  return true; 
		} // end apply 
	  }; // end new 
	traversal.genericCollect(subject, collect); 
	
	//	System.out.println(vector.size());
	
	TNodeList nodeList = getTNodeFactory().makeTNodeList();
	Enumeration e = vector.elements();
	while (e.hasMoreElements()) {
	  TNode n=(TNode)(e.nextElement());
	  //	  xtools.printXMLFromATerm(n);
	  //System.out.println("__");
	  nodeList= nodeList.insert(n);
	}			   		

	return nodeList; 
	//return `xml(<dashdash> nodeList* </dashdash>);
  }

}
