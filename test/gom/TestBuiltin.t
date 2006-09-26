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
import aterm.ATermFactory;
import aterm.pure.SingletonFactory;

public class TestBuiltin extends TestCase {

  %include { b/u/i/l/t/i/n/builtin/Builtin.tom }

  private aterm.ATerm constructNode(String nodeName, int nodeLine) {
    ATermFactory factory = SingletonFactory.getInstance();
    return factory.makeAppl(factory.makeAFun(nodeName,1,false),factory.makeInt(nodeLine));
  }

  private aterm.ATermList constructList(int e1, int e2, int e3) {
    ATermFactory factory = SingletonFactory.getInstance();
    return factory.makeList(factory.makeInt(e1),factory.makeList(factory.makeInt(e2),factory.makeList(factory.makeInt(e3),factory.makeList())));
  }

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

  public void testATerm() {
    aterm.ATerm a = constructNode("n1",2);
    aterm.ATerm b = constructNode("n1",2);
    assertTrue(a == b);
    Wrapper na = `Node(a);
    Wrapper nb = `Node(b);
    assertTrue(na == nb);
  }

  public void testATermList() {
    aterm.ATermList a = constructList(1,2,3);
    aterm.ATermList b = constructList(1,2,3);
    assertTrue(a == b);
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

  public void testMatchLong() {
    String res = "";
    Wrapper t = `Long(1234567890);
    %match(Wrapper t) {
      Long(32l) -> { res += "32l"; }
      Long(1234567890l) -> { res += "1234567890l"; }
    }
    assertTrue(res.equals("1234567890l"));
  }
  
  public void testMatchChar() {
    String res = "";
    Wrapper t = `Char('b');
    %match(Wrapper t) {
      Char('a') -> { res += "a"; }
      Char('b') -> { res += "b"; }
    }
    assertTrue(res.equals("b"));
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

  public void testDisjunctionInt() {
    boolean res = false;
    Wrapper wi = `Int(42);
    %match(Wrapper wi) {
      (Int|IntBis)(42) -> { res = true; }
    }
    assertTrue(res);
  }

  public void testDisjunctionString() {
    boolean res = false;
    Wrapper wi = `Name("42");
    %match(Wrapper wi) {
      (Name|NameBis)("42") -> { res = true; }
    }
    assertTrue(res);
  }

  public final static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestBuiltin.class));
  }
  
}
