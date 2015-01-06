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


public class UC1_7 {
    
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
    

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_7 uc = new UC1_7();
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
	  System.out.println("<bib>");;

	  _bList = _collectData(documentNode,"book");
	  _bList = _sort01(_bList);

	  _forLoop01(_bList);

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
		(_*, aChildNode@<book></book>, _*) -> {
		   // assign value to class-scope variable _b																	
		   _b = aChildNode;

		   // check if b is matched all conditions
		   // where $publisher = "Addison-Wesley" and $b/@year > 1991		
		   if (_isMatched01(_b) && _isMatched02(_b)) {
			 _createResult01(_b);
		   }
		 }	   
	  }

	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }
  

  private TNodeList _sort01(TNodeList nodelist)
  {

	class Comparator01 implements Comparator {
	  public int compare(Object o1, Object o2) {
		TNode node1 = (TNode)o1;
		TNode node2 =(TNode)o2;
		return _compare01(node1, node2);
	  }

	  
	}

	// quick sort
	TNode tnodearray[] = new TNode [nodelist.getLength()];
	for (int i=0; i<nodelist.getLength(); i++){
	  tnodearray[i]=nodelist.getTNodeAt(i);
	}

	Arrays.sort(tnodearray, new Comparator01());
	
	
	TNodeList result = getTNodeFactory().makeTNodeList();
	for (int i=nodelist.getLength()-1;i>=0; i--){
	  result = result.insert(tnodearray[i]);
	}

	return result;
  }


//   private TNodeList _sort01(TNodeList nodelist) 
//   {
// 	%match (TNodeList nodelist) {
// 	  (X1*,p1@<book></book>,X2*,p2@<book></book>,X3*) -> {
// 		 if(_compare01(p1,p2) > 0) {
// 		   return _sort01(`concTNode(X1*, p2, X2*, p1, X3*));
// 		 }	
// 	   }
	   
// 	   _ -> { return nodelist; }
//     }
//   }



  private int _compare01(TNode t1, TNode t2) {
    %match(TNode t1, TNode t2) {
      <book><title>#TEXT(title1)</title></book>,
		 <book><title>#TEXT(title2)</title></book>
		 -> { return `title1.compareTo(`title2); }
    }
    return 0;
  }





  private boolean _isMatched01(TNode objectNode) 
	throws ObjectIncompatibleException 
  {
	try {
	  ObjectComparison comparor = new ObjectComparison();

	  %match (TNode objectNode) {
		<book year=theYear></book> -> {
		   if (comparor.isGreater(theYear, new Integer(1991))) {
			 return true; 
		   }
		 }
	  }
	  return false; 
	}
	catch (ObjectIncompatibleException e) {
	  throw e;
	}
  }


  private boolean _isMatched02(TNode objectNode) 
	throws ObjectIncompatibleException 
  {
	try {
	  ObjectComparison comparor = new ObjectComparison();

	  %match (TNode objectNode) {
		<book><publisher>#TEXT(thePublisher)</publisher></book> -> {
		   if (comparor.isEqual(thePublisher, "Addison-Wesley")) {
			 return true; 
		   }
		 }
	  }
	  return false; 
	}
	catch (ObjectIncompatibleException e) {
	  throw e;
	}
  }



  private void _createResult01(TNode objectNode) 
  {
	String result = ""; 
	%match (TNode objectNode) {
	  <book year=theYear>aTitle@<title></title></book> -> {
		 
		 System.out.println("<book year=\"" + theYear + "\">");
		 xtools.printXMLFromATerm(aTitle);
		 System.out.println("</book>");
		 
	   }
	} 
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
}
