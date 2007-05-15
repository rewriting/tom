/*
 * Copyright (c) 2006-2007, INRIA
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
import gom.rulelist.types.*;

public class TestRuleList extends TestCase {
  %include { rulelist/RuleList.tom }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestRuleList.class));
  }

   public void test1() {
    assertEquals(`f(b()),`f(a()));
    assertEquals(`conc(a(),c()),`conc(a(),c()));
    assertEquals(`conc(b()),`conc(a()));
    assertEquals(`conc(a(),b()),`conc(a(),a()));
    assertNotSame(`conc(b(),b()),`conc(a(),a()));
    assertEquals(`conc(b(),b()),`conc(b(),a()));
    assertEquals(`conc(f(c())),`conc(f(a())));
    assertEquals(`conc(f(c()),b()),`conc(f(a()),a()));
  }

  public void test2() {
    assertEquals(`conc(f(c())),`conc(f(b())));
    assertEquals(`conc(f(c()),f(c())),`conc(f(b()),f(b())));
    assertEquals(`conc(f(c()),f(c())),`conc(f(a()),f(a())));
    assertEquals(`conc(a(),f(c()),a(),f(c()),b()),`conc(a(),f(a()),a(),f(a()),a()));
    assertEquals(`conc(a(),f(c())),`conc(a(),f(b())));
    // tricky test: a() there because there is no reduction
    assertEquals(`conc(a(),f(c()),a(),f(c()),b()),`conc(a(),f(a()),a(),f(c()),a()));
  }

  public void test3() {
    assertEquals(`g(conc(f(d()))),`g(conc(f(a()))));
    // Note: we have a b() be because C1* can be reduced when rewriting f(b()) into f(c())
    assertEquals(`g(conc(b(),f(d()))),`g(conc(a(),f(a()))));
    assertEquals(`g(conc(f(d()),f(d()))),`g(conc(f(a()),f(a()))));
    //tricky test: b() is the middle should really be there!
    assertEquals(`g(conc(b(),f(d()),b(),f(d()),b())),`g(conc(a(),f(a()),a(),f(a()),a())));
  }
}
