package definitions;

/**
 * Each constructor is linked to only one Type.
 */
class Constructor {

  private Field[] fields;

  Constructor(Field[] fields) {
    this.fields = fields;
  }

  /**
   * Cette fonction donne la dimention du constructeur. Elle renvoie le maximum
   * des dimentions des champs non recursifs, c'est a dire dont la construction
   * n'utilise pas ce contructeur. Dans le cas ou tous les champs du
   * constructeurs sont recursifs, le fonction renvoie 0.
   *
   * @return maximum des dimentions des champs non recursifs, 0 si aucun champs
   * n'est pas recursifs
   */
  
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