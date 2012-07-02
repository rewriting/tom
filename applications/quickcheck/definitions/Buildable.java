package definitions;

public interface Buildable {
  public abstract int getDimention();
  public abstract Constructor[] getConstructors();
  public abstract boolean isRec();
}