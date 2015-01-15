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

import java.util.*; 

import xquery.util.*;
import java.io.*; 


public class UC1_8 {
    
  %include {TNode.tom}
  private XmlTools xtools;
 private GenericTraversal traversal = new GenericTraversal();

  // declare document variables    
  // doc("http://bstore1.exemple.com/bib.xml")
  private TNode _XmlDocument01 = null;
    
		
  // declare xquery variables
  //	for $b in doc("http://bstore1.exemple.com/bib.xml")/bib/book
  private TNode _b = null;
  private TNodeList _bList =null;

  private TNodeList _eList = null;
    

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_8 uc = new UC1_8();
	String filename;

	if (args.length == 0) {
	  filename="xquery/data/bib.xml";
	}
	else {
	  filename=args[0];
	}

	uc.run(filename);
  }


  private void run(String filename) 
  {
	xtools = new XmlTools();

	_XmlDocument01 = (TNode)xtools.convertXMLToATerm(filename); 

	try {
	  executeQuery(_XmlDocument01.getDocElem());
	}
	catch (ObjectIncompatibleException e) {
	  System.out.println("error message here");
	}
	

  }


 
  private void executeQuery(TNode documentNode) 
	throws ObjectIncompatibleException 
  {
	try {

	  _bList = _collectData01(documentNode,"book");
	  // System.out.println(_bList);
	  _forLoop01(_bList);

	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }


  private void _forLoop01(TNodeList nodelist) 
	throws ObjectIncompatibleException 
  {
	try {
	  
	  %match (TNodeList nodelist) {
		(_*, aChildNode@<book></book>, _*) -> {
		   // assign value to class-scope variable _b																	
		   _b = aChildNode;
		   
		   _eList = _collectData02(_b, "or", "Suciu");
		   if (_exists(_eList)) {
			 _createResult01(_b,_eList);
		   }
		 }	   
	  }

	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }

  
  private boolean _exists(TNodeList node) 
	throws ObjectIncompatibleException
  {
	ObjectComparison comparor = new ObjectComparison();

	if (node.getLength()>0)
	  return true;
	else 
	  return false;
  }



  private void _createResult01(TNode b, TNodeList elist) 
  {
	System.out.println("<book>");
	
	%match (TNode b) {
	  <_>title@<title></title></_> ->{
		 xtools.printXMLFromATerm(title);
	   }
	}
	System.out.println();
	
	%match (TNodeList elist) {
	  (_*, aChild@<_></_>, _*) -> {
		 xtools.printXMLFromATerm(aChild);
	   }
	}
	System.out.println("</book>");
	
  }
  
  private boolean _endWith(TNode node, String endwith) 
  {
	if (node.hasName()) {
	  String name=node.getName();
	  int location = name.lastIndexOf(endwith);
	  if (location==-1) {
		return false; 
	  }
	  else {
		if ((location + endwith.length()) == name.length()) {
		  return true;
		}
	  }
	  
	}
	return false;
  }


  private boolean _isContain(TNode node,  String str) 
  {
// 	boolean result=false; 
// 	Collect1 collect = new Collect1() { 
// 		public boolean apply(ATerm t) 
// 		{ 
// 		  if(t instanceof TNode) { 
// 			// %match(TNode t) { 
// // 			  <author> </author> -> {
// // 				 vector.add(t); 
// // 				 return true; 
// // 			   } 
// // 			}
// 			TNode tnode=(TNode)t; 
// 			if (tnode.isTextNode()) {
// 			  String text = tnode.getData();
// 			  if (text.indexOf(str) != -1) {
// 				result = true;
// 				return false;
// 			  }
// 			}
			
// 		  } 
// 		  return true; 
// 		} // end apply 
// 	  }; // end new 
// 	traversal.genericCollect(node, str);

// 	return result;

	boolean result = false; 
	
	if (node.isTextNode()) {
	  String text = node.getData();
	  if (text.indexOf(str) != -1) {
		return true; 
	  }
	}

	else if (node.hasChildList()) {
	  TNodeList nodelist= node.getChildList();
	  int len = nodelist.getLength();
	  for(int i=0; i<len; i++) {
		TNode onenode = nodelist.getTNodeAt(i);
		if (_isContain(onenode, str)) 
		  return true; 
	  }
	}
	else {
	  return false;
	}

	return false;
  }




  protected TNodeList _collectData02(TNode subject, final String endwith,final  String containstring) { 
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
			if (_endWith(tnode, endwith) && _isContain(tnode, containstring)) {
			  vector.add(t);
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
	  nodeList= nodeList.append(n);
	}			   		
	return nodeList; 
	//	return `xml(<dashdash> nodeList* </dashdash>);
  }


  protected TNodeList _collectData01(TNode subject,final String clauseName) { 
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
	//	return `xml(<dashdash> nodeList* </dashdash>);
  }
}
