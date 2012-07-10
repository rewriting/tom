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
  Request getNewRequestWith(int d) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  Request[] getNewRequestWith(Constructor cons) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  Constructor chooseConstructor(HashSet<Constructor> listConstructors) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
}
