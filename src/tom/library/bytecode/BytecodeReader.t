/*
 * Copyright (c) 2004-2006, INRIA
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

import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;

public class BytecodeReader implements ClassVisitor {
  %include { adt/bytecode/Bytecode.tom }

  private TClass clazz;

  public TClass getTClass() {
    return clazz;
  }

  public void appendMethod(TMethod method){
    TMethodList l = clazz.getmethods();
    clazz = clazz.setmethods(`MethodList(l*, method));
  }

  public void appendField(TField field) {
    TFieldList l = clazz.getfields();
    clazz = clazz.setfields(`FieldList(l*, field));
  }

  public void appendInnerClass(TInnerClassInfo info) {
    TClassInfo i = clazz.getinfo();
    TInnerClassInfoList l = i.getinnerClasses();
    clazz = clazz.setinfo(i.setinnerClasses(`InnerClassInfoList(l*, info)));
  }

  public void visit(
      int version,
      int access,
      String name,
      String signature,
      String superName,
      String[] interfaces) {
    TClassInfo info = `ClassInfo(name, Signature(signature), ToolBox.buildTAccess(access), superName, ToolBox.buildTStringList(interfaces), InnerClassInfoList(), EmptyOuterClassInfo());
    clazz = `Class(info, FieldList(), MethodList());
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
    TField field = `Field(ToolBox.buildTAccess(access), name, ToolBox.buildTFieldDescriptor(desc), Signature(signature), ToolBox.buildTValue(value));
    appendField(field);

    return null;
  }

  public void visitInnerClass(
      String name,
      String outerName,
      String innerName,
      int access) {
    TInnerClassInfo info = `InnerClassInfo(name, outerName, innerName, ToolBox.buildTAccess(access));
    appendInnerClass(info);
  }

  public MethodVisitor visitMethod(
      int access,
      String name,
      String desc,
      String signature,
      String[] exceptions) {
    return new TMethodGenerator(this, ToolBox.buildTAccess(access), name, ToolBox.buildTMethodDescriptor(desc), `Signature(signature), ToolBox.buildTStringList(exceptions));
  }

  public void visitOuterClass(String owner, String name, String desc) {
    TOuterClassInfo info = `OuterClassInfo(owner, name, ToolBox.buildTMethodDescriptor(desc));
    TClassInfo i = clazz.getinfo();
    clazz = clazz.setinfo(i.setouterClass(info));
  }

  public void visitSource(String source, String debug) {
    // do nothing
  }
}

