/*
 * Copyright (c) 2004-2007, INRIA
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

package sl;

import tom.library.sl.Position;
import tom.library.sl.Path;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestPosition extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestPosition.class));
	}

  public void testNormalize(){
    Position p = new Position(new int[]{1,-1,2,-2,1,2,3,-3,1,1});
    Path pp = p.normalize();
    assertEquals(pp,new Position(new int[]{1,2,1,1}));
  }

  public void testAdd(){
    Position p1 = new Position(new int[]{1,1,2,1});
    Position p2 = new Position(new int[]{1,2,1,2,1});
    Path r = p2.add(p1);
    r = r.normalize();
    assertEquals(r,new Position(new int[]{1,2,1,2,1,1,1,2,1}));
  }

  public void testSub(){
    Position p1 = new Position(new int[]{1,1,2,1});
    Position p2 = new Position(new int[]{1,2,1,2,1});
    Path r = p2.sub(p1);
    r = r.normalize();
    assertEquals(r,new Position(new int[]{-1,-1,-1,2,1,2,1}));
  }

 }
