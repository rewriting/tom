
package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.Item;
import xquery.lib.data.Sequence;
import xquery.lib.data.type.XQueryTypeException;

import xquery.lib.data.*;


public abstract class AbstractExpression {

  protected Object [] childs; 
  protected Object [] options; 

  protected AbstractExpression()
  {
	this.childs = new Object[0]; 
	this.options = new Object[0]; 
  }

  
  public AbstractExpression(Object child) 
  {
	this(1);
	childs[0]=child; 
  }

  public AbstractExpression(Object child1, Object child2) 
  {
	this(2);
	childs[0]=child1; 
	childs[1]=child2; 
  }

  public AbstractExpression(Object child1, Object child2, Object child3) 
  {
	this(3);
	childs[0]=child1; 
	childs[1]=child2; 
	childs[2]=child3; 
  }


  public AbstractExpression(Object child1, Object child2, Object child3, Object child4) 
  {
	this(4);
	childs[0]=child1; 
	childs[1]=child2; 
	childs[2]=child3; 
	childs[3]=child4; 
  }


  public AbstractExpression(Object childExprs[]) 
  {
	childs = new Object[childExprs.length]; 
	for (int i=0;i < childs.length; i++) {
	  childs[i]=childExprs[i];
	}
  }


  public AbstractExpression(Object childExprs[], Object options[]) 
  {
	childs = new Object[childExprs.length]; 
	for (int i=0;i < childs.length; i++) {
	  childs[i]=childExprs[i];
	}

	this.options = new Object[options.length]; 
	for (int i=0;i < childs.length; i++) {
	  this.options[i]=options[i];
	}
  }
  

  // arity must be at least 1
  public AbstractExpression(int arity) 
  {
	childs = new Object[arity];
	for (int i=0;i < childs.length; i++) {
	  childs[i]=null;
	}
  }


  protected boolean verifyContent() 
  {
	for(int i=0; i<childs.length; i++) {
	  if (childs[i]==null) {
		return false; 
	  }
	}
	return true;
  }


//   public Object getOption(int position) 
//   {
// 	if (position > options.length - 1) 
// 	  return null;
// 	else 
// 	  return options[position];
//   }

//   public Object getOption() 
//   {
// 	return getOption(0);
//   }


  

  public int getArity() 
  {
	return childs.length; 
  }


  public Object getChild(int position) 
  {
	if (position > childs.length - 1) 
	  return null;
	else 
	  return childs[position];
  }



  public boolean setChild(Object child, int position) 
  {
	if (position > childs.length - 1) 
	  return false;
	else {
	  childs[position] = child;
	  return true;
	}
  }

//   public boolean setInitialValue(Sequence seq) 
//   {
// 	return setChild(seq, 0); 
//   }

//   public boolean setInitialValue(Atom atom) 
//   {
// 	return setChild(new Item(atom), 0); 
//   }
  
//   public boolean setInitialValue(Node node) 
//   {
// 	return setChild(new Item(node),0);
//   }

//   protected Sequence getInitialValue() 
//   {
// 	return (Sequence)(childs[0]); 
//   }


  public Sequence evaluate() 
	throws XQueryGeneralException 
  {
	if (verifyContent())
	  return new Sequence();
	else 
	  return null;
  }

  public Sequence evaluate(Sequence initialValue) 
	throws XQueryGeneralException 
  {
	if (verifyContent())
	  return initialValue;
	else 
	  return null;
  }

  public Sequence avaluate(Atom atom)
	throws XQueryGeneralException 
  {
	if (verifyContent()) {
	  Sequence s =new Sequence();
	  s.add(new Item(atom));
	  return s;
	}
	else 
	  return null;
  }
}
