package jtom.adt;

abstract public class TomTerm_MakeFunctionBeginImpl
extends TomTerm
{
  TomTerm_MakeFunctionBeginImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_astName = 0;
  private static int index_subjectListAST = 1;
  public shared.SharedObject duplicate() {
    TomTerm_MakeFunctionBegin clone = new TomTerm_MakeFunctionBegin(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomTerm_MakeFunctionBegin) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_MakeFunctionBegin(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isMakeFunctionBegin()
  {
    return true;
  }

  public boolean hasAstName()
  {
    return true;
  }

  public boolean hasSubjectListAST()
  {
    return true;
  }

  public TomName getAstName()
  {
    return (TomName) this.getArgument(index_astName) ;
  }

  public TomTerm setAstName(TomName _astName)
  {
    return (TomTerm) super.setArgument(_astName, index_astName);
  }

  public TomTerm getSubjectListAST()
  {
    return (TomTerm) this.getArgument(index_subjectListAST) ;
  }

  public TomTerm setSubjectListAST(TomTerm _subjectListAST)
  {
    return (TomTerm) super.setArgument(_subjectListAST, index_subjectListAST);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomName)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_MakeFunctionBegin should have type TomName");
        }
        break;
      case 1:
        if (! (arg instanceof TomTerm)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_MakeFunctionBegin should have type TomTerm");
        }
        break;
      default: throw new RuntimeException("TomTerm_MakeFunctionBegin does not have an argument at " + i );
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
