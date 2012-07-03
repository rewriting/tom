package definitions;

import java.util.HashSet;

/**
 *
 * @author hubert
 */
public class Type {

  private HashSet<Constructor> listConstructors;
  private HashSet<Type> listDependances;

  public Type(Scope scope) {
    listConstructors = new HashSet<Constructor>();
    listDependances = new HashSet<Type>();
    scope.addType(this);
  }

  /**
   *
   * @param listFields
   * @return
   * @deprecated
   */
  @Deprecated
  public Type addConstructor(Field[] listFields) {
    listConstructors.add(new Constructor(listFields));
    return this;
  }

  public Type addConstructor(Type[] listTypes) {
    listConstructors.add(new Constructor(this, listTypes));
    for (int i = 0; i < listTypes.length; i++) {
      listDependances.add(listTypes[i]);
    }
    return this;
  }

  /**
   *
   * @return true if no changes were done
   */
  boolean updateDependances() {
    boolean hasChanged = false;
    HashSet<Type> depsClone = (HashSet<Type>) listDependances.clone();
    for (Type deps : depsClone) {
      hasChanged = hasChanged || !depsClone.containsAll(deps.listDependances);
      listDependances.addAll(deps.listDependances);
    }
    return hasChanged;
  }

  public boolean isRec() {
    return listDependances.contains(this);
  }

  public int getDimention() {
    int dim = 0;
    int add = 0;
    if (isRec()) {
      add = 1;
    }
    for (Type type : listDependances) {
      if (!type.listDependances.contains(this)) {
        dim = Math.max(dim, type.getDimention());
      }
    }

    return dim + add;
  }

  @Deprecated
  boolean checkIfConstructorHasFieldOfType(Type t) {
    for (Constructor constructor : listConstructors) {
      if (constructor.hasFieldOfType(t)) {
        return true;
      }
    }
    return false;
  }

  @Deprecated
  public boolean isRec2() {
    return checkIfConstructorHasFieldOfType(this);
  }
}