package jtom.adt;

abstract public class TomTerm_TomRuleToTomTermImpl
extends TomTerm
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TomTerm_TomRuleToTomTermImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_astTomRule = 0;
  public shared.SharedObject duplicate() {
    TomTerm_TomRuleToTomTerm clone = new TomTerm_TomRuleToTomTerm(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomTerm_TomRuleToTomTerm) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_TomRuleToTomTerm(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isTomRuleToTomTerm()
  {
    return true;
  }

  public boolean hasAstTomRule()
  {
    return true;
  }

  public TomRule getAstTomRule()
  {
    return (TomRule) this.getArgument(index_astTomRule) ;
  }

  public TomTerm setAstTomRule(TomRule _astTomRule)
  {
    return (TomTerm) super.setArgument(_astTomRule, index_astTomRule);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomRule)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_TomRuleToTomTerm should have type TomRule");
        }
        break;
      default: throw new RuntimeException("TomTerm_TomRuleToTomTerm does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
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
