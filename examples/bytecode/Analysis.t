
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
          (Iload_0|Lload_0|Fload_0|Dload_0|Aload_0)() -> {
            return `c;
          }
          (Iload_1|Lload_1|Fload_1|Dload_1|Aload_1)() -> {
            return `c;
          }
          (Iload_2|Lload_2|Fload_2|Dload_2|Aload_2)() -> {
            return `c;
          }
          (Iload_3|Lload_3|Fload_3|Dload_3|Aload_3)() -> {
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
          (Istore_0|Lstore_0|Fstore_0|Dstore_0|Astore_0)() -> {
            map.put(var, new Integer(0));
            return `c;
          }
          (Istore_1|Lstore_1|Fstore_1|Dstore_1|Astore_1)() -> {
            map.put(var, new Integer(1));
            return `c;
          }
          (Istore_2|Lstore_2|Fstore_2|Dstore_2|Astore_2)() -> {
            map.put(var, new Integer(2));
            return `c;
          }
          (Istore_3|Lstore_3|Fstore_3|Dstore_3|Astore_3)() -> {
            map.put(var, new Integer(3));
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
        %match(TInstruction inst) {
          (Istore|Lstore|Fstore|Dstore|Astore|Iload|Lload|Fload|Dload|Aload)(index) -> {
            return `c;
          }
          (Istore_0|Lstore_0|Fstore_0|Dstore_0|Astore_0|Iload_0|Lload_0|Fload_0|Dload_0|Aload_0)() -> {
            if(index == 0)
              return `c;
          }
          (Istore_1|Lstore_1|Fstore_1|Dstore_1|Astore_1|Iload_1|Lload_1|Fload_1|Dload_1|Aload_1)() -> {
            if(index == 1)
              return `c;
          }
          (Istore_2|Lstore_2|Fstore_2|Dstore_2|Astore_2|Iload_2|Lload_2|Fload_2|Dload_2|Aload_2)() -> {
            if(index == 2)
              return `c;
          }
          (Istore_3|Lstore_3|Fstore_3|Dstore_3|Astore_3|Iload_3|Lload_3|Fload_3|Dload_3|Aload_3)() -> {
            if(index == 3)
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
        System.out.println("remove "+`head);
        return `tail;}
    }
  }


  // Analyzes the instruction list of each method of the given class.
  // Useless `store i' instruction will be printed.
  // (i.e. a `store i' which is not followed by a `load i' instruction)
  private static TClass analyze(TClass clazz) {
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

        (`BottomUp(Try(ChoiceId(storeNotUsed,PrintInst())))).apply(ins);
        // Remove the useless store.
        `x.getcode().setinstructions((TInstructionList)`RepeatId(BottomUp(Try(ChoiceId(storeNotUsed, RemoveHeadInst())))).apply(ins));
        (`BottomUp(Try(ChoiceId(storeNotUsed,PrintInst())))).apply(ins);

        // Display the imprimer le cfg d'une liste d'instructions :
        //`mu(MuVar("x"),Sequence(PrintInst(),AllCfg(MuVar("x"),labelMap))).apply(ins);
      }
    }
    return clazz;
  }

  public static void main(String[] args) {
    if(args.length <= 0) {
      System.out.println("Usage : java bytecode.Analysis <class name>\nEx: java bytecode.Analysis bytecode.Subject");
      return;
    }

    try {
      ClassReader cr = new ClassReader(args[0]);
      TClassGenerator cg = new TClassGenerator();
      ClassAdapter ca = new ClassAdapter(cg);
      cr.accept(ca, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

      TClass c = cg.getTClass();
      TClass cImproved = analyze(c);
      BytecodeGenerator bg = new BytecodeGenerator();
      byte[] code = bg.toBytecode(cImproved);
      FileOutputStream fos = new FileOutputStream("Generated.class");
      fos.write(code);
      fos.close();

      //System.out.println("gom term :\n\n" + c + "\n");
    } catch(IOException ioe) {
      System.err.println("Class not found : " + args[0]);
    }
  }
}

