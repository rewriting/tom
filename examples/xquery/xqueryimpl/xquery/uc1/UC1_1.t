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

import java.util.*; 

import xquery.util.*;
import java.io.*; 


public class UC1_1 {
    
  %include {TNode.tom}
  private XmlTools xtools;

  // declare document variables    
  // doc("http://bstore1.exemple.com/bib.xml")
  private TNode _XmlDocument01 = null;
    
		
  // declare xquery variables
  //	for $b in doc("http://bstore1.exemple.com/bib.xml")/bib/book
  private TNode _b = null;
    

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_1 uc = new UC1_1();
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
	  System.out.println("<bib>");
	  

	  _forLoop01(documentNode);


	  System.out.println("</bib>");
	  

	}
	catch (ObjectIncompatibleException e) {
	  throw e; 
	}
  }


  private void _forLoop01(TNode node) 
	throws ObjectIncompatibleException 
  {
	try {


	  %match (TNode node) {
		<bib>aChildNode@<book></book></bib> -> {
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
	  // mustbe like this
	  // 				%match (TNode objectNode) {
	  // 								<book>thePublisher@<publisher></publisher></book> -> {
	  // 										 if (comparor.isEqual(thePublisher, "Addison-Wesley")) {
	  // 												 return true; 
	  // 										 }
	  // 								 }
	  // 						}
						
	  return false; 
	}
	catch (ObjectIncompatibleException e) {
	  throw e;
	}
  }



  private void _createResult01(TNode objectNode) 
  {
	
	%match (TNode objectNode) {
	  <book year=theYear>aTitle@<title></title></book> -> {
		 System.out.println("<book year=\"" + theYear + "\">");
		 xtools.printXMLFromATerm(aTitle);
		 System.out.println("</book>");
		 
// 		 result = result + XMLText.createCascadeXML("<book year=\"" + theYear + "\">", 
// 													getXMLFromTNode(aTitle),
// 													,
// 													1);
	   }
	}

  }
  
//   private String getXMLFromTNode(TNode node)
//   {
// 	//First, redirect System.out so we can retrieve it as a string.
// 	PrintStream defaultOut = System.out;
// 	ByteArrayOutputStream outArray = new ByteArrayOutputStream();
	
// 	System.setOut(new PrintStream(outArray));
 
// 	xtools.printXMLFromATerm(node);
// 	String result = outArray.toString();

// 	System.setOut(defaultOut);
// 	return result; 
//   } 	
	
}
