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

  public void generate(java.io.Writer writer) throws java.io.IOException {
writer.write(%[
package @getPackage()@;

public class @className()@ implements tom.library.strategy.mutraveler.MuStrategy, tom.library.sl.Strategy {
  /* Do not manage an internal position, since the arguments is not really
   * used
   */
  public void setPosition(tom.library.strategy.mutraveler.Position pos) { ; /* who cares */ }

  public tom.library.strategy.mutraveler.Position getPosition() {
    throw new RuntimeException("Construction strategies have no position");
  }

  public boolean hasPosition() { return false; }

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

  public jjtraveler.Visitable[] getChildren() {
    return new jjtraveler.Visitable[]{@generateMembersList()@};
  }

  public jjtraveler.Visitable setChildren(jjtraveler.Visitable[] children) {
    @generateMembersSetChildren("children")@
    return this;
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

  public tom.library.sl.Visitable fire(tom.library.sl.Visitable any) {
    try {
      tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());
      getEnvironment().setRoot(any);
      visit();
      return getEnvironment().getRoot();
    } catch (jjtraveler.VisitFailure f) {
      throw new tom.library.sl.FireException();
    }
  }

  public tom.library.strategy.mutraveler.MuStrategy accept(tom.library.strategy.mutraveler.reflective.StrategyVisitorFwd v) throws jjtraveler.VisitFailure {
    return v.visit_Strategy(this);
  }

  public tom.library.sl.Strategy accept(tom.library.sl.reflective.StrategyFwd v) throws jjtraveler.VisitFailure {
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

  public void visit() throws jjtraveler.VisitFailure {
@computeSLNewChilds(slotList,"any")@
    getEnvironment().setSubject((tom.library.sl.Visitable)@fullClassName(operator)@.make(@genMakeArguments(slotList)@));
    return;
  }
}
]%);
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
    StringBuffer out = new StringBuffer();
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      %match(SlotField head) {
        SlotField[Name=name,Domain=domain] -> {
          out.append(%[
  get_slot(@fieldName(`name)@, t) { @fieldName(`name)@ }]%);
        }
      }
    }
    return out.toString();
  }
  private String genStratArgs(SlotFieldList slots,String arg) {
    StringBuffer args = new StringBuffer();
    int i = 0;
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();

      %match(SlotField head) {
        SlotField[Name=name,Domain=domain] -> {
          args.append((i==0?"":", "));
          args.append(fieldName(`name));
          if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
            args.append(":Strategy");
          } else {
            args.append(":");
            args.append(fullClassName(`domain));
          }
        }
      }
      i++;
    }
    return args.toString();
  }

  private String genNonBuiltin() {
    StringBuffer out = new StringBuffer();
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          out.append("true, ");
        } else {
          out.append("false, ");
        }
      }
    }
    if (out.length()!=0) {
      return out.substring(0,out.length()-2);
    } else {
      return out.toString();
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

  /**
    * Store a strategy for each non builtin child, the builtin value otherwise
    */
  private String generateMembers() {
    String res="";
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += "  private jjtraveler.reflective.VisitableVisitor "+fieldName(`fieldName)+";\n";
        } else {
          res += "  private "+fullClassName(`domain)+" "+fieldName(`fieldName)+";\n";
        }
      }
    }
    return res;
  }

  private String generateMembersList() {
    String res="";
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += fieldName(`fieldName) + ", ";
        } else {
          // Skip builtin childs
        }
      }
    }
    if (res.length() != 0) {
      return res.substring(0,res.length()-2);
    } else {
      return res;
    }
  }

  /**
    * Generate "case: " instructions for each non builtin child
    * XXX: this code in duplicated from OperatorTemplate, need to be factorized
    */
  private String nonBuiltinsGetCases() {
    String res = "";
    int index = 0;
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
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
      concSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
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
    StringBuffer res = new StringBuffer();
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      %match(SlotField head) {
        SlotField[Name=name, Domain=domain] -> {
          if (res.length()!=0) {
            res.append(", ");
          }
          if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
            res.append("jjtraveler.reflective.VisitableVisitor ");
            res.append(fieldName(`name));
          } else {
            res.append(fullClassName(`domain));
            res.append(" ");
            res.append(fieldName(`name));
          }
        }
      }
    }
    return res.toString();
  }

  /**
   * Generate code to initialize all members of the strategy
   */
  private String computeNewChilds(SlotFieldList slots, String argName) {
    String res = "";
    %match(SlotFieldList slots) {
      concSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
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
   * Generate code to initialize all members of the strategy with the sl scheme
   */
  private String computeSLNewChilds(SlotFieldList slots, String argName) {
    String res = "";
    %match(SlotFieldList slots) {
      concSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += %[
    ((tom.library.sl.Strategy)@fieldName(`fieldName)@).visit();
    @fullClassName(`domain)@ new@fieldName(`fieldName)@ = (@fullClassName(`domain)@) getEnvironment().getSubject();
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
      concSlotField(_*,SlotField[Name=name],_*) -> {
        res += "    this."+fieldName(`name)+" = "+fieldName(`name)+";\n";
      }
    }
    return res;
  }

  private String generateMembersSetChildren(String array) {
    String res = "";
    int index = 0;
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[Name=name,Domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += "    this."+fieldName(`name)+" = (jjtraveler.reflective.VisitableVisitor)"+array+"["+index+"];\n";
          index++;
        }
      }
    }
    return res;
  }

  /**
    * Generate the argument list for the operator construction, using the
    * values computed by computeNewChilds
    */
  private String genMakeArguments(SlotFieldList slots) {
    StringBuffer res = new StringBuffer();
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      %match(SlotField head) {
        SlotField[Name=name,Domain=domain] -> {
          if (res.length()!=0) {
            res.append(", ");
          }
          if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
            res.append(" new");
            res.append(fieldName(`name));
          } else {
            res.append(" ");
            res.append(fieldName(`name));
          }
        }
      }
    }
    return res.toString();
  }
  private String fieldName(String fieldName) {
    return "_"+fieldName;
  }

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
