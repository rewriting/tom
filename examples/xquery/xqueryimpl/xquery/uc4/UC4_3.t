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


public class UC4_3 {
    
  %include {TNode.tom}
  private XmlTools xtools;
  private GenericTraversal traversal = new GenericTraversal();
  TNodeTool tnodetool = new TNodeTool(); 
  SequenceTool sequencetool = new SequenceTool(); 

  private TNode _xmlDocument01 = null;
  private TNode _xmlDocument02 = null;
    
		
  private TNode _i = null;
  private TNode _u =null;

  QueryRecordSet _queryRecordSet01 = new QueryRecordSet (); 


  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC4_3 uc = new UC4_3();
	String filename01;
	String filename02;

	if (args.length == 0) {
	  filename01="xquery/data/users.xml";
	  filename02="xquery/data/items.xml";
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

	Sequence uSequence = _collectData01(documentNode01);
	//System.out.println(uSequence.size());
	
	System.out.println("<result>");
  
 	_forLetWhere01(uSequence, _queryRecordSet01);
	//	_order01(_queryRecordSet01);
	_return01(_queryRecordSet01);

	System.out.println("</result>");

  }


  private void _return01(QueryRecordSet queryRecordSet)
	throws XQueryGeneralException 
  {
	class _RecordPrinter01_return01 extends RecordPrinter{
	  public void print(QueryRecord record) 
	  {

		TNode u=(TNode)(record.getField(0));
		TNode i=(TNode)(record.getField(1));

		System.out.println("<warning>");
		%match (TNode i, TNode u) {
		  <_>description@<description></description>reserveprice@<reserve_price></reserve_price></_>, 
			 <_>name@<name></name>rating@<rating></rating></_> -> {
			 xtools.printXMLFromATerm(name);
			 System.out.println();
			 xtools.printXMLFromATerm(rating);
			 System.out.println();
			 xtools.printXMLFromATerm(description);
			 System.out.println();
			 xtools.printXMLFromATerm(reserveprice);
			 System.out.println();
		   }
		}
		System.out.println("</warning>");
	  }
	}
	
	tnodetool.printResult(queryRecordSet, new _RecordPrinter01_return01());
  }


  private void _forLetWhere01(Sequence sequence, QueryRecordSet queryRecordSet) 
  {
	Enumeration enum = sequence.elements(); 
	//System.out.println(sequence.size());
	
	while (enum.hasMoreElements()) {
	  TNode usertuple = (TNode)(enum.nextElement());
	  
	  _u = usertuple; 
	  Sequence iSequence = _collectData02(_xmlDocument02);
	  //	  System.out.println(iSequence.size());
	 
	  _forLetWhere02(iSequence, queryRecordSet);
	}
  }



  private void _forLetWhere02(Sequence sequence, QueryRecordSet queryRecordSet) 
  {
	class _TNodeTester01_forLetWhere02 extends TNodeTester {
	  
	  public boolean doTest(Object obj) {
		%match (TNode obj) {
		  <_><rating>#TEXT(therating)</rating></_> -> {
			 if (therating.compareTo("C") > 0 ) {
			   return true;
			 }
			 
		   }

		}
		return false;
	  }
	}
	


	class _TNodeTester02_forLetWhere02 extends TNodeTester {
	  
	  public boolean doTest(Object obj) {
		%match (TNode obj) {
		  <_><reserve_price>#TEXT(theprice)</reserve_price></_> -> {
			 if (Integer.parseInt(theprice) > 1000) {
			   return true;
			 }
		   }

		}
		return false;
	  }
	}

	class _TNodeTester03_forLetWhere02 extends TNodeTester {

	  public boolean doTest(Object obj) {
		TNode u=_u; 
		%match (TNode obj, TNode u) {
		  <_><offered_by>#TEXT(offered)</offered_by></_>,
			 <_><userid>#TEXT(userid)</userid></_> -> {
			 
			 if (offered.compareTo(userid)==0) {
			   return true;
			 }
		   }

		}
		return false;
	  }
	}

	_TNodeTester01_forLetWhere02 tester01 = new _TNodeTester01_forLetWhere02();
	_TNodeTester02_forLetWhere02 tester02 = new _TNodeTester02_forLetWhere02();
	_TNodeTester03_forLetWhere02 tester03 = new _TNodeTester03_forLetWhere02();

	Enumeration enum = sequence.elements(); 

	while (enum.hasMoreElements()) {
	  TNode itemtuple = (TNode)(enum.nextElement());
	  //System.out.println(itemtuple);
	  
	  _i = itemtuple; 
	  
	  if (tester01.doTest(_u) && tester02.doTest(_i) && tester03.doTest(_i)){
		QueryRecord record = new QueryRecord(2); 
		record.setField(_u, 0);
		record.setField(_i,1);
		queryRecordSet.add(record);
	  }
	}

  }
  

  protected Sequence _collectData02(TNode subject) { 
	class _TNodeTester_collectData02 extends TNodeTester {
	 
	  public boolean doTest(Object obj) {
		%match (TNode obj) {
		  <item_tuple></item_tuple> -> {
			 return true;
			 
		   }
		}
		
		return false;
	  }
	}
	SlashSlashOperator oper = new SlashSlashOperator(new _TNodeTester_collectData02(), null);	
	return oper.run(subject);
	
  }


  protected Sequence _collectData01(TNode subject) { 
	class _TNodeTester_collectData01 extends TNodeTester{
	  public boolean doTest(Object obj) {
		%match (TNode obj) {
		  <user_tuple></user_tuple> -> {
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
