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
abstract class Strategy {

  /**
   * Fill a slot by choosing a contructor for it. This choice is influenced by
   * the type of the strategy. According to Agata algorithm, it must also fill
   * all subterm with egal dimention of this term.
   *
   * @param aTerm
   * @param n
   * @return Set of fields of the filled terms
   */
  abstract Set<Slot> fillATerm(Slot aTerm, int ni, StrategyParameters param);

  /**
   * dispatch fields of the term between two categories: these whose dimension
   * equals dimension of the term, and the others
   *
   * @param fields array of all fields of the current term
   * @param res already created set where will be stored lower dimention fields
   * @param currentDim dimention of the current term
   * @return set of fields with the same dimension that th current term.
   */
  protected Set<Slot> dispatchFields(Slot[] fields, Set<Slot> res, int currentDim) {
    Set<Slot> listHigherDimFields = new HashSet<Slot>();
    for(int i = 0; i < fields.length; i++) {
      Slot field = fields[i];
      if(field.getDimension() < currentDim) {
        res.add(field);
      } else {
        listHigherDimFields.add(field);
      }
    }
    return listHigherDimFields;
  }
}
