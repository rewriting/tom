package strategy.hand;

public class Name extends Slot {
  public String name;
  public Name(String s) {
    this.name = s;
  }
  /* Do not visit the "String" child, as we can't make it Visitable */
  public int getChildCount() { return 0; }

  public jjtraveler.Visitable getChildAt(int index) {
    throw new IndexOutOfBoundsException();
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    throw new IndexOutOfBoundsException();
  }

  public String toString() {
    return "Name("+name+")";
  }

  public boolean equals(Object o) {
    if(o instanceof Name) {
      Name n = (Name) o;
      return name.equals(n.name);
    }
    return false;
  }
}
