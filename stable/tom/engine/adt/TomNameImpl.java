package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomNameImpl extends TomSignatureConstructor
{
  static TomName fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }
  static TomName fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }
  public boolean isEqual(TomName peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public static TomName fromTerm(aterm.ATerm trm)
  {
    TomName tmp;
    if ((tmp = TomName_Name.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomName_PositionName.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomName_EmptyName.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomName: " + trm);
  }

  public boolean isName()
  {
    return false;
  }

  public boolean isPositionName()
  {
    return false;
  }

  public boolean isEmptyName()
  {
    return false;
  }

  public boolean hasString()
  {
    return false;
  }

  public boolean hasNumberList()
  {
    return false;
  }

  public String getString()
  {
     throw new RuntimeException("This TomName has no String");
  }

  public TomName setString(String _string)
  {
     throw new RuntimeException("This TomName has no String");
  }

  public TomList getNumberList()
  {
     throw new RuntimeException("This TomName has no NumberList");
  }

  public TomName setNumberList(TomList _numberList)
  {
     throw new RuntimeException("This TomName has no NumberList");
  }


}

