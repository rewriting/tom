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
//Source file: C:\\document\\codegen\\xquery\\lib\\PathOperator.java

package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.Item;
import xquery.lib.data.Sequence;
import xquery.lib.data.type.XQueryTypeException;



public abstract class PathOperator 
{
  protected NodePredicateList predicateList;
  protected NodeTester tester;
  //  protected PathOperator nextOperator;
  protected PathAxe axe;

  public PathOperator(PathAxe axe, NodePredicateList predicateList) {
	this.axe=axe;
	this.predicateList=predicateList; 
	//	this.nextOperator=nextOperator; 
	this.tester = NodeKindTester.createDefaultNodeKindTester(); 
  }


  public void setNodeTest(NodeTester tester) 
  {
	this.tester =tester; 
  }

  public void setPathAxe(PathAxe axe)
  {
	this.axe=axe;
  }

  public void setPredicateList(NodePredicateList predicateList) 
  {
	this.predicateList=predicateList; 
  }


  public void setTester(NodeTester tester) 
  {
	this.tester = tester; 
  }


  /**
   * @roseuid 4110B63102BE
   */
  public PathOperator() 
  {
	this.predicateList = new NodePredicateList();
	this.tester = new NodeTester();
	this.axe=PathAxe.createChildPathAxe();
	//	this.nextOperator = null;
  }
   
  /**
   * @param nodetester
   * @param predicateList
   * @param nextOperator
   * @roseuid 410E10F70150
   */
  public PathOperator(NodeTester nodetester, NodePredicateList predicateList) 
  {
    this.predicateList = predicateList;
	this.tester = nodetester;
	this.axe=PathAxe.createChildPathAxe();
	//	this.nextOperator =  nextOperator;
  }
   
  /**
   * @param axe
   * @param nodetester
   * @param predicateList
   * @param nextOperator
   * @roseuid 410E0C0503B2
   */
  public PathOperator(PathAxe axe, NodeTester nodetester, NodePredicateList predicateList) 
  {
    this.predicateList = predicateList;
	this.tester = nodetester;
	this.axe=axe;
	//	this.nextOperator =  nextOperator;
  }
   
  /**
   * @param item
   * @return boolean
   * @roseuid 410E0BCC0021
   */
  protected boolean doTest(Item item) 
  {
    return doTest(item.getNode());
  }
   
//   /**
//    * @param node
//    * @return xquery.lib.data.Sequence
//    * @throws xquery.lib.data.type.XQueryTypeException
//    * @roseuid 410E0BD40235
//    */
//   protected Sequence runNext(Node node) throws XQueryTypeException 
//   {
// 	if (this.nextOperator ==null) {
// 	  System.out.println("runNext - Add node to Sequence");
	  
// 	  Sequence seq = new Sequence();
// 	  seq.add(node);
// 	  return seq;
// 	}
// 	else {
// 	  return nextOperator.run(node);
// 	}
//   }
   

  /**
   * @param node 
   * @return boolean
   * @roseuid 
   */
  protected boolean doFilter(Node node, int index) 
	throws XQueryTypeException
  {
	//	System.out.println("PathOperator: dofilter");
	
	boolean result; 
	for (int i=0; i<predicateList.size(); i++) {
	  System.out.println("predicate:"+i);
	  
	  NodePredicate predicate = (NodePredicate)(predicateList.get(i)); 
	  result = predicate.doFilter(node, index); 
	  if (!result) 
		return result; 
	}
	
	return true; 
  }
   
 
  /**
   * @param node
   * @return xquery.lib.data.Sequence
   * @throws xquery.lib.data.type.XQueryTypeException
   */
  public Sequence run(Sequence input) throws XQueryTypeException 
  {
	return new Sequence();
  }
   
  /**
   * @param node
   * @return boolean
   * @roseuid 4118034D0257
   */
  protected boolean doTest(Node node) 
  {
    return tester.doTest(node);
  }
}
