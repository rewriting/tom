//Source file: C:\\document\\codegen\\xquery\\lib\\NodePredicate.java

package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.Item;
import xquery.lib.data.type.XQueryTypeException;

public class NodePredicate 
{
  Expression expr=null;  
   
  /**
   * @roseuid 4110B63100D4
   */
  public NodePredicate() 
  {
    this.expr=null;
  }
   
  public Expression getExpression()
  {
	return this.expr;
  }

  public NodePredicate(Expression expr) 
  {
	this.expr=expr; 
  }

  /**
   * @param item
   * @return boolean
   * @throws xquery.lib.data.type.XQueryTypeException
   * @roseuid 410E14390376
   */
  public boolean doFilter(Item item, int siblingPosition) throws XQueryTypeException 
  {
	if (item.isNode())
	  return doFilter(item.getNode(), siblingPosition);
	else 
	  return false;		
  }
   
  /**
   * @param node
   * @return boolean
   * @throws xquery.lib.data.type.XQueryTypeException
   * @roseuid 410FA6320298
   */
  public boolean doFilter(Node node, int siblingPosition) throws XQueryTypeException 
  {
    return true;
  }
  
}
