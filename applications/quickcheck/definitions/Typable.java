package definitions;

/**
 *
 * @author hubert
 */
public interface Typable {

  public boolean isRec();

  public int getDimention();
  
  public boolean dependsOn(Typable t);
}
