package jtom.adt;

abstract public class TomTerm_MatchingConditionImpl
extends TomTerm
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TomTerm_MatchingConditionImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_lhs = 0;
  private static int index_rhs = 1;
  public shared.SharedObject duplicate() {
    TomTerm_MatchingCondition clone = new TomTerm_MatchingCondition(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomTerm_MatchingCondition) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_MatchingCondition(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isMatchingCondition()
  {
    return true;
  }

  public boolean hasLhs()
  {
    return true;
  }

  public boolean hasRhs()
  {
    return true;
  }

  public TomTerm getLhs()
  {
    return (TomTerm) this.getArgument(index_lhs) ;
  }

  public TomTerm setLhs(TomTerm _lhs)
  {
    return (TomTerm) super.setArgument(_lhs, index_lhs);
  }

  public TomTerm getRhs()
  {
    return (TomTerm) this.getArgument(index_rhs) ;
  }

  public TomTerm setRhs(TomTerm _rhs)
  {
    return (TomTerm) super.setArgument(_rhs, index_rhs);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_MatchingCondition should have type TomTerm");
        }
        break;
      case 1:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_MatchingCondition should have type TomTerm");
        }
        break;
      default: throw new RuntimeException("TomTerm_MatchingCondition does not have an argument at " + i );
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
