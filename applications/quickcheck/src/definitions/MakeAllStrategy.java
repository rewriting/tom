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

  @Override
  Request[] getNewRequestWith(Constructor cons) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  HashSet<ATerm> getNewRequestWith(HashSet<ATerm> listATerms) {
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
  Constructor chooseConstructor(HashSet<Constructor> listConstructors) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  void spread(HashSet<ATerm> listAterm) {
    int size = listAterm.size();
    Request[] listRequests = new Request[size];
    int i = 0;
    for (ATerm term : listAterm) {
      if (term.getDstToLeaf() < getCounter()) {
        listRequests[i] = new MakeAllStrategy(0);
      } else {
        listRequests[i] = new MakeLeafStrategy(0);
      }
      i++;
    }
    if (size == 0) {
      return;
    }
    int n = getCounter() - 1;
    while (n != 0) {
      int index = (int) (Math.random() * size);
      listRequests[index].inc();
      n--;
    }
    i = 0;
    for (ATerm term : listAterm) {
      term.setRequest(listRequests[i]);
      i++;
    }
  }

  @Override
  HashSet<ATerm> fillATerm(ATerm aTerm) {
    HashSet<ATerm> res = new HashSet<ATerm>();
    ATerm[] deps = aTerm.chooseConstructor();
    HashSet<ATerm> listSameDim = new HashSet<ATerm>();

    for (int i = 0; i < deps.length; i++) {
      ATerm dep = deps[i];
      if (dep.getDimention() < aTerm.getDimention()) {
        res.add(dep);
      } else {
        listSameDim.add(dep);
      }
    }

    spread(listSameDim);

    for (ATerm term : listSameDim) {
      Request req = term.getRequest();
      res.addAll(req.fillATerm(term));
    }


    return res;
  }
}
