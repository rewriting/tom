package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class XMLTermImpl extends TomSignatureConstructor
{
  XMLTermImpl(TomSignatureFactory factory) {
     super(factory);
  }
  public boolean isEqual(XMLTerm peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public boolean isSortXMLTerm()  {
    return true;
  }

  public boolean isElement()
  {
    return false;
  }

  public boolean isAttribute()
  {
    return false;
  }

  public boolean isReservedWord()
  {
    return false;
  }

  public boolean isXMLPlaceholder()
  {
    return false;
  }

  public boolean isXMLVariable()
  {
    return false;
  }

  public boolean hasAstName()
  {
    return false;
  }

  public boolean hasArgs()
  {
    return false;
  }

  public boolean hasOption()
  {
    return false;
  }

  public TomName getAstName()
  {
     throw new RuntimeException("This XMLTerm has no AstName");
  }

  public XMLTerm setAstName(TomName _astName)
  {
     throw new RuntimeException("This XMLTerm has no AstName");
  }

  public TomList getArgs()
  {
     throw new RuntimeException("This XMLTerm has no Args");
  }

  public XMLTerm setArgs(TomList _args)
  {
     throw new RuntimeException("This XMLTerm has no Args");
  }

  public Option getOption()
  {
     throw new RuntimeException("This XMLTerm has no Option");
  }

  public XMLTerm setOption(Option _option)
  {
     throw new RuntimeException("This XMLTerm has no Option");
  }

}

