
package bytecode;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassAdapter;

import tom.library.strategy.mutraveler.MuStrategy;
import jjtraveler.VisitFailure;

import bytecode.classtree.*;
import bytecode.classtree.types.*;

public class CFGViewer {
  %include { mustrategy.tom }
  %include { classtree/ClassTree.tom }
  %typeterm Map { implement { java.util.Map } }

  // This strategy fills in the `Map' m with the `ConsInstructionList'
  // corresponding to each `LabeledInstruction'.
  // Thus, we can retrieve the `ConsInstructionList' from a `Label'.
  %strategy BuildLabelMap(m:Map) extends Identity() {
    visit TInstructionList {
      c@ConsInstructionList(LabeledInstruction[label=l], _) -> {
        m.put(`l, `c);
      }
    }
  }

  // `AllCfg' stands for AllControlFlowGraph.
  // This works as the classical `All' strategy but is
  // adapted for a ControlFlowGraph run.
  // (i.e. a `Goto' instruction has one child : the one to jump to;
  // a `IfXX' instruction has two children : the one which
  // satisfies the expression, and the other...)
  %strategy AllCfg(s:Strategy, m:Map) extends Identity() {
    visit TInstructionList {
      c@ConsInstructionList(ins, t) -> {
        TInstruction ins = `ins;
        %match(TInstruction ins) {
          Goto(l) -> {
            s.visit((TInstructionList)m.get(`l));
            return `c;
          }

          (Ifeq|Ifne|Iflt|Ifge|Ifgt|Ifle|
           If_icmpeq|If_icmpne|If_icmplt|If_icmpge|If_icmpgt|If_icmple|
           If_acmpeq|If_acmpne|Jsr|Ifnull|Ifnonnull)(l) -> {
            s.visit((TInstructionList)m.get(`l));
          }

          // Visit the next instruction.
          _ -> { s.visit(`t); }
        }
      }
    }
  }

  // Add one to the current node mark.
  %strategy Mark(map:Map) extends Identity() {
    visit TInstructionList {
      c@_ -> {
        Object o = map.get(`c);
        int value = 1;
        if(o != null)
          value = ((Integer)o).intValue() + 1;
        map.put(`c, new Integer(value));
      }
    }
  }

  // Substract one to the current node mark.
  %strategy UnMark(map:Map) extends Identity() {
    visit TInstructionList {
      c@_ -> {
        Object o = map.get(`c);
        if(o == null)
          throw new VisitFailure();
        int value = ((Integer)o).intValue() - 1;
        map.put(`c, new Integer(value));
      }
    }
  }

  // Indicate if the current node is marked (>0) or not.
  %strategy IsMarked(map:Map) extends Identity() {
    visit TInstructionList {
      c@_ -> {
        Object o = map.get(`c);
        if(o == null || ((Integer)o).intValue() <= 0)
          throw new VisitFailure();
      }
    }
  }

  %typeterm Writer { implement {java.io.Writer} }

  private static String getDotId(TInstructionList ins) {
    return ("id" + ins.hashCode()).replace('-', 'm');
  }
  private static String getDotId(TTryCatchBlock bl) {
    return ("blockid" + bl.hashCode()).replace('-', 'm');
  }

  private static String clean(String s) {
    return s.replaceAll("\\\"", "\\\\\\\"");
  }

  // Print the current node.
  %strategy PrintDotNode(out:Writer) extends Identity() {
    visit TInstructionList {
      c@ConsInstructionList(ins, _) -> {
        try {
          String id = getDotId(`c);
          TInstruction ins = `ins;
          %match(TInstruction ins) {
            LabeledInstruction(_, realIns) -> {
              out.write(%[
                  @id@ [label="@clean(`realIns.toString())@"];
                  ]%);
              return `c;
            }

            s@Goto(_) -> {
              out.write(%[
                  @id@ [label="@`s.symbolName()@"];
                  ]%);
              return `c;
            }

            s@(Ifeq|Ifne|Iflt|Ifge|Ifgt|Ifle|
             If_icmpeq|If_icmpne|If_icmplt|If_icmpge|If_icmpgt|If_icmple|
             If_acmpeq|If_acmpne|Jsr|Ifnull|Ifnonnull)(_) -> {
              out.write(%[
                  @id@ [label="@`s.symbolName()@"];
                  ]%);
              return `c;
            }

            _ -> {
              out.write(%[
                  @id@ [label="@clean(ins.toString())@"];
                  ]%);
            }
          }
        } catch(IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  // Print a link from `parent' to the current node.
  %strategy PrintDotLink(out:Writer, parent:InsWrapper) extends Identity() {
    visit TInstructionList {
      c@ConsInstructionList[] -> {
        try {
          out.write(%[@getDotId(parent.ins)@ -> @getDotId(`c)@;
              ]%);
        } catch(IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  // Print all the try/catch/finally informations.
  private static void printTryCatchBlocks(TTryCatchBlockList list, Map labelMap, Writer out) {
    %match(TTryCatchBlockList list) {
      TryCatchBlockList(_*, x, _*) -> {
        try {
          TTryCatchBlock block = `x;
          THandler handler = block.gethandler();
          String id = getDotId(block);

          %match(THandler handler) {
            CatchHandler(h, t) -> {
              out.write(%[
                  @id@ [label="Catch(@clean(`t)@)" shape=box];
                  @id@ -> @getDotId((TInstructionList)labelMap.get(`h))@ [label="handler" style=dotted];
                  ]%);
            }

            FinallyHandler(h) -> {
              out.write(%[
                  @id@ [label="Finally" shape=box];
                  @id@ -> @getDotId((TInstructionList)labelMap.get(`h))@ [label="handler" style=dotted];
                  ]%);
            }
          }

          out.write(%[
              @id@ -> @getDotId((TInstructionList)labelMap.get(block.getstart()))@ [label="start" style=dotted];
              @id@ -> @getDotId((TInstructionList)labelMap.get(block.getend()))@ [label="end" style=dotted];
              ]%);
        } catch(IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  // Used to pass the stored instruction as a strategy parameter.
  private static class InsWrapper { public TInstructionList ins; }
  %typeterm InsWrapper { implement { InsWrapper } }
  %strategy Assign(ins:InsWrapper) extends Identity() {
    visit TInstructionList {
      c@_ -> { ins.ins = `c; }
    }
  }

  // Generates a dot output corresponding to the given class.
  private static void classToDot(TClass clazz) {
    Writer w = new BufferedWriter(new OutputStreamWriter(System.out)); 
    TMethodList methods = clazz.getmethods();
    %match(TMethodList methods) {
      MethodList(_*, x, _*) -> {
        try {
          TMethodInfo info = `x.getinfo();
          w.write(%[digraph @info.getname()@ {
              ]%);

          // Print a root node with the method name and descriptor. Add a link to the first instruction if any.
          w.write(%[method [label="method : @info.getname()@\ndescriptor : @clean(info.getdesc())@" shape=box];
              ]%);
          if(!`x.getcode().isEmptyCode()) {
            TInstructionList ins = `x.getcode().getinstructions();
            if(!ins.isEmptyInstructionList()) {
            w.write(%[method -> @getDotId(ins)@
              ]%);
            }

            // Compute the label map to allow us to retrieve an instruction from a label.
            HashMap labelMap = new HashMap();
            `TopDown(BuildLabelMap(labelMap)).apply(ins);

            // Create a wrapper to pass a parent node to its children.
            InsWrapper insWrapper = new InsWrapper();

            // This strategy run through all node. For each of them, the node is printed.
            // Links between the current node and its children are printed by passing the parent to each of them.
            // AllCfg allows us to get all the children of the current node.
            MuStrategy toDot = `TopDown(
                Try(
                  Sequence(
                    PrintDotNode(w),
                    Sequence(
                      Assign(insWrapper),
                      AllCfg(
                        PrintDotLink(w, insWrapper),
                        labelMap)))));

            toDot.apply(ins);

            printTryCatchBlocks(`x.getcode().gettryCatchBlocks(), labelMap, w);
          }

          w.write("}\n");
          w.flush();
        } catch(IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static void main(String[] args) {
    if(args.length <= 0) {
      System.out.println("Usage : java bytecode.CFGViewer <class name>\nEx: java bytecode.CFGViewer bytecode.Subject");
      return;
    }

    try {
      ClassReader cr = new ClassReader(args[0]);
      TClassGenerator cg = new TClassGenerator();
      ClassAdapter ca = new ClassAdapter(cg);
      cr.accept(ca, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

      TClass c = cg.getTClass();

      classToDot(c);
    } catch(IOException ioe) {
      System.err.println("Class not found : " + args[0]);
    }
  }
}

