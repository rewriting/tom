package jtom.adt;

abstract public class TomTerm_UnamedVariableImpl
extends TomTerm
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TomTerm_UnamedVariableImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_option = 0;
  private static int index_astType = 1;
  public shared.SharedObject duplicate() {
    TomTerm_UnamedVariable clone = new TomTerm_UnamedVariable(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomTerm_UnamedVariable) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_UnamedVariable(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isUnamedVariable()
  {
    return true;
  }

  public boolean hasOption()
  {
    return true;
  }

  public boolean hasAstType()
  {
    return true;
  }

  public Option getOption()
  {
    return (Option) this.getArgument(index_option) ;
  }

  public TomTerm setOption(Option _option)
  {
    return (TomTerm) super.setArgument(_option, index_option);
  }

  public TomType getAstType()
  {
    return (TomType) this.getArgument(index_astType) ;
  }

  public TomTerm setAstType(TomType _astType)
  {
    return (TomTerm) super.setArgument(_astType, index_astType);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof Option)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_UnamedVariable should have type Option");
        }
        break;
      case 1:
        if (! (arg instanceof TomType)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_UnamedVariable should have type TomType");
        }
        break;
      default: throw new RuntimeException("TomTerm_UnamedVariable does not have an argument at " + i );
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
