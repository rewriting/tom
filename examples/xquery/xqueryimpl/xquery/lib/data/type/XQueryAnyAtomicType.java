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
//Source file: C:\\document\\codegen\\xquery\\lib\\data\\type\\AnyAtomicType.java

package xquery.lib.data.type;

import org.w3c.dom.*;


import xquery.lib.data.*;

public class XQueryAnyAtomicType 
{   
  /**
   * @roseuid 4110B803002A
   */
  public XQueryAnyAtomicType() 
  {
	//	this.type = ANY_ATOMIC;
  }

  public boolean qualify(String str) 
  {
	return true; 
  }

  public boolean qualify(Node node) 
  {
	return true; 
  }

  public boolean qualify(Item item) 
  {
	if (item.isNode()) {
	  return qualify(item.getNode());
	}
	else {  // is a atom;
	  return qualify(item.getAtom());
	}
  }

  public XQueryAnyAtomicType createInstance()
  {
	return new XQueryAnyAtomicType(); 
  }


  public boolean qualify(Atom atom) 
  {
	XQueryAnyAtomicType type = atom.getType();
	
	if (type.getClass().isInstance(atom)) {
	  return true; 
	}
	else {
	  return false;
	}
  }

  public boolean qualify(XQueryAnyAtomicType type)
  {
	XQueryAnyAtomicType thistype=createInstance();
	if (type.getClass().isInstance(thistype)) {
	  return true; 
	}
	else {
	  return false; 
	}
  }
  
}
