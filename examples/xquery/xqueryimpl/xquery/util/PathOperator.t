package xquery.util; 

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*;
import jtom.runtime.*;

public class PathOperator {
  %include {TNode.tom}

  protected XmlTools xtools = new XmlTools();

  protected Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }


  protected TNodeTester tester =null; 
  protected PathOperator nextOperator = null;

  public PathOperator(TNodeTester tester, PathOperator nextOperator)
  {
	this.tester =tester;
	this.nextOperator = nextOperator; 
  }

  public PathOperator(PathOperator nextOperator)
  {
	this.tester =new TNodeTester();
	this.nextOperator = nextOperator; 
  }

  public PathOperator()
  {
	this.tester = new TNodeTester();
	this.nextOperator = null; 
  }



  protected boolean doTest(Object node) 
	throws XQueryGeneralException
  {
	return tester.doTest(node);
  }


  protected Sequence runNext(TNode node)
  {
	if (this.nextOperator ==null) {
	  Sequence seq = new Sequence();
	  seq.add(node);
	  return seq;
	}
	else {
	  Sequence seq = new Sequence();
	  if (node.hasChildList()) {
		TNodeList childList = ((TNode)node).getChildList(); 
		
		for (int i=0; i<childList.getLength(); i++) {
		  Sequence s = this.nextOperator.run(childList.getTNodeAt(i));
		  seq.addAll(s);
		}
	  }

	  if (node.hasAttrList()) {
		TNodeList attrList = ((TNode)node).getAttrList(); 
		
		for (int i=0; i<attrList.getLength(); i++) {
		  Sequence s = this.nextOperator.run(attrList.getTNodeAt(i));
		  seq.addAll(s);
		}
	  }

	  return seq;
	}
  }
  

  public Sequence run(TNode subject) 
  {
	final Sequence s=new Sequence(); 

	try {
	  if (doTest(subject)) {
		s.addAll(runNext(subject));
	  } 
	}
	
	catch (XQueryGeneralException e) {
	  System.out.println("ERROR: xqueryGeneral exception");
	  return null; 
	}
	
	return s;
  }

}
