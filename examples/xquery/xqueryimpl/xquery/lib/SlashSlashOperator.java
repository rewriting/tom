

//Source file: C:\\document\\codegen\\xquery\\lib\\SlashSlashOperator.java

package xquery.lib;

import org.w3c.dom.*;

import java.util.*;

import xquery.util.NodeTraversal;
import xquery.util.Collect1;
import xquery.util.Collect2;
import xquery.util.Collect3;
import xquery.util.Collect4;

import xquery.lib.data.Sequence;
import xquery.lib.data.Item;
import xquery.lib.data.type.*;



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
  public SlashSlashOperator(NodeTester tester, NodePredicateList predicateList) 
  {
	super(tester, predicateList);
  }
   
  /**
   * @param axe
   * @param nodetester
   * @param nodefilter
   * @param nextOperator
   * @roseuid 4110B6320125
   */
  public SlashSlashOperator(PathAxe axe, NodeTester nodetester, NodePredicateList predicateList) 
  {
	super(axe, nodetester, predicateList);    
  }

  public Sequence run(Sequence input) throws XQueryTypeException 
  {
	Sequence result = new Sequence();
	Enumeration enum=input.elements(); 
	
	while (enum.hasMoreElements()) {
	  Object obj=enum.nextElement(); 
	  if (obj instanceof Node) {
		result.add(run((Node)obj));
	  }
	  else if (obj instanceof Item) {
		result.add(run(((Item)obj).getNode()));
	  }
	}
	
	return result;
  }
   
  /**
   * @param obj
   * @return xquery.lib.data.Sequence
   * @roseuid 410E10C303C2
   */
  public Sequence run(Node node) 
  {
	//	System.out.println("SlashSlashOperator: run Node: ");
	
	NodeTraversal traversal = new NodeTraversal();
	final Sequence s=new Sequence(); 
	//	int index=1;   // predicate base 1, not 0
	
	Collect1 collect = new Collect1() { 
 		int index=1;
		public boolean apply(Object t) 
		{ 
		  try {
			if(t instanceof Node) {
			  Node anode = (Node)t; 
			  // 			  if (anode.getNodeName().compareTo("book") ==0)
			  // 				System.out.println("Testing BOOK");
			  if (doTest(anode) && doFilter(anode, index)) {
				s.add(anode);
				//				System.out.println("Test OK");
				// 				System.out.println("index:"+index);
 				index++;
				return true; // continue
			  }
			  // 			  else {
			  // 				System.out.println("Test Failed");
			  
			  // 			  }
			  
			} 
			return true;  // continue
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
	if (item.isNode()) {
	  return run(item.getNode());
	}
	else {
	  return new Sequence();
	}
  }

}
