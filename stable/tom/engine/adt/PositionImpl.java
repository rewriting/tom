package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

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
    return term.isEqual(peer.toTerm());
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

  public Integer getLine()
  {
     throw new RuntimeException("This Position has no Line");
  }

  public Position setLine(Integer _line)
  {
     throw new RuntimeException("This Position has no Line");
  }

  public Integer getColumn()
  {
     throw new RuntimeException("This Position has no Column");
  }

  public Position setColumn(Integer _column)
  {
     throw new RuntimeException("This Position has no Column");
  }

}

