package jtom.adt;

import java.io.InputStream;
import java.io.IOException;
import aterm.*;


abstract public class TargetLanguageImpl extends TomSignatureConstructor
{
  static TargetLanguage fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }

  static TargetLanguage fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }

  public boolean isEqual(TargetLanguage peer)
  {
    return term.isEqual(peer.toTerm());
  }

  public static TargetLanguage fromTerm(aterm.ATerm trm)
  {
    TargetLanguage tmp;
    if ((tmp = TargetLanguage_TL.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TargetLanguage: " + trm);
  }


  public boolean isTL()
  {
    return false;
  }

  public boolean hasCode()
  {
    return false;
  }


  public String getCode()
  {
     throw new RuntimeException("This TargetLanguage has no Code");
  }

  public TargetLanguage setCode(String _code)
  {
     throw new RuntimeException("This TargetLanguage has no Code");
  }



}

