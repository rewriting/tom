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
  MakeLeafStrategy[] getNewRequestWith(Constructor cons) {
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

  @Override
  Constructor chooseConstructor(HashSet<Constructor> listConstructors) {
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
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
