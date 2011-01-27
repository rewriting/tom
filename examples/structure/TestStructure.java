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

package structure;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;
import structure.structures.types.*;

@RunWith(Parameterized.class)
public class TestStructure {

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestStructure.class.getName());
  }

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
      { "[a,b,(-a,-b)]", true },
      { "[a,b,(-a,b)]", false },
      { "[a,(-a,[b,-b])]", true },
      { "([a,-a],[b,-b])", true },
      { "[a,-a,b,-b]", true },
      { "[a,-a,(b,-b)]", false },
      { "[-a,-b,-c,(a,b,c)]", true },
      { "[a,b,(-a,-c),(-b,c)]", true },
      { "[-b,(b,[-a,(a,[c,-c])])]", true },
      { "[(a,[c,-c]),(-a,[b,-b])]", true },
      { "[-a,(a,[b,-b],[c,-c])]", true },
      { "([b,-b],[-a,[-c,(a,c)]])", true },
      { "[-a,-b,(a,b,[c,-c])]", true },
      { "[a,(-a,[-b,(b,[c,-c])])]", true },
      { "[-a,([c,-c],[-b,(a,b)])]", true },
      { "[a,b,c,d,(-a,-b,-c,-d)]", true },
      { "[a,b,d,(-b,c),(-a,-d,-c)]", true },
      { "[a,d,(-a,-c),(-b,-d),(b,c)]", true },
      // { "[(-(a,b),[a,b]),a,-a,b,-b]", false }, //verbose
      { "[[(a,b),-a,-b],[(c,[(a,b),-a]),-b,-c]]", true },

      { "[ [(a,b),-a,-b] , [(c,[(a,b),-a]),-b,-c] , [(c,[(a,[d,b]),-a]),-d,-b,-c] ]", true },

      // BVU
      { "[<[a,b];c>,<-a;[-b,-c]>]", true },
      { "[<[a,(b,-a)];(c,d)>,-d1,(d1,<-b;-c>),-d]", true },
      // { "[-a,<a;d;-b>,<b;d1;-c>,c,<-d;-d1>]", true }, // out of memory
      // { "[<a;[b,c]>,d,(-d,<[a,b];c>)]", false }, //OK but verbose
      { "[<a;[b,c]>,d,(-d,<[-a,-b];-c>)]", true },
      { "[-c,[<a;(c,-b)>,<-a;b>]]", true },
      { "[c,(a,-b),<(b,-a);-c>]", false }
      });
  }

  private StructureGom test;

  private String query_string;
  private boolean expected_result;

  public TestStructure(String query, boolean result) {
    this.query_string = query;
    this.expected_result = result;
  }

  @Before
  public void setUp() {
    test = new StructureGom();
  }

  @After
  public void tearDown() {
    test = null;
    query_string = null;
  }

  @Test
  public void testsolve() {
    Struc query = test.strucFromPretty(query_string);
    boolean res = test.localSolve(query);
    assertEquals(this.expected_result,res);
  }

  @Test
  public void testparseandpretty() {
    Struc query = test.strucFromPretty(query_string);
    String pretty = test.prettyPrint(query);
    // the first round is to get the canonical representation of the query
    Struc newquery = test.strucFromPretty(pretty);
    String newpretty = test.prettyPrint(newquery);
    assertSame(query,newquery);
    assertEquals(pretty,newpretty);
  }
}
