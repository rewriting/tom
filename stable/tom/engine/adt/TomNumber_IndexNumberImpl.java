package jtom.adt;

abstract public class TomNumber_IndexNumberImpl
extends TomNumber
{
  TomNumber_IndexNumberImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_number = 0;
  public shared.SharedObject duplicate() {
    TomNumber_IndexNumber clone = new TomNumber_IndexNumber(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomNumber_IndexNumber) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomNumber_IndexNumber(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isIndexNumber()
  {
    return true;
  }

  public boolean hasNumber()
  {
    return true;
  }

  public TomNumber getNumber()
  {
    return (TomNumber) this.getArgument(index_number) ;
  }

  public TomNumber setNumber(TomNumber _number)
  {
    return (TomNumber) super.setArgument(_number, index_number);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomNumber)) { 
          throw new RuntimeException("Argument 0 of a TomNumber_IndexNumber should have type TomNumber");
        }
        break;
      default: throw new RuntimeException("TomNumber_IndexNumber does not have an argument at " + i );
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
