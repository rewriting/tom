package jtom.adt;

abstract public class Declaration_GetSizeDeclImpl
extends Declaration
{
  Declaration_GetSizeDeclImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_kid1 = 0;
  private static int index_tlCode = 1;
  private static int index_orgTrack = 2;
  public shared.SharedObject duplicate() {
    Declaration_GetSizeDecl clone = new Declaration_GetSizeDecl(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof Declaration_GetSizeDecl) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeDeclaration_GetSizeDecl(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isGetSizeDecl()
  {
    return true;
  }

  public boolean hasKid1()
  {
    return true;
  }

  public boolean hasTlCode()
  {
    return true;
  }

  public boolean hasOrgTrack()
  {
    return true;
  }

  public TomTerm getKid1()
  {
    return (TomTerm) this.getArgument(index_kid1) ;
  }

  public Declaration setKid1(TomTerm _kid1)
  {
    return (Declaration) super.setArgument(_kid1, index_kid1);
  }

  public TargetLanguage getTlCode()
  {
    return (TargetLanguage) this.getArgument(index_tlCode) ;
  }

  public Declaration setTlCode(TargetLanguage _tlCode)
  {
    return (Declaration) super.setArgument(_tlCode, index_tlCode);
  }

  public Option getOrgTrack()
  {
    return (Option) this.getArgument(index_orgTrack) ;
  }

  public Declaration setOrgTrack(Option _orgTrack)
  {
    return (Declaration) super.setArgument(_orgTrack, index_orgTrack);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 0 of a Declaration_GetSizeDecl should have type TomTerm");
        }
        break;
      case 1:
        if (! (arg instanceof TargetLanguage)) { 
          throw new RuntimeException("Argument 1 of a Declaration_GetSizeDecl should have type TargetLanguage");
        }
        break;
      case 2:
        if (! (arg instanceof Option)) { 
          throw new RuntimeException("Argument 2 of a Declaration_GetSizeDecl should have type Option");
        }
        break;
      default: throw new RuntimeException("Declaration_GetSizeDecl does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
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
