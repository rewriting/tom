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
interface Strategy {

  /**
   * Fill a slot by choosing a contructor for it. This choice is influenced by
   * the type of the strategy. According to Agata algorithm, it must also fill
   * all subterm with egal dimention of this term.
   *
   * @param aTerm
   * @param n
   * @return Set of fields of the filled terms
   */
  HashSet<Slot> fillATerm(Slot aTerm, int ni, int distStrategy);
}
