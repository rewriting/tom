/*
 * Copyright (c) 2000-2007, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this list of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce the above copyright
 *	notice, this list of conditions and the following disclaimer in the
 *	documentation and/or other materials provided with the distribution.
 *	- Neither the name of the INRIA nor the names of its
 *	contributors may be used to endorse or promote products derived from
 *	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package tom.library.bytecode;

import java.util.HashMap;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;

public class ToolBox {
  %include { ../mapping/java/sl.tom }
  %include { adt/bytecode/Bytecode.tom }

  private final static int[] accessFlags = {
    Opcodes.ACC_ABSTRACT,
    Opcodes.ACC_ANNOTATION,
    Opcodes.ACC_BRIDGE,
    Opcodes.ACC_DEPRECATED,
    Opcodes.ACC_ENUM,
    Opcodes.ACC_FINAL,
    Opcodes.ACC_INTERFACE,
    Opcodes.ACC_NATIVE,
    Opcodes.ACC_PRIVATE,
    Opcodes.ACC_PROTECTED,
    Opcodes.ACC_PUBLIC,
    Opcodes.ACC_STATIC,
    Opcodes.ACC_STRICT,
    Opcodes.ACC_SUPER,
    Opcodes.ACC_SYNCHRONIZED,
    Opcodes.ACC_SYNTHETIC,
    Opcodes.ACC_TRANSIENT,
    Opcodes.ACC_VARARGS,
    Opcodes.ACC_VOLATILE
  };
  private final static TAccess[] accessObj = {
    `ABSTRACT(),
    `ANNOTATION(),
    `BRIDGE(),
    `DEPRECATED(),
    `ENUM(),
    `FINAL(),
    `INTERFACE(),
    `NATIVE(),
    `PRIVATE(),
    `PROTECTED(),
    `PUBLIC(),
    `STATIC(),
    `STRICT(),
    `SUPER(),
    `SYNCHRONIZED(),
    `SYNTHETIC(),
    `TRANSIENT(),
    `VARARGS(),
    `VOLATILE()
  };

  public static TAccessList buildTAccess(int access) {
    TAccessList list = `AccessList();
    
    for(int i = 0; i < accessFlags.length; i++) {
      if((access & accessFlags[i]) != 0)
        list = `ConsAccessList(accessObj[i], list);
    }

    return list;
  }


  public static int buildAccessValue(TAccessList list){
    int value =0;
    HashMap map = new HashMap();
    for(int i =0;i<accessObj.length;i++){
      map.put(accessObj[i],new Integer(accessFlags[i]));
    }

    %match(TAccessList list){
      AccessList(_*,acc,_*)->{
        value = value | ((Integer)map.get(`acc)).intValue();
      }
    }
    return value;   
  }

  public static String buildSignature(TSignature signature){
    String sig = null;
    %match(TSignature signature){
      Signature(s) -> {sig=`s;}
    }
    return sig;
  }

  public static TValue buildTValue(Object v) {
    if(v instanceof String)
      return `StringValue((String)v);
    else if(v instanceof Integer)
      return `IntValue(((Integer)v).intValue());
    else if(v instanceof Long)
      return `LongValue(((Long)v).longValue());
    else if(v instanceof Float)
      return `FloatValue(((Float)v).floatValue());
    else if(v instanceof Double)
      return `DoubleValue(((Double)v).doubleValue());

    return null;
  }

  public static Object buildConstant(TValue value) {
      %match(TValue value){
        StringValue(v) -> { return `v;}
        IntValue(v) -> {return new Integer(`v);}
        LongValue(v) -> {return new Long(`v);}
        FloatValue(v) -> {return new Float(`v);}
        DoubleValue(v) -> {return new Double(`v);}
      }
      return null;
  }

  public static TStringList buildTStringList(String[] array) {
    TStringList list = `StringList();
    if(array != null) {
      for(int i = array.length - 1; i >= 0; i--)
        list = `ConsStringList(array[i], list);
    }

    return list;
  }

  public static TintList buildTintList(int[] array) {
    TintList list = `intList();
    if(array != null) {
      for(int i = array.length - 1; i >= 0; i--) {
        list = `ConsintList(array[i], list);
      }
    }

    return list;
  }

  public static TType buildTType(String type) {
    int t = Type.getType(type).getSort();
    TType ret = null;
    switch(t) {
      case Type.ARRAY:
        ret = `ARRAY();
        break;
      case Type.BOOLEAN:
        ret = `BOOLEAN();
        break;
      case Type.BYTE:
        ret = `BYTE();
        break;
      case Type.CHAR:
        ret = `CHAR();
        break;
      case Type.DOUBLE:
        ret = `DOUBLE();
        break;
      case Type.FLOAT:
        ret = `FLOAT();
        break;
      case Type.INT:
        ret = `INT();
        break;
      case Type.LONG:
        ret = `LONG();
        break;
      case Type.OBJECT:
        ret = `OBJECT();
        break;
      case Type.SHORT:
        ret = `SHORT();
        break;
      case Type.VOID:
        ret = `VOID();
        break;
    }

    return ret;
  }

  private static class Counter { public int count = 0; }
  public static TFieldDescriptor buildTFieldDescriptor(String desc) {
    Counter count = new Counter();
    TFieldDescriptor fDesc = buildTFieldDescriptorFrom(desc, count);
    if(count.count != desc.length())
      System.err.println("Malformed descriptor : " + desc);
    return fDesc;
  }

  private static TFieldDescriptor buildTFieldDescriptorFrom(String desc, Counter count) {
    TFieldDescriptor fDesc = null;
    switch(desc.charAt(count.count)) {
      case 'L':
        count.count++;
        int j = desc.indexOf(';', count.count);
        if(j == -1)
          System.err.println("Malformed descriptor : " + desc);
        String className = desc.substring(count.count, j);
        count.count += className.length() + 1;
        fDesc = `ObjectType(className);
        break;
      case '[':
        count.count++;
        fDesc = `ArrayType(buildTFieldDescriptorFrom(desc, count));
        break;
      case 'B':
        count.count++;
        fDesc = `B();
        break;
      case 'C':
        count.count++;
        fDesc = `C();
        break;
      case 'D':
        count.count++;
        fDesc = `D();
        break;
      case 'F':
        count.count++;
        fDesc = `F();
        break;
      case 'I':
        count.count++;
        fDesc = `I();
        break;
      case 'J':
        count.count++;
        fDesc = `J();
        break;
      case 'S':
        count.count++;
        fDesc = `S();
        break;
      case 'Z':
        count.count++;
        fDesc = `Z();
        break;
    }
    if(fDesc == null)
      System.err.println("Malformed descriptor : " + desc);
    return fDesc;
  }

  public static TReturnDescriptor buildTReturnDescriptor(String desc) {
    if(desc.charAt(0) == 'V' && desc.length() == 1)
      return `Void();
    return `ReturnDescriptor(buildTFieldDescriptor(desc));
  }

  public static TMethodDescriptor buildTMethodDescriptor(String desc) {
    int endParam = desc.indexOf(')', 1);
    if(desc.charAt(0) != '(' || endParam == -1)
      System.err.println("Malformed descriptor : " + desc);

    TFieldDescriptorList fList = `FieldDescriptorList();
    Counter count = new Counter();
    count.count++;
    while(count.count < endParam)
      fList = `FieldDescriptorList(fList*, buildTFieldDescriptorFrom(desc, count));
    if(count.count != endParam)
      System.err.println("Malformed descriptor : " + desc);
    TReturnDescriptor ret = buildTReturnDescriptor(desc.substring(count.count + 1));
    return `MethodDescriptor(fList, ret);
  }

  %typeterm StringBuffer { implement { StringBuffer } }
  %strategy BuildDescriptor(sb:StringBuffer) extends Identity() {
    visit TFieldDescriptor {
      ObjectType(className) -> { sb.append("L" + `className + ";"); }
      ArrayType[] -> { sb.append('['); }
      B() -> { sb.append('B'); }
      C() -> { sb.append('C'); }
      D() -> { sb.append('D'); }
      F() -> { sb.append('F'); }
      I() -> { sb.append('I'); }
      J() -> { sb.append('J'); }
      S() -> { sb.append('S'); }
      Z() -> { sb.append('Z'); }
    }

    visit TMethodDescriptor {
      _ -> { sb.append('('); }
    }

    visit TReturnDescriptor {
      _ -> { sb.append(')'); }
      Void() -> { sb.append('V'); }
    }
  }

  public static String buildDescriptor(TFieldDescriptor desc) {
    StringBuffer sb = new StringBuffer();
    try {
      `TopDown(BuildDescriptor(sb)).visit(desc);
    } catch(jjtraveler.VisitFailure e) { }
    return sb.toString();
  }
  public static String buildDescriptor(TMethodDescriptor desc) {
    StringBuffer sb = new StringBuffer();
    try {
      `TopDown(BuildDescriptor(sb)).visit(desc);
    } catch(jjtraveler.VisitFailure e) { }
    return sb.toString();
  }
}

