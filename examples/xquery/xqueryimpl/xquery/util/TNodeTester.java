package xquery.util; 


import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;


public class TNodeTester {
  
  protected Object objects[] = null;

  public TNodeTester()
  {
	this.objects=null;
  }
  
  public TNodeTester(Object objects[])
  {
	this.objects = new TNode[objects.length];
	for (int i=0; i<objects.length; i++) {
	  this.objects[i]=objects[i];
	}
  }

  public Object doTest (Object obj)
	throws XQueryGeneralException
  {return obj;}


  public void setObject(Object objs[]) 
  {
	this.objects = new TNode[objects.length];
	for (int i=0; i<objects.length; i++) {
	  this.objects[i]=objects[i];
	}
  }
}
