package jtom.adt;

abstract public class TomTerm_PairSlotApplImpl
extends TomTerm
{
  TomTerm_PairSlotApplImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_slotName = 0;
  private static int index_appl = 1;
  public shared.SharedObject duplicate() {
    TomTerm_PairSlotAppl clone = new TomTerm_PairSlotAppl(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomTerm_PairSlotAppl) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_PairSlotAppl(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isPairSlotAppl()
  {
    return true;
  }

  public boolean hasSlotName()
  {
    return true;
  }

  public boolean hasAppl()
  {
    return true;
  }

  public TomName getSlotName()
  {
    return (TomName) this.getArgument(index_slotName) ;
  }

  public TomTerm setSlotName(TomName _slotName)
  {
    return (TomTerm) super.setArgument(_slotName, index_slotName);
  }

  public TomTerm getAppl()
  {
    return (TomTerm) this.getArgument(index_appl) ;
  }

  public TomTerm setAppl(TomTerm _appl)
  {
    return (TomTerm) super.setArgument(_appl, index_appl);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomName)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_PairSlotAppl should have type TomName");
        }
        break;
      case 1:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_PairSlotAppl should have type TomTerm");
        }
        break;
      default: throw new RuntimeException("TomTerm_PairSlotAppl does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = getArgument(1).hashCode() + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
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
