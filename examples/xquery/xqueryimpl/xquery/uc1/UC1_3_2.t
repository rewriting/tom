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


public class UC1_3_2 {
    
  %include {TNode.tom}
  private XmlTools xtools;
  private GenericTraversal traversal = new GenericTraversal();
  TNodeTool tnodetool=new TNodeTool(); 


  private TNode _xmlDocument01 = null;
		
  private TNode _b = null;

  QueryRecordSet _queryRecordSet01 = new QueryRecordSet (); 

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_3_2 uc = new UC1_3_2();
	String filename01;
	String filename02;

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
	xtools = new XmlTools();

	_xmlDocument01 = (TNode)xtools.convertXMLToATerm(filename01); 
	
	try {
	  executeQuery(_xmlDocument01);
	}
	catch (XQueryGeneralException e) {
	  System.out.println("ERROR: xquery exception error");	  
	}
  }


 
  private void executeQuery(TNode documentNode01) 
	throws XQueryGeneralException
  {

	Sequence bSequence = _collectData01(documentNode01.getDocElem());
	

	System.out.println("<results>");
  
 	_forLetWhere01(bSequence, _queryRecordSet01);
	//	System.out.println(bSequence);
	
	//	_order01(_queryRecordSet01);
	_return01(_queryRecordSet01);

	System.out.println("</results>");

  }


  private void _return01(QueryRecordSet queryRecordSet)
	throws XQueryGeneralException
  {
	class _RecordPrinter01_return01 extends RecordPrinter {
	  public void  print(QueryRecord record)
		throws XQueryGeneralException
	  {
		System.out.println("<result>");
		
		TNode b=(TNode)(record.getField(0)); 
		
		%match (TNode b) {
		  <_>title@<title></title></_> -> {
			 xtools.printXMLFromATerm(title);
			 System.out.println();
		   }
		}

		%match (TNode b) {
		  <_>author@<author></author></_> -> {
			 xtools.printXMLFromATerm(author);
			 System.out.println();
		   }
		}
		System.out.println("</result>");
	  }
	}
  

	tnodetool.printResult(queryRecordSet, new _RecordPrinter01_return01()); 
  }



  private void _forLetWhere01(Sequence sequence, QueryRecordSet queryRecordSet) 
	throws XQueryGeneralException
  {
	
	TNodeTester tester = new TNodeTester();
	
	Enumeration enum = sequence.elements(); 
	
	while (enum.hasMoreElements()) {
	  TNode itemtuple = (TNode)(enum.nextElement());
	  
	  _b = itemtuple; 
	  if (tester.doTest(_b)) {
		QueryRecord record = new QueryRecord(1); 
		record.setField(_b, 0);
		queryRecordSet.add(record);
	  }
	}

  }
  

  protected Sequence _collectData01(TNode subject) { 
	class _TNodeTester_collectData01 extends TNodeTester{
	  public boolean doTest(Object obj) {
		%match (TNode obj) {
		  <bib><book></book></bib> -> {
			 return true;
			 
		 }
		}
		
		return false;
	  }
	}
	
	class TNodeQualifier_collectData01 extends TNodeQualifier {
	  public Sequence qualify(Object node) 
	  {
		Sequence seq=new Sequence(); 
		%match (TNode node) {
		  <bib>book@<book></book></bib> -> {
			 seq.add(book);
		   }
		}
		return seq;
	  }
	}
	
	return tnodetool.collectData2(subject, new _TNodeTester_collectData01(), new TNodeQualifier_collectData01());	
  }


}
