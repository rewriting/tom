package jtom.adt;


abstract public class TomStructureTableImpl extends TomSignatureConstructor
{
  protected TomStructureTableImpl(TomSignatureFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(TomStructureTable peer)
  {
    return super.isEqual(peer);
  }
  public boolean isSortTomStructureTable()  {
    return true;
  }

  public boolean isStructTable()
  {
    return false;
  }

  public boolean hasStructList()
  {
    return false;
  }

  public TomList getStructList()
  {
     throw new UnsupportedOperationException("This TomStructureTable has no StructList");
  }

  public TomStructureTable setStructList(TomList _structList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _structList);
  }

}

