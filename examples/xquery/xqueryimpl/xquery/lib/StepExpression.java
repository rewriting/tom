/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the Inria nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.Item;
import xquery.lib.data.Sequence;
import xquery.lib.data.type.XQueryTypeException;





// need to ajoute one 

public class StepExpression extends AbstractExpression {

  public StepExpression(Object child) 
  {
	super(child);
  }

  public StepExpression(Object child1, Object child2) 
  {
	super(child1,child2);
  }

  public StepExpression(Object child1, Object child2, Object child3) 
  {
	super(child1,child2,child3);
  }


  public StepExpression(Object child1, Object child2, Object child3, Object child4) 
  {
	super(child1,child2,child3,child4);
  }


  // first option; 
  PathOperator pathOperator = null; 
  PathAxe pathAxe = null; 
  NodeTester nodeTest = null; 

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
	pathOperator = (PathOperator)(options[0]); 
	pathAxe = (PathAxe)(options[1]);
	nodeTest= (NodeTester)(options[2]);
  }

  public StepExpression( PathOperator pathOperator, PathAxe pathAxe, NodeTester nodeTest,Object childExprs[]) 
  {
	super(childExprs); 

	this.pathOperator = pathOperator; 
	this.pathAxe = pathAxe;
	this.nodeTest=nodeTest;
  }

  public StepExpression(PathOperator pathOperator, PathAxe pathAxe, NodeTester nodeTest) 
  {
	super(); 

	this.pathOperator = pathOperator; 
	this.pathAxe = pathAxe;
	this.nodeTest=nodeTest;
  }


  public StepExpression(PathOperator pathOperator, NodeTester nodeTest,Object childExprs[]) 
  {
	super(childExprs); 

	this.pathOperator = pathOperator; 
	this.pathAxe = PathAxe.createChildPathAxe();
	this.nodeTest=nodeTest;
  }

  public StepExpression(PathOperator pathOperator, NodeTester nodeTest) 
  {
	super(); 

	this.pathOperator = pathOperator; 
	this.pathAxe = PathAxe.createChildPathAxe();
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
// 	else if (!(getChild(0) instanceof Sequence)) {
// 	  return false; 
// 	}
	 
	else if ((pathOperator ==null)
		|| (pathAxe==null)
		|| (nodeTest==null)) {
	  return false; 
	}
	else {
	  return true;
	}

	
  }

  
  protected Sequence doFilter(Sequence inputValue, int childIndex)
	throws XQueryGeneralException
  {
	NodePredicateList predicateList = new NodePredicateList();

	for (int i=childIndex; i < getArity(); i++) {
	  predicateList.add(getChild(i));
	}

	//	System.out.println("StepOperator: doFilter: " + ((NodeNameTester)nodeTest).getName());
	
	pathOperator.setPathAxe(pathAxe); 
	pathOperator.setNodeTest(nodeTest);
	pathOperator.setPredicateList(predicateList);
	Sequence result = pathOperator.run(inputValue);
	//	System.out.println("StepExpr: doFilter: result:" + result.size());
	return result;
  }
  

  
  public Sequence evaluate() 
	throws XQueryGeneralException
  {
	return evaluate(new Sequence());
  }


  public Sequence evaluate(Sequence initValue) 
	throws XQueryGeneralException
  {

	// verify child expressions
	if (!verifyContent()) {
	  return null;
	}
	
	Sequence result = new Sequence(); 
	result.add(initValue);

	//	System.out.println("StepExpr: evaluate: result:" + result.size());
	
	Object secondChild = getChild(0); 
	int childIndex = 0; 

	if (secondChild instanceof AbstractExpression) { // union
	  //	  System.out.println("first child is an Expression");
	  
	  result.add(((AbstractExpression)secondChild).evaluate()); 
	  childIndex = 1;
	}
// 	else {
// 	  System.out.println("Khong phai dau");
	  
// 	}

	//	System.out.println("StepExpr: evaluate: result:" + result.size());
	result= doFilter(result, childIndex);
	
	return result;
  }
}
