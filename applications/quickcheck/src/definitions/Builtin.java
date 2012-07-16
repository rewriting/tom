/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author hubert
 */
public final class Builtin {
  
  public final static Typable Integer = new Typable() {

    @Override
    public java.lang.String getName() {
      return "Integer";
    }

    @Override
    public Set<Typable> getDependences() {
      return new HashSet<Typable>();
    }

    @Override
    public int getDimension() {
      return 0;
    }

    @Override
    public int dstToLeaf() {
      return 0;
    }

    @Override
    public Slot generate(int n) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean updateDependences() {
      return false;
    }
  };
  
  public final static Typable String = new Typable() {

    @Override
    public java.lang.String getName() {
      return "String";
    }

    @Override
    public Set<Typable> getDependences() {
      return new HashSet<Typable>();
    }

    @Override
    public int getDimension() {
      return 0;
    }

    @Override
    public int dstToLeaf() {
      return 0;
    }

    @Override
    public Slot generate(int n) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean updateDependences() {
      return false;
    }
  };
  
  private Builtin(){};
  
}
