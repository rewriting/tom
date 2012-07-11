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

  /**
   * Cette methode permet de construire les requestes a passer lors dela
   * creation des champs du constructeur. La repartition des requetes doit tenir
   * compte des dimensions des champs, mais peut aussi infuer sur la forme du
   * graphe en choisissant par exemple
   *
   * @param cons
   * @return
   */
  @Deprecated
  private MakeLeafStrategy[] getNewRequestWith(Constructor cons) {
    int size = cons.getFields().length;
    MakeLeafStrategy[] listRequests = new MakeLeafStrategy[size];
    for (int i = 0; i < listRequests.length; i++) {
      listRequests[i] = new MakeLeafStrategy(0);
    }
    if (size == 0) {
      return listRequests;
    }
    int n = getCounter();
    while (n != 0) {
      int index = (int) (Math.random() * size);
      listRequests[index].inc();
      n--;
    }
    return listRequests;
  }

  @Deprecated
  private Constructor chooseConstructor(HashSet<Constructor> listConstructors) {
    Constructor res = null;
    int min = Integer.MAX_VALUE;
    for (Constructor constructor : listConstructors) {
      int m = constructor.distanceToReachLeaf();
      if (m < min) {
        min = m;
        res = constructor;
      }
    }
    return res;
  }

  @Override
  HashSet<ATerm> fillATerm(ATerm aTerm) {
    HashSet<ATerm> res = new HashSet<ATerm>();
    ATerm[] deps = aTerm.chooseMinimalConstructor();
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

    spreadBetweenHigherDim(listHigherDim);

    for (ATerm term : listHigherDim) {
      Request req = term.getRequest();
      res.addAll(req.fillATerm(term));
    }
    return res;
  }
}
