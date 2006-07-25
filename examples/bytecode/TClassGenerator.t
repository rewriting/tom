
package bytecode;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Attribute;

import bytecode.classtree.*;
import bytecode.classtree.types.*;

public class TClassGenerator implements ClassVisitor {
	%include { classtree/ClassTree.tom }

  private TClass clazz;

  public TClass getTClass() {
    return clazz;
  }

  public void appendMethod(TMethod method){
    TMethodList l = clazz.getmethods();
    clazz = `Class(clazz.getinfo(), clazz.getfields(), MethodList(l*, method));
  }

  public void appendField(TField field) {
    TFieldList l = clazz.getfields();
    clazz = `Class(clazz.getinfo(), FieldList(l*, field), clazz.getmethods());
  }

  public void appendInnerClass(TInnerClassInfo info) {
    TClassInfo i = clazz.getinfo();
    TInnerClassInfoList l = i.getinnerClasses();
    clazz = `Class(ClassInfo(i.getname(), i.getsignature(), i.getaccess(), i.getsuperName(), i.getinterfaces(), InnerClassInfoList(l*, info), i.getouterClass()), clazz.getfields(), clazz.getmethods());
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
    // TODO
  }

  public FieldVisitor visitField(
      int access,
      String name,
      String desc,
      String signature,
      Object value) {
    TField field = `Field(ToolBox.buildTAccess(access), name, desc, Signature(signature), ToolBox.buildTValue(value));
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
    return new TMethodGenerator(this, ToolBox.buildTAccess(access), name, desc, `Signature(signature), ToolBox.buildTStringList(exceptions));
  }

  public void visitOuterClass(String owner, String name, String desc) {
    TOuterClassInfo info = `OuterClassInfo(owner, name, desc);
    TClassInfo i = clazz.getinfo();
    clazz = `Class(ClassInfo(i.getname(), i.getsignature(), i.getaccess(), i.getsuperName(), i.getinterfaces(), i.getinnerClasses(), info), clazz.getfields(), clazz.getmethods());
  }

  public void visitSource(String source, String debug) {
    // TODO
  }
}

