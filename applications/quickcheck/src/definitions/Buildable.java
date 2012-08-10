package definitions;

import aterm.ATerm;
import java.util.Iterator;
import java.util.Set;

/**
 * Defines what a randomly buildable is.
 *
 * @author hubert
 */
public interface Buildable {

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
   * @precondition This function cannot be used till dependances are not set
   * (using scope.setdependences()).
   *
   * @return set of dependences
   *
   */
  public Set<Buildable> getDependences();

  /**
   * This function add depencences of its fields to its own depedences. It is
   * use in order to set dependances (in fcontion Scope.setdependences()).
   *
   * @see Scope#setDependances()
   * @return true if changes were done
   */
  public boolean updateDependences();

  /**
   * Gives dimension of the current type. This function cannot be used till
   * dependances are not set (using scope.setdependences()).
   *
   * @precondition This function cannot be used till dependances are not set
   * (using scope.setdependences()).
   *
   * @return dimention
   */
  public int getDimension();

  /**
   * This function give the size of the shortest path from here to a leaf.
   *
   * @param strategy
   * @return size of the minimal path between here and a leaf. Returns
   * Integer.MAX_VALUE if no path reaches a leaf.
   */
  public int minimalSize(StrategyParameters.DistStrategy strategy);

  /**
   * Make a new random term. This function cannot be used till dependances are
   * not set (using scope.setdependences()).
   *
   * @precondition This function cannot be used till dependances are not set
   * (using scope.setdependences()).
   *
   * @param n maximal size of the generated term
   * @return generated term
   */
  public ATerm generate(int n);

  /**
   * ===========================================================================
   *
   * Function exclusively for shrinking algorithm
   *
   * ===========================================================================
   */
  /**
   * Tell whether a given term is from type designed by this Buildable. This
   * function check if the name of the aterm is the name of one of the
   * constructors of the current Buildable.
   *
   * @param term
   * @return
   */
  public boolean isTypeOf(ATerm term);

  public Iterator<ATerm> lighten(ATerm term);
}
