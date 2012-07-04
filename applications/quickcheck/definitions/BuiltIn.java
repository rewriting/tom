/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import tom.library.sl.Strategy;

/**
 *
 * @author hubert
 */
public abstract class BuiltIn {

  public static final Typable integer = new Typable() {

    @Override
    public boolean isRec() {
      return false;
    }

    @Override
    public int getDimention() {
      return 0;
    }

    @Override
    public boolean dependsOn(Typable t) {
      return false;
    }

    @Override
    public Strategy makeGenerator(Request request) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  };
  public static final Typable string = new Typable() {

    @Override
    public boolean isRec() {
      return false;
    }

    @Override
    public int getDimention() {
      return 1;
    }

    @Override
    public boolean dependsOn(Typable t) {
      return false;
    }

    @Override
    public Strategy makeGenerator(Request request) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  };
}
