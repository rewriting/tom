

package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.Item;
import xquery.lib.data.Sequence;
import xquery.lib.data.type.XQueryTypeException;



public class ConstantExpression extends AbstractExpression{

  protected ConstantExpression()
  {
	this.childs = null; 
	this.options = null; 
  }


    // arity must be at least 1
  public ConstantExpression(int arity) 
  {
	super(arity);
  }

  public ConstantExpression(Sequence seq)
  {
	super(seq);
  }

  public ConstantExpression(Node node)
  {
	super(node);
  }  

  protected boolean verifyContent() 
  {
	if (!super.verifyContent()) {
	  return false;
	}

	if (!(childs[0] instanceof Sequence) 
		|| !(childs[0] instanceof Node) 
		|| !(childs[0] instanceof Item)){
	  return false; 
	}
	else {
	  return true; 
	}
	  
  }

  public Sequence evaluate() 
	throws XQueryGeneralException
  {
	if (!verifyContent()) {
	  return null;
	}

	if (childs[0] instanceof Sequence) {
	  return (Sequence)(childs[0]);
	}
	else if (childs[0] instanceof Item) {
	  return (Sequence)(childs[0]);
	}
	else { //if (childs[0] instanceof Node) {
	  Sequence r = new Sequence(); 
	  r.add(childs[0]);
	  return r; 
	}
  }
}