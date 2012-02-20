/*
 * Gom
 *
 * Copyright (c) 2006-2011, INPL, INRIA
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
 * 
 *
 **/

package tom.gom.backend.ada.strategy;

import java.io.*;
import java.util.logging.*;
import tom.gom.backend.ada.TemplateClass;
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;

public class SOpTemplate extends tom.gom.backend.ada.TemplateClass {
  ClassName operator;
  SlotFieldList slotList;

  %include { ../../../adt/objects/Objects.tom}

  /*
   * The argument is an operator class, and this template generates the
   * assotiated _Op strategy
   */
  public SOpTemplate(GomClass gomClass, GomEnvironment gomEnvironment) {
    super(gomClass,gomEnvironment);
    ClassName clsName = this.className;
    %match(clsName) {
      ClassName(pkg,name) -> {
        String newpkg = `pkg.replaceFirst(".types.",".strategy.");
        String newname = "_"+`name;
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
        "Wrong argument for SOpTemplate: " + gomClass);
  }

  public void generateSpec(java.io.Writer writer) throws java.io.IOException {
writer.write(%[ --From generateSpec at SOpTemplate.t ]%) ;
}

  public void generate(java.io.Writer writer) throws java.io.IOException {
writer.write(%[
package @getPackage()@;

public class @className()@ implements tom.library.sl.Strategy {
  private static final String msg = "Not an @className(operator)@";
  /* Manage an internal environment */
  protected tom.library.sl.Environment environment;

  public void setEnvironment(tom.library.sl.Environment env) {
    this.environment = env;
  }

  public tom.library.sl.Environment getEnvironment() {
    if(environment!=null) {
      return environment;
    } else {
      throw new RuntimeException("environment not initialized");
    }
  }

  private tom.library.sl.Strategy[] args;

  public int getChildCount() {
    return args.length;
  }
  public tom.library.sl.Visitable getChildAt(int i) {
      return args[i];
  }
  public tom.library.sl.Visitable setChildAt(int i, tom.library.sl.Visitable child) {
    args[i]= (tom.library.sl.Strategy) child;
    return this;
  }

  public tom.library.sl.Visitable[] getChildren() {
    return args.clone();
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
    tom.library.sl.Strategy[] newArgs = new tom.library.sl.Strategy[children.length];
    for(int i = 0; i < children.length; i++) {
      newArgs[i] = (tom.library.sl.Strategy) children[i];
    }
    args = newArgs;
    return this;
  }

  @@SuppressWarnings("unchecked")
  public <T extends tom.library.sl.Visitable> T visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {
    return (T) visit(envt,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public <T extends tom.library.sl.Visitable> T visit(T any) throws tom.library.sl.VisitFailure {
    return visit(any,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public <T extends tom.library.sl.Visitable> T visitLight(T any) throws tom.library.sl.VisitFailure {
    return visitLight(any,tom.library.sl.VisitableIntrospector.getInstance());
  }

  @@SuppressWarnings("unchecked")
  public <T> T visit(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());
    environment.setRoot(any);
    int status = visit(i);
    if(status == tom.library.sl.Environment.SUCCESS) {
      return (T) environment.getRoot();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public @className()@(@genConstrArgs(slotList.length(),"tom.library.sl.Strategy arg",false)@) {
    args = new tom.library.sl.Strategy[] {@genConstrArgs(slotList.length(),"arg",false)@};
  }

  public Object visit(tom.library.sl.Environment envt, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,envt);
    int status = visit(i);
    if(status == tom.library.sl.Environment.SUCCESS) {
      return environment.getSubject();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public <T> T visitLight(T any, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {
    if(any instanceof @fullClassName(operator)@) {
      T result = any;
      Object[] childs = null;
      for (int i = 0, nbi = 0; i < @slotList.length()@; i++) {
          Object oldChild = introspector.getChildAt(any,nbi);
          Object newChild = args[i].visitLight(oldChild,introspector);
          if(childs != null) {
            childs[nbi] = newChild;
          } else if(newChild != oldChild) {
            // allocate the array, and fill it
            childs = introspector.getChildren(any);
            childs[nbi] = newChild;
          }
          nbi++;
      }
      if(childs!=null) {
        result = introspector.setChildren(any,childs);
      }
      return result;
    } else {
      throw new tom.library.sl.VisitFailure(msg);
    }
  }

  public int visit(tom.library.sl.Introspector introspector) {
    Object any = environment.getSubject();
    if(any instanceof @fullClassName(operator)@) {
      Object[] childs = null;
      for(int i = 0, nbi = 0; i < @slotList.length()@; i++) {
          Object oldChild = introspector.getChildAt(any,nbi);
          environment.down(nbi+1);
          int status = args[i].visit(introspector);
          if(status != tom.library.sl.Environment.SUCCESS) {
            environment.upLocal();
            return status;
          }
          Object newChild = environment.getSubject();
          if(childs != null) {
            childs[nbi] = newChild;
          } else if(newChild != oldChild) {
            childs = introspector.getChildren(any);
            childs[nbi] = newChild;
          }
          environment.upLocal();
          nbi++;
      }
      if(childs!=null) {
        environment.setSubject(introspector.setChildren(any,childs));
      }
      return tom.library.sl.Environment.SUCCESS;
    } else {
      return tom.library.sl.Environment.FAILURE;
    }
  }
}
]%);
}

private String genConstrArgs(int count, String arg, boolean withDollar) {
  StringBuilder args = new StringBuilder();
  for(int i = 0; i < count; ++i) {
    args.append((i==0?"":", "));
    if(withDollar) {
      args.append("$");
    }
    args.append(arg);
    args.append(i);
  }
  return args.toString();
}

private String genIdArgs(int count) {
  StringBuilder args = new StringBuilder();
  for(int i = 0; i < count; ++i) {
    args.append((i==0?"":", "));
    args.append("Identity()");
  }
  return args.toString();
}

public String generateMapping() {

  return %[
  %op Strategy @className()@(@genStratArgs(slotList.length(),"arg")@) {
    is_fsym(t) { (($t!=null) && ($t in @fullClassName()@))}
    @genGetSlot(slotList.length(),"arg")@
    make(@genConstrArgs(slotList.length(),"arg",false)@) { new @fullClassName()@(@genConstrArgs(slotList.length(),"arg",true)@) }
  }
  ]%;
}

private String genGetSlot(int count, String arg) {
  StringBuilder out = new StringBuilder();
  for (int i = 0; i < count; ++i) {
    out.append(%[
        get_slot(@arg+i@, t) { (tom.library.sl.Strategy)((@fullClassName()@)$t).getChildAt(@i@) }]%);
  }
  return out.toString();
}

private String genStratArgs(int count, String arg) {
  StringBuilder args = new StringBuilder();
  for(int i = 0; i < count; ++i) {
    args.append((i==0?"":", "));
    args.append(arg);
    args.append(i);
    args.append(":Strategy");
  }
  return args.toString();
}

/** the class logger instance*/
protected Logger getLogger() {
  return Logger.getLogger(getClass().getName());
}
}
