/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this list of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce:w
 the above copyright
 *	notice, this list of conditions and the following disclaimer in the
 *	documentation and/or other materials provided with the distribution.
 *	- Neither the name of the Inria nor the names of its
 *	contributors may be used to endorse or promote products derived from
 *	this software without specific prior written permission.
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

package testgen2;

import testgen2.tinyjava.*;
import testgen2.tinyjava.types.*;
import tom.library.sl.*;
import java.util.*;
import static org.junit.Assert.fail;
import org.junit.Test;

public class TestLookup {

  %include { util.tom }
  %include { lookup.tom }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestLookup.class.getName());
  }


  %strategy FindSuperClass() extends Identity() {
    visit ClassDecl {
      decl@ClassDecl[super=name] -> {
        System.out.println("In the class "+`decl.getname());
        System.out.println("Try to find the super-class "+`name);
        getEnvironment().down(2);
        PositionWrapper pos = new PositionWrapper(Position.make());
        try {
          MuFixPoint.lastEnvironments.clear();
          `Lookup(pos).visit(getEnvironment());
          System.out.println("not found");
          throw new VisitFailure();
        } catch (VisitFailure e) {
          System.out.println("found at position="+pos.value);
          `ApplyAtPosition(pos,Print()).visit(getEnvironment());
        }
        getEnvironment().up();
      }
    }
  }

  @Test
  public void test3() {
    Prog p = `Prog(
        CompUnit(Name("a"),ConcClassDecl(
            ClassDecl(Name("A"),Dot(Name("A")),ConcBodyDecl()))));
    try {
      `TopDown(FindSuperClass()).visit(p);
    } catch ( VisitFailure e) {
      fail();
    }
  }

}
