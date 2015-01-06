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
package xquery.uc4;

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import jtom.runtime.*;

import java.util.*; 

import xquery.util.*;
import java.io.*; 


public class UC4_4 {
    
  %include {TNode.tom}
  private XmlTools xtools;
  private GenericTraversal traversal = new GenericTraversal();


  TNodeTool tnodetool = new TNodeTool(); 
  SequenceTool sequencetool = new SequenceTool(); 


  private TNode _xmlDocument01 = null;
  private TNode _xmlDocument02 = null;
    
		
  private TNode _i = null;

  QueryRecordSet _queryRecordSet01 = new QueryRecordSet (); 






  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC4_4 uc = new UC4_4();
	String filename01;
	String filename02;

	if (args.length == 0) {
	  filename01="xquery/data/items.xml";
	  filename02="xquery/data/bids.xml";
	}
	else {
	  filename01=args[0];
	  filename02=args[1];
	}

	uc.run(filename01, filename02);
  }


  private void run(String filename01, String filename02) 
  {
	xtools = new XmlTools();

	_xmlDocument01 = (TNode)xtools.convertXMLToATerm(filename01); 
	_xmlDocument02 = (TNode)xtools.convertXMLToATerm(filename02); 

	try {
	executeQuery(_xmlDocument01.getDocElem(),_xmlDocument02.getDocElem());
	}
	catch (XQueryGeneralException  e) {
	  System.out.println("ERROR: xquere general exception");
	  
	}
	
  }


 
  private void executeQuery(TNode documentNode01, TNode documentNode02) 
	throws XQueryGeneralException
  {
	System.out.println("<result>");

	Sequence iSequence = _collectData01(documentNode01);
 	_forLetWhere01(iSequence, _queryRecordSet01);
	_return01(_queryRecordSet01);

	System.out.println("</result>");

  }


  private void _return01(QueryRecordSet queryRecordSet)
	throws XQueryGeneralException
  {

	class _RecordPrinter01_return01 extends RecordPrinter{
	  public void print(QueryRecord record) 
	  {
		TNode i=(TNode)(record.getField(0));
		System.out.println("<no_bid_item>");
		%match (TNode i) {
		  <_>itemno@<itemno></itemno>description@<description></description></_> -> {
			 xtools.printXMLFromATerm(itemno);
			 System.out.println();
			 xtools.printXMLFromATerm(description);
			 System.out.println();
		   }
		}
		System.out.println("</no_bid_item>");		
	  }
	}
	
	tnodetool.printResult(queryRecordSet, new _RecordPrinter01_return01());
  }


  private void _forLetWhere01(Sequence sequence, QueryRecordSet queryRecordSet) 
  {
	class _TNodeTester01_forLetWhere01 extends TNodeTester {

	  public boolean doTest(Object obj) {
		TNode doc2 = _xmlDocument02;
		
		if (sequencetool.empty(_collectData02(doc2))) {
		  return true; 
		}
		else {
		  return false;
		}
	  }
	}


	//	TNode objs[]={_xmlDocument02};
	_TNodeTester01_forLetWhere01 tester = new _TNodeTester01_forLetWhere01();

	Enumeration enum = sequence.elements(); 
	//System.out.println(sequence.size());
	
	while (enum.hasMoreElements()) {
	  TNode item = (TNode)(enum.nextElement());
	  
	  _i = item; 
	  if (tester.doTest(_i)) {
		QueryRecord record = new QueryRecord(1);
		record.setField(_i,0);
		queryRecordSet.add(record);
	  }
	}
  }
  


  protected Sequence _collectData02(TNode subject) { 
	class _TNodeTester_collectData02 extends TNodeTester {

	  public boolean doTest(Object obj) {
		TNode i = _i;
		
		%match (TNode obj, TNode i) {
		  <bid_tuple><itemno>#TEXT(itemno1)</itemno></bid_tuple>,
			 <_><itemno>#TEXT(itemno2)</itemno></_>-> {
			 if (Integer.parseInt(itemno1) == Integer.parseInt(itemno2)) {
			   return true;
			 }
			 
		   }
		}
		
		return false;
	  }
	}
	
	//	Object objs[]={_i};

	SlashSlashOperator oper = new SlashSlashOperator(new _TNodeTester_collectData02(), null);	
	return oper.run(subject);
  }


  protected Sequence _collectData01(TNode subject) { 
	class _TNodeTester_collectData01 extends TNodeTester{
	  public boolean doTest(Object obj) {

		%match (TNode obj) {
		  <item_tuple></item_tuple> -> {
			 return true;
			 
		 }
		}
		
		return false;
	  }

	}
	
	SlashSlashOperator oper = new SlashSlashOperator(new _TNodeTester_collectData01(), null);	
	return oper.run(subject);
  }


}
