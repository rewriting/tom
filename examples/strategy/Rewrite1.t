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

package strategy;

import strategy.term.*;
import strategy.term.types.*;

import tom.library.sl.*;

public class Rewrite1 {

  %include { term/term.tom }
  %include { sl.tom }

  public final static void main(String[] args) {
    Term subject = `f(g(g(a(),b()),g(a(),a())));

    Strategy rule = `RewriteSystem(subject);
    Strategy ruleId = `RewriteSystemId();



    try {
      System.out.println("subject       = " + subject);
      System.out.println("fire  = " + `Repeat(rule).visit(subject));
      System.out.println("onceBottomUp  = " + `OnceBottomUp(rule).visit(subject));
      System.out.println("onceBottomUpId= " + `OnceBottomUpId(ruleId).visit(subject));
      System.out.println("bottomUp      = " + `BottomUp(Try(rule)).visit(subject));
      System.out.println("bottomUpId    = " + `BottomUp(ruleId).visit(subject));
      System.out.println("innermost     = " + `Innermost(rule).visit(subject));
      System.out.println("innermostSlow = " + `Repeat(OnceBottomUp(rule)).visit(subject));
      System.out.println("innermostId   = " + `InnermostId(ruleId).visit(subject));
      //ted.StratDebugger.applyGraphicalDebug(subject,`BottomUp(ruleId));
    } catch (VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }
  }

  %strategy RewriteSystem(subject:Term) extends Fail() {
    visit Term {
      a() -> { 
        Position pos = getEnvironment().getPosition();
        System.out.println("a -> b at " + pos);
        System.out.println(subject + " at " + pos + " = " + pos.getSubterm().visit(subject));
        System.out.println("rwr into: " + pos.getReplace(`b()).visit(subject));
        return `b();
      }

      b() -> {
        System.out.println("b -> c at " + getEnvironment().getPosition());
        return `c();
      }

      g(c(),c()) -> {
        System.out.println("g(c,c) -> c at " + getEnvironment().getPosition());
        return `c(); 
      }
    }
  }

  %strategy RewriteSystemId() extends Identity() {
    visit Term {
      a() -> {
        System.out.println("a -> b at " + getEnvironment().getPosition());
        return `b();
      }

      b() -> {
        System.out.println("b -> c at " + getEnvironment().getPosition());
        return `c();
      }

      g(c(),c()) -> {
        System.out.println("g(c,c) -> c at " + getEnvironment().getPosition());
        return `c();
      }
    }
  }
} 
