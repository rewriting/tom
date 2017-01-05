/*
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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
 *	- Neither the name of the Inria nor the names of its
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
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;

public class ToolBox {

  %include { ../mapping/java/sl.tom }
  %include { ../adt/bytecode/Bytecode.tom }

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

  private final static Access[] accessObj = {
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

  public static AccessList buildAccess(int access) {
    AccessList list = `AccessList();

    for(int i = 0; i < accessFlags.length; i++) {
      if((access & accessFlags[i]) != 0) {
       Access tmp = accessObj[i];
        list = `ConsAccessList(tmp, list);
      }
    }

    return list;
  }


  public static int buildAccessValue(AccessList list){
    int value =0;
    HashMap<Access,Integer> map = new HashMap<Access,Integer>();
    for(int i =0;i<accessObj.length;i++){
      map.put(accessObj[i],accessFlags[i]);
    }

    %match(list){
      AccessList(_*,acc,_*)->{
        value = value | (map.get(`acc)).intValue();
      }
    }
    return value;   
  }

  public static String buildSignature(Signature signature){
    String sig = null;
    %match(signature){
      Signature(s) -> {sig=`s;}
    }
    return sig;
  }

  public static Value buildValue(Object v) {
    if(v instanceof String) {
      return `StringValue((String)v);
    }
    else if(v instanceof Integer) {
      return `IntValue(((Integer)v).intValue());
    }
    else if(v instanceof Long) {
      return `LongValue(((Long)v).longValue());
    }
    else if(v instanceof Float) {
      return `FloatValue(((Float)v).floatValue());
    }
    else if(v instanceof Double) {
      return `DoubleValue(((Double)v).doubleValue());
    }
    return null;
  }

  public static Object buildConstant(Value value) {
    %match(value){
      StringValue(v) -> { return `v;}
      IntValue(v) -> {return Integer.valueOf(`v);}
      LongValue(v) -> {return Long.valueOf(`v);}
      FloatValue(v) -> {return new Float(`v);}
      DoubleValue(v) -> {return new Double(`v);}
    }
    return null;
  }

  public static StringList buildStringList(String[] array) {
    StringList list = `StringList();
    if(array != null) {
      for(int i = array.length - 1; i >= 0; i--) {
        String tmp=array[i];
        list = `ConsStringList(tmp, list);
      }
    }

    return list;
  }

  public static IntList buildIntList(int[] array) {
    IntList list = `IntList();
    if(array != null) {
      for(int i = array.length - 1; i >= 0; i--) {
        int tmp=array[i];
        list = `ConsIntList(tmp, list);
      }
    }

    return list;
  }

  public static TypeNode buildType(String type) {
    int t = Type.getType(type).getSort();
    switch(t) {
      case Type.ARRAY:
        return `ARRAY();
      case Type.BOOLEAN:
        return `BOOLEAN();
      case Type.BYTE:
        return `BYTE();
      case Type.CHAR:
        return `CHAR();
      case Type.DOUBLE:
        return `DOUBLE();
      case Type.FLOAT:
        return `FLOAT();
      case Type.INT:
        return `INT();
      case Type.LONG:
        return `LONG();
      case Type.OBJECT:
        return `OBJECT();
      case Type.SHORT:
        return `SHORT();
      case Type.VOID:
        return `VOID();
      default:
        throw new RuntimeException("Unsupported Type :"+t);
    }
  }

  private static class Counter { public int count = 0; }
  public static FieldDescriptor buildFieldDescriptor(String desc) {
    Counter count = new Counter();
    FieldDescriptor fDesc = buildFieldDescriptorFrom(desc, count);
    if(count.count != desc.length()) {
      System.err.println("Malformed descriptor : " + desc);
    }
    return fDesc;
  }

  private static FieldDescriptor buildFieldDescriptorFrom(String desc, Counter count) {
    FieldDescriptor fDesc = null;
    switch(desc.charAt(count.count)) {
      case 'L':
        count.count++;
        int j = desc.indexOf(';', count.count);
        if(j == -1) {
          System.err.println("Malformed descriptor : " + desc);
        }
        String className = desc.substring(count.count, j);
        count.count += className.length() + 1;
        fDesc = `ObjectType(className);
        break;
      case '[':
        count.count++;
        fDesc = `ArrayType(buildFieldDescriptorFrom(desc, count));
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
    if(fDesc == null) {
      System.err.println("Malformed descriptor : " + desc);
    }
    return fDesc;
  }

  public static ReturnDescriptor buildReturnDescriptor(String desc) {
    if(desc.charAt(0) == 'V' && desc.length() == 1) {
      return `Void();
    }
    return `ReturnDescriptor(buildFieldDescriptor(desc));
  }

  public static MethodDescriptor buildMethodDescriptor(String desc) {
    int endParam = desc.indexOf(')', 1);
    if(desc.charAt(0) != '(' || endParam == -1) {
      System.err.println("Malformed descriptor : " + desc);
    }
    FieldDescriptorList fList = `FieldDescriptorList();
    Counter count = new Counter();
    count.count++;
    while(count.count < endParam)
      fList = `FieldDescriptorList(fList*, buildFieldDescriptorFrom(desc, count));
    if(count.count != endParam) {
      System.err.println("Malformed descriptor : " + desc);
    }
    ReturnDescriptor ret = buildReturnDescriptor(desc.substring(count.count + 1));
    return `MethodDescriptor(fList, ret);
  }

  %typeterm StringBuilder { 
    implement { StringBuilder }
    is_sort(t) { ($t instanceof StringBuilder) }
  }

  %strategy BuildDescriptor(sb:StringBuilder) extends Identity() {
    visit FieldDescriptor {
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

    visit MethodDescriptor {
      _ -> { sb.append('('); }
    }

    visit ReturnDescriptor {
      _ -> { sb.append(')'); }
      Void() -> { sb.append('V'); }
    }
  }

  public static String buildDescriptor(FieldDescriptor desc) {
    StringBuilder sb = new StringBuilder();
    try {
      `TopDown(BuildDescriptor(sb)).visitLight(desc);
    } catch(tom.library.sl.VisitFailure e) { }
    return sb.toString();
  }

  public static String buildDescriptor(MethodDescriptor desc) {
    StringBuilder sb = new StringBuilder();
    try {
      `TopDown(BuildDescriptor(sb)).visitLight(desc);
    } catch(tom.library.sl.VisitFailure e) { }
    return sb.toString();
  }

 }

