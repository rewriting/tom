/*
 * Copyright (c) 2004, INRIA
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

import java.util.Stack;

public class PolyTraveler2 {

  private Factory factory;

  public PolyTraveler2(Factory factory) {
    this.factory = factory;
  }
  public Factory getExpressionFactory() {
    return factory;
  }

  %include { expression.tom }

  public final static void main(String[] args) {
    PolyTraveler2 test = new PolyTraveler2(Factory.getInstance(new PureFactory()));
    test.run();
  }

  public void run() {
    Expression var = `variable("X");
    Expression res = `mult(plus(var,zero()),one());
    jjtraveler.Visitor v = new SimplifyPlus();
    //jjtraveler.Visitor bu = new jjtraveler.BottomUp(v);
    jjtraveler.Visitor bu = new jjtraveler.OnceBottomUp(v);
    try {
      System.out.println(" bu.visit(" + res + ")");
      res = (Expression)bu.visit(res);
      System.out.println("Simplified form is " + res);
    } catch (jjtraveler.VisitFailure e) {
      System.out.println("WARNING: VisitFailure: " + e.getMessage());
    }

  }
  
  class SimplifyPlus extends poly.expression.Fwd {
    public SimplifyPlus() {
      //super(new jjtraveler.Identity());
      super(new jjtraveler.Fail());
    }
    
    public poly.expression.types.Expression visit_Expression(poly.expression.types.Expression arg) throws jjtraveler.VisitFailure { 
      System.out.println("arg = " + arg);
      %match(Expression arg) {
        exp(zero())    -> { return `one(); }
        plus(zero(),x) -> { return `x; }
        plus(x,zero()) -> { return `x; }
        mult(one(),x)  -> { return `x; }
        mult(x,one())  -> { return `x; }
        mult(zero(),_) -> { return `zero(); }
        mult(_,zero()) -> { return `zero(); }
      }
      System.out.println("default: " + arg);
      //return arg;
      //return (poly.expression.types.Expression)any.visit(arg);
      throw new jjtraveler.VisitFailure();
    }
    
    /*
   public poly.expression.types.Expression visit_Expression_Variable(poly.expression.types.expression.Variable arg) { 
     System.out.println("coucou: " + arg);
      return arg;
    }
    */


  }


}

 




