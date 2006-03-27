/*
 * Copyright (c) 2006, INRIA
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
 *
 * Antoine Reilles   e-mail: Antoine.Reilles@loria.fr
 */
package gom;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import gom.b.u.i.l.t.i.n.builtin.types.*;

public class TestBuiltin extends TestCase {

  %include { b/u/i/l/t/i/n/builtin/Builtin.tom }

  public void testInt() {
    Wrapper t1 = `Int(10);
    Wrapper t2 = `Int(10);
    assertTrue(t1 == t2);
  }

  public void testString() {
    String a = "Germain";
    String b = new String("Germain");
    assertFalse(a == b);
    Wrapper na = `Name(a);
    Wrapper nb = `Name(b);
    assertTrue(na == nb);
  }

  public void testMatchInt() {
    String res = "";
    Wrapper t = `Int(32);
    %match(Wrapper t) {
      Int(10) -> { res += "10"; }
      Int(32) -> { res += "32"; }
    }
    assertTrue(res.equals("32"));
  }

  public void testMatchString() {
    String res = "";
    Wrapper nm = `Name("Germain");
    %match(Wrapper nm) {
      Name("Pem") -> { res += "pem"; }
      Name("Germain") -> { res += "G"; }
    }
    assertTrue(res.equals("G"));
  }

  public final static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestBuiltin.class));
  }
  
}
