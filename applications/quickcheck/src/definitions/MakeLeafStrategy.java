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
  HashSet<Hole> fillATerm(Hole aTerm) {
    HashSet<Hole> res = new HashSet<Hole>();

    //fill the term by choosing the constructor with minimal terminaison
    Hole[] deps = aTerm.chooseMinimalConstructor();

    //dispatch fields of the term between two categories: these whose dimension 
    //equals dimension of the term, and the others
    HashSet<Hole> listHigherDim = new HashSet<Hole>();
    for (int i = 0; i < deps.length; i++) {
      Hole dep = deps[i];
      if (dep.getDimention() < aTerm.getDimention()) {
        res.add(dep);
      } else {
        listHigherDim.add(dep);
      }
    }

    //spread number of recursions of the curent term into each fields with the 
    //same dimension
    int[] listSpread = Random.pile(this.getCounter() - 1, listHigherDim.size());

    //re-apply algorithm on same dimension fields in order to eliminate them
    int i = 0;
    for (Hole term : listHigherDim) {
      Request req;
      if (term.getDstToLeaf() < this.getCounter()) {
        req = new MakeAllStrategy(listSpread[i]);
      } else {
        req = new MakeLeafStrategy(listSpread[i]);
      }
      res.addAll(req.fillATerm(term));
      i++;
    }
    return res;
  }
}
