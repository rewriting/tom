package definitions;

import java.util.HashSet;

/**
 *
 * @author hubert
 */
public class Scope {

  private HashSet<Algebraic> setOfTypes;

  public Scope() {
    setOfTypes = new HashSet<Algebraic>();
  }
  
  void addType(Algebraic t){
    setOfTypes.add(t);
  }
  
  public void setDependances(){
    boolean hasChanged = true;
    while (hasChanged) {      
      hasChanged = false;
      for (Algebraic type : setOfTypes) {
        hasChanged = hasChanged || type.updateDependances();
      }
    }
  }
  
}
