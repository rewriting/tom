//Source file: C:\\document\\codegen\\xquery\\lib\\NodeNameTester.java

package xquery.lib;


import org.w3c.dom.*;

import xquery.lib.data.type.String;
import xquery.lib.data.Item;

public class NodeNameTester extends NodeTester 
{
  private String name;
   
  /**
   * @roseuid 4110B6310052
   */
  public NodeNameTester() 
  {
    
  }
   
  /**
   * @param name
   * @roseuid 410FA88F02D4
   */
  public NodeNameTester(String name) 
  {
    
  }
   
  /**
   * @return xquery.lib.data.type.String
   * @roseuid 410E136B006D
   */
  protected String getName() 
  {
    return null;
  }
   
  /**
   * @param item
   * @return boolean
   * @roseuid 410F9E67013A
   */
  public boolean doTest(Item item) 
  {
    return true;
  }
   
  /**
   * @param node
   * @return boolean
   * @roseuid 410FA6A30308
   */
  public boolean doTest(Node node) 
  {
    return true;
  }
   
  /**
   * @param name
   * @roseuid 411015CA004A
   */
  public void setName(String name) 
  {
    
  }
}
