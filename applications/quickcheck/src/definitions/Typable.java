package definitions;

import aterm.ATerm;
import java.util.Set;

/**
 *
 * @author hubert
 */
public interface Typable {

  /**
   * Gives name of type.
   *
   * @return name of type
   */
  public String getName();

  /**
   * This function gives the set of types which depend on current type. This
   * function cannot be used till dependances are not set (using
   * scope.setdependences()).
   *
   * @return set of dependences
   */
  public Set<Typable> getDependences();

  /**
   * Gives dimension of the current type. This function cannot be used till
   * dependances are not set (using scope.setdependences()).
   *
   * @return dimention
   */
  public int getDimension();

  /**
   * This function give the size of the shortest path from here to a leaf.
   *
   * @return size of the minimal path between here and a leaf. Returns
   * Integer.MAX_VALUE if no path reaches a leaf.
   */
  public int dstToLeaf();

  /**
   * Make a new random term. This function cannot be used till dependances are
   * not set (using scope.setdependences()).
   *
   * @param n maximal size of the generated term
   * @return
   */
  public ATerm generate(int n);

  /**
   * This function add depencences of its fields to its own depedences. It is
   * use in order to set dependances (in fcontion Scope.setdependences()).
   * @see Scope#setDependances() 
   * @return true if changes were done
   */
  public boolean updateDependences();
}
