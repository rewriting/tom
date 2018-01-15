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

           private static   tom.library.adt.bytecode.types.LabelNodeList  tom_append_list_LabelNodeList( tom.library.adt.bytecode.types.LabelNodeList l1,  tom.library.adt.bytecode.types.LabelNodeList  l2) {     if( l1.isEmptyLabelNodeList() ) {       return l2;     } else if( l2.isEmptyLabelNodeList() ) {       return l1;     } else if(  l1.getTailLabelNodeList() .isEmptyLabelNodeList() ) {       return  tom.library.adt.bytecode.types.labelnodelist.ConsLabelNodeList.make( l1.getHeadLabelNodeList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.labelnodelist.ConsLabelNodeList.make( l1.getHeadLabelNodeList() ,tom_append_list_LabelNodeList( l1.getTailLabelNodeList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.LabelNodeList  tom_get_slice_LabelNodeList( tom.library.adt.bytecode.types.LabelNodeList  begin,  tom.library.adt.bytecode.types.LabelNodeList  end, tom.library.adt.bytecode.types.LabelNodeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyLabelNodeList()  ||  (end== tom.library.adt.bytecode.types.labelnodelist.EmptyLabelNodeList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.labelnodelist.ConsLabelNodeList.make( begin.getHeadLabelNodeList() ,( tom.library.adt.bytecode.types.LabelNodeList )tom_get_slice_LabelNodeList( begin.getTailLabelNodeList() ,end,tail)) ;   }      private static   tom.library.adt.bytecode.types.LocalVariableList  tom_append_list_LocalVariableList( tom.library.adt.bytecode.types.LocalVariableList l1,  tom.library.adt.bytecode.types.LocalVariableList  l2) {     if( l1.isEmptyLocalVariableList() ) {       return l2;     } else if( l2.isEmptyLocalVariableList() ) {       return l1;     } else if(  l1.getTailLocalVariableList() .isEmptyLocalVariableList() ) {       return  tom.library.adt.bytecode.types.localvariablelist.ConsLocalVariableList.make( l1.getHeadLocalVariableList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.localvariablelist.ConsLocalVariableList.make( l1.getHeadLocalVariableList() ,tom_append_list_LocalVariableList( l1.getTailLocalVariableList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.LocalVariableList  tom_get_slice_LocalVariableList( tom.library.adt.bytecode.types.LocalVariableList  begin,  tom.library.adt.bytecode.types.LocalVariableList  end, tom.library.adt.bytecode.types.LocalVariableList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyLocalVariableList()  ||  (end== tom.library.adt.bytecode.types.localvariablelist.EmptyLocalVariableList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.localvariablelist.ConsLocalVariableList.make( begin.getHeadLocalVariableList() ,( tom.library.adt.bytecode.types.LocalVariableList )tom_get_slice_LocalVariableList( begin.getTailLocalVariableList() ,end,tail)) ;   }      private static   tom.library.adt.bytecode.types.InstructionList  tom_append_list_InstructionList( tom.library.adt.bytecode.types.InstructionList l1,  tom.library.adt.bytecode.types.InstructionList  l2) {     if( l1.isEmptyInstructionList() ) {       return l2;     } else if( l2.isEmptyInstructionList() ) {       return l1;     } else if(  l1.getTailInstructionList() .isEmptyInstructionList() ) {       return  tom.library.adt.bytecode.types.instructionlist.ConsInstructionList.make( l1.getHeadInstructionList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.instructionlist.ConsInstructionList.make( l1.getHeadInstructionList() ,tom_append_list_InstructionList( l1.getTailInstructionList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.InstructionList  tom_get_slice_InstructionList( tom.library.adt.bytecode.types.InstructionList  begin,  tom.library.adt.bytecode.types.InstructionList  end, tom.library.adt.bytecode.types.InstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyInstructionList()  ||  (end== tom.library.adt.bytecode.types.instructionlist.EmptyInstructionList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.instructionlist.ConsInstructionList.make( begin.getHeadInstructionList() ,( tom.library.adt.bytecode.types.InstructionList )tom_get_slice_InstructionList( begin.getTailInstructionList() ,end,tail)) ;   }      private static   tom.library.adt.bytecode.types.IntList  tom_append_list_IntList( tom.library.adt.bytecode.types.IntList l1,  tom.library.adt.bytecode.types.IntList  l2) {     if( l1.isEmptyIntList() ) {       return l2;     } else if( l2.isEmptyIntList() ) {       return l1;     } else if(  l1.getTailIntList() .isEmptyIntList() ) {       return  tom.library.adt.bytecode.types.intlist.ConsIntList.make( l1.getHeadIntList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.intlist.ConsIntList.make( l1.getHeadIntList() ,tom_append_list_IntList( l1.getTailIntList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.IntList  tom_get_slice_IntList( tom.library.adt.bytecode.types.IntList  begin,  tom.library.adt.bytecode.types.IntList  end, tom.library.adt.bytecode.types.IntList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyIntList()  ||  (end== tom.library.adt.bytecode.types.intlist.EmptyIntList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.intlist.ConsIntList.make( begin.getHeadIntList() ,( tom.library.adt.bytecode.types.IntList )tom_get_slice_IntList( begin.getTailIntList() ,end,tail)) ;   }      private static   tom.library.adt.bytecode.types.TryCatchBlockList  tom_append_list_TryCatchBlockList( tom.library.adt.bytecode.types.TryCatchBlockList l1,  tom.library.adt.bytecode.types.TryCatchBlockList  l2) {     if( l1.isEmptyTryCatchBlockList() ) {       return l2;     } else if( l2.isEmptyTryCatchBlockList() ) {       return l1;     } else if(  l1.getTailTryCatchBlockList() .isEmptyTryCatchBlockList() ) {       return  tom.library.adt.bytecode.types.trycatchblocklist.ConsTryCatchBlockList.make( l1.getHeadTryCatchBlockList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.trycatchblocklist.ConsTryCatchBlockList.make( l1.getHeadTryCatchBlockList() ,tom_append_list_TryCatchBlockList( l1.getTailTryCatchBlockList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.TryCatchBlockList  tom_get_slice_TryCatchBlockList( tom.library.adt.bytecode.types.TryCatchBlockList  begin,  tom.library.adt.bytecode.types.TryCatchBlockList  end, tom.library.adt.bytecode.types.TryCatchBlockList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyTryCatchBlockList()  ||  (end== tom.library.adt.bytecode.types.trycatchblocklist.EmptyTryCatchBlockList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.trycatchblocklist.ConsTryCatchBlockList.make( begin.getHeadTryCatchBlockList() ,( tom.library.adt.bytecode.types.TryCatchBlockList )tom_get_slice_TryCatchBlockList( begin.getTailTryCatchBlockList() ,end,tail)) ;   }    

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
    method =  tom.library.adt.bytecode.types.method.Method.make( tom.library.adt.bytecode.types.methodinfo.MethodInfo.make(bytecodeReader.getAst().getinfo().getname(), access, name, desc, signature, exceptions) ,  tom.library.adt.bytecode.types.methodcode.EmptyCode.make() ) ;
  }

  private void appendInstruction(Instruction ins) {
    instructions = tom_append_list_InstructionList(instructions, tom.library.adt.bytecode.types.instructionlist.ConsInstructionList.make(ins, tom.library.adt.bytecode.types.instructionlist.EmptyInstructionList.make() ) );
  }

  private void appendTryCatchBlock(TryCatchBlock tcb) {
    tryCatchBlocks = tom_append_list_TryCatchBlockList(tryCatchBlocks, tom.library.adt.bytecode.types.trycatchblocklist.ConsTryCatchBlockList.make(tcb, tom.library.adt.bytecode.types.trycatchblocklist.EmptyTryCatchBlockList.make() ) );
  }
  private void appendLocalVariable(LocalVariable lv) {
    localVariables = tom_append_list_LocalVariableList(localVariables, tom.library.adt.bytecode.types.localvariablelist.ConsLocalVariableList.make(lv, tom.library.adt.bytecode.types.localvariablelist.EmptyLocalVariableList.make() ) );
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
    instructions =  tom.library.adt.bytecode.types.instructionlist.EmptyInstructionList.make() ;
    tryCatchBlocks =  tom.library.adt.bytecode.types.trycatchblocklist.EmptyTryCatchBlockList.make() ;
    localVariables =  tom.library.adt.bytecode.types.localvariablelist.EmptyLocalVariableList.make() ;
  }

  public void visitEnd() {
    method =  tom.library.adt.bytecode.types.method.Method.make(method.getinfo(),  tom.library.adt.bytecode.types.methodcode.MethodCode.make(instructions, localVariables, tryCatchBlocks) ) ;

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
        ins =  tom.library.adt.bytecode.types.instruction.Getstatic.make(owner, name, ToolBox.buildFieldDescriptor(desc)) ;
        break;
      case Opcodes.PUTSTATIC:
        ins =  tom.library.adt.bytecode.types.instruction.Putstatic.make(owner, name, ToolBox.buildFieldDescriptor(desc)) ;
        break;
      case Opcodes.GETFIELD:
        ins =  tom.library.adt.bytecode.types.instruction.Getfield.make(owner, name, ToolBox.buildFieldDescriptor(desc)) ;
        break;
      case Opcodes.PUTFIELD:
        ins =  tom.library.adt.bytecode.types.instruction.Putfield.make(owner, name, ToolBox.buildFieldDescriptor(desc)) ;
        break;
      default:
        throw new RuntimeException("Unsupported OpCode :" + opcode);
    }

    appendInstruction(ins);
  }

  public void visitIincInsn(int var, int increment) {
    appendInstruction( tom.library.adt.bytecode.types.instruction.Iinc.make(increment, var) );
  }

  public void visitInsn(int opcode) {
    Instruction ins = null;
    switch(opcode) {
      case Opcodes.NOP:
        ins =  tom.library.adt.bytecode.types.instruction.Nop.make() ;
        break;
      case Opcodes.ACONST_NULL:
        ins =  tom.library.adt.bytecode.types.instruction.Aconst_null.make() ;
        break;
      case Opcodes.ICONST_M1:
        ins =  tom.library.adt.bytecode.types.instruction.Iconst_m1.make() ;
        break;
      case Opcodes.ICONST_0:
        ins =  tom.library.adt.bytecode.types.instruction.Iconst_0.make() ;
        break;
      case Opcodes.ICONST_1:
        ins =  tom.library.adt.bytecode.types.instruction.Iconst_1.make() ;
        break;
      case Opcodes.ICONST_2:
        ins =  tom.library.adt.bytecode.types.instruction.Iconst_2.make() ;
        break;
      case Opcodes.ICONST_3:
        ins =  tom.library.adt.bytecode.types.instruction.Iconst_3.make() ;
        break;
      case Opcodes.ICONST_4:
        ins =  tom.library.adt.bytecode.types.instruction.Iconst_4.make() ;
        break;
      case Opcodes.ICONST_5:
        ins =  tom.library.adt.bytecode.types.instruction.Iconst_5.make() ;
        break;
      case Opcodes.LCONST_0:
        ins =  tom.library.adt.bytecode.types.instruction.Lconst_0.make() ;
        break;
      case Opcodes.LCONST_1:
        ins =  tom.library.adt.bytecode.types.instruction.Lconst_1.make() ;
        break;
      case Opcodes.FCONST_0:
        ins =  tom.library.adt.bytecode.types.instruction.Fconst_0.make() ;
        break;
      case Opcodes.FCONST_1:
        ins =  tom.library.adt.bytecode.types.instruction.Fconst_1.make() ;
        break;
      case Opcodes.FCONST_2:
        ins =  tom.library.adt.bytecode.types.instruction.Fconst_2.make() ;
        break;
      case Opcodes.DCONST_0:
        ins =  tom.library.adt.bytecode.types.instruction.Dconst_0.make() ;
        break;
      case Opcodes.DCONST_1:
        ins =  tom.library.adt.bytecode.types.instruction.Dconst_1.make() ;
        break;
      case Opcodes.IALOAD:
        ins =  tom.library.adt.bytecode.types.instruction.Iaload.make() ;
        break;
      case Opcodes.LALOAD:
        ins =  tom.library.adt.bytecode.types.instruction.Laload.make() ;
        break;
      case Opcodes.FALOAD:
        ins =  tom.library.adt.bytecode.types.instruction.Faload.make() ;
        break;
      case Opcodes.DALOAD:
        ins =  tom.library.adt.bytecode.types.instruction.Daload.make() ;
        break;
      case Opcodes.AALOAD:
        ins =  tom.library.adt.bytecode.types.instruction.Aaload.make() ;
        break;
      case Opcodes.BALOAD:
        ins =  tom.library.adt.bytecode.types.instruction.Baload.make() ;
        break;
      case Opcodes.CALOAD:
        ins =  tom.library.adt.bytecode.types.instruction.Caload.make() ;
        break;
      case Opcodes.SALOAD:
        ins =  tom.library.adt.bytecode.types.instruction.Saload.make() ;
        break;
      case Opcodes.IASTORE:
        ins =  tom.library.adt.bytecode.types.instruction.Iastore.make() ;
        break;
      case Opcodes.LASTORE:
        ins =  tom.library.adt.bytecode.types.instruction.Lastore.make() ;
        break;
      case Opcodes.FASTORE:
        ins =  tom.library.adt.bytecode.types.instruction.Fastore.make() ;
        break;
      case Opcodes.DASTORE:
        ins =  tom.library.adt.bytecode.types.instruction.Dastore.make() ;
        break;
      case Opcodes.AASTORE:
        ins =  tom.library.adt.bytecode.types.instruction.Aastore.make() ;
        break;
      case Opcodes.BASTORE:
        ins =  tom.library.adt.bytecode.types.instruction.Bastore.make() ;
        break;
      case Opcodes.CASTORE:
        ins =  tom.library.adt.bytecode.types.instruction.Castore.make() ;
        break;
      case Opcodes.SASTORE:
        ins =  tom.library.adt.bytecode.types.instruction.Sastore.make() ;
        break;
      case Opcodes.POP:
        ins =  tom.library.adt.bytecode.types.instruction.Pop.make() ;
        break;
      case Opcodes.POP2:
        ins =  tom.library.adt.bytecode.types.instruction.Pop2.make() ;
        break;
      case Opcodes.DUP:
        ins =  tom.library.adt.bytecode.types.instruction.Dup.make() ;
        break;
      case Opcodes.DUP_X1:
        ins =  tom.library.adt.bytecode.types.instruction.Dup_x1.make() ;
        break;
      case Opcodes.DUP_X2:
        ins =  tom.library.adt.bytecode.types.instruction.Dup_x2.make() ;
        break;
      case Opcodes.DUP2:
        ins =  tom.library.adt.bytecode.types.instruction.Dup2.make() ;
        break;
      case Opcodes.DUP2_X1:
        ins =  tom.library.adt.bytecode.types.instruction.Dup2_x1.make() ;
        break;
      case Opcodes.DUP2_X2:
        ins =  tom.library.adt.bytecode.types.instruction.Dup2_x2.make() ;
        break;
      case Opcodes.SWAP:
        ins =  tom.library.adt.bytecode.types.instruction.Swap.make() ;
        break;
      case Opcodes.IADD:
        ins =  tom.library.adt.bytecode.types.instruction.Iadd.make() ;
        break;
      case Opcodes.LADD:
        ins =  tom.library.adt.bytecode.types.instruction.Ladd.make() ;
        break;
      case Opcodes.FADD:
        ins =  tom.library.adt.bytecode.types.instruction.Fadd.make() ;
        break;
      case Opcodes.DADD:
        ins =  tom.library.adt.bytecode.types.instruction.Dadd.make() ;
        break;
      case Opcodes.ISUB:
        ins =  tom.library.adt.bytecode.types.instruction.Isub.make() ;
        break;
      case Opcodes.LSUB:
        ins =  tom.library.adt.bytecode.types.instruction.Lsub.make() ;
        break;
      case Opcodes.FSUB:
        ins =  tom.library.adt.bytecode.types.instruction.Fsub.make() ;
        break;
      case Opcodes.DSUB:
        ins =  tom.library.adt.bytecode.types.instruction.Dsub.make() ;
        break;
      case Opcodes.IMUL:
        ins =  tom.library.adt.bytecode.types.instruction.Imul.make() ;
        break;
      case Opcodes.LMUL:
        ins =  tom.library.adt.bytecode.types.instruction.Lmul.make() ;
        break;
      case Opcodes.FMUL:
        ins =  tom.library.adt.bytecode.types.instruction.Fmul.make() ;
        break;
      case Opcodes.DMUL:
        ins =  tom.library.adt.bytecode.types.instruction.Dmul.make() ;
        break;
      case Opcodes.IDIV:
        ins =  tom.library.adt.bytecode.types.instruction.Idiv.make() ;
        break;
      case Opcodes.LDIV:
        ins =  tom.library.adt.bytecode.types.instruction.Ldiv.make() ;
        break;
      case Opcodes.FDIV:
        ins =  tom.library.adt.bytecode.types.instruction.Fdiv.make() ;
        break;
      case Opcodes.DDIV:
        ins =  tom.library.adt.bytecode.types.instruction.Ddiv.make() ;
        break;
      case Opcodes.IREM:
        ins =  tom.library.adt.bytecode.types.instruction.Irem.make() ;
        break;
      case Opcodes.LREM:
        ins =  tom.library.adt.bytecode.types.instruction.Lrem.make() ;
        break;
      case Opcodes.FREM:
        ins =  tom.library.adt.bytecode.types.instruction.Frem.make() ;
        break;
      case Opcodes.DREM:
        ins =  tom.library.adt.bytecode.types.instruction.Drem.make() ;
        break;
      case Opcodes.INEG:
        ins =  tom.library.adt.bytecode.types.instruction.Ineg.make() ;
        break;
      case Opcodes.LNEG:
        ins =  tom.library.adt.bytecode.types.instruction.Lneg.make() ;
        break;
      case Opcodes.FNEG:
        ins =  tom.library.adt.bytecode.types.instruction.Fneg.make() ;
        break;
      case Opcodes.DNEG:
        ins =  tom.library.adt.bytecode.types.instruction.Dneg.make() ;
        break;
      case Opcodes.ISHL:
        ins =  tom.library.adt.bytecode.types.instruction.Ishl.make() ;
        break;
      case Opcodes.LSHL:
        ins =  tom.library.adt.bytecode.types.instruction.Lshl.make() ;
        break;
      case Opcodes.ISHR:
        ins =  tom.library.adt.bytecode.types.instruction.Ishr.make() ;
        break;
      case Opcodes.LSHR:
        ins =  tom.library.adt.bytecode.types.instruction.Lshr.make() ;
        break;
      case Opcodes.IUSHR:
        ins =  tom.library.adt.bytecode.types.instruction.Iushr.make() ;
        break;
      case Opcodes.LUSHR:
        ins =  tom.library.adt.bytecode.types.instruction.Lushr.make() ;
        break;
      case Opcodes.IAND:
        ins =  tom.library.adt.bytecode.types.instruction.Iand.make() ;
        break;
      case Opcodes.LAND:
        ins =  tom.library.adt.bytecode.types.instruction.Land.make() ;
        break;
      case Opcodes.IOR:
        ins =  tom.library.adt.bytecode.types.instruction.Ior.make() ;
        break;
      case Opcodes.LOR:
        ins =  tom.library.adt.bytecode.types.instruction.Lor.make() ;
        break;
      case Opcodes.IXOR:
        ins =  tom.library.adt.bytecode.types.instruction.Ixor.make() ;
        break;
      case Opcodes.LXOR:
        ins =  tom.library.adt.bytecode.types.instruction.Lxor.make() ;
        break;
      case Opcodes.I2L:
        ins =  tom.library.adt.bytecode.types.instruction.I2l.make() ;
        break;
      case Opcodes.I2F:
        ins =  tom.library.adt.bytecode.types.instruction.I2f.make() ;
        break;
      case Opcodes.I2D:
        ins =  tom.library.adt.bytecode.types.instruction.I2d.make() ;
        break;
      case Opcodes.L2I:
        ins =  tom.library.adt.bytecode.types.instruction.L2i.make() ;
        break;
      case Opcodes.L2F:
        ins =  tom.library.adt.bytecode.types.instruction.L2f.make() ;
        break;
      case Opcodes.L2D:
        ins =  tom.library.adt.bytecode.types.instruction.L2d.make() ;
        break;
      case Opcodes.F2I:
        ins =  tom.library.adt.bytecode.types.instruction.F2i.make() ;
        break;
      case Opcodes.F2L:
        ins =  tom.library.adt.bytecode.types.instruction.F2l.make() ;
        break;
      case Opcodes.F2D:
        ins =  tom.library.adt.bytecode.types.instruction.F2d.make() ;
        break;
      case Opcodes.D2I:
        ins =  tom.library.adt.bytecode.types.instruction.D2i.make() ;
        break;
      case Opcodes.D2L:
        ins =  tom.library.adt.bytecode.types.instruction.D2l.make() ;
        break;
      case Opcodes.D2F:
        ins =  tom.library.adt.bytecode.types.instruction.D2f.make() ;
        break;
      case Opcodes.I2B:
        ins =  tom.library.adt.bytecode.types.instruction.I2b.make() ;
        break;
      case Opcodes.I2C:
        ins =  tom.library.adt.bytecode.types.instruction.I2c.make() ;
        break;
      case Opcodes.I2S:
        ins =  tom.library.adt.bytecode.types.instruction.I2s.make() ;
        break;
      case Opcodes.LCMP:
        ins =  tom.library.adt.bytecode.types.instruction.Lcmp.make() ;
        break;
      case Opcodes.FCMPL:
        ins =  tom.library.adt.bytecode.types.instruction.Fcmpl.make() ;
        break;
      case Opcodes.FCMPG:
        ins =  tom.library.adt.bytecode.types.instruction.Fcmpg.make() ;
        break;
      case Opcodes.DCMPL:
        ins =  tom.library.adt.bytecode.types.instruction.Dcmpl.make() ;
        break;
      case Opcodes.DCMPG:
        ins =  tom.library.adt.bytecode.types.instruction.Dcmpg.make() ;
        break;
      case Opcodes.IRETURN:
        ins =  tom.library.adt.bytecode.types.instruction.Ireturn.make() ;
        break;
      case Opcodes.LRETURN:
        ins =  tom.library.adt.bytecode.types.instruction.Lreturn.make() ;
        break;
      case Opcodes.FRETURN:
        ins =  tom.library.adt.bytecode.types.instruction.Freturn.make() ;
        break;
      case Opcodes.DRETURN:
        ins =  tom.library.adt.bytecode.types.instruction.Dreturn.make() ;
        break;
      case Opcodes.ARETURN:
        ins =  tom.library.adt.bytecode.types.instruction.Areturn.make() ;
        break;
      case Opcodes.RETURN:
        ins =  tom.library.adt.bytecode.types.instruction.Return.make() ;
        break;
      case Opcodes.ARRAYLENGTH:
        ins =  tom.library.adt.bytecode.types.instruction.Arraylength.make() ;
        break;
      case Opcodes.ATHROW:
        ins =  tom.library.adt.bytecode.types.instruction.Athrow.make() ;
        break;
      case Opcodes.MONITORENTER:
        ins =  tom.library.adt.bytecode.types.instruction.Monitorenter.make() ;
        break;
      case Opcodes.MONITOREXIT:
        ins =  tom.library.adt.bytecode.types.instruction.Monitorexit.make() ;
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
        ins =  tom.library.adt.bytecode.types.instruction.Bipush.make(operand) ;
        break;
      case Opcodes.SIPUSH:
        ins =  tom.library.adt.bytecode.types.instruction.Sipush.make(operand) ;
        break;
      case Opcodes.NEWARRAY:
        ins =  tom.library.adt.bytecode.types.instruction.Newarray.make(operand) ;
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
        ins =  tom.library.adt.bytecode.types.instruction.Ifeq.make(l) ;
        break;
      case Opcodes.IFNE:
        ins =  tom.library.adt.bytecode.types.instruction.Ifne.make(l) ;
        break;
      case Opcodes.IFLT:
        ins =  tom.library.adt.bytecode.types.instruction.Iflt.make(l) ;
        break;
      case Opcodes.IFGE:
        ins =  tom.library.adt.bytecode.types.instruction.Ifge.make(l) ;
        break;
      case Opcodes.IFGT:
        ins =  tom.library.adt.bytecode.types.instruction.Ifgt.make(l) ;
        break;
      case Opcodes.IFLE:
        ins =  tom.library.adt.bytecode.types.instruction.Ifle.make(l) ;
        break;
      case Opcodes.IF_ICMPEQ:
        ins =  tom.library.adt.bytecode.types.instruction.If_icmpeq.make(l) ;
        break;
      case Opcodes.IF_ICMPNE:
        ins =  tom.library.adt.bytecode.types.instruction.If_icmpne.make(l) ;
        break;
      case Opcodes.IF_ICMPLT:
        ins =  tom.library.adt.bytecode.types.instruction.If_icmplt.make(l) ;
        break;
      case Opcodes.IF_ICMPGE:
        ins =  tom.library.adt.bytecode.types.instruction.If_icmpge.make(l) ;
        break;
      case Opcodes.IF_ICMPGT:
        ins =  tom.library.adt.bytecode.types.instruction.If_icmpgt.make(l) ;
        break;
      case Opcodes.IF_ICMPLE:
        ins =  tom.library.adt.bytecode.types.instruction.If_icmple.make(l) ;
        break;
      case Opcodes.IF_ACMPEQ:
        ins =  tom.library.adt.bytecode.types.instruction.If_acmpeq.make(l) ;
        break;
      case Opcodes.IF_ACMPNE:
        ins =  tom.library.adt.bytecode.types.instruction.If_acmpne.make(l) ;
        break;
      case Opcodes.GOTO:
        ins =  tom.library.adt.bytecode.types.instruction.Goto.make(l) ;
        break;
      case Opcodes.JSR:
        ins =  tom.library.adt.bytecode.types.instruction.Jsr.make(l) ;
        break;
      case Opcodes.IFNULL:
        ins =  tom.library.adt.bytecode.types.instruction.Ifnull.make(l) ;
        break;
      case Opcodes.IFNONNULL:
        ins =  tom.library.adt.bytecode.types.instruction.Ifnonnull.make(l) ;
        break;
      default:
          throw new RuntimeException("Unsupported OpCode :" + opcode);
    }

    appendInstruction(ins);
  }

  public void visitLabel(Label label) {
    appendInstruction( tom.library.adt.bytecode.types.instruction.Anchor.make(buildLabelNode(label)) );
  }

  public void visitLdcInsn(Object cst) {
    appendInstruction( tom.library.adt.bytecode.types.instruction.Ldc.make(ToolBox.buildValue(cst)) );
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
    appendLocalVariable( tom.library.adt.bytecode.types.localvariable.LocalVariable.make(name, desc,  tom.library.adt.bytecode.types.signature.Signature.make(signature) , buildLabelNode(start), buildLabelNode(end), index) );
  }

  public void visitLookupSwitchInsn(
      Label dflt,
      int[] keys,
      Label[] labels) {
    IntList kList =  tom.library.adt.bytecode.types.intlist.EmptyIntList.make() ;

    appendInstruction( tom.library.adt.bytecode.types.instruction.Lookupswitch.make(buildLabelNode(dflt), ToolBox.buildIntList(keys), buildLabelNodeList(labels)) );
  }

  public void visitMethodInsn(
      int opcode,
      String owner,
      String name,
      String desc) {
    Instruction ins = null;

    switch(opcode) {
      case Opcodes.INVOKEVIRTUAL:
        ins =  tom.library.adt.bytecode.types.instruction.Invokevirtual.make(owner, name, ToolBox.buildMethodDescriptor(desc)) ;
        break;
      case Opcodes.INVOKESPECIAL:
        ins =  tom.library.adt.bytecode.types.instruction.Invokespecial.make(owner, name, ToolBox.buildMethodDescriptor(desc)) ;
        break;
      case Opcodes.INVOKESTATIC:
        ins =  tom.library.adt.bytecode.types.instruction.Invokestatic.make(owner, name, ToolBox.buildMethodDescriptor(desc)) ;
        break;
      case Opcodes.INVOKEINTERFACE:
        ins =  tom.library.adt.bytecode.types.instruction.Invokeinterface.make(owner, name, ToolBox.buildMethodDescriptor(desc)) ;
        break;
      default:
        throw new RuntimeException("Unsupported OpCode :" + opcode);
    }

    appendInstruction(ins);
  }

  public void visitMultiANewArrayInsn(String desc, int dims) {
    appendInstruction( tom.library.adt.bytecode.types.instruction.Multianewarray.make(desc, dims) );
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
    appendInstruction( tom.library.adt.bytecode.types.instruction.Tableswitch.make(min, max, buildLabelNode(dflt), buildLabelNodeList(labels)) );
  }

  public void visitTryCatchBlock(
      Label start,
      Label end,
      Label handler,
      String type) {
    appendTryCatchBlock( tom.library.adt.bytecode.types.trycatchblock.TryCatchBlock.make(buildLabelNode(start), buildLabelNode(end),  tom.library.adt.bytecode.types.handler.CatchHandler.make(buildLabelNode(handler), type) ) );
  }

  public void visitTypeInsn(int opcode, String desc) {
    Instruction ins = null;

    switch(opcode) {
      case Opcodes.NEW:
        ins =  tom.library.adt.bytecode.types.instruction.New.make(desc) ;
        break;
      case Opcodes.ANEWARRAY:
        ins =  tom.library.adt.bytecode.types.instruction.Anewarray.make(desc) ;
        break;
      case Opcodes.CHECKCAST:
        ins =  tom.library.adt.bytecode.types.instruction.Checkcast.make(desc) ;
        break;
      case Opcodes.INSTANCEOF:
        ins =  tom.library.adt.bytecode.types.instruction.Instanceof.make(desc) ;
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
        ins =  tom.library.adt.bytecode.types.instruction.Iload.make(var) ;
        break;
      case Opcodes.LLOAD:
        ins =  tom.library.adt.bytecode.types.instruction.Lload.make(var) ;
        break;
      case Opcodes.FLOAD:
        ins =  tom.library.adt.bytecode.types.instruction.Fload.make(var) ;
        break;
      case Opcodes.DLOAD:
        ins =  tom.library.adt.bytecode.types.instruction.Dload.make(var) ;
        break;
      case Opcodes.ALOAD:
        ins =  tom.library.adt.bytecode.types.instruction.Aload.make(var) ;
        break;
      case Opcodes.ISTORE:
        ins =  tom.library.adt.bytecode.types.instruction.Istore.make(var) ;
        break;
      case Opcodes.LSTORE:
        ins =  tom.library.adt.bytecode.types.instruction.Lstore.make(var) ;
        break;
      case Opcodes.FSTORE:
        ins =  tom.library.adt.bytecode.types.instruction.Fstore.make(var) ;
        break;
      case Opcodes.DSTORE:
        ins =  tom.library.adt.bytecode.types.instruction.Dstore.make(var) ;
        break;
      case Opcodes.ASTORE:
        ins =  tom.library.adt.bytecode.types.instruction.Astore.make(var) ;
        break;
      case Opcodes.RET:
        ins =  tom.library.adt.bytecode.types.instruction.Ret.make(var) ;
        break;
      default:
        throw new RuntimeException("Unsupported OpCode :" + opcode);
    }

    appendInstruction(ins);
  }

  private LabelNode buildLabelNode(Label label) {
    LabelNode l = labelmap.get(label);
    if(l == null) {
      l =  tom.library.adt.bytecode.types.labelnode.LabelNode.make(labelCounter++) ;
      labelmap.put(label, l);
    }
    return l;
  }

  private LabelNodeList buildLabelNodeList(Label[] labels) {
    LabelNodeList labList =  tom.library.adt.bytecode.types.labelnodelist.EmptyLabelNodeList.make() ;
    if(labels != null) {
      for(int i = labels.length - 1; i >= 0; i--) {
        Label tmp=labels[i];
        labList =  tom.library.adt.bytecode.types.labelnodelist.ConsLabelNodeList.make(buildLabelNode(tmp), labList) ;
      }
    }
    return labList;
  }


}

