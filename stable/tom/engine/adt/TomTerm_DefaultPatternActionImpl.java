package jtom.adt;

abstract public class TomTerm_DefaultPatternActionImpl
extends TomTerm
{
  TomTerm_DefaultPatternActionImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_termList = 0;
  private static int index_tom = 1;
  private static int index_option = 2;
  public shared.SharedObject duplicate() {
    TomTerm_DefaultPatternAction clone = new TomTerm_DefaultPatternAction(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomTerm_DefaultPatternAction) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_DefaultPatternAction(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isDefaultPatternAction()
  {
    return true;
  }

  public boolean hasTermList()
  {
    return true;
  }

  public boolean hasTom()
  {
    return true;
  }

  public boolean hasOption()
  {
    return true;
  }

  public TomTerm getTermList()
  {
    return (TomTerm) this.getArgument(index_termList) ;
  }

  public TomTerm setTermList(TomTerm _termList)
  {
    return (TomTerm) super.setArgument(_termList, index_termList);
  }

  public TomTerm getTom()
  {
    return (TomTerm) this.getArgument(index_tom) ;
  }

  public TomTerm setTom(TomTerm _tom)
  {
    return (TomTerm) super.setArgument(_tom, index_tom);
  }

  public Option getOption()
  {
    return (Option) this.getArgument(index_option) ;
  }

  public TomTerm setOption(Option _option)
  {
    return (TomTerm) super.setArgument(_option, index_option);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_DefaultPatternAction should have type TomTerm");
        }
        break;
      case 1:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_DefaultPatternAction should have type TomTerm");
        }
        break;
      case 2:
        if (! (arg instanceof Option)) { 
          throw new RuntimeException("Argument 2 of a TomTerm_DefaultPatternAction should have type Option");
        }
        break;
      default: throw new RuntimeException("TomTerm_DefaultPatternAction does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
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
