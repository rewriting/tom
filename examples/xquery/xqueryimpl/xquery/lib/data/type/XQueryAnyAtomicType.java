//Source file: C:\\document\\codegen\\xquery\\lib\\data\\type\\AnyAtomicType.java

package xquery.lib.data.type;

import org.w3c.dom.*;


import xquery.lib.data.*;

public class XQueryAnyAtomicType 
{   
  /**
   * @roseuid 4110B803002A
   */
  public XQueryAnyAtomicType() 
  {
	//	this.type = ANY_ATOMIC;
  }

  public boolean checkValueType(String str) 
  {
	return true; 
  }

  public boolean checkType(Node node) 
  {
	return true; 
  }

  public boolean checkType(Item item) 
  {
	if (item.isNode()) {
	  return checkType(item.getNode());
	}
	else {  // is a atom;
	  Atom atom = item.getAtom(); 
	  XQueryAnyAtomicType type = atom.getType();
	  
	  if (type.getClass().isInstance(atom)) {
		return true; 
	  }
	  else {
		return false;
	  }
	}
  }
  
}
