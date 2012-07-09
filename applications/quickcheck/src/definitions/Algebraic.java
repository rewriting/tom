package definitions;

import java.lang.reflect.Method;
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
  private int dstLeaf = Integer.MAX_VALUE;
  private boolean dstIsDefined = false;
  @Deprecated
  private String name;
  private Scope scope;
  private Class type;

  @Deprecated
  public Algebraic(Scope scope, String name) {
    this.scope = scope;
    this.name = name;
//    try {
//      this.type = Class.forName(name);
//    } catch (ClassNotFoundException ex) {
//      Logger.getLogger(Algebraic.class.getName()).log(Level.SEVERE, null, ex);
//    }
    listConstructors = new HashSet<Constructor>();
    listDependances = new HashSet<Typable>();
    scope.addType(this);
  }

  public Algebraic(Scope scope, Class type) {
    this.scope = scope;
    this.name = type.getName();
    this.type = type;
    listConstructors = new HashSet<Constructor>();
    listDependances = new HashSet<Typable>();
    scope.addType(this);
  }

  @Deprecated
  public Algebraic addConstructor(Typable... listTypes) {
    listConstructors.add(new Constructor(this, listTypes));
    listDependances.addAll(Arrays.asList(listTypes));
    return this;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Scope getScope() {
    return scope;
  }

  /**
   * Thie function return true if and only if dstToLeaf() has been already
   * called for the current Algebraic Typable.
   *
   * @return
   */
  @Override
  public boolean isDstToLeafDefined() {
    return dstIsDefined;
  }

  @Override
  public boolean updateDependances() {
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
  public Object generate(Request request) {
    // TODO Not supported yet
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Strategy makeGenerator(Request request) {
    // TODO Not supported yet
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int dstToLeaf() {
    if (this.dstIsDefined) {
      return dstLeaf;
    }
    int res = Integer.MAX_VALUE;
    for (Constructor constructor : listConstructors) {
      if (constructor.isLocked()) {
        //this case should not directly happen if dstToLeaf() is called by user:
        //only during recursive call.
        //the returned value is sensless
        return Integer.MAX_VALUE;
      }
      res = Math.min(res, constructor.distanceToReachLeaf());
    }
    this.dstIsDefined = true;
    this.dstLeaf = res;
    return dstLeaf;
  }

  @Override
  public String toString() {
    String res = this.getName() + " : \n";
    for (Constructor constructor : listConstructors) {
      res += "\t" + constructor + "\n";
    }
    return res;
  }

  /*
   * =========================== USING META-TYPAGE ============================
   */
  /**
   * This methode only work with Gom pattern classes. Indeed, method make()
   * constructed by using Gom is searched in order to build Constructor
   *
   * @param classe class following Gom pattern definition
   * @return
   */
  public Algebraic addConstructor(Class classe) {
    String pattern = "make";
    Method[] listMethods = classe.getDeclaredMethods();
    Method make = null;
    for (int i = 0; i < listMethods.length; i++) {
      Method method = listMethods[i];
      if (method.getName().equals(pattern)) {
        make = method;
        break;
      }
      if (i == listMethods.length - 1) {
        throw new UnsupportedOperationException("Method " + pattern + "() was not found in " + classe);
      }
    }
    Constructor cons = new Constructor(this, make);
    listConstructors.add(cons);
    listDependances.addAll(Arrays.asList(cons.getFields()));
    return this;
  }

  public Algebraic addConstructor(Class classe, String pattern) {
    Method[] listMethods = classe.getDeclaredMethods();
    Method make = null;
    for (int i = 0; i < listMethods.length; i++) {
      Method method = listMethods[i];
      if (method.getName().equals(pattern)) {
        make = method;
        break;
      }
      if (i == listMethods.length - 1) {
        throw new UnsupportedOperationException("Method " + pattern + "() was not found in " + classe);
      }
    }
    Constructor cons = new Constructor(this, make);
    listConstructors.add(cons);
    listDependances.addAll(Arrays.asList(cons.getFields()));
    return this;
  }
}