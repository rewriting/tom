package jtom.adt;

import java.io.InputStream;
import java.io.IOException;
import aterm.*;


abstract public class TomSymbolImpl extends TomSignatureConstructor
{
  static TomSymbol fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }

  static TomSymbol fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }

  public boolean isEqual(TomSymbol peer)
  {
    return term.isEqual(peer.toTerm());
  }

  public static TomSymbol fromTerm(aterm.ATerm trm)
  {
    TomSymbol tmp;
    if ((tmp = TomSymbol_Symbol.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomSymbol: " + trm);
  }


  public boolean isSymbol()
  {
    return false;
  }

  public boolean hasAstName()
  {
    return false;
  }

  public boolean hasTypesToType()
  {
    return false;
  }

  public boolean hasOption()
  {
    return false;
  }

  public boolean hasTlCode()
  {
    return false;
  }


  public TomName getAstName()
  {
     throw new RuntimeException("This TomSymbol has no AstName");
  }

  public TomSymbol setAstName(TomName _astName)
  {
     throw new RuntimeException("This TomSymbol has no AstName");
  }

  public TomType getTypesToType()
  {
     throw new RuntimeException("This TomSymbol has no TypesToType");
  }

  public TomSymbol setTypesToType(TomType _typesToType)
  {
     throw new RuntimeException("This TomSymbol has no TypesToType");
  }

  public Option getOption()
  {
     throw new RuntimeException("This TomSymbol has no Option");
  }

  public TomSymbol setOption(Option _option)
  {
     throw new RuntimeException("This TomSymbol has no Option");
  }

  public TomTerm getTlCode()
  {
     throw new RuntimeException("This TomSymbol has no TlCode");
  }

  public TomSymbol setTlCode(TomTerm _tlCode)
  {
     throw new RuntimeException("This TomSymbol has no TlCode");
  }



}

