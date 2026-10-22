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

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;

import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;

import java.util.HashMap;
import java.util.Map;

public class MethodGenerator implements MethodVisitor {

  %include { ../adt/bytecode/Bytecode.tom }

  private BytecodeReader bytecodeReader;
  private Method method;
  private InstructionList instructions;
  private TryCatchBlockList tryCatchBlocks;
  private LocalVariableList localVariables;

  private static int labelCounter = 0;
  private Map<Label, LabelNode> labelmap = new HashMap<Label, LabelNode>();

  public MethodGenerator (
      BytecodeReader cg,
      AccessList access,
      String name,
      MethodDescriptor desc,
      Signature signature,
      StringList exceptions) {
    bytecodeReader = cg;
    method = `Method(MethodInfo(bytecodeReader.getAst().getinfo().getname(), access, name, desc, signature, exceptions), EmptyCode());
  }

  private void appendInstruction(Instruction ins) {
    instructions = `InstructionList(instructions*, ins);
  }

  private void appendTryCatchBlock(TryCatchBlock tcb) {
    tryCatchBlocks = `TryCatchBlockList(tryCatchBlocks*, tcb);
  }
  private void appendLocalVariable(LocalVariable lv) {
    localVariables = `LocalVariableList(localVariables*, lv);
  }

  public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
    // TODO
    return null;
  }

  public AnnotationVisitor visitAnnotationDefault() {
    // TODO
    return null;
  }

  public void visitAttribute(Attribute attr) {
    // TODO
  }

  public void visitCode() {
    instructions = `InstructionList();
    tryCatchBlocks = `TryCatchBlockList();
    localVariables = `LocalVariableList();
  }

  public void visitEnd() {
    method = `Method(method.getinfo(), MethodCode(instructions, localVariables, tryCatchBlocks));

    bytecodeReader.appendMethod(method);
  }

  public void visitMaxs(int maxStack, int maxLocals) {
  }

  public void visitFrame(
      int type,
      int nLocal,
      Object[] local,
      int nStack,
      Object[] stack) {
  }

  public void visitFieldInsn(
      int opcode,
      String owner,
      String name,
      String desc) {
    Instruction ins = null;
    switch(opcode) {
      case Opcodes.GETSTATIC:
        ins = `Getstatic(owner, name, ToolBox.buildFieldDescriptor(desc));
        break;
      case Opcodes.PUTSTATIC:
        ins = `Putstatic(owner, name, ToolBox.buildFieldDescriptor(desc));
        break;
      case Opcodes.GETFIELD:
        ins = `Getfield(owner, name, ToolBox.buildFieldDescriptor(desc));
        break;
      case Opcodes.PUTFIELD:
        ins = `Putfield(owner, name, ToolBox.buildFieldDescriptor(desc));
        break;
      default:
        throw new RuntimeException("Unsupported OpCode :" + opcode);
    }

    appendInstruction(ins);
  }

  public void visitIincInsn(int var, int increment) {
    appendInstruction(`Iinc(increment, var));
  }

  public void visitInsn(int opcode) {
    Instruction ins = null;
    switch(opcode) {
      case Opcodes.NOP:
        ins = `Nop();
        break;
      case Opcodes.ACONST_NULL:
        ins = `Aconst_null();
        break;
      case Opcodes.ICONST_M1:
        ins = `Iconst_m1();
        break;
      case Opcodes.ICONST_0:
        ins = `Iconst_0();
        break;
      case Opcodes.ICONST_1:
        ins = `Iconst_1();
        break;
      case Opcodes.ICONST_2:
        ins = `Iconst_2();
        break;
      case Opcodes.ICONST_3:
        ins = `Iconst_3();
        break;
      case Opcodes.ICONST_4:
        ins = `Iconst_4();
        break;
      case Opcodes.ICONST_5:
        ins = `Iconst_5();
        break;
      case Opcodes.LCONST_0:
        ins = `Lconst_0();
        break;
      case Opcodes.LCONST_1:
        ins = `Lconst_1();
        break;
      case Opcodes.FCONST_0:
        ins = `Fconst_0();
        break;
      case Opcodes.FCONST_1:
        ins = `Fconst_1();
        break;
      case Opcodes.FCONST_2:
        ins = `Fconst_2();
        break;
      case Opcodes.DCONST_0:
        ins = `Dconst_0();
        break;
      case Opcodes.DCONST_1:
        ins = `Dconst_1();
        break;
      case Opcodes.IALOAD:
        ins = `Iaload();
        break;
      case Opcodes.LALOAD:
        ins = `Laload();
        break;
      case Opcodes.FALOAD:
        ins = `Faload();
        break;
      case Opcodes.DALOAD:
        ins = `Daload();
        break;
      case Opcodes.AALOAD:
        ins = `Aaload();
        break;
      case Opcodes.BALOAD:
        ins = `Baload();
        break;
      case Opcodes.CALOAD:
        ins = `Caload();
        break;
      case Opcodes.SALOAD:
        ins = `Saload();
        break;
      case Opcodes.IASTORE:
        ins = `Iastore();
        break;
      case Opcodes.LASTORE:
        ins = `Lastore();
        break;
      case Opcodes.FASTORE:
        ins = `Fastore();
        break;
      case Opcodes.DASTORE:
        ins = `Dastore();
        break;
      case Opcodes.AASTORE:
        ins = `Aastore();
        break;
      case Opcodes.BASTORE:
        ins = `Bastore();
        break;
      case Opcodes.CASTORE:
        ins = `Castore();
        break;
      case Opcodes.SASTORE:
        ins = `Sastore();
        break;
      case Opcodes.POP:
        ins = `Pop();
        break;
      case Opcodes.POP2:
        ins = `Pop2();
        break;
      case Opcodes.DUP:
        ins = `Dup();
        break;
      case Opcodes.DUP_X1:
        ins = `Dup_x1();
        break;
      case Opcodes.DUP_X2:
        ins = `Dup_x2();
        break;
      case Opcodes.DUP2:
        ins = `Dup2();
        break;
      case Opcodes.DUP2_X1:
        ins = `Dup2_x1();
        break;
      case Opcodes.DUP2_X2:
        ins = `Dup2_x2();
        break;
      case Opcodes.SWAP:
        ins = `Swap();
        break;
      case Opcodes.IADD:
        ins = `Iadd();
        break;
      case Opcodes.LADD:
        ins = `Ladd();
        break;
      case Opcodes.FADD:
        ins = `Fadd();
        break;
      case Opcodes.DADD:
        ins = `Dadd();
        break;
      case Opcodes.ISUB:
        ins = `Isub();
        break;
      case Opcodes.LSUB:
        ins = `Lsub();
        break;
      case Opcodes.FSUB:
        ins = `Fsub();
        break;
      case Opcodes.DSUB:
        ins = `Dsub();
        break;
      case Opcodes.IMUL:
        ins = `Imul();
        break;
      case Opcodes.LMUL:
        ins = `Lmul();
        break;
      case Opcodes.FMUL:
        ins = `Fmul();
        break;
      case Opcodes.DMUL:
        ins = `Dmul();
        break;
      case Opcodes.IDIV:
        ins = `Idiv();
        break;
      case Opcodes.LDIV:
        ins = `Ldiv();
        break;
      case Opcodes.FDIV:
        ins = `Fdiv();
        break;
      case Opcodes.DDIV:
        ins = `Ddiv();
        break;
      case Opcodes.IREM:
        ins = `Irem();
        break;
      case Opcodes.LREM:
        ins = `Lrem();
        break;
      case Opcodes.FREM:
        ins = `Frem();
        break;
      case Opcodes.DREM:
        ins = `Drem();
        break;
      case Opcodes.INEG:
        ins = `Ineg();
        break;
      case Opcodes.LNEG:
        ins = `Lneg();
        break;
      case Opcodes.FNEG:
        ins = `Fneg();
        break;
      case Opcodes.DNEG:
        ins = `Dneg();
        break;
      case Opcodes.ISHL:
        ins = `Ishl();
        break;
      case Opcodes.LSHL:
        ins = `Lshl();
        break;
      case Opcodes.ISHR:
        ins = `Ishr();
        break;
      case Opcodes.LSHR:
        ins = `Lshr();
        break;
      case Opcodes.IUSHR:
        ins = `Iushr();
        break;
      case Opcodes.LUSHR:
        ins = `Lushr();
        break;
      case Opcodes.IAND:
        ins = `Iand();
        break;
      case Opcodes.LAND:
        ins = `Land();
        break;
      case Opcodes.IOR:
        ins = `Ior();
        break;
      case Opcodes.LOR:
        ins = `Lor();
        break;
      case Opcodes.IXOR:
        ins = `Ixor();
        break;
      case Opcodes.LXOR:
        ins = `Lxor();
        break;
      case Opcodes.I2L:
        ins = `I2l();
        break;
      case Opcodes.I2F:
        ins = `I2f();
        break;
      case Opcodes.I2D:
        ins = `I2d();
        break;
      case Opcodes.L2I:
        ins = `L2i();
        break;
      case Opcodes.L2F:
        ins = `L2f();
        break;
      case Opcodes.L2D:
        ins = `L2d();
        break;
      case Opcodes.F2I:
        ins = `F2i();
        break;
      case Opcodes.F2L:
        ins = `F2l();
        break;
      case Opcodes.F2D:
        ins = `F2d();
        break;
      case Opcodes.D2I:
        ins = `D2i();
        break;
      case Opcodes.D2L:
        ins = `D2l();
        break;
      case Opcodes.D2F:
        ins = `D2f();
        break;
      case Opcodes.I2B:
        ins = `I2b();
        break;
      case Opcodes.I2C:
        ins = `I2c();
        break;
      case Opcodes.I2S:
        ins = `I2s();
        break;
      case Opcodes.LCMP:
        ins = `Lcmp();
        break;
      case Opcodes.FCMPL:
        ins = `Fcmpl();
        break;
      case Opcodes.FCMPG:
        ins = `Fcmpg();
        break;
      case Opcodes.DCMPL:
        ins = `Dcmpl();
        break;
      case Opcodes.DCMPG:
        ins = `Dcmpg();
        break;
      case Opcodes.IRETURN:
        ins = `Ireturn();
        break;
      case Opcodes.LRETURN:
        ins = `Lreturn();
        break;
      case Opcodes.FRETURN:
        ins = `Freturn();
        break;
      case Opcodes.DRETURN:
        ins = `Dreturn();
        break;
      case Opcodes.ARETURN:
        ins = `Areturn();
        break;
      case Opcodes.RETURN:
        ins = `Return();
        break;
      case Opcodes.ARRAYLENGTH:
        ins = `Arraylength();
        break;
      case Opcodes.ATHROW:
        ins = `Athrow();
        break;
      case Opcodes.MONITORENTER:
        ins = `Monitorenter();
        break;
      case Opcodes.MONITOREXIT:
        ins = `Monitorexit();
        break;
      default:
        throw new RuntimeException("Unsupported OpCode :" + opcode);
    }

    appendInstruction(ins);
  }

  public void visitIntInsn(int opcode, int operand) {
    Instruction ins = null;

    switch(opcode) {
      case Opcodes.BIPUSH:
        ins = `Bipush(operand);
        break;
      case Opcodes.SIPUSH:
        ins = `Sipush(operand);
        break;
      case Opcodes.NEWARRAY:
        ins = `Newarray(operand);
        break;
      default:
        throw new RuntimeException("Unsupported OpCode :" + opcode);
    }

    appendInstruction(ins);
  }


  public void visitJumpInsn(int opcode, Label label) {
    Instruction ins = null;

    LabelNode l = buildLabelNode(label);

    switch(opcode) {
      case Opcodes.IFEQ:
        ins = `Ifeq(l);
        break;
      case Opcodes.IFNE:
        ins = `Ifne(l);
        break;
      case Opcodes.IFLT:
        ins = `Iflt(l);
        break;
      case Opcodes.IFGE:
        ins = `Ifge(l);
        break;
      case Opcodes.IFGT:
        ins = `Ifgt(l);
        break;
      case Opcodes.IFLE:
        ins = `Ifle(l);
        break;
      case Opcodes.IF_ICMPEQ:
        ins = `If_icmpeq(l);
        break;
      case Opcodes.IF_ICMPNE:
        ins = `If_icmpne(l);
        break;
      case Opcodes.IF_ICMPLT:
        ins = `If_icmplt(l);
        break;
      case Opcodes.IF_ICMPGE:
        ins = `If_icmpge(l);
        break;
      case Opcodes.IF_ICMPGT:
        ins = `If_icmpgt(l);
        break;
      case Opcodes.IF_ICMPLE:
        ins = `If_icmple(l);
        break;
      case Opcodes.IF_ACMPEQ:
        ins = `If_acmpeq(l);
        break;
      case Opcodes.IF_ACMPNE:
        ins = `If_acmpne(l);
        break;
      case Opcodes.GOTO:
        ins = `Goto(l);
        break;
      case Opcodes.JSR:
        ins = `Jsr(l);
        break;
      case Opcodes.IFNULL:
        ins = `Ifnull(l);
        break;
      case Opcodes.IFNONNULL:
        ins = `Ifnonnull(l);
        break;
      default:
          throw new RuntimeException("Unsupported OpCode :" + opcode);
    }

    appendInstruction(ins);
  }

  public void visitLabel(Label label) {
    appendInstruction(`Anchor(buildLabelNode(label)));
  }

  public void visitLdcInsn(Object cst) {
    appendInstruction(`Ldc(ToolBox.buildValue(cst)));
  }

  public void visitLineNumber(int line, Label start) {
    // TODO
  }

  public void visitLocalVariable(
      String name,
      String desc,
      String signature,
      Label start,
      Label end,
      int index) {
    appendLocalVariable(`LocalVariable(name, desc, Signature(signature), buildLabelNode(start), buildLabelNode(end), index));
  }

  public void visitLookupSwitchInsn(
      Label dflt,
      int[] keys,
      Label[] labels) {
    IntList kList = `IntList();

    appendInstruction(`Lookupswitch(buildLabelNode(dflt), ToolBox.buildIntList(keys), buildLabelNodeList(labels)));
  }

  public void visitMethodInsn(
      int opcode,
      String owner,
      String name,
      String desc) {
    Instruction ins = null;

    switch(opcode) {
      case Opcodes.INVOKEVIRTUAL:
        ins = `Invokevirtual(owner, name, ToolBox.buildMethodDescriptor(desc));
        break;
      case Opcodes.INVOKESPECIAL:
        ins = `Invokespecial(owner, name, ToolBox.buildMethodDescriptor(desc));
        break;
      case Opcodes.INVOKESTATIC:
        ins = `Invokestatic(owner, name, ToolBox.buildMethodDescriptor(desc));
        break;
      case Opcodes.INVOKEINTERFACE:
        ins = `Invokeinterface(owner, name, ToolBox.buildMethodDescriptor(desc));
        break;
      default:
        throw new RuntimeException("Unsupported OpCode :" + opcode);
    }

    appendInstruction(ins);
  }

  public void visitMultiANewArrayInsn(String desc, int dims) {
    appendInstruction(`Multianewarray(desc, dims));
  }

  public AnnotationVisitor visitParameterAnnotation(
      int parameter,
      String desc,
      boolean visible) {
    // TODO
    return null;
  }

  public void visitTableSwitchInsn(
      int min,
      int max,
      Label dflt,
      Label[] labels) {
    appendInstruction(`Tableswitch(min, max, buildLabelNode(dflt), buildLabelNodeList(labels)));
  }

  public void visitTryCatchBlock(
      Label start,
      Label end,
      Label handler,
      String type) {
    appendTryCatchBlock(`TryCatchBlock(buildLabelNode(start), buildLabelNode(end), CatchHandler(buildLabelNode(handler), type)));
  }

  public void visitTypeInsn(int opcode, String desc) {
    Instruction ins = null;

    switch(opcode) {
      case Opcodes.NEW:
        ins = `New(desc);
        break;
      case Opcodes.ANEWARRAY:
        ins = `Anewarray(desc);
        break;
      case Opcodes.CHECKCAST:
        ins = `Checkcast(desc);
        break;
      case Opcodes.INSTANCEOF:
        ins = `Instanceof(desc);
        break;
      default:
        throw new RuntimeException("Unsupported OpCode :" + opcode);
    }

    appendInstruction(ins);
  }

  public void visitVarInsn(int opcode, int var) {
    Instruction ins = null;

    switch(opcode) {
      case Opcodes.ILOAD:
        ins = `Iload(var);
        break;
      case Opcodes.LLOAD:
        ins = `Lload(var);
        break;
      case Opcodes.FLOAD:
        ins = `Fload(var);
        break;
      case Opcodes.DLOAD:
        ins = `Dload(var);
        break;
      case Opcodes.ALOAD:
        ins = `Aload(var);
        break;
      case Opcodes.ISTORE:
        ins = `Istore(var);
        break;
      case Opcodes.LSTORE:
        ins = `Lstore(var);
        break;
      case Opcodes.FSTORE:
        ins = `Fstore(var);
        break;
      case Opcodes.DSTORE:
        ins = `Dstore(var);
        break;
      case Opcodes.ASTORE:
        ins = `Astore(var);
        break;
      case Opcodes.RET:
        ins = `Ret(var);
        break;
      default:
        throw new RuntimeException("Unsupported OpCode :" + opcode);
    }

    appendInstruction(ins);
  }

  private LabelNode buildLabelNode(Label label) {
    LabelNode l = labelmap.get(label);
    if(l == null) {
      l = `LabelNode(labelCounter++);
      labelmap.put(label, l);
    }
    return l;
  }

  private LabelNodeList buildLabelNodeList(Label[] labels) {
    LabelNodeList labList = `LabelNodeList();
    if(labels != null) {
      for(int i = labels.length - 1; i >= 0; i--) {
        Label tmp=labels[i];
        labList = `ConsLabelNodeList(buildLabelNode(tmp), labList);
      }
    }
    return labList;
  }


}

