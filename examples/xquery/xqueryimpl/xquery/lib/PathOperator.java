//Source file: C:\\document\\codegen\\xquery\\lib\\PathOperator.java

package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.Item;
import xquery.lib.data.Sequence;
import xquery.lib.data.type.XQueryTypeException;



public abstract class PathOperator 
{
  protected NodePredicateList predicateList;
  protected NodeTester tester;
  protected PathOperator nextOperator;
  protected PathAxe axe;

  public PathOperator(PathAxe axe, NodePredicateList predicateList, PathOperator nextOperator) {
	this.axe=axe;
	this.predicateList=predicateList; 
	this.nextOperator=nextOperator; 
	this.tester = NodeKindTester.createDefaultNodeKindTester(); 
  }


  public void setNodeTester(NodeTester tester) 
  {
	this.tester =tester; 
  }

  public void setAxe(PathAxe axe)
  {
	this.axe=axe;
  }

  public void setPredicateList(NodePredicateList predicateList) 
  {
	this.predicateList=predicateList; 
  }

  public void setAxe(PathAxe axe) 
  {
	this.axe = axe; 
  }
  

  public void setTester(NodeTester tester) 
  {
	this.tester = tester; 
  }


  /**
   * @roseuid 4110B63102BE
   */
  public PathOperator() 
  {
	this.predicateList = new NodePredicateList();
	this.tester = new NodeTester();
	this.axe=PathAxe.createChildPathAxe();
	this.nextOperator = null;
  }
   
  /**
   * @param nodetester
   * @param predicateList
   * @param nextOperator
   * @roseuid 410E10F70150
   */
  public PathOperator(NodeTester nodetester, NodePredicateList predicateList, PathOperator nextOperator) 
  {
    this.predicateList = predicateList;
	this.tester = nodetester;
	this.axe=PathAxe.createChildPathAxe();
	this.nextOperator =  nextOperator;
  }
   
  /**
   * @param axe
   * @param nodetester
   * @param predicateList
   * @param nextOperator
   * @roseuid 410E0C0503B2
   */
  public PathOperator(PathAxe axe, NodeTester nodetester, NodePredicateList predicateList, PathOperator nextOperator) 
  {
    this.predicateList = predicateList;
	this.tester = nodetester;
	this.axe=axe;
	this.nextOperator =  nextOperator;
  }
   
  /**
   * @param item
   * @return boolean
   * @roseuid 410E0BCC0021
   */
  protected boolean doTest(Item item) 
  {
    return doTest(item.getNode());
  }
   
  /**
   * @param node
   * @return xquery.lib.data.Sequence
   * @throws xquery.lib.data.type.XQueryTypeException
   * @roseuid 410E0BD40235
   */
  protected Sequence runNext(Node node) throws XQueryTypeException 
  {
	if (this.nextOperator ==null) {
	  System.out.println("runNext - Add node to Sequence");
	  
	  Sequence seq = new Sequence();
	  seq.add(node);
	  return seq;
	}
	else {
	  return nextOperator.run(node);
	}
  }
   
  /**
   * @param item 
   * @return boolean
   * @roseuid 410E0BDC038B
   */
  protected boolean doFilter(Item item, int siblingPosition) 
	throws XQueryTypeException
  {
	if (item.isNode())
	  return doFilter(item.getNode(), siblingPosition);
	else 
	  return false;
  }

  /**
   * @param node 
   * @return boolean
   * @roseuid 
   */
  protected boolean doFilter(Node node, int siblingPosition) 
	throws XQueryTypeException
  {
    return filter.doFilter(node, siblingPosition);
  }
   
  /**
   * @param node
   * @return xquery.lib.data.Sequence
   * @throws xquery.lib.data.type.XQueryTypeException
   * @roseuid 410E0DB30343
   */
  public Sequence run(Item item) throws XQueryTypeException 
  {
	if (item.isNode()) {
	  return run(item.getNode());
	}
	else {
	  return null;
	}
  }
  
  /**
   * @param node
   * @return xquery.lib.data.Sequence
   * @throws xquery.lib.data.type.XQueryTypeException
   */
  public Sequence runChild(Node node) throws XQueryTypeException 
  {
	Sequence seq=new Sequence();
	if (node.hasChildNodes()) {
	  System.out.println("deo hieu the nao, co child ro rang kia ma");

	  NodeList nodelist=node.getChildNodes();
	  for(int i=0;i < nodelist.getLength(); i++) {
		System.out.println("i: " + i);
		
		Node childnode = nodelist.item(i);
		if (doTest(childnode) && doFilter(childnode, i)) {  // i is current position in child list
		  seq.add(runNext(childnode));
		}
	  }
	} 

	return seq;
  }

  /**
   * @param node
   * @return xquery.lib.data.Sequence
   * @throws xquery.lib.data.type.XQueryTypeException
   */
  protected Sequence runParent(Node node) throws XQueryTypeException 
  {
	Node parent = node.getParentNode();
	if (parent ==null) {
	  return new Sequence();
	}
	else {
	  if (doTest(parent) && doFilter(parent, 0)) { // O: one SELF NODE only
		return runNext(parent);
	  }
	  else {
		return new Sequence();
	  }
	}
  }

  /**
   * @param node
   * @return xquery.lib.data.Sequence
   * @throws xquery.lib.data.type.XQueryTypeException
   */
  protected Sequence runSelf(Node node) throws XQueryTypeException 
  {
	System.out.println("runself - PathOperator");
	
	if (doTest(node) && doFilter(node, 0)) {  // 0: one SELF node only
	  System.out.println("runSelf-PathOperator return true");
	  
	  Sequence result = runNext(node);
	  System.out.println(result.size());
	  return result;
	}
	else {
	  return new Sequence();
	}
  }

  /**
   * @param node
   * @return xquery.lib.data.Sequence
   * @throws xquery.lib.data.type.XQueryTypeException
   */
  protected Sequence runAttribute(Node node) throws XQueryTypeException 
  {
	System.out.println("run attribute");
	Sequence seq=new Sequence();
	if (node.hasAttributes()) {
	  System.out.println("deo hieu the nao, co attribute ro rang kia ma");
	  
	  NamedNodeMap nodelist=node.getAttributes();
	  for(int i=0;i < nodelist.getLength(); i++) {
		Node childnode = nodelist.item(i);
		if (doTest(childnode) && doFilter(childnode, i)) {
		  System.out.println("runAttribute-PathOperator return true");
		  seq.add(runNext(childnode));
		}
	  }
	} 

	return seq;
  }

 
  /**
   * @param node
   * @return xquery.lib.data.Sequence
   * @throws xquery.lib.data.type.XQueryTypeException
   */
  public Sequence run(Node node) throws XQueryTypeException 
  {
	System.out.println("run in PathOperator");
	
    int axeType = axe.getAxeType();
	switch (axeType) {
	case PathAxe.CHILD:
	  return runChild(node);
	case PathAxe.ATTRIBUTE:
	  return runAttribute(node);
	case PathAxe.PARENT:
	  return runParent(node);
	case PathAxe.SELF:
	  return runSelf(node);
	default:
	  return new Sequence();
	}
    
  }
   
  /**
   * @param node
   * @return boolean
   * @roseuid 4118034D0257
   */
  protected boolean doTest(Node node) 
  {
    return tester.doTest(node);
  }
}
