package jtom.adt;


abstract public class TomSymbolTableImpl extends TomSignatureConstructor
{
  protected TomSymbolTableImpl(TomSignatureFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(TomSymbolTable peer)
  {
    return super.isEqual(peer);
  }
  public boolean isSortTomSymbolTable()  {
    return true;
  }

  public boolean isTable()
  {
    return false;
  }

  public boolean hasEntryList()
  {
    return false;
  }

  public TomEntryList getEntryList()
  {
     throw new UnsupportedOperationException("This TomSymbolTable has no EntryList");
  }

  public TomSymbolTable setEntryList(TomEntryList _entryList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _entryList);
  }

}

