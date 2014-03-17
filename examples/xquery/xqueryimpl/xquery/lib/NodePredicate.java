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
//Source file: C:\\document\\codegen\\xquery\\lib\\NodePredicate.java

package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.Item;
import xquery.lib.data.type.XQueryTypeException;

public class NodePredicate 
{
  Expression expr=null;  
   
  /**
   * @roseuid 4110B63100D4
   */
  public NodePredicate() 
  {
    this.expr=null;
  }
   
  public Expression getExpression()
  {
	return this.expr;
  }

  public NodePredicate(Expression expr) 
  {
	this.expr=expr; 
  }

  /**
   * @param item
   * @return boolean
   * @throws xquery.lib.data.type.XQueryTypeException
   * @roseuid 410E14390376
   */
  public boolean doFilter(Item item, int siblingPosition) throws XQueryTypeException 
  {
	if (item.isNode())
	  return doFilter(item.getNode(), siblingPosition);
	else 
	  return false;		
  }
   
  /**
   * @param node
   * @return boolean
   * @throws xquery.lib.data.type.XQueryTypeException
   * @roseuid 410FA6320298
   */
  public boolean doFilter(Node node, int siblingPosition) throws XQueryTypeException 
  {
    return true;
  }
  
}
