package xquery.util;


import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import jtom.runtime.*;
import jtom.runtime.xml.*;

public class TNodeEvaluator {
  %include {TNode.tom}

  private XmlTools xtools = new XmlTools();

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }


  public float getFloatValue(TNode node)
	throws XQueryGeneralException
  {
	try {
	  %match (TNode node) {
 		<_>#TEXT(str)</_> -> {
 		   return Float.parseFloat(str);
 		 }
 	  }
	  throw new XQueryGeneralException();
	}
	catch (NumberFormatException e) {
	  throw new XQueryGeneralException();
	}
  }

  public  int getIntegerValue(TNode node)
	throws XQueryGeneralException
  {
	return 0; 
  }


  public String getStringValue(TNode node)
  {
	return "";
  }
}