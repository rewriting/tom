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
        PositionWrapper pos = new PositionWrapper(new Position());
        `LookupAll(res,pos,name).visit(getEnvironment());
      }
    }
  }

  %op Strategy Lookup(res:PositionWrapper) {
    make(res) {
      `IfThenElse(Is_Name(),LookupName(res),LookupComposedName(res))
    }
  }

  %op Strategy TypeLookup(res:PositionWrapper) {
    make(res) { `Sequence(Lookup(res),ApplyAtPosition(res,Choice(_FieldDecl(Lookup(res),Identity(),Identity()),Identity()))) }
  }


  %op Strategy LookupAll(res:PositionWrapper,pos:PositionWrapper,name:String) {
    make(res,pos,name) { `IfThenElse(Sequence(GetPosition(pos),onTheRightOfDot(pos)),Up(_ConsDot(TypeLookup(res),ApplyAtPosition(res,LookupAllMembers(res,FindName(res,name))))),Choice(LookupAllDecls(res,FindName(res,name)),LookupAllPackages(res,FindName(res,name)))) }
  }


  %strategy FindName(res:PositionWrapper, n:String) extends Identity() {
    visit Name {
      Name[name=name] -> {
        if (`name.equals(`n)) {
          res.value = getPosition();
          throw new VisitFailure();
        }
      }
    }
  }

  %op Strategy onTheRightOfDot(pos: PositionWrapper) {
    make(pos) { `Up(_ConsDot(Identity(),IsPosition(pos))) }
  }

  %strategy GetPosition(pos:PositionWrapper) extends Identity() {
    visit Name {
      _ -> { pos.value = getPosition(); }
    }
  }

  %strategy IsPosition(pos:PositionWrapper) extends Identity() {
    visit Name {
      n -> { 
        if (pos.value.equals(getPosition())) {
          return `n; 
        }
      }
    }
  }

  %op Strategy LookupAllMembers(pos:PositionWrapper,s:Strategy) {
    make(pos,s) { `Mu(MuVar("x"),Choice(
          _ClassDecl(Identity(),Sequence(Lookup(pos),ApplyAtPosition(pos,MuVar("x"))),_ConcBodyDecl(IfThenElse(Is_FieldDecl(),s,IfThenElse(Is_MemberClassDecl(),_MemberClassDecl(s),Identity())))),
          _CompUnit(s)))
    }
  }

  %op Strategy LookupAllDecls(pos:PositionWrapper, s:Strategy) {
    make(pos,s) { 
      `Mu(MuVar("x"),
          Choice(
            When_ClassDecl(Sequence(
                _ClassDecl(s,Identity(),Identity()),
                LookupAllMembers(pos,s),
                ApplyAtEnclosingClass(MuVar("x")))),
            When_CompUnit(Up(_Prog(MuVar("x"))))
            ))
    }
  }

  %op Strategy LookupAllPackages(pos:PositionWrapper,s:Strategy) {
    make(pos,s) { `Mu(MuVar("x"),IfThenElse(Is_ConsProg(),_Prog(s),Up(MuVar("x")))) }
  }

}
