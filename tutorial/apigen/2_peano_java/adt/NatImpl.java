package adt;

import java.io.InputStream;
import java.io.IOException;


abstract public class NatImpl extends PeanoConstructor
{
  static Nat fromString(String str)
  {
    aterm.ATerm trm = getStaticPeanoFactory().parse(str);
    return fromTerm(trm);
  }

  static Nat fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticPeanoFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }

  public boolean isEqual(Nat peer)
  {
    return term.isEqual(peer.toTerm());
  }

  public static Nat fromTerm(aterm.ATerm trm)
  {
    Nat tmp;
    if ((tmp = Nat_Zero.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = Nat_Suc.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a Nat: " + trm);
  }


  public boolean isZero()
  {
    return false;
  }

  public boolean isSuc()
  {
    return false;
  }

  public boolean hasPred()
  {
    return false;
  }


  public Nat getPred()
  {
     throw new RuntimeException("This Nat has no Pred");
  }

  public Nat setPred(Nat _pred)
  {
     throw new RuntimeException("This Nat has no Pred");
  }



}

