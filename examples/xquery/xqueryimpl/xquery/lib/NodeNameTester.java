//Source file: C:\\document\\codegen\\xquery\\lib\\NodeNameTester.java

package xquery.lib;


import org.w3c.dom.*;

import xquery.lib.data.type.*;
import xquery.lib.data.Item;

// wild card can be implemented in this class
// *
// prefix:*
// *:localname:  any namespace, 

public class NodeNameTester extends NodeTester 
{
  private String name;
   
  /**
   * @roseuid 4110B6310052
   */
  public NodeNameTester() 
  {
	super(); 
  }
   
  /**
   * @param name
   * @roseuid 410FA88F02D4
   */
  public NodeNameTester(String name) 
  {
	super(); 
	System.out.println("sao the nhi chang chay gi ca");
	
	this.name=new String(name);
	System.out.println("name");
	
	System.out.println(name);
	
  }
   
  /**
   * @return xquery.lib.data.type.String
   * @roseuid 410E136B006D
   */
  protected String getName() 
  {
    return name;
  }
   
  /**
   * @param item
   * @return boolean
   * @roseuid 410F9E67013A
   */
  public boolean doTest(Item item) 
  {
    if (item.isNode()) {
	  return doTest(item.getNode());
	}
	else {
	  return false;
	}
  }
   
  /**
   * @param node
   * @return boolean
   * @roseuid 410FA6A30308
   */
  public boolean doTest(Node node) 
  {
	System.out.println(name);
	
    if (node instanceof Element && node.getNodeName().compareTo(name)==0) {
	  return true; 
	}
	else {
	  return false;
	}
  }
   
  /**
   * @param name
   * @roseuid 411015CA004A
   */
  public void setName(String name) 
  {
	this.name=name;
  }
}
