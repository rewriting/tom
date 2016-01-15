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

public class MakeOpTemplate extends TemplateClass {
  ClassName operator;
  SlotFieldList slotList;

  %include { ../../adt/objects/Objects.tom}

  /**
   * The argument is an operator class, and this template generates the
   * assotiated MakeOp strategy
   */
  public MakeOpTemplate(GomClass gomClass, GomEnvironment gomEnvironment) {
    super(gomClass,gomEnvironment);
    ClassName clsName = this.className;
    %match(clsName) {
      ClassName(pkg,name) -> {
        String newpkg = `pkg.replaceFirst(".types.",".strategy.");
        String newname = "Make_"+`name;
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
        "Wrong argument for MakeOpTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
writer.write(%[
package @getPackage()@;

public class @className()@ implements tom.library.sl.Strategy {

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
  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
@nonBuiltinsGetCases()@
      default: throw new IndexOutOfBoundsException();
    }
  }
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
    switch(index) {
@nonBuiltinMakeCases("child")@
      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[]{@generateMembersList()@};
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
    @generateMembersSetChildren("children")@
    return this;
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

  public Object visit(tom.library.sl.Environment envt, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,envt);
    int status = visit(i);
    if(status == tom.library.sl.Environment.SUCCESS) {
      return environment.getSubject();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  @@SuppressWarnings("unchecked")
  public <T> T visit(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());
    getEnvironment().setRoot(any);
    int status = visit(i);
    if(status == tom.library.sl.Environment.SUCCESS) {
      return (T) getEnvironment().getRoot();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public @className()@(@childListWithType(slotList)@) {
@generateMembersInit()@
  }

  /**
    * Builds a new @className(operator)@
    * If one of the sub-strategies application fails, throw a VisitFailure
    */

  @@SuppressWarnings("unchecked")
  public <T> T visitLight(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
@computeNewChildren(slotList,"any","i")@
    return (T) @fullClassName(operator)@.make(@genMakeArguments(slotList,false)@);
  }

  public int visit(tom.library.sl.Introspector i) {
@computeSLNewChildren(slotList,"i")@
    getEnvironment().setSubject(@fullClassName(operator)@.make(@genMakeArguments(slotList,false)@));
    return tom.library.sl.Environment.SUCCESS;
  }
}
]%);
  }

  public String generateMapping() {
    String prefix = className();
    return %[
  %op Strategy @className()@(@genStratArgs(prefix,slotList,"arg")@) {
    is_fsym(t) { (($t!=null) && ($t instanceof @fullClassName()@)) }
@genGetSlot(prefix,slotList,"arg")@
    make(@genMakeArguments(slotList,false)@) { new @fullClassName()@(@genMakeArguments(slotList,true)@) }
  }
]%;
  }

private String genGetSlot(String prefix,SlotFieldList slots,String arg) {
  StringBuilder out = new StringBuilder();
  int i = 0;
  %match(slots) {
    ConcSlotField(_*,SlotField[Name=_name,Domain=domain],_*) -> {
      if (!getGomEnvironment().isBuiltinClass(`domain)) {
        out.append(%[
        get_slot(@prefix+arg+i@, t) { (tom.library.sl.Strategy)((@fullClassName()@)$t).getChildAt(@i@) }]%);
      } else {
        out.append(%[
        get_slot(@prefix+arg+i@, t) { ((tom.library.sl.VisitableBuiltin<@primitiveToReferenceType(fullClassName(`domain))@>)(((@fullClassName()@)$t).getChildAt(@i@))).getBuiltin() }]%); 
      }
      i++; 
    }
  }
  return out.toString();
}

private String genStratArgs(String prefix,SlotFieldList slots,String arg) {
    StringBuilder args = new StringBuilder();
    int i = 0;
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();

      %match(head) {
        SlotField[Name=_name,Domain=domain] -> {
          args.append((i==0?"":", "));
          args.append(prefix);
          args.append(arg);
          args.append(i);
          if (!getGomEnvironment().isBuiltinClass(`domain)) {
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
    StringBuilder out = new StringBuilder();
    %match(slotList) {
      ConcSlotField(_*,SlotField[Domain=domain],_*) -> {
        if (!getGomEnvironment().isBuiltinClass(`domain)) {
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
    /**
    int count = 0;
    %match(slotList) {
      ConcSlotField(_*,SlotField[Domain=domain],_*) -> {
        if (!getGomEnvironment().isBuiltinClass(`domain)) {
          count++;
        }
      }
    }
    return count;
    */
    return slotList.length();
  }

  /**
    * Store a strategy for each non builtin child, the builtin value otherwise
    */
  private String generateMembers() {
    String res="";
    %match(slotList) {
      ConcSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
        if (!getGomEnvironment().isBuiltinClass(`domain)) {
          res += "  private tom.library.sl.Strategy "+fieldName(`fieldName)+";\n";
        } else {
          res += "  private "+fullClassName(`domain)+" "+fieldName(`fieldName)+";\n";
        }
      }
    }
    return res;
  }

  private String generateMembersList() {
    String res="";
    %match(slotList) {
      ConcSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
        if (!getGomEnvironment().isBuiltinClass(`domain)) {
          res += fieldName(`fieldName) + ", ";
        } else {
          res += "new tom.library.sl.VisitableBuiltin("+ fieldName(`fieldName) + "), ";
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
    %match(slotList) {
      ConcSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
        if (!getGomEnvironment().isBuiltinClass(`domain)) {
          res += "      case "+index+": return "+fieldName(`fieldName)+";\n";
        } else {
          res += "      case "+index+": return new tom.library.sl.VisitableBuiltin("+fieldName(`fieldName)+");\n";
        }
        index++;
      }
    }
    return res;
  }

  private String nonBuiltinMakeCases(String argName) {
    String res = "";
    int index = 0;
    %match(slotList) {
      ConcSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
        if (!getGomEnvironment().isBuiltinClass(`domain)) {
          res += %[      case @index@: @fieldName(`fieldName)@ = (tom.library.sl.Strategy) @argName@; return this;
]%;
        } else {
          res += %[      case @index@: @fieldName(`fieldName)@ =
            ((tom.library.sl.VisitableBuiltin<@primitiveToReferenceType(fullClassName(`domain))@>) @argName@).getBuiltin(); return this;]%;
        }
        index++;
      }
    }
    return res;
  }

  /**
    * Generate the child list to be used as function parameter declaration
    * Each non builtin child has type VisitableVisitor
    */
  private String childListWithType(SlotFieldList slots) {
    StringBuilder res = new StringBuilder();
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      %match(head) {
        SlotField[Name=name, Domain=domain] -> {
          if (res.length()!=0) {
            res.append(", ");
          }
          if (!getGomEnvironment().isBuiltinClass(`domain)) {
            res.append("tom.library.sl.Strategy ");
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
  private String computeNewChildren(SlotFieldList slots, String argName, String introspectorName) {
    String res = "";
    %match(slots) {
      ConcSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
        if (!getGomEnvironment().isBuiltinClass(`domain)) {
          res += %[
    Object tmp@fieldName(`fieldName)@ = @fieldName(`fieldName)@.visitLight(@argName@,@introspectorName@);
    if (! (tmp@fieldName(`fieldName)@ instanceof @fullClassName(`domain)@)) {
      throw new tom.library.sl.VisitFailure();
    }
    @fullClassName(`domain)@ new@fieldName(`fieldName)@ = (@fullClassName(`domain)@) tmp@fieldName(`fieldName)@;
]%;
        }
      }
    }
    return res;
  }

  /**
   * Generate code to initialize all members of the strategy with the sl scheme
   */
  private String computeSLNewChildren(SlotFieldList slots, String introspectorName) {
    String res = "";
    %match(slots) {
      ConcSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
        if (!getGomEnvironment().isBuiltinClass(`domain)) {
          res += %[
    (@fieldName(`fieldName)@).visit(@introspectorName@);
    if (! (getEnvironment().getSubject() instanceof @fullClassName(`domain)@)) {
      return tom.library.sl.Environment.FAILURE;
    }
    @fullClassName(`domain)@ new@fieldName(`fieldName)@ = (@fullClassName(`domain)@) getEnvironment().getSubject();
]%;
        }
      }
    }
    return res;
  }

  /**
    * Generate the computation of all new children for the target
    */
  private String generateMembersInit() {
    String res = "";
    %match(slotList) {
      ConcSlotField(_*,SlotField[Name=name],_*) -> {
        res += "    this."+fieldName(`name)+" = "+fieldName(`name)+";\n";
      }
    }
    return res;
  }

  private String generateMembersSetChildren(String array) {
    String res = "";
    int index = 0;
    %match(slotList) {
      ConcSlotField(_*,SlotField[Name=name,Domain=domain],_*) -> {
        if (!getGomEnvironment().isBuiltinClass(`domain)) {
          res += "    this."+fieldName(`name)+" = (tom.library.sl.Strategy)"+array+"["+index+"];\n";
          index++;
        }
      }
    }
    return res;
  }

  /**
    * Generate the argument list for the operator construction, using the
    * values computed by computeNewChildren
    */
  private String genMakeArguments(SlotFieldList slots, boolean withDollar) {
    StringBuilder res = new StringBuilder();
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      %match(head) {
        SlotField[Name=name,Domain=domain] -> {
          if (res.length()!=0) {
            res.append(", ");
          }
          if (!getGomEnvironment().isBuiltinClass(`domain)) {
            res.append(" ");
            if(withDollar) {
              res.append("$");
            }
            res.append("new");
            res.append(fieldName(`name));
          } else {
            res.append(" ");
            if(withDollar) {
              res.append("$");
            }
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
