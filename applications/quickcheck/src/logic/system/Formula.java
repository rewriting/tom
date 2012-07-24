/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.system;

import java.util.Map;

/**
 *
 * @author hubert
 */
public abstract class Formula {

  protected abstract boolean interprete(Map<Variable, Object> valuation);
}
