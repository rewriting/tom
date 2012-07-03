package definitions;

/**
 *
 * @author hubert
 * @deprecated 
 */
@Deprecated
public class Field {

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

  @Deprecated
  boolean dependsOnType(Type t) {
    if (type == t) {
      return true;
    } else {
      return type.checkIfConstructorHasFieldOfType(t);
    }
  }
}