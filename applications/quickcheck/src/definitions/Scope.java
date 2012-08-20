package definitions;

import java.util.HashSet;

/**
 * Contains all declared types.
 *
 * @author hubert
 */
public class Scope {

  private HashSet<Buildable> setOfTypes;

  public Scope() {
    setOfTypes = new HashSet<Buildable>();
  }

  void addType(Buildable t) {
    for (Buildable typable : setOfTypes) {
      if (typable.getName().equals(t.getName())) {
        throw new UnsupportedOperationException("Type " + t.getName() + " already defined");
      }
    }
    setOfTypes.add(t);
  }

  /**
   * This funtion sets dependences of all type in this scope.
   */
  public void setDependances() {
    boolean hasChanged = true;
    while (hasChanged) {
      hasChanged = false;
      for (Buildable type : setOfTypes) {
        hasChanged = hasChanged || type.updateDependences();
      }
    }
  }

  Buildable searchType(String name) {
    for (Buildable typable : setOfTypes) {
      if (typable.getName().equals(name)) {
        return typable;
      }
    }
    throw new UnsupportedOperationException("Type " + name + " does not exist in the current scope");
  }
}
