

package xquery.lib; 

import xquery.lib.data.*;

public class Expression {
  protected Object [] childs; 
  
  
  protected Expression()
  {
	this.childs = null; 
  }



  public Expression(Object child) 
  {
	this.childs = new Object[1];
  }


  public Expression(Object childExprs[]) 
  {
	childs = new Object[childExprs.length];
	for (int i=0;i < childs.length; i++) {
	  childs[i]=childExprs[i];
	}
  }
  


  public boolean setChild(Object child, int position) 
  {
	if (position > objects.length - 1) 
	  return false;
	else {
	  childs[position] = child;
	}
  }


  public int getArity() 
  {
	return objects.length; 
  }



  public Object getChild(int position) 
  {
	if (position > objects.length - 1) 
	  return null;
	else 
	  return objects[position];
  }



  public Sequence evaluate() throws XQueryGeneralException
  {
	// default: UNION alls:
	Sequence s = new Sequence(); 
	for (int i=0;i < childs.length; i++) {
	  Object achild = objects[i];
	  if ((achild instanceof Sequence)
		  || (achild instanceof Item)
		  || (achild instanceof Expression)) {
		s.add(childs[i].evaluate()); 
	  }
	  else if (achild instanceof Node) {
		// create Item from this node, evaluate and add to sequence 
		
	  }
	  else if (achild instanceof Atom) {
		// create Item from this node, evaluate and add to sequence 
	  }
	}
  }



}