package xquery.util; 


import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;


public class TNodeElementTester extends TNodeTester {
  
  public TNodeElementTester()
  {
	super();
  }
  
  public TNodeElementTester(Object objects[])
  {
	super(objects);
  }

  public boolean doTest (Object obj)
	throws XQueryGeneralException
  {
	if (obj instanceof TNode) {
	  if (((TNode)obj).isElementNode()) {
		return true;
	  }
	}
	return false;
  }

}
