package xquery.util; 

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*;
import jtom.runtime.*;

public class TNodeQualifier {

  %include {TNode.tom}

  private XmlTools xtools = new XmlTools();

  private Factory getTNodeFactory() 
  {
	return xtools.getTNodeFactory();
  }
  
  
  public Sequence qualify(TNode node) 
  {
	Sequence result = new Sequence(); 
	result.add(node);
	return result;
  }
  
}