package jtom.adt;

abstract public class TomRule_RewriteRuleImpl
extends TomRule
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TomRule_RewriteRuleImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_lhs = 0;
  private static int index_rhs = 1;
  private static int index_condList = 2;
  private static int index_option = 3;
  public shared.SharedObject duplicate() {
    TomRule_RewriteRule clone = new TomRule_RewriteRule(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomRule_RewriteRule) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomRule_RewriteRule(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isRewriteRule()
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

  public boolean hasCondList()
  {
    return true;
  }

  public boolean hasOption()
  {
    return true;
  }

  public TomTerm getLhs()
  {
    return (TomTerm) this.getArgument(index_lhs) ;
  }

  public TomRule setLhs(TomTerm _lhs)
  {
    return (TomRule) super.setArgument(_lhs, index_lhs);
  }

  public TomTerm getRhs()
  {
    return (TomTerm) this.getArgument(index_rhs) ;
  }

  public TomRule setRhs(TomTerm _rhs)
  {
    return (TomRule) super.setArgument(_rhs, index_rhs);
  }

  public TomList getCondList()
  {
    return (TomList) this.getArgument(index_condList) ;
  }

  public TomRule setCondList(TomList _condList)
  {
    return (TomRule) super.setArgument(_condList, index_condList);
  }

  public Option getOption()
  {
    return (Option) this.getArgument(index_option) ;
  }

  public TomRule setOption(Option _option)
  {
    return (TomRule) super.setArgument(_option, index_option);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 0 of a TomRule_RewriteRule should have type TomTerm");
        }
        break;
      case 1:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 1 of a TomRule_RewriteRule should have type TomTerm");
        }
        break;
      case 2:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 2 of a TomRule_RewriteRule should have type TomList");
        }
        break;
      case 3:
        if (! (arg instanceof Option)) { 
          throw new RuntimeException("Argument 3 of a TomRule_RewriteRule should have type Option");
        }
        break;
      default: throw new RuntimeException("TomRule_RewriteRule does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
    a += (getArgument(3).hashCode() << 24);
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
