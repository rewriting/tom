package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomNameImpl extends TomSignatureConstructor
{
  TomNameImpl(TomSignatureFactory factory) {
     super(factory);
  }
  public boolean isEqual(TomName peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public boolean isSortTomName()  {
    return true;
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

  public TomNumberList getNumberList()
  {
     throw new RuntimeException("This TomName has no NumberList");
  }

  public TomName setNumberList(TomNumberList _numberList)
  {
     throw new RuntimeException("This TomName has no NumberList");
  }

}

