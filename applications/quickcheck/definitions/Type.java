package definitions;

import java.util.HashSet;

/**
 *
 * @author hubert
 */
public class Type {

  private HashSet<Constructor> listConstructors;
  private HashSet<Type> dependances;

  private Type() {
    listConstructors = new HashSet<Constructor>();
    dependances = new HashSet<Type>();
  }

  public static Type declare() {
    return new Type();
  }

  public Type addConstructor(Field[] listFields) {
    listConstructors.add(new Constructor(listFields));
    return this;
  }
  
  

  private boolean isRec() {
    for (Constructor constructor : listConstructors) {
      if (constructor.isRec()) {
        return true;
      }
    }
    return false;
  }

  public int getDimention() {
    int dim = 0;
    int add = 0;
    if (isRec()) {
      add = 1;
    }
    for (Constructor constructor : listConstructors) {
      dim = Math.max(dim, constructor.getDimention());
    }
    return dim + add;
  }

  boolean checkIfConstructorHasFieldOfType(Type t) {
    for (Constructor constructor : listConstructors) {
      if (constructor.hasFieldOfType(t)) {
        return true;
      }
    }
    return false;
  }

  public boolean isRec2() {
    return checkIfConstructorHasFieldOfType(this);
  }
}