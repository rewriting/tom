package jtom.adt;

abstract public class Expression_AndImpl
extends Expression
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_arg1 = 0;
  private static int index_arg2 = 1;
  public shared.SharedObject duplicate() {
    Expression_And clone = new Expression_And();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeExpression_And(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("And(<term>,<term>)");
  }

  static public Expression fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      Expression tmp = getStaticTomSignatureFactory().makeExpression_And(Expression.fromTerm( (aterm.ATerm) children.get(0)), Expression.fromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  public boolean isAnd()
  {
    return true;
  }

  public boolean hasArg1()
  {
    return true;
  }

  public boolean hasArg2()
  {
    return true;
  }

  public Expression getArg1()
  {
    return (Expression) this.getArgument(index_arg1) ;
  }

  public Expression setArg1(Expression _arg1)
  {
    return (Expression) super.setArgument(_arg1, index_arg1);
  }

  public Expression getArg2()
  {
    return (Expression) this.getArgument(index_arg2) ;
  }

  public Expression setArg2(Expression _arg2)
  {
    return (Expression) super.setArgument(_arg2, index_arg2);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof Expression)) { 
          throw new RuntimeException("Argument 0 of a Expression_And should have type Expression");
        }
        break;
      case 1:
        if (! (arg instanceof Expression)) { 
          throw new RuntimeException("Argument 1 of a Expression_And should have type Expression");
        }
        break;
      default: throw new RuntimeException("Expression_And does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
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
