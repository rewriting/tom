/*
 * Gom
 *
 * Copyright (c) 2006-2008, INRIA
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
  SlotFieldList slotList;
  boolean multithread;
  boolean maximalsharing;

  %include { ../../adt/objects/Objects.tom}

  public OperatorTemplate(File tomHomePath,
                          OptionManager manager,
                          List importList, 	
                          GomClass gomClass,
                          TemplateClass mapping,
                          boolean multithread,
                          boolean maximalsharing) {
    super(gomClass,manager,tomHomePath,importList,mapping);
    this.multithread = multithread;
    this.maximalsharing = maximalsharing;
    %match(gomClass) {
      OperatorClass[AbstractType=abstractType,
                    ExtendsType=extendsType,
                    SortName=sortName,
                    SlotFields=slots] -> {
        this.abstractType = `abstractType;
        this.extendsType = `extendsType;;
        this.sortName = `sortName;
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
]%);

if (maximalsharing) {
  writer.write(
%[
public final class @className()@ extends @fullClassName(extendsType)@ implements tom.library.sl.Visitable @generateInterface()@ {
  @generateBlock()@
  private static String symbolName = "@className()@";
]%);


  if(slotList.length() > 0) {
    writer.write(%[

  private @className()@() {}
  private int hashCode;
  private static @className()@ proto = new @className()@();
  ]%);
  } else {
    writer.write(%[

  private @className()@() {}
  private static int hashCode = hashFunction();
  private static @className()@ proto = (@className()@) factory.build(new @className()@());
  ]%);
  }
} else {
  writer.write(
%[
public final class @className()@ extends @fullClassName(extendsType)@ implements Cloneable, tom.library.sl.Visitable @generateInterface()@ {
  @generateBlock()@
  private static String symbolName = "@className()@";
]%);


// generate a private constructor that takes as arguments the operator arguments
  writer.write(%[

  private @className()@(@childListWithType(slotList)@) {
  ]%);
  generateMembersInit(writer);
  writer.write(%[
  }
  ]%);
}

  if (!hooks.isEmptyConcHook()) {
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

if (maximalsharing) {
writer.write(%[
  private void init(@childListWithType(slotList) + (slotList.isEmptyConcSlotField()?"":", ") @int hashCode) {
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
]%);
}

writer.write(%[
  /* name and arity */
  @@Override
  public String symbolName() {
    return "@className()@";
  }

  private int getArity() {
    return @slotList.length()@;
  }
]%);


if (maximalsharing) {
if(multithread) {
  writer.write(%[
  public shared.SharedObject duplicate() {
    // the proto is a fresh object: no need to clone it again
    return this;
  }
  ]%);
} else {
  writer.write(%[
  public shared.SharedObject duplicate() {
    @className()@ clone = new @className()@();
    clone.init(@childList(slotList) + (slotList.isEmptyConcSlotField()?"":", ") @hashCode);
    return clone;
  }
  ]%);
 }
}

} else {
    // case: constant
writer.write(%[
  /* name and arity */
  @@Override
  public String symbolName() {
    return "@className()@";
  }

  private static int getArity() {
    return 0;
  }

]%);

if (maximalsharing) {
writer.write(%[
  public shared.SharedObject duplicate() {
    // the proto is a constant object: no need to clone it
    return this;
    //return new @className()@();
  }
]%);
}

  }

  /*
   * Generate a toStringBuilder method if the operator is not associative
   */
  if (sortName == extendsType) {
writer.write(%[
  @@Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
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
  @@Override
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
]%);

if (maximalsharing) {
writer.write(%[
  @@Override
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

 //shared.SharedObject
  @@Override
  public final int hashCode() {
    return hashCode;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof @className()@) {
@generateMembersEqualityTest("peer")@
    }
    return false;
  }

]%);
} else {
  //XXX: compareTo must be correctly implemented
writer.write(%[
  @@Override
  public int compareTo(Object o) {
    throw new UnsupportedOperationException("Unable to compare"); 
  }

  @@Override
  public Object clone() {
]%);

SlotFieldList slots = slotList;
if(slots.isEmptyConcSlotField()) {
  writer.write(%[
      return new @className()@();
  }
      ]%);
} else {

SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();

if (GomEnvironment.getInstance().isBuiltinClass(head.getDomain())) {
  writer.write(%[
      return new @className()@(@getMethod(head)@()]%);

} else {
  writer.write(%[
      return new @className()@( (@fullClassName(head.getDomain())@) @getMethod(head)@().clone()]%);
}

while(!slots.isEmptyConcSlotField()) {
  head = slots.getHeadConcSlotField();
  slots = slots.getTailConcSlotField();
  if (GomEnvironment.getInstance().isBuiltinClass(head.getDomain())) {
   writer.write(%[,@getMethod(head)@()]%);
  } else {
  writer.write(%[,(@fullClassName(head.getDomain())@) @getMethod(head)@().clone()]%);
  }
}
writer.write(");\n}");
}

writer.write(%[
  @@Override
  public final boolean deepEquals(Object o) {
    if (o instanceof @className()@) {
      @className()@ typed_o = (@className()@) o;
]%);

slots = slotList;
if(slots.isEmptyConcSlotField()) {
  writer.write(%[
      return true;
      ]%);
} else {
SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();
if (GomEnvironment.getInstance().isBuiltinClass(head.getDomain())) {
  writer.write(%[
      return @fieldName(head.getName())@ == typed_o.@getMethod(head)@()
      ]%);

} else {
  writer.write(%[
      return @fieldName(head.getName())@.deepEquals(typed_o.@getMethod(head)@())
      ]%);
}

while(!slots.isEmptyConcSlotField()) {
  head = slots.getHeadConcSlotField();
  slots = slots.getTailConcSlotField();
  if (GomEnvironment.getInstance().isBuiltinClass(head.getDomain())) {
    writer.write(%[
        && @fieldName(head.getName())@ == typed_o.@getMethod(head)@()
        ]%);

  } else {
    writer.write(%[
      && @fieldName(head.getName())@.deepEquals(typed_o.@getMethod(head)@())
      ]%);
  }
}
writer.write(";");
}
writer.write(%[
    }
    return false;
    }
  ]%);
}

writer.write(%[
   //@className(sortName)@ interface
  @@Override
  public boolean @isOperatorMethod(className)@() {
    return true;
  }
  ]%);

generateGetters(writer);

    writer.write(%[
  /* AbstractType */
  @@Override
  public aterm.ATerm toATerm() {
    return atermFactory.makeAppl(
      atermFactory.makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {@generateToATermChilds()@});
  }

  public static @fullClassName(sortName)@ fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName())) {
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
    return @visitableCount()@;
  }

  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
@getCases()@
      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
    switch(index) {
@makeCases("v")@
      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] childs) {
    if (childs.length == @slotList.length()@) {
      return @arrayMake("childs")@;
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[] { @visitableList(slotList)@ };
  }
]%);

if (maximalsharing) {
    writer.write(%[
    /* internal use */
  protected@((slotList.length()==0)?" static":"")@ int hashFunction() {
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

}

  private void generateMembers(java.io.Writer writer) throws java.io.IOException {
    %match(SlotFieldList slotList) {
      ConcSlotField(_*,SlotField[Name=fieldName,Domain=domainClass],_*) -> {
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
      ConcSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
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
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      writer.write(%[
  @@Override
  public @slotDomain(head)@ @getMethod(head)@() {
    return @fieldName(head.getName())@;
  }
  ]%);
      if (maximalsharing) {
      writer.write(%[
  @@Override
  public @fullClassName(sortName)@ @setMethod(head)@(@slotDomain(head)@ set_arg) {
    return make(@generateMakeArgsFor(head,"set_arg")@);
  }]%);
      } else {
      writer.write(%[
  @@Override
  public @fullClassName(sortName)@ @setMethod(head)@(@slotDomain(head)@ set_arg) {
    @fieldName(head.getName())@ = set_arg; 
    return this;
  }]%);
      }
    }
  }

  private String generateToATermChilds() {
    StringBuilder res = new StringBuilder();
    SlotFieldList slots = slotList;
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      if (res.length()!=0) {
        res.append(", ");
      }
      toATermSlotField(res,head);
    }
    return res.toString();
  }

  private String generatefromATermChilds(String appl) {
    StringBuilder res = new StringBuilder();
    int index = 0;
    SlotFieldList slots = slotList;
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
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
    StringBuilder res = new StringBuilder();
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
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
    StringBuilder res = new StringBuilder();
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
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
    StringBuilder res = new StringBuilder();
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
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
    StringBuilder res = new StringBuilder();
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
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
    StringBuilder res = new StringBuilder();
    if(!slotList.isEmptyConcSlotField()) {
      res.append(%[
      @className()@ peer = (@className()@) obj;]%);;
    }
    res.append(%[
      return ]%);
    %match(SlotFieldList slotList) {
      ConcSlotField(_*,SlotField[Name=fieldName],_*) -> {
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

  private String getCases() {
    StringBuilder res = new StringBuilder();
    int index = 0;
    %match(SlotFieldList slotList) {
      ConcSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res.append("      case ");
          res.append(index);
          res.append(": return ");
          res.append(fieldName(`fieldName));
          res.append(";\n");
          index++;
        } else {
          res.append("      case ");
          res.append(index);
          res.append(": return new tom.library.sl.VisitableBuiltin<");
          res.append(primitiveToReferenceType(fullClassName(`domain)));
          res.append(">(");
          res.append(fieldName(`fieldName));
          res.append(");\n");
          index++;
        }
      }
    }
    return res.toString();
  }

  private String visitableList(SlotFieldList slots) {
    StringBuilder res = new StringBuilder();
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      %match(SlotField head) {
        SlotField[Domain=domain,Name=name] -> {
          if (res.length()!=0) {
            res.append(", ");
          }
          res.append(" ");
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res.append(fieldName(`name));
        } else {
          res.append("new tom.library.sl.VisitableBuiltin<");
          res.append(primitiveToReferenceType(fullClassName(`domain)));
          res.append(">(");
          res.append(fieldName(`name));
          res.append(")");
        }
        }
      }
    }
    return res.toString();
  }


  private String visitableCount() {
    if(className().equals("ConsPath"+sortName.getName())) { 
      return "0";
    } else {
      return ""+slotList.length();
    }
  }

  private String arrayMake(String arrayName) {
    StringBuilder res = new StringBuilder("make(");
    int index = 0;
    %match(SlotFieldList slotList) {
      ConcSlotField(_*,SlotField[Domain=domain],_*) -> {
        if(index>0) { res.append(", "); }
       if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
         res.append("(");
         res.append(fullClassName(`domain));
         res.append(") ");
         res.append(arrayName);
         res.append("[");
         res.append(index);
         res.append("]");
       } else {
         res.append("((tom.library.sl.VisitableBuiltin<");
         res.append(primitiveToReferenceType(fullClassName(`domain)));
         res.append(">)");
         res.append(arrayName);
         res.append("[");
         res.append(index);
         res.append("])");
         res.append(".getBuiltin()");
       }
       index++;
      }
    }
    res.append(")");
    return res.toString();
  }
private String makeCases(String argName) {
  StringBuilder res = new StringBuilder();
  int index = 0;
  %match(slotList) {
    ConcSlotField(_*,SlotField[],_*) -> {
      res.append("      case "+index+": return make("+generateMakeArgsFor(index, argName)+");\n");
      index++;
    }
  }
  return res.toString();
}
private String generateMakeArgsFor(int argIndex, String argName) {
  StringBuilder res = new StringBuilder();
  int index = 0;
  %match(SlotFieldList slotList) {
    ConcSlotField(_*,slot@SlotField[Name=fieldName,Domain=domain],_*) -> {
      if(index>0) { res.append(", "); }
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
      }
      index++;
    }
  }
  return res.toString();
}
private String generateMakeArgsFor(SlotField slot, String argName) {
  StringBuilder res = new StringBuilder();
  int fullindex = 0;
  %match(SlotFieldList slotList) {
    ConcSlotField(_*,itslot@SlotField[Name=fieldName],_*) -> {
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
    StringBuilder res = new StringBuilder();
    SlotFieldList slots = slotList;
		while(!slots.isEmptyConcSlotField()) {
			if(res.length()!=0) {
				res.append(%[@buffer@.append(",");
    ]%);
			}
			SlotField head = slots.getHeadConcSlotField();
			slots = slots.getTailConcSlotField();
      toStringSlotField(res, head, fieldName(head.getName()), buffer);
		}
    return res.toString();
  }

  private String genCompareChilds(String oldOther, String compareFun) {
    StringBuilder res = new StringBuilder();
    String other = "tco";
    if(!slotList.isEmptyConcSlotField()) {
    res.append(%[@className()@ @other@ = (@className()@) @oldOther@;]%);
    }
    %match(SlotFieldList slotList) {
      ConcSlotField(_*,SlotField[Name=slotName,Domain=domain],_*) -> {
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
      ConcSlotField(_*,SlotField[Name=slotName,Domain=domain],_*) -> {
        int shift = (index % 4) * 8;
        String accum = ""+"aaaabbbbcccc".toCharArray()[index % 12];
        writer.write("    "+accum+" += (");
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          writer.write(fieldName(`slotName)+".hashCode()");
        } else {
          if (`domain.equals(`ClassName("","int"))
              || `domain.equals(`ClassName("","long"))
              || `domain.equals(`ClassName("","float"))
              || `domain.equals(`ClassName("","char"))) {
            writer.write(fieldName(`slotName));
          } else if (`domain.equals(`ClassName("","boolean"))) {
            writer.write("("+fieldName(`slotName)+"?1:0)");
          } else if (`domain.equals(`ClassName("","String"))) {
            // Use the string hashFunction for Strings, and pass index as arity
            writer.write("shared.HashFunctions.stringHashFunction("+fieldName(`slotName)+", "+index+")");
          } else if (`domain.equals(`ClassName("","double"))) {
            writer.write("(int)(java.lang.Double.doubleToLongBits(");
            writer.write(fieldName(`slotName));
            writer.write(")^(java.lang.Double.doubleToLongBits(");
            writer.write(fieldName(`slotName));
            writer.write(")>>>32");
            writer.write("))");
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

  public void generateConstructor(java.io.Writer writer) throws java.io.IOException {
    boolean hasHooks = false;
    %match(hooks) {
      /* If there is at least one MakeHook */
lbl:ConcHook(_*,MakeHook[HookArguments=args],_*) -> {
      hasHooks = true;
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
    
    String makeName = "make";
    String visibility = "public";
    if (hasHooks) {
      makeName = "realMake";
      visibility = "private";
    }
    writer.write(%[
  @visibility@ static @className()@ @makeName@(@childListWithType(slotList)@) {
]%);
    
    if (! maximalsharing) {
        writer.write(%[
    return new @className()@(@childList(slotList)@);
    ]%);
    } else {
    if(slotList.length()>0) {
      if(multithread) {
        writer.write(%[
    // allocate and object and make duplicate equal identity
    @className()@ newProto = new @className()@();
    newProto.initHashCode(@childList(slotList)@);
    return (@className()@) factory.build(newProto);
]%);
      } else {
        writer.write(%[
    // use the proto as a model
    proto.initHashCode(@childList(slotList)@);
    return (@className()@) factory.build(proto);
]%);
      }
    } else {
        writer.write(%[
    return proto;
]%);
    }
    }
    writer.write(%[
  }
]%);
}

  public SlotFieldList generateMakeHooks(
      HookList other,
      SlotFieldList oArgs, /* will be null if it is the first hook */
      java.io.Writer writer)
    throws java.io.IOException {
    %match(other) {
      ConcHook(!MakeHook[],tail*) -> {
        /* skip non Make hooks */
        return generateMakeHooks(`tail, oArgs, writer);
      }
      ConcHook(MakeHook(args, code),tail*) -> {
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
      ConcSlotField(),ConcSlotField() -> {
        return ;
      }
      ConcSlotField(SlotField[Name=oargName,Domain=odomain],to*),
      ConcSlotField(SlotField[Name=nargName,Domain=ndomain],tn*) -> {
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

  public void generateTomMapping(Writer writer)
      throws java.io.IOException {
    %match(hooks) {
      !ConcHook(_*,MappingHook[],_*) -> {
        writer.write("%op "+className(sortName)+" "+className()+"(");
        slotDecl(writer,slotList);
        writer.write(") {\n");
        writer.write("  is_fsym(t) { ($t instanceof "+fullClassName()+") }\n");
        %match(slotList) {
          ConcSlotField(_*,slot@SlotField[Name=slotName],_*) -> {
            writer.write("  get_slot("+`slotName+", t) ");
            writer.write("{ $t."+getMethod(`slot)+"() }\n");
          }
        }
        writer.write("  make(");
        slotArgs(writer,slotList);
        writer.write(") { ");
        writer.write(fullClassName());
        writer.write(".make(");
        slotArgsWithDollar(writer,slotList);
        writer.write(") }\n");
        writer.write("}\n");
        writer.write("\n");
        return;
      }
      ConcHook(_*,MappingHook[Code=code],_*) -> {
        CodeGen.generateCode(`code,writer);
      }
    }
    return;
  }
}
