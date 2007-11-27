package tom.library.sl;

/**
 * <code>MuVar(v)</code> always raises a VisitFailure exception. 
 * <p>
 * Basic visitor combinator used to build recursive visitors
 * <p>
 */

public class MuVar extends AbstractStrategy {
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
  
  public final Object visitLight(Object any,Introspector i) throws VisitFailure {
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
  
  protected void setInstance(Strategy v) {
    this.instance = v;
  }
  
  protected void setName(String name) {
    this.name = name;
  }

  protected final boolean isExpanded() {
    return instance != null;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return "[" + name + "," + instance + "]";
  }
}
