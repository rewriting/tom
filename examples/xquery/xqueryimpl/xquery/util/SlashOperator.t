package xquery.util; 

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*;
import jtom.runtime.*;

public class SlashOperator extends PathOperator{
  %include {TNode.tom}

  private XmlTools xtools = new XmlTools();

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }


  public SlashOperator(TNodeTester tester, PathOperator nextOperator)
  {
	super(tester, nextOperator);
  }


  public Sequence run(TNode subject) 
  {

	try {
	  //return null;
	  HashSequence seq=new HashSequence(); 
	  %match (TNode subject) {
		<_></_> -> {
		   if (doTest(subject)) {
			 seq.addAll(runNext(subject));
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

}
