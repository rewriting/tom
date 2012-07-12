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
  private HashSet<Typable> listDependences;
  private int dstLeaf = -1;
  private String name;
  private Scope scope;
  @Deprecated
  private Class type;

  public Algebraic(Scope scope, String name) {
    this.scope = scope;
    this.name = name;
    listConstructors = new HashSet<Constructor>();
    listDependences = new HashSet<Typable>();
    scope.addType(this);
  }

  @Deprecated
  public Algebraic(Scope scope, Class type) {
    this.scope = scope;
    this.name = type.getName();
    this.type = type;
    listConstructors = new HashSet<Constructor>();
    listDependences = new HashSet<Typable>();
    scope.addType(this);
  }

  public Algebraic addConstructor(String name, Typable... listTypes) {
    listConstructors.add(new Constructor(this, listTypes, name));
    listDependences.addAll(Arrays.asList(listTypes));
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

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Scope getScope() {
    return scope;
  }

  @Override
  public boolean updateDependences() {
    boolean hasChanged = false;
    HashSet<Algebraic> depsClone = (HashSet<Algebraic>) listDependences.clone();
    for (Algebraic deps : depsClone) {
      hasChanged = hasChanged || !depsClone.containsAll(deps.listDependences);
      // TODO : utiliser la taille comme moyen de controle
      listDependences.addAll(deps.listDependences);
    }
    return hasChanged;
  }

  public boolean isRec() {
    return listDependences.contains(this);
  }

  @Override
  public int getDimension() {
    int dim = 0;
    int add = 0;
    if (isRec()) {
      add = 1;
    }
    for (Typable typable : listDependences) {
      boolean dependsOn = typable.getDependences().contains(this);
      if (!dependsOn) {
        dim = Math.max(dim, typable.getDimension());
      }
    }
    return dim + add;
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
  
//  public ATerm generate(int n) {
//    if (this.dstToLeaf() < n) {
//      return generate(new MakeAllStrategy(n));
//    } else {
//      return generate(new MakeLeafStrategy(n));
//    }
//  }

  @Override
  public ATerm generate(int n) {
    // TODO empecher les cas de non terminaison
    ATerm res = new ATerm(this);
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
    if (dstLeaf != -1) {
      return dstLeaf;
    }
    int res = Integer.MAX_VALUE;
    for (Constructor constructor : listConstructors) {
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
  @Deprecated
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
    listDependences.addAll(Arrays.asList(cons.getFields()));
    return this;
  }

  @Deprecated
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
    listDependences.addAll(Arrays.asList(cons.getFields()));
    return this;
  }

  @Override
  public HashSet<Typable> getDependences() {
    return listDependences;
  }
}