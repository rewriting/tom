

package xquery.lib; 

import xquery.lib.data.*;

public class Expression extends AbstractExpression{



  // Expr  -> PathExpr* 
  // eval:  UNION ALL (pathexpr.eval())
  
  protected Expression()
  {
	super();  
  }


  public Expression(Object childExprs[]) 
  {
	super(childExprs); 
  }
  

  public Expression(int childCount) 
  {
	super(childCount);
  }



  protected boolean verifyContent()
  {
	for (int i=0;i < getArity(); i++) {
	  if (childs[i]==null) {
		return false; 
	  }
	}
	return true; 
  }

  
  // return null if one child is null
  // return empty sequence if no result
  // return sequence if do have result
  

  public Sequence evaluate() throws XQueryGeneralException
  {
	// verify child expressions
	if (!verifyContent()) {
	  return null;
	}

	// default: UNION alls:
	Sequence s = getInitialValue(); 

	for (int i=1 ;i < getArity(); i++) { // 0 is initial value
	  Object achild = getChild(i);
	  if ((achild instanceof Sequence)
		  || (achild instanceof Item)) {
		s.add(achild); 
	  }
	  else if (achild instanceof AbstractExpression) {
		s.add(achild.evaluate()); 
	  }

	  else if (achild instanceof Node) {
		// create Item from this node, evaluate and add to sequence 
		
	  }
	  else if (achild instanceof Atom) {
		// create Item from this node, evaluate and add to sequence 
	  }

	}

	return s;
  }

}