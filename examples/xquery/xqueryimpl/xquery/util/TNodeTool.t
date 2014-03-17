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

import java.util.*;
import jtom.runtime.*;

public class TNodeTool {

  %include {TNode.tom}

  private XmlTools xtools = new XmlTools();

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  
  public Sequence distinctValues(Sequence seq, Comparator comparator, TNodeTester tester, TNodeQualifier qualifier)
	throws XQueryGeneralException
  {

	SequenceTool sequencetool = new SequenceTool (); 
	Sequence output = new Sequence(); 
	Enumeration enum = seq.elements(); 
	
	while (enum.hasMoreElements()) {
	  Object obj = (enum.nextElement());

	  if (tester.doTest(obj)) {
		if (!sequencetool.contain(output, obj, comparator)) {
		  output.add(obj);
		}
	  }
	}

	Sequence result =  new Sequence(); 
	enum = output.elements();
	while (enum.hasMoreElements()) {
	  TNode obj = (TNode)(enum.nextElement());
	  result.addAll(qualifier.qualify(obj));
	}
	return result;
  }


  public void printResult(QueryRecordSet queryRecordSet, RecordPrinter printer) 
	throws XQueryGeneralException
  {
	Enumeration enum = queryRecordSet.elements(); 
	while (enum.hasMoreElements()) {
	  QueryRecord record = (QueryRecord)(enum.nextElement());
	  printer.print(record);
	  
	}
  }


  public boolean endWith(TNode node, String endwith) 
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


  public boolean contains(TNode node,  String str) 
  {	
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
		if (this.contains(onenode, str)) 
		  return true; 
	  }
	}
	else {
	  return false;
	}

	return false;
  }


}
