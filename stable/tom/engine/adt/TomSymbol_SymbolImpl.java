package jtom.adt;

abstract public class TomSymbol_SymbolImpl
extends TomSymbol
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TomSymbol_SymbolImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_astName = 0;
  private static int index_typesToType = 1;
  private static int index_slotList = 2;
  private static int index_option = 3;
  private static int index_tlCode = 4;
  public shared.SharedObject duplicate() {
    TomSymbol_Symbol clone = new TomSymbol_Symbol(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomSymbol_Symbol) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomSymbol_Symbol(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isSymbol()
  {
    return true;
  }

  public boolean hasAstName()
  {
    return true;
  }

  public boolean hasTypesToType()
  {
    return true;
  }

  public boolean hasSlotList()
  {
    return true;
  }

  public boolean hasOption()
  {
    return true;
  }

  public boolean hasTlCode()
  {
    return true;
  }

  public TomName getAstName()
  {
    return (TomName) this.getArgument(index_astName) ;
  }

  public TomSymbol setAstName(TomName _astName)
  {
    return (TomSymbol) super.setArgument(_astName, index_astName);
  }

  public TomType getTypesToType()
  {
    return (TomType) this.getArgument(index_typesToType) ;
  }

  public TomSymbol setTypesToType(TomType _typesToType)
  {
    return (TomSymbol) super.setArgument(_typesToType, index_typesToType);
  }

  public SlotList getSlotList()
  {
    return (SlotList) this.getArgument(index_slotList) ;
  }

  public TomSymbol setSlotList(SlotList _slotList)
  {
    return (TomSymbol) super.setArgument(_slotList, index_slotList);
  }

  public Option getOption()
  {
    return (Option) this.getArgument(index_option) ;
  }

  public TomSymbol setOption(Option _option)
  {
    return (TomSymbol) super.setArgument(_option, index_option);
  }

  public TargetLanguage getTlCode()
  {
    return (TargetLanguage) this.getArgument(index_tlCode) ;
  }

  public TomSymbol setTlCode(TargetLanguage _tlCode)
  {
    return (TomSymbol) super.setArgument(_tlCode, index_tlCode);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomName)) { 
          throw new RuntimeException("Argument 0 of a TomSymbol_Symbol should have type TomName");
        }
        break;
      case 1:
        if (! (arg instanceof TomType)) { 
          throw new RuntimeException("Argument 1 of a TomSymbol_Symbol should have type TomType");
        }
        break;
      case 2:
        if (! (arg instanceof SlotList)) { 
          throw new RuntimeException("Argument 2 of a TomSymbol_Symbol should have type SlotList");
        }
        break;
      case 3:
        if (! (arg instanceof Option)) { 
          throw new RuntimeException("Argument 3 of a TomSymbol_Symbol should have type Option");
        }
        break;
      case 4:
        if (! (arg instanceof TargetLanguage)) { 
          throw new RuntimeException("Argument 4 of a TomSymbol_Symbol should have type TargetLanguage");
        }
        break;
      default: throw new RuntimeException("TomSymbol_Symbol does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
    b += (getArgument(4).hashCode() << 0);
    a += (getArgument(3).hashCode() << 24);
    a += (getArgument(2).hashCode() << 16);
    a += (getArgument(1).hashCode() << 8);
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
