package tom.library.strategy.mutraveler;

import java.util.LinkedList;

public class Position {
  private LinkedList list = null;
   
  public Position() {
      this.list = new LinkedList();
  }

  public void up() {
    list.removeLast();
  }

  public void down(int subtree) {
    list.addLast(new Integer(subtree));
  }
}
