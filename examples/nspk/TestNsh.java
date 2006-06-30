/*
 * Copyright (c) 2004-2006, INRIA
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
import nspk.term.*;
import nspk.term.types.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestNsh extends TestCase {
  private Nsh test;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestNsh.class));
	}

  public void setUp() {
    test = new Nsh();
  }

  public void testQuery() {
    assertEquals("Tests the query function (and toString()).",
                 "state(ConsconcAgent(agent(sender(1),SLEEP(),N(sender(1),sender(1))),ConsconcAgent(agent(sender(0),SLEEP(),N(sender(0),sender(0))),EmptyconcAgent())),ConsconcAgent(agent(receiver(1),SLEEP(),N(receiver(1),receiver(1))),ConsconcAgent(agent(receiver(0),SLEEP(),N(receiver(0),receiver(0))),EmptyconcAgent())),intruder(devil(),EmptyconcNonce(),EmptyconcMessage()),EmptyconcMessage())",
                 test.query(2,2).toString());
  }

  public void testCollectOneStep() {
    Collection c1 = new HashSet();
    State start = test.query(2,2);
    test.collectOneStep(start,c1);
    int nbNext = c1.size();
    assertEquals("Tests that collectOneStep get enough states.",
                 6,nbNext);
  }

  public void testCollect3Steps() {
    Collection c1 = new HashSet();
    c1.add(test.query(2,2));
    for(int i=0;i<3;i++) {
      Collection c2 = new HashSet();
      Iterator it = c1.iterator();
      while(it.hasNext()) {
        test.collectOneStep((State)it.next(),c2);
      }
      c1 = c2;
    }
    int nb2Next = c1.size();
    assertEquals("Tests that collectOneStep get enough states.",
                 66,nb2Next);
  }

}
