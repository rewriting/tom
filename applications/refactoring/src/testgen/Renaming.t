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


public class Renaming {

  %include {sl.tom }
  %include {tinyjava/TinyJava.tom}
  %include {tinyjava/_TinyJava.tom}

  %op Strategy Up(s1:Strategy) {
    is_fsym(t) {( ($t instanceof testgen.Up) )}
    make(v) {( new testgen.Up($v) )}
    get_slot(s1, t) {( (tom.library.sl.Strategy)$t.getChildAt(testgen.Up.ARG) )}
  }

  public Set<Type> collectTypes(Prog p) {
    Context context = new Context();
    context.types = new HashSet<Type>();
    try {
      `Mu(MuVar("x"),CollectTypes(context,MuVar("x"))).visit(p);
    } catch (VisitFailure e) {
      System.out.println(" Unexpected strategy failrure");
    }
    return context.types;
  }

  public void printDeclClass(Prog p) {
    for (Type t : collectTypes(p)) {
      try {
        System.out.println("ast for the type "+t);
        `ApplyAt(t,Print()).visit(p);
      } catch (VisitFailure e) {
        System.out.println(" Unexpected strategy failrure");
      }
    }
  }

  %strategy Print() extends Identity() {
    visit ClassDecl {
      decl -> {
        System.out.println(`decl);
      }
    }
  }


  %strategy ApplyAt(t:Type,s:Strategy) extends Identity() {
    visit Prog {
      Prog(_*) -> {
        return (Prog) `_Prog(ApplyAt(t,s)).visit(getEnvironment());
      }
    }
    visit CompUnit {
      CompUnit[packageName=name] -> {
        if (t.packagename.equals(`name.getname())) {
          if (t.upperclass != null) {
            //find the upper class recursively and then try to use ApplyAt on all its inner classes
            Strategy ApplyOnInnerClasses = `_ClassDecl(Identity(),Identity(),_ConcBodyDecl(Try(_MemberClassDecl(ApplyAt(t,s)))));
            return (CompUnit) `_CompUnit(Identity(),_ConcClassDecl(ApplyAt(t.upperclass,ApplyOnInnerClasses))).visit(getEnvironment());
          } else {
            return (CompUnit) `_CompUnit(Identity(),_ConcClassDecl(ApplyAt(t,s))).visit(getEnvironment());
          }
        }
      }
    }
    visit ClassDecl {
      ClassDecl[name=n] -> {
        if (t.name.equals(`n.getname())) {
          return (ClassDecl) s.visit(getEnvironment());
        }
      }
    }
  }

  %typeterm Type {
    implement { Type }
    is_sort(t) { ($t instanceof Type) }
  }


  %typeterm Context {
    implement { Context }
    is_sort(t) { ($t instanceof Context) }
  }

  public static class Context {
    public String packagename;
    public Type upperclass;
    public Set<Type> types; 
  }

  public static class Type {

    String packagename;
    String name;
    Type upperclass;

    public Type(String packagename, Type upperclass, String name) {
      this.packagename = packagename;
      this.upperclass = upperclass;
      this.name = name;
    }

    public Type(String packagename, String name) {
      this.packagename = packagename;
      this.name = name;
    }

    public String getName() {
      if (upperclass != null ) {
        return upperclass.getName()+"."+name;
      } else {
        return name;
      }
    }

    public String toString() {
      return packagename+"."+this.getName();
    }

  }

  %strategy CollectTypes(context:Context,s:Strategy) extends All(s) {
    visit ClassDecl {
      ClassDecl[name=name] -> {
        Type newtype;
        if (context.upperclass != null) {
          newtype = new Type(context.packagename,context.upperclass,`name.getname());
        } else {
          newtype = new Type(context.packagename,`name.getname());
        }
        context.types.add(newtype);
        Type upperupperclass = context.upperclass;
        context.upperclass = `newtype;
        Position current = getEnvironment().getPosition();
        //down into the body
        getEnvironment().down(3);
        this.visit(getEnvironment());
        getEnvironment().up();
        context.upperclass = upperupperclass;
        return (ClassDecl) getEnvironment().getSubject();
      }
    }
    visit CompUnit {
      CompUnit[packageName=packageName] -> {
        context.packagename = `packageName.getname();
      }
    }
  }


  %typeterm Position {
    implement { Position }
    is_sort(t) { ($t instanceof Position) }
  }

  %op Strategy Lookup(name:Name, position:Position, muvar:Strategy) {
    make(name,position,muvar) { (`Mu(MuVar("x"),LookupLocal(name,position,MuVar("x")))) }
  }

  %strategy LookupLocal(name:Name, position:Position, muvar:Strategy) extends Up(muvar) {
    visit ClassDecl {
      c@ClassDecl[bodyDecl=bodyDecl] -> {
        // search in local fields and inherited ones
        `MemberField(name,position).visit(getEnvironment());
      }
    }

    visit Stmt {
      b@Block(_*,LocalVariableDecl[name=n],_*) -> {
        if (name.equals(`n)) {
          position.setValue(getEnvironment().getPosition());
          return `b; 
        }
      }
    }

  }

  %strategy MemberField(name:Name, position:Position) extends Identity() {
    visit ClassDecl {
      c@ClassDecl[super=supername, bodyDecl=ConcBodyDecl(_*,FieldDecl[name=n],_*)] -> {
        if (name.equals(`n)) {
          position.setValue(getEnvironment().getPosition());
          return `c; 
        }
        //search in super classes
        %match (supername) {
          Dot(_*,typename) -> {
            Position superdefposition = new Position();
            `LookupType(typename.getname(),superdefposition).visit(getEnvironment());
            getEnvironment().goToPosition(superdefposition);
            this.visit(getEnvironment());
          }
        }
      }
    } 
  }


  %strategy LookupType(name:String,position:Position) extends Identity() {
    visit CompUnit {
      CompUnit[classes=ConcClassDecl(_*,ClassDecl[name=n],_*)] -> {
        if (`n.equals(name)) {
          position.setValue(getEnvironment().getPosition());
        }
      }
    }
  }

}
