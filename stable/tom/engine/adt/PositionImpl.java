package jtom.adt;

import aterm.*;

abstract public class PositionImpl extends TomSignatureConstructor
{
  protected PositionImpl(TomSignatureFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(Position peer)
  {
    return super.isEqual(peer);
  }
  public boolean isSortPosition()  {
    return true;
  }

  public boolean isPosition()
  {
    return false;
  }

  public boolean hasLine()
  {
    return false;
  }

  public boolean hasColumn()
  {
    return false;
  }

  public int getLine()
  {
     throw new UnsupportedOperationException("This Position has no Line");
  }

  public Position setLine(int _line)
  {
     throw new IllegalArgumentException("Illegal argument: " + _line);
  }

  public int getColumn()
  {
     throw new UnsupportedOperationException("This Position has no Column");
  }

  public Position setColumn(int _column)
  {
     throw new IllegalArgumentException("Illegal argument: " + _column);
  }

}

