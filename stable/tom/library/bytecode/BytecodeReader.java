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

import org.objectweb.asm.*;

import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;
import java.io.IOException;

import tom.library.bytecode.*;
import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;


public class BytecodeReader implements ClassVisitor {

           private static   tom.library.adt.bytecode.types.MethodList  tom_append_list_MethodList( tom.library.adt.bytecode.types.MethodList l1,  tom.library.adt.bytecode.types.MethodList  l2) {     if( l1.isEmptyMethodList() ) {       return l2;     } else if( l2.isEmptyMethodList() ) {       return l1;     } else if(  l1.getTailMethodList() .isEmptyMethodList() ) {       return  tom.library.adt.bytecode.types.methodlist.ConsMethodList.make( l1.getHeadMethodList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.methodlist.ConsMethodList.make( l1.getHeadMethodList() ,tom_append_list_MethodList( l1.getTailMethodList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.MethodList  tom_get_slice_MethodList( tom.library.adt.bytecode.types.MethodList  begin,  tom.library.adt.bytecode.types.MethodList  end, tom.library.adt.bytecode.types.MethodList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyMethodList()  ||  (end== tom.library.adt.bytecode.types.methodlist.EmptyMethodList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.methodlist.ConsMethodList.make( begin.getHeadMethodList() ,( tom.library.adt.bytecode.types.MethodList )tom_get_slice_MethodList( begin.getTailMethodList() ,end,tail)) ;   }      private static   tom.library.adt.bytecode.types.InnerClassInfoList  tom_append_list_InnerClassInfoList( tom.library.adt.bytecode.types.InnerClassInfoList l1,  tom.library.adt.bytecode.types.InnerClassInfoList  l2) {     if( l1.isEmptyInnerClassInfoList() ) {       return l2;     } else if( l2.isEmptyInnerClassInfoList() ) {       return l1;     } else if(  l1.getTailInnerClassInfoList() .isEmptyInnerClassInfoList() ) {       return  tom.library.adt.bytecode.types.innerclassinfolist.ConsInnerClassInfoList.make( l1.getHeadInnerClassInfoList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.innerclassinfolist.ConsInnerClassInfoList.make( l1.getHeadInnerClassInfoList() ,tom_append_list_InnerClassInfoList( l1.getTailInnerClassInfoList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.InnerClassInfoList  tom_get_slice_InnerClassInfoList( tom.library.adt.bytecode.types.InnerClassInfoList  begin,  tom.library.adt.bytecode.types.InnerClassInfoList  end, tom.library.adt.bytecode.types.InnerClassInfoList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyInnerClassInfoList()  ||  (end== tom.library.adt.bytecode.types.innerclassinfolist.EmptyInnerClassInfoList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.innerclassinfolist.ConsInnerClassInfoList.make( begin.getHeadInnerClassInfoList() ,( tom.library.adt.bytecode.types.InnerClassInfoList )tom_get_slice_InnerClassInfoList( begin.getTailInnerClassInfoList() ,end,tail)) ;   }      private static   tom.library.adt.bytecode.types.FieldList  tom_append_list_FieldList( tom.library.adt.bytecode.types.FieldList l1,  tom.library.adt.bytecode.types.FieldList  l2) {     if( l1.isEmptyFieldList() ) {       return l2;     } else if( l2.isEmptyFieldList() ) {       return l1;     } else if(  l1.getTailFieldList() .isEmptyFieldList() ) {       return  tom.library.adt.bytecode.types.fieldlist.ConsFieldList.make( l1.getHeadFieldList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.fieldlist.ConsFieldList.make( l1.getHeadFieldList() ,tom_append_list_FieldList( l1.getTailFieldList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.FieldList  tom_get_slice_FieldList( tom.library.adt.bytecode.types.FieldList  begin,  tom.library.adt.bytecode.types.FieldList  end, tom.library.adt.bytecode.types.FieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyFieldList()  ||  (end== tom.library.adt.bytecode.types.fieldlist.EmptyFieldList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.fieldlist.ConsFieldList.make( begin.getHeadFieldList() ,( tom.library.adt.bytecode.types.FieldList )tom_get_slice_FieldList( begin.getTailFieldList() ,end,tail)) ;   }    

  private ClassNode ast;

  public BytecodeReader(String className){
    try {
      ClassAdapter ca = new ClassAdapter(this);
      ClassReader cr = new ClassReader(className);
      cr.accept(ca, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
    } catch(IOException io) {
      System.err.println("Class not found : " + className);
    }
  }

  public ClassNode getAst() {
    return ast;
  }

  public void appendMethod(Method method){
    MethodList l = ast.getmethods();
    ast = ast.setmethods(tom_append_list_MethodList(l, tom.library.adt.bytecode.types.methodlist.ConsMethodList.make(method, tom.library.adt.bytecode.types.methodlist.EmptyMethodList.make() ) ));
  }

  public void appendField(Field field) {
    FieldList l = ast.getfields();
    ast = ast.setfields(tom_append_list_FieldList(l, tom.library.adt.bytecode.types.fieldlist.ConsFieldList.make(field, tom.library.adt.bytecode.types.fieldlist.EmptyFieldList.make() ) ));
  }

  public void appendInnerClass(InnerClassInfo info) {
    ClassInfo i = ast.getinfo();
    InnerClassInfoList l = i.getinnerClasses();
    ast = ast.setinfo(i.setinnerClasses(tom_append_list_InnerClassInfoList(l, tom.library.adt.bytecode.types.innerclassinfolist.ConsInnerClassInfoList.make(info, tom.library.adt.bytecode.types.innerclassinfolist.EmptyInnerClassInfoList.make() ) )));
  }

  public void visit(
      int version,
      int access,
      String name,
      String signature,
      String superName,
      String[] interfaces) {
    ClassInfo info =  tom.library.adt.bytecode.types.classinfo.ClassInfo.make(name,  tom.library.adt.bytecode.types.signature.Signature.make(signature) , ToolBox.buildAccess(access), superName, ToolBox.buildStringList(interfaces),  tom.library.adt.bytecode.types.innerclassinfolist.EmptyInnerClassInfoList.make() ,  tom.library.adt.bytecode.types.outerclassinfo.EmptyOuterClassInfo.make() ) ;
    ast =  tom.library.adt.bytecode.types.classnode.Class.make(info,  tom.library.adt.bytecode.types.fieldlist.EmptyFieldList.make() ,  tom.library.adt.bytecode.types.methodlist.EmptyMethodList.make() ) ;
  }

  public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
    // TODO
    return null;
  }

  public void visitAttribute(Attribute attr) {
    // TODO
  }

  public void visitEnd() {
    // do nothing
  }

  public FieldVisitor visitField(
      int access,
      String name,
      String desc,
      String signature,
      Object value) {
    Field field =  tom.library.adt.bytecode.types.field.Field.make(ToolBox.buildAccess(access), name, ToolBox.buildFieldDescriptor(desc),  tom.library.adt.bytecode.types.signature.Signature.make(signature) , ToolBox.buildValue(value)) ;
    appendField(field);

    return null;
  }

  public void visitInnerClass(
      String name,
      String outerName,
      String innerName,
      int access) {
    InnerClassInfo info =  tom.library.adt.bytecode.types.innerclassinfo.InnerClassInfo.make(name, outerName, innerName, ToolBox.buildAccess(access)) ;
    appendInnerClass(info);
  }

  public MethodVisitor visitMethod(
      int access,
      String name,
      String desc,
      String signature,
      String[] exceptions) {
    return new MethodGenerator(this, ToolBox.buildAccess(access), name, ToolBox.buildMethodDescriptor(desc),  tom.library.adt.bytecode.types.signature.Signature.make(signature) , ToolBox.buildStringList(exceptions));
  }

  public void visitOuterClass(String owner, String name, String desc) {
    OuterClassInfo info =  tom.library.adt.bytecode.types.outerclassinfo.OuterClassInfo.make(owner, name, ToolBox.buildMethodDescriptor(desc)) ;
    ClassInfo i = ast.getinfo();
    ast = ast.setinfo(i.setouterClass(info));
  }

  public void visitSource(String source, String debug) {
    // do nothing
  }


}

