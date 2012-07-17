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
   * Gives minimal depth before terminaison.
   *
   */
  int depthToLeaf() {
    if (fields.length == 0) {
      return 0;
    }
    lock = true;
    int res = 0;
    for (int i = 0; i < fields.length; i++) {
      Buildable field = fields[i];
      int tmp = field.depthToLeaf();
      if (tmp == -1) {
        lock = false;
        return Integer.MAX_VALUE;
      }
      if (tmp == Integer.MAX_VALUE) {
        // if field can not lead to terminaison
        lock = false;
        return Integer.MAX_VALUE;
      }
      res = Math.max(res, tmp);
    }
    lock = false;
    return res + 1;
  }
  
  /**
   * Gives minimal number of steps before terminaison.
   *
   */
  int stepsToLeaf() {
    if (fields.length == 0) {
      return 0;
    }
    lock = true;
    int res = 0;
    for (int i = 0; i < fields.length; i++) {
      Buildable field = fields[i];
      int tmp = field.stepsToLeaf();
      if (tmp == -1) {
        // if one of the constructors of field is locked
        lock = false;
        return Integer.MAX_VALUE;
      }
      if (tmp == Integer.MAX_VALUE) {
        // if field can not lead to terminaison
        lock = false;
        return Integer.MAX_VALUE;
      }
      res += tmp;
      if(Math.signum(res)*Math.signum(res - tmp) == -1){
        throw new RuntimeException("Number of steps overflows.");
      }
    }
    lock = false;
    return res + 1;
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