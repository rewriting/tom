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

  public boolean qualify(String str) 
  {
	return true; 
  }

  public boolean qualify(Node node) 
  {
	return true; 
  }

  public boolean qualify(Item item) 
  {
	if (item.isNode()) {
	  return qualify(item.getNode());
	}
	else {  // is a atom;
	  return qualify(item.getAtom());
	}
  }

  public XQueryAnyAtomicType createInstance()
  {
	return new XQueryAnyAtomicType(); 
  }


  public boolean qualify(Atom atom) 
  {
	XQueryAnyAtomicType type = atom.getType();
	
	if (type.getClass().isInstance(atom)) {
	  return true; 
	}
	else {
	  return false;
	}
  }

  public boolean qualify(XQueryAnyAtomicType type)
  {
	XQueryAnyAtomicType thistype=createInstance();
	if (type.getClass().isInstance(thistype)) {
	  return true; 
	}
	else {
	  return false; 
	}
  }
  
}
