/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author hubert
 */
class StrategyMakeMinimal extends Strategy {

  @Override
  public HashSet<Slot> fillATerm(Slot aTerm, int ni, int distStrategy) {
    HashSet<Slot> res = new HashSet<Slot>();

    //fill the term by choosing the constructor with minimal terminaison
    Slot[] fields = aTerm.chooseMinDepthConstructor();

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
      if (field.minimalSize(distStrategy) < ni) {
        req = new StrategyMakeAny();
      } else {
        req = new StrategyMakeMinimal();
      }
      res.addAll(req.fillATerm(field, nis[i], distStrategy));
      i++;
    }
    return res;
  }
}
