/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import java.util.HashSet;

/**
 *
 * @author hubert
 */
class MakeLeafStrategy extends Request {

  public MakeLeafStrategy(int i) {
    super(i);
  }

  @Override
  HashSet<ATerm> fillATerm(ATerm aTerm) {
    HashSet<ATerm> res = new HashSet<ATerm>();
    
    //fill the term by choosing the constructor with minimal terminaison
    ATerm[] deps = aTerm.chooseMinimalConstructor();
    
    //dispatch fields of the term between two categories: these whose dimension 
    //equals dimension of the term, and the others
    HashSet<ATerm> listHigherDim = new HashSet<ATerm>();
    for (int i = 0; i < deps.length; i++) {
      ATerm dep = deps[i];
      if (dep.getDimention() < aTerm.getDimention()) {
        res.add(dep);
      } else {
        listHigherDim.add(dep);
        throw new UnsupportedOperationException("ATerm filled by MakeLeafStrategy cannot have branch aterm with the same dimension.");
      }
    }

    //spread number of recursions of the curent term into each fields with the 
    //same dimension (useless here)
    spreadBetweenHigherDim(listHigherDim);

    //re-apply algorithm on same dimension fields in order to eliminate them
    //(useless here)
    for (ATerm term : listHigherDim) {
      Request req = term.getRequest();
      res.addAll(req.fillATerm(term));
    }
    return res;
  }
}
