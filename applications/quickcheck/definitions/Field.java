package definitions;

public class Field {

  private Type type;
  private boolean isRec; // also true in case of mutual recurstion

  public Field(Type buildable, boolean isRec) {
    this.type = buildable;
    this.isRec = isRec;
  }

  boolean isRec() {
    return isRec;
  }

  int getDimention() {
    return type.getDimention();
  }
}