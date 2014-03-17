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

package testgen;

import testgen.tinyjava.*;
import testgen.tinyjava.types.*;

public class Type {

  %include {tinyjava/TinyJava.tom}
  
  private String packagename;
  private String name;
  private Type upperclass;
  private Type superclass;

  public Type() {
  }

  public Type(String packagename, Type upperclass, String name) {
    this.packagename = packagename;
    this.upperclass = upperclass;
    this.name = name;
  }

  public Type(String packagename, String name) {
    this.packagename = packagename;
    this.name = name;
  }

  public Type(Name fullqualifiedname) {
    %match(fullqualifiedname) {
      Dot(packagename,name) -> {
        //top-level type
        this.packagename = `packagename.getname();
        this.name = `name.getname();
      }
      Dot(upperclass*,name) -> {
        //member type
        %match(upperclass) {
          Dot(packagename,_*) -> {
            this.packagename = `packagename.getname();
          }
        }
        this.upperclass = new Type(`upperclass);
        this.name =`name.getname();
      }
    }
  }

  public String toStringName() {
    if (upperclass != null ) {
      return upperclass.toStringName()+"."+name;
    } else {
      return name;
    }
  }

  public boolean inherits(Type t) {
    if (superclass == null) return false;
    if (superclass.equals(t)) return true;
    return superclass.inherits(t);
  }

  public void setType(Type t) {
    packagename = t.getpackagename();
    name = t.getname();
    if (t.getupperclass() != null) {
      upperclass = new Type();
      upperclass.setType(t.getupperclass());
    }
  }


  public Name getComposedName() {
    if (upperclass != null ) {
      Name uppercomposedname =  upperclass.getComposedName();
      %match(uppercomposedname) {
        Dot(X*) -> {
          return `Dot(X,Name(name));
        }
      }
    } 
    // top-level class
    return `Dot(Name(packagename),Name(name));
  }


  public String getpackagename() {
    return packagename;
  }

  public String getname() {
    return name;
  }

  public  Type getupperclass() {
    return upperclass;
  }

  public  Type getsuperclass() {
    return superclass;
  }

  public void setsuperclass(Type superclass) {
    this.superclass = superclass;
  }


  public String toString() {
    return packagename+"."+toStringName();
  }

  public boolean equals(Object o) {
    if ( o instanceof Type) {
      Type t = (Type) o;
      return 
        name.equals(t.getname()) && 
        (packagename!=null?packagename.equals(t.getpackagename()):true) && 
        (upperclass!=null?upperclass.equals(t.getupperclass()):true);
    }
    return false;
  }

}
