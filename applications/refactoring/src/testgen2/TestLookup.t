/*
 * Copyright (c) 2004-2008, INRIA
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
 *	- Neither the name of the INRIA nor the names of its
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
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.*;

public class TestLookup extends TestCase {

  %include { util.tom }
  %include { lookup.tom }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestLookup.class));
  }


  %strategy FindSuperClass() extends Identity() {
    visit ClassDecl {
      decl@ClassDecl[super=name] -> {
        System.out.println("In the class "+`decl.getname());
        System.out.println("Try to find the super-class "+`name);
        getEnvironment().down(2);
        PositionWrapper pos = new PositionWrapper(new Position());
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

  /**
  public void test1() {
    Prog p = `Prog(
        CompUnit(Name("a"),ConcClassDecl(
            ClassDecl(Name("A"),Dot(Name("b"),Name("B")),ConcBodyDecl(
                MemberClassDecl(ClassDecl(Name("C"),Dot(Name("b"),Name("B")),ConcBodyDecl())),
                MemberClassDecl(ClassDecl(Name("D"),Name("C"),ConcBodyDecl())))))),
        CompUnit(Name("b"),ConcClassDecl(
            ClassDecl(Name("B"),Dot(Name("Object")),ConcBodyDecl())))
        );
    try {
      `TopDown(FindSuperClass()).visit(p);
    } catch ( VisitFailure e) {
      fail();
    }
  }

  public static void test2() {
    Prog p = ` Prog(CompUnit(Name("s"),ConcClassDecl(ClassDecl(Name("t"),Undefined(),ConcBodyDecl(Initializer(Block(LocalVariableDecl(Undefined(),Name("u"),Undefined()))))),ClassDecl(Name("k"),Undefined(),ConcBodyDecl(FieldDecl(Undefined(),Name("k"),Undefined()))),ClassDecl(Name("j"),Undefined(),ConcBodyDecl(MemberClassDecl(ClassDecl(Name("t"),Dot(Name("s"),Name("o")),ConcBodyDecl())))),ClassDecl(Name("o"),Dot(Name("t"),Name("n")),ConcBodyDecl(Initializer(Block(LocalVariableDecl(Undefined(),Name("j"),Undefined()),LocalVariableDecl(Undefined(),Name("y"),Undefined()),LocalVariableDecl(Undefined(),Name("b"),Undefined()),LocalVariableDecl(Undefined(),Name("k"),Undefined()),LocalVariableDecl(Undefined(),Name("m"),Undefined()))),MemberClassDecl(ClassDecl(Name("p"),Undefined(),ConcBodyDecl(Initializer(LocalVariableDecl(Undefined(),Name("l"),Undefined())),MemberClassDecl(ClassDecl(Name("f"),Undefined(),ConcBodyDecl()))))),MemberClassDecl(ClassDecl(Name("c"),Undefined(),ConcBodyDecl())))))),CompUnit(Name("w"),ConcClassDecl(ClassDecl(Name("k"),Undefined(),ConcBodyDecl(Initializer(LocalVariableDecl(Undefined(),Name("h"),Undefined())))))),CompUnit(Name("t"),ConcClassDecl(ClassDecl(Name("n"),Dot(Name("w"),Name("k")),ConcBodyDecl(MemberClassDecl(ClassDecl(Name("d"),Undefined(),ConcBodyDecl(Initializer(LocalVariableDecl(Undefined(),Name("v"),Undefined()))))),Initializer(Block()))),ClassDecl(Name("c"),Undefined(),ConcBodyDecl(MemberClassDecl(ClassDecl(Name("h"),Undefined(),ConcBodyDecl(Initializer(Block(LocalVariableDecl(Undefined(),Name("w"),Undefined())))))),MemberClassDecl(ClassDecl(Name("u"),Undefined(),ConcBodyDecl(FieldDecl(Undefined(),Name("g"),Undefined())))),Initializer(Block(LocalVariableDecl(Undefined(),Name("b"),Undefined()),LocalVariableDecl(Undefined(),Name("u"),Undefined()),LocalVariableDecl(Undefined(),Name("m"),Undefined()))))))));
    try {
      `TopDown(FindSuperClass()).visit(p);
    } catch ( VisitFailure e) {
      fail();
    }
  }
  */

  public static void test3() {
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
