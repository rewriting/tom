package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomStructureTableImpl extends TomSignatureConstructor
{
  public static TomStructureTable fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }
  public static TomStructureTable fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }
  public boolean isEqual(TomStructureTable peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public static TomStructureTable fromTerm(aterm.ATerm trm)
  {
    TomStructureTable tmp;
    if ((tmp = TomStructureTable_StructTable.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomStructureTable: " + trm);
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

