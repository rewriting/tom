package jtom.runtime.set.jgtreeset;

import aterm.*;

abstract public class JGTreeSetImpl extends SetConstructor
{
  protected JGTreeSetImpl(SetFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(JGTreeSet peer)
  {
    return super.isEqual(peer);
  }
  public boolean isSortJGTreeSet()  {
    return true;
  }

  public boolean isEmptySet()
  {
    return false;
  }

  public boolean isSingleton()
  {
    return false;
  }

  public boolean isPair()
  {
    return false;
  }

  public boolean isBranch()
  {
    return false;
  }

  public boolean hasValue()
  {
    return false;
  }

  public boolean hasMultiplicity()
  {
    return false;
  }

  public boolean hasLeft()
  {
    return false;
  }

  public boolean hasRight()
  {
    return false;
  }

  public aterm.ATerm getValue()
  {
     throw new UnsupportedOperationException("This JGTreeSet has no Value");
  }

  public JGTreeSet setValue(aterm.ATerm _value)
  {
     throw new IllegalArgumentException("Illegal argument: " + _value);
  }

  public int getMultiplicity()
  {
     throw new UnsupportedOperationException("This JGTreeSet has no Multiplicity");
  }

  public JGTreeSet setMultiplicity(int _multiplicity)
  {
     throw new IllegalArgumentException("Illegal argument: " + _multiplicity);
  }

  public JGTreeSet getLeft()
  {
     throw new UnsupportedOperationException("This JGTreeSet has no Left");
  }

  public JGTreeSet setLeft(JGTreeSet _left)
  {
     throw new IllegalArgumentException("Illegal argument: " + _left);
  }

  public JGTreeSet getRight()
  {
     throw new UnsupportedOperationException("This JGTreeSet has no Right");
  }

  public JGTreeSet setRight(JGTreeSet _right)
  {
     throw new IllegalArgumentException("Illegal argument: " + _right);
  }

}

