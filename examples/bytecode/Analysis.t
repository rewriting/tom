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

import java.util.HashMap;
import tom.library.sl.*;

import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;
import tom.library.bytecode.*;
import java.io.FileOutputStream;

public class Analysis {

  %include { float.tom }
  %include { adt/bytecode/Bytecode.tom }
  %include { bytecode/cfg.tom }

  // Checks if the current instruction is a load type instruction.
  %strategy IsLoad() extends Fail() {
    visit InstructionList {
      c@ConsInstructionList(ins, _) -> {
        Instruction inst = `ins;
        %match(inst) {
          (Iload|Lload|Fload|Dload|Aload)(_) -> {
            return `c;
          }
       }
      }
    }
  }

  // Checks if the current instruction is a store type instruction.
  // If it is true, then the index of the variable to be store is
  // putted into the `Map' map with the key `var'.
  %strategy IsStore(map:Map, var:String) extends Fail() {
    visit InstructionList {
      c@ConsInstructionList(ins, _) -> {
        Instruction inst = `ins;
        %match(inst) {
          (Istore|Lstore|Fstore|Dstore|Astore)(i) -> {
            map.put(var, new Integer(`i));
            return `c;
          }
        }
      }
    }
  }

  // Checks if the current load/store instruction has the wanted index.
  // The index is retrieved from the map at key `var'.
  %strategy HasIndex(map:Map, var:String) extends Fail() {
    visit InstructionList {
      c@ConsInstructionList(ins, _) -> {
        int index = ((Integer)map.get(var)).intValue();
        Instruction inst = `ins;
        %match(Instruction inst, int index) {
          (Istore|Lstore|Fstore|Dstore|Astore|Iload|Lload|Fload|Dload|Aload)(i), i -> {
            return `c;
          }
        }
      }
    }
  }

  // Prints the gom-subterm of the current `InstructionList'.
  %strategy PrintInst() extends Identity() {
    visit InstructionList {
      InstructionList(i, _*) -> { System.out.println(`i); }
    }
  }

  // Removes the head instruction of an instruction list 
  %strategy RemoveHeadInst() extends Fail() {
    visit InstructionList {
      ConsInstructionList(head,tail) -> {
        Instruction head = `head;

        System.out.println("removes : " + head);
        return `tail;
      }
    }
  }


  // Analyzes the instruction list of each method of the given class.
  // Useless `store i' instruction will be printed.
  // (i.e. a `store i' which is not followed by a `load i' instruction)
  private static ClassNode analyze(ClassNode ast) {
    ClassNode impClass = ast.setmethods(`MethodList());

    MethodList methods = ast.getmethods();
    %match(methods) {
      MethodList(_*, x, _*) -> {
        System.out.println("Analysis of method "+`x.getinfo().getname());
        InstructionList ins = `x.getcode().getinstructions();

        // Builds the labelMap to be able to retrieve the `InstructionList' for each `Label'.
        // (This is needed for the flow simulation when a jump instruction is encoutered.)
        HashMap labelMap = new HashMap();
        try {
          `TopDown(BuildLabelMap(labelMap)).visit(ins);
        } catch(VisitFailure e) {
          throw new tom.engine.exception.TomRuntimeException();
        }

        HashMap indexMap = new HashMap();
        Strategy noLoad =
          `AUMap(
              Not(Sequence(IsLoad(), HasIndex(indexMap, "index"))),
              Sequence(IsStore(indexMap, "useless"), HasIndex(indexMap, "index")),
              labelMap);

        Strategy storeNotUsed = `Sequence(IsStore(indexMap, "index"), AllCfg(noLoad, labelMap));
        try {
          `BottomUp(Try(ChoiceId(storeNotUsed,PrintInst()))).visit(ins);
        } catch(VisitFailure e) {
          throw new tom.engine.exception.TomRuntimeException();
        }

        // Removes the useless stores of the method stratKiller
        // We have not managed to do it in the general case because of
        // stack size problems
        InstructionList impInstList = ins;
        /*
           if(`x.getinfo().getname().equals("stratKiller")) {
           impInstList = (InstructionList)`RepeatId(BottomUp(Try(ChoiceId(storeNotUsed, RemoveHeadInst())))).visit(ins);
           }
         */
        MethodCode impCode = `x.getcode().setinstructions(impInstList);
        Method impMethod = `x.setcode(impCode);
        impClass = appendMethod(impClass, impMethod);

      }
    }
    return impClass;
  }

  private static ClassNode appendMethod(ClassNode ast, Method method){
    MethodList l = ast.getmethods();
    return `Class(ast.getinfo(), ast.getfields(), MethodList(l*, method));
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

  private static ClassNode renameClass(ClassNode ast, String newName) {
    ClassInfo classInfo = ast.getinfo();
    String currentName = classInfo.getname();

    ClassNode newAst = ast.setinfo(classInfo.setname(newName));
    try {
      return `TopDown(RenameDescAndOwner(currentName, newName)).visit(newAst);
    } catch(VisitFailure e) {
      throw new tom.engine.exception.TomRuntimeException();
    }
  }

  public static void main(String[] args) {
    if(args.length <= 0) {
      System.out.println("Usage : java bytecode.Analysis <class name>\nEx: java bytecode.Analysis bytecode.Subject");
      return;
    }
    System.out.println("Parsing class file " + args[0] + " ...");
    BytecodeReader cr = new BytecodeReader(args[0]);
    System.out.println("Analyzing ...");
    ClassNode c = cr.getAst();
    ClassNode cImproved = analyze(c);

    String impClassName = args[0] + "Imp";
    System.out.println("Generating improved class " + impClassName + " ...");
    cImproved = renameClass(cImproved, impClassName);

    BytecodeGenerator bg = new BytecodeGenerator(cImproved);
    byte[] code = bg.toByteArray();
    try{
      FileOutputStream fos = new FileOutputStream(impClassName + ".class");
      fos.write(code);
      fos.close();
    }catch(java.io.IOException e){
      System.out.println("IO Exception");
    }
  }
}


