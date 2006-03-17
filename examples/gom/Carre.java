package gom;
import gom.rond.types.*;

public class Carre implements jjtraveler.Visitable {

  public Rond r1;
  public Rond r2;

  Carre(Rond r1, Rond r2) {
    this.r1 = r1;
    this.r2 = r2;
  }

  public int getChildCount() {
    return 2;
  }

  public jjtraveler.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return r1;
      case 1: return r2;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
    switch(index) {
      case 0: r1=(Rond)v; return this;
      case 1: r2=(Rond)v; return this;

      default: throw new IndexOutOfBoundsException();
    }
  }

}
