package jtom.adt;

abstract public class TomType_TypesToTypeImpl
extends TomType
{
  TomType_TypesToTypeImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_domain = 0;
  private static int index_codomain = 1;
  public shared.SharedObject duplicate() {
    TomType_TypesToType clone = new TomType_TypesToType(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomType_TypesToType) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomType_TypesToType(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isTypesToType()
  {
    return true;
  }

  public boolean hasDomain()
  {
    return true;
  }

  public boolean hasCodomain()
  {
    return true;
  }

  public TomTypeList getDomain()
  {
    return (TomTypeList) this.getArgument(index_domain) ;
  }

  public TomType setDomain(TomTypeList _domain)
  {
    return (TomType) super.setArgument(_domain, index_domain);
  }

  public TomType getCodomain()
  {
    return (TomType) this.getArgument(index_codomain) ;
  }

  public TomType setCodomain(TomType _codomain)
  {
    return (TomType) super.setArgument(_codomain, index_codomain);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomTypeList)) { 
          throw new RuntimeException("Argument 0 of a TomType_TypesToType should have type TomTypeList");
        }
        break;
      case 1:
        if (! (arg instanceof TomType)) { 
          throw new RuntimeException("Argument 1 of a TomType_TypesToType should have type TomType");
        }
        break;
      default: throw new RuntimeException("TomType_TypesToType does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = getArgument(1).hashCode() + (getAnnotations().hashCode()<<8);
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
