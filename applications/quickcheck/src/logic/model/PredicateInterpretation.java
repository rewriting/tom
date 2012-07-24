/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import java.util.List;

/**
 *
 * @author hubert
 */
public interface PredicateInterpretation {
  
  public abstract boolean isTrue(Object[] args);
  
  public abstract boolean isTrue(List<Object> args);
}
