/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

/**
 *
 * @author hubert
 */
public class StrategyParameters {

  public static enum DistStrategy {

    DEPTH,
    STEPS
  }

  public static enum TerminaisonCriterion {

    FORECAST,
    POINT_OF_NO_RETURN;
  }
  private DistStrategy distStrategy;
  private TerminaisonCriterion terminaisonCriterion;

  public StrategyParameters(DistStrategy distStrategy, TerminaisonCriterion terminaisonCriterion) {
    this.distStrategy = distStrategy;
    this.terminaisonCriterion = terminaisonCriterion;
  }

  DistStrategy getDistStrategy() {
    return distStrategy;
  }
  
  TerminaisonCriterion getTerminaisonCriterion(){
    return terminaisonCriterion;
  }
  
  void changeTerminaisonCriterion(TerminaisonCriterion criterion){
    this.terminaisonCriterion = criterion;
  }

  boolean requireTerminaison(Slot field, int n) {
    switch (terminaisonCriterion) {
      case FORECAST:
        return field.minimalSize(this.getDistStrategy()) >= n;
      case POINT_OF_NO_RETURN:
        return n <= 0;
      default:
        throw new RuntimeException();
    }
  }
}
