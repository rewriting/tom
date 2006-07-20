
package bytecode;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassAdapter;

import bytecode.classtree.*;
import bytecode.classtree.types.*;

public class Test {
  public static void main(String[] args) throws Exception {
    if(args.length <= 0) {
      System.out.println("Usage : java bytecode.Test <class name>\nEx: java bytecode.Test bytecode.Subject");
      return;
    }

    ClassReader cr = new ClassReader(args[0]);
    TClassGenerator cg = new TClassGenerator();
    ClassAdapter ca = new ClassAdapter(cg);
    cr.accept(ca, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

    TClass c = cg.getTClass();
    System.out.println(c);
  }
}

