package jtom.adt;

abstract public class TomTerm_BuildTermImpl
extends TomTerm
{
  TomTerm_BuildTermImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_astName = 0;
  private static int index_args = 1;
  public shared.SharedObject duplicate() {
    TomTerm_BuildTerm clone = new TomTerm_BuildTerm(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomTerm_BuildTerm) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_BuildTerm(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isBuildTerm()
  {
    return true;
  }

  public boolean hasAstName()
  {
    return true;
  }

  public boolean hasArgs()
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

  public TomList getArgs()
  {
    return (TomList) this.getArgument(index_args) ;
  }

  public TomTerm setArgs(TomList _args)
  {
    return (TomTerm) super.setArgument(_args, index_args);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TomName)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_BuildTerm should have type TomName");
        }
        break;
      case 1:
        if (! (arg instanceof TomList)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_BuildTerm should have type TomList");
        }
        break;
      default: throw new RuntimeException("TomTerm_BuildTerm does not have an argument at " + i );
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
