
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

/**
 * A dot control flow graph exporter.
 * This class generates a control flow graph for each method of a class.
 */
public class CFGViewer {
  %include { mustrategy.tom }
  %include { classtree/ClassTree.tom }
  %typeterm Map { implement { java.util.Map } }

  /**
   * This strategy fills in the `Map' m with the `ConsInstructionList'
   * corresponding to each `LabeledInstruction'.
   * Thus, we can retrieve the `ConsInstructionList' from a `Label'.
   * @param m the map to be filled.
   */
  %strategy BuildLabelMap(m:Map) extends Identity() {
    visit TInstructionList {
      c@ConsInstructionList(LabeledInstruction[label=l], _) -> {
        m.put(`l, `c);
      }
    }
  }

  /**
   * `AllCfg' stands for AllControlFlowGraph.
   * This works as the classical `All' strategy but is
   * adapted for a ControlFlowGraph run.
   * (i.e. a `Goto' instruction has one child : the one to jump to;
   * a `IfXX' instruction has two children : the one which
   * satisfies the expression, and the other...)
   * @param s the strategy to be applied on each child.
   * @param m the label map (see the BuildLabelMap strategy).
   */
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

          (Tableswitch|Lookupswitch)[dflt=dflt, labels=labels] -> {
            TLabelList labelList = `labels;
            %match(TLabelList labelList) {
              LabelList(_*, x, _*) -> {
                s.visit((TInstructionList)m.get(`x));
              }
            }
            s.visit((TInstructionList)m.get(`dflt));
            return `c;
          }

          // Visit the next instruction.
          _ -> { s.visit(`t); }
        }
      }
    }
  }

  /**
   * Adds one to the current node mark.
   * @param map the map containing all instructions marks.
   */
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

  /**
   * Substract one to the current node mark.
   * @param map the map containing all instructions marks.
   */
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

  /**
   * Indicates if the current node is marked (>0) or not.
   * @param map the map containing all instructions marks.
   */
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

  /**
   * Returns the dot node id of the given TInstructionList.
   * @param ins the instruction.
   * @return the id.
   */
  private static String getDotId(TInstructionList ins) {
    return ("insid" + ins.hashCode()).replace('-', 'm');
  }

  /**
   * Returns the dot node id of the given TTryCatchBlock.
   * @param bl the try/catch block.
   * @return the id.
   */
  private static String getDotId(TTryCatchBlock bl) {
    return ("blockid" + bl.hashCode()).replace('-', 'm');
  }

  /**
   * Returns the dot node id of the given TLocalVariable.
   * @param the local variable.
   * @return the id.
   */
  private static String getDotId(TLocalVariable var) {
    return ("varid" + var.hashCode()).replace('-', 'm');
  }

  /**
   * Cleans the given string to prevent dot compilation problems.
   * (ex: replace the character '"' with the string "\"").
   * @param s the string to be cleaned.
   * @return the cleaned string.
   */
  private static String clean(String s) {
    return s.replaceAll("\\\"", "\\\\\\\"");
  }

  /**
   * Prints the current instruction node with a suitable label.
   * @param out the writer to be used for the dot output.
   */
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

            s@Tableswitch[min=min, max=max] -> {
              out.write(%[
                  @id@ [label="@`s.symbolName()@(@`min@, @`max@)"];
                  ]%);
              return `c;
            }

            s@Lookupswitch[keys=keys] -> {
              out.write(%[
                  @id@ [label="@`s.symbolName()@(]%);
              TintList keys = `keys;
              %match(TintList keys) {
                intList(_*, x, _*, _) -> {
                  out.write(%[@Integer.toString(`x)@, ]%);
                }
                intList(_*, x) -> {
                  out.write(Integer.toString(`x));
                }
              }
              out.write(%[)"];]%);
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

  /**
   * Prints a link from the `parent' instruction to the current node instruction.
   * @param out the writer to be used for the dot output.
   */
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

  /**
   * Prints all the try/catch/finally informations of the given block list.
   * @param list the try/catch/finally blocks to be printed.
   * @param labelMap the label map (see the BuildLabelMap strategy).
   * @param out the writer to be used for the dot output.
   */
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

  /**
   * Prints all the local variables informations.
   * @param list the local variables list to be printed.
   * @param labelMap the label map (see the BuildLabelMap strategy).
   * @param out the writer to be used for the dot output.
   */
  private static void printLocalVariables(TLocalVariableList list, Map labelMap, Writer out) {
    %match(TLocalVariableList list) {
      LocalVariableList(_*, x, _*) -> {
        try {
          TLocalVariable var = `x;
          String id = getDotId(var);

          out.write(%[
              @id@ [label="var : @var.getname()@\ndescriptor : @clean(var.getdesc())@\nindex : @Integer.toString(var.getindex())@" shape=box];
              @id@ -> @getDotId((TInstructionList)labelMap.get(var.getstart()))@ [label="start" style=dotted];
              @id@ -> @getDotId((TInstructionList)labelMap.get(var.getend()))@ [label="end" style=dotted];
              ]%);
        } catch(IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Used to pass the stored instruction as a strategy parameter.
   */
  private static class InsWrapper { public TInstructionList ins; }
  %typeterm InsWrapper { implement { InsWrapper } }

  /**
   * Assign the current instruction node to the given InsWrapper.
   * @param ins the instruction wrapper.
   */
  %strategy Assign(ins:InsWrapper) extends Identity() {
    visit TInstructionList {
      c@_ -> { ins.ins = `c; }
    }
  }

  /**
   * Generates a control flow graph for each method of the given class.
   * @param clazz the gom-term subject representing the class.
   */
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

            // Prints the try/catch/finally blocks.
            printTryCatchBlocks(`x.getcode().gettryCatchBlocks(), labelMap, w);

            // Prints the local variables informations.
            printLocalVariables(`x.getcode().getlocalVariables(), labelMap, w);
          }

          w.write("}\n");
          w.flush();
        } catch(IOException e) {
          e.printStackTrace();
        }
      }
    }
  }


  /**
   * Generates the dot control flow graphs for each method of the specified class.
   * Usage : java bytecode.CFGViewer <class name>
   * Ex: java bytecode.CFGViewer bytecode.Subject
   * @param args args[0] : the class name
   */
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

