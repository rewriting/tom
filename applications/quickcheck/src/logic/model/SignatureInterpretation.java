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
public interface SignatureInterpretation {

  public abstract Object compute(Object[] args);
  
  public abstract Object compute(List<Object> args);
}
