//Source file: C:\\document\\codegen\\xquery\\lib\\data\\Sequence.java

package xquery.lib.data;

import java.util.*;

public class Sequence extends Vector 
{
   
   /**
    * @roseuid 4110B7F400A0
    */
   public Sequence() 
   {
    
   }
   
   /**
    * @param o
    * @return boolean
    * @roseuid 410E2F7B0177
    */
   public boolean add(Object o) 
   {
	 super.add(o);
	 //	 System.out.println("add object");
	 return true;
   }
  
  /**
   * @param s
   * @return xquery.lib.data.Sequence
   * @roseuid 410F88940191
    */
  public Sequence add(Sequence s) 
  {
    super.addAll(s);
	//	System.out.println("add sequence");
	
	return this;
  }
  
  public void print()
  {
	Enumeration enum = this.elements(); 
	while (enum.hasMoreElements()) {
	  System.out.println(enum.nextElement().toString());
	}
  }
}
