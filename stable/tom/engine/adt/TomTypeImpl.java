package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomTypeImpl extends TomSignatureConstructor
{
  TomTypeImpl(TomSignatureFactory factory) {
     super(factory);
  }
  public boolean isEqual(TomType peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public boolean isSortTomType()  {
    return true;
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

  public boolean hasDomain()
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

  public TomTypeList getDomain()
  {
     throw new RuntimeException("This TomType has no Domain");
  }

  public TomType setDomain(TomTypeList _domain)
  {
     throw new RuntimeException("This TomType has no Domain");
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

