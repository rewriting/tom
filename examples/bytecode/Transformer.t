/*
 * Copyright (c) 2004-2011, INPL, INRIA
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

import java.io.FileOutputStream;
import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;
import tom.library.bytecode.*;
import tom.library.sl.*;
import java.util.Vector;

public class Transformer {

  %include { sl.tom }
  %include { adt/bytecode/Bytecode.tom }

  public static String clm;

  %strategy FindFileAccess() extends Identity() {
    visit InstructionList {
    
      //Case 1: new FileReader(nameFile).read();
      InstructionList(before*,New("java/io/FileReader"),Dup(),Aload(nombre),
       Invokespecial[owner="java/io/FileReader", name="<init>"],
       Astore(nombre2),middle*,Aload(nombre2),Aload(nombre1),
       Invokevirtual("java/io/FileReader","read",MethodDescriptor(ConsFieldDescriptorList(ArrayType(C()),EmptyFieldDescriptorList()),ReturnDescriptor(I()))),
       Pop(), after*)-> {
        System.out.println("Access to a file(reader(buf))" );
        return `InstructionList(before*,
            middle*,
            New("bytecode/SecureAccess"),
            Dup(),
            Invokespecial("bytecode/SecureAccess","<init>",MethodDescriptor(EmptyFieldDescriptorList(),Void())),
            Aload(nombre),
            Aload(nombre1),
            Invokevirtual("bytecode/SecureAccess","sreadBuf",MethodDescriptor(ConsFieldDescriptorList(ObjectType("java/lang/String"),ConsFieldDescriptorList(ArrayType(C()),EmptyFieldDescriptorList())),Void())),
            after*); 
      }

      //Case 2: FileReader r = new FileReader(nameFile); ... ; r.read()
      InstructionList(before*,New("java/io/FileReader"),Dup(),Aload(nombre),
       Invokespecial[owner="java/io/FileReader", name="<init>"],
       middle*,
       Invokevirtual[owner ="java/io/FileReader",name="read"], 
       Pop(),
       after*) -> {
        System.out.println("Attempt to read a file");
        return `InstructionList(before*,
            New("bytecode/SecureAccess"),
            Dup(),
            Invokespecial("bytecode/SecureAccess","<init>",MethodDescriptor(EmptyFieldDescriptorList(),Void())),
            middle*,
            Aload(nombre),
            Invokevirtual("bytecode/SecureAccess","sread",MethodDescriptor(ConsFieldDescriptorList(ObjectType("java/lang/String"),EmptyFieldDescriptorList()),ReturnDescriptor(I()))),
            Pop(),
            Return(),
            after*);
      }

      //Case 3
      InstructionList(before*,Aload(nombre), Invokevirtual[owner ="java/io/FileReader",name="read"], Pop(),after*) -> {
        return `InstructionList(before*,
            New("bytecode/SecureAccess"),
            Dup(),
            Invokespecial("bytecode/SecureAccess","<init>",MethodDescriptor(EmptyFieldDescriptorList(),Void())),
            Aload(nombre),
            Invokevirtual("bytecode/SecureAccess","sread",MethodDescriptor(ConsFieldDescriptorList(ObjectType("java/lang/String"),EmptyFieldDescriptorList()),ReturnDescriptor(I()))),
            Pop(),
            Return(),
            after*);
      }

    }//visit
  }//strategy


  private static ClassNode fileAccesVerify(ClassNode givenClass) {
    MethodList methods = givenClass.getmethods();
    MethodList secureMethods = `MethodList();
    %match(methods) {
      MethodList(_*, x, _*) -> {
        System.out.println("Analysis of the method "+`x.getinfo().getname());
        InstructionList ins = `x.getcode().getinstructions();
        try {
          InstructionList secureInstList = `TopDown(FindFileAccess()).visit(ins);
          MethodCode secureCode = `x.getcode().setinstructions(secureInstList);
          Method secureMethod = `x.setcode(secureCode);
          secureMethods = `MethodList(secureMethods*,secureMethod);	
        } catch(VisitFailure e) {
          throw new RuntimeException("Failure during file access verification");
        }
      } 
    }
    return givenClass.setmethods(secureMethods);
  }



  private static ClassNode renameClass(ClassNode ast, String newName) {
    ClassInfo classInfo = ast.getinfo();
    String currentName = classInfo.getname();
    ClassNode newClass = ast.setinfo(classInfo.setname(newName));
    try {
      return (ClassNode)`TopDown(RenameDescAndOwner(currentName, newName)).visit(newClass);
    } catch(VisitFailure e) {
      throw new RuntimeException("Failure during class renaming");
    }
  }

  %strategy RenameDescAndOwner(currentName:String, newName:String) extends Identity() {
    visit OuterClassInfo {
      x@OuterClassInfo[owner=owner] -> {
        if(`owner.equals(currentName))
          return `x.setowner(newName);
      }
    }

    visit LocalVariable {
      x@LocalVariable[typeDesc=t] -> {
        if(`t.equals(currentName))
          return `x.settypeDesc(newName);
      }
    }

    visit FieldDescriptor {
      x@ObjectType[className=n] -> {
        if(`n.equals(currentName))
          return `x.setclassName(newName);
      }
    }

    visit MethodInfo {
      x@MethodInfo[owner=owner] -> {
        if(`owner.equals(currentName))
          return `x.setowner(newName);
      }
    }

    visit Instruction {
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
    System.out.println("Parsing class file " + file + " ...");
    BytecodeReader br = new BytecodeReader(file);
    System.out.println("Analyzing ...");
    ClassNode c= br.getAst();
    //String secureName = c.getinfo().getname() + "Secure";
    //ClassNode cSecured = renameClass(fileAccesVerify(c),secureName);
    c = fileAccesVerify(c);
    BytecodeGenerator bg = new BytecodeGenerator(c);
    return bg.toByteArray();
  }

}

