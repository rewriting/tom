package jtom.adt;

import aterm.*;
import java.io.InputStream;
import java.io.IOException;

abstract public class TomNameImpl extends TomSignatureConstructor
{
  protected TomNameImpl(TomSignatureFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(TomName peer)
  {
    return term.isEqual(peer.toTerm());
  }
  public boolean isSortTomName()  {
    return true;
  }

  public boolean isName()
  {
    return false;
  }

  public boolean isPositionName()
  {
    return false;
  }

  public boolean isEmptyName()
  {
    return false;
  }

  public boolean hasString()
  {
    return false;
  }

  public boolean hasNumberList()
  {
    return false;
  }

  public String getString()
  {
     throw new RuntimeException("This TomName has no String");
  }

  public TomName setString(String _string)
  {
     throw new RuntimeException("This TomName has no String");
  }

  public TomNumberList getNumberList()
  {
     throw new RuntimeException("This TomName has no NumberList");
  }

  public TomName setNumberList(TomNumberList _numberList)
  {
     throw new RuntimeException("This TomName has no NumberList");
  }

}

