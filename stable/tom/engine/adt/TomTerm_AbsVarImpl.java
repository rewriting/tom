package jtom.adt;

abstract public class TomTerm_AbsVarImpl
extends TomTerm
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_number = 0;
  public shared.SharedObject duplicate() {
    TomTerm_AbsVar clone = new TomTerm_AbsVar();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_AbsVar(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("AbsVar(<term>)");
  }

  static public TomTerm fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      TomTerm tmp = getStaticTomSignatureFactory().makeTomTerm_AbsVar(TomTerm.fromTerm( (aterm.ATerm) children.get(0)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  public boolean isAbsVar()
  {
    return true;
  }

  public boolean hasNumber()
  {
    return true;
  }

  public TomTerm getNumber()
  {
    return (TomTerm) this.getArgument(index_number) ;
  }

  public TomTerm setNumber(TomTerm _number)
  {
    return (TomTerm) super.setArgument(_number, index_number);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_AbsVar should have type TomTerm");
        }
        break;
      default: throw new RuntimeException("TomTerm_AbsVar does not have an argument at " + i );
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
