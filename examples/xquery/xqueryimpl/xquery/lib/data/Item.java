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
//Source file: C:\\document\\codegen\\xquery\\lib\\data\\Item.java

package xquery.lib.data;

import org.w3c.dom.*;

import xquery.lib.XQueryGeneralException;
import xquery.lib.data.type.XQueryTypeException;


public class Item extends Sequence 
{
  protected Node node;
  protected Atom atom;


  public Item(Node node) 
  {
	this.node =node; 
  }
   
  public Item(Atom atom) 
  {
	this.atom = atom; 
  }

  /**
   * @roseuid 4110B675033E
   */
  protected Item() 
  {
	node=null;
	atom=null;
  }
   
  /**
   * @return boolean
   * @roseuid 410F87F6018A
   */
  public boolean isNode() 
  {
    if (node!=null) {
	  return true;
	}
	else {
	  return false;
	}
  }
   
  /**
   * @return boolean
   * @roseuid 410F8816008B
   */
  public boolean isAtom() 
  {
	if (atom!=null) {
	  return true;
	}
	else {
	  return false;
	}

  }
   
  /**
   * @return org.w3c.dom.Node
   * @roseuid 410F88410123
   */
  public Node getNode() 
  {
    return node;
  }
   
  /**
   * @return xquery.lib.data.Atom
   * @roseuid 410F8881014D
   */
  public Atom getAtom() 
  {
    return atom;
  }
   
  /**
   * @param o
   * @return boolean
   * @roseuid 41115FF50155
   */
  public boolean add(Object o) 
  {
	if (this.atom !=null) {
	  super.add(atom); 
	  atom=null;
	}

	if (this.node !=null) {
	  super.add(node); 
	  node=null;
	}

	super.add(o);
    return true;
  }
   
  /**
   * @param s
   * @return xquery.lib.data.Sequence
   * @roseuid 4111600902C6
   */
  public Sequence add(Sequence s) 
  {
	if (this.atom!=null) {
	  super.add(atom); 
	  atom=null;

	}

	if (this.node!=null) {
	  super.add(node); 
	  node=null;
	}

	super.add(s);
    return null;
  }

  public static Item convertSequenceToItem(Sequence s)
	throws XQueryGeneralException
  {
	if (s.size()!=1) {
	  throw new XQueryTypeException("Error when convert Sequence to Item. Sequence's element count > 1");
	}
	
	Object o = s.elementAt(0);
	if (o instanceof Item) {
	  return (Item)o; 
	}
	else if (o instanceof Atom) {
	  return new Item((Atom)o);
	}
	else if (o instanceof Node) {
	  return new Item((Node)o);
	}
	else {
	  throw new XQueryTypeException("Error when convert Sequence to Item. Sequence's first element isn't Node, Atom or Item");
	}
  }


}
