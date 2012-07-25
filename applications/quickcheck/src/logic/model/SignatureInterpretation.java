/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.ATerm;
import java.util.List;

/**
 * A class implementing this interface is an interpretation of Signature.
 *
 * @author hubert
 */
public interface SignatureInterpretation {

  /**
   * Gives ATerm equivalente of the result of the function which interprete a
   * Signature.
   *
   * @param args
   * @return
   */
  public abstract ATerm compute(List<ATerm> args);
}
