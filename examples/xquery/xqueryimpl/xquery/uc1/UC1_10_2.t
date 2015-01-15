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


public class UC1_10_2 {
    
  %include {TNode.tom}
  private XmlTools xtools=new XmlTools();
  private GenericTraversal traversal = new GenericTraversal();
  TNodeTool tnodetool=new TNodeTool(); 
  SequenceTool sequencetool = new SequenceTool();

  private TNode _xmlDocument01 = null;

  private TNode _doc = null; 
    
		
  private Object _t = null;
  private Sequence _p=new Sequence();

  QueryRecordSet _queryRecordSet01 = new QueryRecordSet (); 

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_10_2 uc = new UC1_10_2();
	String filename01;

	if (args.length == 0) {
	  filename01="xquery/data/prices.xml";
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
	_doc=_xmlDocument01.getDocElem();
	
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

	

	System.out.println("<results>");
	
	Sequence tSequence = _distinctValue01(_collectData01(_doc));
	_FLWOR01(tSequence);

	System.out.println("</results>");

  }

  private Sequence _distinctValue01(Sequence seq) 
	throws XQueryGeneralException
  {
	class Comparator01_distinctValue01 implements Comparator {
	  public int compare(Object obj1, Object obj2) 
	  {
		%match (TNode obj1, TNode obj2) {
		  <_>#TEXT(thetitle1)</_>, <_>#TEXT(thetitle2)</_> -> {
			 return thetitle1.compareTo(thetitle2); 
			 
		   }
		}
		return 0; 
	  }
	}

	class TNodeTester01_distinctValue01 extends TNodeTester 
	{
	  
	  public boolean doTest(TNode obj)
		throws XQueryGeneralException
	  {
		return super.doTest(obj);
	  }
	
	}
	class TNodeQualifier01_distinctValue01 extends TNodeQualifier {
		public Sequence qualify(Object node) {
		Sequence seq = new Sequence(); 
		%match (TNode node) {
		  <_>#TEXT(thetitle)</_> -> {
			 seq.add(thetitle); 
		   }
		}
		return seq;
		}
	  }
	  
	  seq= tnodetool.distinctValues(seq, 
									new Comparator01_distinctValue01(), 
									new TNodeTester01_distinctValue01(),
									new TNodeQualifier01_distinctValue01());
	  return seq;
  }

  private void _FLWOR01(Sequence seq) 
			throws XQueryGeneralException
  {
	_forLetWhere01(seq, _queryRecordSet01);
	_return01(_queryRecordSet01);
  }


  private float _min01(Sequence seq)
	throws XQueryGeneralException
  {
	ArithmeticTool tool = new ArithmeticTool();
	return tool.min(seq, new TNodeEvaluator());
  }

  private void _return01(QueryRecordSet queryRecordSet)
	throws XQueryGeneralException
  {
	class _RecordPrinter01_return01 extends RecordPrinter {
	  public void  print(QueryRecord record)
		throws XQueryGeneralException
	  {
		String t=(String)(record.getField(0)); 
		Sequence p=(Sequence)(record.getField(1));

		System.out.print("<minprice title=\"");
		System.out.print(t);
		System.out.println("\"");

		System.out.print("<price>");
		System.out.print(_min01(p));
		System.out.println("</price>");
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
	  String itemtuple = (String)(enum.nextElement());
	  
	  _t = itemtuple; 
	  _p = _collectData02(_doc);


	  if (tester.doTest(_t)) {
		QueryRecord record = new QueryRecord(2); 
		record.setField(_t, 0);
		record.setField(_p, 1);

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
		%match (TNode obj) {
		  <book><title>#TEXT(thetitle)</title><price></price></book> -> {
			 if (thetitle == _t) {
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
		  <book>price@<price></price></book> -> {
			 result.add(price);
			 
		 }
		}
		
		return result;
	  }
	}

	
	return tnodetool.collectData(subject, new _TNodeTester_collectData02(), new _TNodeQualifier_collectData02());	
	
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
		  <book>title@<title></title></book> -> {
			 result.add(title);
			 
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
