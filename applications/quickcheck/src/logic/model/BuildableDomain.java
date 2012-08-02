/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.ATerm;
import definitions.Buildable;
import java.util.Iterator;
import java.util.Set;

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
    return model.generate(20);
  }

  @Override
  public boolean includes(ATerm term) {
    return model.isTypeOf(term);
  }

  @Override
  public DomainInterpretation[] getDepsDomains() {
    //TODO : this method can be optimized because it use currently all recursive dependences.
    Set<Buildable> deps = model.getDependences();
    DomainInterpretation[] res = new DomainInterpretation[deps.size()];
    int i = 0;
    for (Buildable buildable : deps) {
      res[i] = new BuildableDomain(buildable);
      i++;
    }
    return res;
  }

  @Override
  public Iterator<ATerm> lighten(ATerm term) {
    return model.lighten(term);
  }
}
