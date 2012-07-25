/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.ATerm;
import java.util.List;

/**
 *
 * @author hubert
 */
public interface SignatureInterpretation {

  @Deprecated
  public abstract Object compute(Object[] args);

  /**
   * Gives an interpretation of a signature (function). Since its parameters are
   * only knew to be instance of Object, this method has to cast them.
   *
   * @param args
   * @return
   */
  public abstract ATerm compute(List<ATerm> args);
}
