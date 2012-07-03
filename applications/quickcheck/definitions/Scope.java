package definitions;

import java.util.HashSet;

/**
 *
 * @author hubert
 */
public class Scope {

  private HashSet<Type> setOfTypes;

  public Scope() {
    setOfTypes = new HashSet<Type>();
  }
  
  void addType(Type t){
    setOfTypes.add(t);
  }
  
  public void setDependances(){
    throw new UnsupportedOperationException("not yet implemented");
  }
  
}
