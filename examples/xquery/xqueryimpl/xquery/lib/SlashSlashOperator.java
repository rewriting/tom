//Source file: C:\\document\\codegen\\xquery\\lib\\SlashSlashOperator.java

package xquery.lib;

import org.w3c.dom.*;

import xquery.util.NodeTraversal;
import xquery.util.Collect1;
import xquery.util.Collect2;
import xquery.util.Collect3;
import xquery.util.Collect4;

import xquery.lib.data.Sequence;
import xquery.lib.data.Item;

public class SlashSlashOperator extends PathOperator 
{
   
  /**
   * @roseuid 4110D9CD00FA
   */
  public SlashSlashOperator() 
  {
    super();
  }
   
  /**
   * @param nodetester
   * @param filter
   * @param nextOperator
   * @roseuid 4110D9B2000B
   */
  public SlashSlashOperator(NodeTester tester, NodePredicate filter, PathOperator nextOperator) 
  {
	super(tester, filter, nextOperator);
  }
   
  /**
   * @param axe
   * @param nodetester
   * @param nodefilter
   * @param nextOperator
   * @roseuid 4110B6320125
   */
  public SlashSlashOperator(PathAxe axe, NodeTester nodetester, NodePredicate nodefilter, PathOperator nextOperator) 
  {
	super(axe, nodetester, nodefilter, nextOperator);
    
  }
   
  /**
   * @param obj
   * @return xquery.lib.data.Sequence
   * @roseuid 410E10C303C2
   */
  public Sequence run(Node node) 
  {
	NodeTraversal traversal = new NodeTraversal();
	final Sequence s=new Sequence(); 
	
	Collect1 collect = new Collect1() { 
		public boolean apply(Object t) 
		{ 
		  try {
			if(t instanceof Node) {
			  Node anode = (Node)t; 
			  if (doTest(anode)) {
				s.addAll(runNext(anode));
				return true;
			  }
			} 
			return true; 
		  }
		  catch (XQueryGeneralException e) {
			System.out.println("ERROR: xqueryGeneral exception");
			return false; 
		  }
		} // end apply 
	  }; // end new 
	traversal.genericCollect(node, collect); 

    return s;
  }
   
  /**
   * @param item
   * @return xquery.lib.data.Sequence
   * @roseuid 411016F701FC
   */
  public Sequence run(Item item) 
  {
    return run(item.getNode());
  }
}
