package jtom.adt;

import aterm.*;

abstract public class XMLTermImpl extends TomSignatureConstructor
{
  protected XMLTermImpl(TomSignatureFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(XMLTerm peer)
  {
    return super.isEqual(peer);
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
     throw new UnsupportedOperationException("This XMLTerm has no AstName");
  }

  public XMLTerm setAstName(TomName _astName)
  {
     throw new IllegalArgumentException("Illegal argument: " + _astName);
  }

  public TomList getArgs()
  {
     throw new UnsupportedOperationException("This XMLTerm has no Args");
  }

  public XMLTerm setArgs(TomList _args)
  {
     throw new IllegalArgumentException("Illegal argument: " + _args);
  }

  public Option getOption()
  {
     throw new UnsupportedOperationException("This XMLTerm has no Option");
  }

  public XMLTerm setOption(Option _option)
  {
     throw new IllegalArgumentException("Illegal argument: " + _option);
  }

}

