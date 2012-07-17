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
  public int depthToLeaf() {
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
  
  Slot generateSlot(int n){
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
      int[] listSpread = Random.pile(n, toVisit.size());

      //fill each maximal dimension term
      int i = 0;
      for (Slot term : toVisit) {
        Strategy req;
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
    return slot;
  }

  @Override
  public ATermAppl generate(int n) {
    return generateSlot(n).toATerm();
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
      if (cons.depthToLeaf() != Integer.MAX_VALUE) {
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