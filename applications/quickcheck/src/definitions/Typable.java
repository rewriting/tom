package definitions;

import java.util.HashSet;

/**
 *
 * @author hubert
 */
public interface Typable {

  public String getName();
  
  public HashSet<Typable> getDependences();

  public int getDimension();

  /**
   * This function give the size of the shortest path from here to a leaf.
   *
   * @return size of the minimal path between here and a leaf. Returns
   * Integer.MAX_VALUE if no path reaches a leaf.
   */
  public int dstToLeaf();

  public ATerm generate(int n);

  /**
   *
   * @return false if no changes were done
   */
  @Deprecated
  public boolean updateDependences();

  public Scope getScope();
  
}
