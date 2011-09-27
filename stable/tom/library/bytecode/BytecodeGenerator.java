/*
 * Copyright (c) 2000-2011, INPL, INRIA
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

import java.util.HashMap;
import java.util.Map;

public class BytecodeGenerator extends ClassWriter implements Opcodes {

  /**
   * <p>
   * This class allows to directly generate from the Gom Bytecode representation
   * the Bytecode program (object of type byte[]) 
   * <p>
   */       

           private static   tom.library.adt.bytecode.types.MethodList  tom_append_list_MethodList( tom.library.adt.bytecode.types.MethodList l1,  tom.library.adt.bytecode.types.MethodList  l2) {     if( l1.isEmptyMethodList() ) {       return l2;     } else if( l2.isEmptyMethodList() ) {       return l1;     } else if(  l1.getTailMethodList() .isEmptyMethodList() ) {       return  tom.library.adt.bytecode.types.methodlist.ConsMethodList.make( l1.getHeadMethodList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.methodlist.ConsMethodList.make( l1.getHeadMethodList() ,tom_append_list_MethodList( l1.getTailMethodList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.MethodList  tom_get_slice_MethodList( tom.library.adt.bytecode.types.MethodList  begin,  tom.library.adt.bytecode.types.MethodList  end, tom.library.adt.bytecode.types.MethodList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyMethodList()  ||  (end== tom.library.adt.bytecode.types.methodlist.EmptyMethodList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.methodlist.ConsMethodList.make( begin.getHeadMethodList() ,( tom.library.adt.bytecode.types.MethodList )tom_get_slice_MethodList( begin.getTailMethodList() ,end,tail)) ;   }      private static   tom.library.adt.bytecode.types.LocalVariableList  tom_append_list_LocalVariableList( tom.library.adt.bytecode.types.LocalVariableList l1,  tom.library.adt.bytecode.types.LocalVariableList  l2) {     if( l1.isEmptyLocalVariableList() ) {       return l2;     } else if( l2.isEmptyLocalVariableList() ) {       return l1;     } else if(  l1.getTailLocalVariableList() .isEmptyLocalVariableList() ) {       return  tom.library.adt.bytecode.types.localvariablelist.ConsLocalVariableList.make( l1.getHeadLocalVariableList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.localvariablelist.ConsLocalVariableList.make( l1.getHeadLocalVariableList() ,tom_append_list_LocalVariableList( l1.getTailLocalVariableList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.LocalVariableList  tom_get_slice_LocalVariableList( tom.library.adt.bytecode.types.LocalVariableList  begin,  tom.library.adt.bytecode.types.LocalVariableList  end, tom.library.adt.bytecode.types.LocalVariableList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyLocalVariableList()  ||  (end== tom.library.adt.bytecode.types.localvariablelist.EmptyLocalVariableList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.localvariablelist.ConsLocalVariableList.make( begin.getHeadLocalVariableList() ,( tom.library.adt.bytecode.types.LocalVariableList )tom_get_slice_LocalVariableList( begin.getTailLocalVariableList() ,end,tail)) ;   }      private static   tom.library.adt.bytecode.types.InstructionList  tom_append_list_InstructionList( tom.library.adt.bytecode.types.InstructionList l1,  tom.library.adt.bytecode.types.InstructionList  l2) {     if( l1.isEmptyInstructionList() ) {       return l2;     } else if( l2.isEmptyInstructionList() ) {       return l1;     } else if(  l1.getTailInstructionList() .isEmptyInstructionList() ) {       return  tom.library.adt.bytecode.types.instructionlist.ConsInstructionList.make( l1.getHeadInstructionList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.instructionlist.ConsInstructionList.make( l1.getHeadInstructionList() ,tom_append_list_InstructionList( l1.getTailInstructionList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.InstructionList  tom_get_slice_InstructionList( tom.library.adt.bytecode.types.InstructionList  begin,  tom.library.adt.bytecode.types.InstructionList  end, tom.library.adt.bytecode.types.InstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyInstructionList()  ||  (end== tom.library.adt.bytecode.types.instructionlist.EmptyInstructionList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.instructionlist.ConsInstructionList.make( begin.getHeadInstructionList() ,( tom.library.adt.bytecode.types.InstructionList )tom_get_slice_InstructionList( begin.getTailInstructionList() ,end,tail)) ;   }      private static   tom.library.adt.bytecode.types.InnerClassInfoList  tom_append_list_InnerClassInfoList( tom.library.adt.bytecode.types.InnerClassInfoList l1,  tom.library.adt.bytecode.types.InnerClassInfoList  l2) {     if( l1.isEmptyInnerClassInfoList() ) {       return l2;     } else if( l2.isEmptyInnerClassInfoList() ) {       return l1;     } else if(  l1.getTailInnerClassInfoList() .isEmptyInnerClassInfoList() ) {       return  tom.library.adt.bytecode.types.innerclassinfolist.ConsInnerClassInfoList.make( l1.getHeadInnerClassInfoList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.innerclassinfolist.ConsInnerClassInfoList.make( l1.getHeadInnerClassInfoList() ,tom_append_list_InnerClassInfoList( l1.getTailInnerClassInfoList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.InnerClassInfoList  tom_get_slice_InnerClassInfoList( tom.library.adt.bytecode.types.InnerClassInfoList  begin,  tom.library.adt.bytecode.types.InnerClassInfoList  end, tom.library.adt.bytecode.types.InnerClassInfoList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyInnerClassInfoList()  ||  (end== tom.library.adt.bytecode.types.innerclassinfolist.EmptyInnerClassInfoList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.innerclassinfolist.ConsInnerClassInfoList.make( begin.getHeadInnerClassInfoList() ,( tom.library.adt.bytecode.types.InnerClassInfoList )tom_get_slice_InnerClassInfoList( begin.getTailInnerClassInfoList() ,end,tail)) ;   }      private static   tom.library.adt.bytecode.types.FieldList  tom_append_list_FieldList( tom.library.adt.bytecode.types.FieldList l1,  tom.library.adt.bytecode.types.FieldList  l2) {     if( l1.isEmptyFieldList() ) {       return l2;     } else if( l2.isEmptyFieldList() ) {       return l1;     } else if(  l1.getTailFieldList() .isEmptyFieldList() ) {       return  tom.library.adt.bytecode.types.fieldlist.ConsFieldList.make( l1.getHeadFieldList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.fieldlist.ConsFieldList.make( l1.getHeadFieldList() ,tom_append_list_FieldList( l1.getTailFieldList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.FieldList  tom_get_slice_FieldList( tom.library.adt.bytecode.types.FieldList  begin,  tom.library.adt.bytecode.types.FieldList  end, tom.library.adt.bytecode.types.FieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyFieldList()  ||  (end== tom.library.adt.bytecode.types.fieldlist.EmptyFieldList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.fieldlist.ConsFieldList.make( begin.getHeadFieldList() ,( tom.library.adt.bytecode.types.FieldList )tom_get_slice_FieldList( begin.getTailFieldList() ,end,tail)) ;   }      private static   tom.library.adt.bytecode.types.TryCatchBlockList  tom_append_list_TryCatchBlockList( tom.library.adt.bytecode.types.TryCatchBlockList l1,  tom.library.adt.bytecode.types.TryCatchBlockList  l2) {     if( l1.isEmptyTryCatchBlockList() ) {       return l2;     } else if( l2.isEmptyTryCatchBlockList() ) {       return l1;     } else if(  l1.getTailTryCatchBlockList() .isEmptyTryCatchBlockList() ) {       return  tom.library.adt.bytecode.types.trycatchblocklist.ConsTryCatchBlockList.make( l1.getHeadTryCatchBlockList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.trycatchblocklist.ConsTryCatchBlockList.make( l1.getHeadTryCatchBlockList() ,tom_append_list_TryCatchBlockList( l1.getTailTryCatchBlockList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.TryCatchBlockList  tom_get_slice_TryCatchBlockList( tom.library.adt.bytecode.types.TryCatchBlockList  begin,  tom.library.adt.bytecode.types.TryCatchBlockList  end, tom.library.adt.bytecode.types.TryCatchBlockList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyTryCatchBlockList()  ||  (end== tom.library.adt.bytecode.types.trycatchblocklist.EmptyTryCatchBlockList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.trycatchblocklist.ConsTryCatchBlockList.make( begin.getHeadTryCatchBlockList() ,( tom.library.adt.bytecode.types.TryCatchBlockList )tom_get_slice_TryCatchBlockList( begin.getTailTryCatchBlockList() ,end,tail)) ;   }    

  private ClassNode ast;

  public BytecodeGenerator(ClassNode ast) {
    super(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    this.ast = ast;
  }

  public byte[] toByteArray(){

    {{if ( (((Object)ast) instanceof tom.library.adt.bytecode.types.ClassNode) ) {if ( ((( tom.library.adt.bytecode.types.ClassNode )((Object)ast)) instanceof tom.library.adt.bytecode.types.ClassNode) ) {if ( ((( tom.library.adt.bytecode.types.ClassNode )(( tom.library.adt.bytecode.types.ClassNode )((Object)ast))) instanceof tom.library.adt.bytecode.types.classnode.Class) ) { tom.library.adt.bytecode.types.ClassInfo  tomMatch677_1= (( tom.library.adt.bytecode.types.ClassNode )((Object)ast)).getinfo() ;if ( (tomMatch677_1 instanceof tom.library.adt.bytecode.types.ClassInfo) ) {if ( ((( tom.library.adt.bytecode.types.ClassInfo )tomMatch677_1) instanceof tom.library.adt.bytecode.types.classinfo.ClassInfo) ) { tom.library.adt.bytecode.types.InnerClassInfoList  tom_innerClasses= tomMatch677_1.getinnerClasses() ; tom.library.adt.bytecode.types.OuterClassInfo  tom_outerClass= tomMatch677_1.getouterClass() ; tom.library.adt.bytecode.types.FieldList  tom_fields= (( tom.library.adt.bytecode.types.ClassNode )((Object)ast)).getfields() ; tom.library.adt.bytecode.types.MethodList  tom_methods= (( tom.library.adt.bytecode.types.ClassNode )((Object)ast)).getmethods() ;



        // bytecode for the header
        visit(V1_1, ToolBox.buildAccessValue( tomMatch677_1.getaccess() ),  tomMatch677_1.getname() , ToolBox.buildSignature( tomMatch677_1.getsignature() ), tomMatch677_1.getsuperName() , ((tom.library.adt.bytecode.types.stringlist.StringList) tomMatch677_1.getinterfaces() ).toArray(new String[0]));


        //bytecode for the inner classes

        {{if ( (((Object)tom_innerClasses) instanceof tom.library.adt.bytecode.types.InnerClassInfoList) ) {if ( (((( tom.library.adt.bytecode.types.InnerClassInfoList )(( tom.library.adt.bytecode.types.InnerClassInfoList )((Object)tom_innerClasses))) instanceof tom.library.adt.bytecode.types.innerclassinfolist.ConsInnerClassInfoList) || ((( tom.library.adt.bytecode.types.InnerClassInfoList )(( tom.library.adt.bytecode.types.InnerClassInfoList )((Object)tom_innerClasses))) instanceof tom.library.adt.bytecode.types.innerclassinfolist.EmptyInnerClassInfoList)) ) { tom.library.adt.bytecode.types.InnerClassInfoList  tomMatch678__end__4=(( tom.library.adt.bytecode.types.InnerClassInfoList )((Object)tom_innerClasses));do {{if (!( tomMatch678__end__4.isEmptyInnerClassInfoList() )) { tom.library.adt.bytecode.types.InnerClassInfo  tom_innerClass= tomMatch678__end__4.getHeadInnerClassInfoList() ;{{if ( (((Object)tom_innerClass) instanceof tom.library.adt.bytecode.types.InnerClassInfo) ) {if ( ((( tom.library.adt.bytecode.types.InnerClassInfo )((Object)tom_innerClass)) instanceof tom.library.adt.bytecode.types.InnerClassInfo) ) {if ( ((( tom.library.adt.bytecode.types.InnerClassInfo )(( tom.library.adt.bytecode.types.InnerClassInfo )((Object)tom_innerClass))) instanceof tom.library.adt.bytecode.types.innerclassinfo.InnerClassInfo) ) {



                visitInnerClass( (( tom.library.adt.bytecode.types.InnerClassInfo )((Object)tom_innerClass)).getname() , (( tom.library.adt.bytecode.types.InnerClassInfo )((Object)tom_innerClass)).getouterName() , (( tom.library.adt.bytecode.types.InnerClassInfo )((Object)tom_innerClass)).getinnerName() ,ToolBox.buildAccessValue( (( tom.library.adt.bytecode.types.InnerClassInfo )((Object)tom_innerClass)).getaccess() ));
              }}}}}

          }if ( tomMatch678__end__4.isEmptyInnerClassInfoList() ) {tomMatch678__end__4=(( tom.library.adt.bytecode.types.InnerClassInfoList )((Object)tom_innerClasses));} else {tomMatch678__end__4= tomMatch678__end__4.getTailInnerClassInfoList() ;}}} while(!( (tomMatch678__end__4==(( tom.library.adt.bytecode.types.InnerClassInfoList )((Object)tom_innerClasses))) ));}}}}


        //bytecode for the outer class

        {{if ( (((Object)tom_outerClass) instanceof tom.library.adt.bytecode.types.OuterClassInfo) ) {if ( ((( tom.library.adt.bytecode.types.OuterClassInfo )((Object)tom_outerClass)) instanceof tom.library.adt.bytecode.types.OuterClassInfo) ) {if ( ((( tom.library.adt.bytecode.types.OuterClassInfo )(( tom.library.adt.bytecode.types.OuterClassInfo )((Object)tom_outerClass))) instanceof tom.library.adt.bytecode.types.outerclassinfo.OuterClassInfo) ) {

            visitOuterClass( (( tom.library.adt.bytecode.types.OuterClassInfo )((Object)tom_outerClass)).getowner() , (( tom.library.adt.bytecode.types.OuterClassInfo )((Object)tom_outerClass)).getname() ,ToolBox.buildDescriptor( (( tom.library.adt.bytecode.types.OuterClassInfo )((Object)tom_outerClass)).getdesc() ));
          }}}}}


        //bytecode for the fields 

        {{if ( (((Object)tom_fields) instanceof tom.library.adt.bytecode.types.FieldList) ) {if ( (((( tom.library.adt.bytecode.types.FieldList )(( tom.library.adt.bytecode.types.FieldList )((Object)tom_fields))) instanceof tom.library.adt.bytecode.types.fieldlist.ConsFieldList) || ((( tom.library.adt.bytecode.types.FieldList )(( tom.library.adt.bytecode.types.FieldList )((Object)tom_fields))) instanceof tom.library.adt.bytecode.types.fieldlist.EmptyFieldList)) ) { tom.library.adt.bytecode.types.FieldList  tomMatch681__end__4=(( tom.library.adt.bytecode.types.FieldList )((Object)tom_fields));do {{if (!( tomMatch681__end__4.isEmptyFieldList() )) { tom.library.adt.bytecode.types.Field  tom_field= tomMatch681__end__4.getHeadFieldList() ;{{if ( (((Object)tom_field) instanceof tom.library.adt.bytecode.types.Field) ) {if ( ((( tom.library.adt.bytecode.types.Field )((Object)tom_field)) instanceof tom.library.adt.bytecode.types.Field) ) {if ( ((( tom.library.adt.bytecode.types.Field )(( tom.library.adt.bytecode.types.Field )((Object)tom_field))) instanceof tom.library.adt.bytecode.types.field.Field) ) {





                FieldVisitor fw = visitField(ToolBox.buildAccessValue( (( tom.library.adt.bytecode.types.Field )((Object)tom_field)).getaccess() ), (( tom.library.adt.bytecode.types.Field )((Object)tom_field)).getname() ,ToolBox.buildDescriptor( (( tom.library.adt.bytecode.types.Field )((Object)tom_field)).getdesc() ),ToolBox.buildSignature( (( tom.library.adt.bytecode.types.Field )((Object)tom_field)).getsignature() ),ToolBox.buildConstant( (( tom.library.adt.bytecode.types.Field )((Object)tom_field)).getvalue() ));
                // we do not visit the annotations and attributes
                fw.visitEnd(); 
              }}}}}

          }if ( tomMatch681__end__4.isEmptyFieldList() ) {tomMatch681__end__4=(( tom.library.adt.bytecode.types.FieldList )((Object)tom_fields));} else {tomMatch681__end__4= tomMatch681__end__4.getTailFieldList() ;}}} while(!( (tomMatch681__end__4==(( tom.library.adt.bytecode.types.FieldList )((Object)tom_fields))) ));}}}}

    
        //bytecode for the methods

        {{if ( (((Object)tom_methods) instanceof tom.library.adt.bytecode.types.MethodList) ) {if ( (((( tom.library.adt.bytecode.types.MethodList )(( tom.library.adt.bytecode.types.MethodList )((Object)tom_methods))) instanceof tom.library.adt.bytecode.types.methodlist.ConsMethodList) || ((( tom.library.adt.bytecode.types.MethodList )(( tom.library.adt.bytecode.types.MethodList )((Object)tom_methods))) instanceof tom.library.adt.bytecode.types.methodlist.EmptyMethodList)) ) { tom.library.adt.bytecode.types.MethodList  tomMatch683__end__4=(( tom.library.adt.bytecode.types.MethodList )((Object)tom_methods));do {{if (!( tomMatch683__end__4.isEmptyMethodList() )) { tom.library.adt.bytecode.types.Method  tom_method= tomMatch683__end__4.getHeadMethodList() ;

            MethodVisitor methodVisitor;
            {{if ( (((Object)tom_method) instanceof tom.library.adt.bytecode.types.Method) ) {if ( ((( tom.library.adt.bytecode.types.Method )((Object)tom_method)) instanceof tom.library.adt.bytecode.types.Method) ) {if ( ((( tom.library.adt.bytecode.types.Method )(( tom.library.adt.bytecode.types.Method )((Object)tom_method))) instanceof tom.library.adt.bytecode.types.method.Method) ) { tom.library.adt.bytecode.types.MethodInfo  tomMatch684_1= (( tom.library.adt.bytecode.types.Method )((Object)tom_method)).getinfo() ; tom.library.adt.bytecode.types.MethodCode  tomMatch684_2= (( tom.library.adt.bytecode.types.Method )((Object)tom_method)).getcode() ;if ( (tomMatch684_1 instanceof tom.library.adt.bytecode.types.MethodInfo) ) {if ( ((( tom.library.adt.bytecode.types.MethodInfo )tomMatch684_1) instanceof tom.library.adt.bytecode.types.methodinfo.MethodInfo) ) {if ( (tomMatch684_2 instanceof tom.library.adt.bytecode.types.MethodCode) ) {if ( ((( tom.library.adt.bytecode.types.MethodCode )tomMatch684_2) instanceof tom.library.adt.bytecode.types.methodcode.MethodCode) ) { tom.library.adt.bytecode.types.InstructionList  tom_code= tomMatch684_2.getinstructions() ; tom.library.adt.bytecode.types.LocalVariableList  tom_localVariables= tomMatch684_2.getlocalVariables() ; tom.library.adt.bytecode.types.TryCatchBlockList  tom_tryCatchBlockLists= tomMatch684_2.gettryCatchBlocks() ;

                methodVisitor = visitMethod(ToolBox.buildAccessValue( tomMatch684_1.getaccess() ),
                     tomMatch684_1.getname() ,
                    ToolBox.buildDescriptor( tomMatch684_1.getdesc() ),
                    ToolBox.buildSignature( tomMatch684_1.getsignature() ),
                    ((tom.library.adt.bytecode.types.stringlist.StringList) tomMatch684_1.getexceptions() ).toArray(new String[0]));

                methodVisitor.visitCode();

                Map<LabelNode,Label> labelmap = new HashMap<LabelNode,Label>();
                //bytecode for the method code 
                {{if ( (((Object)tom_code) instanceof tom.library.adt.bytecode.types.InstructionList) ) {if ( (((( tom.library.adt.bytecode.types.InstructionList )(( tom.library.adt.bytecode.types.InstructionList )((Object)tom_code))) instanceof tom.library.adt.bytecode.types.instructionlist.ConsInstructionList) || ((( tom.library.adt.bytecode.types.InstructionList )(( tom.library.adt.bytecode.types.InstructionList )((Object)tom_code))) instanceof tom.library.adt.bytecode.types.instructionlist.EmptyInstructionList)) ) { tom.library.adt.bytecode.types.InstructionList  tomMatch685__end__4=(( tom.library.adt.bytecode.types.InstructionList )((Object)tom_code));do {{if (!( tomMatch685__end__4.isEmptyInstructionList() )) { tom.library.adt.bytecode.types.Instruction  tomMatch685_8= tomMatch685__end__4.getHeadInstructionList() ;if ( (tomMatch685_8 instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )tomMatch685_8) instanceof tom.library.adt.bytecode.types.instruction.Anchor) ) {

                    labelmap.put( tomMatch685_8.getlabel() ,new Label());
                  }}}if ( tomMatch685__end__4.isEmptyInstructionList() ) {tomMatch685__end__4=(( tom.library.adt.bytecode.types.InstructionList )((Object)tom_code));} else {tomMatch685__end__4= tomMatch685__end__4.getTailInstructionList() ;}}} while(!( (tomMatch685__end__4==(( tom.library.adt.bytecode.types.InstructionList )((Object)tom_code))) ));}}}{if ( (((Object)tom_code) instanceof tom.library.adt.bytecode.types.InstructionList) ) {if ( (((( tom.library.adt.bytecode.types.InstructionList )(( tom.library.adt.bytecode.types.InstructionList )((Object)tom_code))) instanceof tom.library.adt.bytecode.types.instructionlist.ConsInstructionList) || ((( tom.library.adt.bytecode.types.InstructionList )(( tom.library.adt.bytecode.types.InstructionList )((Object)tom_code))) instanceof tom.library.adt.bytecode.types.instructionlist.EmptyInstructionList)) ) { tom.library.adt.bytecode.types.InstructionList  tomMatch685__end__14=(( tom.library.adt.bytecode.types.InstructionList )((Object)tom_code));do {{if (!( tomMatch685__end__14.isEmptyInstructionList() )) { tom.library.adt.bytecode.types.Instruction  tom_inst= tomMatch685__end__14.getHeadInstructionList() ;{{if ( (((Object)tom_inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)tom_inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)tom_inst))) instanceof tom.library.adt.bytecode.types.instruction.Anchor) ) {



                        methodVisitor.visitLabel(labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)tom_inst)).getlabel() ));
                      }}}}{if ( (((Object)tom_inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {
addInstruction(methodVisitor,(( tom.library.adt.bytecode.types.Instruction )((Object)tom_inst)),labelmap);}}}

                  }if ( tomMatch685__end__14.isEmptyInstructionList() ) {tomMatch685__end__14=(( tom.library.adt.bytecode.types.InstructionList )((Object)tom_code));} else {tomMatch685__end__14= tomMatch685__end__14.getTailInstructionList() ;}}} while(!( (tomMatch685__end__14==(( tom.library.adt.bytecode.types.InstructionList )((Object)tom_code))) ));}}}}{{if ( (((Object)tom_tryCatchBlockLists) instanceof tom.library.adt.bytecode.types.TryCatchBlockList) ) {if ( (((( tom.library.adt.bytecode.types.TryCatchBlockList )(( tom.library.adt.bytecode.types.TryCatchBlockList )((Object)tom_tryCatchBlockLists))) instanceof tom.library.adt.bytecode.types.trycatchblocklist.ConsTryCatchBlockList) || ((( tom.library.adt.bytecode.types.TryCatchBlockList )(( tom.library.adt.bytecode.types.TryCatchBlockList )((Object)tom_tryCatchBlockLists))) instanceof tom.library.adt.bytecode.types.trycatchblocklist.EmptyTryCatchBlockList)) ) { tom.library.adt.bytecode.types.TryCatchBlockList  tomMatch687__end__4=(( tom.library.adt.bytecode.types.TryCatchBlockList )((Object)tom_tryCatchBlockLists));do {{if (!( tomMatch687__end__4.isEmptyTryCatchBlockList() )) { tom.library.adt.bytecode.types.TryCatchBlock  tomMatch687_10= tomMatch687__end__4.getHeadTryCatchBlockList() ;if ( (tomMatch687_10 instanceof tom.library.adt.bytecode.types.TryCatchBlock) ) {if ( ((( tom.library.adt.bytecode.types.TryCatchBlock )tomMatch687_10) instanceof tom.library.adt.bytecode.types.trycatchblock.TryCatchBlock) ) { tom.library.adt.bytecode.types.LabelNode  tom_start= tomMatch687_10.getstart() ; tom.library.adt.bytecode.types.LabelNode  tom_end= tomMatch687_10.getend() ; tom.library.adt.bytecode.types.Handler  tom_handler= tomMatch687_10.gethandler() ;{{if ( (((Object)tom_handler) instanceof tom.library.adt.bytecode.types.Handler) ) {if ( ((( tom.library.adt.bytecode.types.Handler )((Object)tom_handler)) instanceof tom.library.adt.bytecode.types.Handler) ) {if ( ((( tom.library.adt.bytecode.types.Handler )(( tom.library.adt.bytecode.types.Handler )((Object)tom_handler))) instanceof tom.library.adt.bytecode.types.handler.CatchHandler) ) {






                        methodVisitor.visitTryCatchBlock(labelmap.get(tom_start),labelmap.get(tom_end),labelmap.get( (( tom.library.adt.bytecode.types.Handler )((Object)tom_handler)).gethandler() ), (( tom.library.adt.bytecode.types.Handler )((Object)tom_handler)).gettype() );
                      }}}}{if ( (((Object)tom_handler) instanceof tom.library.adt.bytecode.types.Handler) ) {if ( ((( tom.library.adt.bytecode.types.Handler )((Object)tom_handler)) instanceof tom.library.adt.bytecode.types.Handler) ) {if ( ((( tom.library.adt.bytecode.types.Handler )(( tom.library.adt.bytecode.types.Handler )((Object)tom_handler))) instanceof tom.library.adt.bytecode.types.handler.FinallyHandler) ) {

                        methodVisitor.visitTryCatchBlock(labelmap.get(tom_start),labelmap.get(tom_end),labelmap.get( (( tom.library.adt.bytecode.types.Handler )((Object)tom_handler)).gethandler() ),null);
                      }}}}}

                  }}}if ( tomMatch687__end__4.isEmptyTryCatchBlockList() ) {tomMatch687__end__4=(( tom.library.adt.bytecode.types.TryCatchBlockList )((Object)tom_tryCatchBlockLists));} else {tomMatch687__end__4= tomMatch687__end__4.getTailTryCatchBlockList() ;}}} while(!( (tomMatch687__end__4==(( tom.library.adt.bytecode.types.TryCatchBlockList )((Object)tom_tryCatchBlockLists))) ));}}}}{{if ( (((Object)tom_localVariables) instanceof tom.library.adt.bytecode.types.LocalVariableList) ) {if ( (((( tom.library.adt.bytecode.types.LocalVariableList )(( tom.library.adt.bytecode.types.LocalVariableList )((Object)tom_localVariables))) instanceof tom.library.adt.bytecode.types.localvariablelist.ConsLocalVariableList) || ((( tom.library.adt.bytecode.types.LocalVariableList )(( tom.library.adt.bytecode.types.LocalVariableList )((Object)tom_localVariables))) instanceof tom.library.adt.bytecode.types.localvariablelist.EmptyLocalVariableList)) ) { tom.library.adt.bytecode.types.LocalVariableList  tomMatch689__end__4=(( tom.library.adt.bytecode.types.LocalVariableList )((Object)tom_localVariables));do {{if (!( tomMatch689__end__4.isEmptyLocalVariableList() )) { tom.library.adt.bytecode.types.LocalVariable  tomMatch689_13= tomMatch689__end__4.getHeadLocalVariableList() ;if ( (tomMatch689_13 instanceof tom.library.adt.bytecode.types.LocalVariable) ) {if ( ((( tom.library.adt.bytecode.types.LocalVariable )tomMatch689_13) instanceof tom.library.adt.bytecode.types.localvariable.LocalVariable) ) {




                    methodVisitor.visitLocalVariable( tomMatch689_13.getname() ,  tomMatch689_13.gettypeDesc() , ToolBox.buildSignature( tomMatch689_13.getsignature() ), labelmap.get( tomMatch689_13.getstart() ), labelmap.get( tomMatch689_13.getend() ),  tomMatch689_13.getindex() );
                  }}}if ( tomMatch689__end__4.isEmptyLocalVariableList() ) {tomMatch689__end__4=(( tom.library.adt.bytecode.types.LocalVariableList )((Object)tom_localVariables));} else {tomMatch689__end__4= tomMatch689__end__4.getTailLocalVariableList() ;}}} while(!( (tomMatch689__end__4==(( tom.library.adt.bytecode.types.LocalVariableList )((Object)tom_localVariables))) ));}}}}


                methodVisitor.visitMaxs(0, 0);
                methodVisitor.visitEnd();
              }}}}}}}}}

          }if ( tomMatch683__end__4.isEmptyMethodList() ) {tomMatch683__end__4=(( tom.library.adt.bytecode.types.MethodList )((Object)tom_methods));} else {tomMatch683__end__4= tomMatch683__end__4.getTailMethodList() ;}}} while(!( (tomMatch683__end__4==(( tom.library.adt.bytecode.types.MethodList )((Object)tom_methods))) ));}}}}

      }}}}}}}


    // gets the bytecode
    return super.toByteArray();
  }

  public void addInstruction(MethodVisitor methodVisitor, Instruction inst, Map<LabelNode,Label> labelmap) {
    {{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Nop) ) {

        methodVisitor.visitInsn(NOP);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Aconst_null) ) {

        methodVisitor.visitInsn(ACONST_NULL);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Iconst_m1) ) {

        methodVisitor.visitInsn(ICONST_M1);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Iconst_0) ) {

        methodVisitor.visitInsn(ICONST_0);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Iconst_1) ) {

        methodVisitor.visitInsn(ICONST_1);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Iconst_2) ) {

        methodVisitor.visitInsn(ICONST_2);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Iconst_3) ) {

        methodVisitor.visitInsn(ICONST_3);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Iconst_4) ) {

        methodVisitor.visitInsn(ICONST_4);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Iconst_5) ) {

        methodVisitor.visitInsn(ICONST_5);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lconst_0) ) {

        methodVisitor.visitInsn(LCONST_0);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lconst_1) ) {

        methodVisitor.visitInsn(LCONST_1);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Fconst_0) ) {

        methodVisitor.visitInsn(FCONST_0);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Fconst_1) ) {

        methodVisitor.visitInsn(FCONST_1);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Fconst_2) ) {

        methodVisitor.visitInsn(FCONST_2);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dconst_0) ) {

        methodVisitor.visitInsn(DCONST_0);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dconst_1) ) {

        methodVisitor.visitInsn(DCONST_1);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Iaload) ) {

        methodVisitor.visitInsn(IALOAD);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Laload) ) {

        methodVisitor.visitInsn(LALOAD);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Faload) ) {

        methodVisitor.visitInsn(FALOAD);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Daload) ) {

        methodVisitor.visitInsn(DALOAD);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Aaload) ) {

        methodVisitor.visitInsn(AALOAD);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Baload) ) {

        methodVisitor.visitInsn(BALOAD);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Caload) ) {

        methodVisitor.visitInsn(CALOAD);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Saload) ) {

        methodVisitor.visitInsn(SALOAD);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Iastore) ) {

        methodVisitor.visitInsn(IASTORE);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lastore) ) {

        methodVisitor.visitInsn(LASTORE);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Fastore) ) {

        methodVisitor.visitInsn(FASTORE);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dastore) ) {

        methodVisitor.visitInsn(DASTORE);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Aastore) ) {

        methodVisitor.visitInsn(AASTORE);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Bastore) ) {

        methodVisitor.visitInsn(BASTORE);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Castore) ) {

        methodVisitor.visitInsn(CASTORE);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Sastore) ) {

        methodVisitor.visitInsn(SASTORE);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Pop) ) {

        methodVisitor.visitInsn(POP);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Pop2) ) {

        methodVisitor.visitInsn(POP2);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dup) ) {

        methodVisitor.visitInsn(DUP);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dup_x1) ) {

        methodVisitor.visitInsn(DUP_X1);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dup_x2) ) {

        methodVisitor.visitInsn(DUP_X2);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dup2) ) {

        methodVisitor.visitInsn(DUP2);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dup2_x1) ) {

        methodVisitor.visitInsn(DUP2_X1);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dup2_x2) ) {

        methodVisitor.visitInsn(DUP2_X2);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Swap) ) {

        methodVisitor.visitInsn(SWAP);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Iadd) ) {

        methodVisitor.visitInsn(IADD);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ladd) ) {

        methodVisitor.visitInsn(LADD);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Fadd) ) {

        methodVisitor.visitInsn(FADD);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dadd) ) {

        methodVisitor.visitInsn(DADD);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Isub) ) {

        methodVisitor.visitInsn(ISUB);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lsub) ) {

        methodVisitor.visitInsn(LSUB);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Fsub) ) {

        methodVisitor.visitInsn(FSUB);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dsub) ) {

        methodVisitor.visitInsn(DSUB);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Imul) ) {

        methodVisitor.visitInsn(IMUL);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lmul) ) {

        methodVisitor.visitInsn(LMUL);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Fmul) ) {

        methodVisitor.visitInsn(FMUL);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dmul) ) {

        methodVisitor.visitInsn(DMUL);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Idiv) ) {

        methodVisitor.visitInsn(IDIV);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ldiv) ) {

        methodVisitor.visitInsn(LDIV);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Fdiv) ) {

        methodVisitor.visitInsn(FDIV);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ddiv) ) {

        methodVisitor.visitInsn(DDIV);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Irem) ) {

        methodVisitor.visitInsn(IREM);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lrem) ) {

        methodVisitor.visitInsn(LREM);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Frem) ) {

        methodVisitor.visitInsn(FREM);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Drem) ) {

        methodVisitor.visitInsn(DREM);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ineg) ) {

        methodVisitor.visitInsn(INEG);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lneg) ) {

        methodVisitor.visitInsn(LNEG);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Fneg) ) {

        methodVisitor.visitInsn(FNEG);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dneg) ) {

        methodVisitor.visitInsn(DNEG);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ishl) ) {

        methodVisitor.visitInsn(ISHL);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lshl) ) {

        methodVisitor.visitInsn(LSHL);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ishr) ) {

        methodVisitor.visitInsn(ISHR);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lshr) ) {

        methodVisitor.visitInsn(LSHR);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Iushr) ) {

        methodVisitor.visitInsn(IUSHR);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lushr) ) {

        methodVisitor.visitInsn(LUSHR);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Iand) ) {

        methodVisitor.visitInsn(IAND);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Land) ) {

        methodVisitor.visitInsn(LAND);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ior) ) {

        methodVisitor.visitInsn(IOR);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lor) ) {

        methodVisitor.visitInsn(LOR);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ixor) ) {

        methodVisitor.visitInsn(IXOR);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lxor) ) {

        methodVisitor.visitInsn(LXOR);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.I2l) ) {

        methodVisitor.visitInsn(I2L);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.I2f) ) {

        methodVisitor.visitInsn(I2F);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.I2d) ) {

        methodVisitor.visitInsn(I2D);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.L2i) ) {

        methodVisitor.visitInsn(L2I);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.L2f) ) {

        methodVisitor.visitInsn(L2F);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.L2d) ) {

        methodVisitor.visitInsn(L2D);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.F2i) ) {

        methodVisitor.visitInsn(F2I);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.F2l) ) {

        methodVisitor.visitInsn(F2L);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.F2d) ) {

        methodVisitor.visitInsn(F2D);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.D2i) ) {

        methodVisitor.visitInsn(D2I);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.D2l) ) {

        methodVisitor.visitInsn(D2L);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.D2f) ) {

        methodVisitor.visitInsn(D2F);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.I2b) ) {

        methodVisitor.visitInsn(I2B);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.I2c) ) {

        methodVisitor.visitInsn(I2C);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.I2s) ) {

        methodVisitor.visitInsn(I2S);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lcmp) ) {

        methodVisitor.visitInsn(LCMP);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Fcmpl) ) {

        methodVisitor.visitInsn(FCMPL);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Fcmpg) ) {

        methodVisitor.visitInsn(FCMPG);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dcmpl) ) {

        methodVisitor.visitInsn(DCMPL);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dcmpg) ) {

        methodVisitor.visitInsn(DCMPG);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ireturn) ) {

        methodVisitor.visitInsn(IRETURN);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lreturn) ) {

        methodVisitor.visitInsn(LRETURN);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Freturn) ) {

        methodVisitor.visitInsn(FRETURN);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dreturn) ) {

        methodVisitor.visitInsn(DRETURN);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Areturn) ) {

        methodVisitor.visitInsn(ARETURN);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Return) ) {

        methodVisitor.visitInsn(RETURN);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Arraylength) ) {

        methodVisitor.visitInsn(ARRAYLENGTH);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Athrow) ) {

        methodVisitor.visitInsn(ATHROW);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Monitorenter) ) {

        methodVisitor.visitInsn(MONITORENTER);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Monitorexit) ) {

        methodVisitor.visitInsn(MONITOREXIT);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Getstatic) ) {

        methodVisitor.visitFieldInsn(GETSTATIC, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getowner() , (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getname() ,ToolBox.buildDescriptor( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getfieldDesc() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Putstatic) ) {

        methodVisitor.visitFieldInsn(PUTSTATIC, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getowner() , (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getname() ,ToolBox.buildDescriptor( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getfieldDesc() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Getfield) ) {

        methodVisitor.visitFieldInsn(GETFIELD, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getowner() , (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getname() ,ToolBox.buildDescriptor( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getfieldDesc() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Putfield) ) {

        methodVisitor.visitFieldInsn(PUTFIELD, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getowner() , (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getname() ,ToolBox.buildDescriptor( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getfieldDesc() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Bipush) ) {

        methodVisitor.visitIntInsn(BIPUSH, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getoperand() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Sipush) ) {

        methodVisitor.visitIntInsn(SIPUSH, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getoperand() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Newarray) ) {

        methodVisitor.visitIntInsn(NEWARRAY, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getoperand() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ifeq) ) {

        methodVisitor.visitJumpInsn(IFEQ,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ifne) ) {

        methodVisitor.visitJumpInsn(IFNE,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Iflt) ) {

        methodVisitor.visitJumpInsn(IFLT,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ifge) ) {

        methodVisitor.visitJumpInsn(IFGE,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ifgt) ) {

        methodVisitor.visitJumpInsn(IFGT,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ifle) ) {

        methodVisitor.visitJumpInsn(IFLE,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.If_icmpeq) ) {

        methodVisitor.visitJumpInsn(IF_ICMPEQ,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.If_icmpne) ) {

        methodVisitor.visitJumpInsn(IF_ICMPNE,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.If_icmplt) ) {

        methodVisitor.visitJumpInsn(IF_ICMPLT,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.If_icmpge) ) {

        methodVisitor.visitJumpInsn(IF_ICMPGE,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.If_icmpgt) ) {

        methodVisitor.visitJumpInsn(IF_ICMPGT,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.If_icmple) ) {

        methodVisitor.visitJumpInsn(IF_ICMPLE,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.If_acmpeq) ) {

        methodVisitor.visitJumpInsn(IF_ACMPEQ,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.If_acmpne) ) {

        methodVisitor.visitJumpInsn(IF_ACMPNE,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Goto) ) {

        methodVisitor.visitJumpInsn(GOTO,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Jsr) ) {

        methodVisitor.visitJumpInsn(JSR,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ifnull) ) {

        methodVisitor.visitJumpInsn(IFNULL,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ifnonnull) ) {

        methodVisitor.visitJumpInsn(IFNONNULL,labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabel() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Invokevirtual) ) {

        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getowner() , (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getname() ,ToolBox.buildDescriptor( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getmethodDesc() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Invokespecial) ) {

        methodVisitor.visitMethodInsn(INVOKESPECIAL, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getowner() , (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getname() ,ToolBox.buildDescriptor( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getmethodDesc() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Invokestatic) ) {

        methodVisitor.visitMethodInsn(INVOKESTATIC, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getowner() , (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getname() ,ToolBox.buildDescriptor( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getmethodDesc() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Invokeinterface) ) {

        methodVisitor.visitMethodInsn(INVOKEINTERFACE, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getowner() , (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getname() ,ToolBox.buildDescriptor( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getmethodDesc() ));
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.New) ) {


        methodVisitor.visitTypeInsn(NEW, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).gettypeDesc() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Anewarray) ) {

        methodVisitor.visitTypeInsn(ANEWARRAY, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).gettypeDesc() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Checkcast) ) {

        methodVisitor.visitTypeInsn(CHECKCAST, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).gettypeDesc() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Instanceof) ) {

        methodVisitor.visitTypeInsn(INSTANCEOF, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).gettypeDesc() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Iload) ) {

        methodVisitor.visitVarInsn(ILOAD, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getvar() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lload) ) {

        methodVisitor.visitVarInsn(LLOAD, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getvar() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Fload) ) {

        methodVisitor.visitVarInsn(FLOAD, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getvar() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dload) ) {

        methodVisitor.visitVarInsn(DLOAD, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getvar() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Aload) ) {

        methodVisitor.visitVarInsn(ALOAD, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getvar() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Istore) ) {

        methodVisitor.visitVarInsn(ISTORE, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getvar() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lstore) ) {

        methodVisitor.visitVarInsn(LSTORE, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getvar() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Fstore) ) {

        methodVisitor.visitVarInsn(FSTORE, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getvar() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Dstore) ) {

        methodVisitor.visitVarInsn(DSTORE, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getvar() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Astore) ) {

        methodVisitor.visitVarInsn(ASTORE, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getvar() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ret) ) {

        methodVisitor.visitVarInsn(RET, (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getvar() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Iinc) ) {

        methodVisitor.visitIincInsn( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getvar() , (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getincr() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Ldc) ) {

        methodVisitor.visitLdcInsn(ToolBox.buildConstant( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getcst() )); 
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Multianewarray) ) {


        methodVisitor.visitMultiANewArrayInsn( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).gettypeDesc() , (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getdims() );
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Tableswitch) ) {


        LabelNode[] labelnodes= ((tom.library.adt.bytecode.types.labelnodelist.LabelNodeList) (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabels() ).toArray(new LabelNode[0]);
        Label[] labels = null;
        if(labelnodes != null){
          labels = new Label[labelnodes.length];
          for(int i=0;i<labels.length;i++){
            labels[i] = labelmap.get(labelnodes[i]);
          }
        }
        methodVisitor.visitTableSwitchInsn( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getmin() ,  (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getmax() , labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getdflt() ), labels);
      }}}}{if ( (((Object)inst) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )((Object)inst)) instanceof tom.library.adt.bytecode.types.Instruction) ) {if ( ((( tom.library.adt.bytecode.types.Instruction )(( tom.library.adt.bytecode.types.Instruction )((Object)inst))) instanceof tom.library.adt.bytecode.types.instruction.Lookupswitch) ) { tom.library.adt.bytecode.types.IntList  tom_keys= (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getkeys() ;


        LabelNode[] labelnodes= ((tom.library.adt.bytecode.types.labelnodelist.LabelNodeList) (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getlabels() ).toArray(new LabelNode[0]);
        Label[] labels = null;
        if(labelnodes != null){
          labels = new Label[labelnodes.length];
          for(int i=0;i<labels.length;i++){
            labels[i]=labelmap.get(labelnodes[i]);
          }
        }
        int[] array = new int[((IntList)tom_keys).length()];
        java.util.Iterator<Integer> it = ((tom.library.adt.bytecode.types.intlist.IntList)tom_keys).iterator();
        for(int i=0 ; it.hasNext() ; i++) {
          array[i] = it.next();
        }
        methodVisitor.visitLookupSwitchInsn(labelmap.get( (( tom.library.adt.bytecode.types.Instruction )((Object)inst)).getdflt() ),array,labels);
      }}}}}

  }
}
