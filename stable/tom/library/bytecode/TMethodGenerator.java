/* Generated by TOM (version 2.6alpha): Do not edit this file *//*
 * Copyright (c) 2000-2008, INRIA
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
 *	- Neither the name of the INRIA nor the names of its
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

public class TMethodGenerator implements MethodVisitor {
  /* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file */  /* Generated by TOM (version 2.6alpha): Do not edit this file */ /* Generated by TOM (version 2.6alpha): Do not edit this file */ /* Generated by TOM (version 2.6alpha): Do not edit this file */ /* Generated by TOM (version 2.6alpha): Do not edit this file */    private static   tom.library.adt.bytecode.types.TLabelList  tom_append_list_LabelList( tom.library.adt.bytecode.types.TLabelList l1,  tom.library.adt.bytecode.types.TLabelList  l2) {     if( l1.isEmptyLabelList() ) {       return l2;     } else if( l2.isEmptyLabelList() ) {       return l1;     } else if(  l1.getTailLabelList() .isEmptyLabelList() ) {       return  tom.library.adt.bytecode.types.tlabellist.ConsLabelList.make( l1.getHeadLabelList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.tlabellist.ConsLabelList.make( l1.getHeadLabelList() ,tom_append_list_LabelList( l1.getTailLabelList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.TLabelList  tom_get_slice_LabelList( tom.library.adt.bytecode.types.TLabelList  begin,  tom.library.adt.bytecode.types.TLabelList  end, tom.library.adt.bytecode.types.TLabelList  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.library.adt.bytecode.types.tlabellist.ConsLabelList.make( begin.getHeadLabelList() ,( tom.library.adt.bytecode.types.TLabelList )tom_get_slice_LabelList( begin.getTailLabelList() ,end,tail)) ;     }   }      private static   tom.library.adt.bytecode.types.TTryCatchBlockList  tom_append_list_TryCatchBlockList( tom.library.adt.bytecode.types.TTryCatchBlockList l1,  tom.library.adt.bytecode.types.TTryCatchBlockList  l2) {     if( l1.isEmptyTryCatchBlockList() ) {       return l2;     } else if( l2.isEmptyTryCatchBlockList() ) {       return l1;     } else if(  l1.getTailTryCatchBlockList() .isEmptyTryCatchBlockList() ) {       return  tom.library.adt.bytecode.types.ttrycatchblocklist.ConsTryCatchBlockList.make( l1.getHeadTryCatchBlockList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.ttrycatchblocklist.ConsTryCatchBlockList.make( l1.getHeadTryCatchBlockList() ,tom_append_list_TryCatchBlockList( l1.getTailTryCatchBlockList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.TTryCatchBlockList  tom_get_slice_TryCatchBlockList( tom.library.adt.bytecode.types.TTryCatchBlockList  begin,  tom.library.adt.bytecode.types.TTryCatchBlockList  end, tom.library.adt.bytecode.types.TTryCatchBlockList  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.library.adt.bytecode.types.ttrycatchblocklist.ConsTryCatchBlockList.make( begin.getHeadTryCatchBlockList() ,( tom.library.adt.bytecode.types.TTryCatchBlockList )tom_get_slice_TryCatchBlockList( begin.getTailTryCatchBlockList() ,end,tail)) ;     }   }      private static   tom.library.adt.bytecode.types.TintList  tom_append_list_intList( tom.library.adt.bytecode.types.TintList l1,  tom.library.adt.bytecode.types.TintList  l2) {     if( l1.isEmptyintList() ) {       return l2;     } else if( l2.isEmptyintList() ) {       return l1;     } else if(  l1.getTailintList() .isEmptyintList() ) {       return  tom.library.adt.bytecode.types.tintlist.ConsintList.make( l1.getHeadintList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.tintlist.ConsintList.make( l1.getHeadintList() ,tom_append_list_intList( l1.getTailintList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.TintList  tom_get_slice_intList( tom.library.adt.bytecode.types.TintList  begin,  tom.library.adt.bytecode.types.TintList  end, tom.library.adt.bytecode.types.TintList  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.library.adt.bytecode.types.tintlist.ConsintList.make( begin.getHeadintList() ,( tom.library.adt.bytecode.types.TintList )tom_get_slice_intList( begin.getTailintList() ,end,tail)) ;     }   }      private static   tom.library.adt.bytecode.types.TInstructionList  tom_append_list_InstructionList( tom.library.adt.bytecode.types.TInstructionList l1,  tom.library.adt.bytecode.types.TInstructionList  l2) {     if( l1.isEmptyInstructionList() ) {       return l2;     } else if( l2.isEmptyInstructionList() ) {       return l1;     } else if(  l1.getTailInstructionList() .isEmptyInstructionList() ) {       return  tom.library.adt.bytecode.types.tinstructionlist.ConsInstructionList.make( l1.getHeadInstructionList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.tinstructionlist.ConsInstructionList.make( l1.getHeadInstructionList() ,tom_append_list_InstructionList( l1.getTailInstructionList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.TInstructionList  tom_get_slice_InstructionList( tom.library.adt.bytecode.types.TInstructionList  begin,  tom.library.adt.bytecode.types.TInstructionList  end, tom.library.adt.bytecode.types.TInstructionList  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.library.adt.bytecode.types.tinstructionlist.ConsInstructionList.make( begin.getHeadInstructionList() ,( tom.library.adt.bytecode.types.TInstructionList )tom_get_slice_InstructionList( begin.getTailInstructionList() ,end,tail)) ;     }   }      private static   tom.library.adt.bytecode.types.TLocalVariableList  tom_append_list_LocalVariableList( tom.library.adt.bytecode.types.TLocalVariableList l1,  tom.library.adt.bytecode.types.TLocalVariableList  l2) {     if( l1.isEmptyLocalVariableList() ) {       return l2;     } else if( l2.isEmptyLocalVariableList() ) {       return l1;     } else if(  l1.getTailLocalVariableList() .isEmptyLocalVariableList() ) {       return  tom.library.adt.bytecode.types.tlocalvariablelist.ConsLocalVariableList.make( l1.getHeadLocalVariableList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.tlocalvariablelist.ConsLocalVariableList.make( l1.getHeadLocalVariableList() ,tom_append_list_LocalVariableList( l1.getTailLocalVariableList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.TLocalVariableList  tom_get_slice_LocalVariableList( tom.library.adt.bytecode.types.TLocalVariableList  begin,  tom.library.adt.bytecode.types.TLocalVariableList  end, tom.library.adt.bytecode.types.TLocalVariableList  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.library.adt.bytecode.types.tlocalvariablelist.ConsLocalVariableList.make( begin.getHeadLocalVariableList() ,( tom.library.adt.bytecode.types.TLocalVariableList )tom_get_slice_LocalVariableList( begin.getTailLocalVariableList() ,end,tail)) ;     }   }    

  private BytecodeReader bytecodeReader;
  private TMethod method;
  private TInstructionList instructions;
  private TTryCatchBlockList tryCatchBlocks;
  private TLocalVariableList localVariables;

  private static int labelCounter = 0;
  private HashMap labelsMap = new HashMap();

  private TLabel buildTLabel(Label label) {
    Object o = labelsMap.get(label);
    TLabel l = null;
    if(o == null) {
      l =  tom.library.adt.bytecode.types.tlabel.Label.make(labelCounter++) ;
      labelsMap.put(label, l);
    } else
      l = (TLabel)o;

    return l;
  }

  private TLabelList buildTLabelList(Label[] labels) {
    TLabelList labList =  tom.library.adt.bytecode.types.tlabellist.EmptyLabelList.make() ;
    if(labels != null) {
      for(int i = labels.length - 1; i >= 0; i--) {
        labList =  tom.library.adt.bytecode.types.tlabellist.ConsLabelList.make(buildTLabel(labels[i]), labList) ;
      }
    }

    return labList;
  }

  public TMethodGenerator (
      BytecodeReader cg,
      TAccessList access,
      String name,
      TMethodDescriptor desc,
      TSignature signature,
      TStringList exceptions) {
    bytecodeReader = cg;
    method =  tom.library.adt.bytecode.types.tmethod.Method.make( tom.library.adt.bytecode.types.tmethodinfo.MethodInfo.make(bytecodeReader.getTClass().getinfo().getname(), access, name, desc, signature, exceptions) ,  tom.library.adt.bytecode.types.tmethodcode.EmptyCode.make() ) ;
  }

  private void appendInstruction(TInstruction ins) {
    instructions = tom_append_list_InstructionList(instructions, tom.library.adt.bytecode.types.tinstructionlist.ConsInstructionList.make(ins, tom.library.adt.bytecode.types.tinstructionlist.EmptyInstructionList.make() ) );
  }

  private void appendTryCatchBlock(TTryCatchBlock tcb) {
    tryCatchBlocks = tom_append_list_TryCatchBlockList(tryCatchBlocks, tom.library.adt.bytecode.types.ttrycatchblocklist.ConsTryCatchBlockList.make(tcb, tom.library.adt.bytecode.types.ttrycatchblocklist.EmptyTryCatchBlockList.make() ) );
  }
  private void appendLocalVariable(TLocalVariable lv) {
    localVariables = tom_append_list_LocalVariableList(localVariables, tom.library.adt.bytecode.types.tlocalvariablelist.ConsLocalVariableList.make(lv, tom.library.adt.bytecode.types.tlocalvariablelist.EmptyLocalVariableList.make() ) );
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
    instructions =  tom.library.adt.bytecode.types.tinstructionlist.EmptyInstructionList.make() ;
    tryCatchBlocks =  tom.library.adt.bytecode.types.ttrycatchblocklist.EmptyTryCatchBlockList.make() ;
    localVariables =  tom.library.adt.bytecode.types.tlocalvariablelist.EmptyLocalVariableList.make() ;
  }

  public void visitEnd() {
    method =  tom.library.adt.bytecode.types.tmethod.Method.make(method.getinfo(),  tom.library.adt.bytecode.types.tmethodcode.MethodCode.make(instructions, localVariables, tryCatchBlocks) ) ;

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
    TInstruction ins = null;
    switch(opcode) {
      case Opcodes.GETSTATIC:
        ins =  tom.library.adt.bytecode.types.tinstruction.Getstatic.make(owner, name, ToolBox.buildTFieldDescriptor(desc)) ;
        break;
      case Opcodes.PUTSTATIC:
        ins =  tom.library.adt.bytecode.types.tinstruction.Putstatic.make(owner, name, ToolBox.buildTFieldDescriptor(desc)) ;
        break;
      case Opcodes.GETFIELD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Getfield.make(owner, name, ToolBox.buildTFieldDescriptor(desc)) ;
        break;
      case Opcodes.PUTFIELD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Putfield.make(owner, name, ToolBox.buildTFieldDescriptor(desc)) ;
        break;
      default:
        System.err.println("Unsupported OpCode :" + opcode);
        System.exit(-1);
    }

    appendInstruction(ins);
  }

  public void visitIincInsn(int var, int increment) {
    appendInstruction( tom.library.adt.bytecode.types.tinstruction.Iinc.make(increment, var) );
  }

  public void visitInsn(int opcode) {
    TInstruction ins = null;
    switch(opcode) {
      case Opcodes.NOP:
        ins =  tom.library.adt.bytecode.types.tinstruction.Nop.make() ;
        break;
      case Opcodes.ACONST_NULL:
        ins =  tom.library.adt.bytecode.types.tinstruction.Aconst_null.make() ;
        break;
      case Opcodes.ICONST_M1:
        ins =  tom.library.adt.bytecode.types.tinstruction.Iconst_m1.make() ;
        break;
      case Opcodes.ICONST_0:
        ins =  tom.library.adt.bytecode.types.tinstruction.Iconst_0.make() ;
        break;
      case Opcodes.ICONST_1:
        ins =  tom.library.adt.bytecode.types.tinstruction.Iconst_1.make() ;
        break;
      case Opcodes.ICONST_2:
        ins =  tom.library.adt.bytecode.types.tinstruction.Iconst_2.make() ;
        break;
      case Opcodes.ICONST_3:
        ins =  tom.library.adt.bytecode.types.tinstruction.Iconst_3.make() ;
        break;
      case Opcodes.ICONST_4:
        ins =  tom.library.adt.bytecode.types.tinstruction.Iconst_4.make() ;
        break;
      case Opcodes.ICONST_5:
        ins =  tom.library.adt.bytecode.types.tinstruction.Iconst_5.make() ;
        break;
      case Opcodes.LCONST_0:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lconst_0.make() ;
        break;
      case Opcodes.LCONST_1:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lconst_1.make() ;
        break;
      case Opcodes.FCONST_0:
        ins =  tom.library.adt.bytecode.types.tinstruction.Fconst_0.make() ;
        break;
      case Opcodes.FCONST_1:
        ins =  tom.library.adt.bytecode.types.tinstruction.Fconst_1.make() ;
        break;
      case Opcodes.FCONST_2:
        ins =  tom.library.adt.bytecode.types.tinstruction.Fconst_2.make() ;
        break;
      case Opcodes.DCONST_0:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dconst_0.make() ;
        break;
      case Opcodes.DCONST_1:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dconst_1.make() ;
        break;
      case Opcodes.IALOAD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Iaload.make() ;
        break;
      case Opcodes.LALOAD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Laload.make() ;
        break;
      case Opcodes.FALOAD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Faload.make() ;
        break;
      case Opcodes.DALOAD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Daload.make() ;
        break;
      case Opcodes.AALOAD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Aaload.make() ;
        break;
      case Opcodes.BALOAD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Baload.make() ;
        break;
      case Opcodes.CALOAD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Caload.make() ;
        break;
      case Opcodes.SALOAD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Saload.make() ;
        break;
      case Opcodes.IASTORE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Iastore.make() ;
        break;
      case Opcodes.LASTORE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lastore.make() ;
        break;
      case Opcodes.FASTORE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Fastore.make() ;
        break;
      case Opcodes.DASTORE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dastore.make() ;
        break;
      case Opcodes.AASTORE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Aastore.make() ;
        break;
      case Opcodes.BASTORE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Bastore.make() ;
        break;
      case Opcodes.CASTORE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Castore.make() ;
        break;
      case Opcodes.SASTORE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Sastore.make() ;
        break;
      case Opcodes.POP:
        ins =  tom.library.adt.bytecode.types.tinstruction.Pop.make() ;
        break;
      case Opcodes.POP2:
        ins =  tom.library.adt.bytecode.types.tinstruction.Pop2.make() ;
        break;
      case Opcodes.DUP:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dup.make() ;
        break;
      case Opcodes.DUP_X1:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dup_x1.make() ;
        break;
      case Opcodes.DUP_X2:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dup_x2.make() ;
        break;
      case Opcodes.DUP2:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dup2.make() ;
        break;
      case Opcodes.DUP2_X1:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dup2_x1.make() ;
        break;
      case Opcodes.DUP2_X2:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dup2_x2.make() ;
        break;
      case Opcodes.SWAP:
        ins =  tom.library.adt.bytecode.types.tinstruction.Swap.make() ;
        break;
      case Opcodes.IADD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Iadd.make() ;
        break;
      case Opcodes.LADD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ladd.make() ;
        break;
      case Opcodes.FADD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Fadd.make() ;
        break;
      case Opcodes.DADD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dadd.make() ;
        break;
      case Opcodes.ISUB:
        ins =  tom.library.adt.bytecode.types.tinstruction.Isub.make() ;
        break;
      case Opcodes.LSUB:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lsub.make() ;
        break;
      case Opcodes.FSUB:
        ins =  tom.library.adt.bytecode.types.tinstruction.Fsub.make() ;
        break;
      case Opcodes.DSUB:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dsub.make() ;
        break;
      case Opcodes.IMUL:
        ins =  tom.library.adt.bytecode.types.tinstruction.Imul.make() ;
        break;
      case Opcodes.LMUL:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lmul.make() ;
        break;
      case Opcodes.FMUL:
        ins =  tom.library.adt.bytecode.types.tinstruction.Fmul.make() ;
        break;
      case Opcodes.DMUL:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dmul.make() ;
        break;
      case Opcodes.IDIV:
        ins =  tom.library.adt.bytecode.types.tinstruction.Idiv.make() ;
        break;
      case Opcodes.LDIV:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ldiv.make() ;
        break;
      case Opcodes.FDIV:
        ins =  tom.library.adt.bytecode.types.tinstruction.Fdiv.make() ;
        break;
      case Opcodes.DDIV:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ddiv.make() ;
        break;
      case Opcodes.IREM:
        ins =  tom.library.adt.bytecode.types.tinstruction.Irem.make() ;
        break;
      case Opcodes.LREM:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lrem.make() ;
        break;
      case Opcodes.FREM:
        ins =  tom.library.adt.bytecode.types.tinstruction.Frem.make() ;
        break;
      case Opcodes.DREM:
        ins =  tom.library.adt.bytecode.types.tinstruction.Drem.make() ;
        break;
      case Opcodes.INEG:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ineg.make() ;
        break;
      case Opcodes.LNEG:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lneg.make() ;
        break;
      case Opcodes.FNEG:
        ins =  tom.library.adt.bytecode.types.tinstruction.Fneg.make() ;
        break;
      case Opcodes.DNEG:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dneg.make() ;
        break;
      case Opcodes.ISHL:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ishl.make() ;
        break;
      case Opcodes.LSHL:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lshl.make() ;
        break;
      case Opcodes.ISHR:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ishr.make() ;
        break;
      case Opcodes.LSHR:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lshr.make() ;
        break;
      case Opcodes.IUSHR:
        ins =  tom.library.adt.bytecode.types.tinstruction.Iushr.make() ;
        break;
      case Opcodes.LUSHR:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lushr.make() ;
        break;
      case Opcodes.IAND:
        ins =  tom.library.adt.bytecode.types.tinstruction.Iand.make() ;
        break;
      case Opcodes.LAND:
        ins =  tom.library.adt.bytecode.types.tinstruction.Land.make() ;
        break;
      case Opcodes.IOR:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ior.make() ;
        break;
      case Opcodes.LOR:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lor.make() ;
        break;
      case Opcodes.IXOR:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ixor.make() ;
        break;
      case Opcodes.LXOR:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lxor.make() ;
        break;
      case Opcodes.I2L:
        ins =  tom.library.adt.bytecode.types.tinstruction.I2l.make() ;
        break;
      case Opcodes.I2F:
        ins =  tom.library.adt.bytecode.types.tinstruction.I2f.make() ;
        break;
      case Opcodes.I2D:
        ins =  tom.library.adt.bytecode.types.tinstruction.I2d.make() ;
        break;
      case Opcodes.L2I:
        ins =  tom.library.adt.bytecode.types.tinstruction.L2i.make() ;
        break;
      case Opcodes.L2F:
        ins =  tom.library.adt.bytecode.types.tinstruction.L2f.make() ;
        break;
      case Opcodes.L2D:
        ins =  tom.library.adt.bytecode.types.tinstruction.L2d.make() ;
        break;
      case Opcodes.F2I:
        ins =  tom.library.adt.bytecode.types.tinstruction.F2i.make() ;
        break;
      case Opcodes.F2L:
        ins =  tom.library.adt.bytecode.types.tinstruction.F2l.make() ;
        break;
      case Opcodes.F2D:
        ins =  tom.library.adt.bytecode.types.tinstruction.F2d.make() ;
        break;
      case Opcodes.D2I:
        ins =  tom.library.adt.bytecode.types.tinstruction.D2i.make() ;
        break;
      case Opcodes.D2L:
        ins =  tom.library.adt.bytecode.types.tinstruction.D2l.make() ;
        break;
      case Opcodes.D2F:
        ins =  tom.library.adt.bytecode.types.tinstruction.D2f.make() ;
        break;
      case Opcodes.I2B:
        ins =  tom.library.adt.bytecode.types.tinstruction.I2b.make() ;
        break;
      case Opcodes.I2C:
        ins =  tom.library.adt.bytecode.types.tinstruction.I2c.make() ;
        break;
      case Opcodes.I2S:
        ins =  tom.library.adt.bytecode.types.tinstruction.I2s.make() ;
        break;
      case Opcodes.LCMP:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lcmp.make() ;
        break;
      case Opcodes.FCMPL:
        ins =  tom.library.adt.bytecode.types.tinstruction.Fcmpl.make() ;
        break;
      case Opcodes.FCMPG:
        ins =  tom.library.adt.bytecode.types.tinstruction.Fcmpg.make() ;
        break;
      case Opcodes.DCMPL:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dcmpl.make() ;
        break;
      case Opcodes.DCMPG:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dcmpg.make() ;
        break;
      case Opcodes.IRETURN:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ireturn.make() ;
        break;
      case Opcodes.LRETURN:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lreturn.make() ;
        break;
      case Opcodes.FRETURN:
        ins =  tom.library.adt.bytecode.types.tinstruction.Freturn.make() ;
        break;
      case Opcodes.DRETURN:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dreturn.make() ;
        break;
      case Opcodes.ARETURN:
        ins =  tom.library.adt.bytecode.types.tinstruction.Areturn.make() ;
        break;
      case Opcodes.RETURN:
        ins =  tom.library.adt.bytecode.types.tinstruction.Return.make() ;
        break;
      case Opcodes.ARRAYLENGTH:
        ins =  tom.library.adt.bytecode.types.tinstruction.Arraylength.make() ;
        break;
      case Opcodes.ATHROW:
        ins =  tom.library.adt.bytecode.types.tinstruction.Athrow.make() ;
        break;
      case Opcodes.MONITORENTER:
        ins =  tom.library.adt.bytecode.types.tinstruction.Monitorenter.make() ;
        break;
      case Opcodes.MONITOREXIT:
        ins =  tom.library.adt.bytecode.types.tinstruction.Monitorexit.make() ;
        break;
      default:
        System.err.println("Unsupported OpCode :" + opcode);
        System.exit(-1);
    }

    appendInstruction(ins);
  }

  public void visitIntInsn(int opcode, int operand) {
    TInstruction ins = null;

    switch(opcode) {
      case Opcodes.BIPUSH:
        ins =  tom.library.adt.bytecode.types.tinstruction.Bipush.make(operand) ;
        break;
      case Opcodes.SIPUSH:
        ins =  tom.library.adt.bytecode.types.tinstruction.Sipush.make(operand) ;
        break;
      case Opcodes.NEWARRAY:
        ins =  tom.library.adt.bytecode.types.tinstruction.Newarray.make(operand) ;
        break;
      default:
        System.err.println("Unsupported OpCode :" + opcode);
        System.exit(-1);
    }

    appendInstruction(ins);
  }


  public void visitJumpInsn(int opcode, Label label) {
    TInstruction ins = null;

    TLabel l = buildTLabel(label);

    switch(opcode) {
      case Opcodes.IFEQ:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ifeq.make(l) ;
        break;
      case Opcodes.IFNE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ifne.make(l) ;
        break;
      case Opcodes.IFLT:
        ins =  tom.library.adt.bytecode.types.tinstruction.Iflt.make(l) ;
        break;
      case Opcodes.IFGE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ifge.make(l) ;
        break;
      case Opcodes.IFGT:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ifgt.make(l) ;
        break;
      case Opcodes.IFLE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ifle.make(l) ;
        break;
      case Opcodes.IF_ICMPEQ:
        ins =  tom.library.adt.bytecode.types.tinstruction.If_icmpeq.make(l) ;
        break;
      case Opcodes.IF_ICMPNE:
        ins =  tom.library.adt.bytecode.types.tinstruction.If_icmpne.make(l) ;
        break;
      case Opcodes.IF_ICMPLT:
        ins =  tom.library.adt.bytecode.types.tinstruction.If_icmplt.make(l) ;
        break;
      case Opcodes.IF_ICMPGE:
        ins =  tom.library.adt.bytecode.types.tinstruction.If_icmpge.make(l) ;
        break;
      case Opcodes.IF_ICMPGT:
        ins =  tom.library.adt.bytecode.types.tinstruction.If_icmpgt.make(l) ;
        break;
      case Opcodes.IF_ICMPLE:
        ins =  tom.library.adt.bytecode.types.tinstruction.If_icmple.make(l) ;
        break;
      case Opcodes.IF_ACMPEQ:
        ins =  tom.library.adt.bytecode.types.tinstruction.If_acmpeq.make(l) ;
        break;
      case Opcodes.IF_ACMPNE:
        ins =  tom.library.adt.bytecode.types.tinstruction.If_acmpne.make(l) ;
        break;
      case Opcodes.GOTO:
        ins =  tom.library.adt.bytecode.types.tinstruction.Goto.make(l) ;
        break;
      case Opcodes.JSR:
        ins =  tom.library.adt.bytecode.types.tinstruction.Jsr.make(l) ;
        break;
      case Opcodes.IFNULL:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ifnull.make(l) ;
        break;
      case Opcodes.IFNONNULL:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ifnonnull.make(l) ;
        break;
      default:
          System.err.println("Unsupported OpCode :" + opcode);
          System.exit(-1);
    }

    appendInstruction(ins);
  }

  public void visitLabel(Label label) {
    appendInstruction( tom.library.adt.bytecode.types.tinstruction.Anchor.make(buildTLabel(label)) );
  }

  public void visitLdcInsn(Object cst) {
    appendInstruction( tom.library.adt.bytecode.types.tinstruction.Ldc.make(ToolBox.buildTValue(cst)) );
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
    appendLocalVariable( tom.library.adt.bytecode.types.tlocalvariable.LocalVariable.make(name, desc,  tom.library.adt.bytecode.types.tsignature.Signature.make(signature) , buildTLabel(start), buildTLabel(end), index) );
  }

  public void visitLookupSwitchInsn(
      Label dflt,
      int[] keys,
      Label[] labels) {
    TintList kList =  tom.library.adt.bytecode.types.tintlist.EmptyintList.make() ;

    appendInstruction( tom.library.adt.bytecode.types.tinstruction.Lookupswitch.make(buildTLabel(dflt), ToolBox.buildTintList(keys), buildTLabelList(labels)) );
  }

  public void visitMethodInsn(
      int opcode,
      String owner,
      String name,
      String desc) {
    TInstruction ins = null;

    switch(opcode) {
      case Opcodes.INVOKEVIRTUAL:
        ins =  tom.library.adt.bytecode.types.tinstruction.Invokevirtual.make(owner, name, ToolBox.buildTMethodDescriptor(desc)) ;
        break;
      case Opcodes.INVOKESPECIAL:
        ins =  tom.library.adt.bytecode.types.tinstruction.Invokespecial.make(owner, name, ToolBox.buildTMethodDescriptor(desc)) ;
        break;
      case Opcodes.INVOKESTATIC:
        ins =  tom.library.adt.bytecode.types.tinstruction.Invokestatic.make(owner, name, ToolBox.buildTMethodDescriptor(desc)) ;
        break;
      case Opcodes.INVOKEINTERFACE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Invokeinterface.make(owner, name, ToolBox.buildTMethodDescriptor(desc)) ;
        break;
      default:
        System.err.println("Unsupported OpCode :" + opcode);
        System.exit(-1);
    }

    appendInstruction(ins);
  }

  public void visitMultiANewArrayInsn(String desc, int dims) {
    appendInstruction( tom.library.adt.bytecode.types.tinstruction.Multianewarray.make(desc, dims) );
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
    appendInstruction( tom.library.adt.bytecode.types.tinstruction.Tableswitch.make(min, max, buildTLabel(dflt), buildTLabelList(labels)) );
  }

  public void visitTryCatchBlock(
      Label start,
      Label end,
      Label handler,
      String type) {
    appendTryCatchBlock( tom.library.adt.bytecode.types.ttrycatchblock.TryCatchBlock.make(buildTLabel(start), buildTLabel(end),  tom.library.adt.bytecode.types.thandler.CatchHandler.make(buildTLabel(handler), type) ) );
  }

  public void visitTypeInsn(int opcode, String desc) {
    TInstruction ins = null;

    switch(opcode) {
      case Opcodes.NEW:
        ins =  tom.library.adt.bytecode.types.tinstruction.New.make(desc) ;
        break;
      case Opcodes.ANEWARRAY:
        ins =  tom.library.adt.bytecode.types.tinstruction.Anewarray.make(desc) ;
        break;
      case Opcodes.CHECKCAST:
        ins =  tom.library.adt.bytecode.types.tinstruction.Checkcast.make(desc) ;
        break;
      case Opcodes.INSTANCEOF:
        ins =  tom.library.adt.bytecode.types.tinstruction.Instanceof.make(desc) ;
        break;
      default:
        System.err.println("Unsupported OpCode :" + opcode);
        System.exit(-1);
    }

    appendInstruction(ins);
  }

  public void visitVarInsn(int opcode, int var) {
    TInstruction ins = null;

    switch(opcode) {
      case Opcodes.ILOAD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Iload.make(var) ;
        break;
      case Opcodes.LLOAD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lload.make(var) ;
        break;
      case Opcodes.FLOAD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Fload.make(var) ;
        break;
      case Opcodes.DLOAD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dload.make(var) ;
        break;
      case Opcodes.ALOAD:
        ins =  tom.library.adt.bytecode.types.tinstruction.Aload.make(var) ;
        break;
      case Opcodes.ISTORE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Istore.make(var) ;
        break;
      case Opcodes.LSTORE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Lstore.make(var) ;
        break;
      case Opcodes.FSTORE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Fstore.make(var) ;
        break;
      case Opcodes.DSTORE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Dstore.make(var) ;
        break;
      case Opcodes.ASTORE:
        ins =  tom.library.adt.bytecode.types.tinstruction.Astore.make(var) ;
        break;
      case Opcodes.RET:
        ins =  tom.library.adt.bytecode.types.tinstruction.Ret.make(var) ;
        break;
      default:
        System.err.println("Unsupported OpCode :" + opcode);
        System.exit(-1);
    }

    appendInstruction(ins);
  }
}

