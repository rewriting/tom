/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.ATerm;
import java.util.List;

/**
 * A class implementing this interface is an interpretation of a Predicate.
 *
 * @author hubert
 */
public interface PredicateInterpretation {

  /**
   * Gives the result of the function which interprete a Predicate.
   *
   * @param args
   * @return true if the evaluation of the predicate over given arguments is
   * true.
   */
  public abstract boolean isTrue(List<ATerm> args);
}
