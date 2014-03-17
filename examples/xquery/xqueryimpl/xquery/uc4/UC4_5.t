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
package xquery.uc4;

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import jtom.runtime.*;

import java.util.*; 

import xquery.util.*;
import java.io.*; 


public class UC4_5 {
    
  %include {TNode.tom}
  private XmlTools xtools;
  private GenericTraversal traversal = new GenericTraversal();
  TNodeTool tnodetool = new TNodeTool();
  SequenceTool sequencetool = new SequenceTool();

  private TNode _xmlDocument01 = null;
  private TNode _xmlDocument02 = null;
  private TNode _xmlDocument03 = null;
  private TNode _xmlDocument04 = null;
    
		
  private TNode _seller = null;
  private TNode _buyer = null;
  private TNode _item = null;
  private TNode _highbid = null;

  QueryRecordSet _queryRecordSet01 = new QueryRecordSet (); 



  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC4_5 uc = new UC4_5();
	String filename01;
	String filename02;
	String filename03;
	String filename04;

	if (args.length == 0) {
	  filename01="xquery/data/users.xml";
	  filename02="xquery/data/items.xml";
	  filename03="xquery/data/bids.xml";

	}
	else {
	  filename01=args[0];
	  filename02=args[1];
	  filename03=args[2];
	}

	uc.run(filename01, filename02, filename03);
  }


  private void run(String filename01, String filename02, String filename03) 
  {
	xtools = new XmlTools();

	_xmlDocument01 = (TNode)xtools.convertXMLToATerm(filename01); 
	_xmlDocument02 = _xmlDocument01; 
	_xmlDocument03 = (TNode)xtools.convertXMLToATerm(filename02); 
	_xmlDocument04 = (TNode)xtools.convertXMLToATerm(filename03); 
	
	try {
	executeQuery(_xmlDocument01.getDocElem(),_xmlDocument02.getDocElem());
	}
	catch (XQueryGeneralException e) {
	  System.out.println("ERROR:XQuery general exception");
	  
	}
  }


 
  private void executeQuery(TNode documentNode01, TNode documentNode02) 
	throws XQueryGeneralException
  {
	//System.out.println(uSequence.size());
	
	System.out.println("<result>");

	Sequence sellerSequence = _collectData01(_xmlDocument01.getDocElem());
 	_forLetWhere01(sellerSequence, _queryRecordSet01);
	_order01(_queryRecordSet01);
	_return01(_queryRecordSet01);

	System.out.println("</result>");

  }


  private void _order01(QueryRecordSet queryRecordSet) 
	throws XQueryGeneralException

  {
 	class _Comparator_order01 implements Comparator {
	  public int compare(Object o1, Object o2) {
		TNode node1 = (TNode)(((QueryRecord)o1).getField(2));
		TNode node2 =(TNode)(((QueryRecord)o2).getField(2));
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

	class _RecordPrinter01_return01 extends RecordPrinter{
	  public void print(QueryRecord record) 
	  {
		TNode buyer=(TNode)(record.getField(1));
		TNode item=(TNode)(record.getField(2));
		TNode highbid=(TNode)(record.getField(3));

		System.out.println("<jones_bike>");
		%match (TNode item, TNode highbid, TNode buyer) {
		  <_>itemno@<itemno></itemno>description@<description></description></_>,
			 <_>bid@<bid></bid></_>,
			 <_>name@<name></name></_>-> {
			 xtools.printXMLFromATerm(itemno);
			 System.out.println();
			 xtools.printXMLFromATerm(description);
			 System.out.println();
			 
			 System.out.println("<high_bid>");
			 xtools.printXMLFromATerm(bid);
			 System.out.println();
			 System.out.println("</high_bid>");

			 System.out.println("<high_bider>");
			 xtools.printXMLFromATerm(name);
			 System.out.println();
			 System.out.println("</high_bider>");
			 
		   }
		}
		System.out.println("</jones_bike>");		
	  }
	}
	
	tnodetool.printResult(queryRecordSet, new _RecordPrinter01_return01());
  }

  private void _forLetWhere01(Sequence sequence, QueryRecordSet queryRecordSet) 
	throws XQueryGeneralException
  {
	Enumeration enum = sequence.elements(); 
	//System.out.println(sequence.size());
	
	while (enum.hasMoreElements()) {
	  TNode item = (TNode)(enum.nextElement());
	  
	  _seller = item; 
	  Sequence sellerSequence = _collectData02(_xmlDocument02);
	  //	  System.out.println(iSequence.size());
	 
	  _forLetWhere02(sellerSequence, queryRecordSet);
	}
  }


  private void _forLetWhere02(Sequence sequence, QueryRecordSet queryRecordSet) 
	throws XQueryGeneralException
  {
	Enumeration enum = sequence.elements(); 
	//System.out.println(sequence.size());
	
	while (enum.hasMoreElements()) {
	  TNode item = (TNode)(enum.nextElement());
	  
	  _buyer = item; 
	  Sequence buyerSequence = _collectData03(_xmlDocument03);
	  //	  System.out.println(iSequence.size());
	 
	  _forLetWhere03(buyerSequence, queryRecordSet);
	}
  }


  private void _forLetWhere03(Sequence sequence, QueryRecordSet queryRecordSet) 
	throws XQueryGeneralException
  {
	Enumeration enum = sequence.elements(); 
	//System.out.println(sequence.size());
	
	while (enum.hasMoreElements()) {
	  TNode item = (TNode)(enum.nextElement());
	  
	  _item = item; 
	  Sequence itemSequence = _collectData04(_xmlDocument04);
	  //	  System.out.println(iSequence.size());
	 
	  _forLetWhere04(itemSequence, queryRecordSet);
	}
  }

  private void _forLetWhere04(Sequence sequence, QueryRecordSet queryRecordSet) 
	throws XQueryGeneralException
  {
	class _TNodeTester01_forLetWhere04 extends TNodeTester {
	  public boolean doTest(Object obj) {
		%match (TNode obj) {
		  <_><name>#TEXT(thename)</name></_> -> {
			 if (thename.toUpperCase().compareTo("TOM JONES")==0){
			   return true;
			 }
		   }
		}
		return false;
	  }
	}

	class _TNodeTester02_forLetWhere04 extends TNodeTester {
	  public boolean doTest(Object obj) {
		TNode item = _item;
		%match (TNode obj, TNode item) {
		  <_><userid>#TEXT(id1)</userid></_>,
			 <_><offered_by>#TEXT(id2)</offered_by></_>-> {
			 if (id1.compareTo(id2) == 0){
			   return true;
			 }
		   }
		}
		return false;

	  }
	}


	class _TNodeTester03_forLetWhere04 extends TNodeTester {
	  public boolean doTest(Object obj) {
		%match (TNode obj) {
		  <_><description>#TEXT(description)</description></_> -> {
			 if (description.toUpperCase().indexOf("BICYCLE") >= 0){
			   return true;
			 }
		   }
		}
		return false;
	  }
	}

	class _TNodeTester04_forLetWhere04 extends TNodeTester {
	  public boolean doTest(Object obj) {

		TNode highbid = _highbid;
		%match (TNode obj, TNode highbid) {
		  <_><itemno>#TEXT(no1)</itemno></_>,
			 <_><itemno>#TEXT(no2)</itemno></_>-> {
			 if (no1.compareTo(no2) == 0){
			   return true;
			 }
		   }
		}
		return false;
	  }
	}

	class _TNodeTester05_forLetWhere04 extends TNodeTester {
	  
	  public boolean doTest(Object obj) {

		TNode buyer = _buyer;
		%match (TNode obj, TNode buyer) {
		  <_><userid>#TEXT(id1)</userid></_>,
			 <_><userid>#TEXT(id2)</userid></_>-> {
			 if (id1.compareTo(id2) == 0){
			   return true;
			 }
		   }
		}
		return false;
	  }
	}





	class _TNodeTester06_forLetWhere04 extends TNodeTester {
	  public boolean doTest(Object obj) 
		throws XQueryGeneralException
	  {
		TNode doc4 = _xmlDocument04.getDocElem(); 
		Sequence bidSequence = this._collectData01(doc4);
		%match (TNode obj) {
		  
		  
		  <_><bid>#TEXT(bid)</bid></_> -> {
			if (Float.parseFloat(bid) == this._max01(bidSequence)) {
			  return true;
			}
		  }
		}
		return false;
	  }
	  
	  
	  protected Sequence _collectData01(TNode subject) 
		throws XQueryGeneralException
	  { 
		 class _TNodeTester01_collectData01 extends TNodeTester{
		   public boolean doTest(Object obj) {
			 TNode item=_item; 
			 %match (TNode obj, TNode item) {
			   <bid_tuple><itemno>#TEXT(itemno1)</itemno></bid_tuple>, 
				  <_><itemno>#TEXT(itemno2)</itemno></_> -> {
				  if (itemno1.compareTo(itemno2)==0) {
					return true;
				  }				  
				}
			 }
			 
			 return false;
		   }		   
		 }
		 SlashSlashOperator oper = new SlashSlashOperator(new _TNodeTester01_collectData01(), null);	
		 return oper.run(subject);
	   }
	  
	  protected float _max01 (Sequence seq) 
		throws XQueryGeneralException
	  {
		
		class _TNodeEvaluator01_max01 extends TNodeEvaluator {
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
		
		return ArithmeticTool.max(seq, new _TNodeEvaluator01_max01());
	  }
		
	  
	}// tester 06


	_TNodeTester01_forLetWhere04 tester01 = new _TNodeTester01_forLetWhere04();
	_TNodeTester02_forLetWhere04 tester02 = new _TNodeTester02_forLetWhere04();
	_TNodeTester03_forLetWhere04 tester03 = new _TNodeTester03_forLetWhere04();
	_TNodeTester04_forLetWhere04 tester04 = new _TNodeTester04_forLetWhere04();
	_TNodeTester05_forLetWhere04 tester05 = new _TNodeTester05_forLetWhere04();
	_TNodeTester06_forLetWhere04 tester06 = new _TNodeTester06_forLetWhere04();

	Enumeration enum = sequence.elements(); 
	//System.out.println(sequence.size());
	
	while (enum.hasMoreElements()) {
	  TNode item = (TNode)(enum.nextElement());
	  
	  _highbid = item; 
	  if (tester01.doTest(_seller) 
		  &&  tester02.doTest(_seller)
		  && tester03.doTest(_item)
		  && tester04.doTest(_highbid)
		  && tester05.doTest(_highbid)
		  && tester06.doTest(_highbid)) {
		QueryRecord record = new QueryRecord(4);
		record.setField(_seller,0);
		record.setField(_buyer,1);
		record.setField(_item,2);
		record.setField(_highbid,3);
		queryRecordSet.add(record);
	  }
	}
  }
  


  protected Sequence _collectData01(TNode subject) 
	throws XQueryGeneralException
  { 
	class _TNodeTester01_collectData01 extends TNodeTester{
	  public boolean doTest(Object obj) {
		%match (TNode obj) {
		  <user_tuple></user_tuple> -> {
			 return true;
			 
		 }
		}
		
		return false;
	  }

	}
		SlashSlashOperator oper = new SlashSlashOperator(new _TNodeTester01_collectData01(), null);	
	return oper.run(subject);
  }

  protected Sequence _collectData02(TNode subject) throws XQueryGeneralException
  { 
	class _TNodeTester01_collectData02 extends TNodeTester{
	  public boolean doTest(Object obj) {
		%match (TNode obj) {
		  <user_tuple></user_tuple> -> {
			 return true;
			 
		 }
		}
		
		return false;
	  }

	}
		SlashSlashOperator oper = new SlashSlashOperator(new _TNodeTester01_collectData02(), null);	
	return oper.run(subject);
  }


  protected Sequence _collectData03(TNode subject) 
	throws XQueryGeneralException
  { 
	class _TNodeTester01_collectData03 extends TNodeTester{
	  public boolean doTest(Object obj) {
		%match (TNode obj) {
		  <item_tuple></item_tuple> -> {
			 return true;
			 
		 }
		}
		
		return false;
	  }

	}
	SlashSlashOperator oper = new SlashSlashOperator(new _TNodeTester01_collectData03(), null);	
	return oper.run(subject);
  }


  protected Sequence _collectData04(TNode subject) 
	throws XQueryGeneralException
  { 
	class _TNodeTester01_collectData04 extends TNodeTester{
	  public boolean doTest(Object obj) {
		%match (TNode obj) {
		  <bid_tuple></bid_tuple> -> {
			 return true;
			 
		 }
		}
		
		return false;
	  }

	}
	SlashSlashOperator oper = new SlashSlashOperator(new _TNodeTester01_collectData04(), null);		
	return oper.run(subject);
  }


 
}
