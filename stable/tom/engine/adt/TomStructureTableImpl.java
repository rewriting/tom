package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomStructureTableImpl extends TomSignatureConstructor
{
  TomStructureTableImpl(TomSignatureFactory factory) {
     super(factory);
  }
  public boolean isEqual(TomStructureTable peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public boolean isSortTomStructureTable()  {
    return true;
  }

  public boolean isStructTable()
  {
    return false;
  }

  public boolean hasStructList()
  {
    return false;
  }

  public TomList getStructList()
  {
     throw new RuntimeException("This TomStructureTable has no StructList");
  }

  public TomStructureTable setStructList(TomList _structList)
  {
     throw new RuntimeException("This TomStructureTable has no StructList");
  }

}

