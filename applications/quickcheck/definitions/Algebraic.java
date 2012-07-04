package definitions;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author hubert
 */
public class Algebraic implements Typable {

  public static final Typable integer = new Typable() {

    @Override
    public boolean isRec() {
      return false;
    }

    @Override
    public int getDimention() {
      return 0;
    }

    @Override
    public boolean dependsOn(Typable t) {
      return false;
    }
  };
  public static final Typable string = new Typable() {

    @Override
    public boolean isRec() {
      return false;
    }

    @Override
    public int getDimention() {
      return 1;
    }

    @Override
    public boolean dependsOn(Typable t) {
      return false;
    }
  };
  private HashSet<Constructor> listConstructors;
  private HashSet<Typable> listDependances;

  private Algebraic() {
  }

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
}