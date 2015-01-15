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



public class UC1_4 {

  %include {TNode.tom}

  private XmlTools xtools=new XmlTools();
  private GenericTraversal traversal = new GenericTraversal();

  private TNode  _a ;
  private TNode  _ba ;
  private TNode  _b ;

  private TNode _last=null;
  private TNode _first=null;
  private TNodeList  _aList ;


  private TNodeList _lastList=null;
  private TNodeList _firstList=null;
  private TNode _xmlDocument01=null;		

		
	
  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }


  public static void main(String args[]) 
  {
	UC1_4 uc = new UC1_4 ();
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

	try {
	  executeQuery(_xmlDocument01.getDocElem());
	}
	catch (ObjectIncompatibleException e) {
	  System.out.println("error message here");
	}
  }

  
  private void executeQuery(TNode documentNode) 
	throws ObjectIncompatibleException 
  {
	try {
	  System.out.println("<results>");
	  _aList=_collectData(documentNode,"author");
	  
 	  _lastList=_distinctValues01(_aList);
	  
	 
	  _lastList=_sort01(_lastList);
	  //System.out.println(_lastList);
	  
	  _forLoop01(_lastList);

	  System.out.println("</results>");
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


//   private TNodeList _sort01(TNodeList node) 
//   {
// 	%match (TNodeList node) {
// 	  (X1*,p1,X2*,p2,X3*) -> {
// 		 if(_compare01(p1,p2) > 0) {
// 		   return _sort01(`concTNode(X1*,p2,X2*,p1,X3*));
// 		 }	
// 	   }
	   
// 	   _ -> { return node; }
//     }
//   }


 private TNodeList _sort02(TNodeList nodelist)
  {

	class Comparator02 implements Comparator {
	  public int compare(Object o1, Object o2) {
		TNode node1 = (TNode)o1;
		TNode node2 =(TNode)o2;
		return _compare02(node1, node2);
	  }

	  
	}

	// quick sort
	TNode tnodearray[] = new TNode [nodelist.getLength()];
	for (int i=0; i<nodelist.getLength(); i++){
	  tnodearray[i]=nodelist.getTNodeAt(i);
	}

	Arrays.sort(tnodearray, new Comparator02());
	
	
	TNodeList result = getTNodeFactory().makeTNodeList();
	for (int i=nodelist.getLength()-1;i>=0; i--){
	  result = result.insert(tnodearray[i]);
	}

	return result;
  }


//   private TNodeList _sort02(TNodeList node) 
//   {
// 	%match (TNodeList node) {
// 	  (X1*,p1,X2*,p2,X3*) -> {
// 		 if(_compare02(p1,p2) > 0) {
// 		   return _sort02(`concTNode(X1*, p2, X2*, p1, X3*));
// 		 }	
// 	   }
	   
// 	   _ -> { return node; }
//     }
//   }


  private int _compare01(TNode t1, TNode t2) {
    %match(TNode t1, TNode t2) {
      <last>#TEXT(last1)</last>,
		 <last>#TEXT(last2)</last>
		 -> { return `last1.compareTo(`last2); }
    }
    return 0;
  }

  private int _compare02(TNode t1, TNode t2) {
    %match(TNode t1, TNode t2) {
      <first>#TEXT(first1)</first>,
		 <first>#TEXT(first2)</first>
		 -> { return `first1.compareTo(`first2); }
    }
    return 0;
  }

  private TNodeList _distinctValues02(TNodeList node, TNode lastname)
  {
	TNodeList nodeList = getTNodeFactory().makeTNodeList();

	%match (TNodeList node) {
	  (_*,<author>alast@<last></last>afirst@<first></first></author>,_*) -> {
		 
		 if (lastname.isEqual(alast) && (!_isContain(nodeList, afirst))) {
		   nodeList=nodeList.insert(afirst);
		 }
	   }
	}

	return nodeList;
  }


  private TNodeList _distinctValues01(TNodeList node)
  {
	TNodeList nodeList = getTNodeFactory().makeTNodeList();
	%match (TNodeList node) {
	  (_*,<author>anItem@<last></last></author>, _*) -> {
		 if (!_isContain(nodeList, anItem)) {
		   nodeList=nodeList.insert(anItem);
		 }
	   }
	}

	return nodeList;
  }

  private boolean _isContain(TNodeList list, TNode item) 
  {
	%match (TNodeList list) {
	  (_*, node, _*) -> {
		if (node.isEqual(item)) {
		  return true; 
		}
	  }

	}
	return false;
  }

  private void _forLoop01(TNodeList node) 
	throws ObjectIncompatibleException 
  {
	try {
	  %match (TNodeList node) {
		(_*,lastName@<last></last>,_*) -> {
		   _last = lastName;

		   _firstList = _distinctValues02(_aList, _last);
		   _forLoop02(_firstList);
		 }	   
	  }
	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }


  private void _forLoop02(TNodeList node) 
	throws ObjectIncompatibleException 
  {
	try {
	  %match (TNodeList node) {
		(_*,firstname@<first></first>,_*) -> {
		   _first = firstname; 
		   if (isMatch(null)) {
			 _createResult01(_last,_first);
		   }
		 }	   
	  }
	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }


  private boolean isMatch(TNode objectNode) 
	throws ObjectIncompatibleException 
  {
	ObjectComparison comparor = new ObjectComparison(); 
	// nothing to do;
	return true;
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
	//	return `xml(<dashdash> nodeList* </dashdash>);
  }
		

  private void _createResult01(TNode last,TNode first )
  {
	System.out.println("<result>");
	%match (TNode last, TNode first) {
	  <last>#TEXT(lastname)</last> , <first>#TEXT(firstname)</first> -> {
		 System.out.println("<author>");
		 System.out.println("<last>" + lastname  + "</last>");
		 System.out.println("<first>" + firstname + "</first>");
		 System.out.println("</author>");
		 _forLoop03(_xmlDocument01.getDocElem());
	   }
	}
	System.out.println("</result>");
	
  }
   

  private void _forLoop03(TNode node) 
  {
	%match (TNode node) {
	  <bib>aBook@<book></book></bib> -> {
		 _b= aBook;
		 //		 System.out.println(_b);
		 
		 _forLoop04(_b);
	   }
	}
  }

  private void _forLoop04(TNode node) 
  {
	%match (TNode node) {
	  <book>anAuthor@<author></author></book> -> {
		 _ba=anAuthor; 
		 //		 System.out.println(_ba);
		 
		 if (_isMatch01(_ba,_last) && _isMatch02(_ba,_first)) {
		   //		   System.out.println(_ba);
		   _createResult02(_b);
		 }
	   }
	}
  }

  private boolean _isMatch01(TNode author, TNode last) 
  {
	%match (TNode author) {
	  <author>lastname@<last></last></author> -> {
		 if (lastname.isEqual(last)) {
		   return true; 
		 }
	   }
	}
	return false; 
  }

  private boolean _isMatch02(TNode author, TNode first) 
  {
	%match (TNode author) {
	  <author>firstname@<first></first></author> -> {
		 if (firstname.isEqual(first)) {
		   return true; 
		 }
	   }
	}
	return false; 
  }

  private void _createResult02(TNode node) {
	%match (TNode node) {
	  <book><title>#TEXT(title)</title></book> -> {
		 System.out.println("<title>" + title + "</title>");
		 
	   }
	}
  }
}

