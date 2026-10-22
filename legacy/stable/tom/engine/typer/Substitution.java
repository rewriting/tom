

























package tom.engine.typer;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;



import tom.engine.adt.tomtype.types.*;



public class Substitution {
  private HashMap<TomType,TomType> substitutions;

  public Substitution() {
    this.substitutions = new HashMap<TomType,TomType>();
  }

  public TomType get(TomType key) {
    return substitutions.get(key);
  }

  public boolean containsKey(TomType key) {
    return substitutions.containsKey(key);
  }

  
  public void addSubstitution(TomType key, TomType value) {
    

    TomType newValue = substitutions.get(value);
    if(newValue == null) {
      newValue = value;
    }
    
    
    
    
    substitutions.put(key,newValue);

    
    for (TomType currentKey : substitutions.keySet()) {
      if (substitutions.get(currentKey) == key) {
          substitutions.put(currentKey,newValue);
        }
      }
  }
}

