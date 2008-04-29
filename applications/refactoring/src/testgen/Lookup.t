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
import java.io.*;

import tom.library.sl.*;

import java.util.*;


public class Lookup {

  %include {util.tom}

  %op Strategy LookupComposedName(res:PositionWrapper) {
    make(res) { `Mu(MuVar("x"),IfThenElse(_ConsDot(Identity(),_EmptyDot()),
          _ConsDot(LookupName(res),Identity()),
          _ConsDot(Identity(),MuVar("x"))))  }
  }


  %strategy LookupName(res:PositionWrapper) extends Identity() {
    visit Name {
      Name[name=name] -> {
        `LookupAll(res,name).visit(getEnvironment());
      }
    }
  }

  %op Strategy Lookup(res:PositionWrapper) {
    make(res) {
      `IfThenElse(Is_Name(),LookupName(res),LookupComposedName(res))
    }
  }

  %op Strategy TypeLookup(res:PositionWrapper) {
    make(res) { `Sequence(
        Lookup(res),
        ApplyAtPosition(res,IfThenElse(Is_FieldDecl(),_FieldDecl(Lookup(res),Identity(),Identity()),Identity())))
    }
  }


  %op Strategy LookupAll(res:PositionWrapper,name:String) {
    make(res,name) { `IfThenElse(
        onTheRightOfDot(),
        Choice(Up(Up(_ConsDot(TypeLookup(res),Identity()))),ApplyAtPosition(res,LookupAllMembers(res,FindName(res,name)))),
        LookupAllPackages(res,FindName(res,name))) }
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

  %op Strategy onTheRightOfDot() {
    make() { `Up(Sequence(Is_ConsDot(),Up(Is_ConsDot()))) }
  }

  %op Strategy LookupAllMembers(pos:PositionWrapper,s:Strategy) {
    make(pos,s) { `Mu(MuVar("x"),IfThenElse(Is_ClassDecl(),
          _ClassDecl(Identity(),Sequence(Lookup(pos),ApplyAtPosition(pos,MuVar("x"))),_ConcBodyDecl(IfThenElse(Is_FieldDecl(),s,IfThenElse(Is_MemberClassDecl(),_MemberClassDecl(s),Identity())))),
          IfThenElse(Is_CompUnit(),_CompUnit(Identity(),_ConcClassDecl(s)),Identity())))
    }
  }

  %op Strategy LookupAllDecls(pos:PositionWrapper, s:Strategy) {
    make(pos,s) { 
      `Mu(MuVar("x"),          
          IfThenElse(Is_ClassDecl(),
            Sequence(
              _ClassDecl(s,Identity(),Identity()),
              LookupAllMembers(pos,s),
              ApplyAtEnclosingClass(MuVar("x"))),
            IfThenElse(Is_CompUnit(),
              LookupAllMembers(pos,s),
              Sequence(
                ApplyAtEnclosingStmt(ApplyToPredecessors(s)),
                ApplyAtEnclosingScope(MuVar("x")))
              )))
    }
  }

  %op Strategy LookupAllPackages(pos:PositionWrapper,s:Strategy) {
    make(pos,s) { `Mu(MuVar("x"),IfThenElse(Is_ConsProg(),_Prog(s),Up(MuVar("x")))) }
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
      Generator generator = new Generator();
      Prog p = generator.generateProg();
      p = generator.removeConflicts(p);
      p = generator.generateInheritanceHierarchyForTopLevelClasses(p);
      System.out.println("Generated classes ");
      generator.printDeclClass(p);

    /**
    Prog p = `Prog(
        CompUnit(Name("a"),ConcClassDecl(
            ClassDecl(Name("A"),Dot(Name("b"),Name("B")),ConcBodyDecl()))),
        CompUnit(Name("b"),ConcClassDecl(
            ClassDecl(Name("B"),Dot(Name("Object")),ConcBodyDecl())))
        );

    System.out.println(p);
     */

    try {
      `TopDown(FindSuperClass()).visit(p);
    } catch ( VisitFailure e) {
      throw new RuntimeException("Unexpected strategy failure");
    }
  }


}
