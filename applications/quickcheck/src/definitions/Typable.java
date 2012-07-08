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

  /**
   * This function give the size of the shortest path from here to a leaf.
   *
   * @return size of the minimal path between here and a leaf. Returns
   * Integer.MAX_VALUE if no path reaches a leaf.
   */
  public int dstToLeaf();

  public boolean isDstToLeafDefined();

  public Strategy makeGenerator(Request request);
}
