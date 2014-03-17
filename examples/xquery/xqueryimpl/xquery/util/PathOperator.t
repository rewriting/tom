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

public class PathOperator {
  %include {TNode.tom}

  protected XmlTools xtools = new XmlTools();

  protected Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }


  protected TNodeTester tester =null; 
  protected PathOperator nextOperator = null;

  public PathOperator(TNodeTester tester, PathOperator nextOperator)
  {
	this.tester =tester;
	this.nextOperator = nextOperator; 
  }

  public PathOperator(PathOperator nextOperator)
  {
	this.tester =new TNodeTester();
	this.nextOperator = nextOperator; 
  }

  public PathOperator()
  {
	this.tester = new TNodeTester();
	this.nextOperator = null; 
  }



  protected boolean doTest(Object node) 
	throws XQueryGeneralException
  {
	return tester.doTest(node);
  }


  protected Sequence runNext(TNode node)
  {
	if (this.nextOperator ==null) {
	  Sequence seq = new Sequence();
	  seq.add(node);
	  return seq;
	}
	else {
	  Sequence seq = new Sequence();
	  if (node.hasChildList()) {
		TNodeList childList = ((TNode)node).getChildList(); 
		
		for (int i=0; i<childList.getLength(); i++) {
		  Sequence s = this.nextOperator.run(childList.getTNodeAt(i));
		  seq.addAll(s);
		}
	  }

	  if (node.hasAttrList()) {
		TNodeList attrList = ((TNode)node).getAttrList(); 
		
		for (int i=0; i<attrList.getLength(); i++) {
		  Sequence s = this.nextOperator.run(attrList.getTNodeAt(i));
		  seq.addAll(s);
		}
	  }

	  return seq;
	}
  }
  

  public Sequence run(TNode subject) 
  {
	final Sequence s=new Sequence(); 

	try {
	  if (doTest(subject)) {
		s.addAll(runNext(subject));
	  } 
	}
	
	catch (XQueryGeneralException e) {
	  System.out.println("ERROR: xqueryGeneral exception");
	  return null; 
	}
	
	return s;
  }

}
