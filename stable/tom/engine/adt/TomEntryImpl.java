package jtom.adt;

import java.io.InputStream;
import java.io.IOException;
import aterm.*;


abstract public class TomEntryImpl extends TomSignatureConstructor
{
  static TomEntry fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }

  static TomEntry fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }

  public boolean isEqual(TomEntry peer)
  {
    return term.isEqual(peer.toTerm());
  }

  public static TomEntry fromTerm(aterm.ATerm trm)
  {
    TomEntry tmp;
    if ((tmp = TomEntry_Entry.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomEntry: " + trm);
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

