package strategy.hand;

public class B extends Term {
  public int getChildCount() { return 0; }

  public jjtraveler.Visitable getChildAt(int index) {
    throw new IndexOutOfBoundsException();
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    throw new IndexOutOfBoundsException();
  }

  public String toString() {
    return "B()";
  }

  public boolean equals(Object o) {
    if(o instanceof B) {
      return true;
    }
    return false;
  }
}
