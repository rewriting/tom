package xquery.util; 

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*;
import jtom.runtime.*;

public class SlashOperator extends PathOperator{
  %include {TNode.tom}

  public SlashOperator(TNodeTester tester, PathOperator nextOperator)
  {
	super(tester, nextOperator);
  }

  public SlashOperator(PathOperator nextOperator)
  {
	super(new TNodeElementTester(), nextOperator);
  }

  public SlashOperator()
  {
	super(new TNodeElementTester(), null);
  }

//   public Sequence run(TNode subject) 
//   {

// 	try {
// 	  //return null;
// 	  HashSequence seq=new HashSequence(); 
// 	  if (doTest(subject)) {
// 		seq.addAll(runNext(subject));
// 	  }	  
// 	  return seq;
// 	}
// 	catch (XQueryGeneralException e) {
// 	  System.out.println("ERROR: xqueryGeneral exception");
// 	  return null; 
// 	}
	
//   }

}
