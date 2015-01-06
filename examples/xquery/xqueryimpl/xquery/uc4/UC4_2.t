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


public class UC4_2 {
    
  %include {TNode.tom}
  private XmlTools xtools=new XmlTools();
  private GenericTraversal traversal = new GenericTraversal();
  TNodeTool tnodetool=new TNodeTool(); 
  SequenceTool sequencetool = new SequenceTool();

  private TNode _xmlDocument01 = null;
  private TNode _xmlDocument02 = null;
    
		
  private TNode _i = null;
  private Sequence _b =new Sequence();

  QueryRecordSet _queryRecordSet01 = new QueryRecordSet (); 

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC4_2 uc = new UC4_2();
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
	//	xtools = new XmlTools();

	_xmlDocument01 = (TNode)xtools.convertXMLToATerm(filename01); 
	_xmlDocument02 = (TNode)xtools.convertXMLToATerm(filename02); 
	
	try {
	executeQuery(_xmlDocument01.getDocElem(),_xmlDocument02.getDocElem());
	}
	catch (XQueryGeneralException e) {
	  System.out.println("ERROR: xquery exception error");
	  
	}
  }


 
  private void executeQuery(TNode documentNode01, TNode documentNode02) 
	throws XQueryGeneralException
  {

	Sequence iSequence = _collectData01(documentNode01);
	

	System.out.println("<result>");
  
 	_forLetWhere01(iSequence, _queryRecordSet01);
	_order01(_queryRecordSet01);
	_return01(_queryRecordSet01);

	System.out.println("</result>");

  }


  private void _order01(QueryRecordSet queryRecordSet) 
	throws XQueryGeneralException

  {
 	class _Comparator_order01 implements Comparator {
	  public int compare(Object o1, Object o2) {
		TNode node1 = (TNode)(((QueryRecord)o1).getField(0));
		TNode node2 =(TNode)(((QueryRecord)o2).getField(0));
		%match (TNode node1, TNode node2) {
		  <_><itemno>#TEXT(itemno1)</itemno></_>, <_><itemno>#TEXT(itemno2)</itemno></_> -> {
			 return (Integer.parseInt(itemno1) - Integer.parseInt(itemno2));
		   }
		}
		return 0;
	  }
 	}

	sequencetool.sort(queryRecordSet, new _Comparator_order01());
  }


  private void _return01(QueryRecordSet queryRecordSet)
	throws XQueryGeneralException
  {
	class _RecordPrinter01_return01 extends RecordPrinter {
	  public void  print(QueryRecord record)
		throws XQueryGeneralException
	  {
		TNode i=(TNode)(record.getField(0)); 
		Sequence b=(Sequence)(record.getField(1));
		
		System.out.println("<item_tuple>");
		%match (TNode i) {
		  <_>itemno@<itemno></itemno>description@<description></description></_> -> {
			 xtools.printXMLFromATerm(itemno);
			 System.out.println();
			 xtools.printXMLFromATerm(description);
			 System.out.println();
			 System.out.println("<high_bid>" + _max01(b) + "</high_bid>");		   
		   }
		}
		System.out.println("</item_tuple>");

		
	  }
	}
  

	tnodetool.printResult(queryRecordSet, new _RecordPrinter01_return01()); 
  }



  private void _forLetWhere01(Sequence sequence, QueryRecordSet queryRecordSet) 
  {
	class _TNodeTester_forLetWhere01 extends TNodeTester {
	  
	  public boolean doTest(TNode obj) {
		%match (TNode obj) {
		  <_><description>#TEXT(descriptiontext)</description></_> -> {
			 if (descriptiontext.toUpperCase().indexOf("BICYCLE") != -1) {
			   return true;
			 }
		   }

		}
		return false;
	  }
	}
	
	_TNodeTester_forLetWhere01 tester = new _TNodeTester_forLetWhere01();

	Enumeration enum = sequence.elements(); 

	while (enum.hasMoreElements()) {
	  TNode itemtuple = (TNode)(enum.nextElement());
	  
	  _i = itemtuple; 
	  _b = _collectData02(_xmlDocument02);
	  if (tester.doTest(_i)) {
		QueryRecord record = new QueryRecord(2); 
		record.setField(_i, 0);
		record.setField(_b, 1);

		queryRecordSet.add(record);
	  }
	}

  }
  


  protected float _max01(Sequence nodeSequence) 
	throws XQueryGeneralException
  {

	class _TNodeEvaluator_max01 extends TNodeEvaluator {
	  public float getFloatValue(TNode node)
		throws XQueryGeneralException
	  {
		try {
		  %match (TNode node) {
			<_><bid>#TEXT(bidtext)</bid></_> -> {
			   return Float.parseFloat(bidtext);
			 }
		  }
		  throw new XQueryGeneralException();
		}
		catch (NumberFormatException e) {
		  throw new XQueryGeneralException();
		}
	  }
	}

	return ArithmeticTool.max(nodeSequence, new _TNodeEvaluator_max01());
  }


  protected Sequence _collectData02(TNode subject) { 
	class _TNodeTester_collectData02 extends TNodeTester {

	  public boolean doTest(Object obj)
	  {
		TNode o = (TNode) obj;
		if (o.isElementNode()) {
		  TNode obj2 = _i;
		  %match (TNode o, TNode obj2) {
			<bid_tuple>itemno1@<itemno></itemno></bid_tuple>, 
			   <item_tuple>itemno2@<itemno></itemno></item_tuple> -> {
			   if (itemno1.isEqual(itemno2))
				 return true;
			   else
				 return false; 
			 }
		  }
		}
		return false;
	  }	  
	}
	
	TNode objs[]={_i};
	
	SlashSlashOperator oper = new SlashSlashOperator(new _TNodeTester_collectData02(), null);
	return oper.run(subject);
	
  }


  protected Sequence _collectData01(TNode subject) { 
	class _TNodeTester_collectData01 extends TNodeTester{
	  public boolean doTest(Object obj) {
		TNode o = (TNode) obj;
		%match (TNode o) {
		  <item_tuple></item_tuple> -> {
			 return true;
			 
		 }
		}
		
		return false;
	  }

	  public boolean doTest(TNode obj1, TNode obj2) {
		return false; 
	  }
	}
	
	SlashSlashOperator oper = new SlashSlashOperator(new _TNodeTester_collectData01(), null);

	return oper.run(subject);
  }


}
