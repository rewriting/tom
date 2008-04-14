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
  %include {util/types/List.tom}
  %include {tinyjava/TinyJava.tom}
  %include {tinyjava/_TinyJava.tom}

  static Random random = new java.util.Random();
  
  %op Strategy Up(s1:Strategy) {
    is_fsym(t) {( ($t instanceof testgen.Up) )}
    make(v) {( new testgen.Up($v) )}
    get_slot(s1, t) {( (tom.library.sl.Strategy)$t.getChildAt(testgen.Up.ARG) )}
  }

  public List<Type> collectTypes(Prog p) {
    Context context = new Context();
    context.types = new ArrayList<Type>();
    try {
      `Mu(MuVar("x"),CollectTypes(context,MuVar("x"))).visit(p);
    } catch (VisitFailure e) {
      System.out.println(" Unexpected strategy failure");
    }
    return context.types;
  }

  public void printDeclClass(Prog p) {
    for (Type t : collectTypes(p)) {
      try {
        System.out.println("ast for the type "+t);
        TypeWrapper tt = new TypeWrapper(t);
        `ApplyAt(tt,Print()).visit(p);
      } catch (VisitFailure e) {
        System.out.println(" Unexpected strategy failure");
      }
    }
  }

  public static List<Type> getTopLevelTypes(List<Type> types) {
    List<Type> topleveltypes = new ArrayList<Type>();
    for (Type t:types) {
      if(t.getupperclass()==null) {
        topleveltypes.add(t);
      }
    }
    return topleveltypes;
  }

  public Prog generateInheritanceHierarchy(Prog p) {
    List<Type> undefinedtypes = collectTypes(p);
    List<Type> alltopleveltypes = getTopLevelTypes(undefinedtypes);
    List<Type> availabletypes = new ArrayList<Type>();
    availabletypes.addAll(alltopleveltypes);
    TypeWrapper current = new TypeWrapper((Type)undefinedtypes.get(0)); 
    try {
      return (Prog) `Mu(MuVar("x"),IfThenElse(IsComplete(undefinedtypes), Identity(), Sequence(ApplyAt(current,RenameSuperClass(availabletypes,alltopleveltypes,undefinedtypes,current)),MuVar("x")))).visit(p);
    } catch (VisitFailure e) {
      throw new RuntimeException(" Unexpected strategy failure");
    }
  }


  %strategy RenameSuperClass(availabletypes:List,alltopleveltypes:List,undefinedtypes:List,current:TypeWrapper) extends Identity() {
    visit ClassDecl {
      c@ClassDecl[]  -> {
        undefinedtypes.remove(current.type);
        if (availabletypes.isEmpty()) {
          // the hierarchy is now complete
          return `c.setsuper(`Dot(Name("Object")));
        } else {
          availabletypes.remove(current.type);
          int index = random.nextInt(availabletypes.size()+1);
          if (index == availabletypes.size()) {
            // this index corresponds to the case where there is no super-class
            availabletypes.addAll(alltopleveltypes);
            if(! undefinedtypes.isEmpty()) {
              current.type = (Type) undefinedtypes.get(0);
            }
            return `c.setsuper(`Dot(Name("Object")));
          } else {
            current.type = (Type) availabletypes.get(index);
            // construct the ComposedName
            return `c.setsuper(current.type.getComposedName());
          }
        }
      }
    }
  }

  %typeterm TypeWrapper {
    implement {TypeWrapper}
  }

  static class TypeWrapper {

    public Type type;

    public TypeWrapper(Type t) {
      this.type = t;
    }
  }

  %strategy IsComplete(undefinedtypes:List) extends Fail() {
    visit Prog {
      p -> {
        if (undefinedtypes.isEmpty()) {
          // the hierarchy is now complete
          return `p;
        }
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

  %op Strategy ApplyAt(t:TypeWrapper,s:Strategy) {
    make(t,s) { (`_Prog(ApplyAtLocal(t,s))) }
  }

  %strategy ApplyAtLocal(t:TypeWrapper,s:Strategy) extends Identity() {
    visit CompUnit {
      CompUnit[packageName=name] -> {
        if (t.type.getpackagename().equals(`name.getname())) {
          return (CompUnit) `_CompUnit(Identity(),_ConcClassDecl(this)).visit(getEnvironment());
        }
      }
    }
    visit ClassDecl {
      ClassDecl[name=n] -> {
        if (t.type.getname().equals(`n.getname())) {
          return (ClassDecl) s.visit(getEnvironment());
        } else {
          if (t.type.getupperclass() != null) {
            //find the upper class recursively and then try to use ApplyAt on all its inner classes
            return (ClassDecl) `_ClassDecl(Identity(),Identity(),_ConcBodyDecl(Try(_MemberClassDecl(this)))).visit(getEnvironment());

          }
        }
      }
    }
  }


  %typeterm Context {
    implement { Context }
    is_sort(t) { ($t instanceof Context) }
  }

  public static class Context {
    public String packagename;
    public Type upperclass;
    public List<Type> types; 
  }

  %strategy CollectTypes(context:Context,s:Strategy) extends All(s) {
    visit ClassDecl {
      c@ClassDecl[name=name] -> {
        Type newtype;
        if (context.upperclass != null) {
          newtype = new Type(context.packagename,context.upperclass,`name.getname());
        } else {
          newtype = new Type(context.packagename,`name.getname());
        }
        context.types.add(newtype);
        Type upperupperclass = context.upperclass;
        context.upperclass = `newtype;
        //visit the body
        getEnvironment().down(3);
        this.visit(getEnvironment());
        getEnvironment().up();
        context.upperclass = upperupperclass;
        return `c;
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
