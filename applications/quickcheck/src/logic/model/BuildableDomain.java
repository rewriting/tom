/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.ATerm;
import aterm.ATermList;
import definitions.Buildable;
import java.lang.reflect.Method;

/**
 * This class is a exemple of an implementation of Domain Interface. It gives
 * domain to any Buildable class.
 *
 * @author hubert
 */
public class BuildableDomain implements DomainInterpretation {

  private Buildable model;

  public BuildableDomain(Buildable model) {
    this.model = model;
  }

  @Override
  public ATerm chooseElement() {
    return model.generate(100);
  }

  @Override
  public boolean include(ATerm term) {
    return model.isTypeOf(term);
  }
}
