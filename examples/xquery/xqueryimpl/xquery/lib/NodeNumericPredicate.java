//Source file: C:\\document\\codegen\\xquery\\lib\\NodeNumericPredicate.java

package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.type.*;
import xquery.lib.data.Item;

import xquery.util.DomTree02;

public class NodeNumericPredicate extends NodePredicate 
{
   
  protected int value; 
  
  /**
   * @roseuid 4110FBD60026
   */
  public NodeNumericPredicate(int value) 
  {
    super();
	this.value = value;
  }
   
  /**
   * @param next
   * @roseuid 4110F8FE0337
   */
  public NodeNumericPredicate(NodePredicate next) 
  {
    super(next);
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
