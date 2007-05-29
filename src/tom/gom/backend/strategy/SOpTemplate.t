/*
 * Gom
 *
 * Copyright (C) 2006-2007, INRIA
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

public class SOpTemplate extends TemplateClass {
  ClassName operator;
  SlotFieldList slotList;

  %include { ../../adt/objects/Objects.tom}

  /*
   * The argument is an operator class, and this template generates the
   * assotiated _Op strategy
   */
  public SOpTemplate(GomClass gomClass) {
    super(gomClass);
    ClassName clsName = this.className;
    %match(clsName) {
      ClassName(pkg,name) -> {
        String newpkg = `pkg.replaceFirst(".types.",".strategy.");
        String newname = "_"+`name;
        this.className = `ClassName(newpkg,newname);
      }
    }
    %match(gomClass) {
      OperatorClass[ClassName=opclass,Slots=slots] -> {
        this.operator = `opclass;
        this.slotList = `slots;
        return;
      }
    }
    throw new GomRuntimeException(
        "Wrong argument for SOpTemplate: " + gomClass);
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

  public tom.library.sl.Strategy getArgument(int i) {
    return args[i];
  }
  public void setArgument(int i, tom.library.sl.Strategy child) {
    args[i]= child;
  }
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
    return (tom.library.sl.Visitable[]) args.clone();
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
    tom.library.sl.Strategy[] newArgs = new tom.library.sl.Strategy[children.length];
    for(int i = 0; i < children.length; i++) {
      newArgs[i] = (tom.library.sl.Strategy) children[i];
    }
    args = newArgs;
    return this;
  }

  public tom.library.sl.Visitable visit(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());
    environment.setRoot(any);
    int status = visit();
    if(status == tom.library.sl.Environment.SUCCESS) {
      return environment.getRoot();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public tom.library.sl.Strategy accept(tom.library.sl.reflective.StrategyFwd v) throws tom.library.sl.VisitFailure {
    return v.visit_Strategy(this);
  }


  private static boolean[] nonbuiltin = new boolean[]{@genNonBuiltin()@};
  public @className()@(@genConstrArgs(slotList.length(),"tom.library.sl.Strategy arg")@) {
    args = new tom.library.sl.Strategy[] {@genConstrArgs(slotList.length(),"arg")@};
  }

  public tom.library.sl.Visitable visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {
    setEnvironment(envt);
    int status = visit();
    if(status == tom.library.sl.Environment.SUCCESS) {
      return environment.getRoot();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {
    if(any instanceof @fullClassName(operator)@) {
      int childCount = any.getChildCount();
      tom.library.sl.Visitable result = any;
      tom.library.sl.Visitable[] childs = null;
      for (int i = 0, nbi = 0; i < @slotList.length()@; i++) {
        if(nonbuiltin[i]) {
          tom.library.sl.Visitable oldChild = any.getChildAt(nbi);
          tom.library.sl.Visitable newChild = args[i].visitLight(oldChild);
          if(childs != null) {
            childs[nbi] = newChild;
          } else if(newChild != oldChild) {
            // allocate the array, and fill it
            childs = any.getChildren();
            childs[nbi] = newChild;
          }
          nbi++;
        }
      }
      if(childs!=null) {
        result = any.setChildren(childs);
      }
      return result;
    } else {
      throw new tom.library.sl.VisitFailure(msg);
    }
  }

  public int visit() {
    tom.library.sl.Visitable any = environment.getSubject();
    if(any instanceof @fullClassName(operator)@) {
      int childCount = any.getChildCount();
      tom.library.sl.Visitable[] childs = null;
      for(int i = 0, nbi = 0; i < @slotList.length()@; i++) {
        if(nonbuiltin[i]) {
          tom.library.sl.Visitable oldChild = (tom.library.sl.Visitable)any.getChildAt(nbi);
          environment.down(nbi+1);
          int status = args[i].visit();
          if(status != tom.library.sl.Environment.SUCCESS) {
            environment.upLocal();
            return status;
          }
          tom.library.sl.Visitable newChild = environment.getSubject();
          if(childs != null) {
            childs[nbi] = newChild;
          } else if(newChild != oldChild) {
            childs = ((tom.library.sl.Visitable) any).getChildren();
            childs[nbi] = newChild;
          } 
          environment.upLocal();
          nbi++;
        }
      }
      if(childs!=null) {
        environment.setSubject((tom.library.sl.Visitable)any.setChildren(childs));
      }
      return tom.library.sl.Environment.SUCCESS;
    } else {
      return tom.library.sl.Environment.FAILURE;
    }
  }
}
]%);
}

private String genConstrArgs(int count, String arg) {
  StringBuffer args = new StringBuffer();
  for(int i = 0; i < count; ++i) {
    args.append((i==0?"":", "));
    args.append(arg);
    args.append(i);
  }
  return args.toString();
}

private String genIdArgs(int count) {
  StringBuffer args = new StringBuffer();
  for(int i = 0; i < count; ++i) {
    args.append((i==0?"":", "));
    args.append("Identity()");
  }
  return args.toString();
}

public String generateMapping() {

  return %[
    %op Strategy @className()@(@genStratArgs(slotList.length(),"arg")@) {
      is_fsym(t) { (t!=null) && t instanceof (@fullClassName()@)}
      @genGetSlot(slotList.length(),"arg")@
        make(@genConstrArgs(slotList.length(),"arg")@) { new @fullClassName()@(@genConstrArgs(slotList.length(),"arg")@) }
    }
  
  ]%;
}

private String genGetSlot(int count, String arg) {
  StringBuffer out = new StringBuffer();
  for (int i = 0; i < count; ++i) {
    out.append(%[
        get_slot(@arg+i@, t) { t.getArgument(@i@) }]%);
  }
  return out.toString();
}

private String genStratArgs(int count, String arg) {
  StringBuffer args = new StringBuffer();
  for(int i = 0; i < count; ++i) {
    args.append((i==0?"":", "));
    args.append(arg);
    args.append(i);
    args.append(":Strategy");
  }
  return args.toString();
}

private String genNonBuiltin() {
  String out = "";
  %match(SlotFieldList slotList) {
    concSlotField(_*,SlotField[Domain=domain],_*) -> {
      if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
        out += "true, ";
      } else {
        out += "false, ";
      }
    }
  }
  if (out.length()!=0) {
    return out.substring(0,out.length()-2);
  } else {
    return out;
  }
}

private int nonBuiltinChildCount() {
  int count = 0;
  %match(SlotFieldList slotList) {
    concSlotField(_*,SlotField[Domain=domain],_*) -> {
      if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
        count++;
      }
    }
  }
  return count;
}

/** the class logger instance*/
private Logger getLogger() {
  return Logger.getLogger(getClass().getName());
}
}
