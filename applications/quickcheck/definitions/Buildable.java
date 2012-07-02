package definitions;

public class Buildable {

  private Constructor[] listConstructors;

  public Buildable(Constructor[] constructors) {
    this.listConstructors = constructors;
  }

  public boolean isRec() {
    int l = this.listConstructors.length;
    for (int i = 0; i < l; i++) {
      if (listConstructors[i].isRec()) {
        return true;
      }
    }
    return false;
  }

  public int getDimention() {
    int dim = 0;
    int add = 0;
    if (isRec()) {
      add++;
    }
    for (int i = 0; i < listConstructors.length; i++) {
      dim = Math.max(dim, listConstructors[i].getDimention());
    }
    return dim;
  }
}