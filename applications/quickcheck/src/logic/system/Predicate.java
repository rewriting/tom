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
public class Predicate extends Formula {

  private Term[] args;
  private String name;

  public Predicate(String name, Term... args) {
    this.name = name;
    this.args = args;
  }
  
  @Override
  public String toString() {
    String res = this.name + "(";
    for (Term term : args) {
      res += term.toString() + ",";
    }
    res = res.substring(0, res.length()-1);
    res += ")";
    return res;
  }

  public int getArity() {
    return args.length;
  }
  
  public Term[] getArgs(){
    return args;
  }

  @Override
  protected boolean interprete(Map<Variable, Object> valuation) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
