package definitions;

import java.lang.reflect.Method;

/**
 *
 * @author hubert
 */
class Constructor {

  private Algebraic caller;
  private Typable[] fields;
  private boolean lock = false;
  private Method make;

  Constructor(Algebraic caller, Typable[] fields) {
    this.caller = caller;
    this.fields = fields;
  }

  Constructor(Algebraic caller, Method make) {
    this.make = make;
    this.caller = caller;
    this.fields = extractFields(make);
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
      res = Math.max(res, fields[i].getDimention());
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
      if (!field.isDstToLeafDefined()) {
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
}