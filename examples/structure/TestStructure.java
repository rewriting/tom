/*
 * Copyright (c) 2004-2008, INRIA
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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.*;
import structure.structures.types.*;

public class TestStructure extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  public static Map<String,Boolean> dataSuite() {
    Map<String,Boolean> map = new HashMap<String,Boolean>();
    map.put("[a,b,(-a,-b)]",true);
    map.put("[a,b,(-a,b)]",false);
    map.put("[a,(-a,[b,-b])]",true);
    map.put("([a,-a],[b,-b])",true);
    map.put("[a,-a,b,-b]",true);
    map.put("[a,-a,(b,-b)]",false);
    map.put("[-a,-b,-c,(a,b,c)]",true);
    map.put("[a,b,(-a,-c),(-b,c)]",true);
    map.put("[-b,(b,[-a,(a,[c,-c])])]",true);
    map.put("[(a,[c,-c]),(-a,[b,-b])]",true);
    map.put("[-a,(a,[b,-b],[c,-c])]",true);
    map.put("([b,-b],[-a,[-c,(a,c)]])",true);
    map.put("[-a,-b,(a,b,[c,-c])]",true);
    map.put("[a,(-a,[-b,(b,[c,-c])])]",true);
    map.put("[-a,([c,-c],[-b,(a,b)])]",true);
    map.put("[a,b,c,d,(-a,-b,-c,-d)]",true);
    map.put("[a,b,d,(-b,c),(-a,-d,-c)]",true);
    map.put("[a,d,(-a,-c),(-b,-d),(b,c)]",true);
    //map.put("[(-(a,b),[a,b]),a,-a,b,-b]",false); //verbose
    map.put("[[(a,b),-a,-b],[(c,[(a,b),-a]),-b,-c]]",true);

    map.put("[ [(a,b),-a,-b] , [(c,[(a,b),-a]),-b,-c] , [(c,[(a,[d,b]),-a]),-d,-b,-c] ]",true);

    // BVU
    map.put("[<[a,b];c>,<-a;[-b,-c]>]",true);
    map.put("[<[a,(b,-a)];(c,d)>,-d1,(d1,<-b;-c>),-d]",true);
    //map.put("[-a,<a;d;-b>,<b;d1;-c>,c,<-d;-d1>]",true);// out of memory
    //map.put("[<a;[b,c]>,d,(-d,<[a,b];c>)]",false);//OK but verbose
    map.put("[<a;[b,c]>,d,(-d,<[-a,-b];-c>)]",true);
    map.put("[-c,[<a;(c,-b)>,<-a;b>]]",true);
    map.put("[c,(a,-b),<(b,-a);-c>]",false);
    return map;
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
    Map<String,Boolean> map = dataSuite();
    for (String query:map.keySet()) {
      suite.addTest(new TestStructure("testsolve",query,map.get(query)));
      suite.addTest(new TestStructure("testparseandpretty",query));
    }
    return suite;
  }

  private StructureGom test;

  private String query_string;
  private boolean expected_result;

  public TestStructure(String test, String query) {
    super(test);
    this.query_string = query;
  }

  public TestStructure(String test, String query, boolean result) {
    this(test,query);
    this.expected_result = result;
  }

  public void setUp() {
    test = new StructureGom();
  }

  public void tearDown() {
    test = null;
    query_string = null;
  }

  public void testsolve() {
    Struc query = test.strucFromPretty(query_string);
    boolean res = test.localSolve(query);
    assertEquals(this.expected_result,res);
  }

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
