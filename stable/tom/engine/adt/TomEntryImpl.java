package jtom.adt;


abstract public class TomEntryImpl extends TomSignatureConstructor
{
  protected TomEntryImpl(TomSignatureFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(TomEntry peer)
  {
    return super.isEqual(peer);
  }
  public boolean isSortTomEntry()  {
    return true;
  }

  public boolean isEntry()
  {
    return false;
  }

  public boolean hasStrName()
  {
    return false;
  }

  public boolean hasAstSymbol()
  {
    return false;
  }

  public String getStrName()
  {
     throw new UnsupportedOperationException("This TomEntry has no StrName");
  }

  public TomEntry setStrName(String _strName)
  {
     throw new IllegalArgumentException("Illegal argument: " + _strName);
  }

  public TomSymbol getAstSymbol()
  {
     throw new UnsupportedOperationException("This TomEntry has no AstSymbol");
  }

  public TomEntry setAstSymbol(TomSymbol _astSymbol)
  {
     throw new IllegalArgumentException("Illegal argument: " + _astSymbol);
  }

}

