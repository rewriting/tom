

package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.Item;
import xquery.lib.data.Sequence;
import xquery.lib.data.type.XQueryTypeException;




// need to ajoute one 

public class StepExpresstion extends Expression {

  PathOperator pathOperator = null; 

  // eval:     -> need ajout "sequence" (may be EMPTY)  before 1st part

  // StepExpr: -> Expr
  //           -> [Predicate * ]  
  // eval:     -> (sequence.eval() UNION Expr.eval()) filter Predicate * 

  // StepExpr: -> Variable
  //           -> [Predicate* ]
  // eval:     -> (sequence.eval() UNION Variable.eval()) filter Predicate * 

  // StepExpr: -> (QName | NodeKind)
  //           -> [Predicate* ]
  // eval      -> sequence.eval() filter (QName | NodeKind) filter Predicate *

  protected StepExpression()
  {
	super();  
  }


  public StepExpression(Object childExprs[]) 
  {
	super(childExprs); 
  }
  

  public StepExpression(int childCount) 
  {
	super(childCount);
  }


  // StepExpr cannot contient PathExpr
  protected boolean verifyContent() 
  {
	if (!super.verifyContent()) {
	  return false; 
	}

	// 1st must be sequence 
	if (!(getChild(0) instanceof Sequence)) {
	  return false; 
	}
	// 
	
  }

  protected Sequence 

  public Sequence evaluate() 
  {
	Sequence result = getInitialValue(); 
	
	Object secondChild = getChild(1); 
	if (secondChild instanceof AbstractExpression) { // union
	  result.add(secondChild.evaluate()); 
	}
	
	doFilter(result);
  }
}
