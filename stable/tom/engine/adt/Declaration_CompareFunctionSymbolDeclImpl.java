package jtom.adt;

abstract public class Declaration_CompareFunctionSymbolDeclImpl
extends Declaration
{
  Declaration_CompareFunctionSymbolDeclImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_symbolArg1 = 0;
  private static int index_symbolArg2 = 1;
  private static int index_tlCode = 2;
  private static int index_orgTrack = 3;
  public shared.SharedObject duplicate() {
    Declaration_CompareFunctionSymbolDecl clone = new Declaration_CompareFunctionSymbolDecl(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof Declaration_CompareFunctionSymbolDecl) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeDeclaration_CompareFunctionSymbolDecl(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isCompareFunctionSymbolDecl()
  {
    return true;
  }

  public boolean hasSymbolArg1()
  {
    return true;
  }

  public boolean hasSymbolArg2()
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

  public TomTerm getSymbolArg1()
  {
    return (TomTerm) this.getArgument(index_symbolArg1) ;
  }

  public Declaration setSymbolArg1(TomTerm _symbolArg1)
  {
    return (Declaration) super.setArgument(_symbolArg1, index_symbolArg1);
  }

  public TomTerm getSymbolArg2()
  {
    return (TomTerm) this.getArgument(index_symbolArg2) ;
  }

  public Declaration setSymbolArg2(TomTerm _symbolArg2)
  {
    return (Declaration) super.setArgument(_symbolArg2, index_symbolArg2);
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
          throw new RuntimeException("Argument 0 of a Declaration_CompareFunctionSymbolDecl should have type TomTerm");
        }
        break;
      case 1:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 1 of a Declaration_CompareFunctionSymbolDecl should have type TomTerm");
        }
        break;
      case 2:
        if (! (arg instanceof TargetLanguage)) { 
          throw new RuntimeException("Argument 2 of a Declaration_CompareFunctionSymbolDecl should have type TargetLanguage");
        }
        break;
      case 3:
        if (! (arg instanceof Option)) { 
          throw new RuntimeException("Argument 3 of a Declaration_CompareFunctionSymbolDecl should have type Option");
        }
        break;
      default: throw new RuntimeException("Declaration_CompareFunctionSymbolDecl does not have an argument at " + i );
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
