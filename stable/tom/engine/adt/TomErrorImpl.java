package jtom.adt;

import aterm.*;

abstract public class TomErrorImpl extends TomSignatureConstructor
{
  protected TomErrorImpl(TomSignatureFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(TomError peer)
  {
    return super.isEqual(peer);
  }
  public boolean isSortTomError()  {
    return true;
  }

  public boolean isError()
  {
    return false;
  }

  public boolean hasMessage()
  {
    return false;
  }

  public boolean hasFile()
  {
    return false;
  }

  public boolean hasLine()
  {
    return false;
  }

  public boolean hasLevel()
  {
    return false;
  }

  public String getMessage()
  {
     throw new UnsupportedOperationException("This TomError has no Message");
  }

  public TomError setMessage(String _message)
  {
     throw new IllegalArgumentException("Illegal argument: " + _message);
  }

  public String getFile()
  {
     throw new UnsupportedOperationException("This TomError has no File");
  }

  public TomError setFile(String _file)
  {
     throw new IllegalArgumentException("Illegal argument: " + _file);
  }

  public int getLine()
  {
     throw new UnsupportedOperationException("This TomError has no Line");
  }

  public TomError setLine(int _line)
  {
     throw new IllegalArgumentException("Illegal argument: " + _line);
  }

  public int getLevel()
  {
     throw new UnsupportedOperationException("This TomError has no Level");
  }

  public TomError setLevel(int _level)
  {
     throw new IllegalArgumentException("Illegal argument: " + _level);
  }

}

