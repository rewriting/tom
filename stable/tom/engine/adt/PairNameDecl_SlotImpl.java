package jtom.adt;

abstract public class PairNameDecl_SlotImpl
extends PairNameDecl
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_slotName = 0;
  private static int index_slotDecl = 1;
  public shared.SharedObject duplicate() {
    PairNameDecl_Slot clone = new PairNameDecl_Slot();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makePairNameDecl_Slot(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("Slot(<term>,<term>)");
  }

  static public PairNameDecl fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      PairNameDecl tmp = getStaticTomSignatureFactory().makePairNameDecl_Slot(TomName.fromTerm( (aterm.ATerm) children.get(0)), Declaration.fromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
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
    int b = 0x9e3779b9;
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
