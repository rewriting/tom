/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author hubert
 */
public class Predicate extends Formula {

  private Set<Term> args;
  private String name;

  public Predicate(String name, Term... args) {
    this.name = name;
    this.args = new HashSet<Term>(Arrays.asList(args));
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
}
