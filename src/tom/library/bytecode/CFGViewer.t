/*
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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

import java.util.Map;
import java.util.HashMap;
import java.io.Writer;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassAdapter;

import tom.library.sl.*;

import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;

/**
 * A dot control flow graph exporter.
 * This class generates a control flow graph for each method of a class.
 */
public class CFGViewer {
  
  %include { ../mapping/java/bytecode/cfg.tom }

  %typeterm Writer { 
    implement {java.io.Writer} 
    is_sort(t) { ($t instanceof java.io.Writer) }
  }

  /**
   * Returns the dot node id of the given InstructionList.
   * @param ins the instruction.
   * @return the id.
   */
  private static String getDotId(InstructionList ins) {
    return ("insid" + ins.hashCode()).replace('-', 'm');
  }

  /**
   * Returns the dot node id of the given TryCatchBlock.
   * @param bl the try/catch block.
   * @return the id.
   */
  private static String getDotId(TryCatchBlock bl) {
    return ("blockid" + bl.hashCode()).replace('-', 'm');
  }

  /**
   * Returns the dot node id of the given LocalVariable.
   * @param the local variable.
   * @return the id.
   */
  private static String getDotId(LocalVariable var) {
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
    visit InstructionList {
      c@ConsInstructionList(ins, _) -> {
        String id = `getDotId(c);
        printDotInstruction(`ins, id, out);
      }
    }
  }

  /**
   * Prints the given instruction with a suitable label and id.
   * @param ins the instruction to be printed.
   * @param id the id of the dot node.
   * @param out the writer to be used for the dot output.
   */
  private static void printDotInstruction(Instruction ins, String id, Writer out) {
    try {
      %match(ins) {

        (Bipush|Sipush|Newarray)(operand) -> {
          out.write(%[
              @id@ [label="@ins.symbolName()@\noperand : @Integer.toString(`operand)@"];
              ]%);
          return;
        }

        Multianewarray(typeDesc, dims) -> {
          out.write(%[
              @id@ [label="@ins.symbolName()@\ntypeDesc : @`typeDesc@\ndims : @Integer.toString(`dims)@"];
              ]%);
          return;
        }

        Ldc(cst) -> {
          out.write(%[
              @id@ [label="@ins.symbolName()@\ncst : @clean(`cst.toString())@"];
              ]%);
          return;
        }

        (Iload|Lload|Fload|Dload|Aload|
         Istore|Lstore|Fstore|Dstore|Astore|
         Ret)(var) -> {
          out.write(%[
              @id@ [label="@ins.symbolName()@\nvar : @Integer.toString(`var)@"];
              ]%);
          return;
        }

        Iinc(incr, var) -> {
          out.write(%[
              @id@ [label="@ins.symbolName()@\nincr : @Integer.toString(`incr)@\nvar : @Integer.toString(`var)@"];
              ]%);
          return;
        }

        Tableswitch[min=min, max=max] -> {
          out.write(%[
              @id@ [label="@ins.symbolName()@\nmin : @`min@\nmax : @`max@"];
              ]%);
          return;
        }

        Lookupswitch[keys=keys] -> {
          out.write(%[
              @id@ [label="@ins.symbolName()@\nkeys : ]%);
              IntList keys = `keys;
              %match(keys) {
                IntList(_*, x, _*, _) -> {
                  out.write(%[@Integer.toString(`x)@, ]%);
                }
                IntList(_*, x) -> {
                  out.write(Integer.toString(`x));
                }
              }
              out.write(%["];]%);
              return;
        }

        (Getstatic|Putstatic|Getfield|Putfield)(owner, name, fieldDesc) -> {
          out.write(%[
              @id@ [label="@ins.symbolName()@\nowner : @`owner@\nname : @`name@\ndescriptor : @ToolBox.buildDescriptor(`fieldDesc)@"];
              ]%);
          return;
        }

        (Invokevirtual|Invokespecial|Invokestatic|Invokeinterface)(owner, name, methodDesc) -> {
          out.write(%[
              @id@ [label="@ins.symbolName()@\nowner : @`owner@\nname : @`name@\ndescriptor : @ToolBox.buildDescriptor(`methodDesc)@"];
              ]%);
          return;
        }

        (New|Anewarray|Checkcast|Instanceof)(typeDesc) -> {
          out.write(%[
              @id@ [label="@ins.symbolName()@\ndescriptor : @`typeDesc@"];
              ]%);
          return;
        }

        _ -> {
          out.write(%[
              @id@ [label="@ins.symbolName()@"];
              ]%);
        }
      }
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Prints a link from the `parent' instruction to the current node instruction.
   * @param out the writer to be used for the dot output.
   */
  %strategy PrintDotLink(out:Writer, parent:InsWrapper) extends Identity() {
    visit InstructionList {
      c@ConsInstructionList[] -> {
        try {
          out.write(%[@getDotId(parent.ins)@ -> @`getDotId(c)@;
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
   * @param inst the global list of instructions.
   */
  private static void printTryCatchBlocks(TryCatchBlockList list, Map labelMap, Writer out,InstructionList inst) throws VisitFailure{
    %match(list) {
      TryCatchBlockList(_*, x, _*) -> {
        try {
          TryCatchBlock block = `x;
          Handler handler = block.gethandler();
          String id = getDotId(block);

          %match(handler) {
            CatchHandler(h, t) -> {
              Position labelPosition = (Position) labelMap.get(`h);
              InstructionList labelInst = (InstructionList) labelPosition.getSubterm().visit(inst);
              out.write(%[
                  @id@ [label="Catch\ntype : @`t@" shape=box];
                  @id@ -> @getDotId(labelInst)@ [label="handler" style=dotted];
                  ]%);
            }

            FinallyHandler(h) -> {
              Position labelPosition = (Position) labelMap.get(`h);
              InstructionList labelInst = (InstructionList) labelPosition.getSubterm().visit(inst);
              out.write(%[
                  @id@ [label="Finally" shape=box];
                  @id@ -> @getDotId(labelInst)@ [label="handler" style=dotted];
                  ]%);
            }
          }

          Position startPosition = (Position) labelMap.get(block.getstart());
          InstructionList startInst = (InstructionList) startPosition.getSubterm().visit(inst);
          Position endPosition = (Position) labelMap.get(block.getend());
          InstructionList lastInst = (InstructionList) endPosition.getSubterm().visit(inst);
          out.write(%[
              @id@ -> @getDotId(startInst)@ [label="start" style=dotted];
              @id@ -> @getDotId(lastInst)@ [label="end" style=dotted];
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
   * @param inst the global list of instructions.
   */
  private static void printLocalVariables(LocalVariableList list, Map labelMap, Writer out, InstructionList inst) throws VisitFailure {
    %match(list) {
      LocalVariableList(_*, x, _*) -> {
        try {
          LocalVariable var = `x;
          String id = getDotId(var);
          Position startPosition = (Position) labelMap.get(var.getstart());
          InstructionList startInst = (InstructionList) startPosition.getSubterm().visit(inst);
          Position endPosition = (Position) labelMap.get(var.getend());
          InstructionList lastInst = (InstructionList) endPosition.getSubterm().visit(inst);

          out.write(%[
              @id@ [label="var : @var.getname()@\ndescriptor : @var.gettypeDesc()@\nindex : @Integer.toString(var.getindex())@" shape=box];
              @id@ -> @getDotId(startInst)@ [label="start" style=dotted];
              @id@ -> @getDotId(lastInst)@ [label="end" style=dotted];
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
  private static class InsWrapper { public InstructionList ins; }
  %typeterm InsWrapper { 
    implement { InsWrapper }
    is_sort(t) { ($t instanceof InsWrapper) }
  }

  /**
   * Assign the current instruction node to the given InsWrapper.
   * @param ins the instruction wrapper.
   */
  %strategy Assign(ins:InsWrapper) extends Identity() {
    visit InstructionList {
      c@_ -> { ins.ins = `c; }
    }
  }

  /**
   * Generates a control flow graph for each method of the given class.
   * @param ast the gom-term subject representing the class.
   */
  public static void classToDot(ClassNode ast) throws VisitFailure {
    Writer w = new BufferedWriter(new OutputStreamWriter(System.out)); 
    MethodList methods = ast.getmethods();
    %match(methods) {
      MethodList(_*, x, _*) -> {
        try {
          MethodInfo info = `x.getinfo();
          w.write(%[digraph @info.getname()@ {
              ]%);

          // Print a root node with the method name and descriptor. Add a link to the first instruction if any.
          w.write(%[method [label="method : @info.getname()@\ndescriptor : @ToolBox.buildDescriptor(info.getdesc())@" shape=box];
              ]%);
          if(!`x.getcode().isEmptyCode()) {
            InstructionList ins = `x.getcode().getinstructions();
            if(!ins.isEmptyInstructionList()) {
              w.write(%[method -> @getDotId(ins)@
                  ]%);
            }

            // Compute the label map to allow us to retrieve an instruction from a label.
            HashMap labelMap = new HashMap();
            `TopDown(BuildLabelMap(labelMap)).visit(ins);

            // Create a wrapper to pass a parent node to its children.
            InsWrapper insWrapper = new InsWrapper();

            // This strategy run through all node. For each of them, the node is printed.
            // Links between the current node and its children are printed by passing the parent to each of them.
            // AllCfg allows us to get all the children of the current node.
            Strategy toDot = `TopDown(
                Try(
                  Sequence(
                    PrintDotNode(w),
                    Sequence(
                      Assign(insWrapper),
                      AllCfg(
                        PrintDotLink(w, insWrapper),
                        labelMap)))));

            toDot.visit(ins);

            // Prints the try/catch/finally blocks.
            printTryCatchBlocks(`x.getcode().gettryCatchBlocks(), labelMap, w, ins);

            // Prints the local variables informations.
            printLocalVariables(`x.getcode().getlocalVariables(), labelMap, w, ins);
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
       System.out.println("Usage : java bytecode.CFGViewer <class name>\nEx: java bytecode.CFGViewer MyClass");
       return;
     }
     BytecodeReader cg = new BytecodeReader(args[0]);
     ClassNode c = cg.getAst();
     try {
       classToDot(c);
     } catch (VisitFailure e) {
       System.out.println("Unexpected failure in strategies");
     }
   }

}


