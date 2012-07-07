package definitions;

import java.util.Arrays;
import java.util.HashSet;
import tom.library.sl.Strategy;

/**
 *
 * @author hubert
 */
public class Algebraic implements Typable {

  private HashSet<Constructor> listConstructors;
  private HashSet<Typable> listDependances;

  public Algebraic(Scope scope) {
    listConstructors = new HashSet<Constructor>();
    listDependances = new HashSet<Typable>();
    scope.addType(this);
  }

  public Algebraic addConstructor(Typable[] listTypes) {
    listConstructors.add(new Constructor(this, listTypes));
    listDependances.addAll(Arrays.asList(listTypes));
    return this;
  }

  /**
   *
   * @return true if no changes were done
   */
  boolean updateDependances() {
    boolean hasChanged = false;
    HashSet<Algebraic> depsClone = (HashSet<Algebraic>) listDependances.clone();
    for (Algebraic deps : depsClone) {
      hasChanged = hasChanged || !depsClone.containsAll(deps.listDependances);
      listDependances.addAll(deps.listDependances);
    }
    return hasChanged;
  }

  @Override
  public boolean isRec() {
    return listDependances.contains(this);
  }

  @Override
  public int getDimention() {
    int dim = 0;
    int add = 0;
    if (isRec()) {
      add = 1;
    }
    for (Typable type : listDependances) {
      if (!type.dependsOn(this)) {
        dim = Math.max(dim, type.getDimention());
      }
    }
    return dim + add;
  }

  @Override
  public boolean dependsOn(Typable t) {
    return listDependances.contains(t);
  }

  @Override
  public Strategy makeGenerator(Request request) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int isLeafable() {
    int res = Integer.MAX_VALUE;
    for (Constructor constructor : listConstructors) {
      res = Math.min(res, constructor.distanceToReachLeaf());
    }
    return res;
  }
}