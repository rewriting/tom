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
public class MakeAllStrategy extends Request {

  public MakeAllStrategy(int i) {
    super(i);
  }

  @Deprecated
  private HashSet<ATerm> getNewRequestWith(HashSet<ATerm> listATerms) {
    int dimMax = 0;
    HashSet<ATerm> toVisit = new HashSet<ATerm>();
    for (ATerm term : listATerms) {
      int d = term.getDimention();
      if (d > dimMax) {
        dimMax = d;
        toVisit = new HashSet<ATerm>();
      }
      if (d == dimMax) {
        toVisit.add(term);
      }
    }
    listATerms.removeAll(toVisit);
    HashSet<ATerm> newlist = (HashSet<ATerm>) listATerms.clone();

    while (!toVisit.isEmpty()) {
      HashSet<ATerm> listClone = (HashSet<ATerm>) toVisit.clone();
      for (ATerm aTerm : listClone) {
        toVisit.remove(aTerm);
        ATerm[] deps = aTerm.chooseConstructor();
        for (int i = 0; i < deps.length; i++) {
          ATerm dep = deps[i];

//          dep.req = new 

          if (dep.getDimention() == dimMax) {
            toVisit.add(dep);
          } else {
            newlist.add(dep);
          }
        }
      }
    }
    return newlist;
  }

  @Override
  HashSet<ATerm> fillATerm(ATerm aTerm) {
    HashSet<ATerm> res = new HashSet<ATerm>();
    ATerm[] deps = aTerm.chooseConstructor();
    HashSet<ATerm> listHigherDim = new HashSet<ATerm>();

    for (int i = 0; i < deps.length; i++) {
      ATerm dep = deps[i];
      if (dep.getDimention() < aTerm.getDimention()) {
        res.add(dep);
      } else {
        listHigherDim.add(dep);
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
