package xquery.util; 


import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;


public class TNodeAttributeTester extends TNodeTester {
  
  public TNodeAttributeTester()
  {
	super();
  }
  
  public TNodeAttributeTester(Object objects[])
  {
	super(objects);
  }

  public boolean doTest (Object obj)
	throws XQueryGeneralException
  {
// 	System.out.println("enter");
	
// 	System.out.println(obj);
	
	if (obj instanceof TNode) {
	  if (((TNode)obj).isAttributeNode()) {
		return true;
	  }
	}
	return false;
  }

}
