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

import java.util.*; 
import xquery.util.*;
import java.io.*; 


public class UC1_8_2 {
    
  %include {TNode.tom}
  private XmlTools xtools=new XmlTools();
  private GenericTraversal traversal = new GenericTraversal();
  TNodeTool tnodetool=new TNodeTool(); 
  SequenceTool sequencetool = new SequenceTool();

  private TNode _xmlDocument01 = null;

		
  private TNode _b = null;
  private Sequence _e=new Sequence();

  QueryRecordSet _queryRecordSet01 = new QueryRecordSet (); 

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_8_2 uc = new UC1_8_2();
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
	//	xtools = new XmlTools();

	_xmlDocument01 = (TNode)xtools.convertXMLToATerm(filename01); 
	
	try {
	  executeQuery();
	}
	catch (XQueryGeneralException e) {
	  System.out.println("ERROR: xquery exception error");
	  
	}
  }


 
  private void executeQuery() 
	throws XQueryGeneralException
  {

	Sequence bSequence = _collectData01(_xmlDocument01.getDocElem());
	_FLWOR01(bSequence);
  }


  private void _FLWOR01(Sequence seq) 
			throws XQueryGeneralException
  {
	_forLetWhere01(seq, _queryRecordSet01);
	_return01(_queryRecordSet01);
  }


  private void _return01(QueryRecordSet queryRecordSet)
	throws XQueryGeneralException
  {
	class _RecordPrinter01_return01 extends RecordPrinter {
	  public void  print(QueryRecord record)
		throws XQueryGeneralException
	  {
		System.out.println("<book>");
		
		TNode b=(TNode)(record.getField(0)); 
		Sequence e=(Sequence)(record.getField(1));

		%match (TNode b) {
		  <book>title@<title></title></book> -> {
			 xtools.printXMLFromATerm(title);
			 System.out.println();
			 
		   }
		}

		System.out.println("</book>");
	  }
	}
  

	tnodetool.printResult(queryRecordSet, new _RecordPrinter01_return01()); 
  }



  private void _forLetWhere01(Sequence sequence, QueryRecordSet queryRecordSet) 
			throws XQueryGeneralException
  {
	class _TNodeTester extends TNodeTester {
	  public boolean doTest(Object obj) 
	  {
		return sequencetool.existes((Sequence)obj);
	  }
	}

	_TNodeTester tester = new _TNodeTester();
	Enumeration enum = sequence.elements(); 

	while (enum.hasMoreElements()) {
	  _b = (TNode)(enum.nextElement());
	  _e = _collectData02(_b);

	  System.out.println(_e);
	  
	  if (tester.doTest(_e)) {
		QueryRecord record = new QueryRecord(2); 
		record.setField(_b, 0);
		record.setField(_e, 1);

		queryRecordSet.add(record);
	  }
	}

  }
  

  protected Sequence _collectData02(TNode subject) { 
	class _TNodeTester_collectData02 extends TNodeTester {
	  public boolean doTest(Object obj)
	  {
		%match (TNode obj) {
		  <_>innernode@<_></_></_> -> {
			 if (tnodetool.contains(innernode, "Suciu")
				 && tnodetool.endWith(innernode, "or")) {
			   return true;
			 }
		   }
		   
		}
		
		return false;
	  }	  
	}

	class _TNodeQualifier_collectData02 extends TNodeQualifier {
	  public Sequence qualify(Object node) 
	  {
		Sequence result = new Sequence(); 
		%match (TNode node) {
		  <_>innernode@<_></_></_> -> {
			 result.add(innernode);
		 }
		}
		
		return result;
	  }
	}

	
	return tnodetool.collectData2(subject,
								  new _TNodeTester_collectData02(), 
								  new _TNodeQualifier_collectData02());	
	
  }


  protected Sequence _collectData01(TNode subject) { 
	class _TNodeTester_collectData01 extends TNodeTester{
	  public boolean doTest(Object obj) {
		%match (TNode obj) {
		  <book><title></title></book> -> {
			 return true;
			 
		 }
		}
		
		return false;
	  }
	}

	class _TNodeQualifier_collectData01 extends TNodeQualifier {
	  public Sequence qualify(Object node) 
	  {
		Sequence result = new Sequence(); 
		%match (TNode node) {
		  <book></book> -> {
			 result.add(node);
			 
		 }
		}
		
		return result;
	  }
	}
	
	return tnodetool.collectData(subject,
								 new _TNodeTester_collectData01(), 
								 new _TNodeQualifier_collectData01());	
  }


}
