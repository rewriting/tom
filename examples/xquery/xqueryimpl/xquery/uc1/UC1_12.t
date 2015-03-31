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


public class UC1_12 {
    
  %include {TNode.tom}
  private XmlTools xtools;
 private GenericTraversal traversal = new GenericTraversal();

  // declare document variables    
  // doc("http://bstore1.exemple.com/bib.xml")
  private TNode _XmlDocument01 = null;
    
		
  // declare xquery variables
  //	for $b in doc("http://bstore1.exemple.com/bib.xml")/bib/book
  private TNode _book1 = null;
  private TNode _book2 = null;
  private TNodeList _bookList = null;
  private TNodeList _aut1 = null;
  private TNodeList _aut2=null;
  private TNode _a=null;
  

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_12 uc = new UC1_12();
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

	  executeQuery(_XmlDocument01.getDocElem());
	

  }


 
  private void executeQuery(TNode documentNode) 
  {

	  _bookList = _collectData01(documentNode,"book");
	  System.out.println("<results>");
	  
	  // System.out.println(_bList);
	  _forLoop01(_bookList, _bookList);
	  System.out.println("</results>");
	  
  }


  private void _forLoop01(TNodeList list1, TNodeList list2) 
  {
	  
	  %match (TNodeList list1, TNodeList list2) {
	  (_*,book1@<book></book>,_*), 
		(_*,book2@<book></book>,_*) -> {
		   _book1=book1;
		   _book2=book2;
		   _aut1 = _createAuthorList01(_book1);
		   _aut2 = _createAuthorList01(_book2);

		   // if ((_aut1.getLength()==0) || (_aut2.getLength()==0)) {
// 			 // donothing
// 		   }
// 		   else {
			 _aut1 = _sort01(_aut1);
			 _aut2 = _sort01(_aut2);
			 
			 if (_isMatch01(list1, _book1, _book2)
				 && !_isMatch02(_book1, _book2)
				 && _isMatch03(_aut1, _aut2)) {
			   _createResult01(_book1, _book2);
			 }
			 //}
		   
		}
	  }

  }


  private boolean _isMatch01(TNodeList nodelist, TNode b1, TNode b2) 
  {
	return _isLessThan(nodelist, b1, b2);
  }


  private boolean _isLessThan(TNodeList nodelist, TNode node1, TNode node2)
  {
	// System.out.println("saokhongthaygi");
	
	int index1=nodelist.indexOf(node1,0); 
	int index2=nodelist.indexOf(node2,0);
	if (index1 < index2) {
	//   System.out.println(index1);
// 	  System.out.println(index2);
	  
	  return true; 
	}
	else {
	//   System.out.println("o nho hon ti nao");
	  
	  return false;
	}
  }


  private boolean _isMatch02(TNode b1, TNode b2) 
  {
	%match (TNode b1, TNode b2) {
	  <book><title>#TEXT(title1)</title></book>, 
		<book><title>#TEXT(title2)</title></book>-> {
		if (title1==title2) {
		  return true;
		}
		else {
		  return false;
		}
	  }
	}
	return false;
  }

  private boolean _isMatch03(TNodeList aut1, TNodeList aut2) 
  {
	return aut1.isEqual(aut2);
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

  // private TNode _sort01(TNode node) 
//   {
// 	%match (TNode node) {
// 	  <dashdash>(X1*,p1,X2*,p2,X3*)</dashdash> -> {
// 		 if(_compare01(p1,p2) > 0) {
// 		   return _sort01(`xml(<dashdash>X1* p2 X2* p1 X3*</dashdash>));
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


//   private TNode _sort02(TNode node) 
//   {
// 	%match (TNode node) {
// 	  <dashdash>(X1*,p1,X2*,p2,X3*)</dashdash> -> {
// 		 if(_compare02(p1,p2) > 0) {
// 		   return _sort02(`xml(<dashdash>X1* p2 X2* p1 X3*</dashdash>));
// 		 }	
// 	   }
	   
// 	   _ -> { return node; }
//     }
//   }


  private int _compare01(TNode t1, TNode t2) {
    %match(TNode t1, TNode t2) {
      <author><last>#TEXT(last1)</last></author>,
		 <last>#TEXT(last2)</last>
		 -> { return `last1.compareTo(`last2); }
    }
    return 0;
  }

  private int _compare02(TNode t1, TNode t2) {
    %match(TNode t1, TNode t2) {
      <author><first>#TEXT(first1)</first></author>,
		 <first>#TEXT(first2)</first>
		 -> { return `first1.compareTo(`first2); }
    }
    return 0;
  }



  private TNodeList _createAuthorList01(TNode book) 
  {
	TNodeList nodeList = getTNodeFactory().makeTNodeList(); 
	%match (TNode book) {
	  <book>author@<author></author></book> -> {
		 nodeList = nodeList.insert(author);
	   }
	}

	//return `xml(<dashdash>nodeList*</dashdash>);
	return nodeList; 
  }

  
  private boolean _exists(TNode node) 
	throws ObjectIncompatibleException
  {
	ObjectComparison comparor = new ObjectComparison();

	if (node.getChildList().getLength()>0)
	  return true;
	else 
	  return false;
  }



  private void _createResult01(TNode b1, TNode b2) 
  {
	System.out.println("<book-pair>");
	
	%match (TNode b1, TNode b2) {
	  <_>title1@<title></title></_>, <_>title2@<title></title></_>->{
		 xtools.printXMLFromATerm(title1);
		 System.out.println();
		 xtools.printXMLFromATerm(title2);
	   }
	}
	System.out.println("<book-pair>");	
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
