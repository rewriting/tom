package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomEntryImpl extends TomSignatureConstructor
{
  TomEntryImpl(TomSignatureFactory factory) {
     super(factory);
  }
  public boolean isEqual(TomEntry peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public boolean isSortTomEntry()  {
    return true;
  }

  public boolean isEntry()
  {
    return false;
  }

  public boolean hasStrName()
  {
    return false;
  }

  public boolean hasAstSymbol()
  {
    return false;
  }

  public String getStrName()
  {
     throw new RuntimeException("This TomEntry has no StrName");
  }

  public TomEntry setStrName(String _strName)
  {
     throw new RuntimeException("This TomEntry has no StrName");
  }

  public TomSymbol getAstSymbol()
  {
     throw new RuntimeException("This TomEntry has no AstSymbol");
  }

  public TomEntry setAstSymbol(TomSymbol _astSymbol)
  {
     throw new RuntimeException("This TomEntry has no AstSymbol");
  }

}

