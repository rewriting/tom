package definitions;

/**
 * Each constructor is linked to only one Buildable.
 */
class Constructor {

  private Field[] fields;

  Constructor(Field[] fields) {
    this.fields = fields;
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
  
  boolean isRec() {
    int l = fields.length;
    for (int i = 0; i < l; i++) {
      if (fields[i].isRec()) {
        return true;
      }
    }
    return false;
  }
}