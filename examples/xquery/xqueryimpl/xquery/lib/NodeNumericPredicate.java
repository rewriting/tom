//Source file: C:\\document\\codegen\\xquery\\lib\\NodeNumericPredicate.java

package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.type.*;
import xquery.lib.data.Item;

import xquery.util.DomTree02;

import xquery.lib.data.*;

public class NodeNumericPredicate extends NodePredicate 
{
   
  protected int internalValue; 
  
  /**
   * @roseuid 4110FBD60026
   */
  public NodeNumericPredicate(int value) 
  {
    super();
	this.internalValue = value;
  }
   
  public NodeNumericPredicate(Expression expr)
  {
	super(expr);
	this.internalValue=0; 
  }

  protected int evaluate() throws XQueryGeneralException
  {
	if (internalValue==0) {// not evaluated
	  Sequence value = getExpression().evaluate(); 
	  Item item= Item.convertSequenceToItem(value);
	
	  XQueryAnyAtomicType atomic=new XQueryAnyAtomicType(); 
	  
	  if (item.isAtom()) {
		if (item.getAtom().isTyped()) {
		  internalValue=item.getAtom().getDecimalValue(); 
		}
	  }
	  
	  //XQueryInteger integer=XQueryInteger.qualify(item);
	  //this.value=integer.
	}
	return 1;
  }
  
  /**
   * @param node
   * @return boolean
   * @roseuid 4110F795028D
   */
  public boolean doFilter(Node node, int siblingPosition) throws XQueryTypeException 
  {
	System.out.println("NodeNumericPredicate is running");
	
	System.out.println("sibling: " + siblingPosition);
	DomTree02 dt = new DomTree02(); 
	//	dt.processNode(node);
	
	if (this.internalValue == siblingPosition) {
	  
	  return true;
	}
	else {
	  return false;
	}

  }
}
