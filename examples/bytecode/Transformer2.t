/*
 * Copyright (c) 2004-2007, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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
package bytecode;

import tom.library.sl.*;
import java.io.FileOutputStream;
import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;
import tom.library.adt.bytecode.types.tstringlist.*;
import tom.library.adt.bytecode.types.tlabellist.*;
import tom.library.adt.bytecode.types.tintlist.*;
import tom.library.bytecode.*;
import java.util.Vector;
import java.util.HashMap;

public class Transformer2 {

  %include { bytecode/cfg.tom }
  %include { adt/bytecode/Bytecode.tom }

  public static Vector  v = new Vector();

  %typeterm IntWrapper {
    implement           { IntWrapper }
    is_sort(t)     { t instanceof IntWrapper }
    equals(t1,t2)       { (t1.equals(t2)) }
  } 

  public static class IntWrapper{
    private int index_reader;
    private int index_file;

    public void set(int reader,int file){
      index_reader = reader;
      index_file = file;
    }

    public int getReaderIndex(){
      return index_reader;
    }

    public int getFileIndex(){
      return index_file;
    }

  }

  public static String clm;

  %strategy NewFileReader(index:IntWrapper) extends Identity() {
    visit TInstructionList {
      c@(New("java/io/FileReader"),Dup(),Aload(file),
          Invokespecial[owner="java/io/FileReader", name="<init>"],
          Astore(reader),_*) -> {
        index.set(`reader,`file);
        getEnvironment().setStatus(Environment.FAILURE);
        return `c;
      }
    }
  }

  %strategy CallToSRead(index:IntWrapper) extends Identity() {
    visit TInstructionList {
      c@(Aload(i),
          inv@Invokevirtual[owner="java/io/FileReader",name="read"],
          Pop(), tail*)-> {
        if(index.getReaderIndex()==`i){ 
          System.out.println("Attempt to read a file");
          int index_file = index.getFileIndex();
          return `InstructionList(
              New("bytecode/SecureAccess"),
              Dup(),
              Invokespecial("bytecode/SecureAccess","<init>",MethodDescriptor(EmptyFieldDescriptorList(),Void())),
              Aload(index_file),
              Invokevirtual("bytecode/SecureAccess","sread",MethodDescriptor(ConsFieldDescriptorList(ObjectType("java/lang/String"),EmptyFieldDescriptorList()),ReturnDescriptor(I()))),
              Pop(),
              tail*);
        }
      }
    }
  }

  %strategy UpdateLabelMap(m:Map) extends Identity() {
    visit TInstructionList {
      _ -> {
        Position current = getEnvironment().getPosition();
        getEnvironment().goTo(current.inv());
        execute(`TopDown(BuildLabelMap(m)));
        getEnvironment().goTo(current);
      }
    }
  }

  %strategy Print() extends Identity() {
    visit TInstructionList {
      (i,_*) -> {
        Position current = getEnvironment().getPosition();
        System.out.println("at pos "+current);
        System.out.println(`i);
      }
    }
  }

  private static TClass fileAccesVerify(TClass givenClass) {
    TMethodList methods = givenClass.getmethods();
    TMethodList secureMethods = `MethodList();
    %match(TMethodList methods) {
      MethodList(_*, x, _*) -> {
        System.out.println("Analysis of the method "+`x.getinfo().getname());
        TInstructionList ins = `x.getcode().getinstructions();
        // Builds the labelMap to be able to retrieve the `TInstructionList' for each `Label'.
        // (This is needed for the flow simulation when a jump instruction is encoutered.)
        HashMap labelMap = new HashMap();
        `TopDown(BuildLabelMap(labelMap)).fire(ins);
        HashMap indexMap = new HashMap();
        IntWrapper index = new IntWrapper();
        Strategy securiseAccess =
          `Sequence(NewFileReader(index),AGMap(Sequence(CallToSRead(index),UpdateLabelMap(labelMap)),labelMap));
        /**
        `AGMap(Print(),labelMap).fire(ins);
        */
        TInstructionList secureInstList = (TInstructionList) `TopDown(Try(securiseAccess)).fire(ins);
        TMethodCode secureCode = `x.getcode().setinstructions(secureInstList);
        TMethod secureMethod = `x.setcode(secureCode);
        secureMethods = `MethodList(secureMethods*,secureMethod);	
      } 
    }
    return givenClass.setmethods(secureMethods);
  }



  private static TClass renameClass(TClass clazz, String newName) {
    TClassInfo classInfo = clazz.getinfo();
    String currentName = classInfo.getname();
    TClass newClass = clazz.setinfo(classInfo.setname(newName));
    return (TClass)`TopDown(RenameDescAndOwner(currentName, newName)).fire(newClass);
  }

  %strategy RenameDescAndOwner(currentName:String, newName:String) extends Identity() {
    visit TOuterClassInfo {
      x@OuterClassInfo[owner=owner] -> {
        if(`owner.equals(currentName))
          return `x.setowner(newName);
      }
    }

    visit TLocalVariable {
      x@LocalVariable[typeDesc=t] -> {
        if(`t.equals(currentName))
          return `x.settypeDesc(newName);
      }
    }

    visit TFieldDescriptor {
      x@ObjectType[className=n] -> {
        if(`n.equals(currentName))
          return `x.setclassName(newName);
      }
    }

    visit TMethodInfo {
      x@MethodInfo[owner=owner] -> {
        if(`owner.equals(currentName))
          return `x.setowner(newName);
      }
    }

    visit TInstruction {
      x@(Getstatic|Putstatic|Getfield|Putfield|
          Invokevirtual|Invokespecial|Invokestatic|Invokeinterface)
        [owner=owner] -> {
          if(`owner.equals(currentName))
            return `x.setowner(newName);
        }
      x@(New|Anewarray|Checkcast|Instanceof|Multianewarray)[typeDesc=t] -> {
        if(`t.equals(currentName))
          return `x.settypeDesc(newName);
      }
    }

  }


  public static byte[] transform(String file){
    v.add(file);
    System.out.println("Parsing class file " + file + " ...");
    BytecodeReader br = new BytecodeReader(file);
    System.out.println("Analyzing ...");
    TClass c= br.getTClass();
    //String secureName = c.getinfo().getname() + "Secure";
    //TClass cSecured = renameClass(fileAccesVerify(c),secureName);
    c = fileAccesVerify(c);
    BytecodeGenerator bg = new BytecodeGenerator();
    return bg.toBytecode(c);
  }

}

