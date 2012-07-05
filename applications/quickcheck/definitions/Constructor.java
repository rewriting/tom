package definitions;

/**
 *
 * @author hubert
 */
class Constructor {

  private Algebraic caller;
  private Typable[] champs;

  Constructor(Algebraic caller, Typable[] fields) {
    this.caller = caller;
    this.champs = fields;
  }

  int getDimentionMax() {
    int res = 0;
    for (int i = 0; i < champs.length; i++) {
      res = Math.max(res, champs[i].getDimention());
    }
    return res;
  }
}