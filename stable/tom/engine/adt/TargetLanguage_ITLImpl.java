package jtom.adt;

abstract public class TargetLanguage_ITLImpl
extends TargetLanguage
{
  TargetLanguage_ITLImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_code = 0;
  public shared.SharedObject duplicate() {
    TargetLanguage_ITL clone = new TargetLanguage_ITL(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TargetLanguage_ITL) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTargetLanguage_ITL(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isITL()
  {
    return true;
  }

  public boolean hasCode()
  {
    return true;
  }

  public String getCode()
  {
   return ((aterm.ATermAppl) this.getArgument(index_code)).getAFun().getName();
  }

  public TargetLanguage setCode(String _code)
  {
    return (TargetLanguage) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_code, 0, true)), index_code);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TargetLanguage_ITL should have type str");
        }
        break;
      default: throw new RuntimeException("TargetLanguage_ITL does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
