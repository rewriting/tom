package jtom.adt;

import java.io.InputStream;
import java.io.IOException;
import aterm.*;


abstract public class TomSymbolTableImpl extends TomSignatureConstructor
{
  static TomSymbolTable fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }

  static TomSymbolTable fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }

  public boolean isEqual(TomSymbolTable peer)
  {
    return term.isEqual(peer.toTerm());
  }

  public static TomSymbolTable fromTerm(aterm.ATerm trm)
  {
    TomSymbolTable tmp;
    if ((tmp = TomSymbolTable_Table.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomSymbolTable: " + trm);
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

