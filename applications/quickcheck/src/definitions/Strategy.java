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
  
   HashSet<Slot> fillATerm(Slot aTerm, int n);
}
