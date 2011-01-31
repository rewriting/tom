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

package poly;

import poly.expression.*;
import poly.expression.types.*;

import static org.junit.Assert.*;
import org.junit.Test;

import tom.library.sl.*;

public class PolyTraveler1 {

  public PolyTraveler1() {
  }

  %include { expression/expression.tom }
  %include{ sl.tom }
    
  %strategy SimplifyPlus() extends `Identity() { 

    visit Expression {
      plus(a1,a2) -> {
        //System.out.println("plus = " + `plus(a1,a2));
        return `plus(a1,a2);
      }
      zero() -> {
        //System.out.println("zero = " + `zero());
        return `zero();
      } 
    }
  }
  
  @Test
  public void testSimplifyPlus() {
    Expression t    = `mult(variable("x"),plus(variable("x"),constant("a")));
    Strategy v = `SimplifyPlus();
    //v.setTerm(t);
    Strategy bu = `BottomUp(v);
    try {
    //System.out.println(" bu.visitLight(" + t + ")");
    bu.visitLight(t);
    } catch (VisitFailure e) {
      System.out.println("WARNING: VisitFailure: " + e.getMessage());
    }
    assertSame("does nothing so nothing to assert",`mult(variable("x"),plus(variable("x"),constant("a"))),t);
  }
  
  public final static void main(String[] args) {
    org.junit.runner.JUnitCore.main(PolyTraveler1.class.getName());
  }
}

