package jtom.adt;

abstract public class TomTerm_ExpressionToTomTermImpl
extends TomTerm
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TomTerm_ExpressionToTomTermImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_astExpression = 0;
  public shared.SharedObject duplicate() {
    TomTerm_ExpressionToTomTerm clone = new TomTerm_ExpressionToTomTerm(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomTerm_ExpressionToTomTerm) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_ExpressionToTomTerm(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isExpressionToTomTerm()
  {
    return true;
  }

  public boolean hasAstExpression()
  {
    return true;
  }

  public Expression getAstExpression()
  {
    return (Expression) this.getArgument(index_astExpression) ;
  }

  public TomTerm setAstExpression(Expression _astExpression)
  {
    return (TomTerm) super.setArgument(_astExpression, index_astExpression);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof Expression)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_ExpressionToTomTerm should have type Expression");
        }
        break;
      default: throw new RuntimeException("TomTerm_ExpressionToTomTerm does not have an argument at " + i );
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
