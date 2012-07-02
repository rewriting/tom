package definitions;

public class Field {
  private Buildable val;
  private boolean isRec;
  public Field(Buildable val, boolean isRec){
    this.val = val;
    this.isRec = isRec;
  }
  public Buildable getBuildable(){
    return val;
  }
  public boolean isRec(){
    return isRec;
  }
}