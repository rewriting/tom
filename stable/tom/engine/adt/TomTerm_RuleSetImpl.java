package jtom.adt;

abstract public class TomTerm_RuleSetImpl
extends TomTerm
{
  TomTerm_RuleSetImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_ruleList = 0;
  private static int index_orgTrack = 1;
  public shared.SharedObject duplicate() {
    TomTerm_RuleSet clone = new TomTerm_RuleSet(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomTerm_RuleSet) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_RuleSet(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isRuleSet()
  {
    return true;
  }

  public boolean hasRuleList()
  {
    return true;
  }

  public boolean hasOrgTrack()
  {
    return true;
  }

  public TomRuleList getRuleList()
  {
    return (TomRuleList) this.getArgument(index_ruleList) ;
  }

  public TomTerm setRuleList(TomRuleList _ruleList)
  {
    return (TomTerm) super.setArgument(_ruleList, index_ruleList);
  }

  public Option getOrgTrack()
  {
    return (Option) this.getArgument(index_orgTrack) ;
  }

  public TomTerm setOrgTrack(Option _orgTrack)
  {
    return (TomTerm) super.setArgument(_orgTrack, index_orgTrack);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomRuleList)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_RuleSet should have type TomRuleList");
        }
        break;
      case 1:
        if (! (arg instanceof Option)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_RuleSet should have type Option");
        }
        break;
      default: throw new RuntimeException("TomTerm_RuleSet does not have an argument at " + i );
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
