package jtom.adt;

abstract public class Declaration_MakeAddListImpl
extends Declaration
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected Declaration_MakeAddListImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_astName = 0;
  private static int index_varElt = 1;
  private static int index_varList = 2;
  private static int index_tlCode = 3;
  private static int index_orgTrack = 4;
  public shared.SharedObject duplicate() {
    Declaration_MakeAddList clone = new Declaration_MakeAddList(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof Declaration_MakeAddList) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeDeclaration_MakeAddList(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isMakeAddList()
  {
    return true;
  }

  public boolean hasAstName()
  {
    return true;
  }

  public boolean hasVarElt()
  {
    return true;
  }

  public boolean hasVarList()
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

  public TomName getAstName()
  {
    return (TomName) this.getArgument(index_astName) ;
  }

  public Declaration setAstName(TomName _astName)
  {
    return (Declaration) super.setArgument(_astName, index_astName);
  }

  public TomTerm getVarElt()
  {
    return (TomTerm) this.getArgument(index_varElt) ;
  }

  public Declaration setVarElt(TomTerm _varElt)
  {
    return (Declaration) super.setArgument(_varElt, index_varElt);
  }

  public TomTerm getVarList()
  {
    return (TomTerm) this.getArgument(index_varList) ;
  }

  public Declaration setVarList(TomTerm _varList)
  {
    return (Declaration) super.setArgument(_varList, index_varList);
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
        if (! (arg instanceof TomName)) { 
          throw new RuntimeException("Argument 0 of a Declaration_MakeAddList should have type TomName");
        }
        break;
      case 1:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 1 of a Declaration_MakeAddList should have type TomTerm");
        }
        break;
      case 2:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 2 of a Declaration_MakeAddList should have type TomTerm");
        }
        break;
      case 3:
        if (! (arg instanceof TargetLanguage)) { 
          throw new RuntimeException("Argument 3 of a Declaration_MakeAddList should have type TargetLanguage");
        }
        break;
      case 4:
        if (! (arg instanceof Option)) { 
          throw new RuntimeException("Argument 4 of a Declaration_MakeAddList should have type Option");
        }
        break;
      default: throw new RuntimeException("Declaration_MakeAddList does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
    b += (getArgument(4).hashCode() << 0);
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
