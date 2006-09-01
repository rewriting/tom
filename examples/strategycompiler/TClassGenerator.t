
package strategycompiler;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Attribute;

import strategycompiler.classtree.*;
import strategycompiler.classtree.types.*;

public class TClassGenerator implements ClassVisitor {
	%include { classtree/ClassTree.tom }

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

