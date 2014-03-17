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
package xquery.uc1;

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;
import jtom.runtime.*;
import java.io.*;

import java.util.*; 

import xquery.util.*;



public class UC1_6 {

  %include {TNode.tom}

  private XmlTools xtools=new XmlTools();
  private GenericTraversal traversal = new GenericTraversal();

  private TNode _b=null;

  private TNode _a=null;
  private TNodeList _bList=null;

  private TNode _xmlDocument01 = null;
		
	
  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  //TNodeList _tt=new TNodeList(getTNodeFactory());

  public static void main(String args[]) 
  {
	UC1_6 uc = new UC1_6 ();
	String filename01;

	if (args.length == 0) {
	  filename01="xquery/data/bib.xml"; 
	}
	else {
	  filename01=args[0];
	}

	uc.run(filename01);
  }

  private void run(String filename01) 
  {
				

	_xmlDocument01 = (TNode)xtools.convertXMLToATerm(filename01); 
				
	_bList=_collectData(_xmlDocument01.getDocElem(),"book");
	
	try {
	  executeQuery(_bList);
	}
	catch (ObjectIncompatibleException e) {
	  System.out.println("error message here");
	}
  }

  
  private void executeQuery(TNodeList nodelist) 
	throws ObjectIncompatibleException 
  {
	try {
	  System.out.println("<bib>");

	  _forLoop01( nodelist);

	  System.out.println("</bib>");
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
		(_*, aBook@<book></book>, _*) -> {
		   _b=aBook;
		   if (_isMatch01(_b)) {
			 _createResult01(_b);
		   }
		 }
	  }
	  
	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }


  private void _forLoop02(TNode node) 
  {
	  int counter = 0; 
	  %match (TNode node) {
		<_>author@<author></author></_> -> {
		   counter++;
		   _a=author;
		   if (counter<=2) {
			 _createResult02(_a);
		   }
		 }
	  }
	  
  }


  private int _count01(TNode node) 
  {
	int counter=0;
	%match (TNode node) {
	  <_>author@<author></author></_> -> {
		 counter++; 
	   }
	}
	return counter;
  }

  private boolean _isMatch01(TNode node) 
	throws ObjectIncompatibleException 
  {
	ObjectComparison comparor = new ObjectComparison(); 
	
	if (_count01(node) > 0) 
	  return true;
	else 
	  return false;
  }


  private boolean _isMatch02(TNode node) 
	throws ObjectIncompatibleException 
  {
	ObjectComparison comparor = new ObjectComparison(); 
	
	if (_count01(node) > 2) 
	  return true;
	else 
	  return false;
  }


  private void _createResult02(TNode node) 
  {
	xtools.printXMLFromATerm(node);
  }


  private void _createResult01(TNode b)
	throws ObjectIncompatibleException 
  {
	
	System.out.println("<book>");
	%match (TNode b) {
	  <_><title>#TEXT(title)</title></_> -> {
		 System.out.println("<title>" + title + "</title>");
		 
		 _forLoop02(_b);

		 if (_isMatch02(_b)) {
		   _createResult03(_b);
		 }
	   }
	}

  
	
	System.out.println("</book>");
  }

  private void _createResult03(TNode node) 
  {
	System.out.println("<et-al/>");
	
  }
   
  protected TNodeList _collectData(TNode subject,final String clauseName) { 
	final Vector vector=new Vector(); 

	Collect1 collect = new Collect1() { 
		public boolean apply(ATerm t) 
		{ 
		  if(t instanceof TNode) { 
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
  

//   protected TNodeList _collectData(TNode subject,final String clauseName) { 
// 	final Vector vector=new Vector(); 

// 	Collect1 collect = new Collect1() { 
// 		public boolean apply(ATerm t) 
// 		{ 
// 		  if(t instanceof TNode) { 
// 			TNode tnode=(TNode)t; 
			
			
// 			if (tnode.hasName() && (tnode.getName()==clauseName)) {
// 			  vector.add(t);
// 			  return true;
// 			}
// 		  } 
// 		  return true; 
// 		} // end apply 
// 	  }; // end new 
// 	traversal.genericCollect(subject, collect); 
	
// 	//	System.out.println(vector.size());
	
// 	TNodeList nodeList = getTNodeFactory().makeTNodeList();
// 	Enumeration e = vector.elements();
// 	while (e.hasMoreElements()) {
// 	  TNode n=(TNode)(e.nextElement());
// 	  //	  xtools.printXMLFromATerm(n);
// 	  //System.out.println("__");
// 	  nodeList= nodeList.append(n);
// 	}			   		

// 	return nodeList;
// 	//	return `xml(<dashdash> nodeList* </dashdash>);
//   }
}
