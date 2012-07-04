package definitions;

/**
 *
 * @author hubert
 */
class Constructor {

  private Type caller;
  private Typable[] champs;


  public Constructor(Type caller, Typable[] fields) {
    this.caller = caller;
    this.champs = fields;
  }
}