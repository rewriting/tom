package definitions;

import java.lang.reflect.Method;
import java.util.*;

/**
 *
 * @author hubert
 */
public class Algebraic implements Typable {

  private String name;
  private List<Constructor> constructors;
  private Set<Typable> dependences;
  private int dstLeaf;
  private Scope scope;

  public Algebraic(Scope scope, String name) {
    this.name = name;
    constructors = new ArrayList<Constructor>();
    dependences = new HashSet<Typable>();
    dstLeaf = -1;
    this.scope = scope;
    scope.addType(this);
  }

  @Deprecated
  public Algebraic(Scope scope, Class type) {
    dstLeaf = -1;
    this.scope = scope;
    this.name = type.getName();
    constructors = new ArrayList<Constructor>();
    dependences = new HashSet<Typable>();
    scope.addType(this);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Set<Typable> getDependences() {
    return dependences;
  }

  @Override
  public int getDimension() {
    int dim = 0;
    int add = 0;
    if (isRec()) {
      add = 1;
    }
    for (Typable typable : dependences) {
      boolean dependsOn = typable.getDependences().contains(this);
      if (!dependsOn) {
        dim = Math.max(dim, typable.getDimension());
      }
    }
    return dim + add;
  }

  @Override
  public int dstToLeaf() {
    if (dstLeaf != -1 && dstLeaf != Integer.MAX_VALUE) {
      return dstLeaf;
    }
    int res = Integer.MAX_VALUE;
    for (Constructor constructor : constructors) {
      if (constructor.isLocked()) {
        //this case should not directly happen if dstToLeaf() is called by user:
        //only during recursive call.
        //the returned value is sensless
        return -1;
      }
      res = Math.min(res, constructor.distanceToReachLeaf());
    }
    this.dstLeaf = res;
    return dstLeaf;
  }

  @Override
  public Slot generate(int n) {
    if (this.dstToLeaf() == Integer.MAX_VALUE) {
      throw new UnsupportedOperationException("Type " + this.getName() + " does not terminate.");
    }
    Slot res = new Slot(this);
    HashSet<Slot> listHoles = new HashSet<Slot>();
    listHoles.add(res);
    while (!listHoles.isEmpty()) {

      //retrieve set of maximal dimension terms
      int dimMax = 0;
      HashSet<Slot> toVisit = new HashSet<Slot>();
      for (Slot term : listHoles) {
        int d = term.getDimension();
        if (d > dimMax) {
          dimMax = d;
          toVisit = new HashSet<Slot>();
        }
        if (d == dimMax) {
          toVisit.add(term);
        }
      }

      //spread n across maximal dimention terms
      int[] listSpread = Random.pile(n, toVisit.size());

      //fill each maximal dimension term
      int i = 0;
      for (Slot term : toVisit) {
        Request req;
        int dst = term.getDstToLeaf();
        if (dst < listSpread[i]) {
          req = new MakeAllStrategy();
        } else {
          req = new MakeLeafStrategy();
        }
        listHoles.addAll(req.fillATerm(term, listSpread[i]));
        i++;
      }

      //remove newly filled terms
      listHoles.removeAll(toVisit);
    }
    return res;
  }

  @Override
  public boolean updateDependences() {
    Set<Typable> depsClone = new HashSet<Typable>(dependences);
    for (Typable deps : depsClone) {
      dependences.addAll(deps.getDependences());
    }
    //return true if there were changes
    return dependences.size() != depsClone.size();
  }

  /**
   * This function make it possible to add constructors of the given type.
   *
   * @param name name of the constructor
   * @param listTypes types of the fields of the constructor
   * @return
   */
  public Algebraic addConstructor(String name, Typable... listTypes) {
    constructors.add(new Constructor(name, this, listTypes));
    dependences.addAll(Arrays.asList(listTypes));
    return this;
  }

  Scope getScope() {
    return scope;
  }

  /**
   * Choose randomly one of the constructors of the current type. This function
   * does not check whether choosen constructor is finite, that is, whether each
   * of these fields is finite.
   *
   * @return choosen constructor
   * @deprecated
   */
  @Deprecated
  Constructor chooseConstructor() {
    if (constructors.isEmpty()) {
      throw new UnsupportedOperationException("No constructors");
    }
    int choice = (int) (Math.random() * constructors.size());
    return constructors.get(choice);
  }

  /**
   * Choose randomly one of the constructor of the current type and check
   * whether choosen constructor is finite, that is, whether each of these
   * fields is finite.
   *
   * @return choosen constructor
   */
  Constructor chooseFiniteConstructor() {
    if (constructors.isEmpty()) {
      throw new UnsupportedOperationException("No constructors");
    }
    ArrayList<Constructor> copy = new ArrayList<Constructor>(constructors.size());
    for (Constructor constructor : constructors) {
      copy.add(constructor);
    }
    while (!copy.isEmpty()) {
      int choice = (int) (Math.random() * copy.size());
      Constructor cons = copy.get(choice);
      if (cons.distanceToReachLeaf() != Integer.MAX_VALUE) {
        return cons;
      }
      copy.remove(cons);
    }
    throw new UnsupportedOperationException("No constructors are finite");
  }

  /**
   * Choose one the constructor that can terminate in the minimum of steps.
   *
   * @return choosen constructor
   */
  Constructor chooseMinimalConstructor() {
    ArrayList<Constructor> minCons = new ArrayList<Constructor>(constructors.size());
    for (Constructor constructor : constructors) {
      int m = constructor.distanceToReachLeaf();
      if (m == dstToLeaf()) {
        minCons.add(constructor);
      }
    }
    if (minCons.isEmpty()) {
      throw new UnsupportedOperationException("Internal error happends when backtracking (" + getName() + " : " + dstToLeaf() + ").");
    } else {
      return minCons.get((int) (Math.random() * minCons.size()));
    }
  }

  /**
   * Check whether the current type is recursive, that is, whether it is
   * contained in its own dependences. This function cannot be used till
   * dependances are not set
   *
   * @see Scope#setDependances() 
   * @return true if type is recursive.
   */
  public boolean isRec() {
    return dependences.contains(this);
  }

  @Override
  public String toString() {
    String res = this.getName() + " : \n";
    for (Constructor constructor : constructors) {
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
   * @deprecated 
   */
  @Deprecated
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
    Constructor cons = new Constructor(classe.getSimpleName(), this, make);
    constructors.add(cons);
    dependences.addAll(Arrays.asList(cons.getFields()));
    return this;
  }

  /**
   * The method make it possible to add constructor by using java class of this constructor. 
   * @param name
   * @param classe
   * @param pattern
   * @return
   * @deprecated
   */
  @Deprecated
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
    Constructor cons = new Constructor(classe.getSimpleName(), this, make);
    constructors.add(cons);
    dependences.addAll(Arrays.asList(cons.getFields()));
    return this;
  }
}