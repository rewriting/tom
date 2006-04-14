package strategy.hand;

public class F extends Term {
  public Term a;
  public Term b;
  public F(Term arg0, Term arg1) {
    a = arg0;
    b= arg1;
  }
  public int getChildCount() { return 2; }

  public jjtraveler.Visitable getChildAt(int index) {
    if(index == 0) {
      return a;
    } else if(index == 1) {
      return b;
    }
    throw new IndexOutOfBoundsException();
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    if(index == 0) {
      return new F((Term)v,b);
    } else if(index == 1) {
      return new F(a,(Term)v);
    }
    throw new IndexOutOfBoundsException();
  }

  public String toString() {
    return "F("+a.toString()+", "+b.toString()+")";
  }
}
