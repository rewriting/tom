/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.ATerm;

/**
 * A class implementing this interface is an interpretation of a set.
 *
 * @author hubert
 */
public interface DomainInterpretation {

  /**
   * Choose an element in the interpreted set.
   *
   * @return
   */
  public ATerm chooseElement();
}
