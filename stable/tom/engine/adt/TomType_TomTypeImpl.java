package jtom.adt;

abstract public class TomType_TomTypeImpl
extends TomType
{
  TomType_TomTypeImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_string = 0;
  public shared.SharedObject duplicate() {
    TomType_TomType clone = new TomType_TomType(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TomType_TomType) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeTomType_TomType(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isTomType()
  {
    return true;
  }

  public boolean hasString()
  {
    return true;
  }

  public String getString()
  {
   return ((aterm.ATermAppl) this.getArgument(index_string)).getAFun().getName();
  }

  public TomType setString(String _string)
  {
    return (TomType) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_string, 0, true)), index_string);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TomType_TomType should have type str");
        }
        break;
      default: throw new RuntimeException("TomType_TomType does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
