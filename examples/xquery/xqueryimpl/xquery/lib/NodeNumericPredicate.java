//Source file: C:\\document\\codegen\\xquery\\lib\\NodeNumericPredicate.java

package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.type.*;
import xquery.lib.data.Item;

import xquery.util.DomTree02;

public class NodeNumericPredicate extends NodePredicate 
{
   
  protected int internalValue; 
  
  /**
   * @roseuid 4110FBD60026
   */
  public NodeNumericPredicate(int value) 
  {
    super();
	this.value = value;
  }
   
  public NodeNumericPredicate(Expression expr)
  {
	super(expr);
	this.value=0; 
  }

  protected int evaluate() throws XQueryGeneralException
  {
	if (value==0) {// not evaluated
	  Sequence value = getExpression.evaluate(); 
	  Item item= Item.convertSequenceToItem(value);
	
	  XQueryAnyAtomicType atomic=new XQueryAnyAtomicType(); 
	  
	  if (item.isAtom()) {
		if (item.getAtom().istyped()) {
		  value=item.getAtom().getDecimalValue(); 
		}
	  }
	  
	  XQueryInteger integer=XQueryInteger.qualify(item);
	  this.value=integer.
	}
  }
  
  /**
   * @param node
   * @return boolean
   * @roseuid 4110F795028D
   */
  public boolean doFilter(Node node, int siblingPosition) throws XQueryTypeException 
  {
	System.out.println("sibling: " + siblingPosition);
	DomTree02 dt = new DomTree02(); 
	//	dt.processNode(node);
	
	if (this.value == siblingPosition) {
	  
	  return true;
	}
	else {
	  return false;
	}

  }
}
