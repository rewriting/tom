//Source file: C:\\document\\codegen\\xquery\\lib\\SlashOperator.java

package xquery.lib;

import xquery.lib.data.Sequence;
import xquery.lib.data.Item;

import org.w3c.dom.*;
import xquery.lib.data.type.*;

public class SlashOperator extends PathOperator 
{
   
  /**
   * @roseuid 4110D94E030A
   */
  public SlashOperator() 
  {
	super();
  }
   
  /**
   * @param nodetester
   * @param filter
   * @param nextOperator
   * @roseuid 4110D8F003CD
   */
  public SlashOperator(NodeTester nodetester, NodePredicate filter, PathOperator nextOperator) 
  {
	super(nodetester, filter, nextOperator);
  }
   
  /**
   * @param axe
   * @param nodetester
   * @param nodefilter
   * @param nextOperator
   * @roseuid 4110B632008F
   */
  public SlashOperator(PathAxe axe, NodeTester nodetester, NodePredicate nodefilter, PathOperator nextOperator) 
  {
	super(axe, nodetester, nodefilter, nextOperator);
  }
   
  /**
   * @param obj
   * @return xquery.lib.data.Sequence
   * @roseuid 410E10B60124
   */
  public Sequence run(Node node) throws XQueryTypeException 
  {
    return super.run(node);
  }
   
}
