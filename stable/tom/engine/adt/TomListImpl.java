package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomListImpl extends TomSignatureConstructor
{
  public static TomList fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }
  public static TomList fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }
  public boolean isEqual(TomList peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public static TomList fromTerm(aterm.ATerm trm)
  {
    TomList tmp;
    if ((tmp = TomList_Empty.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomList_Cons.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomList: " + trm);
  }

  public boolean isEmpty()
  {
    return false;
  }

  public boolean isCons()
  {
    return false;
  }

  public boolean hasHead()
  {
    return false;
  }

  public boolean hasTail()
  {
    return false;
  }

  public TomTerm getHead()
  {
     throw new RuntimeException("This TomList has no Head");
  }

  public TomList setHead(TomTerm _head)
  {
     throw new RuntimeException("This TomList has no Head");
  }

  public TomList getTail()
  {
     throw new RuntimeException("This TomList has no Tail");
  }

  public TomList setTail(TomList _tail)
  {
     throw new RuntimeException("This TomList has no Tail");
  }


}

