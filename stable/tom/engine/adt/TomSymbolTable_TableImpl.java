package jtom.adt;

abstract public class TomSymbolTable_TableImpl
extends TomSymbolTable
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TomSymbolTable_TableImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_entryList = 0;
  public shared.SharedObject duplicate() {
    TomSymbolTable_Table clone = new TomSymbolTable_Table(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomSymbolTable_Table) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomSymbolTable_Table(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isTable()
  {
    return true;
  }

  public boolean hasEntryList()
  {
    return true;
  }

  public TomEntryList getEntryList()
  {
    return (TomEntryList) this.getArgument(index_entryList) ;
  }

  public TomSymbolTable setEntryList(TomEntryList _entryList)
  {
    return (TomSymbolTable) super.setArgument(_entryList, index_entryList);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomEntryList)) { 
          throw new RuntimeException("Argument 0 of a TomSymbolTable_Table should have type TomEntryList");
        }
        break;
      default: throw new RuntimeException("TomSymbolTable_Table does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
    a += (getArgument(0).hashCode() << 0);

    a -= b; a -= c; a ^= (c >> 13);
    b -= c; b -= a; b ^= (a << 8);
    c -= a; c -= b; c ^= (b >> 13);
    a -= b; a -= c; a ^= (c >> 12);
    b -= c; b -= a; b ^= (a << 16);
    c -= a; c -= b; c ^= (b >> 5);
    a -= b; a -= c; a ^= (c >> 3);
    b -= c; b -= a; b ^= (a << 10);
    c -= a; c -= b; c ^= (b >> 15);

    return c;
  }
}
