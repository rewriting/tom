package jtom.adt;

abstract public class TomNumber_NumberImpl
extends TomNumber
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TomNumber_NumberImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_integer = 0;
  public shared.SharedObject duplicate() {
    TomNumber_Number clone = new TomNumber_Number(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomNumber_Number) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomNumber_Number(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isNumber()
  {
    return true;
  }

  public boolean hasInteger()
  {
    return true;
  }

  public Integer getInteger()
  {
   return new Integer(((aterm.ATermInt) this.getArgument(index_integer)).getInt());
  }

  public TomNumber setInteger(Integer _integer)
  {
    return (TomNumber) super.setArgument(getFactory().makeInt(_integer.intValue()), index_integer);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermInt)) { 
          throw new RuntimeException("Argument 0 of a TomNumber_Number should have type int");
        }
        break;
      default: throw new RuntimeException("TomNumber_Number does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
