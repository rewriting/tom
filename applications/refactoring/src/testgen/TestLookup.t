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

package testgen;

import testgen.tinyjava.*;
import testgen.tinyjava.types.*;
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
      decl@ClassDecl[super=name@!Undefined()] -> {
        System.out.println("In the class "+`decl.getname());
        System.out.println("Try to find the super-class "+`name);
        getEnvironment().down(2);
        PositionWrapper pos = new PositionWrapper(new Position());
        boolean found = false;
        try {
          MuFixPoint.lastEnvironments.clear();
          `LookupClassDecl(pos).visit(getEnvironment());
          found = true;
          System.out.println("found at position="+pos.value);
          `ApplyAtPosition(pos,Print()).visit(getEnvironment());

          System.out.println("not found");
        } catch (VisitFailure e) {
          System.out.println("not found");
        }
        getEnvironment().up();
      }
    }
  }


  public void test1() {
    Prog p = `Prog(
        PackageNode(Name("a"),ConcClassDecl(
            ClassDecl(Name("A"),Dot(Name("b"),Name("B")),ConcBodyDecl(
                MemberClassDecl(ClassDecl(Name("C"),Dot(Name("b"),Name("B")),ConcBodyDecl())),
                MemberClassDecl(ClassDecl(Name("D"),Name("C"),ConcBodyDecl())))))),
        PackageNode(Name("b"),ConcClassDecl(
            ClassDecl(Name("B"),Dot(Name("Object")),ConcBodyDecl())))
        );
    try {
      `TopDown(FindSuperClass()).visit(p);
    } catch ( VisitFailure e) {
      fail();
    }
  }

  public static void test2() {
    Prog p = ` Prog(PackageNode(Name("s"),ConcClassDecl(ClassDecl(Name("t"),Undefined(),ConcBodyDecl(Initializer(Block(LocalVariableDecl(Undefined(),Name("u"),Undefined()))))),ClassDecl(Name("k"),Undefined(),ConcBodyDecl(FieldDecl(Undefined(),Name("k"),Undefined()))),ClassDecl(Name("j"),Undefined(),ConcBodyDecl(MemberClassDecl(ClassDecl(Name("t"),Dot(Name("s"),Name("o")),ConcBodyDecl())))),ClassDecl(Name("o"),Dot(Name("t"),Name("n")),ConcBodyDecl(Initializer(Block(LocalVariableDecl(Undefined(),Name("j"),Undefined()),LocalVariableDecl(Undefined(),Name("y"),Undefined()),LocalVariableDecl(Undefined(),Name("b"),Undefined()),LocalVariableDecl(Undefined(),Name("k"),Undefined()),LocalVariableDecl(Undefined(),Name("m"),Undefined()))),MemberClassDecl(ClassDecl(Name("p"),Undefined(),ConcBodyDecl(Initializer(LocalVariableDecl(Undefined(),Name("l"),Undefined())),MemberClassDecl(ClassDecl(Name("f"),Undefined(),ConcBodyDecl()))))),MemberClassDecl(ClassDecl(Name("c"),Undefined(),ConcBodyDecl())))))),PackageNode(Name("w"),ConcClassDecl(ClassDecl(Name("k"),Undefined(),ConcBodyDecl(Initializer(LocalVariableDecl(Undefined(),Name("h"),Undefined())))))),PackageNode(Name("t"),ConcClassDecl(ClassDecl(Name("n"),Dot(Name("w"),Name("k")),ConcBodyDecl(MemberClassDecl(ClassDecl(Name("d"),Undefined(),ConcBodyDecl(Initializer(LocalVariableDecl(Undefined(),Name("v"),Undefined()))))),Initializer(Block()))),ClassDecl(Name("c"),Undefined(),ConcBodyDecl(MemberClassDecl(ClassDecl(Name("h"),Undefined(),ConcBodyDecl(Initializer(Block(LocalVariableDecl(Undefined(),Name("w"),Undefined())))))),MemberClassDecl(ClassDecl(Name("u"),Undefined(),ConcBodyDecl(FieldDecl(Undefined(),Name("g"),Undefined())))),Initializer(Block(LocalVariableDecl(Undefined(),Name("b"),Undefined()),LocalVariableDecl(Undefined(),Name("u"),Undefined()),LocalVariableDecl(Undefined(),Name("m"),Undefined()))))))));
    try {
      `TopDown(FindSuperClass()).visit(p);
    } catch ( VisitFailure e) {
      fail();
    }
  }


  public static void test3() {
    Prog p = `Prog(
        PackageNode(Name("a"),ConcClassDecl(
            ClassDecl(Name("A"),Dot(Name("A")),ConcBodyDecl()))));
    try {
      `TopDown(FindSuperClass()).visit(p);
    } catch ( VisitFailure e) {
      fail();
    }
  }

  public void test4() {
    Prog p = `Prog(PackageNode(Name("w"),ConcClassDecl(ClassDecl(Name("f"),Dot(Name("Object")),ConcBodyDecl(Initializer(Block()),MemberClassDecl(ClassDecl(Name("m"),Undefined(),ConcBodyDecl(Initializer(Block())))))))));
    try {
      `TopDown(FindSuperClass()).visit(p);
    } catch ( VisitFailure e) {
      fail();
    }
  }

  public void test5() {
    Prog p = `Prog(
        PackageNode(Name("a"),ConcClassDecl(
            ClassDecl(Name("A"),Dot(Name("b"),Name("B")),ConcBodyDecl()),
            ClassDecl(Name("b"),Undefined(),ConcBodyDecl(
                MemberClassDecl(ClassDecl(Name("C"),Undefined(),ConcBodyDecl())))))),
        PackageNode(Name("b"),ConcClassDecl(
            ClassDecl(Name("B"),Dot(Name("Object")),ConcBodyDecl())))
        );
    try {
      `TopDown(FindSuperClass()).visit(p);
    } catch ( VisitFailure e) {
      fail();
    }
  }

  public void test6() {
    Prog p = `Prog(PackageNode(Name("p"),ConcClassDecl(ClassDecl(Name("c"),Dot(Name("k"),Name("b")),ConcBodyDecl()),ClassDecl(Name("p"),Dot(Name("k"),Name("b")),ConcBodyDecl(Initializer(Block()),FieldDecl(Undefined(),Name("a"),Undefined()))))),PackageNode(Name("c"),ConcClassDecl(ClassDecl(Name("w"),Dot(Name("y"),Name("b")),ConcBodyDecl(MemberClassDecl(ClassDecl(Name("q"),Undefined(),ConcBodyDecl())))),ClassDecl(Name("r"),Dot(Name("k"),Name("i")),ConcBodyDecl()),ClassDecl(Name("q"),Dot(Name("k"),Name("f")),ConcBodyDecl()),ClassDecl(Name("j"),Dot(Name("k"),Name("e")),ConcBodyDecl(Initializer(Block()))),ClassDecl(Name("n"),Dot(Name("y"),Name("b")),ConcBodyDecl(FieldDecl(Undefined(),Name("z"),Undefined()),Initializer(LocalVariableDecl(Undefined(),Name("l"),Undefined())))),ClassDecl(Name("f"),Dot(Name("k"),Name("r")),ConcBodyDecl()),ClassDecl(Name("i"),Dot(Name("k"),Name("f")),ConcBodyDecl()))),PackageNode(Name("y"),ConcClassDecl(ClassDecl(Name("b"),Dot(Name("k"),Name("i")),ConcBodyDecl()))),PackageNode(Name("u"),ConcClassDecl(ClassDecl(Name("n"),Dot(Name("c"),Name("i")),ConcBodyDecl(FieldDecl(Undefined(),Name("x"),Undefined()))))),PackageNode(Name("k"),ConcClassDecl(ClassDecl(Name("i"),Dot(Name("Object")),ConcBodyDecl()),ClassDecl(Name("a"),Dot(Name("u"),Name("n")),ConcBodyDecl()),ClassDecl(Name("s"),Dot(Name("c"),Name("i")),ConcBodyDecl()),ClassDecl(Name("m"),Dot(Name("c"),Name("n")),ConcBodyDecl(Initializer(LocalVariableDecl(Undefined(),Name("n"),Undefined())))),ClassDecl(Name("f"),Dot(Name("c"),Name("f")),ConcBodyDecl(MemberClassDecl(ClassDecl(Name("z"),Dot(Name("p")),ConcBodyDecl())),Initializer(LocalVariableDecl(Undefined(),Name("i"),Undefined())))),ClassDecl(Name("e"),Dot(Name("u"),Name("n")),ConcBodyDecl(MemberClassDecl(ClassDecl(Name("p"),Undefined(),ConcBodyDecl())))),ClassDecl(Name("r"),Dot(Name("y"),Name("b")),ConcBodyDecl(FieldDecl(Undefined(),Name("h"),Undefined()),Initializer(Block()))),ClassDecl(Name("b"),Dot(Name("c"),Name("j")),ConcBodyDecl()))),PackageNode(Name("e"),ConcClassDecl(ClassDecl(Name("x"),Dot(Name("k"),Name("r")),ConcBodyDecl(MemberClassDecl(ClassDecl(Name("g"),Undefined(),ConcBodyDecl(MemberClassDecl(ClassDecl(Name("i"),Undefined(),ConcBodyDecl(FieldDecl(Undefined(),Name("g"),Undefined()))))))))),ClassDecl(Name("o"),Dot(Name("k"),Name("i")),ConcBodyDecl()))));
    try {
      PositionWrapper pos = new PositionWrapper(new Position(new int[]{2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 3, 1, 1}));
      `ApplyAtPosition(pos,FindSuperClass()).visit(p);
    } catch ( VisitFailure e) {
      fail();
    }
  }


}
