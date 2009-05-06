package tom.library.sl;

/**
 * <p>
 * Basic strategy combinator with one argument <code>name</code> 
 * which denotes the name of the mu-variable
 * This basic visitor combinator used to build recursive visitors
 * After the mu-expansion, the private variable <code>instance</code>
 * is a pointer to the strategy to execute
 * <p>
 */

public class MuVar extends AbstractStrategyCombinator {
  private Strategy instance = null;
  protected String name;
  
  public MuVar(String name) {
    // make sure the MuVar is seen as a leaf for all visitors
    initSubterm();
    this.name = name;
  }

  public boolean equals(Object o) {
    if(o instanceof MuVar) {
      MuVar muVar = (MuVar)o;
      if(muVar != null) {
        if(name != null) {
          return name.equals(muVar.name);
        } else {
          return name==muVar.name && instance==muVar.instance;
        }
      }
    }
    return false;
  }

  public int hashCode() {
    if(name!=null) {
      return name.hashCode();
    } else {
      return instance.hashCode();
    }
  }
  
  public final <T> T visitLight(T any,Introspector i) throws VisitFailure {
    if(instance != null) {
      return instance.visitLight(any,i);
    } else {
      throw new VisitFailure();
    }
  }

  public int visit(Introspector i) {
    if(instance != null) {
      return instance.visit(i);
    } else {
      //setStatus(Environment.FAILURE);
      return Environment.FAILURE;
    }
  }

  public Strategy getInstance() {
    return instance;
  }
  
  public void setInstance(Strategy v) {
    this.instance = v;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public final boolean isExpanded() {
    return instance != null;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return "[" + name + "," + instance + "]";
  }
}
