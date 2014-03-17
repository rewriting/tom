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


public class UC1_4_2 {
    
  %include {TNode.tom}
  private XmlTools xtools;
  private GenericTraversal traversal = new GenericTraversal();
  TNodeTool tnodetool=new TNodeTool(); 
  SequenceTool sequencetool = new SequenceTool();

  private TNode _xmlDocument01 = null;
		
  private Sequence _a = null;

  private Object _last = null;
  private Object _first = null; 
  

  
  QueryRecordSet _queryRecordSet01 = new QueryRecordSet (); 

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }

  public static void main(String args[]) 
  {
	UC1_4_2 uc = new UC1_4_2();
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
	  executeQuery(_xmlDocument01.getDocElem());
	}
	catch (XQueryGeneralException e) {
	  System.out.println("ERROR: xquery exception error");	  
	}
  }


 
  private void executeQuery(TNode documentNode01) 
	throws XQueryGeneralException
  {

	_a = _collectData01(documentNode01);
	System.out.println("<results>");
	
	
	_forLetWhereOrderReturn01();
	// System.out.println(lastSequence.size());
// 	System.out.println(lastSequence);
	
 	
	System.out.println("</results>");

  }

  private void _forLetWhereOrderReturn01() 
	throws XQueryGeneralException
  {
	Sequence lastSequence = _distinctValue01(_a); 
	_forLetWhere01(lastSequence, _queryRecordSet01);
	//	System.out.println(bSequence);
	
 	_order01(_queryRecordSet01);
	
	_return01(_queryRecordSet01);
	
  }



  private void _order01(QueryRecordSet queryRecordSet) 
	throws XQueryGeneralException
  {
 	class _Comparator_order01 implements Comparator {
	  public int compare(Object o1, Object o2) {
		String last1 = (String)(((QueryRecord)o1).getField(0));
		String last2 =(String)(((QueryRecord)o2).getField(0));
		int result =last1.compareTo(last2);
		if (result ==0) {
		  String first1 = (String)(((QueryRecord)o1).getField(1));
		  String first2 =(String)(((QueryRecord)o2).getField(1));
		  return first1.compareTo(first2);
		}
		else {
		  return result;
		}
	  }
 	}

	sequencetool.sort(queryRecordSet, new _Comparator_order01());
  }
  

  private void _order02(QueryRecordSet queryRecordSet) 
	throws XQueryGeneralException
  {
 	class _Comparator_order01 implements Comparator {
	  public int compare(Object o1, Object o2) {
		String node1 = (String)(((QueryRecord)o1).getField(1));
		String node2 =(String)(((QueryRecord)o2).getField(1));
		return node1.compareTo(node2);
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
		System.out.println("<result>");
		
		_last=(String)(record.getField(0)); 
		_first=(String)(record.getField(1)); 
		System.out.println("<author>");
		
		System.out.println("<last>"+ _last + "</last>");
		System.out.println("<first>"+ _first + "</first>");
		System.out.println("</author>");
		
		this._forLetWhereOrderReturn02(); 


		System.out.println("</result>");
	  }

	  private TNode _b = null; 
	  private TNode _ba = null;

	  QueryRecordSet _queryRecordSet02 = new QueryRecordSet(); 


	  private void _forLetWhereOrderReturn02() 
		throws XQueryGeneralException
	  {
		_queryRecordSet02 = new QueryRecordSet(); 
		Sequence bSequence = this._collectData01(_xmlDocument01.getDocElem());
		
		//System.out.println(bSequence);
		
		
		this._forLetWhere01(bSequence, this._queryRecordSet02);
		//System.out.println(bSequence);
		
		this._return02(this._queryRecordSet02);
	  }
	  

	  private void _forLetWhere01(Sequence sequence, QueryRecordSet queryRecordSet) 
		  throws XQueryGeneralException
	  {
		
		class _TNodeTester_forLetWhere01 extends TNodeTester {
		  public boolean doTest(Object obj) 
		  {
			%match (TNode obj) {
			  <_><author><last>#TEXT(tlast)</last><first>#TEXT(tfirst)</first></author></_> -> {
				 if ((tlast==_last) && (tfirst.compareTo(_first)==0)) {
				   
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
		  _b = itemtuple; 
		  
		  if (tester.doTest(_b)) {
			QueryRecord r = new QueryRecord(1);
			r.setField(_b,0); 
			queryRecordSet.add(r);
		  }
		}
	  }


	  private void _return02(QueryRecordSet recordset) 
		throws XQueryGeneralException
	  {
		class _RecordPrinter01_return02 extends RecordPrinter {
		  public void  print(QueryRecord record)
			throws XQueryGeneralException
		  {
			TNode b=(TNode)(record.getField(0)); 
			%match (TNode b) {
			  <book>title@<title></title></book> -> {
				 xtools.printXMLFromATerm(title);
				 System.out.println();				 
			   }
			}
			
			
		  }
		}
		
		tnodetool.printResult(recordset, new _RecordPrinter01_return02()); 
	  }

	  protected Sequence _collectData01(TNode subject) { 
		class _TNodeTester_collectData01 extends TNodeTester{
		  public boolean doTest(Object obj) {
			%match (TNode obj) {
			  <bib></bib> -> {
				 return true; 
			   }
			}
			
			return false;
		  }
		}
		
		class _TNodeTester02_collectData01 extends TNodeTester{
		    public boolean doTest(Object node) 
		  {
			%match (TNode node) {
			  <book></book> -> {
				 return true; 
			   }
			}
			return false;
		  }
		}

		PathOperator oper = new SlashOperator(new _TNodeTester_collectData01(), 
											  new SlashOperator(new _TNodeTester02_collectData01(), null));
		return oper.run(subject);
	  }
	}
  

	tnodetool.printResult(queryRecordSet, new _RecordPrinter01_return01()); 
  }


  private void _forLetWhere01(Sequence sequence, QueryRecordSet queryRecordSet) 
	throws XQueryGeneralException
  {
	
	Enumeration enum = sequence.elements(); 
	
	while (enum.hasMoreElements()) {
	  Object itemtuple = enum.nextElement();  
	  _last = itemtuple; 
	  
	  Sequence firstSequence = _distinctValue02(_a);
	  _forLetWhere02(firstSequence, queryRecordSet);
	}
  }


  private void _forLetWhere02(Sequence sequence, QueryRecordSet queryRecordSet) 
	throws XQueryGeneralException
  {
	
	Enumeration enum = sequence.elements(); 
	
	while (enum.hasMoreElements()) {
	  Object itemtuple = enum.nextElement();  
	  _first = itemtuple; 
	  
	  QueryRecord record = new QueryRecord(2);
	  record.setField(_last,0);
	  record.setField(_first,1);
	  queryRecordSet.add(record);
	}
  }


  private Sequence _distinctValue02(Sequence seq) 
	throws XQueryGeneralException
  {
	class Comparator01_distinctValue02 implements Comparator {
	  public int compare(Object obj1, Object obj2) 
	  {
		%match (TNode obj1, TNode obj2) {
		  <_><first>#TEXT(thefirst1)</first></_>, <_><first>#TEXT(thefirst2)</first></_> -> {
			 return thefirst1.compareTo(thefirst2); 
			 
		   }
		}
		return 0; 
	  }
	}


	class TNodeTester01_distinctValue02 extends TNodeTester {
	  public boolean doTest(Object obj)
	  {
		%match (TNode obj) {
		  <_><last>#TEXT(thelast)</last></_> -> {
			 if (thelast == _last) {
			   return true;
			 }
		   }
		}
		return false;
	  }
	}


	class TNodeQualifier01_distinctValue02 extends TNodeQualifier {
	  public Sequence qualify(Object node) {
		Sequence seq = new Sequence(); 
		%match (TNode node) {
		  <_><first>#TEXT(thefirst)</first></_> -> {
			 seq.add(thefirst); 
		   }
		}
		return seq;
	  }
	}

	seq=tnodetool.distinctValues(seq, 
							new Comparator01_distinctValue02(), 
							new TNodeTester01_distinctValue02(),
							new TNodeQualifier01_distinctValue02());

	return seq; 
  }



  private Sequence _distinctValue01(Sequence seq) 
	throws XQueryGeneralException
  {
	class Comparator01_distinctValue01 implements Comparator {
	  public int compare(Object obj1, Object obj2) 
	  {
		%match (TNode obj1, TNode obj2) {
		  <_><last>#TEXT(thelast1)</last></_>, <_><last>#TEXT(thelast2)</last></_> -> {
			 return thelast1.compareTo(thelast2); 
			 
		   }
		}
		return 0; 
	  }
	}

	class TNodeTester01_distinctValue01 extends TNodeTester 
	{
	  
	  public boolean doTest(TNode obj)
	  {
		%match (TNode obj) {
		  <_><last></last></_> -> {
			 return true; 
		   }
		}
		return false;
	  }
	}

	class TNodeQualifier01_distinctValue01 extends TNodeQualifier {
	  public Sequence qualify(Object node) {
		Sequence seq = new Sequence(); 
		%match (TNode node) {
		  <_><last>#TEXT(thelast)</last></_> -> {
			 seq.add(thelast); 
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


  protected Sequence _collectData01(TNode subject) { 
	class _TNodeTester_collectData01 extends TNodeTester{
	  public boolean doTest(Object obj) {
		%match (TNode obj) {
		  <author></author> -> {
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
