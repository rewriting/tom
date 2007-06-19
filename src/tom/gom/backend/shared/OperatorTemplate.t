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

package tom.gom.backend.shared;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import tom.gom.backend.TemplateHookedClass;
import tom.gom.backend.TemplateClass;
import tom.gom.backend.CodeGen;
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;
import tom.platform.OptionManager;

public class OperatorTemplate extends TemplateHookedClass {
  ClassName abstractType;
  ClassName extendsType;
  ClassName sortName;
  ClassName visitor;
  SlotFieldList slotList;

  %include { ../../adt/objects/Objects.tom}

  public OperatorTemplate(File tomHomePath,
                          OptionManager manager,
                          List importList, 	
                          GomClass gomClass,
                          TemplateClass mapping) {
    super(gomClass,manager,tomHomePath,importList,mapping);
    %match(gomClass) {
      OperatorClass[AbstractType=abstractType,
                    ExtendsType=extendsType,
                    Mapping=mapping,
                    SortName=sortName,
                    Visitor=visitorName,
                    Slots=slots] -> {
        this.abstractType = `abstractType;
        this.extendsType = `extendsType;;
        this.sortName = `sortName;
        this.visitor = `visitorName;
        this.slotList = `slots;
        return;
      }
    }
    throw new GomRuntimeException(
        "Bad argument for OperatorTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {

writer.write(
%[
package @getPackage()@;
@generateImport()@

public final class @className()@ extends @fullClassName(extendsType)@ implements tom.library.sl.Visitable @generateInterface()@ {
@generateBlock()@
  private @className()@() {}
]%);
  if(slotList.length()>0) {
    writer.write(%[
  private int hashCode;
  private static @className()@ proto = new @className()@();
]%);
  } else { 
    writer.write(%[
  private static int hashCode = hashFunction();
  private static @className()@ proto = (@className()@) factory.build(new @className()@());
]%);
  }
  if (!hooks.isEmptyconcHook()) {
    mapping.generate(writer); 
  }
  generateMembers(writer);
  generateBody(writer);
  writer.write(%[
}
]%);
  }

  private void generateBody(java.io.Writer writer) throws java.io.IOException {
    writer.write(%[
    /* static constructor */
]%);
generateConstructor(writer);
if(slotList.length()>0) {
writer.write(%[
  private void init(@childListWithType(slotList) + (slotList.isEmptyconcSlotField()?"":", ") @int hashCode) {
]%);
generateMembersInit(writer);
writer.write(%[
    this.hashCode = hashCode;
  }

  private void initHashCode(@childListWithType(slotList)@) {
]%);
generateMembersInit(writer);
writer.write(%[
  this.hashCode = hashFunction();
  }

  /* name and arity */
  public String symbolName() {
    return "@className()@";
  }

  private int getArity() {
    return @slotList.length()@;
  }

  public shared.SharedObject duplicate() {
    @className()@ clone = new @className()@();
    clone.init(@childList(slotList) + (slotList.isEmptyconcSlotField()?"":", ") @hashCode);
    return clone;
  }

]%);
  } else {
    // case: constant
writer.write(%[
  /* name and arity */
  public String symbolName() {
    return "@className()@";
  }

  private static int getArity() {
    return 0;
  }

  public shared.SharedObject duplicate() {
    return new @className()@();
  }

]%);
  }

  /*
   * Generate a toStringBuffer method if the operator is not associative
   */
  if (sortName == extendsType) {
writer.write(%[
  public void toStringBuffer(java.lang.StringBuffer buffer) {
    buffer.append("@className()@(");
    @toStringChilds("buffer")@
    buffer.append(")");
  }
]%);
  }

writer.write(%[

  /**
    * This method implements a lexicographic order
    */
  public int compareToLPO(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    @fullClassName(abstractType)@ ao = (@fullClassName(abstractType)@) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* compare the childs */
    @genCompareChilds("ao","compareToLPO")@
    throw new RuntimeException("Unable to compare");
  }

  public int compareTo(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    @fullClassName(abstractType)@ ao = (@fullClassName(abstractType)@) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* use the hash values to discriminate */
    
    if(hashCode != ao.hashCode())
      return (hashCode < ao.hashCode())?-1:1;

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* last resort: compare the childs */
    @genCompareChilds("ao","compareTo")@
    throw new RuntimeException("Unable to compare");
  }

  /* shared.SharedObject */
  public final int hashCode() {
    return hashCode;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof @className()@) {
@generateMembersEqualityTest("peer")@
    }
    return false;
  }

  /* @className(sortName)@ interface */
  public boolean @isOperatorMethod(className)@() {
    return true;
  }
]%);
generateGetters(writer);

    writer.write(%[
  /* AbstractType */
  public aterm.ATerm toATerm() {
    return atermFactory.makeAppl(
      atermFactory.makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {@generateToATermChilds()@});
  }

  public static @fullClassName(sortName)@ fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(proto.symbolName().equals(appl.getName())) {
        return make(
@generatefromATermChilds("appl")@
        );
      }
    }
    return null;
  }

]%);

    writer.write(%[
  /* Visitable */
  public int getChildCount() {
    return @nonBuiltinChildCount()@;
  }

  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
@nonBuiltinsGetCases()@
      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
    switch(index) {
@nonBuiltinMakeCases("v")@
      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] childs) {
    if (childs.length == @nonBuiltinChildCount()@) {
      return @nonBuiltinArrayMake("childs")@;
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[] { @nonBuiltinChildList(slotList)@ };
  }
]%);

    writer.write(%[
    /* internal use */
  protected @((slotList.length()==0)?"static":"")@ int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (@shared.HashFunctions.stringHashFunction(fullClassName(),slotList.length())@<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
]%);
generateHashArgs(writer);
writer.write(%[
    a -= b; a -= c; a ^= (c >> 13);
    b -= c; b -= a; b ^= (a << 8);
    c -= a; c -= b; c ^= (b >> 13);
    a -= b; a -= c; a ^= (c >> 12);
    b -= c; b -= a; b ^= (a << 16);
    c -= a; c -= b; c ^= (b >> 5);
    a -= b; a -= c; a ^= (c >> 3);
    b -= c; b -= a; b ^= (a << 10);
    c -= a; c -= b; c ^= (b >> 15);
    /* ------------------------------------------- report the result */
    return c;
  }
]%);
  }

  private void generateMembers(java.io.Writer writer) throws java.io.IOException {
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[Name=fieldName,Domain=domainClass],_*) -> {
        writer.write("  private ");
        writer.write(fullClassName(`domainClass));
        writer.write(" ");
        writer.write(fieldName(`fieldName));
        writer.write(";\n");
      }
    }
  }

  private void generateMembersInit(java.io.Writer writer) throws java.io.IOException {
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
        writer.write("    this.");
        writer.write(fieldName(`fieldName));
        writer.write(" = ");
        writer.write(fieldName(`fieldName));
        if (GomEnvironment.getInstance().isBuiltinClass(`domain) && `domain.equals(`ClassName("","String"))) {
          writer.write(".intern()");
        }
        writer.write(";\n");
      }
    }
  }

  private void generateGetters(java.io.Writer writer) throws java.io.IOException {
    SlotFieldList slots = slotList;
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      writer.write(%[
  public @slotDomain(head)@ @getMethod(head)@() {
    return @fieldName(head.getName())@;
  }
      
  public @fullClassName(sortName)@ @setMethod(head)@(@slotDomain(head)@ set_arg) {
    return make(@generateMakeArgsFor(head,"set_arg")@);
  }]%);
    }
  }

  private String generateToATermChilds() {
    StringBuffer res = new StringBuffer();
    SlotFieldList slots = slotList;
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      if (res.length()!=0) {
        res.append(", ");
      }
      toATermSlotField(res,head);
    }
    return res.toString();
  }

  private String generatefromATermChilds(String appl) {
    StringBuffer res = new StringBuffer();
    int index = 0;
    SlotFieldList slots = slotList;
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      if (res.length()!=0) {
        res.append(", ");
      }
      fromATermSlotField(res,head, appl+".getArgument("+index+")");
      index++;
    }
    return res.toString();
  }

  private String fieldName(String fieldName) {
    return "_"+fieldName;
  }

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
          res.append(fullClassName(`domain));
          res.append(" ");
          res.append(fieldName(`name));
        }
      }
    }
    return res.toString();
  }
  private String unprotectedChildListWithType(SlotFieldList slots) {
    StringBuffer res = new StringBuffer();
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      %match(SlotField head) {
        SlotField[Name=name, Domain=domain] -> {
          if (res.length()!=0) {
            res.append(", ");
          }
          res.append(fullClassName(`domain));
          res.append(" ");
          res.append(`name);
        }
      }
    }
    return res.toString();
  }
  private String childList(SlotFieldList slots) {
    StringBuffer res = new StringBuffer();
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      %match(SlotField head) {
        SlotField[Name=name] -> {
          if (res.length()!=0) {
            res.append(", ");
          }
          res.append(" ");
          res.append(fieldName(`name));
        }
      }
    }
    return res.toString();
  }
  private String unprotectedChildList(SlotFieldList slots) {
    StringBuffer res = new StringBuffer();
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      %match(SlotField head) {
        SlotField[Name=name] -> {
          if (res.length()!=0) {
            res.append(", ");
          }
          res.append(" ");
          res.append(`name);
        }
      }
    }
    return res.toString();
  }
  private String generateMembersEqualityTest(String peer) {
    StringBuffer res = new StringBuffer();
    if(!slotList.isEmptyconcSlotField()) {
      res.append(%[
      @className()@ peer = (@className()@) obj;]%);;
    }
    res.append(%[
      return ]%);
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[Name=fieldName],_*) -> {
        res.append(fieldName(`fieldName));
        res.append("==");
        res.append(peer);
        res.append(".");
        res.append(fieldName(`fieldName));
        res.append(" && ");
      }
    }
    res.append("true;"); // to handle the "no childs" case
    return res.toString();
  }
  private int nonBuiltinChildCount() {
    int count = 0;
    //in case of reference, paths are considered as constants
    //to avoid traversal inside a path
    //TODO: in the future Gom replace this code by a hook
    if(className().equals("Conspath"+sortName.getName())) return count;
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[Domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          count++;
        }
      }
    }
    return count;
  }

  private String nonBuiltinsGetCases() {
    StringBuffer res = new StringBuffer();
    int index = 0;
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res.append("      case ");
          res.append(index);
          res.append(": return ");
          res.append(fieldName(`fieldName));
          res.append(";\n");
          index++;
        }
      }
    }
    return res.toString();
  }

  private String nonBuiltinChildList(SlotFieldList slots) {
    StringBuffer res = new StringBuffer();
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      %match(SlotField head) {
        SlotField[Domain=domain,Name=name] -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          if (res.length()!=0) {
            res.append(", ");
          }
          res.append(" ");
          res.append(fieldName(`name));
        }
        }
      }
    }
    return res.toString();
  }

  private String nonBuiltinArrayMake(String arrayName) {
    StringBuffer res = new StringBuffer("make(");
    int index = 0;
    int fullindex = 0;
    %match(SlotFieldList slotList) {
      concSlotField(_*,slot@SlotField[Domain=domain],_*) -> {
        if(fullindex>0) { res.append(", "); }
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res.append("(");
          res.append(fullClassName(`domain));
          res.append(") ");
          res.append(arrayName);
          res.append("[");
          res.append(index);
          res.append("]");
          index++;
        } else {
          res.append(getMethod(`slot));
          res.append("()");
        }
        fullindex++;
      }
    }
    res.append(")");
    return res.toString();
  }
  private String nonBuiltinMakeCases(String argName) {
    StringBuffer res = new StringBuffer();
    int index = 0;
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[Domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res.append("      case "+index+": return make("+generateMakeArgsFor(index, argName)+");\n");
          index++;
        }
      }
    }
    return res.toString();
  }
  private String generateMakeArgsFor(int argIndex, String argName) {
    StringBuffer res = new StringBuffer();
    int index = 0;
    int fullindex = 0;
    %match(SlotFieldList slotList) {
      concSlotField(_*,slot@SlotField[Name=fieldName,Domain=domain],_*) -> {
        if(fullindex>0) { res.append(", "); }
        if (GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res.append(getMethod(`slot));
          res.append("()");
        } else {
          if (index != argIndex) {
            res.append(fieldName(`fieldName));
          } else {
            res.append("(");
            res.append(fullClassName(`domain));
            res.append(") ");
            res.append(argName);
          }
          index++;
        }
        fullindex++;
      }
    }
    return res.toString();
  }
  private String generateMakeArgsFor(SlotField slot, String argName) {
    StringBuffer res = new StringBuffer();
    int fullindex = 0;
    %match(SlotFieldList slotList) {
      concSlotField(_*,itslot@SlotField[Name=fieldName,Domain=domain],_*) -> {
        if(fullindex>0) { res.append(", "); }
        if (`itslot == slot) {
          res.append(argName);
        } else {
          res.append(fieldName(`fieldName));
        }
        fullindex++;
      }
    }
    return res.toString();
  }

  private String toStringChilds(String buffer) {
    if (0 == slotList.length()) {
      return "";
    }
    StringBuffer res = new StringBuffer();
    SlotFieldList slots = slotList;
		while(!slots.isEmptyconcSlotField()) {
			if(res.length()!=0) {
				res.append(%[@buffer@.append(",");
    ]%);
			}
			SlotField head = slots.getHeadconcSlotField();
			slots = slots.getTailconcSlotField();
      toStringSlotField(res, head, fieldName(head.getName()), buffer);
		}
    return res.toString();
  }

  private String genCompareChilds(String oldOther, String compareFun) {
    StringBuffer res = new StringBuffer();
    String other = "tco";
    if(!slotList.isEmptyconcSlotField()) {
    res.append(%[@className()@ @other@ = (@className()@) @oldOther@;]%);
    }
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[Name=slotName,Domain=domain],_*) -> {
        if (GomEnvironment.getInstance().isBuiltinClass(`domain)) {
         if (`domain.equals(`ClassName("","int"))
             || `domain.equals(`ClassName("","long"))
             || `domain.equals(`ClassName("","double"))
             || `domain.equals(`ClassName("","float"))
             || `domain.equals(`ClassName("","char"))) { 
           res.append(%[
    if( this.@fieldName(`slotName)@ != @other@.@fieldName(`slotName)@)
      return (this.@fieldName(`slotName)@ < @other@.@fieldName(`slotName)@)?-1:1;
]%);
         } else if (`domain.equals(`ClassName("","boolean"))) {
           res.append(%[
    if( this.@fieldName(`slotName)@ != @other@.@fieldName(`slotName)@)
      return (!this.@fieldName(`slotName)@ && @other@.@fieldName(`slotName)@)?-1:1;
]%);
         } else if (`domain.equals(`ClassName("","String"))) {
           res.append(%[
    int @fieldName(`slotName)@Cmp = (this.@fieldName(`slotName)@).compareTo(@other@.@fieldName(`slotName)@);
    if(@fieldName(`slotName)@Cmp != 0)
      return @fieldName(`slotName)@Cmp;
             
]%);
         } else if (`domain.equals(`ClassName("aterm","ATerm"))
             ||`domain.equals(`ClassName("aterm","ATermList"))) {
           res.append(%[
    /* Inefficient total order on ATerm */
    int @fieldName(`slotName)@Cmp = ((this.@fieldName(`slotName)@).toString()).compareTo((@other@.@fieldName(`slotName)@).toString());
    if(@fieldName(`slotName)@Cmp != 0)
      return @fieldName(`slotName)@Cmp;
]%);
         } else {
            throw new GomRuntimeException("Builtin "+`domain+" not supported");
         }
        } else {
          res.append(%[
    int @fieldName(`slotName)@Cmp = (this.@fieldName(`slotName)@).@compareFun@(@other@.@fieldName(`slotName)@);
    if(@fieldName(`slotName)@Cmp != 0)
      return @fieldName(`slotName)@Cmp;
]%); 
        }
      }
    }
    return res.toString();
  }

  private void generateHashArgs(java.io.Writer writer) throws java.io.IOException {
    int index = slotList.length() - 1;
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[Name=slotName,Domain=domain],_*) -> {
        int shift = (index % 4) * 8;
        String accum = ""+"aaaabbbbcccc".toCharArray()[index % 12];
        writer.write("    "+accum+" += (");
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          writer.write(fieldName(`slotName)+".hashCode()");
        } else {
          if (`domain.equals(`ClassName("","int"))
              || `domain.equals(`ClassName("","long"))
              || `domain.equals(`ClassName("","double"))
              || `domain.equals(`ClassName("","float"))
              || `domain.equals(`ClassName("","char"))) {
            writer.write(fieldName(`slotName));
          } else if (`domain.equals(`ClassName("","boolean"))) {
            writer.write("("+fieldName(`slotName)+"?1:0)");
          } else if (`domain.equals(`ClassName("","String"))) {
            // Use the string hashFunction for Strings, and pass index as arity
            writer.write("shared.HashFunctions.stringHashFunction("+fieldName(`slotName)+", "+index+")");
          } else if (`domain.equals(`ClassName("aterm","ATerm"))||`domain.equals(`ClassName("aterm","ATermList"))) {
            // Use the string hashFunction for Strings, and pass index as arity
            writer.write(fieldName(`slotName)+".hashCode()");
          }  else {
            throw new GomRuntimeException("generateHashArgs: Builtin " + `domain + " not supported");
          }
        }
        if (shift!=0) { writer.write(" << "+(shift)); }
        writer.write(");\n");
        index--;
      }
    }
  }

  public void generateConstructor(java.io.Writer writer)
    throws java.io.IOException {

    %match(hooks) {
      /* If there is no MakeHook */
      !concHook(_*,MakeHook[],_*) -> {
        if(slotList.length()>0) {
        writer.write(%[
  public static @className()@ make(@childListWithType(slotList)@) {
    proto.initHashCode(@childList(slotList)@);
    return (@className()@) factory.build(proto);
  }
  ]%);
        } else {
        writer.write(%[
  public static @className()@ make(@childListWithType(slotList)@) {
    return proto;
  }
  ]%);
        }
      }
   
      /* If there is at least one MakeHook */
      lbl:concHook(_*,MakeHook[HookArguments=args],_*) -> {
        if(slotList.length()>0) {
        writer.write(%[
    private static @className()@ realMake(@childListWithType(slotList)@) {
      proto.initHashCode(@childList(slotList)@);
      return (@className()@) factory.build(proto);
    }
  ]%);
      } else {
        writer.write(%[
    public static @className()@ realMake(@childListWithType(slotList)@) {
      return proto;
    }
  ]%);
      }
        writer.write(%[
    public static @fullClassName(sortName)@ make(@unprotectedChildListWithType(`args)@) {
  ]%);
        SlotFieldList bargs = generateMakeHooks(hooks,null,writer);
        writer.write(%[
      return realMake(@unprotectedChildList(bargs)@);
    }
  ]%);
        break lbl;
      }
    }
  }

  public SlotFieldList generateMakeHooks(
      HookList other,
      SlotFieldList oArgs, /* will be null if it is the first hook */
      java.io.Writer writer)
    throws java.io.IOException {
    %match(other) {
      concHook(!MakeHook[],tail*) -> {
        /* skip non Make hooks */
        return generateMakeHooks(`tail, oArgs, writer);
      }
      concHook(MakeHook(args, code),tail*) -> {
        /* Rename the previous arguments according to new, if needed */
        if(oArgs != null && oArgs != `args) {
          recVarNameRemap(oArgs,`args, writer);
        }
        /* Make sure we defeat java dead code detection */
        writer.write("if (true) {");
        CodeGen.generateCode(`code,writer);
        writer.write("}");
        return generateMakeHooks(`tail, `args, writer);
      }
    }
    return oArgs;
  }

  private void recVarNameRemap(
      SlotFieldList oargs,
      SlotFieldList nargs,
      java.io.Writer writer)
  throws java.io.IOException {
    %match(oargs, nargs) {
      concSlotField(),concSlotField() -> {
        return ;
      }
      concSlotField(SlotField[Name=oargName,Domain=odomain],to*),
      concSlotField(SlotField[Name=nargName,Domain=ndomain],tn*) -> {
        if (!(`odomain==`ndomain)) {
          throw new GomRuntimeException(
              "OperatorTemplate: incompatible args "+
              "should be rejected by typechecker");
        } else if (!`oargName.equals(`nargName)) {
          /* XXX: the declaration should be omitted if nargName was previously
           * used */
          writer.write(%[
    @fullClassName(`ndomain)@ @`nargName@ = @`oargName@;
]%);
        } /* else nothing to rename */
        recVarNameRemap(`to,`tn, writer);
        return;
      }
    }
    throw new GomRuntimeException(
        "OperatorTemplate:recVarNameRemap failed " + oargs + " " + nargs);
  }

  public void generateTomMapping(Writer writer, ClassName basicStrategy)
      throws java.io.IOException {
    %match(hooks) {
      !concHook(_*,MappingHook[],_*) -> {
        writer.write("%op "+className(sortName)+" "+className()+"(");
        slotDecl(writer,slotList);
        writer.write(") {\n");
        writer.write("  is_fsym(t) { t instanceof "+fullClassName()+" }\n");
        %match(slotList) {
          concSlotField(_*,slot@SlotField[Name=slotName],_*) -> {
            writer.write("  get_slot("+`slotName+", t) ");
            writer.write("{ t."+getMethod(`slot)+"() }\n");
          }
        }
        writer.write("  make(");
        slotArgs(writer,slotList);
        writer.write(") { ");
        writer.write(fullClassName());
        writer.write(".make(");
        slotArgs(writer,slotList);
        writer.write(") }\n");
        writer.write("}\n");
        writer.write("\n");
        return;
      }
      concHook(_*,MappingHook[Code=code],_*) -> {
        CodeGen.generateCode(`code,writer);
      }
    }
    return;
  }
}
