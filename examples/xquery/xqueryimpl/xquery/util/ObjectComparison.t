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
package xquery.util; 

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

public class ObjectComparison {
  
  %include {TNode.tom}

  private XmlTools xtools;

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }
		
  public boolean isGreater(Object object, Object objectToCompare) 
	throws ObjectIncompatibleException 
  {
	try {
	  int val = compareObject(object, objectToCompare); 
	  if (val >0) { 
		return true; 
	  }
	  else {
		return false; 
	  }
	}
	catch (ObjectIncompatibleException e) {
	  throw e;
	}
  }

  public boolean isEqual(Object object, Object objectToCompare) 
	throws ObjectIncompatibleException 
  {
	try {
	  int val = compareObject(object, objectToCompare); 
	  if (val == 0) { 
		return true; 
	  }
	  else {
		return false; 
	  }
	}
	catch (ObjectIncompatibleException e) {
	  throw e;
	}	
  }

  public boolean isSmaller(Object object, Object objectToCompare) 
	throws ObjectIncompatibleException 
  {
	try {
	  int val = compareObject(object, objectToCompare); 
	  if (val < 0) { 
		return true; 
	  }
	  else {
		return false; 
	  }
	}
	catch (ObjectIncompatibleException e) {
	  throw e;
	}
  }


  // return >0 if greater
  //        0 if equal
  //       <0 if smaller
  private int compareObject(Object object, Object objectToCompare) 
	throws ObjectIncompatibleException 
  {
	// compare String and String
	if ((object instanceof java.lang.String)
		&& (objectToCompare instanceof java.lang.String)) {
	  return compareStringAndString((java.lang.String)object, (java.lang.String)objectToCompare); 
	}
	// compare String and "int"
	else if ((object instanceof java.lang.String)
			 && (objectToCompare instanceof Integer)) {
	  return compareStringAndInt((java.lang.String)object, ((java.lang.Integer)objectToCompare).intValue()); 
	}
	// compare two TNode
	else if ((object instanceof TNode )
			 && (objectToCompare instanceof TNode)) {
	  return compareTNodeAndTNode((jtom.adt.tnode.types.TNode)object, (jtom.adt.tnode.types.TNode)objectToCompare);
	}

	else if ((object instanceof TNode )
			 && (objectToCompare instanceof String)) {
	  return compareTNodeAndString((jtom.adt.tnode.types.TNode)object, (java.lang.String)objectToCompare);
	}
				
	else if ((object instanceof TNode )
			 && (objectToCompare instanceof Integer)) {
	  return compareTNodeAndInt((jtom.adt.tnode.types.TNode)object, ((java.lang.Integer)objectToCompare).intValue());
	}
	else {
	  throw new ObjectIncompatibleException();
	}
  }


  private int compareStringAndInt(String string, int valueToCompare) 
  {
	int value = Integer.parseInt(string); 
	return value - valueToCompare;
  }


  private int compareStringAndString(String string, String stringToCompare) 
  {
	return string.compareTo(stringToCompare);
  }

  private int compareTNodeAndTNode(TNode object, TNode objectToCompare) 
  {
	return 1;  // now always greater
  }

  private int compareTNodeAndString(TNode object, String str) 
	throws ObjectIncompatibleException
  {
	//		System.out.println(object.getName());
	//	System.out.println(object.getValue());
	%match (TNode object) {
	  <a>#TEXT(text)</a> -> {
		 return compareStringAndString(text,str);
	   }
	   _ -> {
		 //					return 0;
		 throw new ObjectIncompatibleException();
	   }
	}
  }

  private int compareTNodeAndInt(TNode object, int value) 
	throws ObjectIncompatibleException
  {
				
	%match (TNode object) {
	  <a>#TEXT(text)</a> -> {
		 return compareStringAndInt(text,value);
	   }
	   _ -> {
		 throw new ObjectIncompatibleException();
	   }
	}

  }


  
}
