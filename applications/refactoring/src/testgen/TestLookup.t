/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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

package testgen;

import testgen.tinyjava.*;
import testgen.tinyjava.types.*;
import tom.library.sl.*;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

public class TestLookup {

  %include { util.tom }
  %include { lookup.tom }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestLookup.class.getName());
  }

  @Test
  public void test1() {
    Prog p = `Prog(
        PackageNode(Name("a"),ConcClassDecl(
            ClassDecl(Name("A"),Dot(Name("b"),Name("B")),ConcBodyDecl(
                MemberClassDecl(ClassDecl(Name("C"),Dot(Name("b"),Name("B")),ConcBodyDecl())),
                MemberClassDecl(ClassDecl(Name("D"),Name("C"),ConcBodyDecl())))))),
        PackageNode(Name("b"),ConcClassDecl(
            ClassDecl(Name("B"),Dot(Name("Object")),ConcBodyDecl())))
        );
    Position pos_A = Position.makeFromArray(new int[]{1, 2, 1, 2});
    Position pos_B = Position.makeFromArray(new int[]{2, 2, 1, 2});
    Position pos_C = Position.makeFromArray(new int[]{1, 2, 1, 3, 1, 1, 2});
    Position pos_D = Position.makeFromArray(new int[]{1, 2, 1, 3, 2, 1, 1, 2});

    try {
      PositionWrapper pos = new PositionWrapper(Position.make());
      pos_A.getOmega(`LookupClassDecl(pos)).visit(p);
      assertEquals(pos.value.getSubterm().visit(p),`ClassDecl(Name("B"),Dot(Name("Object")),ConcBodyDecl()));
      pos_C.getOmega(`LookupClassDecl(pos)).visit(p);
      assertEquals(pos.value.getSubterm().visit(p),`ClassDecl(Name("B"),Dot(Name("Object")),ConcBodyDecl()));
      pos_D.getOmega(`LookupClassDecl(pos)).visit(p);
      assertEquals(pos.value.getSubterm().visit(p),`ClassDecl(Name("C"),Dot(Name("b"),Name("B")),ConcBodyDecl()));
    } catch ( VisitFailure e) {
      fail();
    }

    try {
      PositionWrapper pos = new PositionWrapper(Position.make());
      pos_B.getOmega(`LookupClassDecl(pos)).visit(p);
      fail();
    } catch ( VisitFailure e) {}
  }

  @Test
  public void test2() {
    Prog p = `Prog(
        PackageNode(Name("a"),ConcClassDecl(
            ClassDecl(Name("A"),Dot(Name("b"),Name("B")),ConcBodyDecl()),
            ClassDecl(Name("b"),Undefined(),ConcBodyDecl(
                MemberClassDecl(ClassDecl(Name("C"),Undefined(),ConcBodyDecl())))))),
        PackageNode(Name("b"),ConcClassDecl(
            ClassDecl(Name("B"),Dot(Name("Object")),ConcBodyDecl())))
        );
    Position pos_A = Position.makeFromArray(new int[]{1, 2, 1, 2});
    try {
      PositionWrapper pos = new PositionWrapper(Position.make());
      pos_A.getOmega(`LookupClassDecl(pos)).visit(p);
      fail();
    } catch ( VisitFailure e) {}
  }

  @Test
  public void test3() {
    Prog p = `Prog(
        PackageNode(Name("a"),ConcClassDecl(
            ClassDecl(Name("A"),Dot(Name("b"),Name("C")),ConcBodyDecl(
                FieldDecl(Name("Object"),Name("B"),Undefined()),
                MemberClassDecl(ClassDecl(Name("AA"),Dot(Name("Object")),ConcBodyDecl(FieldDecl(Dot(Name("B")),Name("x"),Undefined())))))),
            ClassDecl(Name("B"),Dot(Name("Object")),ConcBodyDecl()))),
        PackageNode(Name("b"),ConcClassDecl(
            ClassDecl(Name("C"),Dot(Name("Object")),ConcBodyDecl()))));
    Position pos_x = Position.makeFromArray(new int[]{1, 2, 1, 3, 2, 1, 1, 3, 1});
    try {
      PositionWrapper pos = new PositionWrapper(Position.make());
      `pos_x.getOmega(`_FieldDecl(LookupClassDecl(pos),Identity(),Identity())).visit(p);
      assertEquals(pos.value.getSubterm().visit(p),`ClassDecl(Name("B"),Dot(Name("Object")),ConcBodyDecl()));
    } catch ( VisitFailure e) {
      fail();
    }
  }

  @Test
  public void test8() {
    Prog p = `Prog(
        PackageNode(Name("s"),ConcClassDecl(
            ClassDecl(Name("z"),Dot(Name("Object")),ConcBodyDecl()),
            ClassDecl(Name("q"),Dot(Name("Object")),ConcBodyDecl(
                FieldDecl(Undefined(),Name("j"),Undefined()),
                FieldDecl(Undefined(),Name("o"),Undefined()),
                MemberClassDecl( ClassDecl(Name("p"),Undefined(),ConcBodyDecl())),
                Initializer(LocalVariableDecl(Undefined(),Name("f"),Undefined())))),
            ClassDecl(Name("f"),Dot(Name("j"),Name("v")),ConcBodyDecl()))),
        PackageNode(Name("j"),ConcClassDecl(
            ClassDecl(Name("d"),Dot(Name("j"),Name("v")),ConcBodyDecl()),
            ClassDecl(Name("v"),Dot(Name("s"),Name("z")),ConcBodyDecl(
                MemberClassDecl(ClassDecl(Name("a"),Dot(Name("r")),ConcBodyDecl(
                      Initializer(SuperCall(Dot(Name("v"),Name("this")))),
                      MemberClassDecl(ClassDecl(Name("o"),Undefined(),ConcBodyDecl(
                            MemberClassDecl(ClassDecl(Name("l"),Undefined(),ConcBodyDecl(Initializer(Block())))))))))),
                MemberClassDecl(ClassDecl(Name("r"),Dot(Name("v")),ConcBodyDecl(
                      FieldDecl(Undefined(),Name("e"),Undefined())))),
                Initializer(Block(Block(LocalVariableDecl(Undefined(),Name("v"),Undefined())))))))));
    Position pos_v = Position.makeFromArray(new int[]{2, 1, 2, 2, 1, 2});
    try {
      PositionWrapper pos = new PositionWrapper(Position.make());
      pos_v.getOmega(`LookupClassDecl(pos)).visit(p);
    } catch ( VisitFailure e) {
      fail();
    }
  }

  @Test
  public void test9() {
    Prog p = `Prog(
        PackageNode(Name("w"),ConcClassDecl(
            ClassDecl(Name("k"),Dot(Name("Object")),ConcBodyDecl()))),

        PackageNode(Name("i"),ConcClassDecl(
            ClassDecl(Name("w"),Dot(Name("Object")),ConcBodyDecl(
                MemberClassDecl(ClassDecl(Name("k"),Dot(Name("w"),Name("k")),ConcBodyDecl())))))));

    Position pos_iwk = Position.makeFromArray(new int[]{2, 1, 2, 1, 3, 1, 1, 2});
    try {
      PositionWrapper pos = new PositionWrapper(Position.make());
      pos_iwk.getOmega(`LookupClassDecl(pos)).visit(p);
    } catch ( VisitFailure e) {
      fail();
    }
  }

  @Test
  public void test10() {
    Prog p = `Prog(
        PackageNode(Name("s"),ConcClassDecl(
            ClassDecl(Name("z"),Dot(Name("Object")),ConcBodyDecl()),
            ClassDecl(Name("q"),Dot(Name("Object")),ConcBodyDecl(
                FieldDecl(Undefined(),Name("j"),Undefined()),
                FieldDecl(Undefined(),Name("o"),Undefined()),
                MemberClassDecl( ClassDecl(Name("p"),Undefined(),ConcBodyDecl())),
                Initializer(LocalVariableDecl(Undefined(),Name("f"),Undefined())))),
            ClassDecl(Name("f"),Dot(Name("j"),Name("v")),ConcBodyDecl()))),
        PackageNode(Name("j"),ConcClassDecl(
            ClassDecl(Name("d"),Dot(Name("j"),Name("v")),ConcBodyDecl()),
            ClassDecl(Name("v"),Dot(Name("s"),Name("z")),ConcBodyDecl(
                MemberClassDecl(ClassDecl(Name("a"),Dot(Name("r")),ConcBodyDecl(
                      Initializer(SuperCall(Dot(Name("v"),Name("this")))),
                      MemberClassDecl(ClassDecl(Name("o"),Undefined(),ConcBodyDecl(
                            MemberClassDecl(ClassDecl(Name("l"),Undefined(),ConcBodyDecl(Initializer(Block())))))))))),
                MemberClassDecl(ClassDecl(Name("r"),Dot(Name("v")),ConcBodyDecl(
                      FieldDecl(Undefined(),Name("e"),Undefined())))),
                Initializer(Block(Block(LocalVariableDecl(Undefined(),Name("v"),Undefined())))))))));
    Position pos_v = Position.makeFromArray(new int[]{2, 1, 2, 2, 1, 2});
    try {
      PositionWrapper pos = new PositionWrapper(Position.make());
      pos_v.getOmega(`LookupClassDecl(pos)).visit(p);
    } catch ( VisitFailure e) {
      fail();
    }
  }

  @Test
  public void test11() {
    Prog p = `Prog(
        PackageNode(Name("w"),ConcClassDecl(
            ClassDecl(Name("k"),Dot(Name("Object")),ConcBodyDecl()))),

        PackageNode(Name("i"),ConcClassDecl(
            ClassDecl(Name("w"),Dot(Name("Object")),ConcBodyDecl(
                MemberClassDecl(ClassDecl(Name("k"),Dot(Name("w"),Name("k")),ConcBodyDecl())))))));

    Position pos_iwk = Position.makeFromArray(new int[]{2, 1, 2, 1, 3, 1, 1, 2});
    try {
      PositionWrapper pos = new PositionWrapper(Position.make());
      pos_iwk.getOmega(`LookupClassDecl(pos)).visit(p);
    } catch ( VisitFailure e) {
      fail();
    }
  }




}
