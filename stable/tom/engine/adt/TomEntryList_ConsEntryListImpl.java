package jtom.adt;

import aterm.*;

public class TomEntryList_ConsEntryListImpl
extends TomEntryList
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_headEntryList = 0;
  private static int index_tailEntryList = 1;

  public shared.SharedObject duplicate() {
    TomEntryList_ConsEntryList clone = new TomEntryList_ConsEntryList();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomEntryList_ConsEntryList(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("[<term>,<list>]");
  }


  static public TomEntryList fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      TomEntryList tmp = getStaticTomSignatureFactory().makeTomEntryList_ConsEntryList(TomEntry.fromTerm( (aterm.ATerm) children.get(0)), TomEntryList.fromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }

  public boolean isConsEntryList()
  {
    return true;
  }

  public boolean hasHeadEntryList()
  {
    return true;
  }

  public boolean hasTailEntryList()
  {
    return true;
  }


  public TomEntry getHeadEntryList()
  {
    return (TomEntry) this.getArgument(index_headEntryList) ;
  }

  public TomEntryList setHeadEntryList(TomEntry _headEntryList)
  {
    return (TomEntryList) super.setArgument(_headEntryList, index_headEntryList);
  }

  public TomEntryList getTailEntryList()
  {
    return (TomEntryList) this.getArgument(index_tailEntryList) ;
  }

  public TomEntryList setTailEntryList(TomEntryList _tailEntryList)
  {
    return (TomEntryList) super.setArgument(_tailEntryList, index_tailEntryList);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomEntry)) { 
          throw new RuntimeException("Argument 0 of a TomEntryList_ConsEntryList should have type TomEntry");
        }
        break;
      case 1:
        if (! (arg instanceof TomEntryList)) { 
          throw new RuntimeException("Argument 1 of a TomEntryList_ConsEntryList should have type TomEntryList");
        }
        break;
      default: throw new RuntimeException("TomEntryList_ConsEntryList does not have an argument at " + i );
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
