package jtom.adt;

abstract public class TomEntry_EntryImpl
extends TomEntry
{
  TomEntry_EntryImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_strName = 0;
  private static int index_astSymbol = 1;
  public shared.SharedObject duplicate() {
    TomEntry_Entry clone = new TomEntry_Entry(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomEntry_Entry) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomEntry_Entry(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isEntry()
  {
    return true;
  }

  public boolean hasStrName()
  {
    return true;
  }

  public boolean hasAstSymbol()
  {
    return true;
  }

  public String getStrName()
  {
   return ((aterm.ATermAppl) this.getArgument(index_strName)).getAFun().getName();
  }

  public TomEntry setStrName(String _strName)
  {
    return (TomEntry) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_strName, 0, true)), index_strName);
  }

  public TomSymbol getAstSymbol()
  {
    return (TomSymbol) this.getArgument(index_astSymbol) ;
  }

  public TomEntry setAstSymbol(TomSymbol _astSymbol)
  {
    return (TomEntry) super.setArgument(_astSymbol, index_astSymbol);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TomEntry_Entry should have type str");
        }
        break;
      case 1:
        if (! (arg instanceof TomSymbol)) { 
          throw new RuntimeException("Argument 1 of a TomEntry_Entry should have type TomSymbol");
        }
        break;
      default: throw new RuntimeException("TomEntry_Entry does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
