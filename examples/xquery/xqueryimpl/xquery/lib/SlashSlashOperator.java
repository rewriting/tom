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


//Source file: C:\\document\\codegen\\xquery\\lib\\SlashSlashOperator.java

package xquery.lib;

import org.w3c.dom.*;

import java.util.*;


import xquery.lib.data.Sequence;
import xquery.lib.data.Item;
import xquery.lib.data.type.*;



public class SlashSlashOperator extends PathOperator 
{
   
  /**
   * @roseuid 4110D9CD00FA
   */
  public SlashSlashOperator() 
  {
    super();
  }
   
  /**
   * @param nodetester
   * @param filter
   * @param nextOperator
   * @roseuid 4110D9B2000B
   */
  public SlashSlashOperator(NodeTester tester, NodePredicateList predicateList) 
  {
	super(tester, predicateList);
  }
   
  /**
   * @param axe
   * @param nodetester
   * @param nodefilter
   * @param nextOperator
   * @roseuid 4110B6320125
   */
  public SlashSlashOperator(PathAxe axe, NodeTester nodetester, NodePredicateList predicateList) 
  {
	super(axe, nodetester, predicateList);    
  }

  public Sequence run(Sequence input) throws XQueryTypeException 
  {
	Sequence result = new Sequence();
	Enumeration enum=input.elements(); 
	
	while (enum.hasMoreElements()) {
	  Object obj=enum.nextElement(); 
	  if (obj instanceof Node) {
		result.add(run((Node)obj));
	  }
	  else if (obj instanceof Item) {
		result.add(run(((Item)obj).getNode()));
	  }
	}
	
	return result;
  }
   
  /**
   * @param obj
   * @return xquery.lib.data.Sequence
   * @roseuid 410E10C303C2
   */
  public Sequence run(Node node) 
  {
	//	System.out.println("SlashSlashOperator: run Node: ");
	
	NodeTraversal traversal = new NodeTraversal();
	final Sequence s=new Sequence(); 
	//	int index=1;   // predicate base 1, not 0
	
	Collect2 collect = new Collect2() {  // use one argument 
 		int index=0;
		public boolean apply(Object t,Object arg1) 
		{ 
		  try {
			if(t instanceof Node) {
			  Node anode = (Node)t; 
			  // 			  if (anode.getNodeName().compareTo("book") ==0)
			  // 				System.out.println("Testing BOOK");
			  if (t== arg1) { // t is the root, pass
				return true;
			  }

			  if (doTest(anode)) {
				index++; 
				if  (doFilter(anode, index)) {
				  s.add(anode);
				//   System.out.println("Test OK");
// 				  System.out.println("index:"+index);
				  return true; // continue
				}
			  }
			  
// 			  else {
// 				System.out.println("Test Failed");
// 			  }
			} 
			return true;  // continue
		  }
		  catch (XQueryGeneralException e) {
			System.out.println("ERROR: xqueryGeneral exception");
			return false; 
		  }

		} // end apply 
	  }; // end new 
	traversal.genericCollect(node, collect, node);  // node is the root
// 	SequenceTool st=new SequenceTool();
// 	return st.removeDuplicated(s);
	return s;
  }
   
  /**
   * @param item
   * @return xquery.lib.data.Sequence
   * @roseuid 411016F701FC
   */
  public Sequence run(Item item) 
  {
	if (item.isNode()) {
	  return run(item.getNode());
	}
	else {
	  return new Sequence();
	}
  }

}
