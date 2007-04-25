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
    assertEquals(`f(a()),`f(b()));
    assertEquals(`conc(a()),`conc(b()));
    assertEquals(`conc(a(),a()),`conc(a(),b()));
    assertEquals(`conc(b(),a()),`conc(b(),b()));
    assertEquals(`conc(f(a())),`conc(f(b())));
    assertEquals(`conc(f(a()),a()),`conc(f(b()),b()));
  }
  
  public void test2() {
    assertEquals(`conc(f(b())),`conc(f(c())));
    assertEquals(`conc(f(b()),f(b())),`conc(f(c()),f(c())));
    assertEquals(`conc(f(a()),f(a())),`conc(f(c()),f(c())));
    assertEquals(`conc(a(),f(a()),a(),f(a()),a()),`conc(a(),f(c()),a(),f(c()),a()));
  }
  public void test3() {
    assertEquals(`g(conc(f(a()))),`g(conc(f(d()))));
    assertEquals(`g(conc(f(a()),f(a()))),`g(conc(f(d()),f(d()))));
    assertEquals(`g(conc(a(),f(a()),a(),f(a()),a())),`g(conc(a(),f(d()),a(),f(d()),a())));
  }
}
