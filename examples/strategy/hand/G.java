package strategy.hand;

public class G extends Term {
  public Slot s;
  public G(strategy.hand.Slot s) {
    this.s = s;
  }
  public int getChildCount() { return 1; }

  public jjtraveler.Visitable getChildAt(int index) {
    if(index == 0) {
      return s;
    }
    throw new IndexOutOfBoundsException();
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    if(index == 0) {
      return new G((strategy.hand.Slot) v);
    }
    throw new IndexOutOfBoundsException();
  }

  public String toString() {
    return "G("+s.toString()+")";
  }

  public boolean equals(Object o) {
    if(o instanceof G) {
      G g = (G) o;
      return s.equals(g.s);
    }
    return false;
  }
}
