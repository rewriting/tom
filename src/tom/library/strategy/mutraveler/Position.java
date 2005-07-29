package tom.library.strategy.mutraveler;

import java.util.LinkedList;
import java.util.Iterator;

public class Position {
  private LinkedList list = null;
   
  public Position() {
    this.list = new LinkedList();
  }

  private boolean hasPosition() {
    return list!=null;
  }
  
  public boolean isEmpty() {
    return hasPosition() && list.isEmpty();
  }
  
  public void up() {
    list.removeLast();
  }

  public void down(int subtree) {
    list.addLast(new Integer(subtree));
  }

  public Iterator iterator() {
    return list.iterator();
  }

  public String toString() {
    return list.toString();
  }
}
