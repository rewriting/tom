/*
 * Copyright (c) 2004, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
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

package nspk;

import aterm.*;
import aterm.pure.*;
import java.util.*;
import tom.library.traversal.*;
import nspk.term.*;
import nspk.term.types.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestNsh extends TestCase {
  private Nsh test;
  private static Factory factory;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestNsh.class));
	}

  public void setUp() {
		if (factory == null) {
			factory = Factory.getInstance(new PureFactory());
		}
    test = new Nsh(factory);
  }

  public void testQuery() {
    assertEquals("Tests the query function (and toString())",
                 test.query(2,2).toString(),
                 "state([agent(sender(1),SLEEP,N(sender(1),sender(1))),agent(sender(0),SLEEP,N(sender(0),sender(0)))],[agent(receiver(1),SLEEP,N(receiver(1),receiver(1))),agent(receiver(0),SLEEP,N(receiver(0),receiver(0)))],intruder(devil,[],[]),[])");
}
}
