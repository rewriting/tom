/*
 * Copyright (c) 2004-2006, INRIA
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

package poly;

import aterm.*;
import aterm.pure.PureFactory;
import poly.expression.*;
import poly.expression.types.*;

import tom.library.strategy.mutraveler.Identity;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

public class PolyTraveler1 {

  private Factory factory;

  public PolyTraveler1(Factory factory) {
    this.factory = factory;
  }
  public Factory getExpressionFactory() {
    return factory;
  }

  %include { expression/expression.tom }
  %include{ mutraveler.tom }
    
  public void run() {
    Expression t    = `mult(variable("x"),plus(variable("x"),constant("a")));
    VisitableVisitor v = new SimplifyPlus();
    //v.setTerm(t);
    VisitableVisitor bu = `BottomUp(v);
    try {
    System.out.println(" bu.visit(" + t + ")");
    bu.visit(t);
    } catch (jjtraveler.VisitFailure e) {
      System.out.println("WARNING: VisitFailure: " + e.getMessage());
    }

  }
  
  public final static void main(String[] args) {
    PolyTraveler1 test = new PolyTraveler1(Factory.getInstance(new PureFactory()));
    test.run();
  }

  %strategy SimplifyPlus() extends `Identity() { 

    visit Expression {
      plus(a1,a2) -> {
        System.out.println("plus = " + `plus(a1,a2));
        return `plus(a1,a2);
      }
      zero() -> {
        System.out.println("zero = " + `zero());
        return `zero();
      } 
    }
  }


/*  class SimplifyPlus extends poly.expression.VisitableFwd {
    private ATerm term;
    public void setTerm(ATerm t) {
      term =t;
    }

    public ATerm getTerm() {
      return term;
    }

    public SimplifyPlus() {
      super(new Identity());
    }

    public poly.expression.types.Expression visit_Expression_Plus(poly.expression.types.expression.Plus arg) { //throws jjtraveler.VisitFailure {
      System.out.println("plus = " + arg);
      return arg;
    }

    public poly.expression.types.Expression visit_Expression_Zero(poly.expression.types.expression.Zero arg) { 
      System.out.println("zero = " + arg);
      return arg;
    }

  }
*/

}




