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

  private Constructor getMinimalConstructor() {
    for (Constructor constructor : listConstructors) {
      int m = constructor.distanceToReachLeaf();
      if (m == dstToLeaf()) {
        return constructor;
      }
    }
    throw new UnsupportedOperationException("Internal error happends when making backtraking.");
  }

  private Request[] spread(Request request, int size) {
    Request[] listRequests = new Request[size];
    for (int i = 0; i < listRequests.length; i++) {
      listRequests[i] = new MakeLeafStrategy(0);
    }
    if(size == 0){
      return listRequests;
    }
    int n = request.getCounter();
    while(n != 0){
      int index = (int) (Math.random()*size);
      listRequests[index].inc();
      n--;
    }
    return listRequests;
  }

  @Override
  public Object makeLeaf(Request request) {
    Constructor cons = getMinimalConstructor();
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

  @Override
  public Object generate(Request request) {
    int dst2leaf = dstToLeaf();
    if (dst2leaf == Integer.MAX_VALUE) {
      throw new UnsupportedOperationException("Type " + type + " is not finite.");
    }
    int n = request.getCounter();
    if (n < dst2leaf) {
      return makeLeaf(request);
    } else {
      // TODO Not supported yet
      throw new UnsupportedOperationException("Not yet implemented");
    }
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