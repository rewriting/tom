/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import java.util.HashSet;

/**
 *
 * @author hubert
 */
class MakeMaxDimStrategy implements Strategy {

  @Override
  public HashSet<Slot> fillATerm(Slot aTerm, int ni) {
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
      if (field.getDstToLeaf() < ni) {
        req = new MakeMaxDimStrategy();
      } else {
        req = new BacktrackDepthStrategy();
      }
      res.addAll(req.fillATerm(field, nis[i]));
      i++;
    }
    return res;
  }
}
