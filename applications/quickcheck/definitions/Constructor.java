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
}