package definitions;

import tom.library.sl.Strategy;
/**
 *
 * @author hubert
 */
public interface Typable {

  public boolean isRec();

  public int getDimention();

  public boolean dependsOn(Typable t);
  
  public Strategy makeGenerator(Request request);
}
