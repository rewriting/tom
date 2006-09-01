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

package tom.gom.backend.shared;

import java.io.*;
import java.util.logging.*;
import tom.gom.backend.TemplateClass;
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;

public class OperatorTemplate extends TemplateClass {
  ClassName abstractType;
  ClassName extendsType;
  ClassName sortName;
  ClassName visitor;
  SlotFieldList slotList;
  HookList hooks;
  TemplateClass mapping;

  %include { ../../adt/objects/Objects.tom}

  public OperatorTemplate(ClassName className,
                          ClassName abstractType,
                          ClassName extendsType,
                          ClassName sortName,
                          ClassName visitor,
                          SlotFieldList slots,
                          HookList hooks,
                          TemplateClass mapping) {
    super(className);
    this.abstractType = abstractType;
    this.extendsType = extendsType;;
    this.sortName = sortName;
    this.visitor = visitor;
    this.slotList = slots;
    this.hooks = hooks;
    this.mapping = mapping;
  }

  public String generate() {

    String classBody = %[
package @getPackage()@;

public class @className()@ extends @fullClassName(extendsType)@ implements tom.library.strategy.mutraveler.MuVisitable {
  private static @className()@ proto = new @className()@();
  private int hashCode;
  private @className()@() {}

@generateMembers()@

@generateBody()@

}
]%;

    return classBody;
  }

  private String generateBody() {
    StringBuffer out = new StringBuffer();

    out.append(%[
    /* static constructor */
@generateConstructor()@

  private void init(@childListWithType(slotList) + (slotList.isEmptyconcSlotField()?"":", ") @int hashCode) {
@generateMembersInit()@
    this.hashCode = hashCode;
  }

  private void initHashCode(@childListWithType(slotList)@) {
@generateMembersInit()@
  this.hashCode = hashFunction();
  }

  /* name and arity */
  public String symbolName() {
    return "@className()@";
  }

  private int getArity() {
    return @getLength(slotList)@;
  }

  public String toString() {
    return "@className()@(@toStringChilds()@)";
  }

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
    
    if(this.hashCode != ao.hashCode())
      return  (this.hashCode < ao.hashCode())?-1:1;

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

  public shared.SharedObject duplicate() {
    @className()@ clone = new @className()@();
    clone.init(@childList(slotList) + (slotList.isEmptyconcSlotField()?"":", ") @hashCode);
    return clone;
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

@generateGetters()@

]%);

    out.append(%[
  /* AbstractType */
  public aterm.ATerm toATerm() {
    return aterm.pure.SingletonFactory.getInstance().makeAppl(
      aterm.pure.SingletonFactory.getInstance().makeAFun(symbolName(),getArity(),false),
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

    out.append(%[
  /* jjtraveler.Visitable */
  public int getChildCount() {
    return @nonBuiltinChildCount()@;
  }

  public jjtraveler.Visitable getChildAt(int index) {
    switch(index) {
@nonBuiltinsGetCases()@
      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    switch(index) {
@nonBuiltinMakeCases("v")@
      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChilds(jjtraveler.Visitable[] childs) {
    if (childs.length == @nonBuiltinChildCount()@) {
      return @nonBuiltinArrayMake("childs")@;
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

]%);

    out.append(%[
      /* internal use */
  protected int hashFunction() {
    int a, b, c;

    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (@shared.HashFunctions.stringHashFunction(fullClassName(),getLength(slotList))@<<8);
    c = getArity();
    /*---------------------------------------- handle most of the key */

    /*------------------------------------- handle the last 11 bytes */

@generateHashArgs()@

    a -= b;
    a -= c;
    a ^= (c >> 13);
    b -= c;
    b -= a;
    b ^= (a << 8);
    c -= a;
    c -= b;
    c ^= (b >> 13);
    a -= b;
    a -= c;
    a ^= (c >> 12);
    b -= c;
    b -= a;
    b ^= (a << 16);
    c -= a;
    c -= b;
    c ^= (b >> 5);
    a -= b;
    a -= c;
    a ^= (c >> 3);
    b -= c;
    b -= a;
    b ^= (a << 10);
    c -= a;
    c -= b;
    c ^= (b >> 15);

    /*-------------------------------------------- report the result */
    return c;
  }
]%);
    return out.toString();
  }

  private String generateMembers() {
    String res="";
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=fieldName,domain=domainClass],_*) -> {
        res += "  private "+fullClassName(`domainClass)+" "+fieldName(`fieldName)+";\n";
      }
    }
    return res;
  }
  private String generateMembersInit() {
    String res = "";
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=fieldName,domain=domain],_*) -> {
        if (GomEnvironment.getInstance().isBuiltinClass(`domain) && `domain.equals(`ClassName("","String"))) {
          res += "    this."+fieldName(`fieldName)+" = "+fieldName(`fieldName)+".intern();\n";
        } else {
          res += "    this."+fieldName(`fieldName)+" = "+fieldName(`fieldName)+";\n";
        }
      }
    }
    return res;
  }

  private String generateGetters() {
    String res = "";
    SlotFieldList slots = slotList;
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      res+= %[
  public @slotDomain(head)@ @getMethod(head)@() {
    return @fieldName(head.getname())@;
  }
      
  public @fullClassName(sortName)@ @setMethod(head)@(@slotDomain(head)@ set_arg) {
    return make(@generateMakeArgsFor(head,"set_arg")@);
  }]%;
    }
    return res;
  }

  private String generateToATermChilds() {
    String res = "";
    SlotFieldList slots = slotList;
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      if (!res.equals("")) {
        res+= ", ";
      }
      res+= toATermSlotField(head);
    }
    return res;
  }
  private String toATermSlotField(SlotField slot) {
    String res = "";
    %match(SlotField slot) {
      SlotField[domain=domain] -> {
        if(!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += getMethod(slot)+"().toATerm()";
        } else {
          if (`domain.equals(`ClassName("","int"))) {
            res += "(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeInt("+getMethod(slot)+"())";
          } else if (`domain.equals(`ClassName("","long"))) {
            res += "(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeReal("+getMethod(slot)+"())";
          } else if (`domain.equals(`ClassName("","double"))) {
            res += "(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeReal("+getMethod(slot)+"())";
          } else if (`domain.equals(`ClassName("","float"))) {
            res += "(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeReal("+getMethod(slot)+"())";
          } else if (`domain.equals(`ClassName("","char"))) {
            res += "(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeInt("+getMethod(slot)+"())";
          } else if (`domain.equals(`ClassName("","String"))) {
            res += "(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeAppl(";
            res += "aterm.pure.SingletonFactory.getInstance().makeAFun(";
            res += getMethod(slot)+"() ,0 , true))";
          } else if (`domain.equals(`ClassName("aterm","ATerm")) ||`domain.equals(`ClassName("aterm","ATermList"))){
            res += getMethod(slot)+"()";
          }
            else {
            throw new GomRuntimeException("Builtin " + `domain + " not supported");
          }
        }
      }
    }
    return res;
  }

  private String generatefromATermChilds(String appl) {
    String res = "";
    int index = 0;
    SlotFieldList slots = slotList;
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      if (!res.equals("")) {
        res+= ", ";
      }
      res+= fromATermSlotField(head, appl, index);
      index++;
    }
    return res;
  }
  private String fromATermSlotField(SlotField slot, String appl, int index) {
    String res = "          ";
    %match(SlotField slot) {
      SlotField[domain=domain] -> {
        if(!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res+= fullClassName(`domain)+".fromTerm("+appl+".getArgument("+index+"))";
        } else {
          if (`domain.equals(`ClassName("","int"))) {
            res+= "((aterm.ATermInt)"+appl+".getArgument("+index+")).getInt()";
          } else  if (`domain.equals(`ClassName("","float"))) {
            res+= "(float) ((aterm.ATermReal)"+appl+".getArgument("+index+")).getReal()";
          } else  if (`domain.equals(`ClassName("","long"))) {
            res+= "(long) ((aterm.ATermReal)"+appl+".getArgument("+index+")).getReal()";
          } else  if (`domain.equals(`ClassName("","double"))) {
            res+= "((aterm.ATermReal)"+appl+".getArgument("+index+")).getReal()";
          } else  if (`domain.equals(`ClassName("","char"))) {
            res+= "(char) ((aterm.ATermInt)"+appl+".getArgument("+index+")).getInt()";
          } else if (`domain.equals(`ClassName("","String"))) {
            res+= "(String)((aterm.ATermAppl)"+appl+".getArgument("+index+")).getAFun().getName()";
          } else if (`domain.equals(`ClassName("aterm","ATerm")) || `domain.equals(`ClassName("aterm","ATermList")) ){
            res +=  appl+".getArgument("+index+")";
          }
            else {
            throw new GomRuntimeException("Builtin " + `domain + " not supported");
          }
        }
      }
    }
    return res;
  }

  private String fieldName(String fieldName) {
    return "_"+fieldName;
  }
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
          res+= fullClassName(`domain) + " "+fieldName(`name);
        }
      }
    }
    return res;
  }
  private String unprotectedChildListWithType(SlotFieldList slots) {
    String res = "";
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      %match(SlotField head) {
        SlotField[name=name, domain=domain] -> {
          if (!res.equals("")) {
            res+= ", ";
          }
          res+= fullClassName(`domain) + " "+`name;
        }
      }
    }
    return res;
  }
  private String childList(SlotFieldList slots) {
    String res = "";
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      %match(SlotField head) {
        SlotField[name=name] -> {
          if (!res.equals("")) {
            res+= ", ";
          }
          res+= " "+fieldName(`name);
        }
      }
    }
    return res;
  }
  private String unprotectedChildList(SlotFieldList slots) {
    String res = "";
    while(!slots.isEmptyconcSlotField()) {
      SlotField head = slots.getHeadconcSlotField();
      slots = slots.getTailconcSlotField();
      %match(SlotField head) {
        SlotField[name=name] -> {
          if (!res.equals("")) {
            res+= ", ";
          }
          res+= " "+`name;
        }
      }
    }
    return res;
  }
  private String generateMembersEqualityTest(String peer) {
    String res = "";
    if(!slotList.isEmptyconcSlotField()) {
      res += %[
      @className()@ peer = (@className()@) obj;]%;
    }
    res += %[
      return ]%;
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=fieldName],_*) -> {
        res += fieldName(`fieldName)+"=="+peer+"."+fieldName(`fieldName);
        res+= " && ";
      }
    }
    res += "true;"; // to handle the "no childs" case
    return res;
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

  private String nonBuiltinArrayMake(String arrayName) {
    String res = "make(";
    int index = 0;
    int fullindex = 0;
    %match(SlotFieldList slotList) {
      concSlotField(_*,slot@SlotField[domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += "("+fullClassName(`domain)+") "+arrayName+"["+index+"], ";
          index++;
        } else {
          res += getMethod(`slot)+"(), ";
        }
        fullindex++;
      }
    }
    if (fullindex>0) {
      res = res.substring(0,res.length()-2);
    }
    res += ")";
    return res;
  }
  private String nonBuiltinMakeCases(String argName) {
    String res = "";
    int index = 0;
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += "      case "+index+": return make("+generateMakeArgsFor(index, argName)+");\n";
          index++;
        }
      }
    }
    return res;
  }
  private String generateMakeArgsFor(int argIndex, String argName) {
    String res = "";
    int index = 0;
    %match(SlotFieldList slotList) {
      concSlotField(_*,slot@SlotField[name=fieldName,domain=domain],_*) -> {
        if (GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += getMethod(`slot)+"(), ";
        } else {
          if (index != argIndex) {
            res += fieldName(`fieldName)+", ";
          } else {
            res += "("+fullClassName(`domain)+") " + argName+", ";
          }
          index++;
        }
      }
    }
    return res.substring(0,res.length()-2);
  }
  private String generateMakeArgsFor(SlotField slot, String argName) {
    String res = "";
    int index = 0;
    %match(SlotFieldList slotList) {
      concSlotField(_*,itslot@SlotField[name=fieldName,domain=domain],_*) -> {
        if (`itslot == slot) {
          res += argName+", ";
        } else {
          res += fieldName(`fieldName)+", ";
        }
      }
    }
    return res.substring(0,res.length()-2);
  }

  private String toStringChilds() {
    String res = "";
    if (0 == slotList.length()) {
      return res;
    }
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=slotName,domain=domain],_*) -> {
        if (GomEnvironment.getInstance().isBuiltinClass(`domain)) {
         if (`domain.equals(`ClassName("","int")) || `domain.equals(`ClassName("","long")) || `domain.equals(`ClassName("","double")) || `domain.equals(`ClassName("","float")) || `domain.equals(`ClassName("","char"))) { 
           res+= %["+@fieldName(`slotName)@+"]%;
         } else if (`domain.equals(`ClassName("","String"))) {
           res+= %[\""+@fieldName(`slotName)@+"\"]%;
         } else if (`domain.equals(`ClassName("aterm","ATerm")) ||`domain.equals(`ClassName("aterm","ATermList"))) {
           res+= %["+@fieldName(`slotName)@.toString()+"]%;
         } else {
            throw new GomRuntimeException("Builtin "+`domain+" not supported");
         }
        } else {
          res+= %["+@fieldName(`slotName)@.toString()+"]%; 
        }
        res += ",";
      }
    }
    return res.substring(0,res.length()-1);
  }

  private String genCompareChilds(String oldOther, String compareFun) {
    String res = "";
    String other = "tco";
    if(!slotList.isEmptyconcSlotField()) {
    res += %[@className()@ @other@ = (@className()@) @oldOther@;]%;
    }
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=slotName,domain=domain],_*) -> {
        if (GomEnvironment.getInstance().isBuiltinClass(`domain)) {
         if (`domain.equals(`ClassName("","int"))|| `domain.equals(`ClassName("","long")) || `domain.equals(`ClassName("","double")) || `domain.equals(`ClassName("","float")) || `domain.equals(`ClassName("","char"))) { 
           res+= %[
    if( this.@fieldName(`slotName)@ != @other@.@fieldName(`slotName)@)
      return (this.@fieldName(`slotName)@ < @other@.@fieldName(`slotName)@)?-1:1;
]%;
         } else if (`domain.equals(`ClassName("","String"))) {
           res+= %[
    int @fieldName(`slotName)@Cmp = (this.@fieldName(`slotName)@).compareTo(@other@.@fieldName(`slotName)@);
    if(@fieldName(`slotName)@Cmp != 0)
      return @fieldName(`slotName)@Cmp;
             
]%;
         } else if (`domain.equals(`ClassName("aterm","ATerm")) ||`domain.equals(`ClassName("aterm","ATermList"))) {
           res+= %[
    /* Inefficient total order on ATerm */
    int @fieldName(`slotName)@Cmp = ((this.@fieldName(`slotName)@).toString()).compareTo((@other@.@fieldName(`slotName)@).toString());
    if(@fieldName(`slotName)@Cmp != 0)
      return @fieldName(`slotName)@Cmp;
]%;
         } else {
            throw new GomRuntimeException("Builtin "+`domain+" not supported");
         }
        } else {
          res+= %[
    int @fieldName(`slotName)@Cmp = (this.@fieldName(`slotName)@).@compareFun@(@other@.@fieldName(`slotName)@);
    if(@fieldName(`slotName)@Cmp != 0)
      return @fieldName(`slotName)@Cmp;
]%; 
        }
      }
    }
    return res;
  }

  private String generateHashArgs() {
    String res = "";
    int index = getLength(slotList) - 1;
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=slotName,domain=domain],_*) -> {
        int shift = (index % 4) * 8;
        String accum = ""+"aaaabbbbcccc".toCharArray()[index % 12];
        res += "    "+accum+" += (";
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += fieldName(`slotName)+".hashCode()";
        } else {
          if (`domain.equals(`ClassName("","int")) || `domain.equals(`ClassName("","long")) || `domain.equals(`ClassName("","double")) || `domain.equals(`ClassName("","float")) || `domain.equals(`ClassName("","char"))) {
            res+= fieldName(`slotName);
          } else if (`domain.equals(`ClassName("","String"))) {
            // Use the string hashFunction for Strings, and pass index as arity
            res+= "shared.HashFunctions.stringHashFunction("+fieldName(`slotName)+", "+index+")";
          } else if (`domain.equals(`ClassName("aterm","ATerm"))||`domain.equals(`ClassName("aterm","ATermList"))) {
            // Use the string hashFunction for Strings, and pass index as arity
            res+= fieldName(`slotName)+".hashCode()";
          }  else {
            throw new GomRuntimeException("generateHashArgs: Builtin " + `domain + " not supported");
          }
        }
        if (shift!=0) { res += " << "+(shift); }
        res += ");\n";
        index--;
      }
    }
    return res;
  }

  public String generateConstructor() {
    StringBuffer out = new StringBuffer();
    if (hooks.isEmptyconcHook()) {
      out.append(%[
  public static @className()@ make(@childListWithType(slotList)@) {
    proto.initHashCode(@childList(slotList)@);
    return (@className()@) shared.SingletonSharedObjectFactory.getInstance().build(proto);
  }

]%);
    } else { // we have to generate an hidden "real" make
      out.append(%[
  private static @className()@ realMake(@childListWithType(slotList)@) {
    proto.initHashCode(@childList(slotList)@);
    return (@className()@) shared.SingletonSharedObjectFactory.getInstance().build(proto);
  }

]%);
      if(getLength(hooks) > 1) {
        throw new GomRuntimeException("Support for multiple hooks for an operator not implemented yet");
      }
      // then a make function calling it
      %match(HookList hooks) {
        concHook(MakeHook(args,code)) -> {
          // replace the inner make call
          out.append(%[
  public static @fullClassName(sortName)@ make(@unprotectedChildListWithType(`args)@) {
    @`code@
    return realMake(@unprotectedChildList(`args)@);
  }

]%);
        }
      }
      // also generate the tom mapping
     out.append(mapping.generate()); 
    }
    return out.toString();
  }

  /*
   * The function for generating the file is extended, to be able to call Tom if necessary
   * (i.e. if there are user defined hooks)
   */
  public int generateFile() {
    if (hooks.isEmptyconcHook()) {
      try {
         File output = fileToGenerate();
         // make sure the directory exists
         output.getParentFile().mkdirs();
         Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
         writer.write(generate());
         writer.flush();
         writer.close();
      } catch(Exception e) {
        e.printStackTrace();
        return 1;
      }
    } else { /* We need to call gom to generate the file */
      String config_xml = System.getProperty("tom.home") + File.separator + "Tom.xml";
      try {
        File file = new File(config_xml);
        config_xml = file.getCanonicalPath();
      } catch (IOException e) {
        getLogger().log(Level.FINER,"Failed to get canonical path for "+config_xml);
      }
      String file_path = null;
      try {
        File output = fileToGenerate();
        file_path = output.getCanonicalPath();
      } catch (IOException e) {
        getLogger().log(Level.FINER,"Failed to get canonical path for "+fileName());
      }
      String[] params = {"-X",config_xml,"--optimize","--optimize2","--output",file_path,"-"};
      //String[] params = {"-X",config_xml,"--output",file_path,"-"};

      String gen = generate();

      InputStream backupIn = System.in;
      System.setIn(new DataInputStream(new StringBufferInputStream(gen)));
      int res = tom.engine.Tom.exec(params);
      System.setIn(backupIn);
      if (res != 0 ) {
        getLogger().log(Level.SEVERE, tom.gom.GomMessage.tomFailure.getMessage(),new Object[]{file_path});
        return res;
      }
    }
    return 0;
  }

  private int getLength(SlotFieldList list) {
    %match(SlotFieldList list) {
      concSlotField()     -> { return 0; }
      concSlotField(_,t*) -> { return getLength(`t*)+1; }
    }
    return -1;
  }
  private int getLength(HookList list) {
    %match(HookList list) {
      concHook()     -> { return 0; }
      concHook(_,t*) -> { return getLength(`t*)+1; }
    }
    return -1;
  }

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
