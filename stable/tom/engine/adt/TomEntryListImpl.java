package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomEntryListImpl extends TomSignatureConstructor
{
  public static TomEntryList fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }
  public static TomEntryList fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }
  public boolean isEqual(TomEntryList peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public static TomEntryList fromTerm(aterm.ATerm trm)
  {
    TomEntryList tmp;
    if ((tmp = TomEntryList_EmptyEntryList.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomEntryList_ConsEntryList.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomEntryList: " + trm);
  }

  public boolean isEmptyEntryList()
  {
    return false;
  }

  public boolean isConsEntryList()
  {
    return false;
  }

  public boolean hasHeadEntryList()
  {
    return false;
  }

  public boolean hasTailEntryList()
  {
    return false;
  }

  public TomEntry getHeadEntryList()
  {
     throw new RuntimeException("This TomEntryList has no HeadEntryList");
  }

  public TomEntryList setHeadEntryList(TomEntry _headEntryList)
  {
     throw new RuntimeException("This TomEntryList has no HeadEntryList");
  }

  public TomEntryList getTailEntryList()
  {
     throw new RuntimeException("This TomEntryList has no TailEntryList");
  }

  public TomEntryList setTailEntryList(TomEntryList _tailEntryList)
  {
     throw new RuntimeException("This TomEntryList has no TailEntryList");
  }


}

