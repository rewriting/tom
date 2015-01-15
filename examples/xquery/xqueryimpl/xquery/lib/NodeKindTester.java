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
//Source file: C:\\document\\codegen\\xquery\\lib\\NodeKindTester.java

package xquery.lib;


import org.w3c.dom.*;

import xquery.lib.data.Item;
import xquery.lib.data.type.*;

public class NodeKindTester extends NodeTester 
{
  protected int nodeKind;
  protected String name;  // used only in Element and Attribute
  protected XQueryAnyAtomicType type; // used only in Element and Attribute


  public static final int DOCUMENT_NODE = org.w3c.dom.Node.DOCUMENT_NODE;
  public static final int ELEMENT_NODE = org.w3c.dom.Node.ELEMENT_NODE;
  public static final int COMMENT_NODE = org.w3c.dom.Node.COMMENT_NODE;
  public static final int ATTRIBUTE_NODE = org.w3c.dom.Node.ATTRIBUTE_NODE;
  public static final int PROCESSING_INSTRUCTION_NODE = org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE;
  public static final int TEXT_NODE = org.w3c.dom.Node.TEXT_NODE;
  public static final int ANY_NODE = DOCUMENT_NODE + 100;
  public static final int OTHER_NODE = DOCUMENT_NODE + 101;

  public static final int DEFAULT_NODE = DOCUMENT_NODE + 1000;




  public NodeKindTester(int nodekind) 
  {
	name="";
	type=null;
    this.nodeKind=nodekind;
  }


   
  /**
   * @roseuid 4110B63002A9
   */
  public NodeKindTester() 
  {
	name="";
	type=null;
	// by default, attribute axis selects attributes node, all others axes select elements bay defaut. 
	// the default node kind is called the PRINCIPAL NODE KIND for AXIS
	nodeKind = DEFAULT_NODE;  // 
  }
   
  /**
   * @return int
   * @roseuid 410E137A001E
   */
  protected int getKind() 
  {
    return nodeKind;
  }
   
  /**
   * @param item
   * @return boolean
   * @roseuid 410F9E630261
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
   * @roseuid 410FA69F01E0
   */
  public boolean doTest(Node node) 
  {

	// if the nodetest is ANYNODE, OK
	if (nodeKind==ANY_NODE) {
	  return true; 
	}
	

	// default node test
	if (nodeKind == DEFAULT_NODE) {  // Default, select by axis
	  // OK return true because this kind is created by PathOperator
	  return true;
	}

	// ELEMENT NodeTest
	if (nodeKind == ELEMENT_NODE && node.getNodeType()==ELEMENT_NODE) {
	  if (name.compareTo("")==0) {
		if ((type== null) || (type instanceof XQueryAnyAtomicType)) {
		  return true;  
		}
		else {
		  return true;
		}
	  }
	  else {
		
	  }
	}

	

	if (nodeKind == node.getNodeType()) {
	  return true;
	}

	return false;
  }
   

  /**
   * @return NodeKindTester
   * @roseuid 410FAB7A0075
   */
  public static NodeKindTester createElementNodeKindTester() 
  {
    return new NodeKindTester(ELEMENT_NODE);
  }
   
  /**
   * @param name
   * @param type
   * @return NodeKindTester
   * @roseuid 410FAF7001D1
   */
  public static NodeKindTester createElementNodeKindTester(String name, XQueryAnyAtomicType type) 
  {
	NodeKindTester result =  new NodeKindTester(ELEMENT_NODE);
	result.name = name;
	result.type = type;
	return result;
  }
   
  /**
   * @return NodeKindTester
   * @roseuid 410FAB8C0310
   */
  public static NodeKindTester createAttributeNodeKindTester() 
  {
    return new NodeKindTester(ATTRIBUTE_NODE);
  }
   
  /**
   * @return NodeKindTester
   * @roseuid 410FAB900261
   */
  public static NodeKindTester createProcessingInstructionNodeKindTester() 
  {
    return new NodeKindTester(PROCESSING_INSTRUCTION_NODE);
  }
   
  /**
   * @return NodeKindTester
   * @roseuid 410FAB91010E
   */
  public static NodeKindTester createDocumentNodeKindTester() 
  {

    return new NodeKindTester(DOCUMENT_NODE);
  }
   
  /**
   * @return NodeKindTester
   * @roseuid 410FAB910335
   */
  public static NodeKindTester createTextNodeKindTester() 
  {
    return new NodeKindTester(TEXT_NODE);
  }
   
  /**
   * @return NodeKindTester
   * @roseuid 410FAB92012E
   */
  public static NodeKindTester createCommentNodeKindTester() 
  {
    return new NodeKindTester(COMMENT_NODE);
  }
   
  /**
   * @return NodeKindTester
   * @roseuid 410FB0FC01E5
   */
  public static NodeKindTester createAnyNodeNodeKindTester() 
  {
	return new NodeKindTester(ANY_NODE); 
  }

  // called only by PathOperator
  public static NodeKindTester createDefaultNodeKindTester() 
  {
	return new NodeKindTester(DEFAULT_NODE); 
  }
  
  /**
   * @param name
   * @return NodeKindTester
   * @roseuid 410FAB920336
   */
  public static NodeKindTester createProcessingInstructionNodeKindTester(String name) 
  {
	NodeKindTester result = new NodeKindTester(PROCESSING_INSTRUCTION_NODE);	
    result.name = name; 
	return result;
  }
}
