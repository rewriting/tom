

package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.Item;
import xquery.lib.data.Sequence;
import xquery.lib.data.type.XQueryTypeException;




// need to ajoute one 

public class StepExpresstion extends AbstractExpression {

  // first option; 
  PathOperator pathOperator = null; 
  PathAxe pathAxe = null; 
  NodeTest nodeTest = null; 

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


  public StepExpression(Object childExprs[], Object options[]) 
  {
	super(childExprs); 
	pathOperator = options[0]; 
	pathAxe = options[1];
	nodeTest=options[2];
  }

  public StepExpression(Object childExprs[], PathOperator pathOperator, PathAxe pathAxe, NodeTest nodeTest) 
  {
	super(childExprs); 

	this.pathOperator = pathOperator; 
	this.pathAxe = pathAxe;
	this.nodeTest=nodeTest;
  }


  public StepExpression(Object childExprs[]) 
  {
	super(childExprs); 
	pathOperator = null; 
	pathAxe = null;
	nodeTest=null;
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
	if ((pathOperator ==null)
		|| (pathAxe==null)
		|| (nodeTest==null)) {
	  return false; 
	}	
  }

  
  protected Sequence doFilter(Sequence inputValue, int childIndex)
  {
	NodePredicateList predicateList = new NodePredicateList();

	for (int i=childIndex; i < getArity(); i++) {
	  predicateList.add(getChild(i));
	}

	pathOperator.setPathAxe(pathAxe); 
	pathOperator.setNodeTest(nodeTest);
	pathOperator.setNodePredicateList(predicateList);

	return pathOperator.run(inputValue);
  }
  

  public Sequence evaluate() 
  {
	// verify child expressions
	if (!verifyContent()) {
	  return null;
	}

	
	Sequence result = getInitialValue(); 
	
	Object secondChild = getChild(1); 
	int childIndex = 1; 

	if (secondChild instanceof AbstractExpression) { // union
	  result.add(((AbstractExpression)secondChild).evaluate()); 
	  childIndex = 2;
	}
	
	return doFilter(result, childIndex);
  }
}
