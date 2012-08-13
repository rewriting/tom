/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import aterm.ATerm;
import aterm.ATermInt;
import aterm.ATermIterator;
import aterm.ATermIteratorFromList;
import aterm.ATermList;
import aterm.pure.PureFactory;
import definitions.StrategyParameters.DistStrategy;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Implements several built-in types.
 *
 * @author hubert
 */
public final class Builtin {

  /**
   * Representation of type Integer.
   */
  public final static Buildable Integer = new Buildable() {
    @Override
    public java.lang.String getName() {
      return "Integer";
    }

    @Override
    public Set<Buildable> getDependences() {
      return new HashSet<Buildable>();
    }

    @Override
    public int getDimension() {
      return 0;
    }

    @Override
    public ATerm generate(int n) {
      return (new PureFactory()).makeInt((int) (Math.random() * n));
    }

    @Override
    public boolean updateDependences() {
      return false;
    }

    @Override
    public int minimalSize(DistStrategy strategy) {
      return 0;
    }

    @Override
    public boolean isTypeOf(ATerm term) {
      return term instanceof ATermInt;
    }

    @Override
    public ATermIterator lighten(ATerm term) {
      List<ATerm> list = new LinkedList<ATerm>();
      list.add(term);
      return new ATermIteratorFromList(list);
    }
  };
  /**
   * Representation of type String.
   */
  public final static Buildable String = new Buildable() {
    @Override
    public java.lang.String getName() {
      return "String";
    }

    @Override
    public Set<Buildable> getDependences() {
      return new HashSet<Buildable>();
    }

    @Override
    public int getDimension() {
      return 0;
    }

    @Override
    public ATerm generate(int n) {
      PureFactory factory = new PureFactory();
      ATermList res = factory.makeList();
      for(int i = 0; i < n; i++) {
        res = factory.makeList(factory.makeInt((int) (Math.random() * 256)), res);
      }
      return res;
    }

    @Override
    public boolean updateDependences() {
      return false;
    }

    @Override
    public int minimalSize(DistStrategy strategy) {
      return 0;
    }

    @Override
    public boolean isTypeOf(ATerm term) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ATermIterator lighten(ATerm term) {
      List<ATerm> list = new LinkedList<ATerm>();
      list.add(term);
      return new ATermIteratorFromList(list);
    }
  };

  private Builtin() {
  }
;
}
