/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import java.util.HashSet;

/**
 * ne peut être utilisé avec le générateur actuel car on ne doit plus regarder si oui ou non la taille minimal
 * @author hubert
 */
class StrategyMakeMaxDimAtMost implements Strategy {

  @Override
  public HashSet<Slot> fillATerm(Slot aTerm, int ni, int distStrategy) {
    HashSet<Slot> res = new HashSet<Slot>();

    //fill the term by choosing one of its constructors
    Slot[] fields = aTerm.chooseMaxDimConstructor();

    //dispatch fields of the term between two categories: these whose dimension 
    //equals dimension of the term, and the others
    HashSet<Slot> listHigherDimFields = new HashSet<Slot>();
    for (int i = 0; i < fields.length; i++) {
      Slot field = fields[i];
      if (field.getDimension() < aTerm.getDimension()) {
        res.add(field);
      } else {
        listHigherDimFields.add(field);
      }
    }

    //spread number of recursions of the curent term into each fields with the 
    //same dimension
    int[] nis = Random.pile(ni - 1, listHigherDimFields.size());

    //re-apply algorithm on same dimension fields in order to eliminate them
    int i = 0;
    for (Slot field : listHigherDimFields) {
      Strategy req;
      if (field.minimalSize(distStrategy) < ni) {
        req = new StrategyMakeMaxDimAtMost();
      } else {
        req = new StrategyBacktrackDepth();
      }
      res.addAll(req.fillATerm(field, nis[i], distStrategy));
      i++;
    }
    return res;
  }
}
