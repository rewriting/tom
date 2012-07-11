package definitions;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

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
  public Algebraic addConstructor(String name, Typable... listTypes) {
    listConstructors.add(new Constructor(this, listTypes, name));
    listDependances.addAll(Arrays.asList(listTypes));
    return this;
  }

  boolean checkLink(Constructor cons) {
    return listConstructors.contains(cons);
  }

  Constructor chooseConstructor() {
    int choice = (int) (Math.random() * listConstructors.size());
    int i = 0;
    for (Constructor constructor : listConstructors) {
      if (i == choice) {
        return constructor;
      }
      i++;
    }
    throw new UnsupportedOperationException("ERROR");
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
      // TODO : utiliser la taille comme moyen de controle
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
    for (Typable typable : listDependances) {
      if (!typable.dependsOn(this)) {
        dim = Math.max(dim, typable.getDimention());
      }
    }
    return dim + add;
  }

  @Override
  public boolean dependsOn(Typable t) {
    return listDependances.contains(t);
  }

  Constructor chooseMinimalConstructor() {
    //TODO improve choice randomly
    for (Constructor constructor : listConstructors) {
      int m = constructor.distanceToReachLeaf();
      if (m == dstToLeaf()) {
        return constructor;
      }
    }
    throw new UnsupportedOperationException("Internal error happends when backtraking.");
  }

  @Deprecated
  private Request[] spread(Request request, int size) {
    Request[] listRequests = new Request[size];
    for (int i = 0; i < listRequests.length; i++) {
      listRequests[i] = new MakeLeafStrategy(0);
    }
    if (size == 0) {
      return listRequests;
    }
    int n = request.getCounter();
    while (n != 0) {
      int index = (int) (Math.random() * size);
      listRequests[index].inc();
      n--;
    }
    return listRequests;
  }

  private int[] spread(int n, int size) {
    int[] res = new int[size];
    for (int i = 0; i < res.length; i++) {
      res[i] = 0;
    }
    if (size == 0) {
      return res;
    }
    while (n != 0) {
      int index = (int) (Math.random() * size);
      res[index]++;
      n--;
    }
    return res;
  }

  @Override
  @Deprecated
  public Object makeLeaf(Request request) {
    Constructor cons = chooseMinimalConstructor();
    Typable[] fields = cons.getFields();
    Request[] listRequests = spread(request, fields.length);
    Object[] branches = new Object[fields.length];
    for (int i = 0; i < fields.length; i++) {
      Typable typable = fields[i];
      Request req = listRequests[i];
      branches[i] = typable.makeLeaf(req);
    }
    return cons.make(branches);
  }

  public ATerm generate(int n) {
    if (this.dstToLeaf() < n) {
      return generate(new MakeAllStrategy(n));
    } else {
      return generate(new MakeLeafStrategy(n));
    }
  }

  @Override
  public ATerm generate(Request request) {
    // TODO verifier les cas de non terminaison
    int n = request.getCounter();
    ATerm res = new ATerm(this);
//    res.setRequest(request);
    HashSet<ATerm> listHoles = new HashSet<ATerm>();
    listHoles.add(res);
    while (!listHoles.isEmpty()) {
      
      //retrieve set of maximal dimension terms
      int dimMax = 0;
      HashSet<ATerm> toVisit = new HashSet<ATerm>();
      for (ATerm term : listHoles) {
        int d = term.getDimention();
        if (d > dimMax) {
          dimMax = d;
          toVisit = new HashSet<ATerm>();
        }
        if (d == dimMax) {
          toVisit.add(term);
        }
      }

      //spread n across maximal dimention terms
      int[] listSpread = spread(n, toVisit.size());

      //fill each maximal dimension term
      int i = 0;
      for (ATerm term : toVisit) {
        Request req;
        if (term.getDstToLeaf() < listSpread[i]) {
          req = new MakeAllStrategy(listSpread[i]);
          if (term.getRequest() != null) {
            throw new UnsupportedOperationException("Request of term should not be defined here.");
          }
          term.setRequest(req); // normaly useless...
        } else {
          req = new MakeLeafStrategy(listSpread[i]);
          if (term.getRequest() != null) {
            throw new UnsupportedOperationException("Request of term should not be defined here.");
          }
          term.setRequest(req); // normaly useless...
        }
        listHoles.addAll(req.fillATerm(term));
        i++;
      }

      //remove newly filled terms
      listHoles.removeAll(toVisit);
    }
    return res;
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
  public Algebraic addConstructor(String name, Class classe) {
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
    Constructor cons = new Constructor(this, make, name);
    listConstructors.add(cons);
    listDependances.addAll(Arrays.asList(cons.getFields()));
    return this;
  }

  public Algebraic addConstructor(String name, Class classe, String pattern) {
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
    Constructor cons = new Constructor(this, make, name);
    listConstructors.add(cons);
    listDependances.addAll(Arrays.asList(cons.getFields()));
    return this;
  }
}