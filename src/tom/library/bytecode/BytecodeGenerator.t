/*
 * Copyright (c) 2000-2007, INRIA
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
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;

import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;
import tom.library.adt.bytecode.types.tstringlist.*;
import tom.library.adt.bytecode.types.tlabellist.*;
import tom.library.adt.bytecode.types.tintlist.*;

import java.util.HashMap;

public class BytecodeGenerator extends ToolBox implements Opcodes {
  %include { adt/bytecode/Bytecode.tom }

  public byte[] toBytecode(TClass clazz){

    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

    %match(TClass clazz){

      Class(ClassInfo(name,signature,access,superName,interfaces,innerClasses,outerClass), fields, methods) -> {

        // bytecode for the header

        cw.visit(V1_1, buildAccessValue(`access), `name, buildSignature(`signature),`superName, ((StringList)`interfaces).toArray());


        //bytecode for the inner classes

        %match(TInnerClassInfoList innerClasses){
          InnerClassInfoList(_*,innerClass,_*)->{
            %match(TInnerClassInfo innerClass){
              InnerClassInfo(innerName,innerOuterName,innerInnerName,innerAccess)->{
                cw.visitInnerClass(`innerName,`innerOuterName,`innerInnerName,buildAccessValue(`innerAccess));
              }
            }
          }
        }

        //bytecode for the outer class

        %match(TOuterClassInfo outerClass){
          OuterClassInfo(outerOwner,outerName,outerDesc)->{
            cw.visitOuterClass(`outerOwner,`outerName,buildDescriptor(`outerDesc));
          }
        }

        //bytecode for the fields 

        %match(TFieldList fields){

          FieldList(_*,field,_*) ->{

            %match(TField field){
             Field(fieldAccess,fieldName, fieldDesc, fieldSignature, fieldValue)->{
               FieldVisitor fw = cw.visitField(buildAccessValue(`fieldAccess),`fieldName,buildDescriptor(`fieldDesc),buildSignature(`fieldSignature),buildConstant(`fieldValue));
               // we do not visit the annotations and attributes
               fw.visitEnd(); 
             }
            }
          }
        }
        //bytecode for the methods

        MethodVisitor mw;
        %match(TMethodList methods){
          MethodList(_*,method,_*) ->{
            %match(TMethod method){
              Method(MethodInfo(owner,methAccess,methName,desc,methSignature,exceptions),MethodCode(code,localVariables,tryCatchBlockLists)) -> {
                mw = cw.visitMethod(buildAccessValue(`methAccess),
                    `methName,
                    buildDescriptor(`desc),
                    buildSignature(`methSignature),
                    ((StringList)`exceptions).toArray());

                mw.visitCode();

                HashMap labelMap = new HashMap();
                //bytecode for the method code 
                %match(TInstructionList code){
                  InstructionList(_*,Anchor(label),_*)->{
                    labelMap.put(`label,new Label());
                  }
                  InstructionList(_*,inst,_*)->{
                    %match(TInstruction inst) {
                      Anchor(label) -> {
                        mw.visitLabel((Label)labelMap.get(`label));
                      }
                      i@_ -> {addInstruction(mw,`i,labelMap);}
                    }
                  }
                }

                %match(TTryCatchBlockList tryCatchBlockLists) {
                  TryCatchBlockList(_*, TryCatchBlock(start,end,handler), _*) -> {
                    %match(THandler handler) {
                      CatchHandler(h, type) -> {
                        mw.visitTryCatchBlock((Label)labelMap.get(`start),(Label)labelMap.get(`end),(Label)labelMap.get(`h),`type);
                      }
                      FinallyHandler(h) -> {
                        mw.visitTryCatchBlock((Label)labelMap.get(`start),(Label)labelMap.get(`end),(Label)labelMap.get(`h),null);
                      }
                    }
                  }
                }

                %match(TLocalVariableList localVariables) {
                  LocalVariableList(_*, LocalVariable(varname, vardesc, varsignature, start, end, index), _*) -> {
                    mw.visitLocalVariable(`varname, `vardesc, buildSignature(`varsignature), (Label)labelMap.get(`start), (Label)labelMap.get(`end), `index);
                  }
                }

                mw.visitMaxs(0, 0);
                mw.visitEnd();
              }
            }
          }
        }
      }
    }


    // gets the bytecode
    return cw.toByteArray();
  }

  public void addInstruction(MethodVisitor mw,TInstruction inst,HashMap labelMap){
    %match(TInstruction inst){
      Nop()->{
        mw.visitInsn(NOP);
      }
      Aconst_null() ->{
        mw.visitInsn(ACONST_NULL);
      }
      Iconst_m1()->{
        mw.visitInsn(ICONST_M1);
      }
      Iconst_0()->{
        mw.visitInsn(ICONST_0);
      }
      Iconst_1()->{
        mw.visitInsn(ICONST_1);
      }
      Iconst_2()->{
        mw.visitInsn(ICONST_2);
      }
      Iconst_3()->{
        mw.visitInsn(ICONST_3);
      }
      Iconst_4()->{
        mw.visitInsn(ICONST_4);
      }
      Iconst_5()->{
        mw.visitInsn(ICONST_5);
      }
      Lconst_0()->{
        mw.visitInsn(LCONST_0);
      }
      Lconst_1()->{
        mw.visitInsn(LCONST_1);
      }
      Fconst_0()->{
        mw.visitInsn(FCONST_0);
      }
      Fconst_1()->{
        mw.visitInsn(FCONST_1);
      }
      Fconst_2()->{
        mw.visitInsn(FCONST_2);
      }
      Dconst_0()->{
        mw.visitInsn(DCONST_0);
      }
      Dconst_1()->{
        mw.visitInsn(DCONST_1);
      }
      Iaload()->{
        mw.visitInsn(IALOAD);
      }
      Laload()->{
        mw.visitInsn(LALOAD);
      }
      Faload()->{
        mw.visitInsn(FALOAD);
      }
      Daload()->{
        mw.visitInsn(DALOAD);
      }
      Aaload()->{
        mw.visitInsn(AALOAD);
      }
      Baload()->{
        mw.visitInsn(BALOAD);
      }
      Caload()->{
        mw.visitInsn(CALOAD);
      }
      Saload()->{
        mw.visitInsn(SALOAD);
      }
      Iastore()->{
        mw.visitInsn(IASTORE);
      }
      Lastore()->{
        mw.visitInsn(LASTORE);
      }
      Fastore()->{
        mw.visitInsn(FASTORE);
      }
      Dastore()->{
        mw.visitInsn(DASTORE);
      }
      Aastore()->{
        mw.visitInsn(AASTORE);
      }
      Bastore()->{
        mw.visitInsn(BASTORE);
      }
      Castore()->{
        mw.visitInsn(CASTORE);
      }
      Sastore()->{
        mw.visitInsn(SASTORE);
      }
      Pop()->{
        mw.visitInsn(POP);
      }
      Pop2()->{
        mw.visitInsn(POP2);
      }
      Dup()->{
        mw.visitInsn(DUP);
      }
      Dup_x1()->{
        mw.visitInsn(DUP_X1);
      }
      Dup_x2()->{
        mw.visitInsn(DUP_X2);
      }
      Dup2()->{
        mw.visitInsn(DUP2);
      }
      Dup2_x1()->{
        mw.visitInsn(DUP2_X1);
      }
      Dup2_x2()->{
        mw.visitInsn(DUP2_X2);
      }
      Swap()->{
        mw.visitInsn(SWAP);
      }
      Iadd()->{
        mw.visitInsn(IADD);
      }
      Ladd()->{
        mw.visitInsn(LADD);
      }
      Fadd()->{
        mw.visitInsn(FADD);
      }
      Dadd()->{
        mw.visitInsn(DADD);
      }
      Isub()->{
        mw.visitInsn(ISUB);
      }
      Lsub()->{
        mw.visitInsn(LSUB);
      }
      Fsub()->{
        mw.visitInsn(FSUB);
      }
      Dsub()->{
        mw.visitInsn(DSUB);
      }
      Imul()->{
        mw.visitInsn(IMUL);
      }
      Lmul()->{
        mw.visitInsn(LMUL);
      }
      Fmul()->{
        mw.visitInsn(FMUL);
      }
      Dmul()->{
        mw.visitInsn(DMUL);
      }
      Idiv()->{
        mw.visitInsn(IDIV);
      }
      Ldiv()->{
        mw.visitInsn(LDIV);
      }
      Fdiv()->{
        mw.visitInsn(FDIV);
      }
      Ddiv()->{
        mw.visitInsn(DDIV);
      }
      Irem()->{
        mw.visitInsn(IREM);
      }
      Lrem()->{
        mw.visitInsn(LREM);
      }
      Frem()->{
        mw.visitInsn(FREM);
      }
      Drem()->{
        mw.visitInsn(DREM);
      }
      Ineg()->{
        mw.visitInsn(INEG);
      }
      Lneg()->{
        mw.visitInsn(LNEG);
      }
      Fneg()->{
        mw.visitInsn(FNEG);
      }
      Dneg()->{
        mw.visitInsn(DNEG);
      }
      Ishl()->{
        mw.visitInsn(ISHL);
      }
      Lshl()->{
        mw.visitInsn(LSHL);
      }
      Ishr()->{
        mw.visitInsn(ISHR);
      }
      Lshr()->{
        mw.visitInsn(LSHR);
      }
      Iushr()->{
        mw.visitInsn(IUSHR);
      }
      Lushr()->{
        mw.visitInsn(LUSHR);
      }
      Iand()->{
        mw.visitInsn(IAND);
      }
      Land()->{
        mw.visitInsn(LAND);
      }
      Ior()->{
        mw.visitInsn(IOR);
      }
      Lor()->{
        mw.visitInsn(LOR);
      }
      Ixor()->{
        mw.visitInsn(IXOR);
      }
      Lxor()->{
        mw.visitInsn(LXOR);
      }
      I2l()->{
        mw.visitInsn(I2L);
      }
      I2f()->{
        mw.visitInsn(I2F);
      }
      I2d()->{
        mw.visitInsn(I2D);
      }
      L2i()->{
        mw.visitInsn(L2I);
      }
      L2f()->{
        mw.visitInsn(L2F);
      }
      L2d()->{
        mw.visitInsn(L2D);
      }
      F2i()->{
        mw.visitInsn(F2I);
      }
      F2l()->{
        mw.visitInsn(F2L);
      }
      F2d()->{
        mw.visitInsn(F2D);
      }
      D2i()->{
        mw.visitInsn(D2I);
      }
      D2l()->{
        mw.visitInsn(D2L);
      }
      D2f()->{
        mw.visitInsn(D2F);
      }
      I2b()->{
        mw.visitInsn(I2B);
      }
      I2c()->{
        mw.visitInsn(I2C);
      }
      I2s()->{
        mw.visitInsn(I2S);
      }
      Lcmp()->{
        mw.visitInsn(LCMP);
      }
      Fcmpl()->{
        mw.visitInsn(FCMPL);
      }
      Fcmpg()->{
        mw.visitInsn(FCMPG);
      }
      Dcmpl()->{
        mw.visitInsn(DCMPL);
      }
      Dcmpg()->{
        mw.visitInsn(DCMPG);
      }
      Ireturn()->{
        mw.visitInsn(IRETURN);
      }
      Lreturn()->{
        mw.visitInsn(LRETURN);
      }
      Freturn()->{
        mw.visitInsn(FRETURN);
      }
      Dreturn()->{
        mw.visitInsn(DRETURN);
      }
      Areturn()->{
        mw.visitInsn(ARETURN);
      }
      Return()->{
        mw.visitInsn(RETURN);
      }
      Arraylength()->{
        mw.visitInsn(ARRAYLENGTH);
      }
      Athrow()->{
        mw.visitInsn(ATHROW);
      }
      Monitorenter()->{
        mw.visitInsn(MONITORENTER);
      }
      Monitorexit()->{
        mw.visitInsn(MONITOREXIT);
      }
      Getstatic(owner, name, desc)->{
        mw.visitFieldInsn(GETSTATIC,`owner,`name,buildDescriptor(`desc));
      }
      Putstatic(owner, name, desc)->{
        mw.visitFieldInsn(PUTSTATIC,`owner,`name,buildDescriptor(`desc));
      }
      Getfield(owner, name, desc)->{
        mw.visitFieldInsn(GETFIELD,`owner,`name,buildDescriptor(`desc));
      }
      Putfield(owner, name, desc)->{
        mw.visitFieldInsn(PUTFIELD,`owner,`name,buildDescriptor(`desc));
      }
      Bipush(operand)->{
        mw.visitIntInsn(BIPUSH,`operand);
      }
      Sipush(operand)->{
        mw.visitIntInsn(SIPUSH,`operand);
      }
      Newarray(operand)->{
        mw.visitIntInsn(NEWARRAY,`operand);
      }
      Ifeq(l)->{
        mw.visitJumpInsn(IFEQ,(Label)labelMap.get(`l));
      }
      Ifne(l)->{
        mw.visitJumpInsn(IFNE,(Label)labelMap.get(`l));
      }
      Iflt(l)->{
        mw.visitJumpInsn(IFLT,(Label)labelMap.get(`l));
      }
      Ifge(l)->{
        mw.visitJumpInsn(IFGE,(Label)labelMap.get(`l));
      }
      Ifgt(l)->{
        mw.visitJumpInsn(IFGT,(Label)labelMap.get(`l));
      }
      Ifle(l)->{
        mw.visitJumpInsn(IFLE,(Label)labelMap.get(`l));
      }
      If_icmpeq(l)->{
        mw.visitJumpInsn(IF_ICMPEQ,(Label)labelMap.get(`l));
      }
      If_icmpne(l)->{
        mw.visitJumpInsn(IF_ICMPNE,(Label)labelMap.get(`l));
      }
      If_icmplt(l)->{
        mw.visitJumpInsn(IF_ICMPLT,(Label)labelMap.get(`l));
      }
      If_icmpge(l)->{
        mw.visitJumpInsn(IF_ICMPGE,(Label)labelMap.get(`l));
      }
      If_icmpgt(l)->{
        mw.visitJumpInsn(IF_ICMPGT,(Label)labelMap.get(`l));
      }
      If_icmple(l)->{
        mw.visitJumpInsn(IF_ICMPLE,(Label)labelMap.get(`l));
      }
      If_acmpeq(l)->{
        mw.visitJumpInsn(IF_ACMPEQ,(Label)labelMap.get(`l));
      }
      If_acmpne(l)->{
        mw.visitJumpInsn(IF_ACMPNE,(Label)labelMap.get(`l));
      }
      Goto(l)->{
        mw.visitJumpInsn(GOTO,(Label)labelMap.get(`l));
      }
      Jsr(l)->{
        mw.visitJumpInsn(JSR,(Label)labelMap.get(`l));
      }
      Ifnull(l)->{
        mw.visitJumpInsn(IFNULL,(Label)labelMap.get(`l));
      }
      Ifnonnull(l)->{
        mw.visitJumpInsn(IFNONNULL,(Label)labelMap.get(`l));
      }
      Invokevirtual(owner, name, desc)->{
        mw.visitMethodInsn(INVOKEVIRTUAL,`owner,`name,buildDescriptor(`desc));
      }
      Invokespecial(owner, name, desc)->{
        mw.visitMethodInsn(INVOKESPECIAL,`owner,`name,buildDescriptor(`desc));
      }
      Invokestatic(owner, name, desc)->{
        mw.visitMethodInsn(INVOKESTATIC,`owner,`name,buildDescriptor(`desc));
      }
      Invokeinterface(owner, name, desc)->{
        mw.visitMethodInsn(INVOKEINTERFACE,`owner,`name,buildDescriptor(`desc));
      }

      New(desc)->{
        mw.visitTypeInsn(NEW,`desc);
      }
      Anewarray(desc)->{
        mw.visitTypeInsn(ANEWARRAY,`desc);
      }
      Checkcast(desc)->{
        mw.visitTypeInsn(CHECKCAST,`desc);
      }
      Instanceof(desc)->{
        mw.visitTypeInsn(INSTANCEOF,`desc);
      }
      Iload(var)->{
        mw.visitVarInsn(ILOAD,`var);
      }
      Lload(var)->{
        mw.visitVarInsn(LLOAD,`var);
      }
      Fload(var)->{
        mw.visitVarInsn(FLOAD,`var);
      }
      Dload(var)->{
        mw.visitVarInsn(DLOAD,`var);
      }
      Aload(var)->{
        mw.visitVarInsn(ALOAD,`var);
      }
      Istore(var)->{
        mw.visitVarInsn(ISTORE,`var);
      }
      Lstore(var)->{
        mw.visitVarInsn(LSTORE,`var);
      }
      Fstore(var)->{
        mw.visitVarInsn(FSTORE,`var);
      }
      Dstore(var)->{
        mw.visitVarInsn(DSTORE,`var);
      }
      Astore(var)->{
        mw.visitVarInsn(ASTORE,`var);
      }
      Ret(var)->{
        mw.visitVarInsn(RET,`var);
      }
      Iinc(increment, var)->{
        mw.visitIincInsn(`var,`increment);
      }
      Ldc(value)->{
        mw.visitLdcInsn(buildConstant(`value)); 
      }

      Multianewarray(desc, dims)->{
        mw.visitMultiANewArrayInsn(`desc,`dims);
      }

      Tableswitch(min, max, dflt, labels) -> {
        TLabel[] tlabelTab = ((LabelList)`labels).toArray();
        Label[] labelTab = null;
        if(tlabelTab != null){
          labelTab = new Label[tlabelTab.length];
          for(int i=0;i<labelTab.length;i++){
            labelTab[i]=(Label)labelMap.get(tlabelTab[i]);
          }
        }
        mw.visitTableSwitchInsn(`min, `max, (Label)labelMap.get(`dflt), labelTab);
      }

      Lookupswitch(dflt, keys, labels) -> {
        TLabel[] tlabelTab = ((LabelList)`labels).toArray();
        Label[] labelTab = null;
        if(tlabelTab != null){
          labelTab = new Label[tlabelTab.length];
          for(int i=0;i<labelTab.length;i++){
            labelTab[i]=(Label)labelMap.get(tlabelTab[i]);
          }
        }
        mw.visitLookupSwitchInsn((Label)labelMap.get(`dflt),((intList)`keys).toArray(),labelTab);
      }
    }
  }
}
