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


public class UC1_10 {
    
  %include {TNode.tom}
  private XmlTools xtools;
  private GenericTraversal traversal = new GenericTraversal();
  
  // declare document variables    
  // doc("http://bstore1.exemple.com/bib.xml")
  private TNode _xmlDocument01 = null;
  
		
  // declare xquery variables
  //	for $b in doc("http://bstore1.exemple.com/bib.xml")/bib/book
  private TNode _t = null;
  private TNode _p = null;
  private TNodeList _pList =null;
  private TNodeList _tList = null;
  private TNode _doc=null;

    

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_10 uc = new UC1_10();
	String filename;

	if (args.length == 0) {
	  filename="xquery/data/prices.xml";
	}
	else {
	  filename=args[0];
	}

	uc.run(filename);
  }


  private void run(String filename) 
  {
	xtools = new XmlTools();

	_xmlDocument01 = (TNode)xtools.convertXMLToATerm(filename); 


	  _doc=_xmlDocument01.getDocElem();
	  executeQuery(_doc);
	

  }



  private void executeQuery(TNode documentNode) 
  {
	System.out.println("<results>");
	  

	  // System.out.println(_bList);
	  _tList=_collectData01(_doc);
	  _tList=_distinctValues01(_tList);

	  //  System.out.println(_tList);
	  

	  _forLoop01(_tList);
	  System.out.println("</results>");
	  
  }


  private TNodeList _distinctValues01(TNodeList nodelist)
  {
	TNodeList nodeList = getTNodeFactory().makeTNodeList();
	%match (TNodeList nodelist) {
	  (_*, aTitle@<title></title> , _*) -> {
		 if (!_isContain(nodeList, aTitle)) {
		   nodeList=nodeList.insert(aTitle);
		 }
	   }
	}
	
	return nodeList; 
	//return `xml(<dashdash>  nodeList* </dashdash>);
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


  private void _forLoop01(TNodeList nodelist) 

  {
	%match (TNodeList nodelist) {
	  (_*, aTitle@<title></title>, _*) -> {
		 _t=aTitle;
		 _pList=_collectData02(_doc);
		 
		 //		 System.out.println(_pList);
		 
		 _createResult01(_t, _pList);
	   }
	}
  }

  
  private void _createResult01(TNode t, TNodeList pList) 
  {
	%match (TNode t) {
	  <title>#TEXT(title)</title> -> {
		 System.out.println("<minprice title=\"" + title + "\"");
	   }
	}
	System.out.print("<price>");
	TNode minprice=_min01(pList);
	%match (TNode minprice) {
	  <price>#TEXT(price)</price> -> {
		 System.out.print(price);
		 
	   }
	}
	System.out.println("</price>");
	System.out.println("</minprice>");
	
  }


  private TNode _min01(TNodeList nodelist) 
  {
	TNode minNode=null;
	float minPrice = 100000000;
	%match (TNodeList nodelist) {
	  (_*, oneNode@<price>#TEXT(price)</price>, _*) -> 
	   {
		 float f = Float.parseFloat(price);
		 if (f<minPrice) {
		   minPrice=f;
		   minNode=oneNode; 
		 }
	   }
	}
	return minNode;
	//	return node.getChildList().getTNodeAt(0);
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
						
			%match (TNode t) {
			  <book>tChild@<title></title></book> -> {
				 vector.add(tChild);
				 return true;
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
	  nodeList= nodeList.insert(n);
	}			   		
	return nodeList; 
	//	return `xml(<dashdash> nodeList* </dashdash>);
  }


  protected TNodeList _collectData02(TNode subject) 
  {
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

			if (_isMatch01((TNode)t)) {
			  %match (TNode t) {
				<book>price@<price></price></book> -> {
				   vector.add(price);
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
	  nodeList= nodeList.insert(n);
	}			   		
	return nodeList; 
	//	return `xml(<dashdash> nodeList* </dashdash>);
  }
  
  
  private boolean _isMatch01(TNode objectNode) 
	
  {
	  
	  %match (TNode objectNode) {
		<book>title@<title></title></book> -> {
		   if (title.isEqual(_t)) {
			 return true;
		   }
		 }
	  }
	  return false; 
  }
}
