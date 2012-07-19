/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import java.util.Collection;
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
    if (param.getTerminaisonCriterion() == StrategyParameters.TerminaisonCriterion.FORECAST) {
      System.out.print("WARNING: current terminaison criterion ("
              + param.getTerminaisonCriterion()
              + ") does not seem to be compliant with StrategyMakeMaxDimAtMost. ");
      param.changeTerminaisonCriterion(StrategyParameters.TerminaisonCriterion.POINT_OF_NON_RETURN);
      System.out.println("The criterion have thus been changed into "
              + param.getTerminaisonCriterion()
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
      int rand;
      if (nis[i] == 0) {
        rand = 0;
      } else {
        rand = 1 + (int) (Math.random() * nis[i]);
      }
//      res.addAll(propagate(field, rand, param));
      Strategy req = new StrategyMakeMaxDimExactly();
      res.addAll(req.fillATerm(field, rand, param));
      i++;
    }
    return res;
  }
  
  @Deprecated
  private Collection propagate(Slot aTerm, int rand, StrategyParameters param) {
    Set<Slot> res = new HashSet<Slot>();
    Slot[] fields = aTerm.chooseMaxDimConstructor();
    int currentDim = aTerm.getDimension();
    Set<Slot> listHigherDimFields = dispatchFields(fields, res, currentDim);
    int[] nis = Random.pile(rand - 1, listHigherDimFields.size());
    int i = 0;
    for (Slot field : listHigherDimFields) {
      if (nis[i] > 0) {
        res.addAll(propagate(field, nis[i], param));
      } else {
        Strategy req = new StrategyMakeMinimal();
        res.addAll(req.fillATerm(field, nis[i], param));
      }
      i++;
    }
    return res;
  }
}
