package definitions;

import java.util.HashMap;
import java.util.Map;

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

  /**
   * Checks whether fields of the current constructor are included in those of
   * cons. Return true of and only if the set of fields of the current
   * constructor is a subset the set of fields of cons. For instance cons1(int,
   * String) is a a sub-constructor of cons2(String, String, int) but the
   * converse in false.
   *
   * @param cons
   * @return true if the current constructor is include a sub-constructor of
   * cons
   */
  boolean isSubCons(Constructor cons) {
    Map<Buildable, Integer> map = new HashMap<Buildable, Integer>();
    for(Buildable buildable : cons.fields) {
      Integer n = map.get(buildable);
      if(n == null) {
        map.put(buildable, 1);
      } else {
        map.put(buildable, n + 1);
      }
    }

    for(Buildable buildable : fields) {
      Integer n = map.get(buildable);
      if(n == null) {
        return false;
      } else if(n > 1) {
        map.put(buildable, n - 1);
      } else {
        map.remove(buildable);
      }
    }
    return true;
  }

  /**
   * This function is necessary for computation of the minimal size of a term. A
   * constructor is locked from the beginning of both minimalDepth and
   * minimalNodes functions till their ends.
   *
   * @return
   */
  boolean isLocked() {
    return lock;
  }

  int getSize() {
    return fields.length;
  }

  /**
   * Gives the minimal depth of a term build with this constructor.
   *
   * @return either the expected result (Integer.MAX_VALUE in case of non
   * termination), or Integer.MAX_VALUE if one constructor of one its fields is
   * locked.
   */
  int minimalDepth() {
    if(fields.length == 0) {
      return 0;
    }
    lock = true;
    int res = 0;
    for(int i = 0; i < fields.length; i++) {
      Buildable field = fields[i];
      int tmp = field.minimalSize(StrategyParameters.DistStrategy.DEPTH);
      if(tmp == -1) {
        //if one the constructors of the field is locked
        lock = false;
        return Integer.MAX_VALUE;
      }
      if(tmp == Integer.MAX_VALUE) {
        // if field can not lead to termination
        lock = false;
        return Integer.MAX_VALUE;
      }
      res = Math.max(res, tmp);
    }
    lock = false;
    return res + 1;
  }

  /**
   * Gives the minimal number of nodes of a term build with this constructor.
   *
   * @return either the expected result (Integer.MAX_VALUE in case of non
   * termination), or Integer.MAX_VALUE if one constructor of one its fields is
   * locked.
   */
  int minimalNodes() {
    if(fields.length == 0) {
      return 0;
    }
    lock = true;
    int res = 0;
    for(int i = 0; i < fields.length; i++) {
      Buildable field = fields[i];
      int tmp = field.minimalSize(StrategyParameters.DistStrategy.NODES);
      if(tmp == -1) {
        // if one of the constructors of field is locked
        lock = false;
        return Integer.MAX_VALUE;
      }
      if(tmp == Integer.MAX_VALUE) {
        // if field can not lead to termination
        lock = false;
        return Integer.MAX_VALUE;
      }
      res += tmp;
      if(Math.signum(res) * Math.signum(res - tmp) == -1) {
        throw new RuntimeException("Number of steps overflows.");
      }
    }
    lock = false;
    return res + 1;
  }

//  @Override
//  public String toString() {
//    String res = "[";
//    for (int i = 0; i < fields.length; i++) {
//      Buildable typable = fields[i];
//      res += typable.getName() + ", ";
//    }
//    res += "]";
//    return res;
//  }
  @Override
  public String toString() {
    return getName();
  }

  /**
   * Gives all fields of the current constructor
   *
   * @return
   */
  Slot[] giveATermDeps() {
    Slot[] res = new Slot[getSize()];
    for(int i = 0; i < res.length; i++) {
      res[i] = new Slot(fields[i]);
    }
    return res;
  }

  int getDimention() {
    int dimMax = 0;
    for(Buildable field : fields) {
      dimMax = Math.max(dimMax, field.getDimension());
    }
    return dimMax;
  }
}