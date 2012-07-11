package definitions;

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

  /**
   * This function returns true if function dstToLeaf() can give its results in
   * constant time.
   *
   * @return
   */
  public boolean isDstToLeafDefined();

  public ATerm generate(Request request);

  /**
   *
   * @return false if no changes were done
   */
  public boolean updateDependances();

  public String getName();

  public Scope getScope();

  @Deprecated
  public Object makeLeaf(Request request);
}
