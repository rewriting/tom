package xquery.util; 

import java.util.*; 

public class Sequence extends Vector 
{

  public void print()
  {
	Enumeration enum = this.elements(); 
	while (enum.hasMoreElements()) {
	  System.out.println(enum.nextElement().toString());
	}
  }
  
}