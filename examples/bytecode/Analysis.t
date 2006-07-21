
package bytecode;

import java.util.HashMap;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassAdapter;

import tom.library.strategy.mutraveler.MuStrategy;

import bytecode.classtree.*;
import bytecode.classtree.types.*;

public class Analysis {
  %include { mustrategy.tom }
  %include { classtree/ClassTree.tom }
  %typeterm Map { implement { java.util.Map } }

  %strategy BuildLabelMap(m:Map) extends Identity() {
    visit TInstructionList {
      c@ConsInstructionList(LabeledInstruction[label=l], _) -> {
        m.put(`l, `c);
      }
    }
  }

  %strategy AllCfg(s:Strategy, m:Map) extends Identity() {
    visit TInstructionList {
      c@ConsInstructionList(Goto(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
        return `c;
      }
      ConsInstructionList(Ifeq(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(Ifne(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(Iflt(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(Ifge(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(Ifgt(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(Ifle(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(If_icmpeq(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(If_icmpne(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(If_icmplt(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(If_icmpge(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(If_icmpgt(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(If_icmple(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(If_acmpeq(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(If_acmpne(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(Jsr(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(Ifnull(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }
      ConsInstructionList(Ifnonnull(l), t) -> {
        s.visit((TInstructionList)m.get(`l));
      }

      // Visit the next instruction.
      ConsInstructionList(_, t) -> { s.visit(`t); }
    }
  }

  %strategy IsLoad() extends Fail() {
    visit TInstructionList {
      c@ConsInstructionList(ins, _) -> {
        TInstruction inst = `ins;
        %match(TInstruction inst) {
          Iload(_) -> {
            return `c;
          }
          Lload(_) -> {
            return `c;
          }
          Fload(_) -> {
            return `c;
          }
          Dload(_) -> {
            return `c;
          }
          Aload(_) -> {
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

  %strategy IsStore(map:Map, var:String) extends Fail() {
    visit TInstructionList {
      c@ConsInstructionList(ins, _) -> {
        TInstruction inst = `ins;
        %match(TInstruction inst) {
          Istore(i) -> {
            map.put(var, new Integer(`i));
            return `c;
          }
          Lstore(i) -> {
            map.put(var, new Integer(`i));
            return `c;
          }
          Fstore(i) -> {
            map.put(var, new Integer(`i));
            return `c;
          }
          Dstore(i) -> {
            map.put(var, new Integer(`i));
            return `c;
          }
          Astore(i) -> {
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

  %strategy HasIndex(map:Map, var:String) extends Fail() {
    visit TInstructionList {
      c@ConsInstructionList(ins, _) -> {
        int index = ((Integer)map.get(var)).intValue();
        TInstruction inst = `ins;
        %match(TInstruction inst) {
          Istore(index) -> {
            return `c;
          }
          Lstore(index) -> {
            return `c;
          }
          Fstore(index) -> {
            return `c;
          }
          Dstore(index) -> {
            return `c;
          }
          Astore(index) -> {
            return `c;
          }
          (Istore_0|Lstore_0|Fstore_0|Dstore_0|Astore_0)() -> {
            if(index == 0)
              return `c;
          }
          (Istore_1|Lstore_1|Fstore_1|Dstore_1|Astore_1)() -> {
            if(index == 1)
              return `c;
          }
          (Istore_2|Lstore_2|Fstore_2|Dstore_2|Astore_2)() -> {
            if(index == 2)
              return `c;
          }
          (Istore_3|Lstore_3|Fstore_3|Dstore_3|Astore_3)() -> {
            if(index == 3)
              return `c;
          }
        }
      }
    }
  }

  %op Strategy AU(s1:Strategy, s2:Strategy, m:Map) {
    make(s1,s2,m) { `mu(MuVar("x"),Choice(s2,Sequence(Sequence(s1,AllCfg(MuVar("x"), m)),One(Identity()))))}
  }

  %strategy PrintInst() extends Identity() {
    visit TInstructionList {
      c@ConsInstructionList(ins, _) -> { System.out.println(`c); }
    }
  }

  public static void analyze(TClass clazz) {
    TMethodList methods = clazz.getmethods();
    %match(TMethodList methods) {
      MethodList(_*, x, _*) -> {
        System.out.println("method : " + `x.getinfo().getname());

        TInstructionList ins = `x.getcode().getinstructions();

        HashMap labelMap = new HashMap();
        `TopDown(BuildLabelMap(labelMap)).apply(ins);

        HashMap indexMap = new HashMap();
        MuStrategy noLoad =
          `AU(
              Not(Sequence(IsLoad(), HasIndex(indexMap, "index"))),
              Sequence(IsStore(indexMap, "useless"), HasIndex(indexMap, "index")),
              labelMap);

        MuStrategy storeNotUsed = `Sequence(IsStore(indexMap, "index"), AllCfg(noLoad, labelMap));

        `BottomUp(Try(ChoiceId(storeNotUsed, PrintInst()))).apply(ins);

      //imprimer le cfg d'une liste d'instructions :
      //`mu(MuVar("x"),Sequence(PrintInst(),AllCfg(MuVar("x"),labelMap))).apply(ins);
         

        //`TopDown(Try(Choice(Sequence(IsStore(indexMap, "index"), PrintInst()), Sequence(HasIndex(indexMap, "index"), PrintInst())))).apply(ins);
      }
    }
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

      //System.out.println("gom term :\n\n" + c + "\n");
      analyze(c);
    } catch(IOException ioe) {
      System.err.println("Class not found : " + args[0]);
    }
  }
}

