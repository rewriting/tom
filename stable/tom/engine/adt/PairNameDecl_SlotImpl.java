package jtom.adt;

abstract public class PairNameDecl_SlotImpl
extends PairNameDecl
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected PairNameDecl_SlotImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_slotName = 0;
  private static int index_slotDecl = 1;
  public shared.SharedObject duplicate() {
    PairNameDecl_Slot clone = new PairNameDecl_Slot(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof PairNameDecl_Slot) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makePairNameDecl_Slot(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isSlot()
  {
    return true;
  }

  public boolean hasSlotName()
  {
    return true;
  }

  public boolean hasSlotDecl()
  {
    return true;
  }

  public TomName getSlotName()
  {
    return (TomName) this.getArgument(index_slotName) ;
  }

  public PairNameDecl setSlotName(TomName _slotName)
  {
    return (PairNameDecl) super.setArgument(_slotName, index_slotName);
  }

  public Declaration getSlotDecl()
  {
    return (Declaration) this.getArgument(index_slotDecl) ;
  }

  public PairNameDecl setSlotDecl(Declaration _slotDecl)
  {
    return (PairNameDecl) super.setArgument(_slotDecl, index_slotDecl);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomName)) { 
          throw new RuntimeException("Argument 0 of a PairNameDecl_Slot should have type TomName");
        }
        break;
      case 1:
        if (! (arg instanceof Declaration)) { 
          throw new RuntimeException("Argument 1 of a PairNameDecl_Slot should have type Declaration");
        }
        break;
      default: throw new RuntimeException("PairNameDecl_Slot does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
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
