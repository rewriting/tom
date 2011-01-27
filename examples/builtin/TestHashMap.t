/*
 * Copyright (c) 2006-2011, INPL, INRIA
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
package builtin;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestHashMap {

  public final static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestHashMap.class.getName());
  }

  %include { java/util/HashMap.tom }

  /* we have to do this to match a string as an object */
  %include { string.tom }
  %op Object string(s:String) {
    is_fsym(s) { ($s != null) && ( $s instanceof String ) }
    make(s) { $s }
    get_slot(s,o) { ((String) $o) }
  }

  @Test
  public void testMake() {
    java.util.HashMap map = `concHashMap(mapEntry("one","1"),
                                         mapEntry("two","2"),
                                         mapEntry("three","3"));

    assertEquals(map.get("one"),"1");
    assertEquals(map.get("two"),"2");
    assertEquals(map.get("three"),"3");
  }

  @Test
  public void testGetValue() {
    java.util.HashMap<String,String> m = new java.util.HashMap<String,String>();
    m.put("one","1");
    m.put("two","2");
    m.put("three","3");

    /* finding a value by key */
    %match(HashMap m) {
      concHashMap(_*, mapEntry(string("two"),y), _*) -> { 
        assertEquals(`y,"2");;
      }
    }
  }

  @Test
  public void testAddingRemoving() {
    java.util.HashMap m = `concHashMap(mapEntry("one","1"),
                                       mapEntry("two","2"));
    
    java.util.HashMap m2 = `concHashMap(m*, mapEntry("hello","world"));
    
    assertEquals(m2.get("one"),"1");
    assertEquals(m2.get("two"),"2");
    assertEquals(m2.get("hello"),"world");

    java.util.HashMap m3 = null;
    %match(HashMap m2) {
      concHashMap(X*, mapEntry(_,string("world")), Y* ) -> {
        m3 = `concHashMap(X*,Y*);
      }
    }

    assertEquals(m3.get("one"),"1");
    assertEquals(m3.get("two"),"2");
    assertEquals(m3.get("hello"), null);
  }
}
