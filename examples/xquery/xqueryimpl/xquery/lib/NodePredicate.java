//Source file: C:\\document\\codegen\\xquery\\lib\\NodePredicate.java

package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.Item;
import xquery.lib.data.type.XQueryTypeException;

public class NodePredicate 
{
  protected NodePredicate nextPredicate;
   
  /**
   * @param next
   * @roseuid 4110F8ED03BE
   */
  public NodePredicate(NodePredicate next) 
  {
	this.nextPredicate = next; 
  }
   
  /**
   * @roseuid 4110B63100D4
   */
  public NodePredicate() 
  {
    this.nextPredicate=null;
  }
   

  public boolean hasNextPredicate() 
  {
	if (nextPredicate !=null) 
	  return true;
	else 
	  return false;
  }

  public NodePredicate getNextPredicate()
  {
	return nextPredicate; 
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
   
  /**
   * @param next
   * @return boolean
   * @roseuid 4110F419028B
   */
  public boolean setNextPredicate(NodePredicate next) 
  {
	this.nextPredicate = next; 
    return true;
  }
   
  /**
   * @param pre
   * @return boolean
   * @roseuid 4110F42E029F
   */
  public boolean addPredicateToPredicateChain(NodePredicate pre) 
  {
	NodePredicate next = nextPredicate; 
	while (next.hasNextPredicate()) {
	  next=next.getNextPredicate();
	}
	next.setNextPredicate(pre);
	return true;
  }
}
