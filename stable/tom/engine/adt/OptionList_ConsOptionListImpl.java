package jtom.adt;

import aterm.*;

public class OptionList_ConsOptionListImpl
extends OptionList
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_head = 0;
  private static int index_tail = 1;

  public shared.SharedObject duplicate() {
    OptionList_ConsOptionList clone = new OptionList_ConsOptionList();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeOptionList_ConsOptionList(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("[<term>,<list>]");
  }


  static public OptionList fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      OptionList tmp = getStaticTomSignatureFactory().makeOptionList_ConsOptionList(Option.fromTerm( (aterm.ATerm) children.get(0)), OptionList.fromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }

  public boolean isConsOptionList()
  {
    return true;
  }

  public boolean hasHead()
  {
    return true;
  }

  public boolean hasTail()
  {
    return true;
  }


  public Option getHead()
  {
    return (Option) this.getArgument(index_head) ;
  }

  public OptionList setHead(Option _head)
  {
    return (OptionList) super.setArgument(_head, index_head);
  }

  public OptionList getTail()
  {
    return (OptionList) this.getArgument(index_tail) ;
  }

  public OptionList setTail(OptionList _tail)
  {
    return (OptionList) super.setArgument(_tail, index_tail);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof Option)) { 
          throw new RuntimeException("Argument 0 of a OptionList_ConsOptionList should have type Option");
        }
        break;
      case 1:
        if (! (arg instanceof OptionList)) { 
          throw new RuntimeException("Argument 1 of a OptionList_ConsOptionList should have type OptionList");
        }
        break;
      default: throw new RuntimeException("OptionList_ConsOptionList does not have an argument at " + i );
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
