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
package xquery.uc3;

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import jtom.runtime.*;

import java.util.*; 

import xquery.util.*;
import java.io.*; 


public class UC3_5_2 {
    
  %include {TNode.tom}
  private XmlTools xtools;
 private GenericTraversal traversal = new GenericTraversal();

  // declare document variables    
  // doc("http://bstore1.exemple.com/bib.xml")
  private TNode _XmlDocument01 = null;
    
		
  // declare xquery variables
  //	for $b in doc("http://bstore1.exemple.com/bib.xml")/bib/book
  private TNode _proc = null;
  private TNode _i1 = null; 
  private TNode _i2 = null;  
  private TNode _n =null;
    

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC3_5_2 uc = new UC3_5_2();
	String filename;

	if (args.length == 0) {
	  filename="xquery/data/report1.xml";
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
	
	executeQuery(_XmlDocument01.getDocElem());
  }


 
  private void executeQuery(TNode documentNode) 
  {
	System.out.println("<critical_sequence>");
	
	TNodeList procList = _collectData01(documentNode);
	
	_proc=procList.getTNodeAt(0);
	TNodeList incisionList = _collectData03(_proc,"incision");
	_i1 = incisionList.getTNodeAt(0);
	_i2 = incisionList.getTNodeAt(1);

	TNodeList nList = _collectData02(_proc);
 	_forLoop01(nList);

	System.out.println("</critical_sequence>");
  }


  private void _forLoop01(TNodeList nlist) 
  {
	%match (TNodeList nlist) {
	  (_*, oneChild, _*) -> {
		_n = oneChild;
		//		System.out.println(_n);
		if (_isMatch02(_n)  && _isMatch01(_n)) {
		  _createResult01(_n);
		}
	  }
	}
  }

  private boolean _isMatch02(TNode node) 
  {
	if (_isTNodeContainTNode(_i1, node))
	  return false;
	else 
	  return  true;
	  
  }

  private void _createResult01(TNode node) 
  {
	xtools.printXMLFromATerm(node);
	System.out.println();
	
  }


  private boolean _isMatch01(TNode node) 
  {
	if (_operatorAfter(_proc, node, _i1) && _operatorBefore(_proc, node, _i2))
	  return true; 
	else {
	  return false;
	}
  }


  private int _getTNodePosition(TNode root, TNode node, int startNumber) 
  {
	int position = -startNumber; 
// 	System.out.println("startnumber:"+startNumber);
// 	System.out.println(root.getName());
	
	position-=1; 
	if (root==node) {
	  return -position;
	}
	
	if (root.hasChildList()) {
	  TNodeList childList = root.getChildList(); 
	  
	  for (int i=0; i<childList.getLength();i++) {
		TNode oneChild = childList.getTNodeAt(i);
		if (oneChild.isAttributeNode()) { // node must be elementNode, not attributeNode
		  continue;
		}
		
		int subPosition = _getTNodePosition(oneChild, node, -position); 
		if (subPosition < 0) {   // "node" not found in this child branch
		  position = subPosition; 
		}
		else { // found
		  position = subPosition;  
		  break;
		}
	  }
	}

	return position;
  }

  // we must sur that node1 and node2 are two child of root
  private boolean _operatorBefore(TNode root, TNode node1, TNode node2)
  {
	if (_compareTNodePosition(root, node1, node2) > 0) {
	  return true;
	}
	else {
	  return false; 
	}
  }

  // we must sur that node1 and node2 are two child of root
  private boolean _operatorAfter(TNode root, TNode node1, TNode node2)
  {
	if (_compareTNodePosition(root, node1, node2) < 0) {
	  return true;
	}
	else {
	  return false; 
	}
  }
  

  private boolean _isTNodeContainTNode(TNode node1, TNode node2) 
  {
	if (_getTNodePosition(node1, node2,0) > 0) {
	  return true;
	}
	else 
	  return false;
  }
  
  private int _compareTNodePosition (TNode root, TNode node1, TNode node2) 
  {
	if (node1==node2) {
	  return 0;
	}

	if (root == node1) {
	  return 1; 
	}
	
	if (root==node2){
	  return -1;
	}

	if (root.hasChildList()) {
	  TNodeList childList = root.getChildList(); 
	  //	  int childElementCountCount = 0; 
	  
	  for (int i=0; i< childList.getLength() ;i++) {
		TNode oneChild = childList.getTNodeAt(i);
		if (oneChild.isAttributeNode()) {
		  continue;
		}
		int childResult = _compareTNodePosition(oneChild, node1, node2);
		if (childResult == 0) { // find again
		  // nothing to do
		}
		else if (childResult < 0) {  // node2 found 
		  return -1;  // stop  
		}
		else if (childResult > 0) { // node1 found instead of node2
		  return 1; //stop
		}
	  }
	}
	return 0;
  }

  protected TNodeList _collectData03(TNode subject, final String clauseName) { 
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
			
			// %match (TNode tnode) {
// 			  <incision></incision> -> {
// 				 vector.add(tnode);
// 				 return true;
// 			   }
// 			}
			

			if (tnode.isElementNode() && tnode.hasName() && (tnode.getName()==clauseName)) {
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
	  nodeList= nodeList.append(n);
	}			   		

	return nodeList;
  }



  protected TNodeList _collectData02(TNode subject) { 
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
			
			// %match (TNode tnode) {
// 			  <incision></incision> -> {
// 				 vector.add(tnode);
// 				 return true;
// 			   }
// 			}
			

			if (!tnode.isAttributeNode()) {
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
	  nodeList= nodeList.append(n);
	}			   		

	return nodeList;
  }

  protected TNode _getData01(TNode subject, final String clauseName) { 
	final Vector vector=new Vector(); 

	Collect1 collect = new Collect1() { 
		public boolean apply(ATerm t) 
		{ 
		  if(t instanceof TNode) { 
			TNode tnode=(TNode)t; 
		
			if (tnode.hasName() && (tnode.getName()==clauseName)) {
			  vector.add(t);
			  return false;
			}
		  } 
		  return true; 
		} // end apply 
	  }; // end new 
	traversal.genericCollect(subject, collect); 
	
	//	System.out.println(vector.size());
	
	//	TNodeList nodeList = getTNodeFactory().makeTNodeList();
	Enumeration e = vector.elements();
	while (e.hasMoreElements()) {
	  TNode n=(TNode)(e.nextElement());
	  return n; 
	  //	  xtools.printXMLFromATerm(n);
	  //System.out.println("__");
	  //nodeList= nodeList.append(n);
	}			   		
	return null;
	//	return nodeList;
  }


  protected TNodeList _collectData01(TNode subject) { 
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
			
			%match (TNode tnode) {
			  <section><section-title>#TEXT(title)</section-title></section> -> {
				 if (title=="Procedure") {
				   vector.add(tnode);
				   return true;
				 }
			   }
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


}
