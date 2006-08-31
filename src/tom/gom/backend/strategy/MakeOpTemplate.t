/*
 * Gom
 *
 * Copyright (C) 2006 INRIA
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

public class MakeOpTemplate extends TemplateClass {
  ClassName operator;
  SlotFieldList slotList;

  %include { ../../adt/objects/Objects.tom}

  /**
   * The argument is an operator class, and this template generates the
   * assotiated MakeOp strategy
   */
  public MakeOpTemplate(ClassName className,
                        SlotFieldList slots) {
    super(className);
    %match(ClassName className) {
      ClassName(pkg,name) -> {
        String newpkg = `pkg.replaceFirst(".types.",".strategy.");
        String newname = "Make_"+`name;
        this.className = `ClassName(newpkg,newname);
      }
    }
    this.operator = className;
    this.slotList = slots;
  }

  public String generate() {

    String classBody = %[
package @getPackage()@;

public class @className()@ implements tom.library.strategy.mutraveler.MuStrategy {
  /* Do not manage an internal position, since the arguments is not really
   * used
   */
  public void setPosition(tom.library.strategy.mutraveler.Position pos) { ; /* who cares */ }

  public tom.library.strategy.mutraveler.Position getPosition() {
    throw new RuntimeException("Construction strategies have no position");
  }

  public boolean hasPosition() { return false; }

@generateMembers()@

  public int getChildCount() {
    return @nonBuiltinChildCount()@;
  }
  public jjtraveler.Visitable getChildAt(int index) {
    switch(index) {
@nonBuiltinsGetCases()@
      default: throw new IndexOutOfBoundsException();
    }
  }
  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable child) {
    switch(index) {
@nonBuiltinMakeCases("child")@
      default: throw new IndexOutOfBoundsException();
    }
  }
  /*
   * Apply the strategy, and returns the subject in case of VisitFailure
   */
  public jjtraveler.Visitable apply(jjtraveler.Visitable any) {
    try {
      return tom.library.strategy.mutraveler.MuTraveler.init(this).visit(any);
    } catch (jjtraveler.VisitFailure f) {
      return any;
    }
  }

  public tom.library.strategy.mutraveler.MuStrategy accept(tom.library.strategy.mutraveler.reflective.StrategyVisitorFwd v) throws jjtraveler.VisitFailure {
    return v.visit_Strategy(this);
  }

  public @className()@(@childListWithType(slotList)@) {
@generateMembersInit()@
  }

  /**
    * Builds a new @className(operator)@
    * If one of the sub-strategies application fails, throw a VisitFailure
    */
  public jjtraveler.Visitable visit(jjtraveler.Visitable any) throws jjtraveler.VisitFailure {
@computeNewChilds(slotList,"any")@
    return @fullClassName(operator)@.make(@genMakeArguments(slotList)@);
  }
}
]%;

    return classBody;
  }

  public String generateMapping() {
    return %[
%op Strategy @className()@(@genStratArgs(slotList,"arg")@) {
  is_fsym(t) { (t!=null) && t instanceof (@fullClassName()@)}
@genGetSlot(slotList,"arg")@
  make(@genMakeArguments(slotList)@) { new @fullClassName()@(@genMakeArguments(slotList)@) }
}
]%;
  }

  private String genGetSlot(SlotFieldList slots, String arg) {
    String out = "";
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      %match(SlotField head) {
        SlotField[name=name,domain=domain] -> {
          out += %[
  get_slot(@fieldName(`name)@, t) { @fieldName(`name)@ }]%;
        }
      }
    }
    return out;
  }
  private String genStratArgs(SlotFieldList slots,String arg) {
    String args = "";
    int i = 0;
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();

      %match(SlotField head) {
        SlotField[name=name,domain=domain] -> {
          if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
            args += (i==0?"":", ")+fieldName(`name)+":Strategy";
          } else {
            args += (i==0?"":", ")+fieldName(`name)+":"+fullClassName(`domain);
          }
        }
      }
      i++;
    }
    return args;
  }

  private String genNonBuiltin() {
    String out = "";
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=fieldName,domain=domain],_*) -> {
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
      concSlotField(_*,SlotField[domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          count++;
        }
      }
    }
    return count;
  }

  /**
    * Store a strategy for each non builtin child, the builtin value otherwise
    */
  private String generateMembers() {
    String res="";
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=fieldName,domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += "  private jjtraveler.reflective.VisitableVisitor "+fieldName(`fieldName)+";\n";
        } else {
          res += "  private "+fullClassName(`domain)+" "+fieldName(`fieldName)+";\n";
        }
      }
    }
    return res;
  }

  /**
    * Generate "case: " instructions for each non builtin child
    * XXX: this code in duplicated from OperatorTemplate, need to be factorized
    */
  private String nonBuiltinsGetCases() {
    String res = "";
    int index = 0;
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=fieldName,domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += "      case "+index+": return "+fieldName(`fieldName)+";\n";
          index++;
        }
      }
    }
    return res;
  }

  private String nonBuiltinMakeCases(String argName) {
    String res = "";
    int index = 0;
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=fieldName,domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += %[      case @index@: @fieldName(`fieldName)@ = (jjtraveler.reflective.VisitableVisitor) @argName@; return this;
]%;
          index++;
        }
      }
    }
    return res;
  }
  
  /**
    * Generate the child list to be used as function parameter declaration
    * Each non builtin child has type VisitableVisitor
    */
  private String childListWithType(SlotFieldList slots) {
    String res = "";
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      %match(SlotField head) {
        SlotField[name=name, domain=domain] -> {
          if (!res.equals("")) {
            res+= ", ";
          }
          if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
            res+= "jjtraveler.reflective.VisitableVisitor "+fieldName(`name);
          } else {
            res+= fullClassName(`domain) + " "+fieldName(`name);
          }
        }
      }
    }
    return res;
  }

  /**
   * Generate code to initialize all members of the strategy
   */
  private String computeNewChilds(SlotFieldList slots, String argName) {
    String res = "";
    %match(SlotFieldList slots) {
      concSlotField(_*,SlotField[name=fieldName,domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += %[
    @fullClassName(`domain)@ new@fieldName(`fieldName)@ = (@fullClassName(`domain)@) @fieldName(`fieldName)@.visit(@argName@);
]%;
        }
      }
    }
    return res;
  }

  /**
    * Generate the computation of all new childs for the target
    */
  private String generateMembersInit() {
    String res = "";
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=name],_*) -> {
        res += "    this."+fieldName(`name)+" = "+fieldName(`name)+";\n";
      }
    }
    return res;
  }

  /**
    * Generate the argument list for the operator construction, using the
    * values computed by computeNewChilds
    */
  private String genMakeArguments(SlotFieldList slots) {
    String res = "";
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      %match(SlotField head) {
        SlotField[name=name,domain=domain] -> {
          if (!res.equals("")) {
            res+= ", ";
          }
          if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
            res+= " new"+fieldName(`name);
          } else {
            res+= " "+fieldName(`name);
          }
        }
      }
    }
    return res;
  }
  private String fieldName(String fieldName) {
    return "_"+fieldName;
  }

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
