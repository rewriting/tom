package jtom.adt;

import aterm.*;

public class TomSymbolTable_TableImpl
extends TomSymbolTable
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_entryList = 0;

  public shared.SharedObject duplicate() {
    TomSymbolTable_Table clone = new TomSymbolTable_Table();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomSymbolTable_Table(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("Table(<term>)");
  }


  static public TomSymbolTable fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      TomSymbolTable tmp = getStaticTomSignatureFactory().makeTomSymbolTable_Table(TomEntryList.fromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
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
    int b = 0x9e3779b9;
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
