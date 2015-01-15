/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
//Source file: C:\\document\\codegen\\xquery\\lib\\NodeNameTester.java

package xquery.lib;


import org.w3c.dom.*;

import xquery.lib.data.type.*;
import xquery.lib.data.Item;

// wild card can be implemented in this class
// *
// prefix:*
// *:localname:  any namespace, 

public class NodeNameTester extends NodeTester 
{
  private String name;
   
  /**
   * @roseuid 4110B6310052
   */
  public NodeNameTester() 
  {
	super(); 
  }
   
  /**
   * @param name
   * @roseuid 410FA88F02D4
   */
  public NodeNameTester(String name) 
  {
	super(); 
	this.name=name;
	//	System.out.println(this.name);
	
  }
   
  /**
   * @return xquery.lib.data.type.String
   * @roseuid 410E136B006D
   */
  protected String getName() 
  {
    return name;
  }
   
  /**
   * @param item
   * @return boolean
   * @roseuid 410F9E67013A
   */
  public boolean doTest(Item item) 
  {
    if (item.isNode()) {
	  return doTest(item.getNode());
	}
	else {
	  return false;
	}
  }
   
  /**
   * @param node
   * @return boolean
   * @roseuid 410FA6A30308
   */
  public boolean doTest(Node node) 
  {
// 	System.out.println("=================================================");
// 	System.out.println(name);
	
// 	System.out.println(node);
	
    if (node instanceof Element && node.getNodeName().compareTo(name)==0) {
// 	  System.out.println("NodeNameTester: do Test: OK");

	  return true; 
	}
	else {
// 	  System.out.println("NodeNameTester: do Test: failed");
	  return false;
	}

  }
   
  /**
   * @param name
   * @roseuid 411015CA004A
   */
  public void setName(String name) 
  {
	this.name=name;
  }
}
