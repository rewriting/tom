/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO ne peut être utilisé avec le générateur actuel car on ne doit plus
 * regarder si oui ou non la taille minimal est plus grande que le n.
 *
 * @author hubert
 */
class StrategyMakeMaxDimAtMost extends Strategy {

  @Override
  public Set<Slot> fillATerm(Slot aTerm, int ni, StrategyParameters param) {
    if (param.getTerminationCriterion() == StrategyParameters.TerminationCriterion.FORECAST) {
      System.out.print("WARNING: current termination criterion ("
              + param.getTerminationCriterion()
              + ") does not seem to be compliant with StrategyMakeMaxDimAtMost. ");
      param.changeTerminationCriterion(StrategyParameters.TerminationCriterion.POINT_OF_NO_RETURN);
      System.out.println("The criterion have thus been changed into "
              + param.getTerminationCriterion()
              + ".");
    }
    Set<Slot> res = new HashSet<Slot>();

    //fill the term by choosing one of its constructors
    Slot[] fields = aTerm.chooseMaxDimConstructor();

    //dispatch fields of the term between two categories: these whose dimension 
    //equals dimension of the term, and the others
    int currentDim = aTerm.getDimension();
    Set<Slot> listHigherDimFields = dispatchFields(fields, res, currentDim);

    //spread number of recursions of the curent term into each fields with the 
    //same dimension
    int[] nis = Random.pile(ni - 1, listHigherDimFields.size());

    //re-apply algorithm on same dimension fields in order to eliminate them
    int i = 0;
    for (Slot field : listHigherDimFields) {
      Strategy req;
      if (!param.requireTermination(field, ni)) {
        req = new StrategyMakeMaxDimExactly();
      } else {
        req = new StrategyMakeMinimal();
      }
      int rand;
      if (nis[i] == 0) {
        rand = 0;
      } else {
        rand = 1 + (int) (Math.random() * nis[i]);
      }
      res.addAll(req.fillATerm(field, rand, param));
      i++;
    }
    return res;
  }
}
