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
package typeinference;

import typeinference.example.example.types.*;
public class Example{
  %gom {
    module Example
      abstract syntax
      B = b()
  }

  public static void main(String[] args) {
    
    Integer a = new Integer(1);
    Integer a1 = new Integer(1);
    double b = 1;
    String c = new String("c");
    if (a == b) { System.out.println("Test for 'a == b'"); }
    if (b == a) { System.out.println("Test for 'b == a'"); }
    //if (a == c) { System.out.println("Test for 'a == c'"); }
    if (a.equals(a1)) { System.out.println("Test for 'a.equals(a1)'"); }
    if (a1.equals(a)) { System.out.println("Test for 'a1.equals(a)'"); }
    if (a.equals(b)) { System.out.println("Test for 'a.equals(b)'"); }
    //if (b.equals(a)) { System.out.println("Test for 'b.equals(a)'"); }
    if (a.equals(c)) { System.out.println("Test for 'a.equals(c)' where c is boolean"); }
    if (c.equals(a)) { System.out.println("Test for 'c.equals(a)' where c is boolean"); }
    
    B tt = `b();
    %match{
      b() << B tt  && (tt == b()) -> { System.out.println(`tt); }
    }
  }
}
