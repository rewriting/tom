package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomSymbolTableImpl extends TomSignatureConstructor
{
  TomSymbolTableImpl(TomSignatureFactory factory) {
     super(factory);
  }
  public boolean isEqual(TomSymbolTable peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public boolean isSortTomSymbolTable()  {
    return true;
  }

  public boolean isTable()
  {
    return false;
  }

  public boolean hasEntryList()
  {
    return false;
  }

  public TomEntryList getEntryList()
  {
     throw new RuntimeException("This TomSymbolTable has no EntryList");
  }

  public TomSymbolTable setEntryList(TomEntryList _entryList)
  {
     throw new RuntimeException("This TomSymbolTable has no EntryList");
  }

}

