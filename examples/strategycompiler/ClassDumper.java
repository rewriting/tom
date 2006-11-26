
package strategycompiler;

import java.util.HashMap;
import java.io.IOException;
import java.io.FileOutputStream;

import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;

import tom.library.bytecode.*;


/**
 * Java class to Gom term and Gom term to Java class dumper.
 * This class allow to get the Gom term of a class file and vice versa.
 * A map with already loaded class is used to prevent dumping class file into
 * Gom term when it has already been done.
 */
public class ClassDumper {
  /**
   * Map of already loaded class (used to prevent class reloading).
   */
  private static HashMap loadedClass = new HashMap();

  /**
   * Dump a Java class into a Gom term.
   *
   * @param className The Java class filename (without trailing `.class').
   * @return The Gom term representing the Java class.
   */
  public static TClass dumpClass(String className) {
    String internalClassName = className.replace('.', '/');
    Object o = loadedClass.get(internalClassName);
    TClass clazz = null;
    if(o == null) {
        //System.out.println("Parsing class file `" + internalClassName + "' ...");
        BytecodeReader cr = new BytecodeReader(internalClassName);
        clazz = cr.getTClass();
        loadedClass.put(internalClassName, clazz);
    } else
      clazz = (TClass)o;

    return clazz;
  }

  /**
   * Dump the given Gom term into a class file.
   *
   * @param clazz The Gom term representing the class to be dumped.
   * @param filename The class file name (without trailing `.class').
   */
  public static void dumpTClassToFile(TClass clazz, String filename) {
    try {
      BytecodeGenerator bg = new BytecodeGenerator();
      byte[] code = bg.toBytecode(clazz);
      FileOutputStream fos = new FileOutputStream(filename + ".class");
      fos.write(code);
      fos.close();
    } catch(Exception e) {
      System.err.println("Error when writing the class file : " + filename);
      e.printStackTrace();
    }
  }

  /**
   * Dump the given Gom term into a Java class and return the corresponding
   * `Class' object. No file is created by this method, all the job is done in
   * memory.
   *
   * @param clazz The Gom term representing the class to be dumped.
   * @return The `Class' object corresponding to the Gom term.
   */
  public static Class dumpTClass(TClass clazz) throws ClassNotFoundException {
    BytecodeGenerator bg = new BytecodeGenerator();
    byte[] code = bg.toBytecode(clazz);

    // Create a `in memory' class loader.
    class MemClassLoader extends ClassLoader {
      private byte[] b;
      public MemClassLoader(byte[] b) {
        super();
        this.b = b;
      }
      protected Class findClass(String name) {
        return defineClass(name, b, 0, b.length);
      }
    }

    MemClassLoader loader = new MemClassLoader(code);
    Class c = loader.loadClass(clazz.getinfo().getname());

    return c;
  }

  /**
   * Clear the loaded class cache.
   */
  public static void clearCache() {
    loadedClass.clear();
  }
}

