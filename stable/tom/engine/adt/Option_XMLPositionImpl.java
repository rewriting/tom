package jtom.adt;

abstract public class Option_XMLPositionImpl
extends Option
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected Option_XMLPositionImpl(TomSignatureFactory factory) {
    super(factory);
  }
  private static int index_place = 0;
  public shared.SharedObject duplicate() {
    Option_XMLPosition clone = new Option_XMLPosition(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof Option_XMLPosition) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTomSignatureFactory().makeOption_XMLPosition(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTomSignatureFactory().toTerm(this);
    }
    return term;
  }

  public boolean isXMLPosition()
  {
    return true;
  }

  public boolean hasPlace()
  {
    return true;
  }

  public String getPlace()
  {
   return ((aterm.ATermAppl) this.getArgument(index_place)).getAFun().getName();
  }

  public Option setPlace(String _place)
  {
    return (Option) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_place, 0, true)), index_place);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a Option_XMLPosition should have type str");
        }
        break;
      default: throw new RuntimeException("Option_XMLPosition does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
