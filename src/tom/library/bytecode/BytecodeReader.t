/*
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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

  %include { ../adt/bytecode/Bytecode.tom }

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
    ast = ast.setmethods(`MethodList(l*, method));
  }

  public void appendField(Field field) {
    FieldList l = ast.getfields();
    ast = ast.setfields(`FieldList(l*, field));
  }

  public void appendInnerClass(InnerClassInfo info) {
    ClassInfo i = ast.getinfo();
    InnerClassInfoList l = i.getinnerClasses();
    ast = ast.setinfo(i.setinnerClasses(`InnerClassInfoList(l*, info)));
  }

  public void visit(
      int version,
      int access,
      String name,
      String signature,
      String superName,
      String[] interfaces) {
    ClassInfo info = `ClassInfo(name, Signature(signature), ToolBox.buildAccess(access), superName, ToolBox.buildStringList(interfaces), InnerClassInfoList(), EmptyOuterClassInfo());
    ast = `Class(info, FieldList(), MethodList());
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
    Field field = `Field(ToolBox.buildAccess(access), name, ToolBox.buildFieldDescriptor(desc), Signature(signature), ToolBox.buildValue(value));
    appendField(field);

    return null;
  }

  public void visitInnerClass(
      String name,
      String outerName,
      String innerName,
      int access) {
    InnerClassInfo info = `InnerClassInfo(name, outerName, innerName, ToolBox.buildAccess(access));
    appendInnerClass(info);
  }

  public MethodVisitor visitMethod(
      int access,
      String name,
      String desc,
      String signature,
      String[] exceptions) {
    return new MethodGenerator(this, ToolBox.buildAccess(access), name, ToolBox.buildMethodDescriptor(desc), `Signature(signature), ToolBox.buildStringList(exceptions));
  }

  public void visitOuterClass(String owner, String name, String desc) {
    OuterClassInfo info = `OuterClassInfo(owner, name, ToolBox.buildMethodDescriptor(desc));
    ClassInfo i = ast.getinfo();
    ast = ast.setinfo(i.setouterClass(info));
  }

  public void visitSource(String source, String debug) {
    // do nothing
  }


}

