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
package xquery.util; 

import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*;
import jtom.runtime.*;

public class SlashSlashOperator extends PathOperator{
  %include {TNode.tom}


  public SlashSlashOperator(TNodeTester tester, PathOperator nextOperator)
  {
	
	
	super(tester, nextOperator);
  }

  public SlashSlashOperator(PathOperator nextOperator)
  {
	super(new TNodeElementTester(), nextOperator);
  }

  public SlashSlashOperator()
  {
	super(new TNodeElementTester(), null);
  }



  public Sequence run(TNode subject) 
  {
	GenericTraversal traversal = new GenericTraversal();
	final Sequence s=new Sequence(); 
	
	Collect1 collect = new Collect1() { 
		public boolean apply(ATerm t) 
		{ 
		  try {
			if(t instanceof TNode) {
			  TNode node = (TNode)t; 
			  if (doTest(node)) {
				s.addAll(runNext(node));
				return true;
			  }
			} 
			return true; 
		  }
		  catch (XQueryGeneralException e) {
			System.out.println("ERROR: xqueryGeneral exception");
			return false; 
		  }
		} // end apply 
	  }; // end new 
	traversal.genericCollect(subject, collect); 
	
	
    return s;
  }

}
