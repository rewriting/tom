

package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.Item;
import xquery.lib.data.Sequence;
import xquery.lib.data.type.XQueryTypeException;



public class PathExpresstion extends Expression{

  // PathExpr -> [Var]
  //          -> [StepExpr*]

  // PathExpr -> [Var]
  //          -> [Expr*]



  public boolean verifyContent()
  {
	if (!super.verifyContent()) {
	  return false; 
	}
	// do something there
	return true;
  }
  

  // filter 1 -> end
  public Sequence evaluate() 
  {
	// verify child expressions
	if (!verifyContent()) {
	  return null;
	}
	
	Sequence result = getInitialValue(); 
	// 

	Object secondChild = getChild(1); 
	int childIndex = 1; 
	if (secondChild instanceof AbstractExpression) { // union
	  result.add(((AbstractExpression)secondChild).evaluate()); 
	  childIndex = 2; 
	}

	return result;
  }
  
  
  protected Sequence doFilter(Sequence input, int childIndex)
  {
	// filter; by expression
	for (int i=childIndex; i< getArity(); i++) {
	  AbstractExpression expr=(AbstractExpression)getChild(childIndex); 
	  // assign initial value
	  expr.setInitialValue(input); 
	  input=expr.evaluate();
	}
  }
}
