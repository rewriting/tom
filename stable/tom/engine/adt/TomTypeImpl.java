package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomTypeImpl extends TomSignatureConstructor
{
  public static TomType fromString(String str)
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().parse(str);
    return fromTerm(trm);
  }
  public static TomType fromTextFile(InputStream stream) throws aterm.ParseError, IOException
  {
    aterm.ATerm trm = getStaticTomSignatureFactory().readFromTextFile(stream);
    return fromTerm(trm);
  }
  public boolean isEqual(TomType peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public static TomType fromTerm(aterm.ATerm trm)
  {
    TomType tmp;
    if ((tmp = TomType_Type.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomType_TypesToType.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomType_TomType.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomType_TomTypeAlone.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomType_TLType.fromTerm(trm)) != null) {
      return tmp;
    }

    if ((tmp = TomType_EmptyType.fromTerm(trm)) != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TomType: " + trm);
  }

  public boolean isType()
  {
    return false;
  }

  public boolean isTypesToType()
  {
    return false;
  }

  public boolean isTomType()
  {
    return false;
  }

  public boolean isTomTypeAlone()
  {
    return false;
  }

  public boolean isTLType()
  {
    return false;
  }

  public boolean isEmptyType()
  {
    return false;
  }

  public boolean hasTomType()
  {
    return false;
  }

  public boolean hasTlType()
  {
    return false;
  }

  public boolean hasList()
  {
    return false;
  }

  public boolean hasCodomain()
  {
    return false;
  }

  public boolean hasString()
  {
    return false;
  }

  public boolean hasTl()
  {
    return false;
  }

  public TomType getTomType()
  {
     throw new RuntimeException("This TomType has no TomType");
  }

  public TomType setTomType(TomType _tomType)
  {
     throw new RuntimeException("This TomType has no TomType");
  }

  public TomType getTlType()
  {
     throw new RuntimeException("This TomType has no TlType");
  }

  public TomType setTlType(TomType _tlType)
  {
     throw new RuntimeException("This TomType has no TlType");
  }

  public TomList getList()
  {
     throw new RuntimeException("This TomType has no List");
  }

  public TomType setList(TomList _list)
  {
     throw new RuntimeException("This TomType has no List");
  }

  public TomType getCodomain()
  {
     throw new RuntimeException("This TomType has no Codomain");
  }

  public TomType setCodomain(TomType _codomain)
  {
     throw new RuntimeException("This TomType has no Codomain");
  }

  public String getString()
  {
     throw new RuntimeException("This TomType has no String");
  }

  public TomType setString(String _string)
  {
     throw new RuntimeException("This TomType has no String");
  }

  public TargetLanguage getTl()
  {
     throw new RuntimeException("This TomType has no Tl");
  }

  public TomType setTl(TargetLanguage _tl)
  {
     throw new RuntimeException("This TomType has no Tl");
  }


}

