package definitions;

import java.util.HashSet;

/**
 *
 * @author hubert
 */
public class Scope {

  private HashSet<Typable> setOfTypes;

  public Scope() {
    setOfTypes = new HashSet<Typable>();
  }

  void addType(Typable t) {
    for (Typable typable : setOfTypes) {
      if (typable.getName().equals(t.getName())) {
        throw new UnsupportedOperationException("Type " + t.getName() + " already defined");
      }
    }
    setOfTypes.add(t);
  }

  public void setDependances() {
    boolean hasChanged = true;
    while (hasChanged) {
      hasChanged = false;
      for (Typable type : setOfTypes) {
        hasChanged = hasChanged || type.updateDependances();
      }
    }
  }

  Typable searchType(String name) {
    for (Typable typable : setOfTypes) {
      if (typable.getName().equals(name)) {
        return typable;
      }
    }
    throw new UnsupportedOperationException("Type " + name + " does not exist in the current scope");
  } 
}
