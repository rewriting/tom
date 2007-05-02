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

public class @className()@ implements tom.library.strategy.mutraveler.MuStrategy, tom.library.sl.Strategy {
  private static final String msg = "Not an @className(operator)@";
  /* Manage an internal position */
  private tom.library.strategy.mutraveler.Position position;

  public void setPosition(tom.library.strategy.mutraveler.Position pos) {
    this.position = pos;
  }

  public tom.library.strategy.mutraveler.Position getPosition() {
    if(hasPosition()) {
      return position;
    } else {
      throw new RuntimeException("position not initialized");
    }
  }

  public boolean hasPosition() {
    return position!=null;
  }

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

  private jjtraveler.reflective.VisitableVisitor[] args;

  public jjtraveler.reflective.VisitableVisitor getArgument(int i) {
    return args[i];
  }
  public void setArgument(int i, jjtraveler.reflective.VisitableVisitor child) {
    args[i]= child;
  }
  public int getChildCount() {
    return args.length;
  }
  public jjtraveler.Visitable getChildAt(int i) {
      return args[i];
  }
  public jjtraveler.Visitable setChildAt(int i, jjtraveler.Visitable child) {
    args[i]= (jjtraveler.reflective.VisitableVisitor) child;
    return this;
  }

  public jjtraveler.Visitable[] getChildren() {
    return args;
  }

  public jjtraveler.Visitable setChildren(jjtraveler.Visitable[] children) {
    args = (jjtraveler.reflective.VisitableVisitor[])children;
    return this;
  }

  public tom.library.sl.Visitable fire(tom.library.sl.Visitable any) {
    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());
    getEnvironment().setRoot(any);
    int status = visit();
    if(status == tom.library.sl.Environment.SUCCESS) {
      return getEnvironment().getRoot();
    } else {
      throw new tom.library.sl.FireException();
    }
  }

  public tom.library.strategy.mutraveler.MuStrategy accept(tom.library.strategy.mutraveler.reflective.StrategyVisitorFwd v) throws jjtraveler.VisitFailure {
    return v.visit_Strategy(this);
  }

  public tom.library.sl.Strategy accept(tom.library.sl.reflective.StrategyFwd v) throws jjtraveler.VisitFailure {
    return v.visit_Strategy(this);
  }


  private static boolean[] nonbuiltin = new boolean[]{@genNonBuiltin()@};
  public @className()@(@genConstrArgs(slotList.length(),"jjtraveler.reflective.VisitableVisitor arg")@) {
    args = new jjtraveler.reflective.VisitableVisitor[]{@genConstrArgs(slotList.length(),"arg")@};
  }

  public jjtraveler.Visitable visit(jjtraveler.Visitable any) throws jjtraveler.VisitFailure {
    if(any instanceof @fullClassName(operator)@) {
      jjtraveler.Visitable result = any;
      if (any instanceof tom.library.strategy.mutraveler.MuVisitable) {
        boolean updated = false;
        jjtraveler.Visitable[] childs = null;

        if(!hasPosition()) {
          for (int i = 0, nbi = 0; i < @slotList.length()@; i++) {
            if (nonbuiltin[i]) {
              jjtraveler.Visitable oldChild = any.getChildAt(nbi);
              jjtraveler.Visitable newChild = args[i].visit(oldChild);
              if (updated || (newChild != oldChild)) {
                if (!updated) { // this is the first change
                  updated = true;
                  // allocate the array, and fill it
                  childs = new jjtraveler.Visitable[@nonBuiltinChildCount()@];
                  for (int j = 0 ; j<nbi ; j++) {
                    childs[j] = any.getChildAt(j);
                  }
                }
                childs[nbi] = newChild;
              }
              nbi++;
            }
          }
        } else {
          try {
            for (int i = 0, nbi = 0; i < @slotList.length()@; i++) {
              if (nonbuiltin[i]) {
                jjtraveler.Visitable oldChild = any.getChildAt(nbi);
                getPosition().down(i+1);
                jjtraveler.Visitable newChild = args[i].visit(oldChild);
                getPosition().up();
                if (updated || (newChild != oldChild)) {
                  if (!updated) {
                    updated = true;
                    // allocate the array, and fill it
                    childs = new jjtraveler.Visitable[@nonBuiltinChildCount()@];
                    for (int j = 0 ; j<nbi ; j++) {
                      childs[j] = any.getChildAt(j);
                    }
                  }
                  childs[nbi] = newChild;
                }
                nbi++;
              }
            }
          } catch(jjtraveler.VisitFailure f) {
            getPosition().up();
            throw new jjtraveler.VisitFailure();
          }
        }
        if (updated) {
          result = ((tom.library.strategy.mutraveler.MuVisitable) any).setChilds(childs);
        }
      } else {
        if(!hasPosition()) {
          for (int i = 0, nbi = 0; i < @slotList.length()@; i++) {
            if (nonbuiltin[i]) {
              jjtraveler.Visitable newChild = args[i].visit(result.getChildAt(nbi));
              result = result.setChildAt(nbi, newChild);
              nbi++;
            }
          }
        } else {
          try {
            for (int i = 0, nbi = 0; i < @slotList.length()@; i++) {
              if (nonbuiltin[i]) {
                getPosition().down(i+1);
                jjtraveler.Visitable newChild = args[i].visit(result.getChildAt(nbi));
                getPosition().up();
                result = result.setChildAt(nbi, newChild);
                nbi++;
              }
            }
          } catch(jjtraveler.VisitFailure f) {
            getPosition().up();
            throw new jjtraveler.VisitFailure();
          }
        }
      }
      return result;
    } else {
      throw new jjtraveler.VisitFailure(msg);
    }
  }

  public int visit() {
    tom.library.sl.Visitable any = getEnvironment().getSubject();
    if(any instanceof @fullClassName(operator)@) {
    int childCount = any.getChildCount();

    tom.library.sl.Visitable[] childs = null;
    for (int i = 0; i < childCount; i++) {
      tom.library.sl.Visitable oldChild = (tom.library.sl.Visitable)any.getChildAt(i);
      jjtraveler.Visitable[] array = getEnvironment().getSubject().getChildren();
      environment.down(i+1);
      int status = ((tom.library.sl.Strategy)args[i]).visit();
      if(status != tom.library.sl.Environment.SUCCESS) {
        environment.up();
        return status;
      }
      tom.library.sl.Visitable newChild = getEnvironment().getSubject();
      if(childs != null) {
        childs[i] = newChild;
        environment.up();
        /* restore subject */
        getEnvironment().setSubject(any);
      } else if(newChild != oldChild) {
        // allocate the array, and fill it
        // childs = (Visitable[])getEnvironment().getSubject().getChildren();
        java.util.List list = java.util.Arrays.asList(array);
        childs = new tom.library.sl.Visitable[childCount];
        for(int j = 0; j < array.length; j++) {
          childs[j] = (tom.library.sl.Visitable) array[j];
        }
        childs[i] = newChild;
        /* restore subject */
        environment.up();
        getEnvironment().setSubject(any);
      } else {
        /* no need to restore subject */
        environment.up();
      }
    }

    if(childs!=null) {
      getEnvironment().setSubject((tom.library.sl.Visitable)getEnvironment().getSubject().setChildren(childs));
    }
    //return;
    } else {
    return tom.library.sl.Environment.FAILURE;
    }
    return tom.library.sl.Environment.SUCCESS;
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

    String whenName = className().replaceFirst("_","When_");
    String isName = className().replaceFirst("_","Is_");
    return %[
%op Strategy @className()@(@genStratArgs(slotList.length(),"arg")@) {
  is_fsym(t) { (t!=null) && t instanceof (@fullClassName()@)}
@genGetSlot(slotList.length(),"arg")@
  make(@genConstrArgs(slotList.length(),"arg")@) { new @fullClassName()@(@genConstrArgs(slotList.length(),"arg")@) }
}

%op Strategy @whenName@(s:Strategy) {
  make(s) { `Sequence(@className()@(@genIdArgs(slotList.length())@),s) }
}

%op Strategy @isName@() {
  make() { `@className()@(@genIdArgs(slotList.length())@) }
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
