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
  ClassName factoryName;
  ClassName abstractType;
  ClassName sortName;
  ClassName visitor;
  SlotFieldList slotList;
  HookList hooks;
  TemplateClass mapping;

  %include { ../../adt/objects/Objects.tom}

  public OperatorTemplate(ClassName className,
                          ClassName factoryName,
                          ClassName abstractType,
                          ClassName sortName,
                          ClassName visitor,
                          SlotFieldList slots,
                          HookList hooks,
                          TemplateClass mapping) {
    super(className);
    this.factoryName = factoryName;
    this.abstractType = abstractType;
    this.sortName = sortName;
    this.visitor = visitor;
    this.slotList = slots;
    this.hooks = hooks;
    this.mapping = mapping;
  }

  public String generate() {

    String classBody = %[
package @getPackage()@;

public class @className()@ extends @fullClassName(sortName)@ {
  private static @className()@ proto = new @className()@();
  private int hashCode;
  private @className()@() {};

@generateMembers()@

@generateBody()@

}]%;

    return classBody;
  }

  private String generateBody() {
    StringBuffer out = new StringBuffer();

    out.append(%[
    /* static constructor */
@generateConstructor()@

  private void init(@childListWithType(slotList) + (slotList.isEmpty()?"":", ") @int hashCode) {
@generateMembersInit()@
    this.hashCode = hashCode;
  }

  private void initHashCode(@childListWithType(slotList)@) {
@generateMembersInit()@
  this.hashCode = this.hashFunction();
  }

  /* name and arity */
  public String getName() {
    return "@className()@";
  }

  private int getArity() {
    return @slotList.getLength()@;
  }

  /* shared.SharedObject */
  public int hashCode() {
    return this.hashCode;
  }

  public shared.SharedObject duplicate() {
    @className()@ clone = new @className()@();
    clone.init(@childList(slotList) + (slotList.isEmpty()?"":", ") @hashCode);
    return clone;
  }

  public boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof @className()@) {
      @className()@ peer = (@className()@) obj;
      return @generateMembersEqualityTest("peer")@;
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
      aterm.pure.SingletonFactory.getInstance().makeAFun(getName(),getArity(),false),
      new aterm.ATerm[] {@generateToATermChilds()@});
  }

  public static @fullClassName(sortName)@ fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(proto.getName().equals(appl.getName())) {
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

]%);

    out.append(%[
      /* internal use */
  protected int hashFunction() {
    int a, b, c;

    /* Set up the internal state */
    a = b = 0x9e3779b9; /* the golden ratio; an arbitrary value */
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
    while(!slots.isEmpty()) {
      SlotField head = slots.getHead();
      slots = slots.getTail();
      res+= %[
  public @slotDomain(head)@ @getMethod(head)@() {
    return @fieldName(head.getName())@;
  }]%;
    }
    return res;
  }

  private String generateToATermChilds() {
    String res = "";
    SlotFieldList slots = slotList;
    while(!slots.isEmpty()) {
      SlotField head = slots.getHead();
      slots = slots.getTail();
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
          res+= getMethod(slot)+"().toATerm()";
        } else {
          if (`domain.equals(`ClassName("","int"))) {
            res+= "(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeInt("+getMethod(slot)+"())";
          } else if (`domain.equals(`ClassName("","String"))) {
            res+= "(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeAppl(";
            res += "aterm.pure.SingletonFactory.getInstance().makeAFun(";
            res += getMethod(slot)+"() ,0 , true))";
          } else {
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
    while(!slots.isEmpty()) {
      SlotField head = slots.getHead();
      slots = slots.getTail();
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
          } else if (`domain.equals(`ClassName("","String"))) {
            res+= "(String)"+appl+".getArgument("+index+").toString()";
          } else {
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
    while(!slots.isEmpty()) {
      SlotField head = slots.getHead();
      slots = slots.getTail();
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
    while(!slots.isEmpty()) {
      SlotField head = slots.getHead();
      slots = slots.getTail();
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
    while(!slots.isEmpty()) {
      SlotField head = slots.getHead();
      slots = slots.getTail();
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
    while(!slots.isEmpty()) {
      SlotField head = slots.getHead();
      slots = slots.getTail();
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
    %match(SlotFieldList slotList) {
      concSlotField(_*,slot@SlotField[name=fieldName],_*) -> {
        res += fieldName(`fieldName)+"=="+peer+"."+getMethod(`slot)+"()";
        res+= " && ";
      }
    }
    res += "true"; // to handle the "no childs" case
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
      concSlotField(_*,slot@SlotField[domain=domain],_*) -> {
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += "      case "+index+": return "+getMethod(`slot)+"();\n";
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
      concSlotField(_*,slot@SlotField[domain=domain],_*) -> {
        if (GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += getMethod(`slot)+"(), ";
        } else {
          if (index != argIndex) {
            res += getMethod(`slot)+"(), ";
          } else {
            res += "("+fullClassName(`domain)+") " + argName+", ";
          }
          index++;
        }
      }
    }
    return res.substring(0,res.length()-2);
  }

  private String generateHashArgs() {
    String res = "";
    int index = slotList.getLength() - 1;
    %match(SlotFieldList slotList) {
      concSlotField(_*,SlotField[name=slotName,domain=domain],_*) -> {
        int shift = (index % 4) * 8;
        String accum = ""+"aaaabbbbcccc".toCharArray()[index % 12];
        res += "    "+accum+" += (";
        if (!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res += fieldName(`slotName)+".hashCode()";
        } else {
          if (`domain.equals(`ClassName("","int"))) {
            res+= fieldName(`slotName);
          } else if (`domain.equals(`ClassName("","String"))) {
            // Use the string hashFunction for Strings, and pass index as arity
            res+= "shared.HashFunctions.stringHashFunction("+fieldName(`slotName)+", "+index+")";
          } else {
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
    if (hooks.isEmpty()) {
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
      if(hooks.getLength() > 1) {
        throw new GomRuntimeException("Support for multiple hooks for an operator not implemented yet");
      }
      // then a make function calling it
      %match(HookList hooks) {
        concHook(MakeHook(args,code)) -> {
          // replace the inner make call
          out.append(%[
  public static @fullClassName(sortName)@ make(@unprotectedChildListWithType(`args)@) {
    @`code@
  }

]%);
        }
        concHook(MakeBeforeHook(args,code)) -> {
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
    if (hooks.isEmpty()) {
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
      String config_xml = System.getProperty("tom.home") + "/Tom.xml";
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
      String[] params = {"-X",config_xml,"--static","--output",file_path,"-"};

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

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
