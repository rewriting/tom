

package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.Item;
import xquery.lib.data.Sequence;
import xquery.lib.data.type.XQueryTypeException;



public class PathExpression extends AbstractExpression{

  // PathExpr -> [Var]
  //          -> [StepExpr*]

  // PathExpr -> [Var]
  //          -> [Expr*]

  public PathExpression(Object child) 
  {
	super(child);
  }

  public PathExpression(Object child1, Object child2) 
  {
	super(child1,child2);
  }

  public PathExpression(Object child1, Object child2, Object child3) 
  {
	super(child1,child2,child3);
  }


  public PathExpression(Object child1, Object child2, Object child3, Object child4) 
  {
	super(child1,child2,child3,child4);
  }



  protected PathExpression()
  {
	super();  
  }


  public PathExpression(Object childExprs[], Object options[]) 
  {
	super(childExprs,options); 
  }



  public PathExpression(Object childExprs[]) 
  {
	super(childExprs); 
  }
  

  public PathExpression(int childCount) 
  {
	super(childCount);
  }




  public boolean verifyContent()
  {
	if (!super.verifyContent()) {
	  return false; 
	}
	// do something there
	return true;
  }


  public Sequence evaluate() 
	throws XQueryGeneralException
  {
	return evaluate(new Sequence());
  }  

  // filter 1 -> end
  public Sequence evaluate(Sequence initValue) 
	throws XQueryGeneralException
  {
	// verify child expressions
	if (!verifyContent()) {
	  return null;
	}
	
	Sequence result = new Sequence(); 
	result.addAll(initValue); 
	// 


	Object secondChild = getChild(0); 
	int childIndex = 0; 
	if (secondChild instanceof AbstractExpression) { // union
	  result.add(((AbstractExpression)secondChild).evaluate()); 
	  childIndex ++; 
	}
	
// 	System.out.println("PathExpr: evaluate: result:" + result.size());
	result = doFilter(result, childIndex);
// 	System.out.println("PathExpr: evaluate: result:" + result.size());
	

	return result;
  }
  
  
  protected Sequence doFilter(Sequence input, int childIndex)
	throws XQueryGeneralException
  {
	SequenceTool st=new SequenceTool(); 

	// filter; by expression
	Sequence result = new Sequence(); 
	result.add(input);
// 	System.out.println("PathExpr: doFilter: result:" + result.size());

	for (int i=childIndex; i< getArity(); i++) {
	  AbstractExpression expr=(AbstractExpression)getChild(i); 
	  // assign initial value
	  //Sequence s = expr.evaluate(result);
	  result = expr.evaluate(result);
// 	  System.out.println("i="+i);
// 	  System.out.println("PathExpr: doFilter: result:" + result.size());
	  
	  if (result==null) {
		return null; 
	  }

	  result = st.removeDuplicated(result);
	}

// 	System.out.println("PathExpr: doFilter: result:" + result.size());
	return result;
  }
}
