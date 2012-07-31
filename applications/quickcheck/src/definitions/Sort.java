package definitions;

import aterm.ATerm;
import aterm.ATermAppl;
import java.util.*;

/**
 * Represents implementation of any Sort type with Buildable formalism.
 *
 * @author hubert
 */
public class Sort implements Buildable {

  private String name;
  private List<Constructor> constructors;
  private Set<Buildable> dependences;
  private int minDepth;
  private int minNodes;
  private int dimension;

  public Sort(Scope scope, String name) {
    this.name = name;
    constructors = new ArrayList<Constructor>();
    dependences = new HashSet<Buildable>();
    minDepth = -1;
    minNodes = -1;
    dimension = -1;
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
    if (dimension != -1) {
      return dimension;
    }
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
    dimension = dim + add;
    return dimension;
  }

  @Override
  public int minimalSize(StrategyParameters.DistStrategy strategy) {
    switch (strategy) {
      case DEPTH:
        return minimalDepth();
      case NODES:
        return minimalNodes();
      default:
        throw new UnsupportedOperationException("strategy " + strategy + " not defined.");
    }
  }

  /**
   * This function give the size of the shortest path from here to a leaf in
   * terms of depth.
   *
   * @return size of the minimal path between here and a leaf. Returns
   * Integer.MAX_VALUE if no path reaches a leaf.
   */
  private int minimalDepth() {
    if (minDepth != -1 && minDepth != Integer.MAX_VALUE) {
      return minDepth;
    }
    int res = Integer.MAX_VALUE;
    for (Constructor constructor : constructors) {
      if (constructor.isLocked()) {
        //this case should not directly happen if minimalDepth() is called by user:
        //only during recursive call.
        //the returned value is sensless
        return -1;
      }
      res = Math.min(res, constructor.minimalDepth());
    }
    this.minDepth = res;
    return minDepth;
  }

  /**
   * This function give the size of the shortest path from here to a leaf in
   * terms of NODES.
   *
   * @return size of the minimal path between here and a leaf. Returns
   * Integer.MAX_VALUE if no path reaches a leaf.
   */
  private int minimalNodes() {
    if (minNodes != -1 && minNodes != Integer.MAX_VALUE) {
      return minNodes;
    }
    int res = Integer.MAX_VALUE;
    for (Constructor constructor : constructors) {
      if (constructor.isLocked()) {
        //this case should not directly happen if minimalNodes() is called by user:
        //only during recursive call.
        //the returned value is sensless
        return -1;
      }
      res = Math.min(res, constructor.minimalNodes());
    }
    this.minNodes = res;
    return minNodes;
  }

  Slot generateSlot(int n, StrategyParameters param) {
    if (this.minimalDepth() == Integer.MAX_VALUE) {
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
        if (!param.requireTermination(term, ns[i])) {
          req = new StrategyMakeMaxDimAtMost();
        } else {
          req = new StrategyMakeMinimal();
        }
        listHoles.addAll(req.fillATerm(term, ns[i], param));
        i++;
      }

      //remove newly filled terms
      listHoles.removeAll(toVisit);
    }
    return slot;
  }

  @Override
  public ATermAppl generate(int n) {
    StrategyParameters param = new StrategyParameters(
            StrategyParameters.DistStrategy.NODES,
            StrategyParameters.TerminationCriterion.POINT_OF_NO_RETURN);
    return generateSlot(n, param).toATerm();
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
  public Sort addConstructor(String name, Buildable... listTypes) {
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
  public Sort addConstructor(Buildable type) {
    return addConstructor(type.getName(), type);
  }

  /**
   * Choose randomly one of the constructor of the current type and check
   * whether choosen constructor is finite, that is, whether each of these
   * fields is finite.
   *
   * @return choosen constructor
   */
  Constructor chooseAnyFiniteConstructor() {
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
      if (cons.minimalDepth() != Integer.MAX_VALUE) {
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
      if (constructor.minimalDepth() != Integer.MAX_VALUE) {
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
      int m = constructor.minimalDepth();
      if (m == minimalDepth()) {
        minCons.add(constructor);
      }
    }
    if (minCons.isEmpty()) {
      throw new UnsupportedOperationException("Internal error happends when backtracking (" + getName() + " : " + minimalDepth() + ").");
    } else {
      return minCons.get((int) (Math.random() * minCons.size()));
    }
  }

  /**
   * Choose one the constructor that can terminate in the minimum of NODES.
   *
   * @return choosen constructor
   */
  Constructor chooseMinNodesConstructor() {
    ArrayList<Constructor> minCons = new ArrayList<Constructor>(constructors.size());
    for (Constructor constructor : constructors) {
      int m = constructor.minimalNodes();
      if (m == minimalDepth()) {
        minCons.add(constructor);
      }
    }
    if (minCons.isEmpty()) {
      throw new UnsupportedOperationException("Internal error happends when backtracking (" + getName() + " : " + minimalDepth() + ").");
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

  private Constructor getCurrentCons(ATerm term) {
    String nameTerm = term.toString();
    for (Constructor constructor : constructors) {
      if (nameTerm.startsWith(constructor.getName())) {
        return constructor;
      }
    }
    return null;
  }

  @Override
  public boolean isTypeOf(ATerm term) {
    return getCurrentCons(term) != null;
  }

  private class OneConstructorIterator implements Iterator<ATerm> {

    private ATerm current;
    //
    private Map<String, List<ATerm>> mapInitialFields;
    private Map<Buildable, Integer> mapCountConsFieldsType;
    private Buildable[] fieldsInit;

    public OneConstructorIterator(ATerm term, Constructor cons) {

      mapInitialFields = new HashMap<String, List<ATerm>>();
      mapCountConsFieldsType = new HashMap<Buildable, Integer>();
      Constructor constructor = getCurrentCons(term);
      fieldsInit = constructor.getFields();

      // build the map which contains all possible fields
      int nbrChildren = term.getChildCount();
      for (int i = 0; i < nbrChildren; i++) {
        ATerm field = (ATerm) term.getChildAt(i);
        String type = getType(field).getName();
        List<ATerm> occurences = mapInitialFields.get(type);
        if (occurences == null) {
          occurences = new LinkedList<ATerm>();
        }
        occurences.add(field);
      }

      // build the map which contains numbers of needed instances for each type.
      for (int i = 0; i < cons.getFields().length; i++) {
        Buildable buildable = cons.getFields()[i];
        Integer n = mapCountConsFieldsType.get(buildable);
        if (n == null) {
          n = 0;
        }
        mapCountConsFieldsType.put(buildable, n + 1);
      }
    }
    
    private Buildable getType(ATerm term) {
      for (int i = 0; i < fieldsInit.length; i++) {
        Buildable type = fieldsInit[i];
        if (type.isTypeOf(term)) {
          return type;
        }
      }
      throw new UnsupportedOperationException("Term " + term + " is not from known sub type of " + getName());
    }

    @Override
    public boolean hasNext() {
    }

    @Override
    public ATerm next() {
      if (current != null) {
        ATerm res = current;
        current = null;
        return res;
      } else if (hasNext()) {
        System.out.println("WARNING : the use of the methode next() is not preceded by hasNext().");
        ATerm res = current;
        current = null;
        return res;
      } else {
        throw new NoSuchElementException();
      }
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("This method must not be used.");
    }
  }

  @Override
  public Iterator<ATerm> lighten(ATerm term) {
    Constructor constructor = getCurrentCons(term);
    final List<Constructor> listSC = new LinkedList<Constructor>();
    for (Constructor cons : constructors) {
      if (cons.isSubCons(constructor)) {
        listSC.add(cons);
      }
    }

    return new Iterator<ATerm>() {

      List<Constructor> list = listSC;

      @Override
      public boolean hasNext() {
      }

      @Override
      public ATerm next() {
      }

      @Override
      public void remove() {
      }
    };
  }
}
