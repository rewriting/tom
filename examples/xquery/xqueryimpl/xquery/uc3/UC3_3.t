/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
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


public class UC3_3 {
    
  %include {TNode.tom}
  private XmlTools xtools;
 private GenericTraversal traversal = new GenericTraversal();

  // declare document variables    
  // doc("http://bstore1.exemple.com/bib.xml")
  private TNode _XmlDocument01 = null;
    
		
  // declare xquery variables
  //	for $b in doc("http://bstore1.exemple.com/bib.xml")/bib/book
  private TNode _i2 = null;
  private TNodeList _aList =null; 
  private TNode _a =null;

  

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC3_3 uc = new UC3_3();
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

	//	  _bookList = _collectData01(documentNode,"book");
	//System.out.println("<results>");
	TNodeList i2List = _collectData01(documentNode, "action");
	_i2= i2List.getTNodeAt(1); // position 2
	_aList=_collectData01(documentNode, "action"); 
 	_forLoop01(_aList);

	System.out.println(documentNode);
	

	_i2=_getData01(documentNode, "section-title");
	System.out.println(_i2.getName());
	
	System.out.println(_getTNodePosition(documentNode, _i2,0));


	_i2=_getData01(documentNode, "instrument");
	System.out.println(_i2.getName());
	System.out.println(_getTNodePosition(documentNode, _i2,0));

  }


  private void _forLoop01(TNodeList actionlist) 
  {
	int actionCount = 0; 
	%match (TNodeList actionlist) {
	  (_*, action@<action></action>, _*) -> {
		_a = action; 
		if (_isMatch01(_XmlDocument01.getDocElem(),_a, _i2)) {
		  actionCount++; 
		  if (actionCount>2)
			return; 
		  else 
			_createResult01(_a);
		}
	  }
	}
  }

  private void _createResult01(TNode action) 
  {
	TNodeList instrumentList = _collectData01(action, "instrument");
	%match (TNodeList instrumentList) {
	  (_*, instrument@<instrument></instrument>, _*) -> {
		 xtools.printXMLFromATerm(instrument);
		 System.out.println();
		 
	   }
	}
  }

  private boolean _isMatch01(TNode root, TNode node1, TNode node2) 
  {
	int pos1 =  _getTNodePosition(root, node1, 0);
	int pos2 =  _getTNodePosition(root, node2, 0);

	if (pos1 > pos2) 
	  return true;
	else
	  return false;
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




  protected TNodeList _collectData01(TNode subject, final String clauseName) { 
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



}
