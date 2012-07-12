package definitions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hubert
 */
class Constructor {

  private Algebraic caller;
  private Typable[] fields;
  private boolean lock = false;
  private Method make;
  private String name;

  Constructor(Algebraic caller, Typable[] fields, String name) {
    this.caller = caller;
    this.fields = fields;
    this.name = name;
  }

  Constructor(Algebraic caller, Method make, String name) {
    this.make = make;
    this.caller = caller;
    this.name = name;
    this.fields = extractFields(make);
  }

  String getName() {
    return name;
  }

  private Typable[] extractFields(Method make) {
    Class[] listClasses = make.getParameterTypes();
    Typable[] res = new Typable[listClasses.length];
    for (int i = 0; i < res.length; i++) {
      res[i] = caller.getScope().searchType(listClasses[i].getName());
    }
    return res;
  }

  Typable[] getFields() {
    return fields;
  }

  boolean isLocked() {
    return lock;
  }

  int getDimentionMax() {
    int res = 0;
    for (int i = 0; i < fields.length; i++) {
      res = Math.max(res, fields[i].getDimension());
    }
    return res;
  }

  int getSize() {
    return fields.length;
  }

  boolean isLeaf() {
    return fields.length == 0;
  }

  int distanceToReachLeaf() {
    if (fields.length == 0) {
      return 0;
    }
    lock = true;
    int res = 0;
    for (int i = 0; i < fields.length; i++) {
      Typable field = fields[i];
      int tmp = field.dstToLeaf();
//      if (!field.isDstToLeafDefined()) {
      if (tmp == -1) {
        // if one of the constructors of field is locked
        lock = false;
        return Integer.MAX_VALUE;
      }
      if (tmp == Integer.MAX_VALUE) {
        // if field can not lead to terminaison
        lock = false;
        return Integer.MAX_VALUE;
      }
      res = 1 + Math.max(res, tmp);
    }
    lock = false;
    return res;
  }

  Object make(Object[] args) {
    try {
      return make.invoke(null, args);
    } catch (IllegalAccessException ex) {
      System.err.println("Method " + make + " enforces Java language access control and the underlying method is inaccessible.");
      Logger.getLogger(Constructor.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalArgumentException ex) {
      System.err.println("Method " + make + " is not static.");
      Logger.getLogger(Constructor.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvocationTargetException ex) {
      System.err.println("Method " + make + " throws exception.");
      Logger.getLogger(Constructor.class.getName()).log(Level.SEVERE, null, ex);
    }
    throw new UnsupportedOperationException("ERROR");
  }

  @Override
  public String toString() {
    String res = "[";
    for (int i = 0; i < fields.length; i++) {
      Typable typable = fields[i];
      res += typable.getName() + ", ";
    }
    res += "]";
    return res;
  }

  ATerm[] giveATermDeps() {
    ATerm[] res = new ATerm[getSize()];
    for (int i = 0; i < res.length; i++) {
      res[i] = new ATerm(fields[i]);
    }
    return res;
  }
}