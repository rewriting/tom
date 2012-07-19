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
  
  public static enum DistStrategy{
    DEPTH,
    STEPS
  }

  public static enum TerminaisonCriterion{
    FORECAST,
    POINT_OF_NON_RETURN;
  }
  
  private DistStrategy distStrategy;
  private TerminaisonCriterion terminaisonCriterion;
  
  public StrategyParameters(DistStrategy distStrategy, TerminaisonCriterion terminaisonCriterion){
    this.distStrategy = distStrategy;
    this.terminaisonCriterion = terminaisonCriterion;
  }
  
  public DistStrategy getDistStrategy(){
    return distStrategy;
  }
  
  public TerminaisonCriterion getTerminaisonCriterion(){
    return terminaisonCriterion;
  }
  
}
