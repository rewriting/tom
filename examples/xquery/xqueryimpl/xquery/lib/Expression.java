

package xquery.lib; 

import xquery.lib.data.*;

import org.w3c.dom.*;

public class Expression extends AbstractExpression{
  // Expr  -> PathExpr* 
  // eval:  UNION ALL (pathexpr.eval())

  public  Expression(Object child) 
  {
	super(child);
  }

  public  Expression(Object child1, Object child2) 
  {
	super(child1,child2);
  }

  public  Expression(Object child1, Object child2, Object child3) 
  {
	super(child1,child2,child3);
  }


  public  Expression(Object child1, Object child2, Object child3, Object child4) 
  {
	super(child1,child2,child3,child4);
  }
  
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

  


  public Sequence evaluate() throws XQueryGeneralException
  {
	return evaluate(new Sequence());
  }
  // return null if one child is null
  // return empty sequence if no result
  // return sequence if do have result
  

  public Sequence evaluate(Sequence initialValue) throws XQueryGeneralException

  {
	// verify child expressions
	if (!verifyContent()) {
	  return null;
	}
	
	// default: UNION alls:
	Sequence result = new Sequence(); 
	result.addAll(initialValue);
	//	System.out.println("Expr: evaluate: result:" + result.size());

	for (int i=0 ;i < getArity(); i++) { // 0 is initial value
	  Object achild = getChild(i);
	  if ((achild instanceof Sequence)
		  || (achild instanceof Item)) {
		result.add(achild); 
	  }

	  else if (achild instanceof AbstractExpression) {
		AbstractExpression expr=(AbstractExpression)achild; 
		//		Sequence result = expr.evaluate(s);
		result= expr.evaluate(result);

		//		System.out.println("Expr: evaluate: result:" + result.size());
		
		if (result==null) {
		  return null; 
		}
		else {
		  //	  s.add(result); 
		}
	  }

	  else if (achild instanceof Node) {
		// create Item from this node, evaluate and add to sequence 
		
	  }
	  else if (achild instanceof Atom) {
		// create Item from this node, evaluate and add to sequence 
	  }

	}
	return result;
  }

}