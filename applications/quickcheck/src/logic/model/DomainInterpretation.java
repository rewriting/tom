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

  /**
   * Tell whether given term is include in the set. This method is use in the
   * shrink algorithm to fetch terms of given type.
   *
   * @param term
   * @return true if term is from type interpreted by this.
   */
  public boolean includes(ATerm term);
  
  /**
   * 
   * @param term
   * @return 
   */
  public ATerm lighten(ATerm term);

  /**
   * Gives all domains which current domain depends on. This function is used in
   * the shrink algorithm to shrink sub-term ao a term.
   *
   * @return array of depedences.
   */
  public DomainInterpretation[] getDepsDomains();
}
