/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.ATerm;
import aterm.ATermIterator;

/**
 * A class implementing this interface is an interpretation of a set.
 *
 * @author hubert
 */
public interface DomainInterpretation {

  /**
   * Chooses an element in the interpreted set.
   *
   * @return
   */
  public ATerm chooseElement(int n);

  /**
   * Tells whether given term is include in the set. This method is use in the
   * shrink algorithm to fetch terms of given type.
   *
   * @param term
   * @return true if term is from type interpreted by this.
   */
  public boolean includes(ATerm term);

  /**
   * Gives all terms that can be build by using constructors whose arguments are
   * those of term.
   *
   * @param term
   * @return list of all new terms
   */
  public ATermIterator lighten(ATerm term);

  /**
   * Gives all domains on which current domain depends. This function is used in
   * the shrink algorithm to shrink sub-term ao a term.
   *
   * @return array of depedences.
   */
  public DomainInterpretation[] getDepsDomains();
}
