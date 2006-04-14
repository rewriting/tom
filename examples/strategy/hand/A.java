package strategy.hand;

public class A extends Term {

  public int getChildCount() { return 0; }

  public jjtraveler.Visitable getChildAt(int index) {
    throw new IndexOutOfBoundsException();
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    throw new IndexOutOfBoundsException();
  }

  public String toString() {
    return "A()";
  }

  public boolean equals(Object o) {
    if(o instanceof A) {
      return true;
    }
    return false;
  }
}
