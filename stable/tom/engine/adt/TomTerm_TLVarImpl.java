package jtom.adt;

abstract public class TomTerm_TLVarImpl
extends TomTerm
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TomTerm_TLVarImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_strName = 0;
  private static int index_astType = 1;
  public shared.SharedObject duplicate() {
    TomTerm_TLVar clone = new TomTerm_TLVar(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomTerm_TLVar) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomTerm_TLVar(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isTLVar()
  {
    return true;
  }

  public boolean hasStrName()
  {
    return true;
  }

  public boolean hasAstType()
  {
    return true;
  }

  public String getStrName()
  {
   return ((aterm.ATermAppl) this.getArgument(index_strName)).getAFun().getName();
  }

  public TomTerm setStrName(String _strName)
  {
    return (TomTerm) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_strName, 0, true)), index_strName);
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
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TomTerm_TLVar should have type str");
        }
        break;
      case 1:
        if (! (arg instanceof TomType)) { 
          throw new RuntimeException("Argument 1 of a TomTerm_TLVar should have type TomType");
        }
        break;
      default: throw new RuntimeException("TomTerm_TLVar does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
