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


  public   Sequence collectData(TNode subject,final TNodeTester tester, final TNodeQualifier qualifier) 
  {
	GenericTraversal traversal = new GenericTraversal();
	final Sequence sequence=new Sequence(); 
	
	Collect1 collect = new Collect1() { 
		public boolean apply(ATerm t) 
		{ 
		  try {
			if(t instanceof TNode) { 
			  TNode tnode=(TNode)t; 
			  
			  if (tester.doTest(tnode)) {
				sequence.addAll(qualifier.qualify(tnode));
				return true;
			  }
			} 
			return true; 
		  }
		  catch (XQueryGeneralException e) {
			System.out.println("ERROR: xqueryGeneral exception");
			return false; 
		  }
		} // end apply 
	  }; // end new 
	traversal.genericCollect(subject, collect); 
	
	
    return sequence;
  }


  public   Sequence collectData2(TNode subject,final TNodeTester tester,final TNodeQualifier qualifier) 
  {
	try {
	//return null;
	  Sequence seq=new Sequence(); 
	  %match (TNode subject) {
		<_></_> -> {
		   if (tester.doTest(subject)) {
			 seq.addAll(qualifier.qualify(subject));
		   }
		 }
	  }
	  
	  return seq;
	}
	catch (XQueryGeneralException e) {
	  System.out.println("ERROR: xqueryGeneral exception");
	  return null; 
	}
  }

  public   void printResult(QueryRecordSet queryRecordSet, RecordPrinter printer) 
	throws XQueryGeneralException
  {
	Enumeration enum = queryRecordSet.elements(); 
	while (enum.hasMoreElements()) {
	  QueryRecord record = (QueryRecord)(enum.nextElement());
	  printer.print(record);
	  
	}
  }
}
