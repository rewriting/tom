package definitions;

import aterm.ATermAppl;
import java.util.*;

/**
 * Represents implementation of any algebraic type with Buildable formalism.
 *
 * @author hubert
 */
public class Algebraic implements Buildable {

  private String name;
  private List<Constructor> constructors;
  private Set<Buildable> dependences;
  private int dstLeaf;

  public Algebraic(Scope scope, String name) {
    this.name = name;
    constructors = new ArrayList<Constructor>();
    dependences = new HashSet<Buildable>();
    dstLeaf = -1;
    scope.addType(this);
  }

  List<Constructor> getConstructors() {
    return constructors;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Set<Buildable> getDependences() {
    return dependences;
  }

  @Override
  public int getDimension() {
    int dim = 0;
    int add = 0;
    if (isRec()) {
      add = 1;
    }
    for (Buildable typable : dependences) {
      boolean dependsOn = typable.getDependences().contains(this);
      if (!dependsOn) {
        dim = Math.max(dim, typable.getDimension());
      }
    }
    return dim + add;
  }
  
  @Override
  public int distToLeaf(int strategy){
    switch(strategy){
      case DEPTH:
        return depthToLeaf();
      case STEPS:
        return stepsToLeaf();
      default :
        throw new UnsupportedOperationException("strategy " + strategy + " not defined.");
    }
  }

  /**
   * This function give the size of the shortest path from here to a leaf in
   * terms of depth.
   *
   * @return size of the minimal path between here and a leaf. Returns
   * Integer.MAX_VALUE if no path reaches a leaf.
   * @see Buildable#stepsToLeaf()
   */
  private int depthToLeaf() {
    if (dstLeaf != -1 && dstLeaf != Integer.MAX_VALUE) {
      return dstLeaf;
    }
    int res = Integer.MAX_VALUE;
    for (Constructor constructor : constructors) {
      if (constructor.isLocked()) {
        //this case should not directly happen if depthToLeaf() is called by user:
        //only during recursive call.
        //the returned value is sensless
        return -1;
      }
      res = Math.min(res, constructor.depthToLeaf());
    }
    this.dstLeaf = res;
    return dstLeaf;
  }

  /**
   * This function give the size of the shortest path from here to a leaf in
   * terms of steps.
   *
   * @return size of the minimal path between here and a leaf. Returns
   * Integer.MAX_VALUE if no path reaches a leaf.
   * @see Buildable#depthToLeaf()
   */
  private int stepsToLeaf() {
    if (dstLeaf != -1 && dstLeaf != Integer.MAX_VALUE) {
      return dstLeaf;
    }
    int res = Integer.MAX_VALUE;
    for (Constructor constructor : constructors) {
      if (constructor.isLocked()) {
        //this case should not directly happen if stepsToLeaf() is called by user:
        //only during recursive call.
        //the returned value is sensless
        return -1;
      }
      res = Math.min(res, constructor.stepsToLeaf());
    }
    this.dstLeaf = res;
    return dstLeaf;
  }

  Slot generateSlot(int n, int distStrategy) {
    if (this.depthToLeaf() == Integer.MAX_VALUE) {
      throw new UnsupportedOperationException("Type " + this.getName() + " does not terminate.");
    }
    Slot slot = new Slot(this);
    HashSet<Slot> listHoles = new HashSet<Slot>();
    listHoles.add(slot);
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
      int[] ns = Random.pile(n, toVisit.size());

      //fill each maximal dimension term
      int i = 0;
      for (Slot term : toVisit) {
        Strategy req;
        int dst = term.distToLeaf(distStrategy);
        if (dst < ns[i]) {
          req = new MakeMaxDimStrategy();
        } else {
          req = new BacktrackDepthStrategy();
        }
        listHoles.addAll(req.fillATerm(term, ns[i], distStrategy));
        i++;
      }

      //remove newly filled terms
      listHoles.removeAll(toVisit);
    }
    return slot;
  }

  @Override
  public ATermAppl generate(int n) {
    return generateSlot(n, Buildable.DEPTH).toATerm();
  }

  @Override
  public boolean updateDependences() {
    Set<Buildable> depsClone = new HashSet<Buildable>(dependences);
    for (Buildable deps : depsClone) {
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
   * @return type with new constructor
   */
  public Algebraic addConstructor(String name, Buildable... listTypes) {
    constructors.add(new Constructor(name, listTypes));
    dependences.addAll(Arrays.asList(listTypes));
    return this;
  }

  /**
   * This function make it possible to add constructors of the given type. In
   * this case, giving a name to the constructor can be canonic: its name can be
   * the same as the name of its field.
   *
   * @param type type of the field of the constructor
   * @return type with new constructor
   */
  public Algebraic addConstructor(Buildable type) {
    return addConstructor(type.getName(), type);
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
      throw new UnsupportedOperationException("No constructor");
    }
    ArrayList<Constructor> copy = new ArrayList<Constructor>(constructors.size());
    for (Constructor constructor : constructors) {
      copy.add(constructor);
    }
    while (!copy.isEmpty()) {
      int choice = (int) (Math.random() * copy.size());
      Constructor cons = copy.get(choice);
      if (cons.depthToLeaf() != Integer.MAX_VALUE) {
        return cons;
      }
      copy.remove(cons);
    }
    throw new UnsupportedOperationException("No constructor is finite");
  }

  Constructor chooseMaxDimConstructor() {
    if (constructors.isEmpty()) {
      throw new UnsupportedOperationException("No constructor");
    }
    ArrayList<Constructor> maxCons = new ArrayList<Constructor>(constructors.size());
    int max = 0;
    for (Constructor constructor : constructors) {
      if (constructor.depthToLeaf() != Integer.MAX_VALUE) {
        int dim = constructor.getDimention();
        if (dim > max) {
          maxCons = new ArrayList<Constructor>();
          max = dim;
        }
        if (dim == max) {
          maxCons.add(constructor);
        }
      }
    }
    if (maxCons.isEmpty()) {
      throw new UnsupportedOperationException("No constructor is finite");
    } else {
      return maxCons.get((int) (Math.random() * maxCons.size()));
    }
  }

  /**
   * Choose one the constructor that can terminate in the minimum of depth.
   *
   * @return choosen constructor
   */
  Constructor chooseMinDepthConstructor() {
    ArrayList<Constructor> minCons = new ArrayList<Constructor>(constructors.size());
    for (Constructor constructor : constructors) {
      int m = constructor.depthToLeaf();
      if (m == depthToLeaf()) {
        minCons.add(constructor);
      }
    }
    if (minCons.isEmpty()) {
      throw new UnsupportedOperationException("Internal error happends when backtracking (" + getName() + " : " + depthToLeaf() + ").");
    } else {
      return minCons.get((int) (Math.random() * minCons.size()));
    }
  }

  /**
   * Choose one the constructor that can terminate in the minimum of steps.
   *
   * @return choosen constructor
   */
  Constructor chooseMinStepsConstructor() {
    ArrayList<Constructor> minCons = new ArrayList<Constructor>(constructors.size());
    for (Constructor constructor : constructors) {
      int m = constructor.stepsToLeaf();
      if (m == depthToLeaf()) {
        minCons.add(constructor);
      }
    }
    if (minCons.isEmpty()) {
      throw new UnsupportedOperationException("Internal error happends when backtracking (" + getName() + " : " + depthToLeaf() + ").");
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
}