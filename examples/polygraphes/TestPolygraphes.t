/*
 * Copyright (c) 2004-2011, INPL, INRIA
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

package polygraphes;

import static org.junit.Assert.*;
import org.junit.Test;
//import org.junit.Before;

import java.io.*;
import java.util.*;

import tom.library.sl.*;

import polygraphes.polygraphes.*;
import polygraphes.polygraphes.types.*;

public class TestPolygraphes {
  %include { sl.tom }
  %include { polygraphes/Polygraphes.tom }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestPolygraphes.class.getName());
  }

  //@Before
  //public void setUp() { }

  @Test
  public void testNormalisation() {
    TwoPath suc = `g("suc",1,1);
    TwoPath dup = `g("dup",1,2);
    TwoPath add = `g("add",2,1);

    TwoPath res = `c1( c0(c1(dup, c0(suc,id(1))), id(1)), c0(id(1),c1(c0(suc,suc),add)));

    res = Polygraphes.computeNF(res);
    TwoPath expectedRes = `c1(
	c0(g("dup",1,2),id(1)),
	c0(id(1),g("suc",1,1),g("suc",1,1)),
	c0(g("suc",1,1),g("add",2,1)));
    assertTrue(res == expectedRes);
  }
    
}
