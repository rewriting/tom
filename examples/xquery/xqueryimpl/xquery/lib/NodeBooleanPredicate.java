//Source file: C:\\document\\codegen\\xquery\\lib\\NodeBooleanPredicate.java

package xquery.lib;


import org.w3c.dom.*;

import xquery.lib.data.Item;

public class NodeBooleanPredicate extends NodePredicate 
{
   
   /**
    * @roseuid 4110FC290102
    */
   public NodeBooleanPredicate() 
   {
    
   }
   
   /**
    * @param next
    * @roseuid 4110F9120119
    */
   public NodeBooleanPredicate(NodePredicate next) 
   {
    
   }
   
   /**
    * @param node
    * @return boolean
    * @roseuid 4110F79A0399
    */
   public boolean doFilter(Node node) 
   {
    return true;
   }
   
   /**
    * @param item
    * @return boolean
    * @roseuid 4110F7E402FF
    */
   public boolean doFilter(Item item) 
   {
    return true;
   }
}
