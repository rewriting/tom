package definitions;

/**
 * Each constructor is linked to only one Buildable.
 */
public class Constructor {

  private String name;
  private Field[] fields;

  public Constructor(String name, Field[] fields) {
    this.name = name;
    this.fields = fields;
  }

  public Field[] getFields() {
    return fields;
  }

  int getDimention() {
    int l = fields.length;
    int max = 0;
    for (int i = 0; i < l; i++) {
      if (!fields[i].isRec()) {
        max = Math.max(max, fields[i].getDimention());
      }
    }
    return max;
  }

  public String getName() {
    return name;
  }

  public boolean isRec() {
    int l = fields.length;
    for (int i = 0; i < l; i++) {
      if (fields[i].isRec()) {
        return true;
      }
    }
    return false;
  }
}