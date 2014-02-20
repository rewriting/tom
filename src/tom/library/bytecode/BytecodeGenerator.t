/*
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;

import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;

import java.util.HashMap;
import java.util.Map;

public class BytecodeGenerator extends ClassWriter implements Opcodes {

  /**
   * <p>
   * This class allows to directly generate from the Gom Bytecode representation
   * the Bytecode program (object of type byte[]) 
   * <p>
   */       

  %include { ../adt/bytecode/Bytecode.tom }

  private ClassNode ast;

  public BytecodeGenerator(ClassNode ast) {
    super(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    this.ast = ast;
  }

  public byte[] toByteArray(){

    %match(ast){

      Class(ClassInfo(name,signature,access,superName,interfaces,innerClasses,outerClass), fields, methods) -> {

        // bytecode for the header
        visit(V1_1, ToolBox.buildAccessValue(`access), `name, ToolBox.buildSignature(`signature),`superName, ((tom.library.adt.bytecode.types.stringlist.StringList)`interfaces).toArray(new String[0]));


        //bytecode for the inner classes

        %match(innerClasses){
          InnerClassInfoList(_*,innerClass,_*) -> {
            %match(innerClass){
              InnerClassInfo(innerName,innerOuterName,innerInnerName,innerAccess) -> {
                visitInnerClass(`innerName,`innerOuterName,`innerInnerName,ToolBox.buildAccessValue(`innerAccess));
              }
            }
          }
        }

        //bytecode for the outer class

        %match(outerClass){
          OuterClassInfo(outerOwner,outerName,outerDesc) -> {
            visitOuterClass(`outerOwner,`outerName,ToolBox.buildDescriptor(`outerDesc));
          }
        }

        //bytecode for the fields 

        %match(fields){

          FieldList(_*,field,_*) -> {

            %match(field){
              Field(fieldAccess,fieldName, fieldDesc, fieldSignature, fieldValue) -> {
                FieldVisitor fw = visitField(ToolBox.buildAccessValue(`fieldAccess),`fieldName,ToolBox.buildDescriptor(`fieldDesc),ToolBox.buildSignature(`fieldSignature),ToolBox.buildConstant(`fieldValue));
                // we do not visit the annotations and attributes
                fw.visitEnd(); 
              }
            }
          }
        }
    
        //bytecode for the methods

        %match(methods){
          MethodList(_*,method,_*) -> {
            MethodVisitor methodVisitor;
            %match(method){
              Method(MethodInfo(_,methAccess,methName,desc,methSignature,exceptions),MethodCode(code,localVariables,tryCatchBlockLists)) -> {
                methodVisitor = visitMethod(ToolBox.buildAccessValue(`methAccess),
                    `methName,
                    ToolBox.buildDescriptor(`desc),
                    ToolBox.buildSignature(`methSignature),
                    ((tom.library.adt.bytecode.types.stringlist.StringList)`exceptions).toArray(new String[0]));

                methodVisitor.visitCode();

                Map<LabelNode,Label> labelmap = new HashMap<LabelNode,Label>();
                //bytecode for the method code 
                %match(code){
                  InstructionList(_*,Anchor(label),_*) -> {
                    labelmap.put(`label,new Label());
                  }
                  InstructionList(_*,inst,_*) -> {
                    %match(inst) {
                      Anchor(label) -> {
                        methodVisitor.visitLabel(labelmap.get(`label));
                      }
                      i -> {addInstruction(methodVisitor,`i,labelmap);}
                    }
                  }
                }

                %match(tryCatchBlockLists) {
                  TryCatchBlockList(_*, TryCatchBlock(start,end,handler), _*) -> {
                    %match(handler) {
                      CatchHandler(h, type) -> {
                        methodVisitor.visitTryCatchBlock(labelmap.get(`start),labelmap.get(`end),labelmap.get(`h),`type);
                      }
                      FinallyHandler(h) -> {
                        methodVisitor.visitTryCatchBlock(labelmap.get(`start),labelmap.get(`end),labelmap.get(`h),null);
                      }
                    }
                  }
                }

                %match(localVariables) {
                  LocalVariableList(_*, LocalVariable(varname, vardesc, varsignature, start, end, index), _*) -> {
                    methodVisitor.visitLocalVariable(`varname, `vardesc, ToolBox.buildSignature(`varsignature), labelmap.get(`start), labelmap.get(`end), `index);
                  }
                }

                methodVisitor.visitMaxs(0, 0);
                methodVisitor.visitEnd();
              }
            }
          }
        }
      }
    }

    // gets the bytecode
    return super.toByteArray();
  }

  public void addInstruction(MethodVisitor methodVisitor, Instruction inst, Map<LabelNode,Label> labelmap) {
    %match(inst) {
      Nop() -> {
        methodVisitor.visitInsn(NOP);
      }
      Aconst_null() -> {
        methodVisitor.visitInsn(ACONST_NULL);
      }
      Iconst_m1() -> {
        methodVisitor.visitInsn(ICONST_M1);
      }
      Iconst_0() -> {
        methodVisitor.visitInsn(ICONST_0);
      }
      Iconst_1() -> {
        methodVisitor.visitInsn(ICONST_1);
      }
      Iconst_2() -> {
        methodVisitor.visitInsn(ICONST_2);
      }
      Iconst_3() -> {
        methodVisitor.visitInsn(ICONST_3);
      }
      Iconst_4() -> {
        methodVisitor.visitInsn(ICONST_4);
      }
      Iconst_5() -> {
        methodVisitor.visitInsn(ICONST_5);
      }
      Lconst_0() -> {
        methodVisitor.visitInsn(LCONST_0);
      }
      Lconst_1() -> {
        methodVisitor.visitInsn(LCONST_1);
      }
      Fconst_0() -> {
        methodVisitor.visitInsn(FCONST_0);
      }
      Fconst_1() -> {
        methodVisitor.visitInsn(FCONST_1);
      }
      Fconst_2() -> {
        methodVisitor.visitInsn(FCONST_2);
      }
      Dconst_0() -> {
        methodVisitor.visitInsn(DCONST_0);
      }
      Dconst_1() -> {
        methodVisitor.visitInsn(DCONST_1);
      }
      Iaload() -> {
        methodVisitor.visitInsn(IALOAD);
      }
      Laload() -> {
        methodVisitor.visitInsn(LALOAD);
      }
      Faload() -> {
        methodVisitor.visitInsn(FALOAD);
      }
      Daload() -> {
        methodVisitor.visitInsn(DALOAD);
      }
      Aaload() -> {
        methodVisitor.visitInsn(AALOAD);
      }
      Baload() -> {
        methodVisitor.visitInsn(BALOAD);
      }
      Caload() -> {
        methodVisitor.visitInsn(CALOAD);
      }
      Saload() -> {
        methodVisitor.visitInsn(SALOAD);
      }
      Iastore() -> {
        methodVisitor.visitInsn(IASTORE);
      }
      Lastore() -> {
        methodVisitor.visitInsn(LASTORE);
      }
      Fastore() -> {
        methodVisitor.visitInsn(FASTORE);
      }
      Dastore() -> {
        methodVisitor.visitInsn(DASTORE);
      }
      Aastore() -> {
        methodVisitor.visitInsn(AASTORE);
      }
      Bastore() -> {
        methodVisitor.visitInsn(BASTORE);
      }
      Castore() -> {
        methodVisitor.visitInsn(CASTORE);
      }
      Sastore() -> {
        methodVisitor.visitInsn(SASTORE);
      }
      Pop() -> {
        methodVisitor.visitInsn(POP);
      }
      Pop2() -> {
        methodVisitor.visitInsn(POP2);
      }
      Dup() -> {
        methodVisitor.visitInsn(DUP);
      }
      Dup_x1() -> {
        methodVisitor.visitInsn(DUP_X1);
      }
      Dup_x2() -> {
        methodVisitor.visitInsn(DUP_X2);
      }
      Dup2() -> {
        methodVisitor.visitInsn(DUP2);
      }
      Dup2_x1() -> {
        methodVisitor.visitInsn(DUP2_X1);
      }
      Dup2_x2() -> {
        methodVisitor.visitInsn(DUP2_X2);
      }
      Swap() -> {
        methodVisitor.visitInsn(SWAP);
      }
      Iadd() -> {
        methodVisitor.visitInsn(IADD);
      }
      Ladd() -> {
        methodVisitor.visitInsn(LADD);
      }
      Fadd() -> {
        methodVisitor.visitInsn(FADD);
      }
      Dadd() -> {
        methodVisitor.visitInsn(DADD);
      }
      Isub() -> {
        methodVisitor.visitInsn(ISUB);
      }
      Lsub() -> {
        methodVisitor.visitInsn(LSUB);
      }
      Fsub() -> {
        methodVisitor.visitInsn(FSUB);
      }
      Dsub() -> {
        methodVisitor.visitInsn(DSUB);
      }
      Imul() -> {
        methodVisitor.visitInsn(IMUL);
      }
      Lmul() -> {
        methodVisitor.visitInsn(LMUL);
      }
      Fmul() -> {
        methodVisitor.visitInsn(FMUL);
      }
      Dmul() -> {
        methodVisitor.visitInsn(DMUL);
      }
      Idiv() -> {
        methodVisitor.visitInsn(IDIV);
      }
      Ldiv() -> {
        methodVisitor.visitInsn(LDIV);
      }
      Fdiv() -> {
        methodVisitor.visitInsn(FDIV);
      }
      Ddiv() -> {
        methodVisitor.visitInsn(DDIV);
      }
      Irem() -> {
        methodVisitor.visitInsn(IREM);
      }
      Lrem() -> {
        methodVisitor.visitInsn(LREM);
      }
      Frem() -> {
        methodVisitor.visitInsn(FREM);
      }
      Drem() -> {
        methodVisitor.visitInsn(DREM);
      }
      Ineg() -> {
        methodVisitor.visitInsn(INEG);
      }
      Lneg() -> {
        methodVisitor.visitInsn(LNEG);
      }
      Fneg() -> {
        methodVisitor.visitInsn(FNEG);
      }
      Dneg() -> {
        methodVisitor.visitInsn(DNEG);
      }
      Ishl() -> {
        methodVisitor.visitInsn(ISHL);
      }
      Lshl() -> {
        methodVisitor.visitInsn(LSHL);
      }
      Ishr() -> {
        methodVisitor.visitInsn(ISHR);
      }
      Lshr() -> {
        methodVisitor.visitInsn(LSHR);
      }
      Iushr() -> {
        methodVisitor.visitInsn(IUSHR);
      }
      Lushr() -> {
        methodVisitor.visitInsn(LUSHR);
      }
      Iand() -> {
        methodVisitor.visitInsn(IAND);
      }
      Land() -> {
        methodVisitor.visitInsn(LAND);
      }
      Ior() -> {
        methodVisitor.visitInsn(IOR);
      }
      Lor() -> {
        methodVisitor.visitInsn(LOR);
      }
      Ixor() -> {
        methodVisitor.visitInsn(IXOR);
      }
      Lxor() -> {
        methodVisitor.visitInsn(LXOR);
      }
      I2l() -> {
        methodVisitor.visitInsn(I2L);
      }
      I2f() -> {
        methodVisitor.visitInsn(I2F);
      }
      I2d() -> {
        methodVisitor.visitInsn(I2D);
      }
      L2i() -> {
        methodVisitor.visitInsn(L2I);
      }
      L2f() -> {
        methodVisitor.visitInsn(L2F);
      }
      L2d() -> {
        methodVisitor.visitInsn(L2D);
      }
      F2i() -> {
        methodVisitor.visitInsn(F2I);
      }
      F2l() -> {
        methodVisitor.visitInsn(F2L);
      }
      F2d() -> {
        methodVisitor.visitInsn(F2D);
      }
      D2i() -> {
        methodVisitor.visitInsn(D2I);
      }
      D2l() -> {
        methodVisitor.visitInsn(D2L);
      }
      D2f() -> {
        methodVisitor.visitInsn(D2F);
      }
      I2b() -> {
        methodVisitor.visitInsn(I2B);
      }
      I2c() -> {
        methodVisitor.visitInsn(I2C);
      }
      I2s() -> {
        methodVisitor.visitInsn(I2S);
      }
      Lcmp() -> {
        methodVisitor.visitInsn(LCMP);
      }
      Fcmpl() -> {
        methodVisitor.visitInsn(FCMPL);
      }
      Fcmpg() -> {
        methodVisitor.visitInsn(FCMPG);
      }
      Dcmpl() -> {
        methodVisitor.visitInsn(DCMPL);
      }
      Dcmpg() -> {
        methodVisitor.visitInsn(DCMPG);
      }
      Ireturn() -> {
        methodVisitor.visitInsn(IRETURN);
      }
      Lreturn() -> {
        methodVisitor.visitInsn(LRETURN);
      }
      Freturn() -> {
        methodVisitor.visitInsn(FRETURN);
      }
      Dreturn() -> {
        methodVisitor.visitInsn(DRETURN);
      }
      Areturn() -> {
        methodVisitor.visitInsn(ARETURN);
      }
      Return() -> {
        methodVisitor.visitInsn(RETURN);
      }
      Arraylength() -> {
        methodVisitor.visitInsn(ARRAYLENGTH);
      }
      Athrow() -> {
        methodVisitor.visitInsn(ATHROW);
      }
      Monitorenter() -> {
        methodVisitor.visitInsn(MONITORENTER);
      }
      Monitorexit() -> {
        methodVisitor.visitInsn(MONITOREXIT);
      }
      Getstatic(owner, name, desc) -> {
        methodVisitor.visitFieldInsn(GETSTATIC,`owner,`name,ToolBox.buildDescriptor(`desc));
      }
      Putstatic(owner, name, desc) -> {
        methodVisitor.visitFieldInsn(PUTSTATIC,`owner,`name,ToolBox.buildDescriptor(`desc));
      }
      Getfield(owner, name, desc) -> {
        methodVisitor.visitFieldInsn(GETFIELD,`owner,`name,ToolBox.buildDescriptor(`desc));
      }
      Putfield(owner, name, desc) -> {
        methodVisitor.visitFieldInsn(PUTFIELD,`owner,`name,ToolBox.buildDescriptor(`desc));
      }
      Bipush(operand) -> {
        methodVisitor.visitIntInsn(BIPUSH,`operand);
      }
      Sipush(operand) -> {
        methodVisitor.visitIntInsn(SIPUSH,`operand);
      }
      Newarray(operand) -> {
        methodVisitor.visitIntInsn(NEWARRAY,`operand);
      }
      Ifeq(l) -> {
        methodVisitor.visitJumpInsn(IFEQ,labelmap.get(`l));
      }
      Ifne(l) -> {
        methodVisitor.visitJumpInsn(IFNE,labelmap.get(`l));
      }
      Iflt(l) -> {
        methodVisitor.visitJumpInsn(IFLT,labelmap.get(`l));
      }
      Ifge(l) -> {
        methodVisitor.visitJumpInsn(IFGE,labelmap.get(`l));
      }
      Ifgt(l) -> {
        methodVisitor.visitJumpInsn(IFGT,labelmap.get(`l));
      }
      Ifle(l) -> {
        methodVisitor.visitJumpInsn(IFLE,labelmap.get(`l));
      }
      If_icmpeq(l) -> {
        methodVisitor.visitJumpInsn(IF_ICMPEQ,labelmap.get(`l));
      }
      If_icmpne(l) -> {
        methodVisitor.visitJumpInsn(IF_ICMPNE,labelmap.get(`l));
      }
      If_icmplt(l) -> {
        methodVisitor.visitJumpInsn(IF_ICMPLT,labelmap.get(`l));
      }
      If_icmpge(l) -> {
        methodVisitor.visitJumpInsn(IF_ICMPGE,labelmap.get(`l));
      }
      If_icmpgt(l) -> {
        methodVisitor.visitJumpInsn(IF_ICMPGT,labelmap.get(`l));
      }
      If_icmple(l) -> {
        methodVisitor.visitJumpInsn(IF_ICMPLE,labelmap.get(`l));
      }
      If_acmpeq(l) -> {
        methodVisitor.visitJumpInsn(IF_ACMPEQ,labelmap.get(`l));
      }
      If_acmpne(l) -> {
        methodVisitor.visitJumpInsn(IF_ACMPNE,labelmap.get(`l));
      }
      Goto(l) -> {
        methodVisitor.visitJumpInsn(GOTO,labelmap.get(`l));
      }
      Jsr(l) -> {
        methodVisitor.visitJumpInsn(JSR,labelmap.get(`l));
      }
      Ifnull(l) -> {
        methodVisitor.visitJumpInsn(IFNULL,labelmap.get(`l));
      }
      Ifnonnull(l) -> {
        methodVisitor.visitJumpInsn(IFNONNULL,labelmap.get(`l));
      }
      Invokevirtual(owner, name, desc) -> {
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL,`owner,`name,ToolBox.buildDescriptor(`desc));
      }
      Invokespecial(owner, name, desc) -> {
        methodVisitor.visitMethodInsn(INVOKESPECIAL,`owner,`name,ToolBox.buildDescriptor(`desc));
      }
      Invokestatic(owner, name, desc) -> {
        methodVisitor.visitMethodInsn(INVOKESTATIC,`owner,`name,ToolBox.buildDescriptor(`desc));
      }
      Invokeinterface(owner, name, desc) -> {
        methodVisitor.visitMethodInsn(INVOKEINTERFACE,`owner,`name,ToolBox.buildDescriptor(`desc));
      }

      New(desc) -> {
        methodVisitor.visitTypeInsn(NEW,`desc);
      }
      Anewarray(desc) -> {
        methodVisitor.visitTypeInsn(ANEWARRAY,`desc);
      }
      Checkcast(desc) -> {
        methodVisitor.visitTypeInsn(CHECKCAST,`desc);
      }
      Instanceof(desc) -> {
        methodVisitor.visitTypeInsn(INSTANCEOF,`desc);
      }
      Iload(var) -> {
        methodVisitor.visitVarInsn(ILOAD,`var);
      }
      Lload(var) -> {
        methodVisitor.visitVarInsn(LLOAD,`var);
      }
      Fload(var) -> {
        methodVisitor.visitVarInsn(FLOAD,`var);
      }
      Dload(var) -> {
        methodVisitor.visitVarInsn(DLOAD,`var);
      }
      Aload(var) -> {
        methodVisitor.visitVarInsn(ALOAD,`var);
      }
      Istore(var) -> {
        methodVisitor.visitVarInsn(ISTORE,`var);
      }
      Lstore(var) -> {
        methodVisitor.visitVarInsn(LSTORE,`var);
      }
      Fstore(var) -> {
        methodVisitor.visitVarInsn(FSTORE,`var);
      }
      Dstore(var) -> {
        methodVisitor.visitVarInsn(DSTORE,`var);
      }
      Astore(var) -> {
        methodVisitor.visitVarInsn(ASTORE,`var);
      }
      Ret(var) -> {
        methodVisitor.visitVarInsn(RET,`var);
      }
      Iinc(increment, var) -> {
        methodVisitor.visitIincInsn(`var,`increment);
      }
      Ldc(value) -> {
        methodVisitor.visitLdcInsn(ToolBox.buildConstant(`value)); 
      }

      Multianewarray(desc, dims) -> {
        methodVisitor.visitMultiANewArrayInsn(`desc,`dims);
      }

      Tableswitch(min, max, dflt, labels) -> {
        LabelNode[] labelnodes= ((tom.library.adt.bytecode.types.labelnodelist.LabelNodeList)`labels).toArray(new LabelNode[0]);
        Label[] labels = null;
        if(labelnodes != null){
          labels = new Label[labelnodes.length];
          for(int i=0;i<labels.length;i++){
            labels[i] = labelmap.get(labelnodes[i]);
          }
        }
        methodVisitor.visitTableSwitchInsn(`min, `max, labelmap.get(`dflt), labels);
      }

      Lookupswitch(dflt, keys, labels) -> {
        LabelNode[] labelnodes= ((tom.library.adt.bytecode.types.labelnodelist.LabelNodeList)`labels).toArray(new LabelNode[0]);
        Label[] labels = null;
        if(labelnodes != null){
          labels = new Label[labelnodes.length];
          for(int i=0;i<labels.length;i++){
            labels[i]=labelmap.get(labelnodes[i]);
          }
        }
        int[] array = new int[((IntList)`keys).length()];
        java.util.Iterator<Integer> it = ((tom.library.adt.bytecode.types.intlist.IntList)`keys).iterator();
        for(int i=0 ; it.hasNext() ; i++) {
          array[i] = it.next();
        }
        methodVisitor.visitLookupSwitchInsn(labelmap.get(`dflt),array,labels);
      }
    }
  }
}
