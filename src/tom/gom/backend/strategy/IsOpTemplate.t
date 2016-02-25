/*
 * Gom
 *
 * Copyright (c) 2006-2016, Universite de Lorraine, Inria
 * Nancy, France.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.backend.strategy;

import java.io.*;
import java.util.logging.*;
import tom.gom.backend.TemplateClass;
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;

public class IsOpTemplate extends TemplateClass {
  ClassName operator;
  SlotFieldList slotList;
  boolean gwt;

  %include { ../../adt/objects/Objects.tom}

  /*
   * The argument is an operator class, and this template generates the
   * assotiated _Op strategy
   */
  public IsOpTemplate(GomClass gomClass, GomEnvironment gomEnvironment, boolean gwt) {
    super(gomClass,gomEnvironment);
    ClassName clsName = this.className;
    this.gwt = gwt;
    %match(clsName) {
      ClassName(pkg,name) -> {
        String newpkg = `pkg.replaceFirst(".types.",".strategy.");
        String newname = "Is_"+`name;
        this.className = `ClassName(newpkg,newname);
      }
    }
    %match(gomClass) {
      OperatorClass[ClassName=opclass,SlotFields=slots] -> {
        this.operator = `opclass;
        this.slotList = `slots;
        return;
      }
    }
    throw new GomRuntimeException(
        "Wrong argument for IsOpTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
if(!gwt) {
writer.write(%[
System.out.println("coucou");
package @getPackage()@;

public class @className()@ extends tom.library.sl.AbstractStrategyCombinator {
  private static final String msg = "Not an @className(operator)@";

  public @className()@() {
    initSubterm();
  }

  @@SuppressWarnings("unchecked")
  public <T extends tom.library.sl.Visitable> T visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {
    return (T) visit(envt,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public <T extends tom.library.sl.Visitable> T visit(T any) throws tom.library.sl.VisitFailure{
    return visit(any,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public <T extends tom.library.sl.Visitable> T visitLight(T any) throws tom.library.sl.VisitFailure {
    return visitLight(any,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public <T> T visitLight(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
    if(any instanceof @fullClassName(operator)@) {
     return any;
    } else {
      throw new tom.library.sl.VisitFailure(msg);
    }
  }

  public int visit(tom.library.sl.Introspector i) {
    Object any = environment.getSubject();
    if(any instanceof @fullClassName(operator)@) {
     return tom.library.sl.Environment.SUCCESS;
    } else {
      return tom.library.sl.Environment.FAILURE;
    }
  }
}
]%);
} else {
  writer.write(%[
  package @getPackage()@;

  public class @className()@ extends tom.library.sl.AbstractStrategyCombinator {
    private static final String msg = "Not an @className(operator)@";

    public @className()@() {
      initSubterm();
    }

    @@SuppressWarnings("unchecked")
    public <T extends tom.library.sl.Visitable> T visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {
      return (T) visit(envt,tom.library.sl.VisitableIntrospector.getInstance());
    }

    public <T extends tom.library.sl.Visitable> T visit(T any) throws tom.library.sl.VisitFailure{
      return visit(any,tom.library.sl.VisitableIntrospector.getInstance());
    }

    public <T extends tom.library.sl.Visitable> T visitLight(T any) throws tom.library.sl.VisitFailure {
      return visitLight(any,tom.library.sl.VisitableIntrospector.getInstance());
    }

    public <T> T visitLight(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
      if(any instanceof @fullClassName(operator)@) {
       return any;
      } else {
        throw new tom.library.sl.VisitFailure(msg);
      }
    }

    public int visit(tom.library.sl.Introspector i) {
      Object any = environment.getSubject();
      if(any instanceof @fullClassName(operator)@) {
       return tom.library.sl.Environment.SUCCESS;
      } else {
        return tom.library.sl.Environment.FAILURE;
      }
    }
  }
  ]%);
}
}

public String generateMapping() {

  String whenName = className().replaceFirst("Is_","When_");
  return %[
  %op Strategy @whenName@(s:Strategy) {
    make(s) { `Sequence(@className()@(),s) }
  }

  %op Strategy @className()@() {
    make() { new @fullClassName()@()}
  }
  ]%;
}

/** the class logger instance*/
private Logger getLogger() {
  return Logger.getLogger(getClass().getName());
}
}
