package jtom.runtime.set.jgtreeset;

import java.io.InputStream;
import java.io.IOException;

abstract public class JGTreeSetImpl extends SetConstructor
{
  static JGTreeSet fromString(String str)
  {
    aterm.ATerm trm = getStaticFactory().parse(str);
    return fromTerm(trm);
  }
  static JGTreeSet fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }
  public boolean isEqual(JGTreeSet peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public static JGTreeSet fromTerm(aterm.ATerm trm)
  {
    JGTreeSet tmp;
    if ((tmp = JGTreeSet_EmptySet.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = JGTreeSet_Singleton.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = JGTreeSet_Pair.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = JGTreeSet_Branch.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a JGTreeSet: " + trm);
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
     throw new RuntimeException("This JGTreeSet has no Value");
  }

  public JGTreeSet setValue(aterm.ATerm _value)
  {
     throw new RuntimeException("This JGTreeSet has no Value");
  }

  public Integer getMultiplicity()
  {
     throw new RuntimeException("This JGTreeSet has no Multiplicity");
  }

  public JGTreeSet setMultiplicity(Integer _multiplicity)
  {
     throw new RuntimeException("This JGTreeSet has no Multiplicity");
  }

  public JGTreeSet getLeft()
  {
     throw new RuntimeException("This JGTreeSet has no Left");
  }

  public JGTreeSet setLeft(JGTreeSet _left)
  {
     throw new RuntimeException("This JGTreeSet has no Left");
  }

  public JGTreeSet getRight()
  {
     throw new RuntimeException("This JGTreeSet has no Right");
  }

  public JGTreeSet setRight(JGTreeSet _right)
  {
     throw new RuntimeException("This JGTreeSet has no Right");
  }


}

