package jtom.adt;

abstract public class Expression_TomTermToExpressionImpl
extends Expression
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_astTerm = 0;
  public shared.SharedObject duplicate() {
    Expression_TomTermToExpression clone = new Expression_TomTermToExpression();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeExpression_TomTermToExpression(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("TomTermToExpression(<term>)");
  }

  static public Expression fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      Expression tmp = getStaticTomSignatureFactory().makeExpression_TomTermToExpression(TomTerm.fromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  public boolean isTomTermToExpression()
  {
    return true;
  }

  public boolean hasAstTerm()
  {
    return true;
  }

  public TomTerm getAstTerm()
  {
    return (TomTerm) this.getArgument(index_astTerm) ;
  }

  public Expression setAstTerm(TomTerm _astTerm)
  {
    return (Expression) super.setArgument(_astTerm, index_astTerm);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 0 of a Expression_TomTermToExpression should have type TomTerm");
        }
        break;
      default: throw new RuntimeException("Expression_TomTermToExpression does not have an argument at " + i );
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
