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
import java.io.*;

import tom.library.sl.*;

import java.util.*;


public class Lookup {

  %include {util.tom}



  public static Strategy makeLookupComposedName(final PositionWrapper res) {
    return new debugger.Decorator("LookupComposedName", new debugger.Maker() { public Strategy build(Strategy[] a) { 
        return `Mu(MuVar("x"),IfThenElse(_ConsDot(Identity(),_EmptyDot()),
          _ConsDot(LookupName(res),Identity()),
          _ConsDot(Identity(),MuVar("x"))));  
        } }, new Strategy[] {});
  }



  %op Strategy LookupComposedName(res:PositionWrapper) {
    make(res) { makeLookupComposedName(res) }
  }


  %strategy LookupName(res:PositionWrapper) extends Identity() {
    visit Name {
      Name[name=name] -> {
        `LookupAll(res,name).visit(getEnvironment());
      }
    }
  }


  public static Strategy makeLookup(final PositionWrapper res) {
    return new debugger.Decorator("Lookup", new debugger.Maker() { public Strategy build(Strategy[] a) { 
        return   `IfThenElse(Is_Name(),LookupName(res),LookupComposedName(res));

        } }, new Strategy[] {});
  }

  %op Strategy Lookup(res:PositionWrapper) {
    make(res) { makeLookup(res) }
  }

  public static Strategy makeTypeLookup(final PositionWrapper res) {
    return new debugger.Decorator("TypeLookup", new debugger.Maker() { public Strategy build(Strategy[] a) { 
return `Sequence(
        Lookup(res),
        ApplyAtPosition(res,IfThenElse(Is_FieldDecl(),_FieldDecl(Lookup(res),Identity(),Identity()),Identity())));

        } }, new Strategy[] {});
  }


  %op Strategy TypeLookup(res:PositionWrapper) {
    make(res) {  makeTypeLookup(res)   }
  }

  public static Strategy makeLookupAll(final PositionWrapper res, final String name) {
    return new debugger.Decorator("LookupAll", new debugger.Maker() { public Strategy build(Strategy[] a) { 
 return `IfThenElse(
        onTheRightOfDot(),
        Choice(Up(Up(_ConsDot(TypeLookup(res),Identity()))),ApplyAtPosition(res,LookupAllMembers(res,FindName(res,name)))),
        Sequence(LookupAllPackages(res,FindName(res,name)),LookupAllDecls(res,FindName(res,name)))) ;
        } }, new Strategy[] {});
  }

  %op Strategy LookupAll(res:PositionWrapper,name:String) {
    make(res,name) { makeLookupAll(res,name) }
  }


  %strategy FindName(res:PositionWrapper, n:String) extends Identity() {
    visit ClassDecl {
      ClassDecl[name=Name[name=name]] -> {
        if (`name.equals(`n)) {
          res.value = getPosition();
          throw new VisitFailure();
        }
      }
    }
    visit CompUnit {
      CompUnit[packageName=Name[name=name]] -> {
        if (`name.equals(`n)) {
          res.value = getPosition();
          throw new VisitFailure();
        }
      }
    }
    visit Stmt {
      LocalVariableDecl[name=Name[name=name]] -> {
        if (`name.equals(`n)) {
          res.value = getPosition();
          throw new VisitFailure();
        }
      }
    }
    visit BodyDecl {
      FieldDecl[name=Name[name=name]] -> {
        if (`name.equals(`n)) {
          res.value = getPosition();
          throw new VisitFailure();
        }
      }
    }
  }



  public static Strategy makeOnTheRightOfDot() {
    return new debugger.Decorator("onTheRightOfDot", new debugger.Maker() { public Strategy build(Strategy[] a) { 
return `Up(Sequence(Is_ConsDot(),Up(Is_ConsDot()))) ;
        } }, new Strategy[] {});
  }


  %op Strategy onTheRightOfDot() {
    make() { makeOnTheRightOfDot() }
  }

  public static Strategy makeLookupAllMembers(final PositionWrapper pos, Strategy s) {
    return new debugger.Decorator("LookupAllMembers", new debugger.Maker() { public Strategy build(Strategy[] a) { 
        return `Mu(MuVar("x"),IfThenElse(Is_ClassDecl(),
          Sequence(
            _ClassDecl(Identity(),Identity(),_ConcBodyDecl(IfThenElse(Is_FieldDecl(),a[0],IfThenElse(Is_MemberClassDecl(),_MemberClassDecl(a[0]),Identity())))),
            Choice(_ClassDecl(Identity(),Lookup(pos),Identity()),ApplyAtPosition(pos,MuVar("x")))),
          IfThenElse(Is_CompUnit(),_CompUnit(Identity(),_ConcClassDecl(a[0])),Identity())));

        } }, new Strategy[] {s});
  }



  %op Strategy LookupAllMembers(pos:PositionWrapper,s:Strategy) {
    make(pos,s) { makeLookupAllMembers(pos,s)  }
  }

  public static Strategy makeLookupAllDecls(final PositionWrapper pos, Strategy s) {
    return new debugger.Decorator("LookupAllDecls", new debugger.Maker() { public Strategy build(Strategy[] a) { 
return      `Mu(MuVar("x"),          
          IfThenElse(Is_ClassDecl(),
            Sequence(
              _ClassDecl(a[0],Identity(),Identity()),
              LookupAllMembers(pos,a[0]),
              ApplyAtEnclosingScope(MuVar("x"))),
            IfThenElse(Is_CompUnit(),
              LookupAllMembers(pos,a[0]),
              Sequence(
                ApplyAtEnclosingStmt(ApplyToPredecessors(a[0])),
                ApplyAtEnclosingScope(MuVar("x")))
              )));

        } }, new Strategy[] {s});
  }

  %op Strategy LookupAllDecls(pos:PositionWrapper, s:Strategy) {
    make(pos,s) { makeLookupAllDecls(pos,s) }
  }


  public static Strategy makeLookupAllPackages(final PositionWrapper pos, Strategy s) {
    return new debugger.Decorator("LookupAllPackages", new debugger.Maker() { public Strategy build(Strategy[] a) { 
return  `Mu(MuVar("x"),IfThenElse(Is_ConsProg(),_Prog(a[0]),Up(MuVar("x")))) ;
        } }, new Strategy[] {s});
  }


  %op Strategy LookupAllPackages(pos:PositionWrapper,s:Strategy) {
    make(pos,s) { makeLookupAllPackages(pos,s) }
  }

  %strategy FindSuperClass() extends Identity() {
    visit ClassDecl {
      decl@ClassDecl[super=name] -> {
        System.out.println("In the class "+`decl.getname());
        System.out.println("Try to find the super-class "+`name);
        getEnvironment().down(2);
        PositionWrapper pos = new PositionWrapper(new Position());
        try {
          `Lookup(pos).visit(getEnvironment());
          System.out.println("not found");
        } catch (VisitFailure e) {
          System.out.println("found at position="+pos.value);
        }
        getEnvironment().up();
      }
    }
  }


  public static void main(String[] args) {
    /**
      Generator generator = new Generator();
      Prog p = generator.generateProg();
      p = generator.removeConflicts(p);
      p = generator.generateInheritanceHierarchyForTopLevelClasses(p);
      System.out.println("Generated classes ");
      generator.printDeclClass(p);
     */
    Prog p = `Prog(
        CompUnit(Name("a"),ConcClassDecl(
            ClassDecl(Name("A"),Dot(Name("b"),Name("B")),ConcBodyDecl(
                MemberClassDecl(ClassDecl(Name("C"),Dot(Name("b"),Name("B")),ConcBodyDecl())),
                MemberClassDecl(ClassDecl(Name("D"),Name("C"),ConcBodyDecl())))))),
        CompUnit(Name("b"),ConcClassDecl(
            ClassDecl(Name("B"),Dot(Name("Object")),ConcBodyDecl())))
        );

    System.out.println(p);

    try {
      Strategy s = `TopDown(FindSuperClass());
      s = debugger.Lib.weaveBasicDecorators(s);
      s.visit(p);
    } catch ( VisitFailure e) {
      throw new RuntimeException("Unexpected strategy failure");
    }
  }


}
