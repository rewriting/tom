
package bytecode;

import java.util.HashMap;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassAdapter;

import tom.library.strategy.mutraveler.MuStrategy;

import bytecode.classtree.*;
import bytecode.classtree.types.*;

import java.io.FileOutputStream;

public class Analysis {
  %include { Cfg.tom }

  // Checks if the current instruction is a load type instruction.
  %strategy IsLoad() extends Fail() {
    visit TInstructionList {
      c@ConsInstructionList(ins, _) -> {
        TInstruction inst = `ins;
        %match(TInstruction inst) {
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
    visit TInstructionList {
      c@ConsInstructionList(ins, _) -> {
        TInstruction inst = `ins;
        %match(TInstruction inst) {
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
    visit TInstructionList {
      c@ConsInstructionList(ins, _) -> {
        int index = ((Integer)map.get(var)).intValue();
        TInstruction inst = `ins;
        %match(TInstruction inst, int index) {
          (Istore|Lstore|Fstore|Dstore|Astore|Iload|Lload|Fload|Dload|Aload)(i), i -> {
            return `c;
          }
        }
      }
    }
  }

  // Prints the gom-subterm of the current `TInstructionList'.
  %strategy PrintInst() extends Identity() {
    visit TInstructionList {
      c@ConsInstructionList(ins, _) -> { System.out.println(`c); }
    }
  }

  // Removes the head instruction of an instruction list 
  %strategy RemoveHeadInst() extends Fail() {
    visit TInstructionList {
      ConsInstructionList(head,tail) -> {
        TInstruction head = `head;

        System.out.println("removes : " + head);
        return `tail;
      }
    }
  }


  // Analyzes the instruction list of each method of the given class.
  // Useless `store i' instruction will be printed.
  // (i.e. a `store i' which is not followed by a `load i' instruction)
  private static TClass analyze(TClass clazz) {
    TClass impClass = clazz.setmethods(`MethodList());

    TMethodList methods = clazz.getmethods();
    %match(TMethodList methods) {
      MethodList(_*, x, _*) -> {

        TInstructionList ins = `x.getcode().getinstructions();

        // Builds the labelMap to be able to retrieve the `TInstructionList' for each `Label'.
        // (This is needed for the flow simulation when a jump instruction is encoutered.)
        HashMap labelMap = new HashMap();
        `TopDown(BuildLabelMap(labelMap)).apply(ins);

        HashMap indexMap = new HashMap();
        MuStrategy noLoad =
          `AU(
              Not(Sequence(IsLoad(), HasIndex(indexMap, "index"))),
              Sequence(IsStore(indexMap, "useless"), HasIndex(indexMap, "index")),
              labelMap);

        MuStrategy storeNotUsed = `Sequence(IsStore(indexMap, "index"), AllCfg(noLoad, labelMap));

        //(`BottomUp(Try(ChoiceId(storeNotUsed,PrintInst())))).apply(ins);

        TInstructionList impInstList = ins;
        // Removes the useless store.
        if(`x.getinfo().getname().equals("stratKiller")) {
          impInstList = (TInstructionList)`Sequence(RemoveHeadInst(), RemoveHeadInst()).apply(ins);
          //impInstList = (TInstructionList)`RepeatId(BottomUp(Try(ChoiceId(storeNotUsed, RemoveHeadInst())))).apply(ins);
        }

        TMethodCode impCode = `x.getcode().setinstructions(impInstList);
        TMethod impMethod = `x.setcode(impCode);
        impClass = appendMethod(impClass, impMethod);
        
        //(`BottomUp(Try(ChoiceId(storeNotUsed,PrintInst())))).apply(impInstList);
        //System.out.println(impClass);

        // Display the control flow graph.
        //`mu(MuVar("x"),Sequence(PrintInst(),AllCfg(MuVar("x"),labelMap))).apply(ins);
      }
    }
    return impClass;
  }

  private static TClass appendMethod(TClass clazz, TMethod method){
    TMethodList l = clazz.getmethods();
    return `Class(clazz.getinfo(), clazz.getfields(), MethodList(l*, method));
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

  private static TClass renameClass(TClass clazz, String newName) {
    TClassInfo classInfo = clazz.getinfo();
    String currentName = classInfo.getname();

    TClass newClass = clazz.setinfo(classInfo.setname(newName));
    return (TClass)`TopDown(RenameDescAndOwner(currentName, newName)).apply(newClass);
  }

  public static void main(String[] args) {
    if(args.length <= 0) {
      System.out.println("Usage : java bytecode.Analysis <class name>\nEx: java bytecode.Analysis bytecode.Subject");
      return;
    }

    try {
      System.out.println("Parsing class file " + args[0] + " ...");
      ClassReader cr = new ClassReader(args[0]);
      TClassGenerator cg = new TClassGenerator();
      ClassAdapter ca = new ClassAdapter(cg);
      cr.accept(ca, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

      System.out.println("Analyzing ...");
      TClass c = cg.getTClass();
      TClass cImproved = analyze(c);

      String impClassName = args[0] + "Imp";
      System.out.println("Generating improved class " + impClassName + " ...");
      cImproved = renameClass(cImproved, impClassName);

      BytecodeGenerator bg = new BytecodeGenerator();
      byte[] code = bg.toBytecode(cImproved);
      FileOutputStream fos = new FileOutputStream(impClassName + ".class");
      fos.write(code);
      fos.close();

    } catch(IOException ioe) {
      System.err.println("Class not found : " + args[0]);
    }
  }
}


