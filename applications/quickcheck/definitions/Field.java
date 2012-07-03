package definitions;

/**
 *
 * @author hubert
 * @deprecated 
 */
@Deprecated
class Field {

  private Type type;
  private boolean isRec; // also true in case of mutual recurstion

  @Deprecated
  public Field(Type buildable, boolean isRec) {
    this.type = buildable;
    this.isRec = isRec;
  }

  @Deprecated
  boolean isRec() {
    return isRec;
  }

  @Deprecated
  int getDimention() {
    return type.getDimention();
  }
}