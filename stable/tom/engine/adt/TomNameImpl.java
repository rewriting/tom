package jtom.adt;


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
    return super.isEqual(peer);
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
     throw new UnsupportedOperationException("This TomName has no String");
  }

  public TomName setString(String _string)
  {
     throw new IllegalArgumentException("Illegal argument: " + _string);
  }

  public TomNumberList getNumberList()
  {
     throw new UnsupportedOperationException("This TomName has no NumberList");
  }

  public TomName setNumberList(TomNumberList _numberList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _numberList);
  }

}

