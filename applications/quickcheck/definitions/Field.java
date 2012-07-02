package definitions;

public class Field {

  private Type buildable;
  private boolean isRec; // also true in case of mutual recurstion

  public Field(Type buildable, boolean isRec) {
    this.buildable = buildable;
    this.isRec = isRec;
  }

  boolean isRec() {
    return isRec;
  }

  int getDimention() {
    return buildable.getDimention();
  }
}