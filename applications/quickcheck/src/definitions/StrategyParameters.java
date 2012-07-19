/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

/**
 * Makes it possible to choose how to generate terms.
 *
 * @author hubert
 */
public class StrategyParameters {

  /**
   * Contains different ways to calculate the minimal size of a term.
   */
  public static enum DistStrategy {

    /**
     * Indicates that size is given in terms of depth of the tree.
     */
    DEPTH,
    /**
     * Indicates that size is given in number of nodes of the tree.
     */
    STEPS
  }

  /**
   * Contains different ways to determine termination criterion.
   */
  public static enum TerminationCriterion {

    /**
     * Indicates that termination is triggered when minimal size of a term is
     * greater than the n given in parameter.
     */
    FORECAST,
    /**
     * Indicates that termination is triggered when the n given in parameter is
     * lower equal to zero.
     */
    POINT_OF_NO_RETURN;
  }
  private DistStrategy distStrategy;
  private TerminationCriterion terminationCriterion;

  public StrategyParameters(DistStrategy distStrategy, TerminationCriterion terminationCriterion) {
    this.distStrategy = distStrategy;
    this.terminationCriterion = terminationCriterion;
  }

  DistStrategy getDistStrategy() {
    return distStrategy;
  }

  TerminationCriterion getTerminationCriterion() {
    return terminationCriterion;
  }

  void changeTerminationCriterion(TerminationCriterion criterion) {
    this.terminationCriterion = criterion;
  }

  /**
   * Indicates whether the field must be terminated by using termination
   * strategy or not.
   *
   * @param field
   * @param n
   * @return
   */
  boolean requireTermination(Slot field, int n) {
    switch (terminationCriterion) {
      case FORECAST:
        return field.minimalSize(this.getDistStrategy()) >= n;
      case POINT_OF_NO_RETURN:
        return n <= 0;
      default:
        throw new RuntimeException();
    }
  }
}
