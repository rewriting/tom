package jtom.adt;

abstract public class SlotList_ConsSlotListImpl
extends SlotList
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_headSlotList = 0;
  private static int index_tailSlotList = 1;
  public shared.SharedObject duplicate() {
    SlotList_ConsSlotList clone = new SlotList_ConsSlotList();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeSlotList_ConsSlotList(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("[<term>,<list>]");
  }

  static public SlotList fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      SlotList tmp = getStaticTomSignatureFactory().makeSlotList_ConsSlotList(PairNameDecl.fromTerm( (aterm.ATerm) children.get(0)), SlotList.fromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  public boolean isConsSlotList()
  {
    return true;
  }

  public boolean hasHeadSlotList()
  {
    return true;
  }

  public boolean hasTailSlotList()
  {
    return true;
  }

  public PairNameDecl getHeadSlotList()
  {
    return (PairNameDecl) this.getArgument(index_headSlotList) ;
  }

  public SlotList setHeadSlotList(PairNameDecl _headSlotList)
  {
    return (SlotList) super.setArgument(_headSlotList, index_headSlotList);
  }

  public SlotList getTailSlotList()
  {
    return (SlotList) this.getArgument(index_tailSlotList) ;
  }

  public SlotList setTailSlotList(SlotList _tailSlotList)
  {
    return (SlotList) super.setArgument(_tailSlotList, index_tailSlotList);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof PairNameDecl)) { 
          throw new RuntimeException("Argument 0 of a SlotList_ConsSlotList should have type PairNameDecl");
        }
        break;
      case 1:
        if (! (arg instanceof SlotList)) { 
          throw new RuntimeException("Argument 1 of a SlotList_ConsSlotList should have type SlotList");
        }
        break;
      default: throw new RuntimeException("SlotList_ConsSlotList does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = getArgument(1).hashCode() + (getAnnotations().hashCode()<<8);
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
