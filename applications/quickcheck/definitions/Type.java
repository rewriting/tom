package definitions;

import java.util.LinkedList;

public class Type {

  private LinkedList<Constructor> listConstructors;
  
  private Type(){
    listConstructors = new LinkedList<Constructor>();
  }
  
  public static Type declare(){
    return new Type();
  }
  
  public Type addConstructor(Field[] listFields){
    listConstructors.add(new Constructor(listFields));
    return this;
  }

  private boolean isRec() {
    for (Constructor constructor : listConstructors) {
      if (constructor.isRec()) {
        return true;
      }
    }
    return false;
  }

  public int getDimention() {
    int dim = 0;
    int add = 0;
    if (isRec()) {
      add = 1;
    }
    for (Constructor constructor : listConstructors) {
      dim = Math.max(dim, constructor.getDimention());
    }
    return dim + add;
  }
}