package jtom.adt;

abstract public class Declaration_TypeListDeclImpl
extends Declaration
{
  Declaration_TypeListDeclImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_astName = 0;
  private static int index_keywordList = 1;
  private static int index_orgTrack = 2;
  public shared.SharedObject duplicate() {
    Declaration_TypeListDecl clone = new Declaration_TypeListDecl(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof Declaration_TypeListDecl) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeDeclaration_TypeListDecl(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isTypeListDecl()
  {
    return true;
  }

  public boolean hasAstName()
  {
    return true;
  }

  public boolean hasKeywordList()
  {
    return true;
  }

  public boolean hasOrgTrack()
  {
    return true;
  }

  public TomName getAstName()
  {
    return (TomName) this.getArgument(index_astName) ;
  }

  public Declaration setAstName(TomName _astName)
  {
    return (Declaration) super.setArgument(_astName, index_astName);
  }

  public TomList getKeywordList()
  {
    return (TomList) this.getArgument(index_keywordList) ;
  }

  public Declaration setKeywordList(TomList _keywordList)
  {
    return (Declaration) super.setArgument(_keywordList, index_keywordList);
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
        if (! (arg instanceof TomName)) { 
          throw new RuntimeException("Argument 0 of a Declaration_TypeListDecl should have type TomName");
        }
        break;
      case 1:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 1 of a Declaration_TypeListDecl should have type TomList");
        }
        break;
      case 2:
        if (! (arg instanceof Option)) { 
          throw new RuntimeException("Argument 2 of a Declaration_TypeListDecl should have type Option");
        }
        break;
      default: throw new RuntimeException("Declaration_TypeListDecl does not have an argument at " + i );
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
