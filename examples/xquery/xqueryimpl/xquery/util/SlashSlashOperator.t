package xquery.util; 

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*;
import jtom.runtime.*;

public class SlashSlashOperator extends PathOperator{
  %include {TNode.tom}

  private XmlTools xtools = new XmlTools();

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }


  public SlashSlashOperator(TNodeTester tester, PathOperator nextOperator)
  {
	super(tester, nextOperator);
  }


  public Sequence run(TNode subject) 
  {
	GenericTraversal traversal = new GenericTraversal();
	final HashSequence s=new HashSequence(); 
	
	Collect1 collect = new Collect1() { 
		public boolean apply(ATerm t) 
		{ 
		  try {
			if(t instanceof TNode) {
			  TNode node = (TNode)t; 
			  if (doTest(node)) {
				s.addAll(runNext(node));
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
	
	
    return s;
  }

}
