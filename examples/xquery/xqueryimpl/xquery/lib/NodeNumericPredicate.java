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
//Source file: C:\\document\\codegen\\xquery\\lib\\NodeNumericPredicate.java

package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.type.*;
import xquery.lib.data.Item;

//import xquery.util.DomTree02;

import xquery.lib.data.*;

public class NodeNumericPredicate extends NodePredicate 
{
   
  protected int internalValue; 
  
  /**
   * @roseuid 4110FBD60026
   */
  public NodeNumericPredicate(int value) 
  {
    super();
	this.internalValue = value;
  }
   
  public NodeNumericPredicate(Expression expr)
  {
	super(expr);
	this.internalValue=0; 
  }

  protected int evaluate() throws XQueryGeneralException
  {
	if (internalValue==0) {// not evaluated
	  Sequence value = getExpression().evaluate(); 
	  Item item= Item.convertSequenceToItem(value);
	
	  XQueryAnyAtomicType atomic=new XQueryAnyAtomicType(); 
	  
	  if (item.isAtom()) {
		if (item.getAtom().isTyped()) {
		  internalValue=item.getAtom().getDecimalValue(); 
		}
	  }
	  
	  //XQueryInteger integer=XQueryInteger.qualify(item);
	  //this.value=integer.
	}
	return 1;
  }
  
  /**
   * @param node
   * @return boolean
   * @roseuid 4110F795028D
   */
  public boolean doFilter(Node node, int siblingPosition) throws XQueryTypeException 
  {
	System.out.println("NodeNumericPredicate is running");
	
	System.out.println("sibling: " + siblingPosition);
	//	DomTree02 dt = new DomTree02(); 
	//	dt.processNode(node);
	
	if (this.internalValue == siblingPosition) {
	  
	  return true;
	}
	else {
	  return false;
	}

  }
}
