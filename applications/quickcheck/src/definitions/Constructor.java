package definitions;

/**
 *
 * @author hubert
 */
class Constructor {

  private Buildable[] fields;
  private boolean lock = false;
  private String name;

  Constructor(String name, Buildable[] fields) {
    this.fields = fields;
    this.name = name;
  }

  String getName() {
    return name;
  }

  Buildable[] getFields() {
    return fields;
  }

  boolean isLocked() {
    return lock;
  }

  private int getSize() {
    return fields.length;
  }

  /**
   * Gives minimal number of steps before terminaison.
   *
   */
  int distanceToReachLeaf() {
    if (fields.length == 0) {
      return 0;
    }
    lock = true;
    int res = 0;
    for (int i = 0; i < fields.length; i++) {
      Buildable field = fields[i];
      int tmp = field.dstToLeaf();
      if (tmp == -1) {
        lock = false;
        return Integer.MAX_VALUE;
      }
      if (tmp == Integer.MAX_VALUE) {
        // if field can not lead to terminaison
        lock = false;
        return Integer.MAX_VALUE;
      }
      res = 1 + Math.max(res, tmp);
    }
    lock = false;
    return res;
  }

  @Override
  public String toString() {
    String res = "[";
    for (int i = 0; i < fields.length; i++) {
      Buildable typable = fields[i];
      res += typable.getName() + ", ";
    }
    res += "]";
    return res;
  }

  /**
   * Gives all fields of the current constructor
   *
   * @return
   */
  Slot[] giveATermDeps() {
    Slot[] res = new Slot[getSize()];
    for (int i = 0; i < res.length; i++) {
      res[i] = new Slot(fields[i]);
    }
    return res;
  }
}