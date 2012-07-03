package definitions;

/**
 *
 * @author hubert
 */
class Constructor {

  private Type caller;
  private Type[] champs;


  public Constructor(Type caller, Type[] fields) {
    this.caller = caller;
    this.champs = fields;
  }
}